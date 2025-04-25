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
 * 📌 Manipulador de exceções customizado para JSF.
 * 
 * Responsável por capturar, tratar e exibir mensagens de erro amigáveis
 * ao usuário final, além de redirecionar para páginas de erro customizadas
 * e logar problemas para análise posterior.
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    /** Handler padrão encapsulado */
    private ExceptionHandler wrapperd;

    /** Contexto JSF atual */
    final FacesContext facesContext = FacesContext.getCurrentInstance();

    /** Mapa de atributos da requisição */
    final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

    /** Manipulador de navegação */
    final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();


    // =======================================================================
    // 🔧 Construtor: recebe o ExceptionHandler original do JSF
    // =======================================================================

    
    
    public CustomExceptionHandler(ExceptionHandler exceptionHandler) {

        // Armazena o handler padrão
        this.wrapperd = exceptionHandler;
    	
        // Itera sobre todas as exceções não tratadas
        final java.util.Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

        while (iterator.hasNext()) {

            ExceptionQueuedEvent event = iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            // Obtém a exceção do contexto
            Throwable exeption = context.getException();

            try {
                
                // 🔎 Armazena a mensagem da exceção para exibição posterior
                requestMap.put("exceptionMessage", exeption.getMessage());

                // ⚠️ Trata exceções conhecidas com mensagens específicas
                if (exeption != null && exeption.getMessage() != null && exeption.getMessage().contains("ConstraintViolationException")) {

                    FacesContext.getCurrentInstance().addMessage("msg",
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Registro não pode ser removido por estar associado.", ""));

                } else if (exeption != null && exeption.getMessage() != null && exeption.getMessage().contains("org.hibernate.StaleObjectStateException")) {

                    FacesContext.getCurrentInstance().addMessage("msg",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Registro foi atualizado ou excluído por outro usuário. Consulte novamente.", ""));

                } else {

                    // 🧯 Tratamento de exceções genéricas
                    FacesContext.getCurrentInstance().addMessage("msg",
                        new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "O sistema se recuperou de um erro inesperado.", ""));

                    FacesContext.getCurrentInstance().addMessage("msg",
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Você pode continuar usando o sistema normalmente.", ""));

                    FacesContext.getCurrentInstance().addMessage("msg",
                        new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "O erro foi causado por:\n" + exeption.getMessage(), ""));

                    // 🚨 Alertas visuais via PrimeFaces
                    RequestContext.getCurrentInstance().execute("alert('O sistema se recuperou de um erro inesperado.')");
                    RequestContext.getCurrentInstance().showMessageInDialog(
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Erro", "O sistema se recuperou de um erro inesperado."));

                    // 🔁 Redirecionamento para página de erro
                    navigationHandler.handleNavigation(facesContext, null,
                        "/error/error.jsf?faces-redirect=true&expired=true");
                }

                // 🛑 Impede que o JSF continue com o ciclo de renderização
                facesContext.renderResponse();

            } finally {

                // 🧹 Garante que transações abertas sejam revertidas
                SessionFactory factory = HibernateUtil.getSessionFactory();
                if (factory.getCurrentSession().getTransaction().isActive()) {
                    factory.getCurrentSession().getTransaction().rollback();
                }

                // 📋 Loga o erro completo para desenvolvedores
                exeption.printStackTrace();

                // 🗑️ Remove o evento tratado da fila
                iterator.remove();
            }
        }



        // Continua com o tratamento padrão do JSF
        getWrapped().handle();
    }


    // =======================================================================
    // 📦 Implementação obrigatória da ExceptionHandlerWrapper
    // =======================================================================

    @Override
    public ExceptionHandler getWrapped() {
        return wrapperd;
    }

    @Override
    public void handle() throws FacesException {
        super.handle();
    }
}