package br.com.project.converters;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.framwork.hibernate.session.HibernateUtil;
import br.com.project.model.classes.Estado;

@FacesConverter(forClass = Estado.class)
public class EstadoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String codigo) {

		// ✅ Verifica se o valor (ID do Estado) não está nulo nem vazio.
		// Isso evita erros de conversão quando nenhum valor é selecionado no
		// selectOneMenu.
		if (codigo != null && !codigo.isEmpty()) {

			// ✅ Converte a String 'codigo' para Long e busca a entidade Estado no banco
			// usando o Hibernate.
			// Utiliza a sessão atual aberta pelo Hibernate, que é gerenciada via
			// OpenSessionInView.
			return (Estado) HibernateUtil.getCurrentSession().get(Estado.class, Long.parseLong(codigo));

		}

		return codigo;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object objeto) {

		// ✅ Verifica se o objeto passado não é nulo
		if (objeto != null) {
			
			// ✅ Faz o cast para o tipo Estado, pois sabemos que estamos lidando com esse tipo de objeto
			Estado c = (Estado) objeto;

			// ✅ Se o ID do estado não for nulo e maior que zero, converte para string e retorna.
			// Isso é importante porque apenas objetos já persistidos devem ter IDs válidos.
			return c.getEst_id() != null && c.getEst_id() > 0 ? c.getEst_id().toString() : null;
		}

		// ✅ Caso o objeto seja nulo, retorna null, que é o esperado pelo JSF
		return null;
	}
}
