package com.yuckar.infra.register;

public class RegisterEvent<D> {

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String id) {
		this.key = id;
	}

}
