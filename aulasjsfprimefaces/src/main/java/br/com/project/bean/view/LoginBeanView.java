package br.com.project.bean.view;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.framwork.interfac.crud.InterfaceCrud;
import br.com.project.been.geral.BeanManagedViewAbstract;
import br.com.project.geral.controller.SessionController;
import br.com.srv.interfaces.SrvLogin;

/**
 * Bean responsável pelo controle de login do sistema.
 * Utiliza escopo de requisição e integra JSF com Spring.
 * 
 * Funciona como o "backing bean" para a tela de login, 
 * permitindo autenticar usuários e manipular sessões.
 */
@Controller // Indica que esse bean será gerenciado pelo Spring
@Scope(value = "request") // Escopo de requisição: novo bean a cada request
@ManagedBean(name = "loginBeanView") // Nome do bean acessível no JSF
public class LoginBeanView extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	/** Campo para armazenar o nome de usuário informado no login */
	private String username;

	/** Campo para armazenar a senha informada no login */
	private String password;

	/** Bean responsável por controle de sessão do usuário */
	@Resource
	private SessionController sessionController;

	/** Serviço de login injetado via Spring (verifica usuário/senha) */
	@Resource
	private SrvLogin srvLogin;

	
	
	
	
	
// Getters e setters padrão ---------------------------------------------------------------------------------------------------------------------
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
//-------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Método de login executado via action no botão da tela.
	 * Verifica se o usuário está autenticado com sucesso.
	 * 
	 * Se autenticado, invalida sessões anteriores do usuário e permite login.
	 * Se falhar, exibe mensagem de erro na interface.
	 * 
	 * @param event Evento de ação vindo do botão JSF
	 * @throws Exception Qualquer erro interno durante o processo
	 */
	public void invalidar(ActionEvent event) throws Exception {

		// Obtém o contexto Ajax atual do PrimeFaces
		RequestContext context = RequestContext.getCurrentInstance();

		FacesMessage message = null; // Mensagem JSF a ser exibida na tela
		boolean loggedIn = false;    // Status de login

		// Verifica se usuário e senha são válidos
		if (srvLogin.autentico(getUsername(), getPassword())) {

			// Se válido, invalida sessão anterior e permite login
			sessionController.invalidateSession(getUsername());
			loggedIn = true;

		} else {
			// Senha ou login inválidos → cria mensagem de erro
			loggedIn = false;
			message = new FacesMessage(
					FacesMessage.SEVERITY_WARN, 
					"Acesso Negado", 
					"Login ou Senha Incorretos");
		}

		// Se houver mensagem de erro, adiciona ao contexto da página
		if (message != null) {
			FacesContext.getCurrentInstance().addMessage("msg", message);
		}

		// Define o parâmetro "loggedIn" para uso no JavaScript da página
		context.addCallbackParam("loggedIn", loggedIn);
	}

	
	
//-------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Invalida a sessão de um usuário autenticado.
	 *
	 * <p>Este endpoint é acessado via requisição HTTP POST no caminho "invalidar_session".
	 * Ele tenta identificar o usuário logado atual com base nas informações da requisição HTTP
	 * e, caso o usuário seja identificado, invalida a sua sessão por meio do {@code sessionController}.</p>
	 *
	 * @param httpServletRequest objeto {@link HttpServletRequest} contendo informações da requisição HTTP,
	 *                           incluindo detalhes de autenticação do usuário.
	 * @throws Exception se ocorrer algum erro durante o processo de invalidação da sessão.
	 */
	 
	@RequestMapping(value = "**/invalidar_session", method = RequestMethod.POST)
	public void invalidarSessionMethod(HttpServletRequest httpServletRequest) throws Exception {
	    
	    String userLogadoSessao = null;

	    // Tenta obter o nome do usuário logado via getUserPrincipal()
	    if (httpServletRequest.getUserPrincipal() != null) {
	        userLogadoSessao = httpServletRequest.getUserPrincipal().getName();
	    }

	    // Se não conseguiu via getUserPrincipal(), tenta via getRemoteUser()
	    if (userLogadoSessao == null || (userLogadoSessao != null && userLogadoSessao.trim().isEmpty())) {
	        userLogadoSessao = httpServletRequest.getRemoteUser();
	    }

	    // Se o nome do usuário foi obtido com sucesso, invalida a sessão
	    if (userLogadoSessao != null && !userLogadoSessao.isEmpty()) {
	        sessionController.invalidateSession(userLogadoSessao);
	    }
	}


	@Override
	protected InterfaceCrud<?> getController() {
		return null;
	}

	@Override
	protected Class<?> getClassImplement() {
		return null;
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void softExcluir() throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	

}
