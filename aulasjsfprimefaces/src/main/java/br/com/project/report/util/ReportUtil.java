package br.com.project.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Classe utilitária responsável pela geração de relatórios utilizando
 * JasperReports.
 * 
 * <p>
 * Permite exportar relatórios nos formatos: PDF, HTML, XLS e ODS.
 * </p>
 * 
 * <p>
 * Principais funcionalidades:
 * </p>
 * <ul>
 * <li>Localiza arquivos .jasper no projeto.</li>
 * <li>Gera relatórios com base em uma coleção de dados.</li>
 * <li>Define o formato de saída do relatório.</li>
 * <li>Exporta o relatório gerado como arquivo para download.</li>
 * </ul>
 * 
 * <p>
 * Frameworks utilizados:
 * </p>
 * <ul>
 * <li>JasperReports</li>
 * <li>PrimeFaces</li>
 * <li>Spring Framework (Component)</li>
 * <li>JSF (FacesContext)</li>
 * </ul>
 * 
 * @author Emannuel
 */


//----------------------------------------------------------------------------------------------------------------------------------------
@Component
public class ReportUtil implements Serializable {

	private static final long serialVersionUID = 1L;

//----------------------------------------------------------------------------------------------------------------------------------------
	
	/** Caracter de separação nos nomes dos arquivos gerados. */
	private static final String UNDERLINE = "_";

	/** Pasta padrão onde os relatórios ficam armazenados. */
	private static final String FOLDER_RELATORIOS = "/relatorios";

	/** Parâmetro padrão utilizado em sub-relatórios. */
	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";

	
	
//----------------------------------------------------------------------------------------------------------------------------------------
	
	/** Extensões dos arquivos exportados. */
	private static final String EXTENSION_ODS = "ods";
	private static final String EXTENSION_XLS = "xls";
	private static final String EXTENSION_HTML = "html";
	private static final String EXTENSION_PDF = "pdf";

//----------------------------------------------------------------------------------------------------------------------------------------
	
	/** Separador de diretórios conforme o sistema operacional. */
	private String SEPARATOR = File.separator;

	/** Constantes para definição do tipo do relatório exportado. */
	private static final int RELATORIO_PDF = 1;
	private static final int RELATORIO_EXCEL = 2;
	private static final int RELATORIO_HTML = 3;
	private static final int RELATORIO_ODS = 4;

//----------------------------------------------------------------------------------------------------------------------------------------
	
	/** Caracter para concatenação dos nomes dos arquivos. */
	private static final String PONTO = ".";

	/** Arquivo final que será disponibilizado para download. */
	private StreamedContent arquivoRetorno = null;

	private String caminhoArquivoRelatorio = null;
	private JRExporter tipoArquivoExportado = null;
	private String extensaoArquivoExportado = "";
	private File arquivoGerado = null;
	private String caminhoSubReport_Dir = "";

	
// ----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Gera um relatório em diversos formatos (PDF, HTML, ODS, Excel) utilizando
	 * JasperReports, a partir de uma lista de dados e parâmetros informados.
	 * <p>
	 * O arquivo gerado é armazenado temporariamente no servidor e disponibilizado
	 * como {@link StreamedContent} para download ou visualização na aplicação web.
	 *
	 * @param listDataBeanCollectionReport Lista de objetos (beans) que serão
	 *                                     utilizados como fonte de dados no
	 *                                     relatório.
	 * @param parametroRelatorio           Mapa contendo os parâmetros que serão
	 *                                     utilizados na construção do relatório.
	 * @param nomeRelatorioJasper          Nome do arquivo .jasper (modelo compilado
	 *                                     do relatório) que será utilizado.
	 * @param nomeRelatorioSaida           Nome base do arquivo de saída que será
	 *                                     gerado.
	 * @param tipoRelatorio                Tipo do relatório a ser gerado (PDF,
	 *                                     HTML, ODS, XLS), definido por constantes
	 *                                     (ex: RELATORIO_PDF).
	 * 
	 * @return {@link StreamedContent} contendo o arquivo gerado pronto para
	 *         download ou visualização.
	 * 
	 * @throws Exception Caso ocorra algum erro durante a geração do relatório,
	 *                   leitura do arquivo ou exportação.
	 *
	 *                   <p>
	 *                   <b>Observações:</b>
	 *                   </p>
	 *                   <ul>
	 *                   <li>O método identifica o caminho do relatório de forma
	 *                   dinâmica.</li>
	 *                   <li>Verifica a existência do arquivo .jasper
	 *                   informado.</li>
	 *                   <li>Suporta sub-relatórios através do parâmetro
	 *                   {@code SUBREPORT_DIR}.</li>
	 *                   <li>Define o tipo de exportação com base no parâmetro
	 *                   {@code tipoRelatorio}.</li>
	 *                   <li>Gera o arquivo temporário e o exclui automaticamente ao
	 *                   encerrar a aplicação.</li>
	 *                   </ul>
	 */

	
	
