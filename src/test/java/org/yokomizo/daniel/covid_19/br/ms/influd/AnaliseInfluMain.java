package org.yokomizo.daniel.covid_19.br.ms.influd;

import static org.yokomizo.daniel.covid_19.br.ms.influd.ClassificacaoFinalCaso.COVID_19;
import static org.yokomizo.daniel.covid_19.br.ms.influd.EvolucaoCaso.OBITO;
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
import java.io.IOException;

public class AnaliseInfluMain {
	public static void main(String[] args) throws IOException {
		for (String a : args) {
			if (a.startsWith("--")) {
				return;
			}
			System.out.println(a);
			System.out.println(System.currentTimeMillis());
			final File f = new File(a);
			final Tallier<NomeCampo, String> t = new Tallier<>();
			for (final Registro r : new Influ(f)) {
				if (COVID_19.apply(r.get(CLASSI_FIN)) && OBITO.apply(r.get(EVOLUCAO))) {
					t.tally(r.toEntry(DT_NOTIFIC));
					t.tally(r.toEntry(DT_SIN_PRI));
					t.tally(r.toEntry(DT_INTERNA));
					t.tally(r.toEntry(DT_ENTUTI));
					t.tally(r.toEntry(DT_SAIDUTI));
					t.tally(r.toEntry(DT_RAIOX));
					t.tally(r.toEntry(DT_COLETA));
					t.tally(r.toEntry(PCR_RESUL));
					t.tally(r.toEntry(DT_PCR));
					t.tally(r.toEntry(CLASSI_FIN));
					t.tally(r.toEntry(EVOLUCAO));
					t.tally(r.toEntry(DT_EVOLUCA));
					t.tally(r.toEntry(DT_ENCERRA));
					t.tally(r.toEntry(DT_DIGITA));
					t.tally(r.toEntry(PCR_SARS2));
				}
			}
			t.saveAs(f, DT_NOTIFIC, DT_SIN_PRI, DT_INTERNA, DT_ENTUTI, DT_SAIDUTI, DT_RAIOX, DT_COLETA, DT_PCR, DT_EVOLUCA, DT_ENCERRA, DT_DIGITA);
			System.out.println(System.currentTimeMillis());
		}
	}
}
