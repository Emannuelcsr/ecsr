package br.com.framwork.implementacao.crud;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.framwork.hibernate.session.HibernateUtil;
import br.com.framwork.interfac.crud.InterfaceCrud;

/**
 * Implementação genérica da interface de CRUD (Create, Read, Update, Delete) 
 * utilizando Hibernate e recursos do Spring Framework.
 * 
 * Esta classe fornece operações padrões de persistência para qualquer entidade JPA, 
 * evitando duplicação de código nas camadas de repositório.
 * 
 * @param <T> Entidade a ser manipulada.
 * 
 * Tecnologias utilizadas:
 * - Hibernate (SessionFactory / Query)
 * - Spring Framework (@Component / @Transactional / @Autowired)
 * - JDBC Template / SimpleJDBC / SimpleJDBCInsert
 * 
 * 
 */


@Component
@Transactional
public class ImplementacaoCrud<T> implements InterfaceCrud<T> {

	private static final long serialVersionUID = 1L;

	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	@Autowired
	private JDBCTemplateImpl jdbcTemplateImpl;

	@Autowired
	private SimpleJDBCTemplateImpl simpleJDBCTemplateImpl;

	@Autowired
	private SimpleJDBCInsertImpl simpleJDBCInsertImpl;

	@Autowired
	private SimpleJDBCClassImpl simpleJDBCClassImpl;

	
//---------------------------------------------------------------------------------------------------------------------------------
	
	
	/**
	 * Retorna a implementação personalizada do SimpleJDBCClass.
	 * 
	 * @return {@link SimpleJDBCClassImpl}
	 */
	
	public SimpleJDBCClassImpl getSimpleJDBCClassImpl() {
		return simpleJDBCClassImpl;
	}

//---------------------------------------------------------------------------------------------------------------------------------
	
	
	
	@Override
	public void save(T obj) throws Exception { // 	 * Salva um novo registro na base de dados.
		validaSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlushSession();

	}

	@Override
	public void persist(T obj) throws Exception { //	 * Persiste um novo registro na base de dados.
		validaSessionFactory();
		sessionFactory.getCurrentSession().persist(obj);
		executeFlushSession();
	}

	@Override
	public void saveOrUpdate(T obj) throws Exception { //	 * Salva ou atualiza um registro na base de dados.
		validaSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		executeFlushSession();
	}

	@Override
	public void update(T obj) throws Exception { //	 * Atualiza um registro existente na base de dados.
		validaSessionFactory();
		sessionFactory.getCurrentSession().update(obj);
		executeFlushSession();

	}

