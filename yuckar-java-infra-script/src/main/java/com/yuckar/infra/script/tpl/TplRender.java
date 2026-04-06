package com.yuckar.infra.script.tpl;

import java.util.Map;

public interface TplRender {

	String render(String tpl, Map<String, Object> data);

}
