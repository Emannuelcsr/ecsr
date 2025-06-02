// Array global que armazena os IDs dos elementos da página
var arrayIdsElementsPage = new Array;

// Valor simbólico utilizado para representar um ID indefinido (pode ser útil para validações ou retornos de erro)
var idundefined = 'idundefined';

// Representa o tipo de dado java.lang.String (texto). Usado para identificar campos textuais.
var classTypeString = 'java.lang.String';

// Representa o tipo de dado java.lang.Long (número inteiro longo). Utilizado para IDs ou valores numéricos grandes.
var classTypeLong = 'java.lang.Long';

// Representa o tipo de dado java.util.Date (data). Usado em campos de data no sistema.
var classTypeDate = 'java.util.Date';

// Representa o tipo de dado java.lang.Boolean (verdadeiro/falso). Utilizado para checkboxes, status, flags etc.
var classTypeBoolean = 'java.lang.Boolean';

// Representa o tipo de dado java.math.BigDecimal (número decimal de alta precisão). Usado para valores monetários ou com casas decimais.
var classTypeBigDecimal = 'java.math.BigDecimal';




// ------------------------------ Funções de Foco e Navegação entre Campos ------------------------------

// Adiciona foco no campo informado, com base na busca do ID real do elemento
function addFocoCampo(campo) {
	var id = getValorElementPorId(campo);
	if (id != undefined) {
		document.getElementById(id).focus(); // Aplica o foco ao elemento encontrado
	}
}

// Gerencia o uso da tecla Enter nos campos de input
function gerenciaTeclaEnter() {
	$(document).ready(function() {

		// Impede que o Enter dispare envio de formulário em qualquer campo input
		$('input').keypress(function(e) {
			var code = null;
			code = (e.keyCode ? e.keyCode : e.which); // Captura código da tecla
			return (code === 13) ? false : true; // Bloqueia Enter (código 13)
		});

		// Faz com que o Enter avance para o próximo campo de texto
		$('input[type=text]').keydown(function(e) {
			var next_idx = $('input[type=text]').index(this) + 1; // Próximo índice
			var tot_idx = $('body').find('input[type=text]').length; // Total de campos

			if (e.keyCode === 13) { // Se a tecla pressionada for Enter
				if (tot_idx === next_idx)
					$('input[type=text]:eq(0)').focus(); // Volta para o primeiro campo
				else
					$('input[type=text]:eq(' + next_idx + ')').focus(); // Vai para o próximo campo
			}
		});
	});
}

// ------------------------------ Funções de Inicialização do Template e Menu ------------------------------

// Inicializa o template ocultando menus e preparando o evento de clique no botão
function initTamplete() {
	$(document).ready(function() {
		$('#menupop').hide();
		$('#barramenu').hide();
		$('#barramenu').css("left", "-200px"); // Oculta lateralmente

		// Evento de clique no botão que mostra/oculta o menu lateral
		$('#iniciarmenu').click(function() {
			if ($('#barramenu').is(':visible')) {
				ocultarmenu(); // Oculta se estiver visível
			} else {
				$('#barramenu').show();
				$('#barramenu').animate({ "left": "0px" }, "slow"); // Anima para aparecer
			}
		});
	});
}

// Oculta o menu lateral com animação
function ocultarmenu() {
	$('#barramenu').animate({
		"left": "-200px" // Move para fora da tela
	}, "slow", function() {
		$('#barramenu').hide(); // Após animar, oculta totalmente
	});
}

// ------------------------------ Localização pt-BR para PrimeFaces ------------------------------

// Define as configurações de idioma em português para componentes do PrimeFaces
function localeData_pt_br() {
	PrimeFaces.locales['pt'] = {
		closeText: 'Fechar',
		prevText: 'Anterior',
		nextText: 'Próximo',
		currentText: 'Começo',
		monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
		monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
		dayNames: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
		dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
		dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
		weekHeader: 'Semana',
		firstDay: 0,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: '',
		timeOnlyTitle: 'São Horas',
		timeText: 'Tempo',
		hourText: 'Hora',
		minuteText: 'Minuto',
		secondText: 'Segundo',
		ampm: false,
		month: 'Mês',
		week: 'Semana',
		day: 'Dia',
		allDayText: 'Todo o Dia'
	};
}

// ------------------------------ Funções de Manipulação de Componentes da Página ------------------------------


// Carrega todos os IDs de elementos da página HTML nos formulários
function carregarIdElementosPagina() {
	arrayIdsElementsPage = new Array;
	for (form = 0; form < document.forms.length; form++) {
		var formAtual = document.forms[form];
		if (formAtual != undefined) {
			for (i = 0; i < document.forms[form].elements.length; i++) {
				if (document.forms[form].elements[i].id != '') {
					arrayIdsElementsPage[i] = document.forms[form].elements[i].id;
				}
			}
		}
	}
}

