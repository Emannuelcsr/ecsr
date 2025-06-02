package br.com.project.carregamento.lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.framework.controller.crud.Controller;
import br.com.project.listener.ContextLoaderListenerECSRUtils;

/**
 * Classe genérica que estende LazyDataModel do PrimeFaces para permitir
 * carregamento de dados de forma paginada (lazy loading) em componentes como p:dataTable.
 *
 * @param <T> Tipo de entidade a ser carregada na tabela.
 */
public class CarregamentoLazyListForObject<T> extends LazyDataModel<T> {

	private static final long serialVersionUID = 1L;

	// Lista que conterá os dados da página atual
	private List<T> list = new ArrayList<T>();

	// Total de registros disponíveis para a consulta
	private int totalRegistroConsulta = 0;

	// Query dinâmica (JPQL ou HQL) utilizada para buscar os dados no banco
	private String query = null;

	// Controller responsável por executar a query. Recuperado do contexto de aplicação.
	private Controller controller = (Controller) ContextLoaderListenerECSRUtils.getBean(Controller.class);

	/**
	 * Método principal chamado automaticamente pelo PrimeFaces ao carregar uma nova
	 * página na tabela (lazy load).
	 *
	 * @param first      índice do primeiro registro da página atual
	 * @param pageSize   quantidade de registros por página
	 * @param sortField  campo usado para ordenação (não utilizado nesta versão)
	 * @param sortOrder  ordem (ascendente ou descendente) (não utilizado)
	 * @param filters    filtros aplicados pela tabela (não utilizado)
	 * @return lista de objetos da página atual
	 */
	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		
		try {
			// Verifica se a query foi definida
			if (query != null && !query.isEmpty()) {

				// Busca a lista de objetos usando a query e parâmetros de paginação
				list = (List<T>) controller.findListByQueryDinamic(query, first, pageSize);

				// Define o número total de registros da consulta
				if (totalRegistroConsulta == 0) {
					setRowCount(0); // Não há registros
				} else {
					setRowCount(totalRegistroConsulta); // Total informado
				}
			}

			// Define o tamanho da página no modelo
			setPageSize(pageSize);

		} catch (Exception e) {
			// Em caso de erro, imprime o stack trace para debug
			e.printStackTrace();
		}

		// Retorna a lista carregada para a página atual
		return (List<T>) list;
	}

	/**
	 * Define o total de registros da consulta e a query JPQL/HQL a ser usada.
	 * Deve ser chamado antes da primeira chamada ao método load().
	 *
	 * @param totalRegistroConsulta número total de registros da consulta
	 * @param queryDeBuscaConsulta  string da query de consulta a ser utilizada
	 */
	public void setTotalRegistroConsulta(int totalRegistroConsulta, String queryDeBuscaConsulta) {
		this.query = queryDeBuscaConsulta;
		this.totalRegistroConsulta = totalRegistroConsulta;
	}

	public int getTotalRegistroConsulta() {
		return totalRegistroConsulta;
	}
	
	/**
	 * Retorna a lista atual de objetos carregados.
	 *
	 * @return lista de objetos
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * Limpa o estado atual do modelo, removendo a query, zerando os registros
	 * e limpando a lista.
	 */
	public void clean() {
		this.query = null;
		this.totalRegistroConsulta = 0;
		this.list.clear();
	}

	
	
	/**
	 * Remove um objeto da lista atual.
	 *
	 * @param objetoSelecionado objeto a ser removido da lista
	 */
	public void remove(T objetoSelecionado) {
		this.list.remove(objetoSelecionado);
	}

	/**
	 * Adiciona um objeto à lista atual.
	 *
	 * @param objetoSelecionado objeto a ser adicionado à lista
	 */
	public void add(T objetoSelecionado) {
		this.list.add(objetoSelecionado);
	}

	/**
	 * Adiciona uma coleção de objetos à lista atual.
	 *
	 * @param collections lista de objetos a serem adicionados
	 */
	public void addAll(List<T> collections) {
		this.list.addAll(collections);
	}
}
