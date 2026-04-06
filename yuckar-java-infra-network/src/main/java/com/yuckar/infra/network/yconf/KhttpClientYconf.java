package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.http.KhttpClient;
import com.yuckar.infra.network.http.KhttpClientInfo;

public interface KhttpClientYconf extends CacheableInfoYconf<KhttpClientInfo, KhttpClient> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.http(key());
	}

}
