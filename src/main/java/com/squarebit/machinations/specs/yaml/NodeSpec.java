package com.squarebit.machinations.specs.yaml;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Node spec.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PoolSpec.class, name = "pool"),
        @JsonSubTypes.Type(value = GateSpec.class, name = "gate"),
        @JsonSubTypes.Type(value = SourceSpec.class, name = "source"),
        @JsonSubTypes.Type(value = DrainSpec.class, name = "drain"),
        @JsonSubTypes.Type(value = ConverterSpec.class, name = "converter"),
        @JsonSubTypes.Type(value = TraderSpec.class, name = "trader"),
        @JsonSubTypes.Type(value = EndSpec.class, name = "end"),
})
public abstract class NodeSpec extends ElementSpec {
    private String name;
    private String activationMode;
    private String flowMode;

    private List<String> connections = new ArrayList<>();
    private List<String> modifiers = new ArrayList<>();
    private List<String> triggers = new ArrayList<>();
    private List<String> activators = new ArrayList<>();

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     * @return the name
     */
    public NodeSpec setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets activation mode.
     *
     * @return the activation mode
     */
    public String getActivationMode() {
        return activationMode;
    }

    /**
     * Sets activation mode.
     *
     * @param activationMode the activation mode
     * @return the activation mode
     */
    public NodeSpec setActivationMode(String activationMode) {
        this.activationMode = activationMode;
        return this;
    }

    /**
     * Gets flow mode.
     *
     * @return the flow mode
     */
    public String getFlowMode() {
        return flowMode;
    }

    /**
     * Sets flow mode.
     *
     * @param flowMode the flow mode
     * @return the flow mode
     */
    public NodeSpec setFlowMode(String flowMode) {
        this.flowMode = flowMode;
        return this;
    }

    /**
     * Gets connections.
     *
     * @return the connections
     */
    public List<String> getConnections() {
        return connections;
    }

    /**
     * Sets connections.
     *
     * @param connections the connections
     * @return the connections
     */
    public NodeSpec setConnections(List<String> connections) {
        this.connections = connections;
        return this;
    }

    /**
     * Gets modifiers.
     *
     * @return the modifiers
     */
    public List<String> getModifiers() {
        return modifiers;
    }

    /**
     * Sets modifiers.
     *
     * @param modifiers the modifiers
     * @return the modifiers
     */
    public NodeSpec setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    /**
     * Gets triggers.
     *
     * @return the triggers
     */
    public List<String> getTriggers() {
        return triggers;
    }

    /**
     * Sets triggers.
     *
     * @param triggers the triggers
     * @return the triggers
     */
    public NodeSpec setTriggers(List<String> triggers) {
        this.triggers = triggers;
        return this;
    }

    /**
     * Gets activators.
     *
     * @return the activators
     */
    public List<String> getActivators() {
        return activators;
    }

    /**
     * Sets activators.
     *
     * @param activators the activators
     * @return the activators
     */
    public NodeSpec setActivators(List<String> activators) {
        this.activators = activators;
        return this;
    }
}
