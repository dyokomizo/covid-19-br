package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.AbstractMap;
import java.util.Map;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.common.base.Function;

public final class NewMapEntry<K, V> implements Function<V, Map.Entry<K, V>> {
	public static final <K, V> Function<V, Map.Entry<K, V>> of(Function<V, K> function) {
		return new NewMapEntry<K, V>(function);
	}

	private final Function<V, K> function;

	public NewMapEntry(Function<V, K> function) {
		checkNotNull(function, "function");
		this.function = function;
	}

	@Override
	public Map.Entry<K, V> apply(@Nullable V input) {
		return new AbstractMap.SimpleEntry<K, V>(function.apply(input), input);
	}
}