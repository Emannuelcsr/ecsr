package br.com.repository.interfaces;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

/**
 * 🔐 RepositoryLogin
 * -------------------------------------------------------------
 * Interface responsável por definir o contrato de autenticação
 * de usuários na aplicação.
 * 
 * 📌 Padrão utilizado: Repository (Spring Data)
 * - Marca esta interface como um componente de persistência
 * - Permite que o Spring a reconheça e a injete automaticamente
 * 
 * ✅ Implementações desta interface devem:
 * - Acessar o banco de dados (ex: via JDBC, JPA, Hibernate)
 * - Verificar se o login e a senha fornecidos são válidos
 * - Retornar true se as credenciais forem corretas
 * 
 * ⚠️ Exceções:
 * - Pode lançar Exception genérica, mas o ideal seria criar
 *   exceções customizadas como `LoginInvalidoException` etc.
 * 
 * 🧱 Serializable:
 * - Permite que essa interface seja usada em contextos distribuídos,
 *   como sessões ou cache (boas práticas do Spring).
 */
@Repository
public interface RepositoryLogin extends Serializable {

    /**
     * 🧪 Verifica se um login e senha são válidos no sistema.
     * 
     * @param login Nome de login fornecido pelo usuário
     * @param senha Senha correspondente
     * @return true se o login for válido; false caso contrário
     * @throws Exception Caso ocorra erro ao acessar o banco ou regras de negócio
     */
    boolean autentico(String login, String senha) throws Exception;
}
