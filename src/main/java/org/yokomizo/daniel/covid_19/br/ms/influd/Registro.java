package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class Registro<K> implements Serializable {
	private static final long serialVersionUID = -2209725480724054170L;
	private final Map<K, String> campos;

	public Registro(Map<K, String> campos) {
		this.campos = checkNotNull(campos, "campos");
	}

	public boolean has(K nomeCampo) {
		checkNotNull(nomeCampo, "nomeCampo");
		final String s = campos.get(nomeCampo);
		checkArgument(s != null, nomeCampo + " ausente");
		return !s.isEmpty();
	}

	public String get(K nomeCampo) {
		checkNotNull(nomeCampo, "nomeCampo");
		final String s = campos.get(nomeCampo);
		checkArgument(s != null, nomeCampo + " ausente");
		return s;
	}

	public Map.Entry<K, String> toEntry(K nomeCampo) {
		checkNotNull(nomeCampo, "nomeCampo");
		return new AbstractMap.SimpleEntry<K, String>(nomeCampo, get(nomeCampo));
	}

	@Override
	public boolean equals(Object o) {
		return (this == o) || ((o instanceof Registro<?>) && campos.equals(((Registro<?>) o).campos));
	}

	@Override
	public int hashCode() {
		return 0x62a7e9b3 ^ campos.hashCode();
	}

	@Override
	public String toString() {
		return campos.toString();
	}
}
