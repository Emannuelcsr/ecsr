// Pacote onde a classe está localizada, seguindo convenção MVC: bean de controle de visão
package br.com.project.bean.view;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.project.geral.controller.EntidadeController;
import br.com.project.geral.controller.SessionController;
import br.com.project.model.classes.Entidade;

/**
 * Bean gerenciado pelo Spring com escopo de sessão, responsável por
 * fornecer o contexto de segurança (usuário autenticado).
 * 
 * Este bean é usado para acessar a autenticação atual do Spring Security
 * em qualquer ponto da camada de visão (JSF, por exemplo).
 */
@Scope(value = "session") // Define o escopo de sessão: o bean será mantido enquanto durar a sessão do usuário
@Component(value = "contextoBean") // Torna a classe um componente gerenciado com nome acessível no JSF: #{contextoBean}
public class ContextoBean implements Serializable {

    private static final long serialVersionUID = 1L; // Identificador padrão para serialização de beans em Java
    
    private static final String USER_LOGADO_SESSAO = "userLogadoSessao"; // Nome da chave na sessão para armazenar o usuário logado

    // Autowired: Injeção de dependências dos controladores responsáveis pela lógica de entidades e sessão
    @Autowired
    private EntidadeController entidadeController;
    
    @Autowired
    private SessionController sessionController;
    
    /**
     * Retorna o objeto de autenticação atual do Spring Security.
     * 
     * @return Authentication - contém informações do usuário autenticado
     * como nome de usuário, roles (perfis), status de login etc.
     */
    public Authentication getAuthentication() {
        // Acessa o contexto de segurança atual e retorna a autenticação ativa
        return SecurityContextHolder.getContext().getAuthentication(); 
    }

    /**
     * Retorna a entidade (usuário) logada na sessão, obtendo-a do contexto de segurança
     * e validando se a sessão está ativa e atualizada.
     * 
     * @return Entidade - A entidade (usuário) logada, ou null caso não exista
     * @throws Exception - Exceção lançada se houver algum erro ao acessar a entidade
     */
    public Entidade getEntidadeLogada() throws Exception {
        // Obtém a entidade da sessão usando a chave 'userLogadoSessao'
        Entidade entidade = (Entidade) getExternalContext().getSessionMap().get(USER_LOGADO_SESSAO);
        
        // Verifica se a entidade não está na sessão ou se o login é diferente do principal
        if (entidade == null || (entidade != null && !entidade.getEnt_login().equals(getUserPrincipal()))) {
            // Se o usuário estiver autenticado
            if (getAuthentication().isAuthenticated()) {
                
            	
            	// Grava no banco de dados a hora atual que o usuário fez o login
            	// Aqui, o método `updateUltimoAcessoUser` é chamado para atualizar o campo de "último acesso" do usuário no banco de dados.
            	entidadeController.updateUltimoAcessoUser(getAuthentication().getName());  // Passa o nome do usuário logado (via `getAuthentication().getName()`)

            	// Agora, o código busca as informações do usuário logado no banco de dados.
            	// O método `findUserLogado` é chamado para encontrar a entidade (usuário) correspondente ao login atual,
            	// e filtra para garantir que o usuário não esteja inativo (isso é feito pela condição "ent_inativo is false").
            	entidade = entidadeController.findUserLogado(getAuthentication().getName());  // Retorna a entidade do usuário logado
                
                // Coloca a entidade na sessão
                getExternalContext().getSessionMap().put(USER_LOGADO_SESSAO, entidade);
                
                // Adiciona a sessão no controle de sessão
                sessionController.addSession(entidade.getEnt_login(), (HttpSession) getExternalContext().getSession(true));
            }
        }   
        
        // Retorna a entidade (usuário logado) da sessão
        return entidade;
    }

    /**
     * Retorna o nome principal do usuário logado (principal) a partir do contexto de segurança.
     * 
     * @return String - Nome do usuário logado no Spring Security
     */
    private String getUserPrincipal() {
        return getExternalContext().getUserPrincipal().getName();
    }

    /**
     * Retorna o contexto externo do JSF, que permite acessar a sessão HTTP e outros detalhes de contexto.
     * 
     * @return ExternalContext - O contexto externo do JSF
     */
    public ExternalContext getExternalContext() {
        // Obtém a instância atual do FacesContext e retorna o contexto externo
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        
        return externalContext;
    }
    
    
    
    
    /**
     * Verifica se o usuário autenticado possui pelo menos uma das permissões especificadas.
     *
     * @param acessos Lista variável de strings representando as permissões a serem verificadas.
     * @return true se o usuário possui alguma das permissões informadas, false caso contrário.
     */
    public boolean possuiAcesso(String... acessos) {
        
        // Percorre cada permissão solicitada na lista de parâmetros 'acessos'
        for (String acesso : acessos) {
            
            // Obtém a lista de autoridades (permissões) do usuário autenticado e percorre uma a uma
            for (GrantedAuthority authority : getAuthentication().getAuthorities()) {
                
                // Compara a permissão atual do usuário com a permissão que estamos verificando,
                // usando trim() para evitar erros com espaços em branco
                if (authority.getAuthority().trim().equals(acesso.trim())) {
                    // Se encontrar a permissão, retorna true imediatamente, pois o usuário tem acesso
                    return true;
                }
            }
        }
        
        // Se nenhuma das permissões foi encontrada entre as autoridades do usuário, retorna false
        return false;
    }

    
}
