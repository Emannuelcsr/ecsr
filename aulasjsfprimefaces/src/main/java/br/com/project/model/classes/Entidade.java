package br.com.project.model.classes;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

/**
 * Representa a entidade do usuário do sistema.
 * 
 * Essa classe mapeia a tabela de usuários no banco de dados e é utilizada
 * tanto para autenticação quanto para auditoria (via Envers).
 * 
 * A anotação {@code @Audited} indica que qualquer alteração feita em registros
 * dessa tabela será auditada automaticamente pelo Hibernate Envers, gerando
 * entradas de histórico com quem alterou, quando e o que foi modificado.
 */
@Audited
@Entity
public class Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Identificador único da entidade (chave primária).
	 * Representa o código do usuário.
	 */
	@Id
	private Long ent_codigo;

	/**
	 * Login do usuário. Usado para autenticação.
	 */
	private String ent_login = null;

	/**
	 * Senha do usuário.
	 */
	private String ent_senha;

	// Getters e Setters

	/**
	 * Recupera o login do usuário.
	 */
	public String getEnt_login() {
		return ent_login;
	}

	/**
	 * Define o login do usuário.
	 */
	public void setEnt_login(String ent_login) {
		this.ent_login = ent_login;
	}

	/**
	 * Recupera a senha do usuário.
	 */
	public String getEnt_senha() {
		return ent_senha;
	}

	/**
	 * Define a senha do usuário.
	 */
	public void setEnt_senha(String ent_senha) {
		this.ent_senha = ent_senha;
	}

	/**
	 * Define o código do usuário (ID).
	 */
	public void setEnt_codigo(Long ent_codigo) {
		this.ent_codigo = ent_codigo;
	}

	/**
	 * Recupera o código do usuário (ID).
	 */
	public Long getEnt_codigo() {
		return ent_codigo;
	}
}
