package br.com.project.been.geral;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.ViewMapListener;

/**
 * Classe responsável por registrar e executar callbacks associados ao ciclo de vida da View no JSF.
 * 
 * <p>Esta classe implementa {@link ViewMapListener} para capturar eventos de criação e destruição 
 * da View, possibilitando a execução de rotinas específicas no momento em que a tela (View) 
 * é destruída.</p>
 * 
 * <p>Utilizada em conjunto com a implementação de um escopo customizado ViewScope no Spring Framework.</p>
 * 
 * @author Emannuel
 */



//-----------------------------------------------------------------------------------------------------------------------------------



public class ViewScopeCallBackRegister implements ViewMapListener {

	/**
	 * Verifica se a origem do evento é uma instância de {@link UIViewRoot}.
	 * 
	 * <p>Somente eventos disparados pela raiz da View (UIViewRoot) serão processados.</p>
	 * 
	 * @param source Objeto origem do evento.
	 * @return true se for uma instância de UIViewRoot, false caso contrário.
	 */
	
	

	@Override
	public boolean isListenerForSource(Object source) {
		return source instanceof UIViewRoot;
	}

	
	
	
	
	
	
//-----------------------------------------------------------------------------------------------------------------------------------
	
	
	
	
	/**
	 * Processa os eventos do ciclo de vida da View JSF.
	 * 
	 * <p>Eventos tratados:</p>
	 * <ul>
	 *   <li>{@link PostConstructViewMapEvent} - Evento disparado após a criação da ViewMap.</li>
	 *   <li>{@link PreDestroyViewMapEvent} - Evento disparado antes da destruição da ViewMap.</li>
	 * </ul>
	 * 
	 * @param event Evento do sistema JSF.
	 * @throws AbortProcessingException Exceção lançada caso o processamento do evento deva ser interrompido.
	 */	

	
	
	@Override
	@SuppressWarnings("unchecked")
	public void processEvent(SystemEvent event) throws AbortProcessingException {

		if (event instanceof PostConstructViewMapEvent) {

			PostConstructViewMapEvent viewMapEvent = (PostConstructViewMapEvent) event;
			UIViewRoot uiViewRoot = (UIViewRoot) viewMapEvent.getComponent();

			// Cria e adiciona o Map para armazenar os callbacks no escopo da View
			uiViewRoot.getViewMap().put(ViewScope.VIEW_SCOPE_CALLBACKS, new HashMap<String, Runnable>());

		} else if (event instanceof PreDestroyViewMapEvent) {

			PreDestroyViewMapEvent preDestroyViewMapEvent = (PreDestroyViewMapEvent) event;
			UIViewRoot viewRoot = (UIViewRoot) preDestroyViewMapEvent.getComponent();

			Map<String, Runnable> callBacks = (Map<String, Runnable>) viewRoot.getViewMap()
					.get(ViewScope.VIEW_SCOPE_CALLBACKS);

			if (callBacks != null) {
				// Executa todos os callbacks registrados
				for (Runnable c : callBacks.values()) {
					c.run();
				}

				// Limpa o Map após execução
				callBacks.clear();
			}
		}
	}
}
