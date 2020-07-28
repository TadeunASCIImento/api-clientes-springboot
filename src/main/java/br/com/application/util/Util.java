package br.com.application.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Util {

	// converte a data do formato string para Date
	public Date toDate(String string) {

		try {

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(formato.parse(string).getTime());
			return date;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Calcula a idade a partir da data de nascimento
	public Integer getIdade(String data) {
		Calendar nascimento = new GregorianCalendar();
		nascimento.setTime(toDate(data));
		Calendar atual = Calendar.getInstance();

		Integer idade = (atual.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR));
		nascimento.add(Calendar.YEAR, idade);

		if (atual.before(nascimento)) {
			idade--;
		}
		return idade;
	}
}
