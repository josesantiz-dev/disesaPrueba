function buscar(valor, pnlMsg){
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
}

function guardar(valor, pnlOrigen, pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;
	
	if(valor == '')
		Richfaces.hideModalPanel(pnlOrigen);
	
	Richfaces.showModalPanel(pnlMsg);
	
}

function eliminar(valor, pnlOperacion, pnlMsg){
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOperacion);
}

function validaCaracteres(event, permitirEspacio, permitirGuionMedio, permitirGuionBajo) {
	var key = event.key;
	var keyCode = event.keyCode;
	var caracteresValidos = "abcdefghihjklmnñopqrstuvwxyzABCDEFGHIHJKLMNÑOPQRSTUVWXYZ0123456789";
	
	permitirEspacio = permitirEspacio || false;
	if (permitirEspacio === true)
		caracteresValidos += " ";
	
	permitirGuionMedio = permitirGuionMedio || false;
	if (permitirGuionMedio === true)
		caracteresValidos += "-";
	
	permitirGuionBajo = permitirGuionBajo || false;
	if (permitirGuionBajo === true)
		caracteresValidos += "_";

	console.log(event);
	console.log("->     Key: " + key);
	console.log("-> KeyCode: " + keyCode);
	console.log("-> Matcher: " + caracteresValidos);
	
	/*
	 *  8 = BACKSPACE
	 *  9 = TAB
	 * 35 = FIN
	 * 36 = INICIO
	 * 37 = ARROWLEFT
	 * 38 = ARROWUP
	 * 39 = ARROWRIGHT
	 * 40 = ARROWDOWN
	 * 46 = DELETE
	 */
	
	if ( keyCode == 8 || 
			keyCode ==  9 ||
			keyCode == 35 || 
			keyCode == 36 || 
			keyCode == 37 || 
			keyCode == 38 || 
			keyCode == 39 || 
			keyCode == 40 || 
			keyCode == 46 )
		return true;
	
	return (caracteresValidos.indexOf(key) > -1);
}

function soloNumeros(event, permiteNegativos, debug) {
	var key = event.key;
	var keyCode = event.keyCode;
	var caracteresValidos = "0123456789";

	debug 			  = debug || false;
	permiteNegativos  = permiteNegativos || false;
	caracteresValidos = (permiteNegativos == true) ? caracteresValidos + '-' : caracteresValidos;

	if (debug == true) {
		console.log("-> soloNumeros(event, decimal, maxDigitos, maxDecimales)\n----------------------------------");
		console.log("->     Key: " + key);
		console.log("-> KeyCode: " + keyCode);
		console.log("-> Matcher: " + caracteresValidos);
		console.log(event);
	}
	
	/*
	 *  8 = BACKSPACE
	 *  9 = TAB
	 * 35 = FIN
	 * 36 = INICIO
	 * 37 = ARROWLEFT
	 * 38 = ARROWUP
	 * 39 = ARROWRIGHT
	 * 40 = ARROWDOWN
	 * 46 = DELETE
	 */
	
	if (keyCode ==  8 || keyCode ==  9 || keyCode == 35 
		|| keyCode == 36 || keyCode == 37 || keyCode == 38 
		|| keyCode == 39 || keyCode == 40 || keyCode == 46 )
		return true;

	if (debug == true) console.log("-> fin soloDecimales\n----------------------------------");
	return (caracteresValidos.indexOf(key) > -1);
}

function soloDecimales(event, decimal, maxDigitos, maxDecimales, permiteNegativos, debug) {
	var resultEvent = false;
	var matcherString = '^([0-9]{0,MAXDIGIT})$|^([0-9]{0,MAXDIGIT})?([.][0-9]{1,MAXDECIMAL})?$';
	var matcher = null;
	var caracteresValidos = "0123456789.";
	var key = event.key;
	var keyCode = event.keyCode;
	var indexKey = event.target.selectionStart;
	
	// Inicializamos
	decimal 		  = decimal || '';
	maxDigitos 		  = maxDigitos   || 10;
	maxDecimales 	  = maxDecimales ||  2;
	permiteNegativos  = permiteNegativos || false;
	debug 			  = debug || false;
	caracteresValidos = (permiteNegativos == true) ? caracteresValidos + '-' : caracteresValidos;
	matcherString 	  = matcherString.replace(/MAXDIGIT/g, maxDigitos).replace('MAXDECIMAL', maxDecimales)
	decimal 		  = decimal.replace(/,/g, '');
	matcher 		  = new RegExp(matcherString);

	if (debug == true) {
		console.log("-> soloDecimales(event, decimal, maxDigitos, maxDecimales)\n----------------------------------");
		console.log("->          Key : " + key);
		console.log("->      KeyCode : " + keyCode);
		console.log("->      decimal : " + decimal);
		console.log("->   maxDigitos : " + maxDigitos);
		console.log("-> maxDecimales : " + maxDecimales);
		console.log("->     indexKey : " + indexKey);
		console.log("->matcherString : " + matcherString);
		console.log("->  validString : " + caracteresValidos);
		console.log(event);
	}
	
	if (keyCode ==  8 || keyCode ==  9 || keyCode == 35 
		|| keyCode == 36 || keyCode == 37 || keyCode == 38 
		|| keyCode == 39 || keyCode == 40 || keyCode == 46 )
		return true;
	
	// Validaciones contra el valor actual del input
	if (decimal != undefined && decimal != '') {
		if ((caracteresValidos.indexOf(key) > -1) == false)
			return false;

		// Comprobamos que solo exista un punto en la cadena
		if (key == '.') {
			// Si ya existe en la cadena
			if (decimal.indexOf(key) > -1)
				return false;
			return true;
		}

		// Comprobamos que solo exista un guion y que este al inicio de la cadena
		if (key == '-') {
			// Si no permite negativos
			if (permiteNegativos == false)
				return false;

			// si ya existe en la cadena
			if (decimal.indexOf(key) > -1)
				return false;

			// Si no esta al inicio de la cadena
			if (indexKey > 0)
				return false;

			return true;
		}

		// Agregamos el caracter al valor actual del input (decimal) y evaluamos formato y longitud de las partes (digitos y decimales)
		decimal = decimal.slice(0, indexKey) + key + decimal.slice(indexKey);
		if (debug == true) {
			console.log("-> ----------------------------------");
			console.log("->valor updated : " + decimal);
			console.log("-> matcher.test : " + matcher.test(decimal));
		}

		return matcher.test(decimal);
	}

	return (caracteresValidos.indexOf(key) > -1);
}
