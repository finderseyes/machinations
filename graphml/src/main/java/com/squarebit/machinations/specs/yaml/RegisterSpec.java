package com.squarebit.machinations.specs.yaml;

/**
 * The register spec.
 */
public class RegisterSpec extends NodeSpec {
    private String value; // value expression.

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     * @return the value
     */
    public RegisterSpec setValue(String value) {
        this.value = value;
        return this;
    }
}
