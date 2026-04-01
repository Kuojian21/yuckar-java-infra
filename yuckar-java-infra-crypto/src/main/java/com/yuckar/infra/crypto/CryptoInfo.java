package com.yuckar.infra.crypto;

import com.yuckar.infra.common.executor.PoolExecutorInfoDefault;

public class CryptoInfo<T> extends PoolExecutorInfoDefault<T> {

	private String algorithm;

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
}
