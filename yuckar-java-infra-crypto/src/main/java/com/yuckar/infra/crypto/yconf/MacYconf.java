package com.yuckar.infra.crypto.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.crypto.mac.Mac;
import com.yuckar.infra.crypto.mac.MacInfo;

public interface MacYconf extends CacheableInfoYconf<MacInfo, Mac> {
	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.mac(key());
	}

}
