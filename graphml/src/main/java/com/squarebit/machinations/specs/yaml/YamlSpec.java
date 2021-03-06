package com.squarebit.machinations.specs.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Yaml spec.
 */
public class YamlSpec {
    private ConfigsSpec configs = new ConfigsSpec();
    private List<NodeSpec> nodes = new ArrayList<>();
    private List<String> connections = new ArrayList<>();
    private List<String> modifiers = new ArrayList<>();
    private List<String> triggers = new ArrayList<>();
    private List<String> activators = new ArrayList<>();

    /**
     * Gets nodes.
     *
     * @return the nodes
     */
    public List<NodeSpec> getNodes() {
        return nodes;
    }

    /**
     * Sets nodes.
     *
     * @param nodes the nodes
     * @return the nodes
     */
    public YamlSpec setNodes(List<NodeSpec> nodes) {
        this.nodes = nodes;
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
    public YamlSpec setConnections(List<String> connections) {
        this.connections = connections;
        return this;
    }

    /**
     * Gets configs spec.
     *
     * @return the configs spec
     */
    public ConfigsSpec getConfigs() {
        return configs;
    }

    /**
     * Sets configs spec.
     *
     * @param configs the configs spec
     * @return the configs spec
     */
    public YamlSpec setConfigs(ConfigsSpec configs) {
        this.configs = configs;
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
    public YamlSpec setModifiers(List<String> modifiers) {
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
    public YamlSpec setTriggers(List<String> triggers) {
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
    public YamlSpec setActivators(List<String> activators) {
        this.activators = activators;
        return this;
    }

    public static YamlSpec fromFile(String file) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        YamlSpec specs = mapper.readValue(new File(file), YamlSpec.class);
        return specs;
    }
}
