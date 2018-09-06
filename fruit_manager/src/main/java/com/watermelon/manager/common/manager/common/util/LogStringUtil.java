package com.watermelon.manager.common.manager.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class LogStringUtil {
	private static Logger log = LoggerFactory.getLogger(LogStringUtil.class);

	public static <T> T logString2Bean(Class<T> type, String value) {
		Object typeInstance;
		try {
			typeInstance = type.newInstance();
			copyProperties(value, typeInstance, new String[0]);
		} catch (Exception arg3) {
			typeInstance = null;
		}

		return (T) typeInstance;
	}

	public static void copyProperties(String source, Object target, String... ignoreFields) {
		if (source != null && !source.equals("") && target != null) {
			HashMap sourceMap = new HashMap();
			if (source.indexOf(",") > 0) {
				String[] actualEditable = source.split(",");
				String[] arg7 = actualEditable;
				int targetPd = actualEditable.length;

				for (int ignoreList = 0; ignoreList < targetPd; ++ignoreList) {
					String targetPds = arg7[ignoreList];
					if (targetPds.indexOf(":") > 0) {
						sourceMap.put(targetPds.substring(0, targetPds.indexOf(":")),
								targetPds.substring(targetPds.indexOf(":") + 1));
					}
				}
			}

			Class arg16 = target.getClass();
			PropertyDescriptor[] arg17 = BeanUtils.getPropertyDescriptors(arg16);
			List arg18 = ignoreFields != null ? Arrays.asList(ignoreFields) : null;
			PropertyDescriptor[] arg9 = arg17;
			int arg8 = arg17.length;

			for (int arg20 = 0; arg20 < arg8; ++arg20) {
				PropertyDescriptor arg19 = arg9[arg20];
				if (arg19.getWriteMethod() != null && (ignoreFields == null || !arg18.contains(arg19.getName()))
						&& sourceMap.containsKey(arg19.getName())) {
					try {
						String e = (String) sourceMap.get(arg19.getName());
						PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(target.getClass(), arg19.getName());
						Class targetType = pd.getPropertyType();
						Object value;
						if (!Integer.class.equals(targetType) && !Integer.TYPE.equals(targetType)) {
							if (!Long.class.equals(targetType) && !Long.TYPE.equals(targetType)) {
								if (!Boolean.class.equals(targetType) && !Boolean.TYPE.equals(targetType)) {
									if (Date.class.equals(targetType)) {
										value = DateUtil.string2Date(e, "yyyyMMddHHmmss");
									} else {
										value = e;
									}
								} else if (isInteger(e)) {
									value = Boolean.valueOf(Integer.parseInt(e) > 0);
								} else {
									value = Boolean.valueOf(e.equalsIgnoreCase("true"));
								}
							} else {
								value = Long.valueOf(Long.parseLong(e));
							}
						} else {
							value = Integer.valueOf(Integer.parseInt(e));
						}

						Method writeMethod = arg19.getWriteMethod();
						if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
							writeMethod.setAccessible(true);
						}

						writeMethod.invoke(target, new Object[]{value});
					} catch (Throwable arg15) {
						log.error("BeanUtil 对象复制出错:", arg15);
						throw new RuntimeException(arg15);
					}
				}
			}

		}
	}

	public static boolean isDecimal(String str) {
		if (str != null && !"".equals(str)) {
			Pattern pattern = Pattern.compile("[0-9]+(\\.?)[0-9]+");
			return pattern.matcher(str).matches();
		} else {
			return false;
		}
	}

	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		} else {
			Pattern pattern = Pattern.compile("[0-9]+");
			return pattern.matcher(str).matches();
		}
	}

	public static void main(String[] args) {
		String t = "5515";
		System.out.println(isInteger(t));
		System.out.println(isDecimal(t));
	}
}