package com.yuckar.infra.base.bean.info;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class BeanInfoHelper {

	public static BeanInfo beanInfo(Class<?> clazz) {
		return cache.getUnchecked(clazz);
	}

	private static LoadingCache<Class<?>, BeanInfo> cache = CacheBuilder.newBuilder()
			.build(new CacheLoader<Class<?>, BeanInfo>() {
				@Override
				public BeanInfo load(Class<?> clazz) throws Exception {
					return BeanInfo.of(clazz);
				}
			});

}
