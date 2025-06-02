package br.com.project.geral.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.stereotype.Controller;

import br.com.framwork.implementacao.crud.ImplementacaoCrud;
import br.com.framwork.interfac.crud.InterfaceCrud;
import br.com.project.model.classes.Estado;


//✅ A anotação @Controller é da Spring Framework. Ela transforma essa classe em um componente gerenciado (bean),
//permitindo que o Spring a detecte e registre no contexto da aplicação.
@Controller
public class EstadoController extends ImplementacaoCrud<Estado> implements InterfaceCrud<Estado> {

	private static final long serialVersionUID = 1L;

	
	
	  /**
     * ✅ Método responsável por retornar uma lista de objetos SelectItem, utilizados em componentes
     * JSF como <p:selectOneMenu> para listar os estados dinamicamente do banco de dados.     */
	public List<SelectItem> getListEstado() throws Exception {

		
		// ✅ Criação de uma lista de SelectItem. Cada item dessa lista representará um estado
        // com um valor (o objeto Estado) e o texto exibido na tela (nome do estado).
		List<SelectItem> selectItems = new ArrayList<SelectItem>();

		// ✅ Busca todos os estados no banco de dados utilizando uma consulta HQL (Hibernate Query Language).
        // O método 'findListByQueryDinamic' está definido na classe ImplementacaoCrud, herdada por essa classe.
		List<Estado> estados = super.findListByQueryDinamic(" from Estado order by est_nome");

		// ✅ Percorre todos os estados retornados e adiciona à lista de SelectItem
        // Cada item terá como valor o próprio objeto Estado, e como label o nome do estado.
		for (Estado estado : estados) {

			
			// ✅ Cria um novo item de seleção (SelectItem) para ser usado em componentes JSF (ex: <p:selectOneMenu>).
			// O primeiro parâmetro (estado) será o valor real enviado ao bean ao submeter o formulário.
			// O segundo parâmetro (estado.getEst_nome()) será o texto visível na interface para o usuário final.
			//
			// Exemplo prático: Suponha que temos um estado com ID = 5 e nome = "Paraná".
			// Neste caso, será criado um SelectItem equivalente a:
			// <option value="5">Paraná</option> (JSF trata isso internamente com objetos).
			//
			// Isso permite que, ao selecionar um estado na interface, o JSF saiba qual objeto Estado foi escolhido.
			selectItems.add(new SelectItem(estado, estado.getEst_nome()));

		}

		 // ✅ Retorna a lista de SelectItem para ser usada na view JSF (por exemplo, em um menu dropdown).
		return selectItems;

	}

}
