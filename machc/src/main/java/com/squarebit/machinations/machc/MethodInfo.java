package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.GMethod;

/**
 * Information about a method during compilation.
 */
final class MethodInfo extends SymbolInfo implements Scope {
    private TypeInfo type;
    private Block code;

    /**
     * Instantiates a new Method info.
     *
     * @param type the type
     */
    public MethodInfo(TypeInfo type) {
        this.type = type;
        this.code = new Block(this);
    }

    /**
     * Gets declaration.
     *
     * @return the declaration
     */
    public GMethod getDeclaration() {
        return (GMethod)super.getDeclaration();
    }

    /**
     * Sets declaration.
     *
     * @param declaration the declaration
     * @return the declaration
     */
    public MethodInfo setDeclaration(GMethod declaration) {
        super.setDeclaration(declaration);
        return this;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public Block getCode() {
        return code;
    }

    /**
     * Gets the parent scope.
     *
     * @return parent scope.
     */
    @Override
    public Scope getParent() {
        return type;
    }

    /**
     * Finds a local symbol with given name in this scope.
     *
     * @param name the symbol name
     * @return the symbol or null.
     */
    @Override
    public SymbolInfo findSymbol(String name) {
        return null;
    }
}
