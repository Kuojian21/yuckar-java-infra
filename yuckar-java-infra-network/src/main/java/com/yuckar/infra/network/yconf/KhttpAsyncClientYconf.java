package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.http.KhttpAsyncClient;
import com.yuckar.infra.network.http.KhttpAsyncClientInfo;

public interface KhttpAsyncClientYconf extends CacheableInfoYconf<KhttpAsyncClientInfo, KhttpAsyncClient> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.http(key());
	}

}
