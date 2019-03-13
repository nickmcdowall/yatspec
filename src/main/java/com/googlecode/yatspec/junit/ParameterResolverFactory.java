package com.googlecode.yatspec.junit;

import static com.googlecode.yatspec.Creator.create;
import static java.lang.Class.forName;
import static java.lang.System.getProperty;

public class ParameterResolverFactory {

    public static final String PARAMETER_RESOLVER = "yatspec.parameter.resolver";

    public static ParameterResolver parameterResolver() {
        try {
            return create(forName(getProperty(PARAMETER_RESOLVER, VarargsParameterResolver.class.getName())));
        } catch (Exception e) {
            return new VarargsParameterResolver();
        }
    }
}
