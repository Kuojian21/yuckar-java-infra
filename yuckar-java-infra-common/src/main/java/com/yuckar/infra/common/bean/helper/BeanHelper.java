package com.yuckar.infra.common.bean.helper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class BeanHelper {

	public static PropertyDescriptor[] descriptors(Class<?> clazz) {
		return cache.getUnchecked(clazz).descriptors();
	}

	public static Map<String, PropertyDescriptor> descriptor_map(Class<?> clazz) {
		return cache.getUnchecked(clazz).descriptor_map();
	}

	private static LoadingCache<Class<?>, BeanInfo> cache = CacheBuilder.newBuilder()
			.build(new CacheLoader<Class<?>, BeanInfo>() {
				@Override
				public BeanInfo load(Class<?> clazz) throws Exception {
					return new BeanInfo(clazz);
				}

			});

	static class BeanInfo {
		private final PropertyDescriptor[] descriptors;
		private final Map<String, PropertyDescriptor> descriptor_map;

		BeanInfo(Class<?> clazz) throws IntrospectionException {
			this.descriptors = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
			this.descriptor_map = Stream.of(this.descriptors)
					.collect(Collectors.toMap(PropertyDescriptor::getName, d -> d));
		}

		PropertyDescriptor[] descriptors() {
			return this.descriptors;
		}

		Map<String, PropertyDescriptor> descriptor_map() {
			return this.descriptor_map;
		}
	}

}
