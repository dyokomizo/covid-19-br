package org.yokomizo.daniel.covid_19.br.ms.influd.v20200727;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.yokomizo.daniel.covid_19.br.ms.influd.ClassificacaoFinalCaso.COVID_19;
import static org.yokomizo.daniel.covid_19.br.ms.influd.ClassificacaoFinalCaso.SRAG_NAO_ESPECIFICADO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.EvolucaoCaso.OBITO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.CLASSI_FIN;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.EVOLUCAO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.NU_IDADE_N;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.yokomizo.daniel.covid_19.br.ms.influd.Committer;
import org.yokomizo.daniel.covid_19.br.ms.influd.Registro;
import org.yokomizo.daniel.covid_19.br.ms.influd.SaveAsFile;
import org.yokomizo.daniel.covid_19.br.ms.influd.V20200727;

import com.google.common.collect.Range;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

public class PorIdade_V20200727 implements Committer<Registro<V20200727>> {
	private final File file;
	private final SortedMultiset<Integer> covid;
	private final SortedMultiset<Integer> sragNE;

	public PorIdade_V20200727(File file) {
		checkNotNull(file, "file");
		this.file = file;
		covid = TreeMultiset.create();
		sragNE = TreeMultiset.create();
	}

	@Override
	public void accept(Registro<V20200727> r) {
		if (OBITO.apply(r.get(EVOLUCAO))) {
			if (COVID_19.apply(r.get(CLASSI_FIN))) {
				covid.add(Integer.parseInt(r.get(NU_IDADE_N)));
			} else if (SRAG_NAO_ESPECIFICADO.apply(r.get(CLASSI_FIN))) {
				sragNE.add(Integer.parseInt(r.get(NU_IDADE_N)));
			}
		}
	}

	@Override
	public void commit() {
		final File f = new SaveAsFile("IDADE").apply(file);
		try {
			try (final FileWriter fw = new FileWriter(f)) {
				fw.append("Idade,COVID-19,SRAG-NE\r\n");
				final Range<Integer> idades = idades();
				final int lower = idades.lowerEndpoint();
				final int upper = idades.upperEndpoint();
				for (int i = lower; i <= upper; i++) {
					fw.append(String.valueOf(i));
					fw.append(',').append(String.valueOf(covid.count(i)));
					fw.append(',').append(String.valueOf(sragNE.count(i)));
					fw.append("\r\n");
				}
				fw.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException("erro ao salvar " + f.getAbsolutePath(), e);
		}
	}

	private Range<Integer> idades() {
		final int lower = Math.min(covid.firstEntry().getElement(), sragNE.firstEntry().getElement());
		final int upper = Math.max(covid.lastEntry().getElement(), sragNE.lastEntry().getElement());
		return Range.closed(lower, upper);
	}
}