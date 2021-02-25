package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yokomizo.daniel.covid_19.br.ms.influd.v20200402.AnalisePadraoV20200402;
import org.yokomizo.daniel.covid_19.br.ms.influd.v20200727.AnalisePadraoV20200727;
import org.yokomizo.daniel.covid_19.br.ms.influd.v20200727.PorIdade_V20200727;
import org.yokomizo.daniel.covid_19.br.ms.influd.v20200727.TempoEstadia_V20200727;

public class AnaliseInfluMain {
	private static final Pattern NOME = Pattern.compile("^INFLUD(?:21)?-(\\d\\d-\\d\\d-\\d\\d\\d\\d).csv$");
	private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private static final LocalDate DATA_NOVO_LEIAUTE = LocalDate.of(2020, 7, 27);

	public static void main(String[] args) {
		for (String a : args) {
			if (a.startsWith("--")) {
				return;
			}
			System.out.println(a);
			System.out.println(System.currentTimeMillis());
			final File f = new File(a);
			final Matcher m = NOME.matcher(f.getName());
			checkArgument(m.matches(), "Nome invalido %s", f.getAbsolutePath());
			final LocalDate ld = LocalDate.parse(m.group(1), DATA);
			if (ld.isBefore(DATA_NOVO_LEIAUTE)) {
				final Committer<Registro<V20200402>> c = new AnalisePadraoV20200402(f);
				for (final Registro<V20200402> r : new Influ<>(V20200402.BY_ORDINAL.values(), f)) {
					c.accept(r);
				}
				c.commit();
			} else {
				final Committer<Registro<V20200727>> c = new AnalisePadraoV20200727(f) //
						.andThen(new TempoEstadia_V20200727(f)) //
						.andThen(new PorIdade_V20200727(f));
				for (final Registro<V20200727> r : new Influ<>(V20200727.BY_ORDINAL.values(), f)) {
					c.accept(r);
				}
				c.commit();
			}
			System.out.println(System.currentTimeMillis());
		}
	}
}
