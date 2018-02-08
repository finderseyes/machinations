package com.squarebit.machinations.modelsex;

public class Pool extends AbstractNode {
    private int initialSize;

    /**
     * Gets initial size.
     *
     * @return the initial size
     */
    public int getInitialSize() {
        return initialSize;
    }

    /**
     * Sets initial size.
     *
     * @param initialSize the initial size
     * @return the initial size
     */
    public Pool setInitialSize(int initialSize) {
        this.initialSize = initialSize;
        return this;
    }
}
