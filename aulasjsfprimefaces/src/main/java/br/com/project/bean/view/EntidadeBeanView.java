// Pacote padrão para beans de visão no projeto
package br.com.project.bean.view;

import java.util.Date;

import javax.faces.bean.ManagedBean; // Anotação JSF para criar um bean de interface

import org.springframework.beans.factory.annotation.Autowired; // Injeção de dependência via Spring
import org.springframework.context.annotation.Scope; // Define o escopo do bean Spring
import org.springframework.stereotype.Controller; // Marca a classe como um controller (análogo ao @Component, com semântica de controle)

import br.com.framwork.interfac.crud.InterfaceCrud;
import br.com.project.been.geral.BeanManagedViewAbstract; // Classe base genérica para beans de visão
import br.com.project.geral.controller.EntidadeController;
import br.com.project.model.classes.Cidade;
import br.com.project.model.classes.Entidade;

/**
 * Bean de visão (JSF + Spring) responsável por lidar com operações relacionadas
 * à entidade (exemplo genérico), incluindo acesso ao usuário logado.
 * 
 * Este bean está no escopo de sessão e pode ser acessado na camada de visão JSF
 * com o nome "entidadeBeanView".
 */
@Controller // Define como componente Spring com semântica de controller
@Scope(value = "session") // Define o ciclo de vida do bean como "por sessão de usuário"
@ManagedBean(name = "entidadeBeanView") // Disponibiliza o bean para o JSF com esse nome: #{entidadeBeanView}
public class EntidadeBeanView extends BeanManagedViewAbstract {

	// Injeta automaticamente o bean que fornece o contexto de autenticação do Spring Security
	@Autowired
	private ContextoBean contextoBean;
	
	@Autowired
	private EntidadeController entidadeController;

	private static final long serialVersionUID = 1L; // Padrão Java para serialização

	/**
	 * Retorna o nome (username) do usuário autenticado atualmente no sistema,
	 * utilizando o contexto de segurança do Spring Security.
	 * 
	 * @return String - nome do usuário logado (ex: "admin", "joao", etc.)
	 */
	public String getUsuarioLogadoSecurity() {
		// Acessa o ContextoBean que fornece a autenticação do Spring Security
		return contextoBean.getAuthentication().getName();
	}
	
	public Date getUltimoAcesso() throws Exception {
		return contextoBean.getEntidadeLogada().getEnt_ultimoacesso();
	}


	@Override
	protected InterfaceCrud<Entidade> getController() {
		return entidadeController;
	}

	@Override
	protected Class<Entidade> getClassImplement() {
		return Entidade.class;
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}
