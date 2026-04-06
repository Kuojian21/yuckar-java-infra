package com.yuckar.infra.conf.info;

import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;

public interface DatabaseYconf<I, R> extends CacheableYconf<I, R> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.database(key());
	}
}