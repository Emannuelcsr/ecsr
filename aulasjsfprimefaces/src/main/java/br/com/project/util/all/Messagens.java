package br.com.project.util.all;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Classe utilitária responsável por exibir mensagens na interface do usuário (Front-End) 
 * em aplicações JSF (JavaServer Faces).
 * 
 * <p>Essa classe facilita a exibição de mensagens no padrão do {@link FacesMessage} com diferentes níveis de severidade:</p>
 * 
 * <ul>
 *   <li>{@link FacesMessage.SEVERITY_INFO} - Informações</li>
 *   <li>{@link FacesMessage.SEVERITY_WARN} - Alertas/Avisos</li>
 *   <li>{@link FacesMessage.SEVERITY_ERROR} - Erros</li>
 *   <li>{@link FacesMessage.SEVERITY_FATAL} - Erros Críticos</li>
 * </ul>
 * 
 * <p>As mensagens são enviadas para o componente de ID "msg" da página JSF.</p>
 * 
 * 
 */



//--------------------------------------------------------------------------------------------------------------------------------------------

public abstract class Messagens extends FacesContext implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Construtor padrão. */
	public Messagens() {
	}

	
	
//--------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Obtém a instância atual do {@link FacesContext}.
	 * 
	 * @return instância do FacesContext ou null.
	 */
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();		
	}

	
	
	
//--------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Verifica se o {@link FacesContext} está válido (diferente de null).
	 * 
	 * @return true se o contexto estiver disponível, false caso contrário.
	 */
	private static boolean facesContextValido() {
		return getFacesContext() != null;
	}
	
	
//--------------------------------------------------------------------------------------------------------------------------------------------


	/**
	 * Exibe mensagem de aviso (WARN) na tela.
	 * 
	 * @param msg Texto da mensagem.
	 */
	public static void msgSeverityWarn(String msg) {
		if (facesContextValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
		}
	}

	
//--------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Exibe mensagem de erro fatal (FATAL) na tela.
	 * 
	 * @param msg Texto da mensagem.
	 */
	public static void msgSeverityFatal(String msg) {
		if (facesContextValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg));
		}
	}

	
//--------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Exibe mensagem de erro (ERROR) na tela.
	 * 
	 * @param msg Texto da mensagem.
	 */
	public static void msgSeverityError(String msg) {
		if (facesContextValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	
//--------------------------------------------------------------------------------------------------------------------------------------------


	/**
	 * Exibe mensagem informativa (INFO) na tela.
	 * 
	 * @param msg Texto da mensagem.
	 */
	public static void msgSeverityInf(String msg) {
		if (facesContextValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
		}
	}
	
	
//--------------------------------------------------------------------------------------------------------------------------------------------


	/**
	 * Exibe mensagem padrão de erro fatal, com texto definido na constante {@link Constante#ERRO_NA_OPERACAO}.
	 */
	public static void erroNaOperacao() {
		if (facesContextValido()) {
			msgSeverityFatal(Constante.ERRO_NA_OPERACAO);
		}
	}
	
	
//--------------------------------------------------------------------------------------------------------------------------------------------


	/**
	 * Exibe mensagem padrão de sucesso, com texto definido na constante {@link Constante#SUCESSO}.
	 */
	public static void sucesso() {
		if (facesContextValido()) {
			msgSeverityInf(Constante.SUCESSO);
		}
	}
	
	
//--------------------------------------------------------------------------------------------------------------------------------------------


	/**
	 * Exibe uma mensagem de resposta baseada no {@link EstatusPersistencia} retornado.
	 * 
	 * <p>Regras:</p>
	 * <ul>
	 *   <li>SUCESSO = Exibe mensagem de sucesso</li>
	 *   <li>OBJETO_REFERENCIADO = Exibe mensagem informando referência cruzada</li>
	 *   <li>Outros casos = Exibe mensagem de erro fatal</li>
	 * </ul>
	 * 
	 * @param estatusPersistencia Enum com status da operação.
	 */
	public static void responseOperation(EstatusPersistencia estatusPersistencia) {
		if (estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.SUCESSO)) {
			sucesso();
		} else if (estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.OBJETO_REFERENCIADO)) {
			msgSeverityFatal(EstatusPersistencia.OBJETO_REFERENCIADO.toString());
		} else {
			erroNaOperacao();
		}
	}

	
//--------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Exibe uma mensagem simples, sem severidade definida.
	 * 
	 * @param msg Texto da mensagem.
	 */
	public static void msg(String msg) {
		if (facesContextValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(msg));
		}
	}

}
