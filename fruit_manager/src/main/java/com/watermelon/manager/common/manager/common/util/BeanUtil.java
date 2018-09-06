package com.watermelon.manager.common.manager.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class BeanUtil {
	private static Logger log = LoggerFactory.getLogger(BeanUtil.class);
	private static BeanUtilsBean beanUtilsBean;

	static {
		if (beanUtilsBean == null) {
			beanUtilsBean = BeanUtilsBean.getInstance();
		}

	}

	public static void copyProperties(Object source, Object target, String... ignoreFields) {
		if (source != null && target != null) {
			Class actualEditable = target.getClass();
			PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
			List ignoreList = ignoreFields != null ? Arrays.asList(ignoreFields) : null;
			PropertyDescriptor[] arg8 = targetPds;
			int arg7 = targetPds.length;

			for (int arg6 = 0; arg6 < arg7; ++arg6) {
				PropertyDescriptor targetPd = arg8[arg6];
				if (targetPd.getWriteMethod() != null
						&& (ignoreFields == null || !ignoreList.contains(targetPd.getName()))) {
					PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(),
							targetPd.getName());
					if (sourcePd != null && sourcePd.getReadMethod() != null) {
						try {
							Method e = sourcePd.getReadMethod();
							if (!Modifier.isPublic(e.getDeclaringClass().getModifiers())) {
								e.setAccessible(true);
							}

							Object value = e.invoke(source, new Object[0]);
							Class sourceType = sourcePd.getPropertyType();
							PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(target.getClass(),
									targetPd.getName());
							Class targetType = pd.getPropertyType();
							if (sourceType.isEnum()
									&& (Integer.class.equals(targetType) || Integer.TYPE.equals(targetType))) {
								if (value == null) {
									value = Integer.valueOf(0);
								} else {
									value = Integer.valueOf(Enum.valueOf(sourceType, String.valueOf(value)).ordinal());
								}
							} else if (targetType.isEnum()
									&& (Integer.class.equals(sourceType) || Integer.TYPE.equals(sourceType))) {
								if (value == null) {
									value = Integer.valueOf(0);
								}

								int writeMethod = ((Integer) value).intValue();
								Method method = targetType.getMethod("values", new Class[0]);
								Object[] enumValues = (Object[]) method.invoke(targetType, new Object[0]);
								if (writeMethod < 0 || writeMethod >= enumValues.length) {
									continue;
								}

								value = enumValues[writeMethod];
							}

							if (String.class.equals(sourceType) && Number.class.isAssignableFrom(targetType)) {
								Constructor arg19 = targetType.getConstructor(new Class[]{String.class});
								value = arg19.newInstance(new Object[]{String.valueOf(value)});
							} else if (String.class.equals(targetType) && Number.class.isAssignableFrom(sourceType)) {
								value = String.valueOf(value);
							}

							if ((Boolean.class.equals(sourceType) || Boolean.TYPE.equals(sourceType))
									&& (Integer.class.equals(targetType) || Integer.TYPE.equals(targetType))) {
								value = Integer.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
							} else if ((Boolean.class.equals(targetType) || Boolean.TYPE.equals(targetType))
									&& (Integer.class.equals(sourceType) || Integer.TYPE.equals(sourceType))) {
								value = Boolean.valueOf(((Integer) value).intValue() > 0);
							}

							Method arg20 = targetPd.getWriteMethod();
							if (!Modifier.isPublic(arg20.getDeclaringClass().getModifiers())) {
								arg20.setAccessible(true);
							}

							arg20.invoke(target, new Object[]{value});
						} catch (Throwable arg18) {
							log.error("BeanUtil 对象复制出错:", arg18);
							throw new RuntimeException(arg18);
						}
					}
				}
			}

		}
	}

	public static Object cloneBean(Object bean) {
		try {
			return beanUtilsBean.cloneBean(bean);
		} catch (Throwable arg1) {
			log.error("BeanUtil 对象克隆出错:", arg1);
			throw new RuntimeException(arg1);
		}
	}

	public static void copyProperty(Object bean, String name, Object value) {
		try {
			beanUtilsBean.copyProperty(bean, name, value);
		} catch (Throwable arg3) {
			log.error("BeanUtil 对象属性赋值出错:", arg3);
			throw new RuntimeException(arg3);
		}
	}

	public static Map describe(Object bean) {
		try {
			return beanUtilsBean.describe(bean);
		} catch (Throwable arg1) {
			log.error("BeanUtil 对象克隆出错:", arg1);
			throw new RuntimeException(arg1);
		}
	}

	public static Map buildMap(Object bean) {
		if (bean == null) {
			return null;
		} else {
			try {
				Map e = describe(bean);
				PropertyDescriptor[] pds = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(bean);
				PropertyDescriptor[] arg5 = pds;
				int arg4 = pds.length;

				for (int arg3 = 0; arg3 < arg4; ++arg3) {
					PropertyDescriptor pd = arg5[arg3];
					Class type = pd.getPropertyType();
					if (type.isEnum()) {
						Object value = beanUtilsBean.getPropertyUtils().getSimpleProperty(bean, pd.getName());
						int intValue = 0;
						if (value != null) {
							intValue = Enum.valueOf(type, String.valueOf(value)).ordinal();
						}

						e.put(pd.getName(), Integer.valueOf(intValue));
					}
				}

				return e;
			} catch (Throwable arg9) {
				log.error("BeanUtil 创建Map失败:", arg9);
				throw new RuntimeException(arg9);
			}
		}
	}

	public static Object buildBean(Map map, Class clazz) {
		if (map == null) {
			return null;
		} else {
			Object bean = null;

			try {
				bean = clazz.newInstance();
				PropertyDescriptor[] e = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(clazz);
				PropertyDescriptor[] arg6 = e;
				int arg5 = e.length;

				for (int arg4 = 0; arg4 < arg5; ++arg4) {
					PropertyDescriptor pd = arg6[arg4];
					String fieldName = pd.getName();
					if (map.containsKey(fieldName)) {
						Object mapValue = map.get(fieldName);
						Class beanType = pd.getPropertyType();
						Object beanValue = mapValue;
						if (beanType.isEnum()) {
							if (mapValue != null) {
								int e1;
								Method method;
								Object[] enumValues;
								if (mapValue instanceof String) {
									if (String.valueOf(mapValue).matches("\\d+")) {
										Integer arg17 = Integer.valueOf(Integer.parseInt(String.valueOf(mapValue)));
										e1 = ((Integer) arg17).intValue();
										method = beanType.getMethod("values", new Class[0]);
										enumValues = (Object[]) method.invoke(beanType, new Object[0]);
										if (e1 < 0 || e1 >= enumValues.length) {
											continue;
										}

										beanValue = enumValues[e1];
									} else {
										try {
											beanValue = Enum.valueOf(beanType, String.valueOf(mapValue));
										} catch (IllegalArgumentException arg15) {
											continue;
										}
									}
								} else if (mapValue instanceof Integer) {
									e1 = ((Integer) mapValue).intValue();
									method = beanType.getMethod("values", new Class[0]);
									enumValues = (Object[]) method.invoke(beanType, new Object[0]);
									if (e1 < 0 || e1 >= enumValues.length) {
										continue;
									}

									beanValue = enumValues[e1];
								}
							}
						} else if (beanType.equals(Date.class) && mapValue != null && mapValue instanceof String) {
							try {
								SimpleDateFormat arg18 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								beanValue = arg18.parse(String.valueOf(mapValue));
							} catch (ParseException arg14) {
								log.error("BeanUtil buildBean string 转 Date 出错!");
								continue;
							}
						}

						beanUtilsBean.copyProperty(bean, fieldName, beanValue);
					}
				}

				return bean;
			} catch (Throwable arg16) {
				log.error("BeanUtil 根据map创建bean出错:", arg16);
				throw new RuntimeException(arg16);
			}
		}
	}

	public static void setProperty(Object bean, String name, Object value) {
		try {
			beanUtilsBean.setProperty(bean, name, value);
		} catch (Throwable arg3) {
			log.error("BeanUtil 给对象属性赋值出错:", arg3);
			throw new RuntimeException(arg3);
		}
	}

	public static Object getProperty(Object bean, String name) {
		try {
			return beanUtilsBean.getPropertyUtils().getSimpleProperty(bean, name);
		} catch (Throwable arg2) {
			log.error("BeanUtil 获取对象属性值出错:", arg2);
			throw new RuntimeException(arg2);
		}
	}

	public static <T, V> T createBean(Class<T> type, V value) {
		Object typeInstance;
		try {
			typeInstance = type.newInstance();
			copyProperties(value, typeInstance, new String[0]);
		} catch (Exception arg3) {
			typeInstance = null;
		}

		return (T) typeInstance;
	}
}