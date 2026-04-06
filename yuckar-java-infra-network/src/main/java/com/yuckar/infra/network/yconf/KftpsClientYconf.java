package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.ftp.KftpsClient;
import com.yuckar.infra.network.ftp.KftpsClientInfo;

public interface KftpsClientYconf extends CacheableInfoYconf<KftpsClientInfo, KftpsClient> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.ftp(key());
	}

}
