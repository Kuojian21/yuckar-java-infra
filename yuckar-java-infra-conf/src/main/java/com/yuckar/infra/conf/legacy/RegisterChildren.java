package com.yuckar.infra.conf.legacy;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.google.common.collect.Sets;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.YconfsListener;

public class RegisterChildren<T> {

	protected final Logger logger = LoggerUtils.logger(this.getClass());

	private final Set<RegisterChildListener> listeners = Sets.newConcurrentHashSet();

	private final String pKey;
	private final LazySupplier<List<String>> childKeys;
	private final Yconfs<T> yconfs;

	public RegisterChildren(String pKey, LazySupplier<List<String>> childKeys, Yconfs<T> yconfs) {
		super();
		this.pKey = pKey;
		this.childKeys = childKeys;
		this.yconfs = yconfs;
	}

	public List<String> childKeys() {
		return this.childKeys.get();
	}

	public void set(String childKey, T info) {
		this.yconfs.set(wrapKey(childKey), info);
	}

	public T get(String childKey) {
		return this.yconfs.get(wrapKey(childKey));
	}

	public void addKey(String childKey) {
		fireAddChild(childKey);
		this.childKeys.refresh();
	}

	public void removeKey(String childKey) {
		fireRemoveChild(childKey);
		this.childKeys.refresh();
	}

	public void addListener(String childKey, YconfsListener<T> listener) {
		this.yconfs.addListener(wrapKey(childKey), listener);
		logger.info("add listener for {} ", childKey);
	}

	public void addChildListener(RegisterChildListener listener) {
		listeners.add(listener);
		logger.info("add listener for {} ", pKey);
	}

	protected void fireAddChild(String childKey) {
		listeners.forEach(listener -> {
			listener.onCreate(childKey);
		});
	}

	protected void fireRemoveChild(String childKey) {
		listeners.forEach(listener -> {
			listener.onRemove(childKey);
		});
	}

	private String wrapKey(String childKey) {
		return this.pKey + "/" + childKey;
	}

}
