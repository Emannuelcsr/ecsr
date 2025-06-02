package br.com.project.model.classes;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;

import br.com.project.annotation.IdentificaCampoPesquisa;

@Audited
@Entity // Define que essa classe é uma entidade JPA.
@Table(name = "cidade") // Define o nome da tabela no banco.
@SequenceGenerator(name = "cidade_seq", sequenceName = "cidade_seq", initialValue = 1, allocationSize = 1)
// Define o gerador de sequência para o ID.
public class Cidade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@IdentificaCampoPesquisa(descricaoCampo = "Código", campoConsulta = "cid_codigo")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cidade_seq" )
	private Long cid_codigo;// Campo obrigatório no banco com no máximo 100 caracteres.

	
	
	@IdentificaCampoPesquisa(descricaoCampo = "Descrição", campoConsulta = "cid_descricao", principal = 1)
	@Column(length = 100,nullable = false)
	private String cid_descricao;// Campo obrigatório no banco com no máximo 100 caracteres.

	
	@IdentificaCampoPesquisa(descricaoCampo = "Estado",campoConsulta = "estado.est_nome")
	@Basic
	@ManyToOne
	@JoinColumn(name = "estado", nullable = false)
	@ForeignKey(name = "estado_fk")
	private Estado estado = new Estado(); // Relacionamento muitos-para-um com a entidade Estado. Cada cidade pertence a um estado.

	
	@Version
	@Column(name = "versionNum")
	private int versionNum;// Controle de versão para concorrência otimista. Útil para evitar conflito de atualizações simultâneas.

	
	
	public Long getCid_codigo() {
		return cid_codigo;
	}
	public void setCid_codigo(Long cid_codigo) {
		this.cid_codigo = cid_codigo;
	}
	public String getCid_descricao() {
		return cid_descricao;
	}
	public void setCid_descricao(String cid_descricao) {
		this.cid_descricao = cid_descricao;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public int getVersionNum() {
		return versionNum;
	}
	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cid_codigo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cidade other = (Cidade) obj;
		return Objects.equals(cid_codigo, other.cid_codigo);
	}
	@Override
	public String toString() {
		return "Cidade [cid_codigo=" + cid_codigo + ", cid_descricao=" + cid_descricao + "]";
	}

	public Cidade() {
	}


}
