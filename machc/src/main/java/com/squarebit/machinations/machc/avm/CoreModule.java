package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.runtime.*;

/**
 * Core {@link TypeInfo}s.
 */
public final class CoreModule {
    private static final CoreModule INTERNAL = new CoreModule();

    private final ModuleInfo module;

    private final TypeInfo objectType;
    private final TypeInfo voidType;
    private final TypeInfo nanType;

    private final TypeInfo integerType;
    private final TypeInfo floatType;
    private final TypeInfo randomDiceType;
    private final TypeInfo booleanType;
    private final TypeInfo stringType;

    private final TypeInfo setDescriptorType;
    private final TypeInfo setType;

    private final TypeInfo graphType;

    /**
     * Initializes a new core module.
     */
    private CoreModule() {
        this.module = new ModuleInfo();

        try {
            this.objectType = module.createType("Object");
            this.voidType = module.createType("Void").setImplementingClass(TVoid.class);
            this.nanType = module.createType("NaN").setImplementingClass(TNaN.class);

            this.integerType = module.createType("Integer").setImplementingClass(TInteger.class);
            this.floatType = module.createType("Float").setImplementingClass(TFloat.class);
            this.randomDiceType = module.createType("RandomDice").setImplementingClass(TRandomDice.class);
            this.booleanType = module.createType("Boolean").setImplementingClass(TBoolean.class);
            this.stringType = module.createType("String").setImplementingClass(TString.class);

            this.setDescriptorType = module.createType("SetDescriptor").setImplementingClass(TSetDescriptor.class);
            this.setType = module.createType("Set").setImplementingClass(TSet.class);

            this.graphType = module.createType("Graph").setImplementingClass(TGraph.class);
        }
        catch (Exception exception) {
            throw new RuntimeException("Error during initialize core module", exception);
        }
    }

    // Public static data.
    public static final ModuleInfo INSTANCE = INTERNAL.module;

    public static final TypeInfo OBJECT_TYPE = INTERNAL.objectType;
    public static final TypeInfo VOID_TYPE = INTERNAL.voidType;
    public static final TypeInfo NAN_TYPE = INTERNAL.nanType;

    public static final TypeInfo INTEGER_TYPE = INTERNAL.integerType;
    public static final TypeInfo FLOAT_TYPE = INTERNAL.floatType;
    public static final TypeInfo RANDOM_DICE_TYPE = INTERNAL.randomDiceType;
    public static final TypeInfo BOOLEAN_TYPE = INTERNAL.booleanType;
    public static final TypeInfo STRING_TYPE = INTERNAL.stringType;

    public static final TypeInfo SET_DESCRIPTOR_TYPE = INTERNAL.setDescriptorType;
    public static final TypeInfo SET_TYPE = INTERNAL.setType;

    public static final TypeInfo GRAPH_TYPE = INTERNAL.graphType;
}
