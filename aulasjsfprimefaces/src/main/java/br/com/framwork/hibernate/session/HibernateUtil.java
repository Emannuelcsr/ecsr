package br.com.framwork.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framwork.implementacao.crud.VariavelConexaoUtil;

/**
 * Classe utilitária responsável pela configuração, criação e gerenciamento
 * das conexões Hibernate e JDBC (DataSource JNDI).
 * 
 * Esta classe centraliza:
 * - Criação do SessionFactory (Hibernate)
 * - Abertura de Sessions (Hibernate)
 * - Obtenção de Connections (JDBC) via Hibernate ou via Tomcat JNDI
 * 
 * Escopo: ApplicationScoped - Única instância na aplicação.
 * 
 * Arquivo de configuração utilizado: hibernate.cfg.xml
 * 
 * 
 */

@ApplicationScoped
public class HibernateUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	
	/** Caminho JNDI do DataSource configurado no Tomcat */
	public static String JAVA_COMP_EMC_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasource";

	/** Instância única do SessionFactory */
	private static SessionFactory sessionfactory = buildSessionFactory();

	
//--------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Método responsável por criar o SessionFactory a partir do arquivo 
	 * de configuração hibernate.cfg.xml.
	 * 
	 * @return SessionFactory
	 */
	private static SessionFactory buildSessionFactory() {

		try {

			if (sessionfactory == null) {
				sessionfactory = new Configuration().configure().buildSessionFactory();

			}

			return sessionfactory;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Erro ao criar conexao Session Factory");
		}

	}

	
//--------------------------------------------------------------------------------------------------------------------------
	
	
	/**
	 * Retorna a instância atual do SessionFactory.
	 * 
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionfactory;

	}

	
//--------------------------------------------------------------------------------------------------------------------------	
	
	
	/**
	 * Retorna a sessão corrente do Hibernate (session bound to context).
	 * 
	 * @return Session
	 */
	public static Session getCurrentSession() {

		return getSessionFactory().getCurrentSession();
	}
	
	
	
//--------------------------------------------------------------------------------------------------------------------------	

	
	/**
	 * Abre uma nova sessão do Hibernate.
	 * 
	 * @return Session
	 */
	public static Session openSession() {
		
		if(sessionfactory == null) {
			buildSessionFactory();
						
		}
		
		return sessionfactory.openSession();		
	}

	
//--------------------------------------------------------------------------------------------------------------------------
	
	
	
	/**
	 * Obtém a conexão JDBC diretamente do provedor de conexões 
	 * do Hibernate configurado.
	 * 
	 * @return Connection JDBC
	 * @throws SQLException Caso ocorra erro na conexão
	 */
	public static Connection getConnectionProvider() throws SQLException{
		
		
		return ((SessionFactoryImplementor) sessionfactory).getConnectionProvider().getConnection();
	}

	
//--------------------------------------------------------------------------------------------------------------------------	

	
	/**
	 * Obtém a conexão JDBC via lookup no JNDI configurado no Tomcat.
	 * 
	 * @return Connection JDBC via JNDI
	 * @throws Exception Caso ocorra erro na busca do DataSource ou conexão
	 */
	public static Connection getConnection() throws Exception{
		
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource)context.lookup(JAVA_COMP_EMC_JDBC_DATA_SOURCE);
		
		return ds.getConnection();
	}
 	

	
//--------------------------------------------------------------------------------------------------------------------------	
	
	/**
	 * Retorna o DataSource configurado no Tomcat via JNDI.
	 * 
	 * @return DataSource JNDI
	 * @throws NamingException Caso ocorra erro no lookup do JNDI
	 */
	public DataSource getDataSourceJndi() throws NamingException{
		
		InitialContext context = new InitialContext();
		
		return (DataSource) context.lookup(VariavelConexaoUtil.JAVA_COMP_EMC_JDBC_DATA_SOURCE);
	}

	
	
}
