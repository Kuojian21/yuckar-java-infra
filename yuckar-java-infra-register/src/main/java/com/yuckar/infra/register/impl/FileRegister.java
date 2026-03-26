package com.yuckar.infra.register.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.lang3.StringUtils;

import com.google.common.io.Files;
import com.yuckar.infra.common.file.utils.FileUtils;
import com.yuckar.infra.register.AbstractRegister;
import com.yuckar.infra.register.utils.RegisterUtils;
import com.yuckar.infra.text.json.JsonUtils;

public class FileRegister<V> extends AbstractRegister<V> {

	private final String workspace;

	public FileRegister(Class<V> clazz) {
		this(System.getProperty("user.dir") + File.separator + "register", clazz);
	}

	public FileRegister(String workspace, Class<V> clazz) {
		super(clazz);
		this.workspace = workspace;
	}

	@Override
	public void set(String key, V value) {
		try {
			File file = new File(RegisterUtils.toFile(this.workspace, key) + File.separator + "main.json");
			FileUtils.createDirIfNoExists(file.getParentFile());
			String json = "";
			if (value == null) {

			} else {
				json = JsonUtils.toPrettyJson(value);
			}
			Files.asCharSink(file, StandardCharsets.UTF_8).write(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void init(String path) {
		File file = new File(RegisterUtils.toFile(this.workspace, path) + File.separator + "main.json");
		FileUtils.createDirIfNoExists(file.getParentFile());
		RegisterUtils.monitor(file.getParent(), new FileAlterationListenerAdaptor() {
			@Override
			public void onFileCreate(final File ifile) {
				if (file.equals(ifile)) {
					refresh(path);
				}
			}

			@Override
			public void onFileChange(final File ifile) {
				if (file.equals(ifile)) {
					refresh(path);
				}
			}
		});
	}

	@Override
	protected Object json(String path) {
		try {
			File file = new File(RegisterUtils.toFile(this.workspace, path) + File.separator + "main.json");
			if (!file.exists()) {
				return null;
			}
			String json = StringUtils.join(Files.readLines(file, StandardCharsets.UTF_8), "\n").trim();
//			if (StringUtils.isEmpty(json)) {
//				json = defString();
//			}
			if (StringUtils.isNotEmpty(json)
					&& (json.startsWith("{") && json.endsWith("}") || json.startsWith("[") && json.endsWith("]"))) {
				return JsonUtils.fromJson(json, Object.class);
			} else {
				return json;
			}
		} catch (IOException e) {
			logger.error("path:" + path, e);
			throw new RuntimeException(e);
		}
	}

}
