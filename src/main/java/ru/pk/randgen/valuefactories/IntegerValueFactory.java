package ru.pk.randgen.valuefactories;

import ru.pk.randgen.ValueFactory;
import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.exceptions.GenRuntimeException;

public class IntegerValueFactory extends ValueFactory<Integer> {
    @Override
    public Integer makeValue(RandGenField ann) {
        if (RandGenField.GeneratorValueType.BOOLEAN.equals(ann.type()))
            return getBooleanGen().gen() ? 1 : 0;
        if (RandGenField.GeneratorValueType.INTEGER.equals(ann.type())) {
            return getIntegerGen().gen();
        }
        if (RandGenField.GeneratorValueType.LONG.equals(ann.type())) {
            return getIntegerGen().gen();
        }
        throw new GenRuntimeException("Not found generator with Integer-result for annotation type "+ ann.type());
    }
}
