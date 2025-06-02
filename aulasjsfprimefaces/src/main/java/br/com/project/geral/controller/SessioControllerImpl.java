package br.com.project.geral.controller;

import java.util.HashMap;

import javax.faces.bean.ApplicationScoped;
import javax.servlet.http.HttpSession;

/**
 * Implementação da interface {@link SessionController}, responsável
 * por controlar sessões HTTP ativas na aplicação.
 * 
 * <p>Este bean é anotado com {@code @ApplicationScoped}, ou seja,
 * terá uma única instância compartilhada em toda a aplicação web.</p>
 * 
 * <p>Utiliza um {@code HashMap<String, HttpSession>} interno
 * para associar uma chave identificadora do usuário (login ou ID)
 * à sessão HTTP correspondente.</p>
 * 
 * <p><b>Importante:</b> O uso de {@code ApplicationScoped} com sessões
 * exige cuidado com concorrência, pois múltiplos usuários poderão acessar
 * este bean simultaneamente. Em aplicações críticas, considere utilizar
 * estruturas concorrentes como {@code ConcurrentHashMap}.</p>
 * 
 * @author SeuNome
 */
@ApplicationScoped
public class SessioControllerImpl implements SessionController {

    private static final long serialVersionUID = 1L;

    /**
     * Mapa que armazena sessões ativas, associadas à chave do usuário.
     * 
     * <p><b>Chave:</b> Login ou identificador único do usuário<br>
     * <b>Valor:</b> Objeto {@link HttpSession} correspondente</p>
     */
    private HashMap<String, HttpSession> hashMap = new HashMap<>();

    /**
     * Adiciona uma nova sessão HTTP ao mapa de sessões ativas.
     * 
     * @param KeyLoginUser identificador do usuário (login ou ID)
     * @param httpSession sessão HTTP atual do usuário
     */
    @Override
    public void addSession(String KeyLoginUser, HttpSession httpSession) {
        hashMap.put(KeyLoginUser, httpSession);
    }

    /**
     * Encerra (invalida) a sessão do usuário identificado pela chave informada.
     * Se a sessão ainda estiver ativa, ela será encerrada.
     * 
     * <p>Após a invalidação, a entrada é removida do {@code hashMap}.</p>
     * 
     * @param KeyLoginUser identificador do usuário (login ou ID)
     */
    @Override
    public void invalidateSession(String KeyLoginUser) {
        HttpSession session = hashMap.get(KeyLoginUser);

        if (session != null) {
            try {
                // Encerra a sessão do usuário
                session.invalidate();
                System.out.println("encerrou");
            } catch (Exception e) {
                // Registra o erro, mas não impede a remoção do mapa
                e.printStackTrace();
            }
        }

        // Remove a referência da sessão do mapa, mesmo se já inválida
        hashMap.remove(KeyLoginUser);
    }
}
