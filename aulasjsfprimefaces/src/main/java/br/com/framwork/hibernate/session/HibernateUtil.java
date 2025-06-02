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
	 * Constrói a instância única de {@link SessionFactory} com base nas configurações
	 * definidas no arquivo hibernate.cfg.xml.
	 *
	 * <p>Esse método segue o padrão Singleton para garantir que apenas uma instância
	 * de SessionFactory seja criada durante o ciclo de vida da aplicação.</p>
	 *
	 * <p>Utiliza a API do Hibernate para carregar a configuração padrão e instanciar
	 * a fábrica de sessões que será usada para gerar objetos {@link org.hibernate.Session}.</p>
	 *
	 * @return Instância da SessionFactory configurada
	 * @throws ExceptionInInitializerError se houver falha na configuração
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
	 * Retorna a sessão atual do Hibernate vinculada ao contexto da thread.
	 *
	 * <p>Essa sessão é automaticamente gerenciada pelo Hibernate,
	 * sendo aberta e fechada de acordo com o ciclo de vida da transação atual.</p>
	 *
	 * <p>Requer configuração no hibernate.cfg.xml:
	 * <code>hibernate.current_session_context_class</code> deve estar definido como <code>thread</code>.</p>
	 *
	 * @return Sessão corrente do Hibernate associada à thread
	 */
	public static Session getCurrentSession() {

		return getSessionFactory().getCurrentSession();
	}
	
	
	
//--------------------------------------------------------------------------------------------------------------------------	

	
	/**
	 * Abre uma nova instância de Session (sessão independente) do Hibernate.
	 * 
	 * <p>Essa sessão não está vinculada ao contexto da thread atual. Portanto,
	 * deve ser fechada manualmente após o uso para evitar vazamento de conexões.</p>
	 * 
	 * <p>Se o SessionFactory ainda não tiver sido criado, este método irá inicializá-lo.</p>
	 *
	 * @return Uma nova instância de Session do Hibernate
	 */
	public static Session openSession() {
		
		if(sessionfactory == null) {
			buildSessionFactory();
						
		}
		
		return sessionfactory.openSession();		
	}

	
//--------------------------------------------------------------------------------------------------------------------------
	
	
	
	/**
	 * Obtém uma conexão JDBC diretamente do provedor de conexões do Hibernate.
	 * 
	 * Este método acessa o provedor de conexões configurado no Hibernate (geralmente o Hibernate Connection Pool 
	 * ou um pool configurado externamente, como o C3P0 ou DBCP)
	 * e retorna uma conexão JDBC que pode ser utilizada para executar operações diretamente no banco de dados.
	 * 
	 * **Uso recomendado**: Este método é útil quando você precisa de uma conexão JDBC direta, fora do contexto do Hibernate, 
	 * mas ainda deseja utilizar o provedor de conexões configurado no Hibernate. 
	 * Se você estiver utilizando o Hibernate para gerenciamento de transações e sessões, 
	 * é mais recomendável obter uma `Session` ao invés de uma conexão direta.
	 * 
	 * **Exemplo de uso**:
	 * <pre>
	 * Connection connection = HibernateUtil.getConnectionProvider();
	 * // Utilizar a conexão diretamente para executar operações JDBC
	 * </pre>
	 * 
	 * @return Connection Um objeto {@link Connection} que representa a conexão JDBC com o banco de dados configurado no Hibernate.
	 * @throws SQLException Caso ocorra um erro ao tentar obter a conexão do provedor de conexões.
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
	 * Realiza o lookup do DataSource registrado no servidor de aplicação (Tomcat)
	 * via JNDI, conforme definido no arquivo context.xml.
	 * 
	 * Esse DataSource permite conexões diretas com o banco de dados usando JDBC.
	 * 
	 * @return DataSource configurado via JNDI
	 * @throws NamingException se o recurso JNDI não for encontrado ou houver erro de acesso
	 */
	public DataSource getDataSourceJndi() throws NamingException{
		
		InitialContext context = new InitialContext();
		
		return (DataSource) context.lookup(VariavelConexaoUtil.JAVA_COMP_EMC_JDBC_DATA_SOURCE);
	}

	
	
}
