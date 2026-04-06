package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.ftp.KftpClient;
import com.yuckar.infra.network.ftp.KftpClientInfo;

public interface KftpClientYconf extends CacheableInfoYconf<KftpClientInfo, KftpClient> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.ftp(key());
	}

}
