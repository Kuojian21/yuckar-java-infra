package com.yuckar.infra.common.args;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.google.common.collect.Lists;
import com.yuckar.infra.common.lazy.LazySupplier;
import com.yuckar.infra.common.utils.RunUtils;

public class Args {

	public static Args of(String[] args) {
		return new Args(args);
	}

	private final String[] args;
	private final LazySupplier<List<Value>> vals;

	private Args(String[] args) {
		this.args = args;
		this.vals = LazySupplier.wrap(() -> {
			List<Value> vals = Lists.newArrayList();
			for (int i = 0; i < args.length; i++) {
				if (args[i].startsWith("-")) {
					if (i + 1 == args.length || args[i + 1].startsWith("-")) {
						vals.add(Value.of(args[i]));
					} else {
						vals.add(Value.of(args[i], args[i + 1]));
						i++;
					}
				} else if (args[i].indexOf("=") > 0 && args[i].indexOf("=") < args[i].length()) {
					vals.add(Value.of(args[i].substring(0, args[i].indexOf("=")),
							args[i].substring(args[i].indexOf("=") + 1)));
				} else {
					vals.add(Value.of("", args[i]));
				}
			}
			return vals;
		});
	}

	public String[] args() {
		return args;
	}

	public String args(int idx) {
		return args[idx];
	}

	public int args_length() {
		return args.length;
	}

	public Optional<String> option(String option) {
		return options(option).findFirst();
	}

	public Stream<String> options(String option) {
		return Stream.of(this.vals.get()).filter(v -> v.option().equals(option)).map(Value::value);
	}

	public CommandLine options(String prefix, Options options) {
		return RunUtils.throwing(() -> {
			return new DefaultParser().parse(options,
					Stream.of(this.vals.get()).filter(v -> v.option().startsWith(prefix))
							.flatMap(v -> Stream.of(v.key(), v.value())).toArray(i -> new String[i]),
					true);
		});
	}

	static class Value {

		public static Value of(String key) {
			return of(key, null);
		}

		public static Value of(String key, String value) {
			return new Value(key, value);
		}

		private final String key;
		private final String value;
		private final String option;

		private Value(String key, String value) {
			this.key = key;
			this.value = value;
			this.option = key.replaceAll("^--", "").replaceAll("^-", "");
		}

		public String key() {
			return key;
		}

		public String option() {
			return option;
		}

		public String value() {
			return value;
		}

		public boolean hasValues() {
			return StringUtils.isNotEmpty(this.value);
		}

	}

}
