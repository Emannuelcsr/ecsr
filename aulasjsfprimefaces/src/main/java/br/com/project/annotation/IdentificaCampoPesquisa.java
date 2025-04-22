package br.com.project.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation utilizada para marcar campos de uma classe
 * que poderão ser usados em consultas/pesquisas dinâmicas.
 * 
 * Exemplo prático de uso:
 * 
 * {@code
 * @IdentificaCampoPesquisa(descricaoCampo = "Nome do Cliente", campoConsulta = "nome", principal = 1)
 * private String nome;
 * }
 * 
 * Essa anotação permite informar:
 * - Descrição amigável para exibir na tela
 * - Campo do banco de dados que será consultado
 * - Ordem de prioridade na exibição (menor valor aparece primeiro)
 */

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public abstract @interface IdentificaCampoPesquisa {

	/**
	 * Descrição amigável do campo que será exibida na tela.
	 * Exemplo: "Nome do Cliente"
	 */
	String descricaoCampo(); //descricao do campo para tela
	
	/**
	 * Nome do campo correspondente no banco de dados
	 * Exemplo: "nome"
	 */
	String campoConsulta(); // campo do banco
	
	/**
	 * Define a ordem de exibição no combo/lista de pesquisa.
	 * Quanto menor o número, maior a prioridade de exibição.
	 * Valor padrão: 10000 (aparece por último caso não informado)
	 */
	int principal() default 10000; //posicao que ira aparecer no combo
	
	
}
