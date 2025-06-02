package br.com.project.been.geral;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

/**
 * Classe responsável por implementar um escopo customizado "ViewScope" 
 * para ser utilizado em aplicações JSF com integração Spring.
 * 
 * <p>
 * O ViewScope mantém os beans vivos enquanto o usuário permanecer na mesma 
 * página (view), sendo destruídos apenas ao navegar para outra página.
 * </p>
 * 
 * <p>
 * Esta implementação utiliza o ViewMap do JSF (FacesContext.getViewRoot().getViewMap())
 * como mecanismo de armazenamento dos objetos do escopo.
 * </p>
 * 
 * Referência:
 * - Documentação Oficial Spring: {@link org.springframework.beans.factory.config.Scope}
 * - Documentação JSF: {@link javax.faces.component.UIViewRoot#getViewMap()}
 * 
 * 
 */


//-----------------------------------------------------------------------------------------------------------------------------------


@SuppressWarnings("unchecked")
public class ViewScope implements Scope, Serializable {

	private static final long serialVersionUID = 1L;

	/** Constante que representa o nome do atributo de callbacks de destruição no ViewMap */
	public static final String VIEW_SCOPE_CALLBACKS = "viewScope.callBacks";

	
//-----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Recupera um bean do escopo de view (ViewScope) usando seu nome.
	 * 
	 * - Se o bean já estiver armazenado no ViewMap da página atual, ele é retornado.
	 * - Caso contrário, cria uma nova instância do bean utilizando a fábrica (ObjectFactory),
	 *   armazena essa instância no ViewMap e retorna ela.
	 * 
	 * Esse método garante que o bean viva enquanto o usuário estiver na mesma página.
	 * 
	 * @param name nome do bean que se deseja obter
	 * @param objectFactory fábrica responsável por criar o bean, caso não exista
	 * @return a instância do bean (recuperada ou criada)
	 */
	
	
	
	
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
	    
	    // Tenta recuperar do ViewMap um objeto (bean) pelo nome fornecido
	    Object instance = getViewMap().get(name);
	    
	    // Se não encontrar o bean no ViewMap (ou seja, ainda não foi criado)
	    if (instance == null) {
	        
	        // Usa a fábrica para criar uma nova instância do bean
	        instance = objectFactory.getObject();
	        
	        // Armazena essa nova instância no ViewMap, associando ao nome do bean
	        getViewMap().put(name, instance);
	    }
	    
	    // Retorna a instância do bean (recuperada ou criada)
	    return instance;
	}


	
//-----------------------------------------------------------------------------------------------------------------------------------

	
	
	/**
	 * Retorna o ID da conversação, utilizado pelo Spring para identificar o contexto atual.
	 * 
	 * @return String com o formato "ID_Sessao-ID_View"
	 */
	@Override
	public String getConversationId() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.getSessionId() + "-" + facesContext.getViewRoot().getViewId();
	}
	
	
	
//-----------------------------------------------------------------------------------------------------------------------------------

	
	
	/**
	 * Registra um callback de destruição que será executado quando o bean for removido do ViewScope.
	 *
	 * @param name nome do bean
	 * @param runnable callback a ser executado
	 */
	@Override
	public void registerDestructionCallback(String name, Runnable runnable) {
		Map<String, Runnable> callBacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
		if (callBacks != null) {
			callBacks.put(VIEW_SCOPE_CALLBACKS, runnable);
		}
	}

	
	

	
	/**
	 * Remove um bean do ViewScope. Caso existam callbacks de destruição, eles também são removidos.
	 *
	 * @param name nome do bean
	 * @return o bean removido ou null caso não exista
	 */
	@Override
	public Object remove(String name) {
		Object instance = getViewMap().remove(name);
		if (instance != null) {
			Map<String, Runnable> callBacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
			if (callBacks != null) {
				callBacks.remove(name);
			}
		}
		return instance;
	}

	
//-----------------------------------------------------------------------------------------------------------------------------------

	
	/**
	 * Resolve um objeto contextual baseado no nome fornecido.
	 * 
	 * @param name nome do objeto
	 * @return objeto contextual associado ao nome
	 */
	@Override
	public Object resolveContextualObject(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.resolveReference(name);
	}

	
//-----------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Recupera o ViewMap da view atual (página JSF).
	 * 
	 * @return mapa contendo os objetos armazenados na view atual
	 */
	private Map<String, Object> getViewMap() {
		return (Map<String, Object>) (FacesContext.getCurrentInstance() != null 
				? FacesContext.getCurrentInstance().getViewRoot().getViewMap() 
				: new HashMap<>());
	}
}
