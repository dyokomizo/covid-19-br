package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.yokomizo.daniel.covid_19.br.ms.influd.ClassificacaoFinalCaso.COVID_19;
import static org.yokomizo.daniel.covid_19.br.ms.influd.EvolucaoCaso.OBITO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.CLASSI_FIN;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_ENTUTI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_SAIDUTI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.EVOLUCAO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

public class TempoEstadia implements Committer<Registro> {
	private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private final File file;
	private final SortedMultiset<Integer> multiset;

	public TempoEstadia(File file) {
		checkNotNull(file, "file");
		this.file = file;
		multiset = TreeMultiset.create();
	}

	@Override
	public void accept(Registro r) {
		if (COVID_19.apply(r.get(CLASSI_FIN)) && OBITO.apply(r.get(EVOLUCAO))) {
			if (r.has(DT_ENTUTI) && r.has(DT_SAIDUTI)) {
				final LocalDate entuti = LocalDate.parse(r.get(DT_ENTUTI), DATA);
				final LocalDate saiuti = LocalDate.parse(r.get(DT_SAIDUTI), DATA);
				multiset.add((int) DAYS.between(entuti, saiuti) + 1);
			}
		}
	}

	@Override
	public void commit() {
		final File f = new SaveAsFile("ESTADIA").apply(file);
		try {
			try (final FileWriter fw = new FileWriter(f)) {
				fw.append("Dias,Casos\r\n");
				final int lower = multiset.firstEntry().getElement();
				final int upper = multiset.lastEntry().getElement();
				for (int i = lower; i <= upper; i++) {
					fw.append(String.valueOf(i)).append(',').append(String.valueOf(multiset.count(i)))
							.append("\r\n");
				}
				fw.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException("erro ao salvar " + f.getAbsolutePath(), e);
		}
	}
}