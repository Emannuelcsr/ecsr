package br.com.project.bean.view;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.framwork.interfac.crud.InterfaceCrud;
import br.com.project.been.geral.BeanManagedViewAbstract;
import br.com.project.geral.controller.EstadoController;
import br.com.project.model.classes.Estado;

@Controller
@Scope(value = "session")
@ManagedBean(name = "estadoBeanView")
public class EstadoBeanView extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EstadoController estadoController;
	
	
	public List<SelectItem> getEstados() throws Exception{
		
		return estadoController.getListEstado();
		
	}




	@Override
	protected InterfaceCrud<Estado> getController() {
		return estadoController;
	}




	@Override
	protected Class<Estado> getClassImplement() {
		return Estado.class;
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
