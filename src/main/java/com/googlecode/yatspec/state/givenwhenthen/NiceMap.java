package com.googlecode.yatspec.state.givenwhenthen;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

@SuppressWarnings({"unused", "unchecked"})
class NiceMap<T extends NiceMap> {
    private final Map<String, Object> map = Collections.synchronizedMap(new LinkedHashMap<>());

    public NiceMap(Object... instances) {
        for (Object instance : instances) {
            add(instance);
        }
    }

    public <R> R getType(String key, Class<R> aClass) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (!aClass.isAssignableFrom(value.getClass())) {
            throw new ClassCastException("You requested a " + aClass.getSimpleName() + " but got a " + value.getClass() + " (" + value + ")");
        }
        return (R) value;
    }

    public Map<String, Object> getTypes() {
        synchronized (map) {
            return unmodifiableMap(new LinkedHashMap<>(map));
        }
    }

    public <R> R getType(Class<R> aClass) {
        return getType(defaultName(aClass), aClass);
    }

    public T add(String key, Object instance) {
        map.put(key, instance);
        return (T) this;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public T add(Object instance) {
        if (instance == null) {
            return (T) this;
        }
        return add(defaultName(instance.getClass()), instance);
    }

    public boolean contains(Class aClass) {
        return contains(defaultName(aClass));
    }

    public boolean contains(String name) {
        return map.containsKey(name);
    }

    private static String defaultName(Class<?> aClass) {
        return aClass.getSimpleName();
    }

    public T putAll(Collection<Map.Entry<String, Object>> entries) {
        for (Map.Entry<String, Object> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return (T) this;
    }

    public T putAll(Map<String, Object> map) {
        return putAll(map.entrySet());
    }

    public void clear() {
        map.clear();
    }

    public void remove(String key) {
        map.remove(key);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
