package com.yuckar.infra.register.resource;

import com.yuckar.infra.register.utils.RegisterNamespaceUtils;

public interface IConfigResource<I, R> extends IResource<I, R> {

	String key();

	@Override
	default String path() {
		return RegisterNamespaceUtils.config(key());
	}
}
