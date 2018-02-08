package com.squarebit.machinations.models;

import com.squarebit.machinations.specs.yaml.PoolSpec;
import com.squarebit.machinations.specs.yaml.YamlSpec;
import org.bson.types.ObjectId;

import java.util.concurrent.atomic.AtomicReference;

public class MachinationContextFactory {
    public MachinationContext fromSpec(YamlSpec spec) throws Exception {
        MachinationContext context = new MachinationContext();

        this.createNode(context, spec);

        return context;
    }

    private void createNode(MachinationContext context, YamlSpec spec) throws Exception {
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
                    context.addElement(node);
                }
                catch (Exception ex) {
                    lastError.compareAndSet(null, ex);
                }
            }
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private String getOrCreateId(String id) {
        if (id != null && !id.equals(""))
            return id;
        else
            return ObjectId.get().toHexString();
    }
}
