package com.yuckar.infra.conf.yconfs;

import java.util.List;
import java.util.Map;

public interface YconfsGroup<V, I> extends Yconfs<V> {

	List<String> ckeys(String pkey);

	Map<String, I> cget(String pkey);

	void cset(String ckey, I value);

	void cadd(String pkey, I value);

	void caddListener(String pkey, YconfsGroupListener listener);

	void caddListener(String pkey, YconfsListener<I> listener);

}
