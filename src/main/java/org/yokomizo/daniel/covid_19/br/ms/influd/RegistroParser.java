package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public final class RegistroParser<K> implements Function<String, Registro<K>> {
	private static final Pattern VALUES = Pattern.compile(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	private final ImmutableSet<K> keys;

	public RegistroParser(Set<K> keys) {
		checkNotNull(keys, "keys");
		this.keys = ImmutableSet.copyOf(keys);
	}

	@Override
	public Registro<K> apply(@Nullable String input) {
		checkNotNull(input, "input");
		final ImmutableMap.Builder<K, String> imb = ImmutableMap.builder();
		final Iterator<String> vs = Splitter.on(VALUES).split(input).iterator();
		for (final K k : keys) {
			checkArgument(vs.hasNext(), "registro incompleto, ausencia do campo %s: %s", k, input);
			imb.put(k, vs.next());
		}
		for (int i = 0; i < keys.size(); i++) {
		}
		checkArgument(!vs.hasNext(), "registro contem campos em excesso: %s", input);
		return new Registro<>(imb.build());
	}
}