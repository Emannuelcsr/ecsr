package br.com.project.been.geral;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Classe responsável por armazenar e representar as informações dos campos 
 * que serão utilizados nas telas de pesquisa do sistema.
 * 
 * Essa classe funciona como um "modelo" ou "estrutura" para representar:
 * - A descrição amigável que aparecerá para o usuário.
 * - O nome do campo correspondente no banco de dados.
 * - O tipo do dado que o campo representa.
 * - Uma prioridade que define a ordem de exibição dos campos na tela.
 * 
 * Implementa:
 * - Serializable: Para permitir que o objeto possa ser trafegado em redes ou salvo em arquivos.
 * - Comparator<ObjetoCampoConsulta>: Para permitir comparar objetos desta classe, baseado na prioridade.
 */

public class ObjetoCampoConsulta implements Serializable, Comparator<ObjetoCampoConsulta> {

	private static final long serialVersionUID = 1L;

	private String descricao; // Descrição amigável do campo (exibido na tela)
	private String campoBanco; // Nome do campo no banco de dados (usado nas consultas)
	private Object tipoClass; // Tipo da classe do campo (exemplo: String.class, Date.class, etc)
	private Integer principal; // Prioridade do campo na exibição (quanto menor o número, maior a prioridade)

//-------------------------------------------------------------------------------------------------------------------------//
	@Override
	public int hashCode() {
		return Objects.hash(campoBanco);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjetoCampoConsulta other = (ObjetoCampoConsulta) obj;
		return Objects.equals(campoBanco, other.campoBanco);
	}
//-------------------------------------------------------------------------------------------------------------------------//
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCampoBanco() {
		return campoBanco;
	}

	public void setCampoBanco(String campoBanco) {
		this.campoBanco = campoBanco;
	}

	public Object getTipoClass() {
		return tipoClass;
	}

	public void setTipoClass(Object tipoClass) {
		this.tipoClass = tipoClass;
	}

	public Integer getPrincipal() {
		return principal;
	}

	public void setPrincipal(Integer principal) {
		this.principal = principal;
	}

//-------------------------------------------------------------------------------------------------------------------------//
	
	@Override
	public String toString() { // Retorna a descrição do campo
		return getDescricao();
	}

//-------------------------------------------------------------------------------------------------------------------------//	
	
	
	/**
	 * Compara dois objetos ObjetoCampoConsulta com base na propriedade 'principal'.
	 * 
	 * Quanto menor o valor de 'principal', maior a prioridade de exibição.
	 * 
	 * @param o1 Primeiro objeto a ser comparado
	 * @param o2 Segundo objeto a ser comparado
	 * @return valor negativo se o1 < o2, zero se o1 == o2, ou valor positivo se o1 > o2
	 */	
	@Override
	public int compare(ObjetoCampoConsulta o1, ObjetoCampoConsulta o2) {
		return ((ObjetoCampoConsulta) o1).getPrincipal().compareTo(
				((ObjetoCampoConsulta) o2).getPrincipal());
	}

//-------------------------------------------------------------------------------------------------------------------------//	
	
}
