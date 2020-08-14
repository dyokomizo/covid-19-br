package org.yokomizo.daniel.covid_19.br.ms.influd.v20200402;

import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.CLASSI_FIN;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_COLETA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_DIGITA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_ENCERRA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_ENTUTI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_EVOLUCA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_INTERNA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_NOTIFIC;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_PCR;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_RAIOX;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_SAIDUTI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_SIN_PRI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.EVOLUCAO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.PCR_RESUL;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.PCR_SARS2;

import java.io.File;

import org.yokomizo.daniel.covid_19.br.ms.influd.Committer;
import org.yokomizo.daniel.covid_19.br.ms.influd.Registro;
import org.yokomizo.daniel.covid_19.br.ms.influd.V20200402;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

public class AnalisePadraoV20200402 implements Committer<Registro<V20200402>> {
	private final Committer<Registro<V20200402>> committer;

	public AnalisePadraoV20200402(File file) {
		Preconditions.checkNotNull(file, "file");
		this.committer = new MortesOficiaisPorCovid19_V20200402(file,
				ImmutableSet.of(DT_NOTIFIC, DT_SIN_PRI, DT_INTERNA, DT_ENTUTI, DT_SAIDUTI, DT_RAIOX, DT_COLETA,
						PCR_RESUL, DT_PCR, CLASSI_FIN, EVOLUCAO, DT_EVOLUCA, DT_ENCERRA, DT_DIGITA, PCR_SARS2)) //
								.andThen(new TempoEstadia_V20200402(file));
	}

	@Override
	public void accept(Registro<V20200402> t) {
		committer.accept(t);
	}

	@Override
	public void commit() {
		committer.commit();
	}
}
