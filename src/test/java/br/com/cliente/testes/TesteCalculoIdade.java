package br.com.cliente.testes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

import br.com.application.util.Util;

class TesteCalculoIdade {

	@Test
	void deveRetornarAidadeApartirDaDataDeNascimento() {
		
		Date date = new Util().toDate("1979-09-27");
		Calendar birth = new GregorianCalendar();
		birth.setTime(date);
		Calendar today = Calendar.getInstance();
		int age = (today.get(Calendar.YEAR) - birth.get(Calendar.YEAR));
		birth.add(Calendar.YEAR, age);
		if (today.before(birth)) {
			age--;
		}
		assertEquals(age, 40);
	}
}
