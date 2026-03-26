package com.yuckar.infra.register.legacy;

public interface RegisterChildListener {

	void onCreate(String ckey);

	void onRemove(String ckey);

}
