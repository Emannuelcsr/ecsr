package br.com.project.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Fábrica personalizada para retornar uma instância de {@link CustomExceptionHandler}.
 * <p>
 * Esta classe estende {@link ExceptionHandlerFactory} e permite que o JSF utilize
 * um handler de exceção customizado, inserindo o {@code CustomExceptionHandler} no
 * ciclo de vida da aplicação JSF.
 * </p>
 * 
 * <p>
 * Deve ser registrada no arquivo <b>faces-config.xml</b> para substituir a fábrica padrão:
 * <pre>{@code
 * <factory>
 *     <exception-handler-factory>br.com.project.exception.CustomExceptionHandleFactory</exception-handler-factory>
 * </factory>
 * }</pre>
 * </p>
 *
 * @see CustomExceptionHandler
 * @see ExceptionHandler
 * @see ExceptionHandlerFactory
 */
public class CustomExceptionHandleFactory extends ExceptionHandlerFactory {

    /**
     * Referência para a fábrica original de ExceptionHandler.
     */
    private ExceptionHandlerFactory parent;

    /**
     * Construtor que recebe a fábrica original e a armazena internamente.
     *
     * @param parent a fábrica padrão fornecida pelo JSF
     */
    public CustomExceptionHandleFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    /**
     * Retorna uma instância do {@link CustomExceptionHandler}, 
     * encapsulando o {@link ExceptionHandler} original.
     *
     * @return ExceptionHandler customizado
     */
    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler handler = new CustomExceptionHandler(parent.getExceptionHandler());
        return handler;
    }
}
