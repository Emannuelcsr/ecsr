package br.com.project.bean.view;

// Importações principais para o funcionamento do Bean JSF e Spring
import javax.faces.bean.ManagedBean; // Para usar como Managed Bean no JSF

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired; // Injeção automática de dependência Spring
import org.springframework.context.annotation.Scope; // Para definir escopo do Bean
import org.springframework.stereotype.Controller; // Para marcar como Controller Spring

// Importações internas do projeto para CRUD, entidade e carregamento de lista
import br.com.framwork.interfac.crud.InterfaceCrud;
import br.com.project.been.geral.BeanManagedViewAbstract;
import br.com.project.carregamento.lazy.CarregamentoLazyListForObject;
import br.com.project.geral.controller.EntidadeController;
import br.com.project.model.classes.Entidade;

/**
 * Classe FuncionarioBeanView - Bean de Visão para manipular dados de
 * Funcionário.
 *
 * Esta classe é um ManagedBean do JSF e também um Controller do Spring.
 * 
 * Escopo é de sessão (session), ou seja, a mesma instância do Bean é usada
 * durante toda a sessão do usuário.
 *
 * Herda funcionalidades básicas de CRUD da classe BeanManagedViewAbstract.
 * 
 * Faz uso do EntidadeController para operações de negócio no banco.
 */
@Controller // Marca como Controller gerenciado pelo Spring
@Scope("session") // Escopo de sessão para manter dados entre páginas
@ManagedBean(name = "funcionarioBeanView") // Nome para referenciar no JSF (ex: #{funcionarioBeanView})
public class FuncionarioBeanView extends BeanManagedViewAbstract {

	// Necessário para serialização do Bean, recomendação do Java para beans JSF com
	// escopo sessão
	private static final long serialVersionUID = 1L;

	// Objeto Entidade que representa o funcionário selecionado na interface
	private Entidade objetoSelecionado = new Entidade();

	// Lista com carregamento "lazy" (carregamento sob demanda) para otimizar
	// consultas grandes
	private CarregamentoLazyListForObject<Entidade> list = new CarregamentoLazyListForObject<Entidade>();

	// URLs para redirecionamento nas operações de navegação (editar/consultar)
	private String url = "/cadastro/cad_funcionario.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/find_funcionario.jsf?faces-redirect=true";

	// Contexto da sessão do usuário, injetado automaticamente pelo Spring
	@Autowired
	private ContextoBean contextoBean;

	// Controller que faz a ponte com o banco para a entidade Entidade
	@Autowired
	private EntidadeController entidadeController;

	// Getters e Setters para JSF poder acessar e modificar o objeto selecionado e a
	// lista

	public Entidade getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Entidade objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	/**
	 * Método que retorna a classe do objeto que este Bean manipula. É usado para
	 * facilitar operações genéricas na classe pai.
	 */
	@Override
	protected Class<Entidade> getClassImplement() {
		return Entidade.class;
	}

	/**
	 * Método para filtrar os registros na consulta. Aqui definimos que só queremos
	 * funcionários ativos (não inativos). Essa condição será usada nas queries para
	 * filtrar os dados.
	 */
	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return " and entity.ent_inativo = false ";
	}

	/**
	 * Retorna o controller responsável pelas operações de CRUD.
	 */
	@Override
	protected InterfaceCrud<?> getController() {
		return entidadeController;
	}

	// Getter e Setter para a lista carregada lazy

	public void setList(CarregamentoLazyListForObject<Entidade> list) {
		this.list = list;
	}

	public CarregamentoLazyListForObject<Entidade> getList() {
		return list;
	}

	/**
	 * Método para consultar (buscar) os funcionários. Reseta o objeto selecionado e
	 * limpa a lista para nova consulta. Atualiza a quantidade total de registros da
	 * consulta.
	 */
	@Override
	public void consultarEntidade() throws Exception {

		objetoSelecionado = new Entidade(); // limpa seleção

		list.clean(); // limpa lista para recarregar dados

		// Atualiza total de registros para paginação, usando query montada na
		// superclasse
		list.setTotalRegistroConsulta(super.totalRegistroConsulta(), super.getSqlLazyQuery());
	}

	/**
	 * Redireciona para a página de busca de funcionários.
	 * 
	 * @return String URL para redirecionar
	 */
	@Override
	public String redirecionarFindEntidade() throws Exception {
		return urlFind;
	}

	/**
	 * Método para excluir (deletar) um funcionário. Verifica se o objeto
	 * selecionado tem código válido. Executa exclusão via controller e remove da
	 * lista exibida. Após, limpa seleção e mostra mensagem de sucesso.
	 */
	@Override
	public void excluir() throws Exception {

		if (objetoSelecionado.getEnt_codigo() != null && objetoSelecionado.getEnt_codigo() > 0) {
			entidadeController.delete(objetoSelecionado); // exclui no banco
			list.remove(objetoSelecionado); // remove da lista da tela
			objetoSelecionado = new Entidade(); // limpa seleção
			sucesso(); // mensagem sucesso para usuário
		}
	}

	/**
	 * Método para realizar exclusão "soft" (inativação lógica) do funcionário. Não
	 * exclui fisicamente do banco, apenas marca como inativo. Após isso, remove da
	 * lista e limpa seleção.
	 */
	@Override
	public void softExcluir() throws Exception {

		if (objetoSelecionado.getEnt_codigo() != null && objetoSelecionado.getEnt_codigo() > 0) {
			entidadeController.inativar(objetoSelecionado); // marca como inativo no banco
			list.remove(objetoSelecionado); // remove da lista da tela
			objetoSelecionado = new Entidade(); // limpa seleção
			sucesso(); // mensagem sucesso para usuário
		}

	}

	/**
	 * Método para redirecionar para a tela de cadastro de novo funcionário.
	 * 
	 * @return String URL para redirecionar
	 */
	@Override
	public String novo() throws Exception {
		objetoSelecionado = new Entidade();

		list.clean();
		return url;
	}

	@Override
	public void saveNotReturn() throws Exception {

		if (!objetoSelecionado.getAcessos().contains("USER")) {

			objetoSelecionado.getAcessos().add("USER");
		}

		if (entidadeController.existeCpf(objetoSelecionado.getCpf())) {

			addMsg("Cpf ja existente");

		} else {

			objetoSelecionado = entidadeController.merge(objetoSelecionado);

			list.add(objetoSelecionado);

			objetoSelecionado = new Entidade();

			addMsg("Cadastro efetuado com sucesso");

		}
	}

	@Override
	public void saveEdit() throws Exception {

		objetoSelecionado = entidadeController.merge(objetoSelecionado);

		list.add(objetoSelecionado);

		objetoSelecionado = new Entidade();
		addMsg("Atualizado com sucesso");
	}

	@Override
	public String editar() throws Exception {

		list.clean();
		return url;

	}

	@Override
	public StreamedContent getArquivoReport() {

		super.setNomeRelatorioJasper("report_funcionario");
		super.setNomeRelatorioSaida("report_funcionario");
		try {
			super.setListDataBeanCollectionReport(entidadeController.findList(getClassImplement()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.getArquivoReport();
	}

}
