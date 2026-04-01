package com.yuckar.infra.register;

public interface Register<V> {

	V get(String path);

	void set(String path, V value);

	void addListener(String path, RegisterListener<V> listener);

}
