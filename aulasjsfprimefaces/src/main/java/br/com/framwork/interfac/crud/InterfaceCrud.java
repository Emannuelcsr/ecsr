package br.com.framwork.interfac.crud;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;




/**
 * Interface genérica para operações CRUD básicas e utilitárias em entidades persistentes.
 * 
 * @param <T> Tipo da entidade que será manipulada.
 */



@Component
@Transactional
public interface InterfaceCrud<T>  extends Serializable{

	// salva
	void save(T obj) throws Exception;
	
	
	void persist(T obj) throws Exception;
	
	//salva ou atualiza
	void saveOrUpdate(T obj) throws Exception;
	
	//atualiza
	void update(T obj) throws Exception;
	
	//deleta
	void delete(T obj) throws Exception;
	
	//salva e retorna a chave primaria do banco de dados
	T merge(T obj) throws Exception;
	
	//Carrega a lista de dados de determinada classe
	List<T> findList(Class<T> objs) throws Exception;
		
	Object findById(Class<T> entidade, Long id) throws Exception;
	
	T findPorId(Class<T> entidade, Long id) throws Exception;
		
	List<T> findListByQueryDinamic (String s) throws Exception;

	//executar update com HQL
	void executeUpdateQueryDinamic (String s) throws Exception;
	
	//executar update com SQL
	void executeUpdateSQLDinamic(String s) throws Exception;
	
	//limpa a sessao do hibernate
	void clearSession() throws Exception;
	
	//retira um objeto da sessao do hibernate
	void evict(Object objs) throws Exception;
	
	Session getSession() throws Exception;
	
	List<?> getListSqlDinamic(String sql) throws Exception;	
	
	
	//JDBC do Spring
	JdbcTemplate getJdbcTemplate();
	
	SimpleJdbcTemplate getSimpleJdbcTemplate();
	
	SimpleJdbcInsert getSimpleJdbcInsert();
	
	
	Long TotalRegistros(String table) throws Exception;
	
	Query obterQuery(String query) throws Exception;

	//Carregamento Dinamico com JSF e primefaces
	List<T> findListByQueryDinamic(String Query, int iniciaNoRegistro, int maximoResultado) throws Exception;
	
}

