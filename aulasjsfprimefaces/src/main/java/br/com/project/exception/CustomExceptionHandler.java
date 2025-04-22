package br.com.project.exception;

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.hibernate.SessionFactory;
import org.primefaces.context.RequestContext;

import br.com.framwork.hibernate.session.HibernateUtil;

/**
 * Manipulador de exceções customizado para JSF, responsável por capturar,
 * tratar e exibir mensagens de erro amigáveis ao usuário final.
 *
 * <p>
 * Esta classe estende {@link ExceptionHandlerWrapper}, interceptando as
 * exceções não tratadas lançadas durante o ciclo de vida da requisição JSF.
 * </p>
 *
 * <p>
 * Ela exibe mensagens personalizadas de acordo com o tipo de erro,
 * especialmente tratando casos como:
 * <ul>
 *   <li>ConstraintViolationException (restrições de integridade no banco)</li>
 *   <li>StaleObjectStateException (concorrência otimista)</li>
 *   <li>Erros genéricos</li>
 * </ul>
 * </p>
 *
 * <p>
 * Também redireciona para uma página de erro personalizada
 * {@code /error/error.jsf}.
 * </p>
 *
 * @author SeuNome
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

	/** Handler padrão encapsulado. */
	private ExceptionHandler wrapperd;

	/** Contexto JSF atual. */
	final FacesContext facesContext = FacesContext.getCurrentInstance();

	/** Mapa de atributos da requisição (request scope). */
	final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

	/** Manipulador de navegação JSF. */
	final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

	/**
	 * Construtor que recebe o {@link ExceptionHandler} original do JSF.
	 *
	 * @param exceptionHandler o handler original a ser decorado
	 */
	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {

		// Itera sobre todas as exceções não tratadas
		final java.util.Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
		
		
		while (iterator.hasNext()) {
			ExceptionQueuedEvent event = iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			// Obtém a exceção do contexto
			Throwable exeption = context.getException();

			try {
				// Armazena a mensagem da exceção no escopo da requisição
				requestMap.put("exceptionMessage", exeption.getMessage());

				// Trata exceções conhecidas
				if (exeption != null && exeption.getMessage() != null
						&& exeption.getMessage().contains("ConstraintViolationException")) {

					FacesContext.getCurrentInstance().addMessage("msg",
							new FacesMessage(FacesMessage.SEVERITY_WARN,
									"Registro não pode ser removido por estar associado.", ""));

				} else if (exeption != null && exeption.getMessage() != null
						&& exeption.getMessage().contains("org.hibernate.StaleObjectStateException")) {

					FacesContext.getCurrentInstance().addMessage("msg",
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Registro foi atualizado ou excluído por outro usuário. Consulte novamente.", ""));

				} else {
					// Mensagens para exceções genéricas
					FacesContext.getCurrentInstance().addMessage("msg",
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"O sistema se recuperou de um erro inesperado.", ""));

					FacesContext.getCurrentInstance().addMessage("msg",
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Você pode continuar usando o sistema normalmente.", ""));

					FacesContext.getCurrentInstance().addMessage("msg",
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"O erro foi causado por:\n" + exeption.getMessage(), ""));

					// Alertas JavaScript (caso não haja redirecionamento)
					RequestContext.getCurrentInstance().execute("alert('O sistema se recuperou de um erro inesperado.')");

					RequestContext.getCurrentInstance().showMessageInDialog(
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Erro", "O sistema se recuperou de um erro inesperado."));

					// Redireciona para página de erro amigável
					navigationHandler.handleNavigation(facesContext, null,
							"/error/error.jsf?faces-redirect=true&expired=true");
				}

				// Força o JSF a pular a fase de renderização
				facesContext.renderResponse();

			} finally {
				// Garante que transações ativas sejam revertidas
				SessionFactory factory = HibernateUtil.getSessionFactory();
				if (factory.getCurrentSession().getTransaction().isActive()) {
					factory.getCurrentSession().getTransaction().rollback();
				}

				// Log da pilha de erro para desenvolvedores
				exeption.printStackTrace();

				// Remove a exceção tratada da fila
				iterator.remove();
			}
		}
		// Armazena o handler original
		this.wrapperd = exceptionHandler;

		// Chama o handler padrão
		getWrapped().handle();

	}

	/**
	 * Retorna o handler JSF original encapsulado por este decorador.
	 *
	 * @return o handler original
	 */
	@Override
	public ExceptionHandler getWrapped() {
		return wrapperd;
	}

	/**
	 * Método sobrescrito para delegar ao handler padrão.
	 * 
	 * @throws FacesException se houver erro durante o tratamento
	 */
	@Override
	public void handle() throws FacesException {
		super.handle();
	}
}
