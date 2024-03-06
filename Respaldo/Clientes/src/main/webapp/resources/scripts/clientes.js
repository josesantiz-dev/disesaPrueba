function validaEmail(value) {
	var pattern = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	
	if (! expr.test(value))
        alert("Error: La dirección de correo " + value + " es incorrecta.");
}