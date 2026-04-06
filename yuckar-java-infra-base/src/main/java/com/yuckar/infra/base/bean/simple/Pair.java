package com.yuckar.infra.base.bean.simple;

import java.util.Objects;

public class Pair<K, V> {

	public static <K, V> Pair<K, V> pair(K key, V value) {
		return new Pair<K, V>(key, value);
	}

	private final K key;
	private final V value;

	public Pair(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair<K, V> other = (Pair<K, V>) obj;
		return Objects.equals(key, other.key) && Objects.equals(value, other.value);
	}

}
