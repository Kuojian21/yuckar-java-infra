package com.yuckar.infra.conf.yconfs.utils;

public class YconfsNamespaceUtils {

	public static final String namespace_common = "/common";
	public static final String namespace_loadingcache = "/loadingcache";
	public static final String namespace_storage_database = "/storage/database";

	public static final String namespace_runner_binlog = "/runner/binlog";
	public static final String namespace_runner_kafka = "/runner/kafka";
	public static final String namespace_runner_rocket = "/runner/rocket";
	public static final String namespace_runner_grpc = "/runner/grpc";

	public static final String namespace_network_ftp = "/network/ftp";
	public static final String namespace_network_http = "/network/http";
	public static final String namespace_network_jsch = "/network/jsch";
	public static final String namespace_network_mail = "/network/mail";
	public static final String namespace_network_okhttp = "/network/okhttp";
	public static final String namespace_network_web_browser = "/network/web_browser";
	public static final String namespace_network_web_capture = "/network/web_capture";

	public static final String namespace_crypto_cipher = "/crypto/cipher";
	public static final String namespace_crypto_digest = "/crypto/digest";
	public static final String namespace_crypto_mac = "/crypto/mac";
	public static final String namespace_crypto_signature = "/crypto/signature";

	public static String common(String key) {
		return wrap(namespace_common, key);
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

	public static String ftp(String key) {
		return wrap(namespace_network_ftp, key);
	}

	public static String http(String key) {
		return wrap(namespace_network_http, key);
	}

	public static String jsch(String key) {
		return wrap(namespace_network_jsch, key);
	}

	public static String mail(String key) {
		return wrap(namespace_network_mail, key);
	}

	public static String okhttp(String key) {
		return wrap(namespace_network_okhttp, key);
	}

	public static String web_browser(String key) {
		return wrap(namespace_network_web_browser, key);
	}

	public static String web_capture(String key) {
		return wrap(namespace_network_web_capture, key);
	}

	public static String cipher(String key) {
		return wrap(namespace_crypto_cipher, key);
	}

	public static String digest(String key) {
		return wrap(namespace_crypto_digest, key);
	}

	public static String mac(String key) {
		return wrap(namespace_crypto_mac, key);
	}

	public static String signature(String key) {
		return wrap(namespace_crypto_signature, key);
	}

	static String wrap(String namespace, String key) {
		if (key.startsWith("/")) {
			return key;
		} else {
			return namespace + "/" + key;
		}
	}

}
