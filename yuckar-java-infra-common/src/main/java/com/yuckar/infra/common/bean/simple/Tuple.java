package com.yuckar.infra.common.bean.simple;

import java.util.Objects;

public class Tuple<T1, T2> {

	public static <T1, T2> Tuple<T1, T2> tuple(T1 t1, T2 t2) {
		Tuple<T1, T2> tuple = new Tuple<>();
		tuple.setT1(t1);
		tuple.setT2(t2);
		return tuple;
	}

	

	private T1 t1;
	private T2 t2;

	public T1 getT1() {
		return t1;
	}

	public void setT1(T1 t1) {
		this.t1 = t1;
	}

	public T2 getT2() {
		return t2;
	}

	public void setT2(T2 t2) {
		this.t2 = t2;
	}

	@Override
	public int hashCode() {
		return Objects.hash(t1, t2);
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
		Tuple<T1, T2> other = (Tuple<T1, T2>) obj;
		return Objects.equals(t1, other.t1) && Objects.equals(t2, other.t2);
	}
}
