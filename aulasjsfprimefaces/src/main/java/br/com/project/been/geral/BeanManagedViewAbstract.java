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
 * reutilizáveis como: geração dinâmica de campos para pesquisa e integração com relatórios.
 * Essa classe deve ser estendida por Beans de View como "CidadeBeanView".
 */
@Component // Componente gerenciado pelo Spring
public abstract class BeanManagedViewAbstract extends BeanReportView {

    private static final long serialVersionUID = 1L;

    // *******************************************************************************************************************************
    // MÉTODOS ABSTRATOS - devem ser implementados nas subclasses
    // *******************************************************************************************************************************

    /**
     * Deve retornar a classe da entidade associada (ex: Cidade.class).
     */
    protected abstract Class<?> getClassImplement();

    /**
     * Define cláusulas adicionais para compor a condição de pesquisa com AND.
     */
    public abstract String condicaoAndParaPesquisa() throws Exception;

    /**
     * Retorna o controller que implementa InterfaceCrud (ex: cidadeController).
     */
    protected abstract InterfaceCrud<?> getController();

    // Valor digitado pelo usuário para consulta
    public String valorPesquisa;

    // *******************************************************************************************************************************
    // ATRIBUTOS E MÉTODOS RELACIONADOS À CONDIÇÃO DE PESQUISA
    // *******************************************************************************************************************************

    // Lista das condições de pesquisa disponíveis (Ex: IGUAL_A, CONTEM, etc)
    public List<SelectItem> listaCondicaoPesquisa;

    // Condição de pesquisa selecionada pelo usuário
    public CondicaoPesquisa condicaoPesquisaSelecionado;

    /**
     * Retorna a lista de condições de pesquisa disponíveis (ex: igual, contém, etc.)
     */
    public List<SelectItem> getListaCondicaoPesquisa() {
        listaCondicaoPesquisa = new ArrayList<SelectItem>();

        for (CondicaoPesquisa condicaoPesquisa : CondicaoPesquisa.values()) {
            listaCondicaoPesquisa.add(new SelectItem(condicaoPesquisa, condicaoPesquisa.toString()));
        }

        return listaCondicaoPesquisa;
    }

    public CondicaoPesquisa getCondicaoPesquisaSelecionado() {
        return condicaoPesquisaSelecionado;
    }

    public void setCondicaoPesquisaSelecionado(CondicaoPesquisa condicaoPesquisaSelecionado) {
        this.condicaoPesquisaSelecionado = condicaoPesquisaSelecionado;
    }

    public void setValorPesquisa(String valorPesquisa) {
        this.valorPesquisa = valorPesquisa;
    }

    /**
     * Retorna o valor de pesquisa tratado (sem acentos).
     */
    public String getValorPesquisa() {
        return valorPesquisa != null ? new UtilitariaRegex().retiraAcentos(valorPesquisa) : "";
    }

    // *******************************************************************************************************************************
    // ATRIBUTOS E MÉTODOS RELACIONADOS AO CAMPO DE PESQUISA (campo selecionado pelo usuário)
    // *******************************************************************************************************************************

    // Campo selecionado para consulta pelo usuário via selectOneMenu
    public ObjetoCampoConsulta objetoCampoConsultaSelecionado;

    public ObjetoCampoConsulta getObjetoCampoConsultaSelecionado() {
        return objetoCampoConsultaSelecionado;
    }

    /**
     * Define o campo selecionado para pesquisa com base na anotação da entidade.
     */
    public void setObjetoCampoConsultaSelecionado(ObjetoCampoConsulta objetoCampoConsultaSelecionado) {

        if (objetoCampoConsultaSelecionado != null) {
            for (Field field : getClassImplement().getDeclaredFields()) {
                if (field.isAnnotationPresent(IdentificaCampoPesquisa.class)) {
                    if (objetoCampoConsultaSelecionado.getCampoBanco().equalsIgnoreCase(field.getName())) {

                        // Define a descrição e tipo do campo consultado com base na anotação
                        String descricaoCampo = field.getAnnotation(IdentificaCampoPesquisa.class).descricaoCampo();

                        objetoCampoConsultaSelecionado.setDescricao(descricaoCampo);
                        objetoCampoConsultaSelecionado.setTipoClass(field.getType().getCanonicalName());
                        objetoCampoConsultaSelecionado.setPrincipal(field.getAnnotation(IdentificaCampoPesquisa.class).principal());

                        break;
                    }
                }
            }
        }

        this.objetoCampoConsultaSelecionado = objetoCampoConsultaSelecionado;
    }

