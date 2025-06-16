package br.com.project.model.classes;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.envers.Audited;
import org.primefaces.json.JSONObject;

import br.com.project.acessos.Permissao;
import br.com.project.annotation.IdentificaCampoPesquisa;

/**
 * Representa a entidade do usuário do sistema.
 * 
 * Esta classe é uma entidade JPA mapeada para a tabela "entidade" no esquema "public".
 * 
 * A anotação @Audited indica que o Hibernate Envers irá registrar automaticamente
 * todas as alterações feitas nos registros dessa tabela, permitindo auditoria.
 */
@Audited
@Entity
@Table(name = "entidade", schema = "public")
public class Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único da entidade, chave primária no banco.
     * É gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ent_codigo;

    /**
     * Login do usuário, usado para autenticação no sistema.
     */
    private String ent_login = null;

    /**
     * Tipo da entidade, ex: FUNCIONARIO, CLIENTE, etc.
     */
    private String tipoEntidade = "";

    /**
     * Email do usuário.
     */
    private String email;

    /**
     * Senha do usuário.
     * Atenção: em sistema real, senha deve ser armazenada com hash seguro.
     */
    private String ent_senha;

    /**
     * Nome fantasia ou nome completo do usuário.
     * 
     * A anotação personalizada @IdentificaCampoPesquisa indica que esse campo
     * pode ser usado em filtros de busca.
     */
    @IdentificaCampoPesquisa(descricaoCampo = "Nome", campoConsulta = "ent_nomefantasia", principal = 1)
    private String ent_nomefantasia;

    /**
     * Indica se o usuário está inativo (soft delete).
     */
    private boolean ent_inativo = false;

    @Column(unique = true)
    private String cpf;
    
    /**
     * Data e hora do último acesso do usuário ao sistema.
     * Anotação @Temporal informa que este campo é do tipo TIMESTAMP no banco.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date ent_ultimoacesso;

    /**
     * Conjunto de acessos/permissões que o usuário possui.
     * 
     * Mapear lista de Strings para tabela entidadeacesso, com restrição de
     * unicidade entre ent_codigo e esa_codigo.
     * 
     * Carregamento é EAGER, para buscar acessos junto com o usuário.
     */
    @CollectionOfElements
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @JoinTable(name = "entidadeacesso", 
        uniqueConstraints = {
            @UniqueConstraint(name = "unique_acesso_entidade_key", 
                              columnNames = { "ent_codigo", "esa_codigo" })
        }, 
        joinColumns = { @JoinColumn(name = "ent_codigo") })
    @Column(name = "esa_codigo", length = 20)
    private Set<String> acessos = new HashSet<String>();


    // --------------------------- GETTERS E SETTERS --------------------------- //

    public Long getEnt_codigo() {
        return ent_codigo;
    }

    public void setEnt_codigo(Long ent_codigo) {
        this.ent_codigo = ent_codigo;
    }

    public String getEnt_login() {
        return ent_login;
    }

    public void setEnt_login(String ent_login) {
        this.ent_login = ent_login;
    }

    public String getTipoEntidade() {
        return tipoEntidade;
    }

    public void setTipoEntidade(String tipoEntidade) {
        this.tipoEntidade = tipoEntidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnt_senha() {
        return ent_senha;
    }

    public void setEnt_senha(String ent_senha) {
        this.ent_senha = ent_senha;
    }

    public String getEnt_nomefantasia() {
        return ent_nomefantasia;
    }

    public void setEnt_nomefantasia(String ent_nomefantasia) {
        this.ent_nomefantasia = ent_nomefantasia;
    }

    public boolean getEnt_inativo() {
        return ent_inativo;
    }

    public void setEnt_inativo(boolean ent_inativo) {
        this.ent_inativo = ent_inativo;
    }

    public Date getEnt_ultimoacesso() {
        return ent_ultimoacesso;
    }

    public void setEnt_ultimoacesso(Date ent_ultimoacesso) {
        this.ent_ultimoacesso = ent_ultimoacesso;
    }

    public Set<String> getAcessos() {
        return acessos;
    }

    public void setAcessos(Set<String> acessos) {
        this.acessos = acessos;
    }

    
    
    

    // --------------------------- MÉTODOS OVERRIDE ---------------------------- //

    public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	/**
     * HashCode baseado no ID da entidade.
     * Usado para comparação e organização em coleções.
     */
    @Override
    public int hashCode() {
        return Objects.hash(ent_codigo);
    }

    /**
     * Equals compara objetos Entidade pelo código (ID).
     * Garante que duas entidades com o mesmo ID sejam consideradas iguais.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if (obj == null)
            return false;
        
        if (getClass() != obj.getClass())
            return false;
        
        Entidade other = (Entidade) obj;
        
        return Objects.equals(ent_codigo, other.ent_codigo);
    }


    // --------------------------- MÉTODOS AUXILIARES -------------------------- //

    /**
     * Retorna um JSONObject contendo alguns campos da entidade.
     * Pode ser útil para enviar dados para frontend ou APIs REST.
     */
    public JSONObject getJson() {

        Map<Object, Object> map = new HashMap<Object, Object>();

        map.put("ent_codigo", ent_codigo);
        map.put("ent_login", ent_login);
        map.put("ent_nomefantasia", ent_nomefantasia);

        return new JSONObject(map);

    }
    
    
    public Set<Permissao> getAcessosPermissao(){
    	
    	Set<Permissao> permissoes = new HashSet<Permissao>();
    	
    	for(String acesso:acessos) {
    		
    		for(Permissao acess:Permissao.values()) {
    			
    			if(acesso.equalsIgnoreCase(acess.name())) {
    				permissoes.add(acess);
    			}
    			
    		}
    	}
    	
    	return permissoes;
    }

}
