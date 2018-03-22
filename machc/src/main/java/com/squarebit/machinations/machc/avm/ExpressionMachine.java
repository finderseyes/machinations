package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.expressions.*;
import com.squarebit.machinations.machc.avm.runtime.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * In charge of expression evaluation.
 */
final class ExpressionMachine {
    private final Machine machine;

    /**
     * Instantiates a new Expression machine.
     *
     * @param machine the machine
     */
    public ExpressionMachine(Machine machine) {
        this.machine = machine;
    }

    /**
     * Evaluates an expression.
     *
     * @param expression the expression
     * @return the expression
     */
    public CompletableFuture<TObject> evaluate(Expression expression) {
        if (expression instanceof Constant)
            return evaluateConstant((Constant)expression);
        else if (expression instanceof SetDescriptor) {
            return evaluateSetDescriptor((SetDescriptor)expression).thenApply(v -> v);
        }
        else if (expression instanceof Set) {
            return evaluateSet((Set)expression).thenApply(v -> v);
        }
        else if (expression instanceof Variable) {
            Variable variable = (Variable)expression;
            return CompletableFuture.completedFuture(machine.getLocalVariable(variable.getVariableInfo().getIndex()));
        }
        else if (expression instanceof Add) {
            return evaluateAdd((Add)expression);
        }
        else
            throw new RuntimeException("Shall not reach here");
    }

    /**
     * Evaluate constant t object.
     *
     * @param constant the constant
     * @return the t object
     */
    private CompletableFuture<TObject> evaluateConstant(Constant constant) {
        TObject value = constant.getValue();

        if (value instanceof TRandomDice)
            value = ((TRandomDice)value).generate();

        return CompletableFuture.completedFuture(value);
    }

    private CompletableFuture<TSet> evaluateSet(Set set) {
        return evaluateSetDescriptor(set.getDescriptor()).thenCompose(this::instantiateSet);
    }

    private CompletableFuture<TSet> instantiateSet(TSetDescriptor descriptor) {
        CompletableFuture<TSet> returnFuture = new CompletableFuture<>();

        try {
            TSet result = new TSet();
            MachineInvocationPlan machineInvocationPlan = null;

            for (TSetElementTypeDescriptor typeDescriptor: descriptor.getElementTypeDescriptors()) {
                TypeInfo graphElementType = this.machine.findType(typeDescriptor.getName());

                final TypeInfo elementType = graphElementType != null ? graphElementType : CoreModule.NAMED_RESOURCE;

                for (int i = 0; i < typeDescriptor.getSize(); i++) {
                    TObject instance = elementType.allocateInstance();

                    if (elementType == CoreModule.NAMED_RESOURCE) {
                        ((TNamedResource)instance).setTypeName(typeDescriptor.getName());
                    }
                    else {
                        if (machineInvocationPlan == null)
                            machineInvocationPlan = new MachineInvocationPlan(
                                    elementType.getInternalInstanceConstructor(), instance);
                        else
                            machineInvocationPlan.thenInvoke(v ->
                                    new NativeToMachineInvocation(elementType.getInternalInstanceConstructor(), instance)
                            );
                    }

                    result.add((TSetElement)instance);
                }

            }

            if (machineInvocationPlan != null) {
                machine.machineInvoke(machineInvocationPlan).whenComplete((v, ex) -> {
                    if (ex != null)
                        returnFuture.completeExceptionally(ex);
                    else
                        returnFuture.complete(result);
                });
            }
            else {
                returnFuture.complete(result);
            }
        }
        catch (Exception ex) {
            returnFuture.completeExceptionally(ex);
        }

        return returnFuture;
    }

