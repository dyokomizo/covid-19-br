package org.yokomizo.daniel.covid_19.br.ms.influd.v20200727;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.yokomizo.daniel.covid_19.br.ms.influd.ClassificacaoFinalCaso.COVID_19;
import static org.yokomizo.daniel.covid_19.br.ms.influd.EvolucaoCaso.OBITO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.CLASSI_FIN;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.DT_ENTUTI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.DT_SAIDUTI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.EVOLUCAO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yokomizo.daniel.covid_19.br.ms.influd.Committer;
import org.yokomizo.daniel.covid_19.br.ms.influd.Registro;
import org.yokomizo.daniel.covid_19.br.ms.influd.SaveAsFile;
import org.yokomizo.daniel.covid_19.br.ms.influd.V20200727;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

public class TempoEstadia_V20200727 implements Committer<Registro<V20200727>> {
	private static final Pattern QUOTED = Pattern.compile("^\"(.*)\"$");
	private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private final File file;
	private final SortedMultiset<Integer> multiset;

	public TempoEstadia_V20200727(File file) {
		checkNotNull(file, "file");
		this.file = file;
		multiset = TreeMultiset.create();
	}

	@Override
	public void accept(Registro<V20200727> r) {
		if (COVID_19.apply(r.get(CLASSI_FIN)) && OBITO.apply(r.get(EVOLUCAO))) {
			if (r.has(DT_ENTUTI) && r.has(DT_SAIDUTI)) {
				final LocalDate entuti = parse(r.get(DT_ENTUTI));
				final LocalDate saiuti = parse(r.get(DT_SAIDUTI));
				multiset.add((int) DAYS.between(entuti, saiuti) + 1);
			}
		}
	}

	private LocalDate parse(String s) {
		final Matcher m = QUOTED.matcher(s);
		return LocalDate.parse(m.matches() ? m.group(1) : s, DATA);
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
					fw.append(String.valueOf(i)).append(',').append(String.valueOf(multiset.count(i))).append("\r\n");
				}
				fw.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException("erro ao salvar " + f.getAbsolutePath(), e);
		}
	}
}