package com.yuckar.infra.crypto.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.crypto.digest.Digest;
import com.yuckar.infra.crypto.digest.DigestInfo;

public interface DigestYconf extends CacheableInfoYconf<DigestInfo, Digest> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.digest(key());
	}

}
