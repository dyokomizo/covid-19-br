package org.yokomizo.daniel.covid_19.br.ms.influd;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.yokomizo.daniel.covid_19.br.ms.influd.ClassificacaoFinalCaso.COVID_19;
import static org.yokomizo.daniel.covid_19.br.ms.influd.EvolucaoCaso.OBITO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.CLASSI_FIN;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.EVOLUCAO;

import java.io.File;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class MortesOficiaisPorCovid19 implements Committer<Registro> {
	private final File file;
	private final ImmutableSet<NomeCampo> nomesCampos;
	private final Acumulador<NomeCampo, String> acumulador;

	public MortesOficiaisPorCovid19(File file, Iterable<NomeCampo> nomesCampos) {
		checkNotNull(file, "file");
		checkNotNull(nomesCampos, "nomesCampos");
		this.file = file;
		this.nomesCampos = ImmutableSet.copyOf(nomesCampos);
		this.acumulador = new Acumulador<>();
	}

	@Override
	public void accept(Registro r) {
		if (COVID_19.apply(r.get(CLASSI_FIN)) && OBITO.apply(r.get(EVOLUCAO))) {
			for (final NomeCampo nc : nomesCampos) {
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