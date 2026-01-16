package com.reptilemanagement.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utility class for reflection operations.
 */
public class ReflectionUtil {
    
    private ReflectionUtil() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Gets the runtime class of a generic type argument.
     * 
     * @param clazz the class to inspect
     * @param argumentIndex the index of the generic type argument (0-based)
     * @return the class of the generic type argument
     * @throws IllegalArgumentException if the type cannot be determined
     */
    public static Class<?> getRuntimeArgumentClass(Class<?> clazz, int argumentIndex) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            
            if (argumentIndex >= 0 && argumentIndex < typeArguments.length) {
                Type type = typeArguments[argumentIndex];
                if (type instanceof Class) {
                    return (Class<?>) type;
                } else if (type instanceof ParameterizedType) {
                    return (Class<?>) ((ParameterizedType) type).getRawType();
                }
            }
        }
        
        throw new IllegalArgumentException(
            "Cannot determine generic type argument at index " + argumentIndex + " for class " + clazz.getName()
        );
    }
    
    /**
     * Gets the simple name of a runtime generic type argument.
     * 
     * @param clazz the class to inspect
     * @param argumentIndex the index of the generic type argument (0-based)
     * @return the simple name of the generic type argument
     */
    public static String getSimpleRuntimeTypeName(Class<?> clazz, int argumentIndex) {
        try {
            Class<?> argumentClass = getRuntimeArgumentClass(clazz, argumentIndex);
            return argumentClass.getSimpleName();
        } catch (IllegalArgumentException e) {
            return "Unknown";
        }
    }
}
