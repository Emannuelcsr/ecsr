package br.com.project.acessos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Enum responsável por centralizar e representar todas as permissões de acesso do sistema.
 * 
 * Cada permissão possui:
 * - Um valor técnico (salvo no banco de dados)
 * - Uma descrição amigável (exibida nas telas)
 * 
 * Exemplo de uso:
 * Permissao.ADMIN.getDescricao(); --> "Administrador"
 */
public enum Permissao {

//--------------------------------------------------------------------------------------------------------------------------------------//

	// Enumeração das permissões do sistema
	ADMIN("Admin", "Administrador"),
	USER("USER", "Usuário Padrão"),
	CADASTRO_ACESSAR("CADASTRO_ACESSAR", "Cadastro - Acessar"),
	FINANCEIRO_ACESSAR("FINANCEIRO_ACESSAR", "Financeiro - Acessar"),
	MENSAGENS_ACESSAR("MENSAGENS_ACESSAR", "Mensagens recebidas - Acessar"),
	BAIRRO_ACESSAR("BAIRRO_ACESSAR", "Bairro - Acessar"),
	BAIRRO_NOVO("BAIRRO_NOVO", "Bairro - Novo"),
	BAIRRO_EDITAR("BAIRRO_EDITAR", "Bairro - Editar"),
	BAIRRO_EXCLUIR("BAIRRO_EXCLUIR", "Bairro - Excluir");

	
//--------------------------------------------------------------------------------------------------------------------------------------//
	
	// Atributo que representa o valor técnico da permissão (salvo no banco)
	private String valor = "";

	// Atributo que representa a descrição amigável da permissão (exibida ao usuário)
	private String descricao = "";

	
//--------------------------------------------------------------------------------------------------------------------------------------//	

	
	/**
	 * Construtor utilizado pelos itens do Enum.
	 * 
	 * @param name      Valor técnico da permissão
	 * @param descricao Descrição amigável da permissão
	 */
	private Permissao(String name, String descricao) {
		this.valor = name;
		this.descricao = descricao;
	}

	// Construtor padrão (necessário em enum, mas não utilizado)
	private Permissao() {
	}
	
	
//--------------------------------------------------------------------------------------------------------------------------------------//

	
	// Getter do valor técnico
	public String getValor() {
		return valor;
	}

	// Setter do valor técnico (evitar uso em enums)
	public void setValor(String valor) {
		this.valor = valor;
	}

	// Getter da descrição
	public String getDescricao() {
		return descricao;
	}

	// Setter da descrição (evitar uso em enums)
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
//--------------------------------------------------------------------------------------------------------------------------------------//	
	
	
	/**
	 * Define o que será retornado ao imprimir o Enum.
	 * Neste caso, retorna o valor técnico.
	 */
	@Override
	public String toString() {
		return getValor();
	}

//--------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Método utilitário que retorna uma lista de permissões
	 * ordenadas pela ordem em que foram declaradas no Enum.
	 * 
	 * @return Lista de permissões ordenadas
	 */
	public static List<Permissao> getListPermissao() {

		// Cria lista vazia de permissões
		List<Permissao> permissaos = new ArrayList<Permissao>();

		// Adiciona todos os itens do Enum na lista
		for (Permissao permissao : Permissao.values()) {
			permissaos.add(permissao);
		}

		// Ordena a lista com base na ordem alfabetica
		Collections.sort(permissaos, new Comparator<Permissao>() {

			@Override
			public int compare(Permissao o1, Permissao o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});

		// Retorna a lista ordenada
		return permissaos;
	}
	
	
}
