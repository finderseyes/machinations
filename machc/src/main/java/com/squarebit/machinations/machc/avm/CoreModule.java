package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.runtime.TInteger;

/**
 * Core {@link TypeInfo}s.
 */
public final class CoreModule {
    private static final CoreModule INTERNAL = new CoreModule();
    private final ModuleInfo module;
    private final TypeInfo objectType;
    private final TypeInfo integerType;

    /**
     * Initializes a new core module.
     */
    private CoreModule() {
        this.module = new ModuleInfo();

        try {
            this.objectType = module.createType("Object");
            this.integerType = module.createType("Integer").setImplementingClass(TInteger.class);
        }
        catch (Exception exception) {
            throw new RuntimeException("Error during initialize core module", exception);
        }
    }

    // Public static data.
    public static final ModuleInfo INSTANCE = INTERNAL.module;
    public static final TypeInfo OBJECT_TYPE = INTERNAL.objectType;
    public static final TypeInfo INTEGER_TYPE = INTERNAL.integerType;
}
