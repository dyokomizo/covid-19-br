package org.yokomizo.daniel.covid_19.br.ms.influd.v20200727;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.yokomizo.daniel.covid_19.br.ms.influd.ClassificacaoFinalCaso.COVID_19;
import static org.yokomizo.daniel.covid_19.br.ms.influd.ClassificacaoFinalCaso.SRAG_NAO_ESPECIFICADO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.EvolucaoCaso.OBITO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.CLASSI_FIN;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.DT_DIGITA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.DT_SIN_PRI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200727.EVOLUCAO;

import java.io.File;
import java.util.AbstractMap;
import java.util.Map;

import org.yokomizo.daniel.covid_19.br.ms.influd.Acumulador;
import org.yokomizo.daniel.covid_19.br.ms.influd.Committer;
import org.yokomizo.daniel.covid_19.br.ms.influd.Registro;
import org.yokomizo.daniel.covid_19.br.ms.influd.SaveAsFile;
import org.yokomizo.daniel.covid_19.br.ms.influd.V20200727;

public class AnalisePadraoV20200727 implements Committer<Registro<V20200727>> {
	private final File file;
	private final Acumulador<String, String> acumulador;

	public AnalisePadraoV20200727(File file) {
		checkNotNull(file, "file");
		this.file = file;
		this.acumulador = new Acumulador<>();
	}

	@Override
	public void accept(Registro<V20200727> r) {
		acumulador.acumula(prefixed(r.toEntry(DT_SIN_PRI)));
		if (COVID_19.apply(r.get(CLASSI_FIN)) || SRAG_NAO_ESPECIFICADO.apply(r.get(CLASSI_FIN))) {
			acumulador.acumula(prefixed(r.toEntry(DT_SIN_PRI), r.get(CLASSI_FIN)));
			if (OBITO.apply(r.get(EVOLUCAO))) {
				acumulador.acumula(prefixed(r.toEntry(DT_SIN_PRI), "OBITO"));
				acumulador.acumula(prefixed(r.toEntry(DT_DIGITA)));
				acumulador.acumula(prefixed(r.toEntry(DT_DIGITA), r.get(CLASSI_FIN)));
			}
		}
	}

	private <T> Map.Entry<String, String> prefixed(Map.Entry<T, String> e) {
		return new AbstractMap.SimpleEntry<String, String>(e.getKey().toString(), e.getValue());
	}

	private <T> Map.Entry<String, String> prefixed(Map.Entry<T, String> e, Object prefix) {
		return new AbstractMap.SimpleEntry<String, String>(e.getKey() + "-" + prefix, e.getValue());
	}

	@Override
	public void commit() {
		acumulador.salvarComo(new SaveAsFile("CASES").apply(file));
	}
}
