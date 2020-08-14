package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.io.Files.asCharSource;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

public final class Influ<K> implements Iterable<Registro<K>> {
	private final ImmutableSet<K> keys;
	private final File file;

	public Influ(Set<K> keys, File file) {
		checkNotNull(keys, "keys");
		checkNotNull(file, "file");
		this.keys = ImmutableSet.copyOf(keys);
		this.file = file;
	}

	@Override
	public Iterator<Registro<K>> iterator() {
		try {
			final Iterator<String> lines = asCharSource(file, Charsets.UTF_8).lines().iterator();
			checkState(lines.hasNext(), "arquivo vazio: %s", file.getAbsolutePath());
			lines.next();
			return Iterators.transform(lines, new RegistroParser<>(keys));
		} catch (IOException e) {
			throw new IllegalStateException("erro na leitura de " + file.getAbsolutePath(), e);
		}
	}

	@Override
	public String toString() {
		return file.getAbsolutePath();
	}
}
