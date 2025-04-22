package br.com.project.report.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe utilitária para manipulação e formatação de datas em geral.
 * 
 * <p>Fornece métodos estáticos para formatação de datas em formatos
 * utilizados em relatórios e consultas SQL.</p>
 * 
 * <p>Uso exclusivo para apoio, sem necessidade de instanciar a classe.</p>
 * 
 * @author Emannuel
 */



public class DateUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	
	
//--------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Retorna a data atual no formato: ddMMyyyy
	 * 
	 * <p>Exemplo de retorno: 11042025</p>
	 * 
	 * @return String com a data atual no padrão ddMMyyyy
	 */
	public static String getDateAtualReportName() {
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	
//--------------------------------------------------------------------------------------------------------------------------------
	
	
	/**
	 * Formata uma data no padrão SQL: 'yyyy-MM-dd'
	 * 
	 * <p>Exemplo de retorno: '2025-04-11'</p>
	 * 
	 * @param data Objeto Date a ser formatado
	 * @return String com a data formatada no padrão SQL com aspas simples
	 */
	public static String formatDateSql(Date data) {
		StringBuffer retorno = new StringBuffer();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		retorno.append("'");
		retorno.append(dateFormat.format(data));
		retorno.append("'");
		return retorno.toString();
	}

	
//--------------------------------------------------------------------------------------------------------------------------------
	
	
	/**
	 * Formata uma data no padrão SQL simples: yyyy-MM-dd
	 * 
	 * <p>Exemplo de retorno: 2025-04-11</p>
	 * 
	 * @param data Objeto Date a ser formatado
	 * @return String com a data formatada no padrão SQL sem aspas
	 */
	public static String formatDateSqlSimple(Date data) {
		StringBuffer retorno = new StringBuffer();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		retorno.append(dateFormat.format(data));
		return retorno.toString();
	}

	
}
