package teste;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import br.com.project.report.util.DateUtil;

/**
 * Classe de teste unitário para os métodos da classe {@link DateUtil}.
 * 
 * <p>Responsável por validar o comportamento esperado dos métodos utilitários
 * de formatação de datas.</p>
 * 
 * <p>Realiza testes usando JUnit 4.</p>
 * 
 * @author Emannuel
 */
public class TesteData {

	/**
	 * Testa todos os métodos da classe {@link DateUtil}.
	 * 
	 * <p>Valida se os métodos:</p>
	 * <ul>
	 *   <li>{@code getDateAtualReportName()}</li>
	 *   <li>{@code formatDateSql(Date)}</li>
	 *   <li>{@code formatDateSqlSimple(Date)}</li>
	 * </ul>
	 * estão retornando os valores esperados.
	 * 
	 * <p>Em caso de falha, o stack trace é impresso e o teste é marcado como falho.</p>
	 */
	@Test
	public void test() {
		try {
			assertEquals("11042025", DateUtil.getDateAtualReportName());
			assertEquals("'2025-04-11'", DateUtil.formatDateSql(Calendar.getInstance().getTime()));
			assertEquals("2025-04-11", DateUtil.formatDateSqlSimple(Calendar.getInstance().getTime()));

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
