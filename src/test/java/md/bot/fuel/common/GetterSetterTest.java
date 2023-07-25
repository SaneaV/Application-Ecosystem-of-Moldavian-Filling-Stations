package md.bot.fuel.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import static java.lang.Boolean.FALSE;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

public class GetterSetterTest {
    protected static final Map<Class<?>, Supplier<?>> DEFAULT_SUPPLIERS;

    private static final String GET_KEYWORD = "get";
    private static final int GET_KEYWORD_LENGTH = 3;
    private static final String SET_KEYWORD = "set";
    private static final int SET_KEYWORD_LENGTH = 3;
    private static final String IS_KEYWORD = "is";
    private static final int IS_KEYWORD_LENGTH = 2;

    private static final String ERROR_DESCRIPTION = "Unable to create objects for field: %s from class %s";

    static {
        DEFAULT_SUPPLIERS = new HashMap<>();

        DEFAULT_SUPPLIERS.put(double.class, () -> 0.0d);
        DEFAULT_SUPPLIERS.put(long.class, () -> 0L);

        DEFAULT_SUPPLIERS.put(Double.class, () -> 0.0d);
        DEFAULT_SUPPLIERS.put(Boolean.class, () -> FALSE);
        DEFAULT_SUPPLIERS.put(String.class, () -> "");
    }

    private final Set<String> ignoreMethods = new HashSet<>(singleton("getClass"));
    private final Map<Class<?>, Supplier<?>> supplierMap = new HashMap<>(DEFAULT_SUPPLIERS);

    protected void addIgnoreMethods(List<String> ignoreMethodsNames) {
        ignoreMethods.addAll(ignoreMethodsNames);
    }

    public <T> void testGettersAndSetters(T instance) throws Exception {
        final Map<String, GetterSetterPair> getterSetterPairMapping = getStringGetterSetterPairMap(instance);

        for (final Entry<String, GetterSetterPair> entry : getterSetterPairMapping.entrySet()) {
            final GetterSetterPair pair = entry.getValue();

            final String objectName = entry.getKey();
            final String fieldName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);

            if (pair.hasGetterAndSetter()) {
                final Class<?> parameterType = pair.getSetter().getParameterTypes()[0];
                final Object newObject = createObject(fieldName, parameterType);

                pair.getSetter().invoke(instance, newObject);
            }
            if (pair.getGetter() != null) {
                final Class<?> parameterType = pair.getGetter().getReturnType();
                final Object newObject = createObject(fieldName, parameterType);

                final Field field = instance.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(instance, newObject);

                callGetter(pair.getGetter(), instance, newObject);
            }
        }
    }

    private Object createObject(String fieldName, Class<?> clazz) {
        final Supplier<?> supplier = this.supplierMap.get(clazz);

        if (supplier != null) {
            return supplier.get();
        }

        if (clazz.isEnum()) {
            return clazz.getEnumConstants()[0];
        }

        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format(ERROR_DESCRIPTION, fieldName, clazz.getName()), e);
        }
    }

    private <T> Map<String, GetterSetterPair> getStringGetterSetterPairMap(T instance) {
        final Map<String, GetterSetterPair> getterSetterMapping = new HashMap<>();

        for (final Method method : instance.getClass().getMethods()) {
            final String methodName = method.getName();

            if (this.ignoreMethods.contains(methodName)) {
                continue;
            }

            if (methodName.startsWith(GET_KEYWORD) && method.getParameterCount() == 0) {
                final GetterSetterPair getterSetterPair = populateGetterSetterPair(getterSetterMapping, methodName, GET_KEYWORD_LENGTH);
                getterSetterPair.setGetter(method);
            } else if (methodName.startsWith(SET_KEYWORD) && method.getParameterCount() == 1) {
                final GetterSetterPair getterSetterPair = populateGetterSetterPair(getterSetterMapping, methodName, SET_KEYWORD_LENGTH);
                getterSetterPair.setSetter(method);
            } else if (methodName.startsWith(IS_KEYWORD) && method.getParameterCount() == 0) {
                final GetterSetterPair getterSetterPair = populateGetterSetterPair(getterSetterMapping, methodName, IS_KEYWORD_LENGTH);
                getterSetterPair.setGetter(method);
            }
        }
        return getterSetterMapping;
    }

    private GetterSetterPair populateGetterSetterPair(Map<String, GetterSetterPair> getterSetterMapping, String methodName,
                                                      int keyWordLength) {
        final String fieldName = getFieldName(methodName, keyWordLength);
        return putIfAbsent(getterSetterMapping, fieldName);
    }

    private String getFieldName(String methodName, int keyWordLength) {
        return methodName.substring(keyWordLength);
    }

    private GetterSetterPair putIfAbsent(Map<String, GetterSetterPair> getterSetterMapping, String fieldName) {
        final GetterSetterPair getterSetterPair = getterSetterMapping.getOrDefault(fieldName, new GetterSetterPair());
        getterSetterMapping.putIfAbsent(fieldName, getterSetterPair);
        return getterSetterPair;
    }

    private <T> void callGetter(Method getter, T instance, Object expected) throws InvocationTargetException, IllegalAccessException {
        final Object actual = getter.invoke(instance);
        assertThat(actual).isEqualTo(expected);
    }
}