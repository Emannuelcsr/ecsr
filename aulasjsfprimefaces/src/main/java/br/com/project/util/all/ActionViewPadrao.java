package br.com.project.util.all;

import java.io.Serializable;

import javax.annotation.PostConstruct;

/**
 * Interface padrão que define o contrato de ações comuns em beans de visão (JSF ManagedBeans ou Spring Components).
 * <p>
 * Esta interface é voltada para operações de CRUD, controle de estado e mensagens em páginas JSF.
 * Deve ser implementada por classes que representem views (telas) com ações padronizadas.
 * </p>
 *
 * <p><b>Boas práticas:</b> Cada método deve ser tratado com try/catch e exibir mensagens claras ao usuário final.</p>
 *
 * @author SeuNome
 */
public interface ActionViewPadrao extends Serializable {

    /**
     * Limpa a lista de entidades carregadas na tela.
     *
     * @throws Exception caso ocorra erro na operação
     */
	
	
	
	
    void limparLista() throws Exception;

    /**
     * Salva a entidade e retorna para a mesma view ou outra view definida.
     *
     * @return nome da view para redirecionamento
     * @throws Exception caso ocorra erro na operação
     */
    
    
    
    
    String save() throws Exception;

    /**
     * Salva a entidade sem realizar redirecionamento.
     *
     * @throws Exception caso ocorra erro na operação
     */      
    
    void saveNotReturn() throws Exception;

    /**
     * Salva ou atualiza a entidade em modo de edição.
     *
     * @throws Exception caso ocorra erro na operação
     */
    void saveEdit() throws Exception;

    /**
     * Exclui a entidade da base de dados.
     *
     * @throws Exception caso ocorra erro na operação
     */
    void excluir() throws Exception;

    /**
     * Ativa a entidade (normalmente muda seu status para ativo).
     *
     * @return nome da view após ativação
     * @throws Exception caso ocorra erro na operação
     */
    String ativar() throws Exception;

    /**
     * Inicializa a view com dados padrão.
     * Este método é anotado com {@link PostConstruct}, sendo executado automaticamente após a injeção de dependências.
     *
     * @return nome da view para redirecionamento inicial
     * @throws Exception caso ocorra erro na operação
     */
    @PostConstruct
    String novo() throws Exception;

    /**
     * Edita a entidade selecionada na view.
     *
     * @return nome da view de edição
     * @throws Exception caso ocorra erro na operação
     */
    String editar() throws Exception;

    /**
     * Define atributos da view como nulos, evitando dados residuais entre requisições.
     *
     * @throws Exception caso ocorra erro na operação
     */
    void setarVariaveisNulas() throws Exception;

    /**
     * Realiza a consulta da entidade na base de dados.
     *
     * @throws Exception caso ocorra erro na operação
     */
    void consultarEntidade() throws Exception;

    /**
     * Atualiza o status da operação (por exemplo, sucesso, erro ou validação).
     *
     * @param a status da persistência definido pelo enum {@code EstatusPersistencia}
     * @throws Exception caso ocorra erro na operação
     */
    void statusOperation(EstatusPersistencia a) throws Exception;

    /**
     * Redireciona para a view de criação de nova entidade.
     *
     * @return nome da nova view
     * @throws Exception caso ocorra erro na operação
     */
    String redirecionarNewEntidade() throws Exception;

    /**
     * Redireciona para a view de pesquisa ou listagem de entidades.
     *
     * @return nome da view de listagem
     * @throws Exception caso ocorra erro na operação
     */
    String redirecionarFindEntidade() throws Exception;

    /**
     * Adiciona uma mensagem na interface gráfica do usuário.
     *
     * @param msg mensagem a ser exibida
     * @throws Exception caso ocorra erro na operação
     */
    void addMsg(String msg) throws Exception;
}
