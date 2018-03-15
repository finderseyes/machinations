package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.exceptions.MachineException;
import com.squarebit.machinations.machc.avm.runtime.TObjectBase;
import com.squarebit.machinations.machc.avm.runtime.TObject;

import java.util.Stack;

public final class Machine {
    //////////////////////////////////////////
    // Call stack and data stack.
    private Frame activeFrame = null;
    private Stack<TObject> dataStack;

    /**
     * Initializes a new machine instance.
     */
    public Machine() {
        this.activeFrame = null;
        this.dataStack = new Stack<>();
    }

    public TObject newInstance(TypeInfo typeInfo) throws MachineException {
        TObject instance = newUninitializedInstance(typeInfo);
        return null;
    }

    private TObject newUninitializedInstance(TypeInfo typeInfo) throws MachineException {
        Class implementingClass = typeInfo.getImplementingClass();
        try {
            TObject instance = (TObject)implementingClass.newInstance();

            if (instance instanceof TObjectBase) {
                TObjectBase fieldContainer = (TObjectBase)instance;
                allocateFieldStorge(typeInfo, fieldContainer);
            }

            return instance;
        }
        catch (Exception exception) {
            throw new MachineException(exception);
        }
    }

    private void allocateFieldStorge(TypeInfo typeInfo, TObjectBase fieldContainer) {
        fieldContainer.__
    }
}
