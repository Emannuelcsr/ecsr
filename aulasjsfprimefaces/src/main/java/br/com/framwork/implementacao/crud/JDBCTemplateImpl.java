package br.com.framwork.implementacao.crud;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe responsável por implementar o JdbcTemplate com suporte a transações.
 * Utilizada para executar comandos SQL diretamente no banco de dados.
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class JDBCTemplateImpl extends JdbcTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor que recebe o DataSource para operações JDBC no banco de dados.
	 * 
	 * @param dataSource - Fonte de dados utilizada pelo JdbcTemplate
	 */
	public JDBCTemplateImpl(DataSource dataSource) {
		super(dataSource);
	}

}
