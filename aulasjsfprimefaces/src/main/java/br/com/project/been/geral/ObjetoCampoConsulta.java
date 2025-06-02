package br.com.project.been.geral;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Representa um campo que será usado nas telas de pesquisa do sistema.
 * 
 * Essa classe contém informações sobre:
 * - Descrição amigável (o texto que aparece para o usuário na interface)
 * - Nome do campo no banco de dados (para consultas SQL)
 * - Tipo do dado (ex: String, Date, Integer)
 * - Prioridade de exibição (para ordenar campos importantes no topo)
 * 
 * Implementa:
 * - Serializable para permitir transporte e armazenamento do objeto
 * - Comparator para permitir ordenar objetos dessa classe baseado na prioridade
 */
public class ObjetoCampoConsulta implements Serializable, Comparator<ObjetoCampoConsulta> {

    private static final long serialVersionUID = 1L;

    // Descrição legível para o usuário (exemplo: "Nome da Cidade")
    private String descricao;

    // Nome do campo no banco de dados (exemplo: "cid_nome")
    private String campoBanco;

    // Tipo do campo, pode ser uma Class (ex: String.class) ou nome do tipo (ex: "java.lang.String")
    private Object tipoClass;

    // Prioridade do campo para ordenação, menor valor = maior prioridade (ex: 1 = principal)
    private Integer principal;

    //-------------------------------------------------------------------------------------------------------------------------//

    /**
     * hashCode e equals baseados no campo 'campoBanco', para garantir que
     * objetos com o mesmo nome de campo sejam tratados como iguais.
     */

    @Override
    public int hashCode() {
        return Objects.hash(campoBanco);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;   // mesma referência -> iguais
        if (obj == null)
            return false;  // comparando com null -> não são iguais
        if (getClass() != obj.getClass())
            return false;  // classes diferentes -> não são iguais

        ObjetoCampoConsulta other = (ObjetoCampoConsulta) obj;
        return Objects.equals(campoBanco, other.campoBanco);
    }

    //-------------------------------------------------------------------------------------------------------------------------//

    // Getters e Setters para acessar e alterar os atributos privados

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCampoBanco() {
        return campoBanco;
    }

    public void setCampoBanco(String campoBanco) {
        this.campoBanco = campoBanco;
    }

    public Object getTipoClass() {
        return tipoClass;
    }

    public void setTipoClass(Object tipoClass) {
        this.tipoClass = tipoClass;
    }

    public Integer getPrincipal() {
        return principal;
    }

    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }

    //-------------------------------------------------------------------------------------------------------------------------//

    /**
     * Retorna a descrição como representação em texto do objeto.
     * Isso é útil, por exemplo, em componentes JSF que exibem o objeto.
     */
    @Override
    public String toString() {
        return getDescricao();
    }

    //-------------------------------------------------------------------------------------------------------------------------//

    /**
     * Método para comparar dois objetos ObjetoCampoConsulta baseado na prioridade 'principal'.
     * 
     * Retorna:
     * - valor negativo se o1 tem prioridade maior que o2 (ou seja, número menor)
     * - zero se são iguais
     * - valor positivo se o1 tem prioridade menor que o2
     * 
     * Isso permite ordenar listas desses objetos, por exemplo,
     * para mostrar primeiro os campos principais.
     */
    @Override
    public int compare(ObjetoCampoConsulta o1, ObjetoCampoConsulta o2) {
        return o1.getPrincipal().compareTo(o2.getPrincipal());
    }

    //-------------------------------------------------------------------------------------------------------------------------//
}
