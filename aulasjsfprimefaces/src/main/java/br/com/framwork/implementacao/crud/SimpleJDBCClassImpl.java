package br.com.framwork.implementacao.crud;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe responsável por implementar o SimpleJdbcCall com suporte a transações.
 * Utilizada para chamadas de procedures e functions no banco de dados.
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SimpleJDBCClassImpl extends SimpleJdbcCall implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor que recebe o DataSource para chamadas JDBC no banco de dados.
	 * 
	 * @param dataSource - Fonte de dados utilizada pelo SimpleJdbcCall
	 */
	public SimpleJDBCClassImpl(DataSource dataSource) {
		super(dataSource);
	}
}
