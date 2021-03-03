package org.yokomizo.daniel.covid_19.br.ms.influd;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.CLASSI_FIN;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_DIGITA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_ENCERRA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_EVOLUCA;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_NOTIFIC;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.DT_SIN_PRI;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.EVOLUCAO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.HISTO_VGM;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.PAC_DSCBO;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.PAIS_VGM;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.SEM_NOT;
import static org.yokomizo.daniel.covid_19.br.ms.influd.V20200402.SEM_PRI;

import java.io.File;

import org.junit.Test;

public class InfluTest {
	@Test
	@SuppressWarnings("unused")
	public void le_uma_linha() {
		for (final Registro<V20200402> r : i) {
			return;
		}
		fail("Deveria ter lido uma linha");
	}

	@Test
	public void primeiro_registro_ok() {
		for (final Registro<V20200402> r : i) {
			assertThat(r.get(DT_NOTIFIC), equalTo("30/12/2019"));
			assertThat(r.get(SEM_NOT), equalTo("01"));
			assertThat(r.get(DT_SIN_PRI), equalTo("29/12/2019"));
			assertThat(r.get(SEM_PRI), equalTo("01"));
			assertThat(r.get(CLASSI_FIN), equalTo("4"));
			assertThat(r.get(EVOLUCAO), equalTo("1"));
			assertThat(r.get(DT_EVOLUCA), equalTo("04/01/2020"));
			assertThat(r.get(DT_ENCERRA), equalTo("20/01/2020"));
			assertThat(r.get(DT_DIGITA), equalTo("31/12/2019"));
			assertThat(r.get(HISTO_VGM), equalTo("0"));
			assertThat(r.get(PAIS_VGM), equalTo(""));
			assertThat(r.get(PAC_DSCBO), equalTo(""));
			return;
		}
		fail("Deveria ter lido uma linha");
	}

	final Influ<V20200402> i = new Influ<>(V20200402.BY_ORDINAL.values(),
			new File("src/test/resources/2020/INFLUD-07-07-2020.csv"));
}
