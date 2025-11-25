package us.appfluent.xwidget.utils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {

    public static Field findDeclaredField(
            @NotNull Object instance,
            @NotNull String fieldName
    ) throws NoSuchFieldException {
        Class<?> myClass = instance.getClass();
        while (myClass != null) {
            try {
                Field field = myClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                myClass = myClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' was not found in " + instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(
            @NotNull Object instance,
            @NotNull String fieldName
    ) {
        try {
            Field field = findDeclaredField(instance, fieldName);
            return (T) field.get(instance);
        } catch(Exception e) {
            throw new RuntimeException("Problem getting value for field '" + fieldName + "' using reflection.", e);
        }
    }

    public static Method findDeclaredMethod(
            @NotNull Object instance,
            @NotNull String methodName,
            Class<?>... paramTypes
    ) throws NoSuchMethodException {
        Class<?> myClass = instance.getClass();
        while (myClass != null) {
            try {
                Method method = myClass.getDeclaredMethod(methodName, paramTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {
                myClass = myClass.getSuperclass();
            }
        }
        throw new NoSuchMethodException("Method '" + methodName + "' was not found in " + instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(
            @NotNull Object instance,
            @NotNull String methodName,
            Object... methodArgs
    )  {
        Object[] args = new Object[methodArgs.length];
        Class<?>[] types = new Class[methodArgs.length];
        for (int i = 0; i < methodArgs.length; i++) {
            if (methodArgs[i] instanceof  MethodArg) {
                args[i] = ((MethodArg) methodArgs[i]).arg;
                types[i] = ((MethodArg) methodArgs[i]).type;
            } else {
                args[i] = methodArgs[i];
                types[i] = methodArgs[i].getClass();
            }
        }
        try {
            Method method = findDeclaredMethod(instance, methodName, types);
            return (T) method.invoke(instance, args);
        } catch (Exception e) {
            throw new RuntimeException("Problem invoking method '" + methodName + "' using reflection.", e);
        }
    }

    public static MethodArg methodArg(@NotNull Object arg, @NotNull Class<?> type) {
        return new MethodArg(arg, type);
    }

    public static class MethodArg {
        final Object arg;;
        final Class<?> type;

        public MethodArg(@NotNull Object arg, @NotNull Class<?> type) {
            this.arg = arg;
            this.type = type;
        }
    }
}


