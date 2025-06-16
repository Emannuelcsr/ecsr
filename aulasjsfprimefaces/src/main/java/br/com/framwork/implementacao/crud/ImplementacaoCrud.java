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
import br.com.project.model.classes.Entidade;

/**
 * Implementação genérica da interface de CRUD (Create, Read, Update, Delete)
 * utilizando Hibernate e recursos do Spring Framework.
 * 
 * Esta classe fornece operações padrões de persistência para qualquer entidade
 * JPA, evitando duplicação de código nas camadas de repositório.
 * 
 * @param <T> Entidade a ser manipulada.
 * 
 *            Tecnologias utilizadas: - Hibernate (SessionFactory / Query) -
 *            Spring Framework (@Component / @Transactional / @Autowired) - JDBC
 *            Template / SimpleJDBC / SimpleJDBCInsert
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
	public void save(T obj) throws Exception { // * Salva um novo registro na base de dados.
		validaSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlushSession();

	}

	@Override
	public void persist(T obj) throws Exception { // * Persiste um novo registro na base de dados.
		validaSessionFactory();
		sessionFactory.getCurrentSession().persist(obj);
		executeFlushSession();
	}

	@Override
	public void saveOrUpdate(T obj) throws Exception { // * Salva ou atualiza um registro na base de dados.
		validaSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		executeFlushSession();
	}

	@Override
	public void update(T obj) throws Exception { // * Atualiza um registro existente na base de dados.
		validaSessionFactory();
		sessionFactory.getCurrentSession().update(obj);
		executeFlushSession();

	}

	@Override
	public void delete(T obj) throws Exception {// * Remove um registro da base de dados.
		validaSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlushSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(T obj) throws Exception { // * Realiza o merge de um objeto com o contexto da sessão Hibernate.
		validaSessionFactory();
		obj = (T) sessionFactory.getCurrentSession().merge(obj);
		executeFlushSession();

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findList(Class<T> entidade) throws Exception { // * Retorna todos os registros da entidade informada.
		validaSessionFactory();
		StringBuilder query = new StringBuilder();
		query.append(" select distinct (entity) from ").append(entidade.getSimpleName()).append(" entity ");

		List<T> lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();

		return lista;
	}

	@Override
	public Object findById(Class<T> entidade, Long id) throws Exception { // * Busca um registro por ID utilizando o
																			// método load do Hibernate.

		validaSessionFactory();
		Object obj = sessionFactory.getCurrentSession().load(entidade, id);

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findPorId(Class<T> entidade, Long id) throws Exception { // * Busca uma entidade por ID.
		validaSessionFactory();
		T obj = (T) sessionFactory.getCurrentSession().load(entidade, id);

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findListByQueryDinamic(String s) throws Exception { // * Executa uma consulta HQL dinâmica.
		validaSessionFactory();
		List<T> list = new ArrayList<T>();
		list = sessionFactory.getCurrentSession().createQuery(s).list();

		return list;
	}

	@Override
	public void executeUpdateQueryDinamic(String s) throws Exception { // * Executa uma query HQL de atualização.
		validaSessionFactory();
		sessionFactory.getCurrentSession().createQuery(s).executeUpdate();
		executeFlushSession();
	}

	@Override
	public void executeUpdateSQLDinamic(String s) throws Exception { // * Executa uma query SQL de atualização.
		validaSessionFactory();
		sessionFactory.getCurrentSession().createSQLQuery(s).executeUpdate();
		executeFlushSession();
	}

	@Override
	public void clearSession() throws Exception { // * Limpa a sessão atual do Hibernate.
		sessionFactory.getCurrentSession().clear();
	}

	@Override
	public void evict(Object objs) throws Exception { // * Remove um objeto do contexto da sessão Hibernate.
		validaSessionFactory();
		sessionFactory.getCurrentSession().evict(objs);
	}

	@Override
	public Session getSession() throws Exception { // * Retorna a sessão atual do Hibernate.
		validaSessionFactory();

		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<?> getListSqlDinamic(String sql) throws Exception { // * Executa uma query SQL retornando lista de
																	// objetos.
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
	public Long TotalRegistros(String table) throws Exception { // * Retorna o total de registros de uma tabela.

		StringBuilder sql = new StringBuilder();

		sql.append(" select count(1) from ").append(table);

		return jdbcTemplateImpl.queryForLong(sql.toString());
	}

	@Override
	public Query obterQuery(String query) throws Exception { // * Cria e retorna uma Query HQL.
		validaSessionFactory();
		Query queryReturn = sessionFactory.getCurrentSession().createQuery(query.toString());

		return queryReturn;
	}

	/**
	 * Executa uma consulta paginada com HQL.
	 * 
	 * @param Query            Query HQL.
	 * @param iniciaNoRegistro Posição inicial.
	 * @param maximoResultado  Número máximo de resultados.
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

	private void validaTransaction() { // * Valida se a sessionFactory está ativa.

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
	private void executeFlushSession() { // * Força o commit das operações pendentes no Hibernate.

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
	
	
	/**
	 * Retorna um único resultado a partir de uma HQL.
	 */
	@SuppressWarnings("unchecked")
	public T findUniqueByQueryDinamica(String Query) throws Exception {
		validaSessionFactory();

		T obj = (T) sessionFactory.getCurrentSession().createQuery(Query.toString()).uniqueResult();

		return obj;
	}

	
	/**
	 * Realiza uma consulta única em uma entidade com base em um atributo, valor e uma condição adicional.
	 * A consulta é construída dinamicamente utilizando a HQL (Hibernate Query Language) ou JPQL (Java Persistence Query Language).
	 *
	 * @param entidade A classe da entidade que será consultada, representando a tabela no banco de dados.
	 *                 Exemplo: `Entidade.class`
	 * @param valor O valor que será comparado com o atributo fornecido. Esse valor será utilizado na cláusula `WHERE`.
	 * @param atributo O nome do atributo da entidade que será utilizado na comparação. Exemplo: "ent_login".
	 * @param condicao Uma condição adicional para refinar a consulta, como um filtro extra no `WHERE`.
	 *                 Exemplo: `and entity.ent_inativo is false`.
	 * @return O objeto da entidade que corresponde aos critérios fornecidos (se encontrado), ou `null` caso contrário.
	 * @throws Exception Se ocorrer algum erro durante a construção da consulta ou execução da pesquisa.
	 */
	public T findUniqueByProperty(Class<T> entidade, Object valor, String atributo, String condicao) throws Exception {
	    
		
		// Valida a sessão do Hibernate/JPA, garantindo que a fábrica de sessões está corretamente configurada		
	    validaSessionFactory();

	    
	    
	    // Construção dinâmica da consulta utilizando StringBuilder para maior eficiência
	    StringBuilder query = new StringBuilder();
	    query.append(" select entity from ")  // Inicia a consulta com a cláusula select
	         .append(entidade.getSimpleName())  // Adiciona o nome simples da classe da entidade 
	         .append(" entity where entity.")  // Adiciona o alias 'entity' para a entidade
	         .append(atributo)  // ent_login
	         .append(" = '")  // Adiciona o operador de comparação '=' e o valor a ser comparado
	         .append(valor)  // O valor que será comparado com o atributo
	         .append("' ")  // Fecha a comparação com as aspas simples para valores de string
	         .append(condicao);  // Adiciona a condição adicional (como AND, OR, etc.)

	    
	    
	    // Executa a consulta dinâmica e retorna o resultado único (objeto da entidade)
	    T obj = (T) this.findUniqueByQueryDinamica(query.toString());

	    return obj;  // Retorna o objeto encontrado, ou null se não encontrado
	}

	@Override
	public void inativar(T obj) throws Exception {
	    if (obj instanceof Entidade) {
	        Entidade entidade = (Entidade) obj;
	        entidade.setEnt_inativo(true); // marca como inativo
	        update(obj); // atualiza no banco
	    } else {
	        throw new IllegalArgumentException("Objeto não é do tipo Entidade");
	    }
	}

	

	}
