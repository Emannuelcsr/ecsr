package br.com.project.bean.view;

import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.framwork.interfac.crud.InterfaceCrud;
import br.com.project.been.geral.BeanManagedViewAbstract;
import br.com.project.geral.controller.EntidadeController;
import br.com.project.geral.controller.MensagemController;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.Mensagem;

@Controller
@Scope(value = "session")
@ManagedBean(name = "mensagemBeanView")
public class MensagemBeanView extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Mensagem objetoSelecionado = new Mensagem();

	@Autowired
	private EntidadeController entidadeController;

	@Autowired
	private MensagemController mensagemController;

	@Autowired
	private ContextoBean contextoBean;

	public Mensagem getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Mensagem objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	@Override
	public String novo() throws Exception {

		objetoSelecionado = new Mensagem();
		objetoSelecionado.setUsr_origem(contextoBean.getEntidadeLogada());

		return "";
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
		return null;
	}

	@RequestMapping("**/buscarUsuarioDestinoMsg")
	public void buscarUsuarioDestinoMsg(HttpServletResponse httpServletResponse,
			@RequestParam(value = "codEntidade") Long codEntidade) throws Exception {

		Entidade entidade = entidadeController.findPorId(Entidade.class, codEntidade);

		if (entidade != null) {

			objetoSelecionado.setUsr_destino(entidade);

			httpServletResponse.getWriter().write(entidade.getJson().toString());

		}
	}

	@Override
	public void saveNotReturn() throws Exception {

		if (objetoSelecionado.getUsr_destino().getEnt_codigo()
				.equals(objetoSelecionado.getUsr_destino().getEnt_codigo())) {
			addMsg("Origem não pode ser igual ao destino.");
			return;
		}

		mensagemController.merge(objetoSelecionado);
		novo();
		addMsg("Mensagem enviada com sucesso!");
	}

	@Override
	public void softExcluir() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
