package optic_fusion1.slimefunreloaded.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;

public final class ReflectionUtils {

  private ReflectionUtils() {}

  public static Class<?> getNMSClass(String name) {
    return getClass("net.minecraft.server." + getVersion() + "." + name);
  }

  public static Class<?> getCraftClass(String name) {
    return getClass("org.bukkit.craftbukkit." + getVersion() + "." + name);
  }

  public static String getVersion() {
    try {
      return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    } catch (Exception e) {
    }
    return "Couldn't get the version used";
  }

  public static String getRawVersion() {
    try {
      String pkg = Bukkit.getServer().getClass().getPackage().getName();
      return pkg.substring(pkg.lastIndexOf('.') + 1);
    } catch (Exception e) {
    }
    return "Couldn't get the version used";
  }

  public static String getNMSVersion() {
    return "net.minecraft.server." + getRawVersion() + ".";
  }

  public static Class<?> wrapperToPrimitive(Class<?> clazz) {
    if (clazz == Boolean.class) {
      return Boolean.TYPE;
    }
    if (clazz == Integer.class) {
      return Integer.TYPE;
    }
    if (clazz == Double.class) {
      return Double.TYPE;
    }
    if (clazz == Float.class) {
      return Float.TYPE;
    }
    if (clazz == Long.class) {
      return Long.TYPE;
    }
    if (clazz == Short.class) {
      return Short.TYPE;
    }
    if (clazz == Byte.class) {
      return Byte.TYPE;
    }
    if (clazz == Void.class) {
      return Void.TYPE;
    }
    if (clazz == Character.class) {
      return Character.TYPE;
    }
    return clazz;
  }

  public static Class<?>[] toParamTypes(Object... params) {
    Class<?>[] classes = new Class[params.length];
    for (int i = 0; i < params.length; ++i) {
      classes[i] = wrapperToPrimitive(params[i].getClass());
    }
    return classes;
  }

  @SuppressWarnings("unchecked")
  public static Enum<?> getEnum(String enumFullName) {
    String[] x = enumFullName.split("\\.(?=[^\\.]+$)");
    if (x.length == 2) {
      String enumClassName = x[0];
      String enumName = x[1];
      Class enumClass = getClass(enumClassName);
      if (enumClass.isEnum()) {
        return Enum.valueOf(enumClass, enumName);
      }
    }
    return null;
  }

  public static Class<?> getClass(String name) {
    try {
      return Class.forName(name);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  public static void setValue(Object instance, String fieldName, Object value) {
    try {
      Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(instance, value);
    } catch (IllegalAccessException
        | IllegalArgumentException
        | NoSuchFieldException
        | SecurityException e) {
    }
  }

  @SuppressWarnings("deprecation")
  public static Object getValue(Object o, String fieldName) {
    try {
      Field field = o.getClass().getDeclaredField(fieldName);
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      return field.get(o);
    } catch (IllegalAccessException
        | IllegalArgumentException
        | NoSuchFieldException
        | SecurityException e) {
      return null;
    }
  }

  public static Object callMethod(Object object, String method, Object... params) {
    try {
      Class<?> clazz = object.getClass();
      Method m = clazz.getDeclaredMethod(method, toParamTypes(params));
      m.setAccessible(true);
      return m.invoke(object, params);
    } catch (IllegalAccessException
        | IllegalArgumentException
        | NoSuchMethodException
        | SecurityException
        | InvocationTargetException e) {
      return null;
    }
  }

  @SuppressWarnings("deprecation")
  public static Method getMethod(Object o, String methodName, Class<?>... params) {
    try {
      Method method = o.getClass().getMethod(methodName, params);
      if (!method.isAccessible()) {
        method.setAccessible(true);
      }
      return method;
    } catch (NoSuchMethodException | SecurityException e) {
      return null;
    }
  }

  public static Field getField(Field field) {
    field.setAccessible(true);
    return field;
  }
}
