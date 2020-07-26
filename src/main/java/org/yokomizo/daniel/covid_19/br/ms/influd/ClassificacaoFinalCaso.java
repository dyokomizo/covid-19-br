package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Functions.identity;
import static com.google.common.collect.ImmutableBiMap.toImmutableBiMap;

import java.util.Arrays;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableBiMap;

public enum ClassificacaoFinalCaso implements Predicate<String> {
	SRAG_POR_INFLUENZA(1), SRAG_POR_OUTRO_VIRUS_RESPIRATORIO(2), //
	SRAG_POR_OUTRO_AGENTE_ETIOLOGICO(3), SRAG_NAO_ESPECIFICADO(4), COVID_19(5);
	public static final Function<ClassificacaoFinalCaso, Integer> CODIGO = (ec) -> {
		return ec.codigo();
	};
	public static final ImmutableBiMap<Integer, ClassificacaoFinalCaso> POR_CODIGO = Arrays.stream(values())
			.collect(toImmutableBiMap(CODIGO, identity()));

	private final int codigo;

	private ClassificacaoFinalCaso(int codigo) {
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
