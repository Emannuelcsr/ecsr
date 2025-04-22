package br.com.framwork.implementacao.crud;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe responsável por implementar o SimpleJdbcTemplate com suporte a transações.
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SimpleJDBCTemplateImpl extends SimpleJdbcTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor que recebe o DataSource para conexão com o banco de dados.
	 * 
	 * @param dataSource - Fonte de dados utilizada pelo SimpleJdbcTemplate
	 */
	public SimpleJDBCTemplateImpl(DataSource dataSource) {
		super(dataSource);
	}
	
}
