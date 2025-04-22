package br.com.project.listener;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Esta classe é uma extensão do {@link ContextLoaderListener} do Spring, e serve como um utilitário
 * para acessar o contexto do Spring em uma aplicação JSF, permitindo o acesso aos beans do Spring
 * de maneira simplificada. A classe é serializável, pois implementa a interface {@link Serializable}.
 * 
 * <p>Utiliza a anotação {@link ApplicationScoped}, indicando que a classe é gerenciada pelo 
 * contexto de aplicação do JSF, o que significa que ela será compartilhada em toda a aplicação.
 * </p>
 * 
 * <p>Ela fornece métodos estáticos para acessar beans Spring por nome ou por tipo.
 * </p>
 * 
 * <p>Esta classe deve ser utilizada quando for necessário acessar o contexto do Spring diretamente
 * em uma aplicação que utiliza JSF e Spring Framework simultaneamente.</p>
 * 
 * @see ContextLoaderListener
 * @see WebApplicationContext
 */
@ApplicationScoped
public class ContextLoaderListenerECSRUtils extends ContextLoaderListener implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Retorna o contexto da aplicação Spring.
     * 
     * Este método acessa o contexto do Spring na aplicação web e retorna a instância do 
     * {@link WebApplicationContext}, que contém todos os beans configurados no Spring.
     * 
     * @return {@link WebApplicationContext} - O contexto da aplicação Spring.
     */
    private static WebApplicationContext getWebContext() {
        return WebApplicationContextUtils.getWebApplicationContext(
                getCurrentWebApplicationContext().getServletContext());
    }

    
    
//----------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /**
     * Obtém um bean do Spring pelo nome de identificação.
     * 
     * Este método permite acessar um bean configurado no Spring usando o seu nome de identificação,
     * facilitando o acesso aos componentes necessários para a aplicação.
     * 
     * @param idNomeBean Nome do bean a ser acessado no contexto Spring.
     * @return {@link Object} - O bean correspondente ao nome fornecido.
     */
    public static Object getBean(String idNomeBean) {
        return getWebContext().getBean(idNomeBean);
    }

    
    
//------------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Obtém um bean do Spring pelo tipo da classe.
     * 
     * Este método permite acessar um bean configurado no Spring usando a classe como parâmetro,
     * facilitando o acesso a tipos específicos de beans.
     * 
     * @param classe Tipo do bean a ser acessado no contexto Spring.
     * @return {@link Object} - O bean correspondente ao tipo fornecido.
     */
    public static Object getBean(Class<?> classe) {
        return getWebContext().getBean(classe);
    }
}
