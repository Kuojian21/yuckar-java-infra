package com.yuckar.infra.crypto.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.crypto.cipher.CipherInfo;
import com.yuckar.infra.crypto.cipher.Encrypt;

public interface EncryptYconf extends CacheableInfoYconf<CipherInfo, Encrypt> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.cipher(key());
	}

}
