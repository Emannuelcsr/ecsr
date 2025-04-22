package br.com.project.report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.project.util.all.BeanViewAbstract;

/**
 * Classe abstrata base para todos os beans de relatórios da aplicação.
 * <p>
 * Esta classe provê estrutura e comportamento padrão para geração de relatórios
 * com JasperReports, integrando com PrimeFaces (para download) e
 * com o utilitário {@link ReportUtil}.
 * </p>
 * <p>
 * Deve ser estendida por beans concretos específicos de relatórios.
 * </p>
 *
 * @author SeuNome
 */



@Component
public abstract class BeanReportView extends BeanViewAbstract {

	
	
	private static final long serialVersionUID = 1L;

	
	
	/** Objeto de relatório gerado para download via PrimeFaces. */
	protected StreamedContent arquivoReport;

	/** Tipo de relatório a ser gerado (ex: PDF, XLS, etc). */
	protected int tipoRelatorio;

	/** Lista de dados que será utilizada como fonte para o relatório. */
	protected List<?> listDataBeanCollectionReport;

	/** Mapa de parâmetros a serem enviados ao arquivo .jasper. */
	protected HashMap<Object, Object> parametrosRelatorio;

	/** Nome do arquivo .jasper a ser utilizado como template. */
	protected String nomeRelatorioJasper = "dafault"; // (sic)

	/** Nome do arquivo de saída gerado para o usuário. */
	protected String nomeRelatorioSaida = "default";

	
//----------------------------------------------------------------------------------------------------------------------------------
	
	
	/** Classe utilitária responsável pela geração do relatório. */
	@Autowired
	private ReportUtil reportUtil;

	
	
	/**
	 * Construtor padrão que inicializa os parâmetros e a lista de dados com
	 * coleções vazias.
	 */		
	@SuppressWarnings("rawtypes")
	public BeanReportView() {
		parametrosRelatorio = new HashMap<Object, Object>();
		listDataBeanCollectionReport = new ArrayList();
	}

	
	
	
	/**
	 * Obtém a instância do utilitário de geração de relatórios.
	 *
	 * @return o {@link ReportUtil} injetado
	 */
	public ReportUtil getReportUtil() {
		return reportUtil;
	}

	
	
	
	/**
	 * Define o utilitário de geração de relatórios.
	 *
	 * @param reportUtil instância de {@link ReportUtil}
	 */
	public void setReportUtil(ReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}

	
	
	/**
	 * Gera o arquivo de relatório baseado nos dados, parâmetros e configurações
	 * fornecidas.
	 *
	 * @return objeto {@link StreamedContent} contendo o relatório gerado
	 * @throws Exception caso ocorra erro na geração
	 */
	public StreamedContent getArquivoReport() throws Exception {
		return getReportUtil().geraRelatorio(
			getListDataBeanCollectionReport(),
			getParametrosRelatorio(),
			getNomeRelatorioJasper(),
			getNomeRelatorioSaida(),
			getTipoRelatorio()
		);
	}
	
	
	

	/**
	 * Obtém o tipo de relatório a ser gerado.
	 *
	 * @return inteiro representando o tipo (ex: 1 = PDF, 2 = XLS, etc)
	 */
	public int getTipoRelatorio() {
		return tipoRelatorio;
	}

	
	
	
	/**
	 * Define o tipo de relatório a ser gerado.
	 *
	 * @param tipoRelatorio inteiro representando o tipo
	 */
	public void setTipoRelatorio(int tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	
	
	
	/**
	 * Obtém a lista de dados que será usada no relatório.
	 *
	 * @return lista de dados genéricos
	 */
	public List<?> getListDataBeanCollectionReport() {
		return listDataBeanCollectionReport;
	}

	
	
	
	/**
	 * Define a lista de dados para o relatório.
	 *
	 * @param listDataBeanCollectionReport lista contendo os dados
	 */
	public void setListDataBeanCollectionReport(List<?> listDataBeanCollectionReport) {
		this.listDataBeanCollectionReport = listDataBeanCollectionReport;
	}

	
	
	
	/**
	 * Obtém os parâmetros a serem enviados ao relatório.
	 *
	 * @return {@link HashMap} com os parâmetros
	 */
	public HashMap<Object, Object> getParametrosRelatorio() {
		return parametrosRelatorio;
	}

	
	
	
	/**
	 * Define os parâmetros para o relatório.
	 *
	 * @param parametrosRelatorio {@link HashMap} com os parâmetros
	 */
	public void setParametrosRelatorio(HashMap<Object, Object> parametrosRelatorio) {
		this.parametrosRelatorio = parametrosRelatorio;
	}

	
	
	
	/**
	 * Obtém o nome do arquivo .jasper (modelo) a ser utilizado.
	 *
	 * @return nome do arquivo jasper
	 */
	public String getNomeRelatorioJasper() {
		return nomeRelatorioJasper;
	}

	
	
	
	/**
	 * Define o nome do arquivo .jasper.
	 * Se o valor for nulo ou vazio, será definido como "default".
	 *
	 * @param nomeRelatorioJasper nome do arquivo jasper
	 */
	public void setNomeRelatorioJasper(String nomeRelatorioJasper) {
		if (nomeRelatorioJasper == null || nomeRelatorioJasper.isEmpty()) {
			nomeRelatorioJasper = "default";
		}
		this.nomeRelatorioJasper = nomeRelatorioJasper;
	}

	
	
	
	/**
	 * Obtém o nome do arquivo final que será gerado para download.
	 *
	 * @return nome do arquivo de saída
	 */
	public String getNomeRelatorioSaida() {
		return nomeRelatorioSaida;
	}

	
	
	/**
	 * Define o nome do arquivo final gerado.
	 * Se o valor for nulo ou vazio, será definido como "default".
	 *
	 * @param nomeRelatorioSaida nome do arquivo de saída
	 */
	public void setNomeRelatorioSaida(String nomeRelatorioSaida) {
		if (nomeRelatorioSaida == null || nomeRelatorioSaida.isEmpty()) {
			nomeRelatorioSaida = "default";
		}
		this.nomeRelatorioSaida = nomeRelatorioSaida;
	}
}
