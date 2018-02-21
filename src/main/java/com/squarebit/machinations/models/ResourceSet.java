package com.squarebit.machinations.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

public class ResourceSet {
    /**
     * Empty set implementation.
     */
    private static class EmptySet extends ResourceSet {
        @Override
        public ResourceSet add(String name, int amount) {
            return this;
        }

        @Override
        public ResourceSet add(int amount) {
            return this;
        }

        @Override
        public ResourceSet add(ResourceSet amount) {
            return this;
        }

        @Override
        public ResourceSet remove(String name, int amount) {
            return this;
        }

        @Override
        public ResourceSet remove(int amount) {
            return this;
        }

        @Override
        public ResourceSet remove(ResourceSet subSet) {
            return this;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public int deltaSize() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int getCapacity(String name) {
            return 0;
        }

        @Override
        public ResourceSet copy() {
            return this;
        }
    }

    private static class InfiniteSet extends ResourceSet {
        @Override
        public ResourceSet add(String name, int amount) {
            return ResourceSet.of(name, amount);
        }

        @Override
        public ResourceSet add(int amount) {
            return ResourceSet.of(amount);
        }

        @Override
        public ResourceSet add(ResourceSet amount) {
            return amount;
        }

        @Override
        public ResourceSet remove(String name, int amount) {
            return ResourceSet.of(name, amount);
        }

        @Override
        public ResourceSet remove(int amount) {
            return ResourceSet.of(amount);
        }

        @Override
        public ResourceSet remove(ResourceSet subSet) {
            return subSet.copy();
        }

        @Override
        public int size() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int deltaSize() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int getCapacity(String name) {
            return Integer.MAX_VALUE;
        }

        @Override
        public ResourceSet copy() {
            return this;
        }
    }

    /**
     * The empty set.
     */
    private static final ResourceSet EMPTY_SET = new EmptySet();
    private static final ResourceSet INFINITE_SET = new InfiniteSet();

    /**
     * Default resource name.
     */
    public static String DEFAULT_RESOURCE_NAME = "";

    private Map<String, Integer> content = new HashMap<>();
    private Map<String, Integer> change = new HashMap<>();
    protected Map<String, Integer> capacity = new HashMap<>();

    private int contentSize = 0;
    private int changeSize = 0;

    /**
     * Gets total number of resources in this container.
     *
     * @return the container contentSize.
     */
    public int size() {
        return contentSize;
    }

    /**
     * Gets the change contentSize.
     * @return
     */
    public int deltaSize() {
        return this.changeSize;
    }

    /**
     *
     */
    public void commit() {
        this.changeSize = 0;
        this.change.clear();
    }

    /**
     *
     */
    public void discard() {
        this.change.forEach(this::doRemove);
        this.changeSize = 0;
        this.change.clear();
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
        return contentSize <= 0;
    }

    /**
     * Determines if the resource set is subset of another one.
     * @param superSet the super set to check.
     * @return
     */
    public boolean isSubSetOf(ResourceSet superSet) {
        return this.content.entrySet().stream()
                .allMatch(e -> superSet.get(e.getKey()) >= e.getValue());
    }

    /**
     * Adds a certain amount of resource with specified name.
     *
     * @param name  the name
     * @param amount the value
     * @return the contentSize of given resource
     */
    private int doAdd(String name, final int amount) {
        checkArgument(amount >= 0);

        name = (name == null) ? DEFAULT_RESOURCE_NAME : name;

        contentSize += amount;
        changeSize += amount;

        if (change.putIfAbsent(name, amount) != null)
            change.computeIfPresent(name, (n, a) -> (a + amount));

        if (content.putIfAbsent(name, amount) != null)
            content.computeIfPresent(name, (n, a) -> (a + amount));

        return amount;
    }

    /**
     * Adds a certain amount of resource with specified name.
     *
     * @param name  the name
     * @param amount the value
     * @return the contentSize of given resource
     */
    public ResourceSet add(String name, final int amount) {
        name = (name == null) ? DEFAULT_RESOURCE_NAME : name;
        int result = doAdd(name, amount);
        return ResourceSet.of(name, result);
    }

    /**
     * Adds a certain amount of default resource.
     *
     * @param amount the amount
     * @return the new default resource contentSize
     */
    public ResourceSet add(final int amount) {
        return this.add(DEFAULT_RESOURCE_NAME, amount);
    }

