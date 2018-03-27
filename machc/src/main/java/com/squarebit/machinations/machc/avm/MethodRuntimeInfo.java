package com.squarebit.machinations.machc.avm;

/**
 * Method runtime info.
 */
public class MethodRuntimeInfo {
    private final MethodInfo methodInfo;
    private boolean isActive = true;

    /**
     * Instantiates a new Method runtime info.
     *
     * @param methodInfo the method info
     */
    public MethodRuntimeInfo(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
    }

    /**
     * Is active boolean.
     *
     * @return the boolean
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets active.
     *
     * @param active the active
     * @return the active
     */
    public MethodRuntimeInfo setActive(boolean active) {
        isActive = active;
        return this;
    }
}
