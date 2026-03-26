package com.yuckar.infra.register.group;

public interface GroupRegisterListener {

	void onCreate(String ckey);

	void onRemove(String ckey);

}
