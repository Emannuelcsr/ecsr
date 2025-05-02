package br.com.project.geral.controller;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.servlet.http.HttpSession;

/**
 * Interface responsável por controlar sessões de usuários na aplicação.
 * 
 * <p>Marcada com @ApplicationScoped, significa que haverá apenas
 * uma instância dessa interface implementada em toda a aplicação,
 * compartilhada entre todos os usuários e requisições.</p>
 * 
 * <p>Essa interface define métodos para:</p>
 * <ul>
 *   <li>Adicionar uma sessão ao controle (addSession)</li>
 *   <li>Invalidar uma sessão específica (invalidateSession)</li>
 * </ul>
 * 
 * <p>Ela estende Serializable para permitir que possa ser armazenada
 * ou transferida com segurança em ambientes distribuídos ou persistentes.</p>
 * 
 * @author SeuNome
 */
@ApplicationScoped // Escopo JSF: instancia única compartilhada na aplicação inteira
public interface SessionController extends Serializable {

    /**
     * Adiciona uma nova sessão ao controle interno, associando-a a uma chave de login.
     *
     * @param KeyLoginUser chave identificadora do usuário (por exemplo, login ou ID)
     * @param httpSession sessão HTTP ativa do usuário
     */
    void addSession(String KeyLoginUser, HttpSession httpSession);

    /**
     * Invalida (encerra) a sessão associada à chave do usuário.
     * Caso a sessão exista, ela será encerrada e removida do controle.
     *
     * @param KeyLoginUser chave identificadora do usuário (por exemplo, login ou ID)
     */
    void invalidateSession(String KeyLoginUser);
}
