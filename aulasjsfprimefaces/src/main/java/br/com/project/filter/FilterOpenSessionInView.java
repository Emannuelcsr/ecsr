package br.com.project.filter;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.filter.DelegatingFilterProxy;

import br.com.frameworks.utils.utilFramework;
import br.com.framwork.hibernate.session.HibernateUtil;
import br.com.project.listener.ContextLoaderListenerECSRUtils;
import br.com.project.model.classes.Entidade;

/**
 * Filtro responsável por abrir e fechar sessões Hibernate e transações Spring.
 * Também garante que o usuário logado esteja disponível em ThreadLocal para auditoria.
 * 
 * Utiliza o padrão Open Session in View, integrando Spring Transaction Manager + Hibernate.
 */
@WebFilter(filterName = "conexaoFilter")
public class FilterOpenSessionInView extends DelegatingFilterProxy implements Serializable {

	
//----------------------------------------------------------------------------------------------------------------------------------------------
	private static final long serialVersionUID = 1L;

	// Instância da SessionFactory do Hibernate
	private static SessionFactory sf;

	
//----------------------------------------------------------------------------------------------------------------------------------------------

	
	/**
	 * Inicializa a SessionFactory ao carregar o filtro no container.
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		// Obtém a fábrica de sessões do Hibernate (singleton)
		sf = HibernateUtil.getSessionFactory();
	}

	
	
//----------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Método principal do filtro, intercepta todas as requisições.
	 * Abre a sessão, inicia a transação e garante commit ou rollback.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		
		
		
		// Obtém o datasource gerenciado pelo Spring
		BasicDataSource springBasicDataSource = (BasicDataSource) ContextLoaderListenerECSRUtils
				.getBean("springDataSource");

		// Define as configurações da transação
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();

		// Cria o gerenciador de transações usando o datasource do Spring
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(springBasicDataSource);

		// Inicia a transação Spring
		TransactionStatus status = transactionManager.getTransaction(def);

		
		
//----------------------------------------------------------------------------------------------------------------------------------------------

		
		try {
			
			
			
			// Define codificação de caracteres da requisição
			request.setCharacterEncoding("UTF-8");

			// Recupera o usuário logado da sessão HTTP
			HttpServletRequest request2 = (HttpServletRequest) request;
			HttpSession session = request2.getSession();
			Entidade userLogadoSessao = (Entidade) session.getAttribute("userLogadoSessao");

			
//----------------------------------------------------------------------------------------------------------------------------------------------

			
			// Salva o ID do usuário no ThreadLocal (útil para auditoria/logs)
			if (userLogadoSessao != null) {
				utilFramework.getThreadLocal().set(userLogadoSessao.getEnt_codigo());
			}

			// Inicia transação Hibernate
			sf.getCurrentSession().beginTransaction();

			// Executa o processamento da requisição no servidor (JSF, Controller, etc.)
			chain.doFilter(request, response);

			// Realiza commit da transação Spring
			transactionManager.commit(status);

			// Se a transação Hibernate ainda estiver ativa, faz commit também
			
			
//----------------------------------------------------------------------------------------------------------------------------------------------

			
			if (sf.getCurrentSession().getTransaction().isActive()) {
				sf.getCurrentSession().flush(); // Persiste alterações pendentes
				sf.getCurrentSession().getTransaction().commit(); // Finaliza transação Hibernate
			}

			
//----------------------------------------------------------------------------------------------------------------------------------------------

			
			// Fecha a sessão Hibernate
			if (sf.getCurrentSession().isOpen()) {
				sf.getCurrentSession().close();
			}

//----------------------------------------------------------------------------------------------------------------------------------------------

			
			// Define codificação da resposta HTTP
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");

			
//----------------------------------------------------------------------------------------------------------------------------------------------

			
			
		} catch (Exception e) {
			// Em caso de erro, realiza rollback da transação Spring
			transactionManager.rollback(status);

			// Imprime erro no console (substituir por log profissional em produção)
			e.printStackTrace();

			// Rollback da transação Hibernate se ainda estiver ativa
			if (sf.getCurrentSession().getTransaction().isActive()) {
				sf.getCurrentSession().getTransaction().rollback();
			}

			// Fecha a sessão Hibernate, se ainda aberta
			if (sf.getCurrentSession().isOpen()) {
				sf.getCurrentSession().close();
			}

			
//----------------------------------------------------------------------------------------------------------------------------------------------
	
			
			
		} finally {
			// Garantia final: limpa e fecha a sessão Hibernate se ainda estiver aberta
			if (sf.getCurrentSession().isOpen()) {

				// Se ainda houver transação ativa 
				if (sf.getCurrentSession().beginTransaction().isActive()) {
					sf.getCurrentSession().flush(); // Persiste alterações
					sf.getCurrentSession().clear(); // Limpa o cache da sessão
				}

				// Fecha a sessão Hibernate
				if (sf.getCurrentSession().isOpen()) {
					sf.getCurrentSession().close();
				}
			}
		}
	}
}
