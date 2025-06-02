package br.com.project.been.geral;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import br.com.framwork.interfac.crud.InterfaceCrud;
import br.com.project.annotation.IdentificaCampoPesquisa;
import br.com.project.enums.CondicaoPesquisa;
import br.com.project.report.util.BeanReportView;
import br.com.project.util.all.UtilitariaRegex;

/**
 * Classe abstrata base para Beans de visualização. Fornece funcionalidades
 * reutilizáveis como: - Geração dinâmica de campos para pesquisa - Integração
 * com relatórios
 * 
 * Essa classe deve ser estendida por Beans de View como "CidadeBeanView".
 */
@Component // Componente gerenciado pelo Spring
public abstract class BeanManagedViewAbstract extends BeanReportView {

	private static final long serialVersionUID = 1L;

	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// --------------------------------------------------------------------------------------------------------------------
	// MÉTODOS ABSTRATOS: devem ser implementados nas subclasses
	// --------------------------------------------------------------------------------------------------------------------
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************

	/**
	 * Retorna a classe da entidade associada (ex: Cidade.class).
	 */
	protected abstract Class<?> getClassImplement();

	public abstract String condicaoAndParaPesquisa() throws Exception;

	/**
	 * Retorna o controller que implementa InterfaceCrud (ex: cidadeController).
	 */
	protected abstract InterfaceCrud<?> getController();

	public String valorPesquisa;

	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// --------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS PARA CONDIÇÃO DE PESQUISA
	// --------------------------------------------------------------------------------------------------------------------
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************

	public List<SelectItem> listaCondicaoPesquisa;
	public CondicaoPesquisa condicaoPesquisaSelecionado;

	/**
	 * Retorna a lista de condições de pesquisa disponíveis (ex: igual, contém,
	 * etc.)
	 */
	public List<SelectItem> getListaCondicaoPesquisa() {
		listaCondicaoPesquisa = new ArrayList<SelectItem>();

		for (CondicaoPesquisa condicaoPesquisa : CondicaoPesquisa.values()) {
			listaCondicaoPesquisa.add(new SelectItem(condicaoPesquisa, condicaoPesquisa.toString()));
		}

		return listaCondicaoPesquisa;
	}

	/**
	 * Retorna a condição de pesquisa atualmente selecionada pelo usuário.
	 */
	public CondicaoPesquisa getCondicaoPesquisaSelecionado() {
		return condicaoPesquisaSelecionado;
	}

	/**
	 * Define a condição de pesquisa atualmente selecionada.
	 */
	public void setCondicaoPesquisaSelecionado(CondicaoPesquisa condicaoPesquisaSelecionado) {
		this.condicaoPesquisaSelecionado = condicaoPesquisaSelecionado;
	}

	public void setValorPesquisa(String valorPesquisa) {
		this.valorPesquisa = valorPesquisa;
	}

	public String getValorPesquisa() {
		return valorPesquisa != null ? new UtilitariaRegex().retiraAcentos(valorPesquisa) : "";
	}

	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// --------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS PARA CAMPO DE PESQUISA
	// --------------------------------------------------------------------------------------------------------------------
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************

	/**
	 * Campo selecionado pelo usuário no menu de pesquisa. Este campo será
	 * preenchido automaticamente via JSF com o SelectItem.
	 */
	public ObjetoCampoConsulta objetoCampoConsultaSelecionado;

	/**
	 * Retorna o campo selecionado para pesquisa.
	 */
	public ObjetoCampoConsulta getObjetoCampoConsultaSelecionado() {
		return objetoCampoConsultaSelecionado;
	}

	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	/**
	 * Define o campo selecionado pelo usuário e preenche informações adicionais com
	 * base na anotação @IdentificaCampoPesquisa da entidade.
	 */
	public void setObjetoCampoConsultaSelecionado(ObjetoCampoConsulta objetoCampoConsultaSelecionado) {

		if (objetoCampoConsultaSelecionado != null) {
			for (Field field : getClassImplement().getDeclaredFields()) {
				if (field.isAnnotationPresent(IdentificaCampoPesquisa.class)) {
					if (objetoCampoConsultaSelecionado.getCampoBanco().equalsIgnoreCase(field.getName())) {

						String descricaoCampo = field.getAnnotation(IdentificaCampoPesquisa.class).descricaoCampo();

						objetoCampoConsultaSelecionado.setDescricao(descricaoCampo);
						objetoCampoConsultaSelecionado.setTipoClass(field.getType().getCanonicalName());
						objetoCampoConsultaSelecionado
								.setPrincipal(field.getAnnotation(IdentificaCampoPesquisa.class).principal());

						break;
					}
				}
			}
		}

		this.objetoCampoConsultaSelecionado = objetoCampoConsultaSelecionado;
	}

	// *******************************************************************************************************************************
	// *******************************************************************************************************************************
	// *******************************************************************************************************************************

	/**
	 * Lista de campos disponíveis para pesquisa, preenchida dinamicamente.
	 */
	List<SelectItem> listaCampoPesquisa;

	// --------------------------------------------------------------------------------------------------------------------
	// GERAÇÃO DINÂMICA DE CAMPOS PARA PESQUISA
	// --------------------------------------------------------------------------------------------------------------------

