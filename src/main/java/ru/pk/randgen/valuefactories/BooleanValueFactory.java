package ru.pk.randgen.valuefactories;

import ru.pk.randgen.ValueFactory;
import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.exceptions.GenRuntimeException;
import ru.pk.randgen.gentype.StringGen;

public class BooleanValueFactory extends ValueFactory<Boolean> {

    public Boolean makeValue(RandGenField ann) {
        if (RandGenField.GeneratorValueType.BOOLEAN.equals(ann.type()))
            return getBooleanGen().gen();
        if (RandGenField.GeneratorValueType.STRING.equals(ann.type())) {
            StringGen sg = getStringGen(ann);
            String tempS = sg.gen();
            return !tempS.equals("");
        }
        if (RandGenField.GeneratorValueType.INTEGER.equals(ann.type())) {
            return getIntegerGen().gen() != 0;
        }
        if (RandGenField.GeneratorValueType.LONG.equals(ann.type())) {
            return getIntegerGen().gen() != 0;
        }
        if (RandGenField.GeneratorValueType.DOUBLE.equals(ann.type())) {
            return getDoubleGen().gen() != 0d;
        }
        throw new GenRuntimeException("Not found generator with Boolean-result for annotation type "+ ann.type());
    }

}
