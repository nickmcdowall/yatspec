package com.googlecode.yatspec;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class Creator {
    public static <T> T create(Class<?> aClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?>[] constructors = aClass.getConstructors();
        return (T) constructors[0].newInstance();
    }

    public static Optional<Class<?>> optionalClass(String name) {
        try {
            return Optional.of(Class.forName(name));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
