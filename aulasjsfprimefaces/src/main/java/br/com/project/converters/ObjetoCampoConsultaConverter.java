package br.com.project.converters;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.project.been.geral.ObjetoCampoConsulta;

/**
 * Conversor JSF para a classe ObjetoCampoConsulta.
 * Permite converter de String para ObjetoCampoConsulta e vice-versa.
 * 
 * Esse conversor é necessário quando usamos o objeto diretamente em componentes
 * como <h:selectOneMenu> ou <p:selectOneMenu> com <f:selectItems>.
 */


@FacesConverter(forClass = ObjetoCampoConsulta.class) // Indica que este conversor é aplicado automaticamente para objetos desse tipo
public class ObjetoCampoConsultaConverter implements Converter, Serializable {
		
	private static final long serialVersionUID = 1L;
	
	

	/**
	 * Converte a String vinda da interface (do selectOneMenu) para o ObjetoCampoConsulta.
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		// Se o valor vindo da interface não é nulo ou vazio
		if (value != null && !value.isEmpty()) {
			
			
			
			// Divide a string em partes separadas por "*"
			String[] vals = value.split("\\*");
			
						
			
			// Cria e preenche o objeto com os dados da string
			ObjetoCampoConsulta objetoCampoConsulta = new ObjetoCampoConsulta();
			objetoCampoConsulta.setCampoBanco(vals[0]);   // Nome do campo no banco (ex: "cid_nome")
			objetoCampoConsulta.setTipoClass(vals[1]);    // Tipo da classe (ex: "java.lang.String")
			
			

			return objetoCampoConsulta;
		}
		
		
		
		// Se não veio nada, retorna nulo
		return null;
	}

	
	
	
	
	
	
	
	/**
	 * Converte um ObjetoCampoConsulta para uma String que será exibida no HTML.
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		// Se o objeto não é nulo
		if (value != null) {
			
			
			
			// Faz o cast para o tipo correto
			ObjetoCampoConsulta c = (ObjetoCampoConsulta) value;
			
			
			

			// Retorna uma String representando o objeto (usada como valor no <option>)
			return c.getCampoBanco() + "*" + c.getTipoClass();
		}
		
		

		// Caso contrário, retorna nulo
		return null;
	}
}
