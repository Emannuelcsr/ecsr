// 📦 Pacote onde o Bean está localizado
package br.com.project.bean.view;

// 📅 Classe utilitária para datas
import java.util.Date;

// 📦 JSF: Criação e gerenciamento de beans de interface
import javax.faces.bean.ManagedBean;

// 📦 Spring Framework: Injeção de dependências e escopos de beans
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

// 📦 Projeto interno: abstrações e controladores
import br.com.framwork.interfac.crud.InterfaceCrud;
import br.com.project.been.geral.BeanManagedViewAbstract;
import br.com.project.been.geral.EntidadeAtualizaSenhaBean;
import br.com.project.geral.controller.EntidadeController;
import br.com.project.model.classes.Entidade;

/**
 * 🧠 EntidadeBeanView
 * 
 * Classe do tipo **Bean de Visão** (JSF) que lida com a entidade `Entidade`. É
 * responsável por interações entre a camada de interface (XHTML/JSF) e os
 * dados/controladores do backend.
 * 
 * 🔐 Integra com o Spring Security para recuperar informações do usuário
 * logado. 📌 Escopo de sessão: cada usuário terá sua própria instância deste
 * bean.
 */
@Controller // 📌 Define esse bean como um componente controlado pelo Spring
@Scope(value = "session") // ♻️ Escopo de sessão - persiste durante a navegação do usuário
@ManagedBean(name = "entidadeBeanView") // 🌐 Permite uso em arquivos XHTML com #{entidadeBeanView}
public class EntidadeBeanView extends BeanManagedViewAbstract {

	// 🧠 Injeção do contexto de autenticação (Spring Security)
	@Autowired
	private ContextoBean contextoBean;

	// 🧠 Injeção do controller responsável por operações com a entidade
	@Autowired
	private EntidadeController entidadeController;

	private EntidadeAtualizaSenhaBean entidadeAtualizaSenhaBean = new EntidadeAtualizaSenhaBean();;

	// 🔐 Necessário para classes serializáveis (JSF pode serializar beans de
	// sessão)
	private static final long serialVersionUID = 1L;

	public void setEntidadeAtualizaSenhaBean(EntidadeAtualizaSenhaBean entidadeAtualizaSenhaBean) {
		this.entidadeAtualizaSenhaBean = entidadeAtualizaSenhaBean;
	}

	public EntidadeAtualizaSenhaBean getEntidadeAtualizaSenhaBean() {
		return entidadeAtualizaSenhaBean;
	}

	/**
	 * 🔒 Retorna o nome do usuário logado atualmente.
	 * 
	 * @return String - login do usuário autenticado (ex: "admin", "joao", etc.)
	 */
	public String getUsuarioLogadoSecurity() {
		return contextoBean.getAuthentication().getName();
	}

	/**
	 * 🕒 Recupera a data/hora do último acesso do usuário autenticado.
	 * 
	 * @return Date - data do último acesso
	 * @throws Exception - caso algo falhe ao acessar os dados do contexto
	 */
	public Date getUltimoAcesso() throws Exception {
		return contextoBean.getEntidadeLogada().getEnt_ultimoacesso();
	}

	// 🔧 Implementação da injeção do controller (usado pelo sistema de CRUD)
	@Override
	protected InterfaceCrud<Entidade> getController() {
		return entidadeController;
	}

	// 🧩 Define qual classe está sendo manipulada por esse Bean (Entidade)
	@Override
	protected Class<Entidade> getClassImplement() {
		return Entidade.class;
	}

	/**
	 * 🔎 Define uma condição extra para filtragem de dados.
	 * 
	 * Retorna null pois não está sendo usada no momento, mas pode ser sobrescrita
	 * no futuro para personalizar buscas.
	 */
	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return " AND entity.ent_inativo = false ";
	}

	public void updateSenha() throws Exception {
		Entidade entidadeLogada = contextoBean.getEntidadeLogada();

		String senhaAtual = entidadeAtualizaSenhaBean.getSenhaAtual();
		String novaSenha = entidadeAtualizaSenhaBean.getNovaSenha();
		String confirmaSenha = entidadeAtualizaSenhaBean.getConfirmaSenha();

		// Verifica se a senha atual confere com a do usuário logado
		if (!senhaAtual.equals(entidadeLogada.getEnt_senha())) {
			addMsg("Senha atual inválida.");
			return;
		}

		// Impede reuso imediato da senha
		if (senhaAtual.equals(novaSenha)) {
			addMsg("A nova senha deve ser diferente da senha atual.");
			return;
		}

		// Confirmação da nova senha
		if (!novaSenha.equals(confirmaSenha)) {
			addMsg("A confirmação da nova senha não confere.");
			return;
		}

		// Atualiza e persiste a nova senha
		entidadeLogada.setEnt_senha(novaSenha);
		entidadeController.saveOrUpdate(entidadeLogada);

		// Revalida se a senha foi realmente atualizada
		Entidade entidadeAtualizada = entidadeController.findPorId(getClassImplement(), entidadeLogada.getEnt_codigo());

		if (entidadeAtualizada.getEnt_senha().equals(novaSenha)) {
			sucesso();
		} else {
			addMsg("Não foi possível atualizar a senha. Tente novamente ou contate o suporte.");
			error();
		}

	}

	@Override
	public void softExcluir() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
