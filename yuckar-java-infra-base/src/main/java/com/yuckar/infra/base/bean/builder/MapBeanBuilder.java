package com.yuckar.infra.base.bean.builder;

import java.util.Map;

import com.google.common.collect.Maps;

public class MapBeanBuilder<K, V> extends BeanBuilder<Map<K, V>> {

	public static <K, V> MapBeanBuilder<K, V> of() {
		return of(Maps.newLinkedHashMap());
	}

	public static <K, V> MapBeanBuilder<K, V> of(Map<K, V> bean) {
		return new MapBeanBuilder<K, V>(bean);
	}

	public MapBeanBuilder(Map<K, V> bean) {
		super(bean);
	}

	public MapBeanBuilder<K, V> put(K key, V val) {
		super.accept(bean -> bean.put(key, val));
		return this;
	}

}
