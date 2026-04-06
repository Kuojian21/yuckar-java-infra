package com.yuckar.infra.base.bean.simple;

import java.util.Objects;

public class Tuple<V1, V2> {

	public static <V1, V2> Tuple<V1, V2> tuple(V1 value1, V2 value2) {
		return new Tuple<>(value1, value2);
	}

	private final V1 value1;
	private final V2 value2;

	public Tuple(V1 value1, V2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	public V1 value1() {
		return value1;
	}

	public V2 value2() {
		return value2;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value1, value2);
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
		Tuple<V1, V2> other = (Tuple<V1, V2>) obj;
		return Objects.equals(value1, other.value1) && Objects.equals(value2, other.value2);
	}
}