	public StreamedContent geraRelatorio(List<?> listDataBeanCollectionReport, HashMap parametroRelatorio,
			String nomeRelatorioJasper, String nomeRelatorioSaida, int tipoRelatorio) throws Exception {

// Cria um datasource baseado na lista de objetos recebida
		JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
				listDataBeanCollectionReport);

// Obtém o contexto do JSF
		FacesContext context = FacesContext.getCurrentInstance();

// Finaliza a resposta atual do JSF (importante ao gerar arquivos para download)
		context.responseComplete();

// Obtém o contexto do Servlet (necessário para acessar caminhos no servidor)
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

// Recupera o caminho absoluto da pasta de relatórios configurada (FOLDER_RELATORIOS)
		String caminhoRelatorio = servletContext.getRealPath(FOLDER_RELATORIOS);

// Cria uma referência ao arquivo .jasper do relatório
		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper");

// Verifica se o caminho ou arquivo do relatório não existem
		if (caminhoRelatorio == null || (caminhoRelatorio != null && caminhoRelatorio.isEmpty()) || (!file.exists())) {

// Caso não exista, busca o caminho do relatório via getResource (modo alternativo)
			caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();

// Remove o separador (para compatibilidade de caminho)
			SEPARATOR = "";
		}

// Adiciona o caminho das imagens como parâmetro do relatório
		parametroRelatorio.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);

// Monta o caminho completo do arquivo .jasper
		String caminhoArquivoJasper = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper";

// Carrega o relatório .jasper em memória
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivoJasper);

// Define o caminho dos sub-relatórios
		caminhoSubReport_Dir = caminhoRelatorio + SEPARATOR;

// Passa o caminho dos sub-relatórios como parâmetro
		parametroRelatorio.put(SUBREPORT_DIR, caminhoSubReport_Dir);

// Preenche o relatório com os dados e parâmetros
		JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametroRelatorio,
				jrBeanCollectionDataSource);

// Define o exportador e a extensão do arquivo de acordo com o tipo do relatório
		switch (tipoRelatorio) {
		case RELATORIO_PDF:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
			break;
		case RELATORIO_HTML:
			tipoArquivoExportado = new JRHtmlExporter();
			extensaoArquivoExportado = EXTENSION_HTML;
			break;
		case RELATORIO_ODS:
			tipoArquivoExportado = new JROdsExporter();
			extensaoArquivoExportado = EXTENSION_ODS;
			break;
		case RELATORIO_EXCEL:
			tipoArquivoExportado = new JRXlsExporter();
			extensaoArquivoExportado = EXTENSION_XLS;
			break;
		default:
// Caso não seja informado um tipo válido, gera PDF por padrão
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
			break;
		}

// Adiciona a data atual ao nome do arquivo de saída
		nomeRelatorioSaida += UNDERLINE + DateUtil.getDateAtualReportName();

// Monta o caminho completo do arquivo que será gerado
		caminhoArquivoRelatorio = caminhoRelatorio + SEPARATOR + nomeRelatorioSaida + PONTO + extensaoArquivoExportado;

// Cria a referência do arquivo de saída
		arquivoGerado = new File(caminhoArquivoRelatorio);

// Define qual relatório será exportado
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);

// Define o local (arquivo) onde o relatório será exportado
		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);

// Executa a exportação do relatório
		tipoArquivoExportado.exportReport();

// Solicita que o arquivo gerado seja excluído ao encerrar a aplicação
		arquivoGerado.deleteOnExit();

// Cria o inputStream com o conteúdo do arquivo gerado
		InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);

// Cria o objeto StreamedContent que será retornado (utilizado para download no JSF)
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/" + extensaoArquivoExportado,
				nomeRelatorioSaida + PONTO + extensaoArquivoExportado);

// Retorna o arquivo pronto para download
		return arquivoRetorno;
	}

}
