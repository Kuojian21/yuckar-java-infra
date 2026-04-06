package com.yuckar.infra.conf.yconfs.context;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.yuckar.infra.conf.yconfs.Yconfs;

public abstract class AbstractYconfsContext implements YconfsContext {

	private final ConcurrentMap<Class<?>, Yconfs<?>> yconfsMap = Maps.newConcurrentMap();

	@SuppressWarnings("unchecked")
	@Override
	public final <I> Yconfs<I> getYconfs(Class<I> clazz) {
		return (Yconfs<I>) yconfsMap.computeIfAbsent(clazz, cl -> newYconfs(clazz));
	}

	public abstract <I> Yconfs<I> newYconfs(Class<I> clazz);

}
