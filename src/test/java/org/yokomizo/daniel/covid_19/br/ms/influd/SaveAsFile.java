package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import com.google.common.base.Function;

public final class SaveAsFile implements Function<File, File> {
	private final String suffix;

	public SaveAsFile(String suffix) {
		checkNotNull(suffix, "suffix");
		this.suffix = suffix;
	}

	@Override
	public File apply(File input) {
		checkNotNull(input, "input");
		final String name = input.getName();
		final int i = name.lastIndexOf('.');
		if (i < 0) {
			return new File(input.getParentFile(), name + "-" + suffix);
		}
		return new File(input.getParentFile(), name.substring(0, i) + "-" + suffix + name.substring(i));
	}
}