	@Override
	public void delete(T obj) throws Exception {//	 * Remove um registro da base de dados.
		validaSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlushSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(T obj) throws Exception { 	// * Realiza o merge de um objeto com o contexto da sessão Hibernate.
		validaSessionFactory();
		obj = (T) sessionFactory.getCurrentSession().merge(obj);
		executeFlushSession();

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findList(Class<T> entidade) throws Exception { //	 * Retorna todos os registros da entidade informada.
		validaSessionFactory();
		StringBuilder query = new StringBuilder();
		query.append(" select distinct (entity) from ").append(entidade.getSimpleName()).append(" entity ");

		List<T> lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();

		return lista;
	}

	@Override
	public Object findById(Class<T> entidade, Long id) throws Exception { 	 //* Busca um registro por ID utilizando o método load do Hibernate.

		validaSessionFactory();
		Object obj = sessionFactory.getCurrentSession().load(getClass(), id);

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findPorId(Class<T> entidade, Long id) throws Exception { //	 * Busca uma entidade por ID.
		validaSessionFactory();
		T obj = (T) sessionFactory.getCurrentSession().load(getClass(), id);

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findListByQueryDinamic(String s) throws Exception { //	 * Executa uma consulta HQL dinâmica.
		validaSessionFactory();
		List<T> list = new ArrayList<T>();
		list = sessionFactory.getCurrentSession().createQuery(s).list();

		return list;
	}

	@Override
	public void executeUpdateQueryDinamic(String s) throws Exception { //	 * Executa uma query HQL de atualização.
		validaSessionFactory();
		sessionFactory.getCurrentSession().createQuery(s).executeUpdate();
		executeFlushSession();
	}

	@Override
	public void executeUpdateSQLDinamic(String s) throws Exception { //	 * Executa uma query SQL de atualização.
		validaSessionFactory();
		sessionFactory.getCurrentSession().createSQLQuery(s).executeUpdate();
		executeFlushSession();
	}

	@Override
	public void clearSession() throws Exception { //	 * Limpa a sessão atual do Hibernate.
		sessionFactory.getCurrentSession().clear();
	}

	@Override
	public void evict(Object objs) throws Exception { //	 * Remove um objeto do contexto da sessão Hibernate.
		validaSessionFactory();
		sessionFactory.getCurrentSession().evict(objs);
	}

	@Override
	public Session getSession() throws Exception { //	 * Retorna a sessão atual do Hibernate.
		validaSessionFactory();

		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<?> getListSqlDinamic(String sql) throws Exception { 	 //* Executa uma query SQL retornando lista de objetos.
		validaSessionFactory();
		List<?> list = sessionFactory.getCurrentSession().createSQLQuery(sql).list();

		return list;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		// TODO Auto-generated method stub
		return jdbcTemplateImpl;
	}

	@Override
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		// TODO Auto-generated method stub
		return simpleJDBCTemplateImpl;
	}

	@Override
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		// TODO Auto-generated method stub
		return simpleJDBCInsertImpl;
	}

	@Override
	public Long TotalRegistros(String table) throws Exception { //	 * Retorna o total de registros de uma tabela.

		StringBuilder sql = new StringBuilder();

		sql.append(" select count(1) from ").append(table);

		return jdbcTemplateImpl.queryForLong(sql.toString());
	}

	@Override
	public Query obterQuery(String query) throws Exception { //	 * Cria e retorna uma Query HQL.
		validaSessionFactory();
		Query queryReturn = sessionFactory.getCurrentSession().createQuery(query.toString());

		return queryReturn;
	}

	
	/**
	 * Executa uma consulta paginada com HQL.
	 * 
	 * @param Query Query HQL.
	 * @param iniciaNoRegistro Posição inicial.
	 * @param maximoResultado Número máximo de resultados.
	 * @return Lista de resultados.
	 * @throws Exception Caso ocorra erro na operação.
*/	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findListByQueryDinamic(String Query, int iniciaNoRegistro, int maximoResultado) throws Exception {
		validaSessionFactory();
		List<T> list = new ArrayList<T>();

		list = sessionFactory.getCurrentSession().createQuery(Query).setFirstResult(iniciaNoRegistro)
				.setMaxResults(maximoResultado).list();

		return list;
	}

	private void validaTransaction() { //	 * Valida se a sessionFactory está ativa.

		if (!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction();
		}
	}

	@SuppressWarnings("unused")
	private void commitProcessoAjax() {

		sessionFactory.getCurrentSession().beginTransaction().commit();
	}

	@SuppressWarnings("unused")
	private void roolbackProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().rollback();

	}

	private void validaSessionFactory() {

		if (sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}

		validaTransaction();
	}

	/*
	 * Roda instataneamento o sql no banco de dados.
	 * 
	 * 
	 */
	private void executeFlushSession() { 	 //* Força o commit das operações pendentes no Hibernate.

		sessionFactory.getCurrentSession().flush();

	}

	

	/**
	 * Executa uma query SQL retornando lista de arrays de objetos.
	 * 
	 * @param sql Query SQL.
	 * @return Lista de arrays de objetos.
	 * @throws Exception Caso ocorra erro na operação.
	 */
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getListSQLDinamicaArray(String sql) throws Exception {
		validaSessionFactory();
		List<Object[]> list = (List<Object[]>) sessionFactory.getCurrentSession().createSQLQuery(sql).list();

		return list;
	}

}
