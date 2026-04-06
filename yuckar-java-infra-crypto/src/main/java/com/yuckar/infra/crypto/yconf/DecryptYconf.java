package com.yuckar.infra.crypto.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.crypto.cipher.CipherInfo;
import com.yuckar.infra.crypto.cipher.Decrypt;

public interface DecryptYconf extends CacheableInfoYconf<CipherInfo, Decrypt> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.cipher(key());
	}

}
