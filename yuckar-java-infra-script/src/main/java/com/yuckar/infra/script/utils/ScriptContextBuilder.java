package com.yuckar.infra.script.utils;

import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;

public class ScriptContextBuilder {

	public static ScriptContextBuilder builder() {
		return new ScriptContextBuilder();
	}

	private final ScriptContext context = new SimpleScriptContext();

	public ScriptContextBuilder setAttribute(String name, Object value) {
		context.setAttribute(name, value, ScriptContext.ENGINE_SCOPE);
		return this;
	}

	public ScriptContextBuilder setAttribute(String name, Object value, int scope) {
		context.setAttribute(name, value, scope);
		return this;
	}

	public ScriptContext build() {
		return this.context;
	}

}