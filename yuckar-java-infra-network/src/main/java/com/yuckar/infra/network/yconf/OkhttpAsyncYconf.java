package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.okhttp.OkhttpAsync;
import com.yuckar.infra.network.okhttp.OkhttpInfo;

public interface OkhttpAsyncYconf extends CacheableInfoYconf<OkhttpInfo, OkhttpAsync> {
	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.okhttp(key());
	}

}
