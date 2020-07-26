package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Functions.identity;
import static com.google.common.collect.ImmutableBiMap.toImmutableBiMap;

import java.util.Arrays;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableBiMap;

public enum ResultadoPCR implements Predicate<String> {
	DETECTAVEL(1), NAO_DETECTAVEL(2), INCONCLUSIVO(3), NAO_REALIZADO(4), AGUARDANDO_RESULTADO(5), IGNORADO(9);
	public static final Function<ResultadoPCR, Integer> CODIGO = (ec) -> {
		return ec.codigo();
	};
	public static final ImmutableBiMap<Integer, ResultadoPCR> POR_CODIGO = Arrays.stream(values())
			.collect(toImmutableBiMap(CODIGO, identity()));

	private final int codigo;

	private ResultadoPCR(int codigo) {
		this.codigo = codigo;
	}

	public int codigo() {
		return codigo;
	}

	@Override
	public boolean apply(@Nullable String input) {
		return Integer.toString(codigo).equals(input);
	}
}
