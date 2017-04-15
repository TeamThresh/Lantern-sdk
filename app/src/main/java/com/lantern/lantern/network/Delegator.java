package com.lantern.lantern.network;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by YS on 2017-02-07.
 */

public class Delegator {
    private Object source;
    private Object delegate;
    private Class superclass;

    public Delegator(Object source, Class superclass, Object delegate) {
        this.source = source;
        this.superclass = superclass;
        this.delegate = delegate;
    }

    public Delegator(Object source, Class superclass, String delegateClassName) {
        try {
            this.source = source;
            this.superclass = superclass;
            Class e = Class.forName(delegateClassName);
            Constructor delegateConstructor = e.getDeclaredConstructor(new Class[0]);
            delegateConstructor.setAccessible(true);
            this.delegate = delegateConstructor.newInstance(new Object[0]);
        } catch (RuntimeException var6) {
            throw var6;
        } catch (Exception var7) {
            var7.printStackTrace();
            //throw new DelegationException("Could not make delegate object", var7);
        }
    }

    public final <T> T invoke(Object... args) throws Exception {
        String methodName = this.extractMethodName();
        Method method = this.findMethod(methodName, args);
        Object t = this.invoke0(method, args);
        return (T)t;
    }

    private Object invoke0(Method method, Object[] args) throws Exception {
        try {
            this.writeFields(this.superclass, this.source, this.delegate);
            method.setAccessible(true);
            Object e = method.invoke(this.delegate, args);
            this.writeFields(this.superclass, this.delegate, this.source);
            return e;
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    private void writeFields(Class clazz, Object from, Object to) throws Exception {
        Field[] var4 = clazz.getDeclaredFields();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            field.setAccessible(true);
            field.set(to, field.get(from));
        }

    }

    private String extractMethodName() {
        Throwable t = new Throwable();
        String methodName = t.getStackTrace()[2].getMethodName();
        return methodName;
    }

    private Method findMethod(String methodName, Object[] args) throws NoSuchMethodException {
        Class clazz = this.superclass;
        if(args.length == 0) {
            return clazz.getDeclaredMethod(methodName, new Class[0]);
        } else {
            Method match = null;
            Iterator var5 = getAllMethods(clazz).iterator();

            while(true) {
                label45:
                while(true) {
                    Method method;
                    Class[] classes;
                    do {
                        do {
                            if(!var5.hasNext()) {
                                if(match != null) {
                                    return match;
                                }

                                throw new RuntimeException("Could not find method: " + methodName);
                                //throw new DelegationException("Could not find method: " + methodName);
                            }

                            method = (Method)var5.next();
                        } while(!method.getName().equals(methodName));

                        classes = method.getParameterTypes();
                    } while(classes.length != args.length);

                    for(int i = 0; i < classes.length; ++i) {
                        Class argType = classes[i];
                        argType = this.convertPrimitiveClass(argType);
                        if(!argType.isInstance(args[i])) {
                            continue label45;
                        }
                    }

                    if(match != null) {
                        throw new RuntimeException("Duplicate matches");
                    }

                    match = method;
                }
            }
        }
    }

    private Class<?> convertPrimitiveClass(Class<?> primitive) {
        if(primitive.isPrimitive()) {
            if(primitive == Integer.TYPE) {
                return Integer.class;
            }

            if(primitive == Boolean.TYPE) {
                return Boolean.class;
            }

            if(primitive == Float.TYPE) {
                return Float.class;
            }

            if(primitive == Long.TYPE) {
                return Long.class;
            }

            if(primitive == Double.TYPE) {
                return Double.class;
            }

            if(primitive == Short.TYPE) {
                return Short.class;
            }

            if(primitive == Byte.TYPE) {
                return Byte.class;
            }

            if(primitive == Character.TYPE) {
                return Character.class;
            }
        }

        return primitive;
    }

    public Delegator.DelegatorMethodFinder delegateTo(String methodName, Class... parameters) {
        return new Delegator.DelegatorMethodFinder(methodName, parameters);
    }

    public class DelegatorMethodFinder {
        private final Method method;

        public DelegatorMethodFinder(String methodName, Class<?>[] parameterTypes) {
            try {
                this.method = Delegator.this.superclass.getDeclaredMethod(methodName, parameterTypes);
            } catch (RuntimeException var5) {
                throw var5;
            } catch (Exception var6) {
                throw new RuntimeException(var6);
                //throw new DelegationException(var6);
            }
        }

        public <T> T invoke(Object... parameters) throws Exception {
            Object t = Delegator.this.invoke0(this.method, parameters);
            return (T)t;
        }
    }

    public Set<Method> getAllMethods(Class<?> type) {
        HashSet result = new HashSet();
        Iterator var2 = getAllSuperTypes(type).iterator();

        while(var2.hasNext()) {
            Class t = (Class)var2.next();
            Method[] var4 = t.getDeclaredMethods();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Method m = var4[var6];
                result.add(m);
            }
        }

        return result;
    }

    public Set<Class<?>> getAllSuperTypes(Class<?> type) {
        HashSet result = new HashSet();
        if(type != null && !type.equals(Object.class)) {
            result.add(type);
            result.addAll(getAllSuperTypes(type.getSuperclass()));
            Class[] var2 = type.getInterfaces();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Class ifc = var2[var4];
                result.addAll(getAllSuperTypes(ifc));
            }
        }

        return result;
    }
}