    private CompletableFuture<TSetDescriptor> evaluateSetDescriptor(SetDescriptor setDescriptor) {
        List<TSetElementTypeDescriptor> typeDescriptors = new ArrayList<>();
        CompletableFuture<TSetDescriptor> returnFuture = new CompletableFuture<>();

        CompletableFuture[] returnFutures = setDescriptor.getElementDescriptors().stream().map(descriptor ->
            evaluate(descriptor.getSize())
                    .thenCompose(size ->
                            evaluateOrDefault(descriptor.getCapacity(), new TInteger(-1))
                                    .thenCompose(capacity ->
                                            evaluateOrDefault(descriptor.getName(), TString.EMPTY)
                                                    .thenApply(name ->
                                                            new TSetElementTypeDescriptor(
                                                                    evaluateAsInteger(size).getValue(),
                                                                    evaluateAsInteger(capacity).getValue(),
                                                                    evaluateAsString(name).getValue()
                                                            )
                                                    )
                                    )
                    ).thenAccept(typeDescriptors::add)
        ).toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(returnFutures).whenComplete((v, ex) -> {
            if (ex != null)
                returnFuture.completeExceptionally(ex);
            else
                returnFuture.complete(new TSetDescriptor(typeDescriptors));
        });

        return returnFuture;
    }

    private CompletableFuture<TObject> evaluateOrDefault(Expression expression, TObject defaultValue)
    {
        if (expression == null)
            return CompletableFuture.completedFuture(defaultValue);
        else
            return evaluate(expression);
    }

    private CompletableFuture<TObject> evaluateAdd(Add add) {
        return evaluate(add.getFirst()).thenCompose(first ->
            evaluate(add.getSecond()).thenApply(second -> {
                TypeInfo typeInfo = coerceType(first.getTypeInfo(), second.getTypeInfo());

                if (typeInfo == CoreModule.INTEGER_TYPE) {
                    TInteger firstInteger = evaluateAsInteger(first);
                    TInteger secondInteger = evaluateAsInteger(second);
                    return new TInteger(firstInteger.getValue() + secondInteger.getValue());
                }
                else if (typeInfo == CoreModule.NAN_TYPE)
                    return TNaN.INSTANCE;
                else
                    return TNaN.INSTANCE;
            })
        );
//        CompletableFuture<TObject> first = evaluate(add.getFirst());
//        CompletableFuture<TObject> second = evaluate(add.getSecond());
//
//        TypeInfo typeInfo = coerceType(first.getTypeInfo(), second.getTypeInfo());
//
//        if (typeInfo == CoreModule.INTEGER_TYPE) {
//            TInteger firstInteger = evaluateAsInteger(first);
//            TInteger secondInteger = evaluateAsInteger(second);
//            return new TInteger(firstInteger.getValue() + secondInteger.getValue());
//        }
//        else if (typeInfo == CoreModule.NAN_TYPE)
//            return TNaN.INSTANCE;
//        else
//            return TNaN.INSTANCE;
    }

    private TypeInfo coerceType(TypeInfo firstType, TypeInfo secondType) {
        if (firstType == CoreModule.STRING_TYPE || secondType == CoreModule.STRING_TYPE)
            return CoreModule.STRING_TYPE;
        else if (firstType == CoreModule.NAN_TYPE || secondType == CoreModule.NAN_TYPE)
            return CoreModule.NAN_TYPE;
        else if (firstType == CoreModule.FLOAT_TYPE || secondType == CoreModule.FLOAT_TYPE)
            return CoreModule.FLOAT_TYPE;
        else if (firstType == CoreModule.INTEGER_TYPE || secondType == CoreModule.INTEGER_TYPE)
            return CoreModule.INTEGER_TYPE;
        else
            throw new RuntimeException("Cannot coerce types");
    }

    private TInteger evaluateAsInteger(TObject value) {
        if (value instanceof TInteger)
            return (TInteger)value;
        else if (value instanceof TFloat)
            return new TInteger((int)((TFloat)value).getValue());
        else
            throw new RuntimeException("Cannot convert to integer");
    }

    private TString evaluateAsString(TObject value) {
        if (value instanceof TString)
            return (TString) value;
        return new TString(value.toString());
    }
}
