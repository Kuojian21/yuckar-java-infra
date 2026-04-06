package com.yuckar.infra.base.args;

import java.util.List;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.google.common.collect.Lists;
import com.yuckar.infra.base.lazy.LazySupplier;

public class Args {

	public static Args of(String[] args) {
		return new Args(args);
	}

	private final String[] args;
	private final LazySupplier<List<Arg>> objs;

	private Args(String[] args) {
		this.args = args;
		this.objs = LazySupplier.wrap(() -> {
			List<Arg> vals = Lists.newArrayList();
			for (int i = 0; i < args.length; i++) {
				if (args[i].startsWith("-")) {
					if (i + 1 == args.length || args[i + 1].startsWith("-")) {
						vals.add(Arg.of(args[i]));
					} else {
						vals.add(Arg.of(args[i], args[i + 1]));
						i++;
					}
				} else if (args[i].indexOf("=") > 0 && args[i].indexOf("=") < args[i].length()) {
					vals.add(Arg.of(args[i].substring(0, args[i].indexOf("=")),
							args[i].substring(args[i].indexOf("=") + 1)));
				} else {
					vals.add(Arg.of("", args[i]));
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

	public Optional<String> value(String option) {
		return values(option).findFirst();
	}

	public Stream<String> values(String option) {
		return args(option).map(Arg::value);
	}

	public Optional<Arg> arg(String option) {
		return args(option).findFirst();
	}

	public Stream<Arg> args(String option) {
		return Stream.of(this.objs.get()).filter(v -> v.option().equals(option));
	}

	public String[] prefix(String prefix) {
		return Stream.of(this.objs.get()).filter(v -> v.option().startsWith(prefix))
				.flatMap(v -> v.value() == null ? Stream.of(v.key()) : Stream.of(v.key(), v.value()))
				.toArray(i -> new String[i]);
	}

}
