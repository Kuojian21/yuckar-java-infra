package com.yuckar.infra.register.utils;

public class RegisterNamespaceUtils {

	public static final String namespace_config = "/config";
	public static final String namespace_loadingcache = "/loadingcache";
	public static final String namespace_storage_database = "/storage/database";
	public static final String namespace_runner_binlog = "/runner/binlog";
	public static final String namespace_runner_kafka = "/runner/kafka";
	public static final String namespace_runner_rocket = "/runner/rocket";
	public static final String namespace_runner_grpc = "/runner/grpc";

	public static String config(String key) {
		return wrap(namespace_config, key);
	}

	public static String loadingcache(String key) {
		return wrap(namespace_loadingcache, key);
	}

	public static String database(String key) {
		return wrap(namespace_storage_database, key);
	}

	public static String binlog(String key) {
		return wrap(namespace_runner_binlog, key);
	}

	public static String kafka(String key) {
		return wrap(namespace_runner_kafka, key);
	}

	public static String rocket(String key) {
		return wrap(namespace_runner_rocket, key);
	}

	public static String grpc(String key) {
		return wrap(namespace_runner_grpc, key);
	}

	static String wrap(String namespace, String key) {
		if (key.startsWith("/")) {
			return key;
		} else {
			return namespace + "/" + key;
		}
	}

}
