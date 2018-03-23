package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.expressions.*;
import com.squarebit.machinations.machc.avm.runtime.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * In charge of expression evaluation.
 */
public final class ExpressionMachine {
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
     * Evaluate an expression, or return default value if the expression is null.
     *
     * @param expression   the expression
     * @param defaultValue the default value if the expression is null.
     * @return the completable future
     */
    public CompletableFuture<TObject> evaluateOrDefault(Expression expression, TObject defaultValue)
    {
        if (expression == null)
            return CompletableFuture.completedFuture(defaultValue);
        else
            return evaluate(expression);
    }

    /**
     * Evaluate constant t object.
     *
     * @param constant the constant
     * @return the t object
     */
    public CompletableFuture<TObject> evaluateConstant(Constant constant) {
        TObject value = constant.getValue();

        if (value instanceof TRandomDice)
            value = ((TRandomDice)value).generate();

        return CompletableFuture.completedFuture(value);
    }

    /**
     * Evaluates a set.
     * @param set
     * @return
     */
    public CompletableFuture<TSet> evaluateSet(Set set) {
        return evaluateSetDescriptor(set.getDescriptor()).thenCompose(this::instantiateSet);
    }

    /**
     * Instantiate set completable future.
     *
     * @param descriptor the descriptor
     * @return the completable future
     */
    public CompletableFuture<TSet> instantiateSet(TSetDescriptor descriptor) {
        CompletableFuture<TSet> returnFuture = new CompletableFuture<>();

        instantiateSetDescriptor(descriptor).thenAccept(instantiated -> {
            try {
                TSet result = new TSet();
                MachineInvocationPlan machineInvocationPlan = null;

                for (TSetElementTypeDescriptor typeDescriptor: instantiated.getElementTypeDescriptors()) {
                    TypeInfo graphElementType = this.machine.findType(typeDescriptor.getName());

                    final TypeInfo elementType = graphElementType != null ? graphElementType : CoreModule.NAMED_RESOURCE;

                    for (int i = 0; i < typeDescriptor.getInstantiatedSize(); i++) {
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
        });

        return returnFuture;
    }

    public CompletableFuture<TSetDescriptor> instantiateSetDescriptor(TSetDescriptor descriptor) {
        CompletableFuture<TSetDescriptor> returnFuture = new CompletableFuture<>();
        TSetDescriptor instantiated = new TSetDescriptor();

        CompletableFuture.allOf(descriptor.getElementTypeDescriptors().stream()
                .map(d -> this.instantiateSetElementTypeDescriptor(d).thenAccept(instantiated::add))
                .toArray(CompletableFuture[]::new)
        ).whenComplete((value, exception) -> {
            if (exception != null)
                returnFuture.completeExceptionally(exception);
            else {
                returnFuture.complete(instantiated);
            }
        });

        return returnFuture;
    }

    public CompletableFuture<TSetElementTypeDescriptor> instantiateSetElementTypeDescriptor(TSetElementTypeDescriptor descriptor)
    {
        CompletableFuture<TSetElementTypeDescriptor> returnFuture = new CompletableFuture<>();

        if (descriptor.getSize() == null) {
            returnFuture.complete(TSetElementTypeDescriptor.instantiate(
                    1, descriptor.getCapacity(), descriptor.getName()
            ));
        }
        else {
            MachineInvocationPlan machineInvocationPlan = new MachineInvocationPlan(
                    descriptor.getSize().getLambdaTypeInfo().getLambdaMethod(),
                    descriptor.getSize()
            );

            machine.machineInvoke(machineInvocationPlan)
                    .whenComplete((result, exception) -> {
                        if (exception != null)
                            returnFuture.completeExceptionally(exception);
                        else
                            returnFuture.complete(TSetElementTypeDescriptor.instantiate(
                                    evaluateAsInteger(result).getValue(),
                                    descriptor.getCapacity(), descriptor.getName()
                            ));
                    });
        }

        return returnFuture;
    }

    /**
     * Evaluate set descriptor completable future.
     *
     * @param setDescriptor the set descriptor
     * @return the completable future
     */
    public CompletableFuture<TSetDescriptor> evaluateSetDescriptor(SetDescriptor setDescriptor) {
        List<TSetElementTypeDescriptor> typeDescriptors = new ArrayList<>();
        CompletableFuture<TSetDescriptor> returnFuture = new CompletableFuture<>();

        CompletableFuture[] returnFutures = setDescriptor.getElementDescriptors().stream()
                .map(descriptor ->
                        this.evaluateSetElementTypeDescriptor(descriptor)
                                .thenAccept(typeDescriptors::add)
                )
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(returnFutures).whenComplete((v, ex) -> {
            if (ex != null)
                returnFuture.completeExceptionally(ex);
            else
                returnFuture.complete(new TSetDescriptor(typeDescriptors));
        });

        return returnFuture;
    }

    public CompletableFuture<TSetElementTypeDescriptor> evaluateSetElementTypeDescriptor(SetElementDescriptor descriptor)
    {
        CompletableFuture<TSetElementTypeDescriptor> returnFuture = new CompletableFuture<>();

        try {
            TLambda size = (TLambda)machine.getLocalVariable(descriptor.getSize().getVariableInfo().getIndex());

            evaluateOrDefault(descriptor.getCapacity(), new TInteger(-1))
                    .thenCompose(capacity ->
                            evaluateOrDefault(descriptor.getName(), TString.EMPTY)
                                    .thenApply(name ->
                                            new TSetElementTypeDescriptor(
                                                    size,
                                                    evaluateAsInteger(capacity).getValue(),
                                                    evaluateAsString(name).getValue()
                                            )
                                    )
                    )
                    .whenComplete((result, exception) -> {
                        if (exception != null)
                            returnFuture.completeExceptionally(exception);
                        else
                            returnFuture.complete(result);
                    });
        }
        catch (Exception exception) {
            returnFuture.completeExceptionally(exception);
        }

        return returnFuture;
    }

    /**
     * Evaluate add completable future.
     *
     * @param add the add
     * @return the completable future
     */
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
