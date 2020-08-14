package org.yokomizo.daniel.covid_19.br.ms.influd.v20200402;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.yokomizo.daniel.covid_19.br.ms.influd.ClassificacaoFinalCaso.COVID_19;
import static org.yokomizo.daniel.covid_19.br.ms.influd.EvolucaoCaso.OBITO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.CLASSI_FIN;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.EVOLUCAO;

import java.io.File;

import org.yokomizo.daniel.covid_19.br.ms.influd.Acumulador;
import org.yokomizo.daniel.covid_19.br.ms.influd.Committer;
import org.yokomizo.daniel.covid_19.br.ms.influd.Registro;
import org.yokomizo.daniel.covid_19.br.ms.influd.SaveAsFile;
import org.yokomizo.daniel.covid_19.br.ms.influd.V20200402;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class MortesOficiaisPorCovid19_V20200402 implements Committer<Registro<V20200402>> {
	private final File file;
	private final ImmutableSet<V20200402> nomesCampos;
	private final Acumulador<V20200402, String> acumulador;

	public MortesOficiaisPorCovid19_V20200402(File file, Iterable<V20200402> nomesCampos) {
		checkNotNull(file, "file");
		checkNotNull(nomesCampos, "nomesCampos");
		this.file = file;
		this.nomesCampos = ImmutableSet.copyOf(nomesCampos);
		this.acumulador = new Acumulador<>();
	}

	@Override
	public void accept(Registro<V20200402> r) {
		if (COVID_19.apply(r.get(CLASSI_FIN)) && OBITO.apply(r.get(EVOLUCAO))) {
			for (final V20200402 nc : nomesCampos) {
				acumulador.acumula(r.toEntry(nc));
			}
		}
	}

	@Override
	public void commit() {
		acumulador.salvarComo(new SaveAsFile("CASES").apply(file), Sets.filter(nomesCampos, (nc) -> {
			return nc.name().startsWith("DT_");
		}));
	}
}