package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.jsch.JschSftp;
import com.yuckar.infra.network.jsch.JschSftpInfo;

public interface JschSftpYconf extends CacheableInfoYconf<JschSftpInfo, JschSftp> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.jsch(key());
	}

}
