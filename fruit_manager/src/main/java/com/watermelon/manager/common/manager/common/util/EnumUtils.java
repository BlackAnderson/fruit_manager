package com.watermelon.manager.common.manager.common.util;

import java.lang.reflect.Method;

public class EnumUtils {
	public static <T extends Enum<T>> T getEnum(Class<T> enumClass, String name) {
		return Enum.valueOf(enumClass, name);
	}

	public static <T extends Enum<T>> T getEnum(Class<T> enumClass, int value) {
		try {
			if (value < 0) {
				return null;
			} else {
				Method e = enumClass.getMethod("values", new Class[0]);
				Enum[] values = (Enum[]) e.invoke(enumClass, new Object[0]);
				return values.length > value ? (T) values[value] : null;
			}
		} catch (Exception arg3) {
			throw new RuntimeException(arg3);
		}
	}
}