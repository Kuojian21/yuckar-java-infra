package com.yuckar.infra.base.json.config;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.annimon.stream.IntStream;
import com.yuckar.infra.base.json.ConfigUtils;

class Utils_args {

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
