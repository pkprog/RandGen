package ru.pk.randgen.valuefactories;

import ru.pk.randgen.ValueFactory;
import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.exceptions.GenRuntimeException;

public class DoubleValueFactory extends ValueFactory<Double> {
    @Override
    public Double makeValue(RandGenField ann) {
        if (RandGenField.GeneratorValueType.BOOLEAN.equals(ann.type()))
            return getBooleanGen().gen() ? 1d : 0;
        if (RandGenField.GeneratorValueType.INTEGER.equals(ann.type())) {
            return getIntegerGen().gen().doubleValue();
        }
        if (RandGenField.GeneratorValueType.LONG.equals(ann.type())) {
            return getIntegerGen().gen().doubleValue();
        }
        if (RandGenField.GeneratorValueType.DOUBLE.equals(ann.type())) {
            return getDoubleGen().gen();
        }
        throw new GenRuntimeException("Not found generator with Integer-result for annotation type "+ ann.type());

    }
}
