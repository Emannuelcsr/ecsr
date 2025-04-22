package br.com.frameworks.utils;

import java.io.Serializable;

import org.springframework.stereotype.Component;

/**
 * Classe utilitária responsável por armazenar informações específicas da thread atual,
 * utilizando o recurso ThreadLocal.
 * <p>
 * Permite isolar dados por thread, evitando concorrência em ambientes multi-thread.
 * </p>
 * 
 * @author  
 */


@Component
public class utilFramework implements Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * Armazena um valor Long exclusivo por thread.
	 */
	
	private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();
	
	
	/**
	 * Recupera a instância do ThreadLocal da classe.
	 * 
	 * @return ThreadLocal<Long> instância para armazenamento de dados exclusivos por thread
	 */
	
	public synchronized static ThreadLocal<Long> getThreadLocal(){
		
		return threadLocal;
	}

}
