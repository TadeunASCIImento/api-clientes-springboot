package br.com.application.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Util {

	public Date toDate(String string) {
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(formato.parse(string).getTime());
			return date;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Integer getIdade(Date date) {
		Calendar nascimento = new GregorianCalendar();
		nascimento.setTime(date);
		Calendar atual = Calendar.getInstance();
		Integer idade = (atual.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR));
		nascimento.add(Calendar.YEAR, idade);
		if (atual.before(nascimento)) {
			idade--;
		}
		return idade;
	}
}