// Retorna o ID real do elemento HTML que contém a string informada (útil para JSF)
function getValorElementPorId(id) {
	carregarIdElementosPagina();
	for (i = 0; i < arrayIdsElementsPage.length; i++) {
		var valor = "" + arrayIdsElementsPage[i];
		if (valor.indexOf(id) > -1) {
			return valor; // Retorna o ID que contém o texto informado
		}
	}
	return undefined; // Caso não encontre
}

// ------------------------------ Redirecionamento e Sessão ------------------------------

// Redireciona para uma página JSF com base no contexto da aplicação
function redirecionarPagina(context, pagina) {
	pagina = pagina + ".jsf";
	document.location = context + pagina;
}

// Exibe o menu pop-up e aplica estilo visual de seta aberta
function abrirMenuPop() {
	$("#menupop").show('slow').mouseleave(fecharMenuPop);
	$(".seta-menu").addClass("aberta");
}

// Fecha o menu pop-up e remove estilo visual
function fecharMenuPop() {
	if ($("#menupop").is(":visible")) {
		$("#menupop").hide("slow");
		$(".seta-menu").removeClass("aberta");
	}
}

// Realiza logout do sistema via requisição AJAX e redireciona
function logout(contextPath) {
	var post = 'invalidar sessao';

	$.ajax({
		type: 'POST',
		url: post
	}).always(function(resposta) {
		document.location = contextPath + '/j_spring_security_logout';
	});
}

// Invalida a sessão e redireciona para uma página específica
function invalidarSession(context, pagina) {
	document.location = (context + pagina + ".jsf");
}

// ------------------------------ Validação de Login ------------------------------

// Valida se os campos de login e senha foram preenchidos
function validarSenhaLogin() {
	const j_username = document.getElementById("j_username").value.trim();
	const j_password = document.getElementById("j_password").value.trim();

	if (!j_username) {
		alert("Informe o login");
		document.getElementById("j_username").focus();
		return false;
	}

	if (!j_password) {
		alert("Informe a senha");
		document.getElementById("j_password").focus();
		return false;
	}

	return true; // Tudo certo, pode prosseguir
}

// ------------------------------ Máscara de Pesquisa  ------------------------------

// Função que permite digitar apenas números no campo
function permitNumber(e) {

	// Obtém o código da tecla pressionada, dependendo do navegador (charCode ou keyCode)
	var unicode = e.charCode ? e.charCode : e.keyCode;

	// Permite as teclas Backspace (código 8) e Tab (código 9)
	if (unicode != 8 && unicode != 9) {

		// Verifica se o código da tecla está fora do intervalo dos números (48 a 57 em ASCII)
		if (unicode < 48 || unicode > 57) {
			// Se não for número, impede a digitação
			return false;
		}
	}
}


function getValorElementPorIdJQuery(id) {

	var id = getValorElementPorId(id);

	if (id.trim() != idundefined) {

		return PrimeFaces.escapeClientId(id);
	}


	return idundefined;

}

function teste(){
	alert("oi");
	
}

function addMascaraPesquisa(elemento) {

	console.log("Função addMascaraPesquisa foi chamada com:", elemento);
	var id = getValorElementPorIdJQuery('valorPesquisa');

	var vals = elemento.split("*");

	var campoBanco = vals[0];

	var typeCampo = vals[1];

	$(id).unmask();
	$(id).unbind("keypress");
	$(id).unbind("keyup");
	$(id).unbind("focus");
	$(id).val('');

	if (id != idundefined) {

		jQuery(function($) {

			// Verifica se o tipo do campo é Long (número inteiro)
			if (typeCampo === classTypeLong) {
				// Se for Long, aplica um filtro de entrada para permitir apenas números
				$(id).keypress(permitNumber); // Função `permitNumber` deve conter lógica para bloquear caracteres não numéricos
			}

			// Verifica se o tipo do campo é BigDecimal (número decimal)
			else if (typeCampo === classTypeBigDecimal) {
				// Aplica máscara monetária com 4 casas decimais, vírgula como separador decimal e ponto como separador de milhar
				$(id).maskMoney({ precision: 4, decimal: ",", thousands: "." });
			}

			// Verifica se o tipo do campo é Date (data)
			else if (typeCampo === classTypeDate) {
				// Aplica máscara de data no formato dia/mês/ano
				$(id).mask('99/99/9999');
			}

			// Se não for nenhum dos tipos reconhecidos
			else {
				// Remove qualquer máscara aplicada anteriormente
				$(id).unmask();
				// Remove evento associado ao pressionar tecla
				$(id).unbind("keypress");
				// Remove evento associado ao soltar tecla
				$(id).unbind("keyup");
				// Remove evento associado ao foco
				$(id).unbind("focus");
				$(id).unbind("");

			}
			addFocoCampo("valorPesquisa")

		});
	}

}


function validarCampoPesquisa(valor) {
    if (valor != undefined && valor.value != undefined) {
        if (valor.value.trim() == '') {
            valor.value = "";
        } else {
            valor.value = valor.value.trim();
        }
    }
}


