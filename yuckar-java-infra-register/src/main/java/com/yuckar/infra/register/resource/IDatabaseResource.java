package com.yuckar.infra.register.resource;

import com.yuckar.infra.register.utils.RegisterNamespaceUtils;

public interface IDatabaseResource<I, R> extends IResource<I, R> {

	String key();

	@Override
	default String path() {
		return RegisterNamespaceUtils.database(key());
	}
}