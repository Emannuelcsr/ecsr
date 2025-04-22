package br.com.project.model.classes;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import br.com.project.listener.CustomListener;

/**
 * Representa a entidade de revisão utilizada pelo Hibernate Envers.
 * 
 * Esta classe estende a {@link DefaultRevisionEntity}, herdando os campos padrão de revisão,
 * como o identificador da revisão e a data/hora em que a revisão foi criada.
 * 
 * Além disso, adiciona um relacionamento com a entidade {@link Entidade},
 * que representa o usuário (ou operador) que realizou a alteração nos dados auditados.
 * 
 * A anotação {@code @RevisionEntity(CustomListener.class)} indica que o Hibernate deve
 * usar a classe {@link CustomListener} para preencher informações personalizadas
 * antes de persistir a revisão no banco de dados (como associar o usuário logado).
 * 
 * A tabela associada a essa entidade será chamada de "revinfo" no banco de dados.
 */
@Entity
@Table(name = "revinfo")
@RevisionEntity(CustomListener.class)
public class InformacaoRevisao extends DefaultRevisionEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Representa o usuário (entidade) que executou a alteração que gerou esta revisão.
	 * 
	 * Mapeamento ManyToOne: vários registros de revisão podem estar relacionados a uma mesma entidade (usuário).
	 * A chave estrangeira "entidade_fk" será usada para associar os dados na tabela "revinfo".
	 */
	@ManyToOne
	@ForeignKey(name = "entidade_fk")
	@JoinColumn(nullable = false, name = "entidade")
	private Entidade entidade;

	/**
	 * Define a entidade (usuário) responsável pela revisão.
	 * Este método é utilizado pelo {@link CustomListener} para registrar o usuário logado.
	 * 
	 * @param entidade a instância da entidade associada ao usuário logado
	 */
	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
	}

	/**
	 * Retorna a entidade (usuário) associada a esta revisão.
	 * 
	 * @return a entidade que realizou a alteração
	 */
	public Entidade getEntidade() {
		return entidade;
	}
}
