package com.yuckar.infra.base.json.value;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.annimon.stream.IntStream;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yuckar.infra.base.json.ConfigUtils;
import com.yuckar.infra.base.json.JsonUtils;
import com.yuckar.infra.base.json.config.ConstructorConfig;
import com.yuckar.infra.base.json.config.MethodConfig;
import com.yuckar.infra.base.logger.LoggerUtils;

class Utils_invoke {

	private static final Logger logger = LoggerUtils.logger(Utils_invoke.class);

	public static Object invokeList(Object obj, List<?> list) {
		try {
			List<Object> objs = Lists.newArrayList();
			objs.add(obj);
			int j = 0;
			while (j + 2 < list.size()) {
				Class<?> clazz = Class.forName((String) list.get(j));
				String sign = (String) list.get(j + 1);
				if (sign.startsWith("(")) {
					objs.add(invoke(obj, new ConstructorConfig(Maps.newHashMap()).element(clazz, sign),
							list.get(j + 2)));
				} else {
					Method method = new MethodConfig(Maps.newHashMap()).element(clazz, sign);
					if (Modifier.isStatic(method.getModifiers())) {
						objs.add(invoke(null, method, list.get(j + 2)));
					} else {
						objs.add(invoke(objs.get((int) list.get(j + 3)), method, list.get(j + 2)));
						j++;
					}
				}
				j += 3;
			}
			return j < list.size() ? objs.get((int) list.get(j)) : objs.get(objs.size() - 1);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			logger.error("{}", JsonUtils.toPrettyJson(list), e);
			throw new RuntimeException(e);
		}
	}

	public static Object invoke(Object obj, Method method, Object param)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException,
			InstantiationException, NoSuchMethodException, SecurityException {
		return method.invoke(obj, args(obj, method.getParameterTypes(), param, Maps.newHashMap()));
	}

	public static Object invoke(Object obj, Constructor<?> constructor, Object param)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			ClassNotFoundException, NoSuchMethodException, SecurityException {
		return constructor.newInstance(args(obj, constructor.getParameterTypes(), param, Maps.newHashMap()));
	}

	public static Object[] args(Object obj, Type[] types, Object param, Map<Type, Type> mapper) {
		Object[] args = new Object[types.length];
		if (types.length == 0) {

		} else if (types.length == 1) {
			args[0] = param;
		} else {
			if (param != null) {
				IntStream.range(0, types.length).forEach(i -> {
					args[i] = ((List<?>) param).get(i);
				});
			}
		}
		for (int i = 0; i < types.length; i++) {
			Type type = types[i];
			Object arg = args[i];
			args[i] = ConfigUtils.valueUnchecked(arg, type, mapper);
		}
		return args;
	}

}
