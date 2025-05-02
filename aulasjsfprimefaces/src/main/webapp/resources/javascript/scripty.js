function invalidarSession(context, pagina) {
	document.location = (context + pagina + ".jsf");

}

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
    
    return true;
}

