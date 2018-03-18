package ru.pk.randgen.valuefactories;

import ru.pk.randgen.ValueFactory;
import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.exceptions.GenRuntimeException;

import java.text.DecimalFormat;

public class StringValueFactory extends ValueFactory<String> {
    private DecimalFormat decimalFormat = new DecimalFormat("0.000");

    @Override
    public String makeValue(RandGenField ann) {
        if (RandGenField.GeneratorValueType.BOOLEAN.equals(ann.type())) {
            return String.valueOf(getBooleanGen().gen());
        }
        if (RandGenField.GeneratorValueType.STRING.equals(ann.type())) {
            return getStringGen(ann).gen();
        }
        if (RandGenField.GeneratorValueType.INTEGER.equals(ann.type())) {
            return String.valueOf(getIntegerGen().gen());
        }
        if (RandGenField.GeneratorValueType.LONG.equals(ann.type())) {
            return String.valueOf(getIntegerGen().gen());
        } //TODO: Use String max-min length
        if (RandGenField.GeneratorValueType.DOUBLE.equals(ann.type())) {
            return decimalFormat.format(getDoubleGen().gen());
        }
        throw new GenRuntimeException("Not found generator with Long-result for annotation type "+ ann.type());
    }
}
