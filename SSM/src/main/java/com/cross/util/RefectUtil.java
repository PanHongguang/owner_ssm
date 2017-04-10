package com.cross.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Java反射机制
 * @author panhongguang
 *
 */
public class RefectUtil {

	/**
	 * 生成日志实体工具
	 * 
	 * @param objectFrom
	 * @param objectTo
	 */
	public static void setLogValueModelToModel(Object objectFrom, Object objectTo) {
		Class<? extends Object> clazzFrom = objectFrom.getClass();
		Class<? extends Object> clazzTo = objectTo.getClass();

		for (Method toSetMethod : clazzTo.getMethods()) {
			String mName = toSetMethod.getName();
			if (mName.startsWith("set")) {
				// 字段名
				String field = mName.substring(3);

				// 获取from 值
				Object value;
				try {
					if ("LogId".equals(field)) {
						continue;
					}
					Method fromGetMethod = clazzFrom.getMethod("get" + field);
					value = fromGetMethod.invoke(objectFrom);

					// 设置值
					toSetMethod.invoke(objectTo, value);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
