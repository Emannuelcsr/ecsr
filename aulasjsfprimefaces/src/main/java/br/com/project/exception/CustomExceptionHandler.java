package br.com.project.exception;

import java.util.Iterator;
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

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
        this.wrapped = exceptionHandler;
    }

    @Override
    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable exception = context.getException();

            try {
                requestMap.put("exceptionMessage", exception.getMessage());

                if (exception != null && exception.getMessage() != null &&
                        exception.getMessage().indexOf("ConstraintViolationException") != -1) {

                    facesContext.addMessage("msg",
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Registro não pode ser removido por estar associado.", ""));

                } else if (exception != null && exception.getMessage() != null &&
                        exception.getMessage().indexOf("org.hibernate.StaleObjectStateException") != -1 ) {

                    facesContext.addMessage("msg",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Registro foi atualizado ou excluído por outro usuário. Consulte novamente.", ""));

                } else {
                    facesContext.addMessage("msg",
                        new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "O sistema se recuperou de um erro inesperado.", ""));

                    facesContext.addMessage("msg",
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Você pode continuar usando o sistema normalmente!", ""));

                    facesContext.addMessage("msg",
                        new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "O erro foi causado por:\n" + exception.getMessage(), ""));

                    RequestContext.getCurrentInstance().execute("alert('O sistema se recuperou de um erro inesperado.')");

                    RequestContext.getCurrentInstance().showMessageInDialog(
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "O sistema se recuperou de um erro inesperado."));

                    navigationHandler.handleNavigation(facesContext, null,
                        "/error/error.jsf?faces-redirect=true&expired=true");
                }

                facesContext.renderResponse();

            } finally {
                try {
                    SessionFactory factory = HibernateUtil.getSessionFactory();
                    if (factory.getCurrentSession().getTransaction().isActive()) {
                        factory.getCurrentSession().getTransaction().rollback();
                    }
                } catch (Exception e) {
                    // Ignora se não conseguir dar rollback
                }

                exception.printStackTrace();
                iterator.remove();
            }
        }

        getWrapped().handle();
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }
}
