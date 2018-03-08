package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.runtime.components.TField;
import com.squarebit.machinations.machc.runtime.components.TMethod;
import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TType;
import org.junit.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests on type.
 */
public class TTypeTests {
    public static class RuntimeClass extends TObject {

    }

    Supplier<String> foo(String k) {
        String x = k;
        return () -> {
            String y = x + "---";
            return y;
        };
    }

    @Test
    public void should_instantiate_object_of_given_type() throws Exception {
        String a = foo("abc").get();
        String b = foo("sdfds").get();
//        TField nameField = new TField("name", BuiltinTypes.OBJECT_TYPE);
//        TField ageField = new TField("age", BuiltinTypes.INTEGER_TYPE);
//
//        TType<RuntimeClass> type = new TType<>("aaa", BuiltinTypes.GRAPH_TYPE, RuntimeClass.class, false,
//                new TField[]{ nameField, ageField},
//                new TMethod[]{});
//
//        TObject value = BuiltinTypes.OBJECT_TYPE.newInstance();
//        RuntimeClass obj = type.newInstance();
//
//        assertThat(nameField.get(obj)).isNull();
//        assertThat(ageField.get(obj)).isNotNull();
//
//        nameField.set(obj, value);
//        assertThat(nameField.get(obj)).isEqualTo(value);
    }
}
