package com.squarebit.machinations.models;

import com.squarebit.machinations.specs.yaml.ElementSpec;
import com.squarebit.machinations.specs.yaml.PoolSpec;
import com.squarebit.machinations.specs.yaml.YamlSpec;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MachinationsContextFactory {
    private class BuildingContext {
        private MachinationsContext machinations;
        private YamlSpec spec;
        private Map<AbstractElement, ElementSpec> elementSpec = new HashMap<>();
    }

    public MachinationsContext fromSpec(YamlSpec spec) throws Exception {
        BuildingContext context = new BuildingContext();
        context.machinations = new MachinationsContext();
        context.spec = spec;

        this.createNode(context, spec);
        this.createNodeConnections(context);

        return context.machinations;
    }

    private void createNode(BuildingContext context, YamlSpec spec) throws Exception {
        AtomicReference<Exception> lastError = new AtomicReference<>();

        // Build the node, first pass.
        spec.getNodes().forEach(nodeSpec -> {
            if (lastError.get() != null)
                return;

            String id = getOrCreateId(nodeSpec.getId());
            AbstractNode node = null;

            if (nodeSpec instanceof PoolSpec) {
                Pool pool = new Pool();
                node = pool;
            }

            if (node != null) {
                node.setName(nodeSpec.getName())
                        .setActivationMode(ActivationMode.from(nodeSpec.getActivationMode()))
                        .setId(id);
                try {
                    context.machinations.addElement(node);
                    context.elementSpec.put(node, nodeSpec);
                }
                catch (Exception ex) {
                    lastError.compareAndSet(null, ex);
                }
            }
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private void createNodeConnections(BuildingContext context) {

    }

    private String getOrCreateId(String id) {
        if (id != null && !id.equals(""))
            return id;
        else
            return ObjectId.get().toHexString();
    }
}
