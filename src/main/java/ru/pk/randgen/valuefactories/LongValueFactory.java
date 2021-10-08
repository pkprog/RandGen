package ru.pk.randgen.valuefactories;

import ru.pk.randgen.ValueFactory;
import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.exceptions.GenRuntimeException;

public class LongValueFactory extends ValueFactory<Long> {
    public LongValueFactory(RandGenField ann) {
        super(ann);
    }

    @Override
    public Long makeValue() {
        if (RandGenField.GeneratorValueType.BOOLEAN.equals(ann.type()))
            return getBooleanGen().gen() ? 1L : 0L;
        if (RandGenField.GeneratorValueType.INTEGER.equals(ann.type())) {
            return getIntegerGen().gen().longValue();
        }
        if (RandGenField.GeneratorValueType.LONG.equals(ann.type())) {
            return getIntegerGen().gen().longValue();
        }
        if (RandGenField.GeneratorValueType.DOUBLE.equals(ann.type())) {
            return Math.round(getDoubleGen().gen());
        }
        throw new GenRuntimeException("Not found generator with Long-result for annotation type "+ ann.type());
    }
}
