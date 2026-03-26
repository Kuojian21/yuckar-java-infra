package com.yuckar.infra.register.group;

import java.util.List;
import java.util.Map;

import com.yuckar.infra.register.Register;
import com.yuckar.infra.register.RegisterListener;

public interface GroupRegister<V, I> extends Register<V> {

	List<String> ckeys(String pkey);

	Map<String, I> cget(String pkey);

	void cset(String ckey, I value);

	void cadd(String pkey, I value);

	void caddListener(String pkey, GroupRegisterListener listener);

	void caddListener(String pkey, RegisterListener<I> listener);

}