	/**
	 * Gera dinamicamente os campos da entidade anotados
	 * com @IdentificaCampoPesquisa. Esses campos serão exibidos em um
	 * <f:selectItems> em tela, por exemplo, dentro de um <p:selectOneMenu>.
	 * 
	 * @return Lista de SelectItem prontos para uso em componentes JSF.
	 */
	public List<SelectItem> getListaCampoPesquisa() {

		// Instancia a lista que será retornada, contendo os itens do selectOneMenu
		listaCampoPesquisa = new ArrayList<SelectItem>();

		// Lista temporária de objetos que representam os campos disponíveis para
		// pesquisa
		List<ObjetoCampoConsulta> listaTemporaria = new ArrayList<ObjetoCampoConsulta>();

		// Percorre todos os campos declarados da classe da entidade (via reflexão)
		for (Field field : getClassImplement().getDeclaredFields()) {

			// Verifica se o campo possui a anotação @IdentificaCampoPesquisa
			if (field.isAnnotationPresent(IdentificaCampoPesquisa.class)) {

				// Extrai as informações da anotação
				String descricao = field.getAnnotation(IdentificaCampoPesquisa.class).descricaoCampo();
				String campoPesquisa = field.getAnnotation(IdentificaCampoPesquisa.class).campoConsulta();
				int isPrincipal = field.getAnnotation(IdentificaCampoPesquisa.class).principal();

				// Cria um novo ObjetoCampoConsulta com os dados coletados
				ObjetoCampoConsulta objetoCampoConsulta = new ObjetoCampoConsulta();
				objetoCampoConsulta.setDescricao(descricao); // Descrição visível no menu
				objetoCampoConsulta.setCampoBanco(campoPesquisa); // Nome do campo no banco
				objetoCampoConsulta.setPrincipal(isPrincipal); // Se é o campo principal da pesquisa (1 ou 0)
				objetoCampoConsulta.setTipoClass(field.getType().getCanonicalName()); // Tipo do campo (ex:
																						// java.lang.String)

				// Adiciona na lista temporária
				listaTemporaria.add(objetoCampoConsulta);
			}
		}

		// Ordena os campos por prioridade (o campo com principal = 1 vai aparecer no
		// topo)
		orderReverse(listaTemporaria);

		// Transforma cada ObjetoCampoConsulta em um SelectItem para exibir no
		// <f:selectItems>
		for (ObjetoCampoConsulta objetoCampoConsulta : listaTemporaria) {
			listaCampoPesquisa.add(new SelectItem(objetoCampoConsulta));
			// OBS: Aqui o itemValue será o próprio objeto ObjetoCampoConsulta
		}

		// Retorna a lista final com todos os itens prontos para o componente de menu
		return listaCampoPesquisa;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// MÉTODO AUXILIAR: ORDENA OS CAMPOS POR PRIORIDADE
	// --------------------------------------------------------------------------------------------------------------------

	/**
	 * Ordena os campos de pesquisa de acordo com a flag "principal". Campos com
	 * principal = 1 aparecem antes dos demais.
	 */
	private void orderReverse(List<ObjetoCampoConsulta> listaTemporaria) {
		Collections.sort(listaTemporaria, new Comparator<ObjetoCampoConsulta>() {
			@Override
			public int compare(ObjetoCampoConsulta o1, ObjetoCampoConsulta o2) {
				return o1.getPrincipal().compareTo(o2.getPrincipal());
			}
		});
	}

	public String getSqlLazyQuery() throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append(" Select entity from  ");
		sql.append(getQueryConsulta());
		sql.append(" order by entity.");
		sql.append(objetoCampoConsultaSelecionado.getCampoBanco());

		return sql.toString();
	}

	private Object getQueryConsulta() throws Exception {

		valorPesquisa = new UtilitariaRegex().retiraAcentos(valorPesquisa);

		StringBuilder sql = new StringBuilder();

		sql.append(getClassImplement().getSimpleName());
		sql.append(" entity where ");
		sql.append(" retira_acentos(upper(cast(entity.");
		sql.append(objetoCampoConsultaSelecionado.getCampoBanco());
		sql.append(" as text))) ");

		if (condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.IGUAL_A.name())) {
			sql.append(" = retira_acentos(upper('");
			sql.append(valorPesquisa);
			sql.append("'))");
		} else if (condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.CONTEM.name())) {
			sql.append(" like retira_acentos(upper('%");
			sql.append(valorPesquisa);
			sql.append("%'))");

		} else if (condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.TERMINA_COM.name())) {
			sql.append(" like retira_acentos(upper('%");
			sql.append(valorPesquisa);
			sql.append("'))");

		} else if (condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.INICIA.name())) {
			sql.append(" like retira_acentos(upper('");
			sql.append(valorPesquisa);
			sql.append("%'))");

		}

		sql.append(" ");
		sql.append(condicaoAndParaPesquisa());
		
		
		return sql.toString();
	}

	public int totalRegistroConsulta() throws Exception {

		Query query = getController().obterQuery(" select count(entity) from   " + getQueryConsulta());

		Number resultado = (Number) query.uniqueResult();

		return resultado.intValue();
	}
}
