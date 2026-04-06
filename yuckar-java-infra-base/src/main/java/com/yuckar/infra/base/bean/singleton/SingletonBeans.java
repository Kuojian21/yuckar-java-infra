package com.yuckar.infra.base.bean.singleton;

import java.util.List;
import java.util.Map;

import com.annimon.stream.Stream;
import com.google.common.collect.Maps;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.utils.RunUtils;

@SuppressWarnings("unchecked")
public class SingletonBeans {

	private static final Map<Class<?>, LazySupplier<Object>> repo = Maps.newConcurrentMap();

	public static <T> T bean(Class<T> clazz) {
		return (T) repo.computeIfAbsent(clazz, k -> LazySupplier.wrap(() -> {
			return RunUtils.throwing(() -> {
				return clazz.getConstructor(new Class<?>[] {}).newInstance(new Object[] {});
			});
		})).get();
	}

	public static <T> List<T> beans(Class<T> clazz) {
		return Stream.of(repo).filter(e -> clazz.isAssignableFrom(e.getKey())).map(Map.Entry::getValue)
				.map(LazySupplier::get).map(o -> (T) o).toList();
	}

}
