package com.squarebit.machinations.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public class ResourceContainer {
    public static String DEFAULT_RESOURCE_NAME = "";

    protected Map<String, Integer> content = new HashMap<>();
    protected Map<String, Integer> capacity = new HashMap<>();
    private int size = 0;

    /**
     * Gets total number of resources in this container.
     *
     * @return the container size.
     */
    public int size() {
        return size;
    }

    /**
     * Get the amount of resource with given name in the container.
     *
     * @param name the name
     * @return the int
     */
    public int get(String name) {
        return content.getOrDefault(name, 0);
    }

    /**
     * Gets capacity.
     *
     * @param name the name
     * @return the capacity
     */
    public int getCapacity(String name) {
        return capacity.getOrDefault(name, -1);
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return size <= 0;
    }

    /**
     * Adds a certain amount of resource with specified name.
     *
     * @param name  the name
     * @param amount the value
     * @return the size of given resource
     */
    public int add(final String name, final int amount) {
        checkArgument(amount > 0);

        size += amount;

        if (content.putIfAbsent(name, amount) != null)
            return content.computeIfPresent(name, (n, a) -> (a + amount));

        return amount;
    }

    /**
     * Adds a certain amount of default resource.
     *
     * @param amount the amount
     * @return the new default resource size
     */
    public int add(final int amount) {
        return this.add(DEFAULT_RESOURCE_NAME, amount);
    }

    /**
     *
     * @param amount
     * @return
     */
    public int add(ResourceContainer amount) {
        amount.content.forEach(this::add);
        return this.size;
    }

    /**
     * Remove int.
     *
     * @param name   the name
     * @param amount the amount
     * @return the int
     */
    public int remove(final String name, final int amount) {
        checkArgument(amount >= 0);

        int currentAmount = content.getOrDefault(name, 0);
        int removable = Math.min(currentAmount, amount);

        size -= amount;

        if (content.putIfAbsent(name, 0) != null)
            return content.computeIfPresent(name, (n, a) -> (a - removable));

        return 0;
    }

    /**
     * Remove int.
     *
     * @param amount the amount
     * @return the int
     */
    public int remove(final int amount) {
        return this.remove(DEFAULT_RESOURCE_NAME, amount);
    }

    /**
     *
     * @param amount
     * @return
     */
    public int remove(ResourceContainer amount) {
        amount.content.forEach(this::remove);
        return amount.size;
    }

    /**
     * Copy resource container.
     *
     * @return the resource container
     */
    public ResourceContainer copy() {
        ResourceContainer instance = new ResourceContainer();
        instance.content.putAll(this.content);
        instance.size = this.size;
        return instance;
    }

    /**
     *
     * @param name
     * @param amount
     * @param isAllOrNone
     * @return
     */
    public ResourceContainer pull(String name, int amount, boolean isAllOrNone) {
        ResourceContainer result = new ResourceContainer();

        // Consume any resource.
        if (name == null) {
            if (!isAllOrNone || this.size >= amount) {
                boolean done = false;
                while (!done) {
                    Optional<Map.Entry<String, Integer>> nonZero =
                            this.content.entrySet().stream().filter(e -> e.getValue() > 0).findFirst();

                    if (nonZero.isPresent()) {
                        Map.Entry<String, Integer> e = nonZero.get();
                        int delta = Math.min(amount, e.getValue());
                        this.remove(e.getKey(), delta);
                        result.add(e.getKey(), delta);
                        amount -= delta;
                    }

                    if (!isAllOrNone && size <= 0)
                        done = true;
                    else if (isAllOrNone)
                        throw new RuntimeException("FATAL! Should not reach here.");
                }
            }
        }
        else {
            int available = this.get(name);

            if (!isAllOrNone || available >= amount) {
                int delta = Math.min(amount, available);
                this.remove(name, delta);
            }
        }

        return result;
    }
}
