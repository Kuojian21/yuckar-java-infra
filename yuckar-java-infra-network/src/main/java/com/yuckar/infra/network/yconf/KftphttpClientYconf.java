package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.ftp.KftphttpClient;
import com.yuckar.infra.network.ftp.KftphttpClientInfo;

public interface KftphttpClientYconf extends CacheableInfoYconf<KftphttpClientInfo, KftphttpClient> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.ftp(key());
	}

}
