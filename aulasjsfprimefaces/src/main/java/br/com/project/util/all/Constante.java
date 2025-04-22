package br.com.project.util.all;

import java.io.Serializable;

/**
 * Classe utilitária responsável por armazenar constantes de mensagens 
 * utilizadas na aplicação.
 * 
 * <p>Essas constantes são comumente utilizadas para padronizar 
 * mensagens de sucesso ou erro em operações do sistema.</p>
 * 
 * <p>Esta classe não deve ser instanciada.</p>
 * 
 * @author Emannuel Souza
 */
public class Constante implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Constante que representa o texto 'Sucesso'. */
	public static final String SUCESSO = "Sucesso";

	/** Mensagem padrão utilizada em operações bem-sucedidas. */
	public static final String OPERACAO_REALIZADA_COM_SUCESSO = "Operação realizada com sucesso";

	/** Mensagem padrão utilizada em operações que apresentaram erro. */
	public static final String ERRO_NA_OPERACAO = "Não foi possível executar a operação";

}
