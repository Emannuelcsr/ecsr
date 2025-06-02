package br.com.project.bean.view;

import javax.faces.bean.ManagedBean;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.framwork.interfac.crud.InterfaceCrud;
import br.com.project.been.geral.BeanManagedViewAbstract;
import br.com.project.carregamento.lazy.CarregamentoLazyListForObject;
import br.com.project.geral.controller.CidadeController;
import br.com.project.model.classes.Cidade;

@Controller
@Scope(value = "session")
@ManagedBean(name = "cidadeBeanView")
public class CidadeBeanView extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private String url = "/cadastro/cad_cidade.jsf?faces-redirect=true";

	private String urlFind = "/cadastro/find_cidade.jsf?faces-redirect=true";

	private Cidade objetoSelecionado = new Cidade();

	private CarregamentoLazyListForObject<Cidade> list = new CarregamentoLazyListForObject<Cidade>();

	
//--------------------------------------------------------------------------------------------------------------------------------

	@Autowired
	private CidadeController cidadeController;

	public Cidade getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Cidade objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	
	


//--------------------------------------------------------------------------------------------------------------------------------

	public CarregamentoLazyListForObject<Cidade> getList() {
		return list;
	}

	public void setList(CarregamentoLazyListForObject<Cidade> list) {
		this.list = list;
	}

	@Override
	public String save() throws Exception {

		objetoSelecionado = cidadeController.merge(objetoSelecionado);
		novo();

		return "";
	}

	@Override
	public String novo() throws Exception {

		setarVariaveisNulas();

		return url;
	}

	@Override
	public void setarVariaveisNulas() throws Exception {
		list.clean();
		objetoSelecionado = new Cidade();
	}

	public String editar() throws Exception {

		return url;
	}

	public void excluir() throws Exception {
		objetoSelecionado = (Cidade) cidadeController.getSession().get(getClassImplement(),
				objetoSelecionado.getCid_codigo());
		cidadeController.delete(objetoSelecionado);
		novo();
		list.remove(objetoSelecionado);
		sucesso();

	}

	public void saveNotReturn() throws Exception {

		objetoSelecionado = cidadeController.merge(objetoSelecionado);
		list.add(objetoSelecionado);
		objetoSelecionado = new Cidade();
		sucesso();
	}

	@Override
	public void saveEdit() throws Exception {
		saveNotReturn();

	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return urlFind;
	}

	@Override
	protected InterfaceCrud<Cidade> getController() {
		return cidadeController;
	}

	@Override
	protected Class<Cidade> getClassImplement() {
		return Cidade.class;
	}

	@Override
	public StreamedContent getArquivoReport() {

		super.setNomeRelatorioJasper("report_cidade");
		super.setNomeRelatorioSaida("report_cidade");
		try {
			super.setListDataBeanCollectionReport(cidadeController.findList(getClassImplement()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.getArquivoReport();
	}

	public String navegarParaCidade() throws Exception {
		novo(); // opcional, para limpar dados
		return url;
	}

	@Override
	public void consultarEntidade() throws Exception {
		
		objetoSelecionado = new Cidade();
		
		list.clean();
		
		list.setTotalRegistroConsulta(super.totalRegistroConsulta(), super.getSqlLazyQuery());
		
 	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
	}

}
