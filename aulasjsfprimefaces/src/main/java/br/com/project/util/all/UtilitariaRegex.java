package br.com.project.util.all;

public class UtilitariaRegex {

	
	public String retiraAcentos(String string) {
	    String aux = new String(string);
	    aux = aux.replaceAll("[êéèëÊÉÈË]", "e");
	    aux = aux.replaceAll("[ûúùüÛÚÙÜ]", "u");
	    aux = aux.replaceAll("[îíìïÎÍÌÏ]", "i");
	    aux = aux.replaceAll("[âãáàäÂÃÁÀÄ]", "a");
	    aux = aux.replaceAll("[ôõóòöÔÕÓÒÖ]", "o");
	    return aux;
	}
	
	
	
}
