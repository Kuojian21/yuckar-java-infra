package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.okhttp.OkhttpInfo;
import com.yuckar.infra.network.okhttp.OkhttpSync;

public interface OkhttpSyncYconf extends CacheableInfoYconf<OkhttpInfo, OkhttpSync> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.okhttp(key());
	}

}
