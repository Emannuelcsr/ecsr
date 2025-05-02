package br.com.dao.implementacao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.framwork.implementacao.crud.ImplementacaoCrud;
import br.com.repository.interfaces.RepositoryLogin;

/**
 * 🗝️ DaoLogin
 * -----------------------------------------------------------------------------
 * Classe responsável por realizar a autenticação de usuários no sistema.
 * 
 * 📌 Implementa a interface RepositoryLogin, seguindo o padrão DAO.
 * 📌 Usa JdbcTemplate para acesso ao banco de dados de forma direta.
 * 📌 Herda da classe ImplementacaoCrud, que oferece suporte genérico a JDBC.
 * 
 * 🧩 Essa implementação verifica se existe um registro na tabela `entidade`
 *     com o login e senha informados.
 * 
 * 🧪 Se encontrar, o login é considerado válido e retorna `true`; caso contrário, `false`.
 * 
 * ⚠️ IMPORTANTE:
 * - Nunca salve senhas em texto puro (plaintext) como no exemplo!
 * - Sempre use hashing (ex: BCrypt) para segurança.
 * 
 * 📚 Boas práticas utilizadas:
 * - Uso de `JdbcTemplate` via `super.getJdbcTemplate()` para evitar redundância
 * - Prepared statements com `?` para evitar SQL Injection
 * - Uso de `SqlRowSet` para leitura segura dos dados
 * 
 * ❌ Erros comuns evitados:
 * - Evita concatenar strings na query (o que causaria vulnerabilidades)
 * - Evita `NullPointerException` com `rowSet.next() ? ...`
 * 
 */
@Repository
public class DaoLogin extends ImplementacaoCrud<Object> implements RepositoryLogin {

	private static final long serialVersionUID = 1L;

	/**
	 * 🔐 Valida se o login e senha fornecidos correspondem a um usuário válido.
	 * 
	 * @param login - Nome de login informado pelo usuário
	 * @param senha - Senha correspondente (em texto puro, o que não é seguro)
	 * @return true se houver um usuário com as credenciais fornecidas; false caso contrário
	 * @throws Exception em caso de erro no acesso ao banco
	 */
	@Override
	public boolean autentico(String login, String senha) throws Exception {

		// 1. Define o SQL para contar quantos registros existem com login/senha fornecidos
		String sql = "select count(1) as autentica from entidade where ent_login = ? and ent_senha = ?";

		// 2. Executa a SQL com os parâmetros usando JdbcTemplate
		SqlRowSet rowSet = super.getJdbcTemplate().queryForRowSet(sql, new Object[] {login, senha});

		// 3. Move o cursor para a primeira (e única) linha do resultado
		boolean existeResultado = rowSet.next();

		// 4. Se não houver linha (o que é improvável, mas possível em erros), retorna false
		if (!existeResultado) {
		    return false;
		}

		// 5. Recupera o valor da coluna "autentica" (que é a contagem de registros encontrados)
		int autenticacao = rowSet.getInt("autentica");

		// 6. Verifica se essa contagem é maior que zero (se sim, é um login válido)
		boolean loginValido = autenticacao > 0;

		// 7. Retorna o resultado final
		return loginValido;


	}
}
