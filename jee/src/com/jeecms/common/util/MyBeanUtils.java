package com.jeecms.common.util;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Locale;

import org.springframework.util.Assert;

public class MyBeanUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MyBeanUtils.class);

	/**
	 * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
	 */
	public static Object getFieldValue(final Object object,
			final String fieldName) {
		if (logger.isDebugEnabled()) {
			logger.debug("getFieldValue(Object, String) - start"); //$NON-NLS-1$
		}

		Field field = getDeclaredField(object, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");
		}

		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.error("getFieldValue(Object, String)", e); //$NON-NLS-1$

			throw new RuntimeException("never happend exception!", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getFieldValue(Object, String) - end"); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 */
	public static void setFieldValue(final Object object,
			final String fieldName, final Object value) {
		if (logger.isDebugEnabled()) {
			logger.debug("setFieldValue(Object, String, Object) - start"); //$NON-NLS-1$
		}

		Field field = getDeclaredField(object, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");
		}

		makeAccessible(field);

		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			logger.error("setFieldValue(Object, String, Object)", e); //$NON-NLS-1$

			throw new RuntimeException("never happend exception!", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setFieldValue(Object, String, Object) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	protected static Field getDeclaredField(final Object object,
			final String fieldName) {
		if (logger.isDebugEnabled()) {
			logger.debug("getDeclaredField(Object, String) - start"); //$NON-NLS-1$
		}

		Assert.notNull(object);
		Field returnField = getDeclaredField(object.getClass(), fieldName);
		if (logger.isDebugEnabled()) {
			logger.debug("getDeclaredField(Object, String) - end"); //$NON-NLS-1$
		}
		return returnField;
	}

	/**
	 * 循环向上转型,获取类的DeclaredField.
	 */
	@SuppressWarnings("unchecked")
	protected static Field getDeclaredField(final Class clazz,
			final String fieldName) {
		if (logger.isDebugEnabled()) {
			logger.debug("getDeclaredField(Class, String) - start"); //$NON-NLS-1$
		}

		Assert.notNull(clazz);
		Assert.hasText(fieldName);
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Field returnField = superClass.getDeclaredField(fieldName);
				if (logger.isDebugEnabled()) {
					logger.debug("getDeclaredField(Class, String) - end"); //$NON-NLS-1$
				}
				return returnField;
			} catch (NoSuchFieldException e) {
				logger.warn("getDeclaredField(Class, String) - exception ignored", e); //$NON-NLS-1$

				// Field不在当前类定义,继续向上转型
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getDeclaredField(Class, String) - end"); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * 强制转换fileld可访问.
	 */
	protected static void makeAccessible(final Field field) {
		if (logger.isDebugEnabled()) {
			logger.debug("makeAccessible(Field) - start"); //$NON-NLS-1$
		}

		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("makeAccessible(Field) - end"); //$NON-NLS-1$
		}
	}

	public static Object getSimpleProperty(Object bean, String propName)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		if (logger.isDebugEnabled()) {
			logger.debug("getSimpleProperty(Object, String) - start"); //$NON-NLS-1$
		}

		Object returnObject = bean.getClass().getMethod(getReadMethod(propName)).invoke(bean);
		if (logger.isDebugEnabled()) {
			logger.debug("getSimpleProperty(Object, String) - end"); //$NON-NLS-1$
		}
		return returnObject;
	}

	private static String getReadMethod(String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getReadMethod(String) - start"); //$NON-NLS-1$
		}

		String returnString = "get" + name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
		if (logger.isDebugEnabled()) {
			logger.debug("getReadMethod(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
}
