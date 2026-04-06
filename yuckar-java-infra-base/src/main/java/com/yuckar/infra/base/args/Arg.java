package com.yuckar.infra.base.args;

import org.apache.commons.lang3.StringUtils;

public class Arg {

	public static Arg of(String key) {
		return of(key, null);
	}

	public static Arg of(String key, String value) {
		return new Arg(key, value);
	}

	private final String key;
	private final String value;
	private final String option;

	private Arg(String key, String value) {
		this.key = key;
		this.value = value;
		this.option = key.replaceAll("^--", "").replaceAll("^-", "");
	}

	public String key() {
		return key;
	}

	public String option() {
		return option;
	}

	public String value() {
		return value;
	}

	public boolean hasValues() {
		return StringUtils.isNotEmpty(this.value);
	}

}