package com.yuckar.infra.base.utils;

import com.annimon.stream.Optional;

public class ClassUtils {

	public static String simple_name(Class<?> clazz) {
		return Optional.of(clazz.getName()).map(n -> n.substring(n.lastIndexOf(".") + 1)).orElse("");
	}

	public static <T> T instantiate(Class<T> clazz) {
		return RunUtils.throwing(() -> clazz.getDeclaredConstructor(new Class<?>[] {}).newInstance(new Object[] {}));
	}

}
