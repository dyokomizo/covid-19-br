package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public final class Tallier<K, V extends Comparable<V>> {
	private final SortedMap<K, Multiset<V>> map;

	public Tallier() {
		super();
		this.map = new TreeMap<>();
	}

	public Tallier<K, V> tally(Map.Entry<K, V> entry) {
		checkNotNull(entry, "entry");
		final K k = checkNotNull(entry.getKey(), "entry.key");
		final V v = checkNotNull(entry.getValue(), "entry.value");
		Multiset<V> m = map.get(k);
		if (m == null) {
			m = TreeMultiset.create();
			map.put(k, m);
		}
		m.add(v);
		return this;
	}

	public void saveAs(File file, @SuppressWarnings("unchecked") K... keys) throws IOException {
		checkNotNull(file, "file");
		checkNotNull(keys, "keys");
		if (keys.length == 0) {
			return;
		}
		final SortedSet<V> vs = new TreeSet<>();
		for (final K k : keys) {
			final Multiset<V> m = map.get(k);
			vs.addAll(m.elementSet());
		}
		try (final FileWriter fw = new FileWriter(
				new File(file.getParentFile(), saveAsFileName(file.getName(), "CASES")))) {
			for (final K k : keys) {
				fw.append(',').append(k.toString());
			}
			fw.append("\r\n");
			for (final V v : vs) {
				fw.append(String.valueOf(v));
				for (final K k : keys) {
					final Multiset<V> m = map.get(k);
					fw.append(',').append(String.valueOf(m.count(v)));
				}
				fw.append("\r\n");
			}
			fw.flush();
		}
	}

	public String saveAsFileName(String fileName, String suffix) {
		final int i = fileName.lastIndexOf('.');
		if (i < 0) {
			return fileName + "-" + suffix;
		}
		return fileName.substring(0, i) + "-" + suffix + fileName.substring(i);
	}
}