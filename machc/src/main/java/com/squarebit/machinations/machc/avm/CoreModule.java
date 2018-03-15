package com.squarebit.machinations.machc.avm;

/**
 * Core {@link TypeInfo}s.
 */
final class CoreModule {
    private static final CoreModule INTERNAL = new CoreModule();
    private final ModuleInfo module;
    private final TypeInfo objectType;

    /**
     * Initializes a new core module.
     */
    private CoreModule() {
        this.module = new ModuleInfo();

        try {
            this.objectType = module.createType("Object");
        }
        catch (Exception exception) {
            throw new RuntimeException("Error during initialize core module", exception);
        }
    }

    // Public static data.
    public static final ModuleInfo INSTANCE = INTERNAL.module;
    public static final TypeInfo OBJECT_TYPE = INTERNAL.objectType;
}
