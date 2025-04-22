package br.com.project.listener;

import java.io.Serializable;

import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Service;

import br.com.frameworks.utils.utilFramework;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.InformacaoRevisao;

/**
 * Classe responsável por interceptar eventos de revisão (auditoria) no Hibernate Envers.
 * 
 * Implementa a interface {@link RevisionListener}, que permite executar lógica personalizada
 * sempre que uma nova revisão for criada automaticamente pelo Envers.
 * 
 * Esta classe é utilizada pela anotação {@code @RevisionEntity(CustomListener.class)} 
 * na entidade {@link InformacaoRevisao}, permitindo incluir dados adicionais na revisão,
 * como o usuário logado que realizou a alteração.
 * 
 * Está anotada com {@code @Service}, o que permite que o Spring gerencie sua instância.
 */
@Service
public class CustomListener implements RevisionListener, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Método executado automaticamente pelo Hibernate Envers no momento em que uma nova
	 * revisão está sendo criada.
	 * 
	 * O objetivo aqui é capturar o código do usuário logado (armazenado via ThreadLocal),
	 * instanciar uma entidade {@link Entidade} apenas com o ID, e associá-la à revisão
	 * através da classe {@link InformacaoRevisao}.
	 * 
	 * @param revisionEntity objeto de revisão que será salvo no banco (do tipo InformacaoRevisao)
	 */
	@Override
	public void newRevision(Object revisionEntity) {
		
		// Converte o objeto genérico da revisão para a classe personalizada InformacaoRevisao
		InformacaoRevisao informacaoRevisao = (InformacaoRevisao)revisionEntity;
		
		// Recupera o código do usuário logado armazenado via ThreadLocal
		Long codUser = utilFramework.getThreadLocal().get();
		
		// Cria uma nova instância da entidade Entidade apenas com o código (sem buscar do banco)
		Entidade entidade = new Entidade();
		
		// Se o código do usuário estiver presente e válido, associa à revisão
		if(codUser != null && codUser != 0L) {
			entidade.setEnt_codigo(codUser);
			informacaoRevisao.setEntidade(entidade);
		}
	}
}
