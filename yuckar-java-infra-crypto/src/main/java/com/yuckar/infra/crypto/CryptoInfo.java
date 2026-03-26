package com.yuckar.infra.crypto;

import com.yuckar.infra.executor.pool.AbstractPoolExecutorInfo;

public class CryptoInfo<T> extends AbstractPoolExecutorInfo<T> {

	private String algorithm;

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
}