    /**
     *
     * @param amount
     * @return
     */
    public ResourceSet add(ResourceSet amount) {
        ResourceSet result = new ResourceSet();
        amount.content.forEach((n, a) -> {
            result.doAdd(n, this.doAdd(n, a));
        });
        result.commit();
        return result;
    }

    /**
     * Remove int.
     *
     * @param name   the name
     * @param amount the amount
     * @return the int
     */
    private int doRemove(String name, final int amount) {
        checkArgument(amount >= 0);
        name = (name == null) ? DEFAULT_RESOURCE_NAME : name;

        int currentAmount = content.getOrDefault(name, 0);
        int removable = Math.min(currentAmount, amount);

        contentSize -= amount;
        changeSize -= amount;

        if (change.putIfAbsent(name, -amount) != null)
            change.computeIfPresent(name, (n, a) -> (a - removable));

        if (content.putIfAbsent(name, 0) != null)
            content.computeIfPresent(name, (n, a) -> (a - removable));

        return removable;
    }

    /**
     * Removes given amount of resources, regardless of their names.
     * @param amount the amount to remove
     * @return removed resource set
     */
    private ResourceSet doRemoveAny(int amount) {
        ResourceSet result = new ResourceSet();
        boolean done = contentSize <= 0;
        while (!done) {
            Optional<Map.Entry<String, Integer>> nonZeroResource =
                    this.content.entrySet().stream()
                            .filter(e -> e.getValue() > 0).findFirst();

            if (nonZeroResource.isPresent()) {
                Map.Entry<String, Integer> e = nonZeroResource.get();
                int extractedAmount = Math.min(amount, e.getValue());
                this.doRemove(e.getKey(), extractedAmount);
                result.add(e.getKey(), extractedAmount);
                amount -= extractedAmount;
            }

            if (amount == 0 || contentSize <= 0)
                done = true;
        }

        result.commit();

        return result;
    }

    /**
     * Remove a certain amount of resources with given name.
     * @param name the resource name. if null or default resource name, remove any resources. Otherwise, remove
     *             the resource with exact name.
     * @param amount the amount to remove
     * @return the removed resource set
     */
    public ResourceSet remove(String name, int amount) {
        checkArgument(amount >= 0);
        name = (name == null) ? DEFAULT_RESOURCE_NAME : name;

        if (name.equals(DEFAULT_RESOURCE_NAME))
            return doRemoveAny(amount);
        else
            return ResourceSet.of(name, doRemove(name, amount));
    }

    /**
     * Removes any resources from this set, up to given amount.
     * @param amount amount
     * @return removed resource set
     */
    public ResourceSet remove(int amount) {
        return this.doRemoveAny(amount);
    }

    /**
     * Removes a resource set from this set.
     * @param subSet the subset to remove.
     * @return remove resource set.
     */
    public ResourceSet remove(ResourceSet subSet) {
        Set<String> namedResources = subSet.content.keySet().stream()
                .filter(n -> !n.equals(DEFAULT_RESOURCE_NAME))
                .collect(Collectors.toSet());

        ResourceSet result = new ResourceSet();
        namedResources.stream()
                .map(n -> remove(n, subSet.get(n)))
                .forEach(result::add);

        if (subSet.content.containsKey(DEFAULT_RESOURCE_NAME)) {
            result.add(this.doRemoveAny(subSet.content.get(DEFAULT_RESOURCE_NAME)));
        }

        result.commit();

        return result;
    }

    /**
     * Copy resource container.
     *
     * @return the resource container
     */
    public ResourceSet copy() {
        ResourceSet instance = new ResourceSet();
        instance.content.putAll(this.content);
        instance.contentSize = this.contentSize;
        return instance;
    }

    /**
     * Creates a new resource set with given resource name and amount.
     * @param name resource name
     * @param amount amount
     * @return resource set
     */
    public static ResourceSet of(String name, int amount) {
        checkArgument(amount >= 0);

        name = (name == null) ? DEFAULT_RESOURCE_NAME : name;

        ResourceSet set = new ResourceSet();
        set.content.put(name, amount);
        set.contentSize = amount;
        return set;
    }

    /**
     * Creates a new resource set of default resource and given amount.
     * @param amount amount
     * @return resource set
     */
    public static ResourceSet of(int amount) {
        return of(null, amount);
    }

    /**
     * Returns an empty resource set.
     * @return an empty resource set
     */
    public static ResourceSet empty() {
        return EMPTY_SET;
    }

    /**
     * Infinite resource set.
     *
     * @return the resource set
     */
    public static ResourceSet infinite() {
        return INFINITE_SET;
    }
}
