package com.yuckar.infra.conf.loadingcache;

import com.google.common.cache.LoadingCache;

public interface KLoadingCache<K, V> extends LoadingCache<K, V> {

	void refresh();

}