    // *******************************************************************************************************************************
    // MÉTODO PARA GERAR DINAMICAMENTE OS CAMPOS DE PESQUISA A PARTIR DAS ANOTAÇÕES NAS ENTIDADES
    // *******************************************************************************************************************************

    // Lista de campos de pesquisa disponíveis
    List<SelectItem> listaCampoPesquisa;

    /**
     * Gera dinamicamente os campos da entidade anotados com @IdentificaCampoPesquisa.
     * Esses campos são usados como opções de pesquisa na interface.
     */
    public List<SelectItem> getListaCampoPesquisa() {

        listaCampoPesquisa = new ArrayList<SelectItem>();
        List<ObjetoCampoConsulta> listaTemporaria = new ArrayList<ObjetoCampoConsulta>();

        for (Field field : getClassImplement().getDeclaredFields()) {

            if (field.isAnnotationPresent(IdentificaCampoPesquisa.class)) {

                String descricao = field.getAnnotation(IdentificaCampoPesquisa.class).descricaoCampo();
                String campoPesquisa = field.getAnnotation(IdentificaCampoPesquisa.class).campoConsulta();
                int isPrincipal = field.getAnnotation(IdentificaCampoPesquisa.class).principal();

                ObjetoCampoConsulta objetoCampoConsulta = new ObjetoCampoConsulta();
                objetoCampoConsulta.setDescricao(descricao);
                objetoCampoConsulta.setCampoBanco(campoPesquisa);
                objetoCampoConsulta.setPrincipal(isPrincipal);
                objetoCampoConsulta.setTipoClass(field.getType().getCanonicalName());

                listaTemporaria.add(objetoCampoConsulta);
            }
        }

        // Ordena os campos pela flag principal (prioridade)
        orderReverse(listaTemporaria);

        for (ObjetoCampoConsulta objetoCampoConsulta : listaTemporaria) {
            listaCampoPesquisa.add(new SelectItem(objetoCampoConsulta));
        }

        return listaCampoPesquisa;
    }

    /**
     * Ordena os campos de pesquisa por prioridade.
     * Campos com principal = 1 aparecem primeiro.
     */
    private void orderReverse(List<ObjetoCampoConsulta> listaTemporaria) {
        Collections.sort(listaTemporaria, new Comparator<ObjetoCampoConsulta>() {
            @Override
            public int compare(ObjetoCampoConsulta o1, ObjetoCampoConsulta o2) {
                return o1.getPrincipal().compareTo(o2.getPrincipal());
            }
        });
    }

    // *******************************************************************************************************************************
    // MÉTODOS PARA GERAÇÃO DE CONSULTAS SQL DINÂMICAS
    // *******************************************************************************************************************************

    /**
     * Gera o SQL usado para fazer a consulta paginada (LazyDataModel, por exemplo).
     */
    public String getSqlLazyQuery() throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" Select entity from  ");
        sql.append(getQueryConsulta());
        sql.append(" order by entity.");
        sql.append(objetoCampoConsultaSelecionado.getCampoBanco());

        return sql.toString();
    }

    /**
     * Gera o SQL base da cláusula FROM + WHERE + filtro com retira_acentos().
     */
    private Object getQueryConsulta() throws Exception {

        valorPesquisa = new UtilitariaRegex().retiraAcentos(valorPesquisa);

        StringBuilder sql = new StringBuilder();
        sql.append(getClassImplement().getSimpleName());
        sql.append(" entity where ");
        sql.append(" retira_acentos(upper(cast(entity.");
        sql.append(objetoCampoConsultaSelecionado.getCampoBanco());
        sql.append(" as text))) ");

        // Aplica a condição de pesquisa selecionada
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

        // Adiciona cláusula AND adicional
        sql.append(" ");
        sql.append(condicaoAndParaPesquisa());

        return sql.toString();
    }

    /**
     * Executa a contagem total de registros para a consulta atual.
     */
    public int totalRegistroConsulta() throws Exception {
        Query query = getController().obterQuery(" select count(entity) from   " + getQueryConsulta());
        Number resultado = (Number) query.uniqueResult();
        return resultado.intValue();
    }
} 
