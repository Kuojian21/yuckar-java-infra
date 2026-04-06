package com.yuckar.infra.crypto.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.crypto.signature.SignatureInfo;
import com.yuckar.infra.crypto.signature.SignatureSign;

public interface SignatureSignYconf extends CacheableInfoYconf<SignatureInfo, SignatureSign> {
	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.signature(key());
	}

}
