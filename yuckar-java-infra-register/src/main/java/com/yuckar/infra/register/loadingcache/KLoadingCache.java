package com.yuckar.infra.register.loadingcache;

import com.google.common.cache.LoadingCache;

public interface KLoadingCache<K, V> extends LoadingCache<K, V> {

	void refresh();

}
