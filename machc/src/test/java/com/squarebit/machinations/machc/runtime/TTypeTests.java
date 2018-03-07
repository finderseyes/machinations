package com.squarebit.machinations.machc.runtime;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests on type.
 */
public class TTypeTests {
    public static class RuntimeClass extends TObject {

    }

    @Test
    public void should_instantiate_object_of_given_type() throws Exception {
        TField nameField = new TField("name", BuiltinTypes.OBJECT_TYPE);
        TField ageField = new TField("age", BuiltinTypes.INTEGER_TYPE);

        TType<RuntimeClass> type = new TType<>("aaa", BuiltinTypes.GRAPH_TYPE, RuntimeClass.class, false,
                new TField[]{ nameField, ageField},
                new TMethod[]{});

        TObject value = BuiltinTypes.OBJECT_TYPE.newInstance();
        RuntimeClass obj = type.newInstance();

        assertThat(nameField.get(obj)).isNull();
        assertThat(ageField.get(obj)).isNotNull();

        nameField.set(obj, value);
        assertThat(nameField.get(obj)).isEqualTo(value);
    }
}
