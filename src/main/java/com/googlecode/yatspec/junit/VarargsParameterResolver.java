package com.googlecode.yatspec.junit;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class VarargsParameterResolver implements ParameterResolver {

    @Override
    public Object[] resolveParameters(Row row, Class notUsed, Method method) {
        final Object[] suppliedParams = row.value();
        final Class<?>[] declaredParameters = method.getParameterTypes();

        if (isMissingRequiredArguments(suppliedParams, declaredParameters)) {
            throw new IllegalArgumentException("Row does not contain the mimimum number of parameters.");
        }

        if (method.isVarArgs()) {
            int declaredParametersCount = declaredParameters.length;
            List<Object> resolvedParameters = new ArrayList<>();
            resolvedParameters.addAll(normalArgumentsFrom(suppliedParams, declaredParametersCount));
            resolvedParameters.add(varargsFrom(suppliedParams, declaredParameters));
            return resolvedParameters.toArray();
        }

        return suppliedParams;
    }

    public Object[] varargsFrom(Object[] suppliedParams, Class<?>[] declaredParameters) {
        Class varargsParameterType = last(declaredParameters).getComponentType();
        return extractVarargsOrEmptyArray(suppliedParams, varargsParameterType, declaredParameters.length);
    }

    public List<Object> normalArgumentsFrom(Object[] suppliedParams, int numberOfParamaters) {
        return stream(suppliedParams)
                .limit(numberOfParamaters - 1)
                .collect(toList());
    }

    private boolean isMissingRequiredArguments(Object[] input, Class<?>[] expected) {
        return input.length < expected.length - 1;
    }

    private boolean hasVarArgsSupplied(int suppliedParams, int expectedParams) {
        return suppliedParams >= expectedParams;
    }

    private Object[] extractVarargsOrEmptyArray(Object[] suppliedParams, Class componentType, int noOfRequiredParams) {
        if (hasVarArgsSupplied(suppliedParams.length, noOfRequiredParams)) {
            return arrayContainingVarargsOnly(suppliedParams, noOfRequiredParams - 1);
        }
        return emptyArrayOfType(componentType);
    }

    private Object[] arrayContainingVarargsOnly(Object[] suppliedParams, int noOfMandatoryParams) {
        return Arrays.copyOfRange(suppliedParams, noOfMandatoryParams, suppliedParams.length);
    }

    private Object[] emptyArrayOfType(Class componentType) {
        return (Object[]) Array.newInstance(componentType, 0);
    }

    private Class last(Class[] givenArray) {
        return givenArray[givenArray.length - 1];
    }
}
