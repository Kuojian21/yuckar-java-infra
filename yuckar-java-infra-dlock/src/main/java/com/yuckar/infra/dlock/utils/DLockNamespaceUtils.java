package com.yuckar.infra.dlock.utils;

public class DLockNamespaceUtils {

	public static String runner(String key) {
		return wrap("/runner", key);
	}

	static String wrap(String namespace, String key) {
		if (key.startsWith("/")) {
			return key;
		} else {
			return "/dlock" + namespace + "/" + key;
		}
	}

}
