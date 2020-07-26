package org.yokomizo.daniel.covid_19.br.ms.influd;

import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.CLASSI_FIN;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_COLETA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_DIGITA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_ENCERRA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_ENTUTI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_EVOLUCA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_INTERNA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_NOTIFIC;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_PCR;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_RAIOX;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_SAIDUTI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.DT_SIN_PRI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.EVOLUCAO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.PCR_RESUL;
import static org.yokomizo.daniel.covid_19.br.ms.influd.NomeCampo.PCR_SARS2;

import java.io.File;

import com.google.common.collect.ImmutableSet;

public class AnaliseInfluMain {
	public static void main(String[] args) {
		for (String a : args) {
			if (a.startsWith("--")) {
				return;
			}
			System.out.println(a);
			System.out.println(System.currentTimeMillis());
			final File f = new File(a);

			final Committer<Registro> c = new MortesOficiaisPorCovid19(f,
					ImmutableSet.of(DT_NOTIFIC, DT_SIN_PRI, DT_INTERNA, DT_ENTUTI, DT_SAIDUTI, DT_RAIOX, DT_COLETA,
							PCR_RESUL, DT_PCR, CLASSI_FIN, EVOLUCAO, DT_EVOLUCA, DT_ENCERRA, DT_DIGITA, PCR_SARS2)) //
									.andThen(new TempoEstadia(f));
			for (final Registro r : new Influ(f)) {
				c.accept(r);
			}
			c.commit();
			System.out.println(System.currentTimeMillis());
		}
	}
}
