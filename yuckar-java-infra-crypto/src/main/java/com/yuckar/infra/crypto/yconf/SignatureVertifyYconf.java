package com.yuckar.infra.crypto.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.crypto.signature.SignatureInfo;
import com.yuckar.infra.crypto.signature.SignatureVertify;

public interface SignatureVertifyYconf extends CacheableInfoYconf<SignatureInfo, SignatureVertify> {
	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.signature(key());
	}

}
