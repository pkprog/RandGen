package ru.pk.randgen;

import org.omg.CORBA.portable.ValueFactory;
import ru.pk.randgen.valuefactories.StringValueFactory;

import java.util.HashMap;
import java.util.Map;

public class ValueFactoryManager {

    Map<Class, ValueFactory> classValueFactoryMap = new HashMap<>();

    public ValueFactoryManager() {
        classValueFactoryMap.put(String.class, new StringValueFactory());
    }
}
