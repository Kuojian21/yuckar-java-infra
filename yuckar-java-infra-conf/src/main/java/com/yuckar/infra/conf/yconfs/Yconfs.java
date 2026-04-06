package com.yuckar.infra.conf.yconfs;

public interface Yconfs<V> {

	V get(String path);

	void set(String path, V value);

	void addListener(String path, YconfsListener<V> listener);

}
