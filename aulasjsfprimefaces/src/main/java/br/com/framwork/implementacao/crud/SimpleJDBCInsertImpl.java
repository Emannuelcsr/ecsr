package br.com.framwork.implementacao.crud;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe responsável por implementar o SimpleJdbcInsert com suporte a transações.
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SimpleJDBCInsertImpl extends SimpleJdbcInsert implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor que recebe o DataSource para operações de insert no banco de dados.
	 * 
	 * @param dataSource - Fonte de dados utilizada pelo SimpleJdbcInsert
	 */
	public SimpleJDBCInsertImpl(DataSource dataSource) {
		super(dataSource);
	}
}
