package br.com.project.util.all;

/**
 * Enumeração responsável por representar os possíveis status de persistência
 * (operações de salvar, atualizar ou excluir) realizados no sistema.
 * 
 * <p>Esses status são utilizados para indicar o resultado de uma operação
 * de persistência, podendo ser:</p>
 * 
 * <ul>
 *   <li>{@link #ERRO} - Quando a operação falhou.</li>
 *   <li>{@link #SUCESSO} - Quando a operação foi concluída com sucesso.</li>
 *   <li>{@link #OBJETO_REFERENCIADO} - Quando não é possível excluir um registro pois ele está sendo referenciado por outro.</li>
 * </ul>
 * 
 * <p>Cada constante da enum possui uma descrição personalizada.</p>
 * 
 * @author Emannuel Souza
 */

//--------------------------------------------------------------------------------------------------------------------------------------------
public enum EstatusPersistencia {

	
	
	/** Indica que ocorreu um erro na operação. */
	ERRO("Erro"),

	/** Indica que a operação foi concluída com sucesso. */
	SUCESSO("Sucesso"),

	/** Indica que o objeto não pode ser excluído por estar referenciado em outra entidade. */
	OBJETO_REFERENCIADO("Esse objeto não pode ser apagado por possuir referência ao mesmo");

	
	
//--------------------------------------------------------------------------------------------------------------------------------------------

		
	/** Descrição do status de persistência. */
	private String name;

	/**
	 * Construtor da enumeração.
	 * 
	 * @param s Descrição associada ao status.
	 */
	
//--------------------------------------------------------------------------------------------------------------------------------------------

	
	
	private EstatusPersistencia(String s) {
		this.name = s;
	}

	/**
	 * Retorna a descrição do status de persistência.
	 * 
	 * @return Texto descritivo do status.
	 */

	
//--------------------------------------------------------------------------------------------------------------------------------------------

	
	@Override
	public String toString() {
		return this.name;
	}

}
