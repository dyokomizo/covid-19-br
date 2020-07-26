package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

public enum RegistroParser implements Function<String, Registro> {
	INSTANCE;
	private static final Pattern VALUES = Pattern.compile(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

	@Override
	public Registro apply(@Nullable String input) {
		checkNotNull(input, "input");
		final ImmutableMap.Builder<NomeCampo, String> imb = ImmutableMap.builder();
		final NomeCampo[] ks = NomeCampo.values();
		final Iterator<String> vs = Splitter.on(VALUES).split(input).iterator();
		for (int i = 0; i < ks.length; i++) {
			checkArgument(vs.hasNext(), "registro incompleto, ausencia do campo %s: %s", ks[i], input);
			imb.put(ks[i], vs.next());
		}
		checkArgument(!vs.hasNext(), "registro contem campos em excesso: %s", input);
		return new Registro(imb.build());
	}
}