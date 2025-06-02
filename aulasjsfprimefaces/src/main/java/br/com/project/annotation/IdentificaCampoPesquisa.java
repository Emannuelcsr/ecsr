package br.com.project.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation para marcar campos de uma entidade que poderão ser usados
 * como parâmetros em consultas ou pesquisas dinâmicas no sistema.
 * 
 * <p>
 * Essa annotation serve para definir metadados importantes sobre cada campo,
 * como uma descrição amigável para exibição na interface, o nome do campo
 * correspondente na consulta (banco de dados) e a prioridade de exibição
 * para ordenação em listas ou combos.
 * </p>
 * 
 * <h3>Exemplo de uso:</h3>
 * <pre>
 * &#64;IdentificaCampoPesquisa(
 *     descricaoCampo = "Nome do Cliente",
 *     campoConsulta = "nome",
 *     principal = 1
 * )
 * private String nome;
 * </pre>
 * 
 * <p>
 * Com essa annotation, frameworks ou códigos customizados podem refletir
 * sobre os campos da classe e montar filtros, consultas e interfaces
 * dinamicamente, reduzindo a necessidade de codificação repetitiva.
 * </p>
 * 
 * <h3>Detalhes da Annotation:</h3>
 * <ul>
 *   <li><b>@Target(ElementType.FIELD)</b>: só pode ser usada em campos (atributos)</li>
 *   <li><b>@Retention(RetentionPolicy.RUNTIME)</b>: estará disponível em tempo de execução para reflexão</li>
 *   <li><b>@Documented</b>: será documentada no JavaDoc gerado</li>
 * </ul>
 * 
 * 
 * 
 */
@Target(value = ElementType.FIELD) // Onde pode ser aplicada (campo/atributo)
@Retention(value = RetentionPolicy.RUNTIME) // Quando estará disponível (em tempo de execução)
@Documented // Para documentar a annotation no JavaDoc
public @interface IdentificaCampoPesquisa {

    /**
     * Descrição amigável do campo para exibição em telas e relatórios.
     * Exemplo: "Nome do Cliente"
     * 
     * @return String com a descrição do campo
     */
    String descricaoCampo();

    /**
     * Nome do campo correspondente na consulta ou banco de dados.
     * Exemplo: "nome", "cliente.nome", "cid_codigo"
     * 
     * @return String com o nome do campo para consulta
     */
    String campoConsulta();

    /**
     * Prioridade para ordenação na exibição dos campos.
     * Quanto menor o valor, maior a prioridade (aparece primeiro).
     * Valor padrão é 10000 para campos sem prioridade definida.
     * 
     * @return int com a prioridade do campo (default 10000)
     */
    int principal() default 10000;

}
