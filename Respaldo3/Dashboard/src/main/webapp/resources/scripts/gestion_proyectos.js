/* ----- KEYS -----
 *  8 = BACKSPACE
 *  9 = TAB
 * 35 = FIN
 * 36 = INICIO
 * 37 = ARROWLEFT
 * 38 = ARROWUP
 * 39 = ARROWRIGHT
 * 40 = ARROWDOWN
 * 46 = DELETE
 * ----------------
 */

var valOriginal = ''; // ALMACENA EL VALOR ORIGINAL (TOMADO EN EL EVENTO KEYDOWN) PARA USARSE CON EL EVENTO KEYUP VALIDANDO DECIMALES

function buscar(v_bool, pnlMsg){
	console.log('function buscar\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscar DONE\n------------------------------');
}

function agregar(v_bool, pnlMsg, listErrores) {
	console.log('function agregar(v_bool, pnlMsg, listErrores)\n------------------------------');
	console.log("v_bool : " + v_bool);
	console.log("pnlMsg : " + pnlMsg);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> exit on campos_requeridos');
		return;
	}
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	
	console.log('function agregar DONE\n------------------------------');
}

function salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('\n------------------------------\nfunction salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('      pnlOperacion : ' + pnlOperacion);
	console.log('       pnlMensajes : ' + pnlMensajes);
	console.log('         validador : ' + validador);

	console.log('Validando requeridos ...');
	if (campos_requeridos(validador)) {
		console.log('EXIT: REQUERIDOS\n------------------------------');
		return;
	}

	console.log('Validando proceso ...');
	if (operacionCancelada == false)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('salvar DONE\n------------------------------');
}

function eliminar(v_bool, pnlOperacion, pnlMsg) {
	console.log('function eliminar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	console.log('function eliminar DONE\n------------------------------');
}

function seleccionar(v_bool, pnlOperacion, pnlMsg) {
	console.log('function seleccionar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else 
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);

	console.log('function seleccionar DONE\n------------------------------');
}

function redireccionar(url) {
	console.log('redireccionar a: ' + url);
	window.location = url;
	console.log('redireccionar a: ' + url + " :: DONE");
}

function verEditar(v_bool, pnlOperacion, pnlMsg) {
	console.log('function verEditar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);

	console.log('function verEditar DONE\n------------------------------');
}

function revisado(v_bool, pnlOperacion, pnlMensaje) {
	console.log('function revisado(v_bool, pnlOperacion, pnlMensaje)\n------------------------------');
	console.log('>>>       v_bool : ' + v_bool);
	console.log('>>> pnlOperacion : ' + pnlOperacion);
	console.log('>>>   pnlMensaje : ' + pnlMensaje);
	
	if (v_bool == false)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	console.log('>>> fin revisado ------------------------------');
}

function reporte(operacionCancelada, pnlMensaje) {
	console.log('------------------------------\nfunction reporte(operacionCancelada, pnlMensaje)\n');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	else
		javascript:window.open('./reportes/ReporteGeneric.faces', 'Reporte', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	console.log('end function: DONE\n------------------------------');
}

function soloNumeros(event, permiteNegativos, debug) {
	var key = event.key;
	var keyCode = event.keyCode;
	var caracteresValidos = "0123456789";

	debug 			  = debug || false;
	permiteNegativos  = permiteNegativos || false;
	caracteresValidos = (permiteNegativos == true) ? (caracteresValidos + '-') : caracteresValidos;

	if (debug == true) {
		console.log("-> soloNumeros(event, permiteNegativos, debug)\n----------------------------------");
		console.log("->     Key: " + key);
		console.log("-> KeyCode: " + keyCode);
		console.log("-> Matcher: " + caracteresValidos);
		console.log(event);
	}

	if (event.ctrlKey == true && "CVXcvx".includes(key)) {
		if (debug == true)
			console.log("-----------\nControl + " + key + "\n-----------");
		return true;
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

	return (caracteresValidos.indexOf(key) > -1);
}

/* USO: onkeypress="return soloDecimales(event, this.value, 10, 2)" */
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

	if (event.ctrlKey == true && "CVXcvx".includes(key)) {
		if (debug == true)
			console.log("-----------\nControl + " + key + "\n-----------");
		/*if (matcher.test(decimal) == false) {
			alert("El numero indicado no es valido.\nDebe ser de maximo " + maxDigitos + " digitos y maximo " + maxDecimales + " decimales.");
			return false;
		}*/
		
		return true;
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
		var decimalTest = decimal.slice(0, indexKey);
		decimalTest += key;
		decimalTest += decimal.slice(indexKey);
		if (debug == true) {
			console.log("-> ----------------------------------");
			console.log("->valor updated : " + decimalTest);
			console.log("-> matcher.test : " + matcher.test(decimalTest));
		}

		return matcher.test(decimalTest);
	}
	
	return (caracteresValidos.indexOf(key) > -1);
}

function soloNumerosPostvalidate(idItem, decimal, maxDigitos, maxDecimales, permiteNegativos, debug) {
	var matcherString = '^([0-9]{0,MAXDIGIT})$|^([0-9]{0,MAXDIGIT})?([.][0-9]{1,MAXDECIMAL})?$';
	var matcher = null;
	var caracteresValidos = "0123456789.";
	
	// Inicializamos
	idItem 			  = idItem || '';
	decimal 		  = decimal || '';
	permiteNegativos  = permiteNegativos || false;
	caracteresValidos = (permiteNegativos == true) ? caracteresValidos + '-' : caracteresValidos;
	matcherString 	  = matcherString.replace(/MAXDIGIT/g, maxDigitos).replace('MAXDECIMAL', maxDecimales)
	decimal 		  = decimal.replace(/,/g, '');
	maxDigitos 		  = maxDigitos   || 10;
	maxDecimales 	  = maxDecimales ||  2;
	matcher 		  = new RegExp(matcherString);
	debug 			  = debug || false;

	if (debug == true) {
		console.log("-> soloNumerosPostvalidate(decimal, maxDigitos, maxDecimales, permiteNegativos, debug)\n----------------------------------");
		console.log("->      decimal : " + decimal);
		console.log("->   maxDigitos : " + maxDigitos);
		console.log("-> maxDecimales : " + maxDecimales);
		console.log("->matcherString : " + matcherString);
		console.log("->  validString : " + caracteresValidos);
	}

	if (decimal != '' && matcher.test(decimal) == false) {
		alert("El numero indicado no es valido.\nDebe ser de maximo " + maxDigitos + " digitos y maximo " + maxDecimales + " decimales.");
		if (idItem != '')
			document.getElementById(idItem).focus();
	}
}

function funPaste(event, decimal, maxDigitos, maxDecimales) {
	console.log(event);
	console.log("You pasted text!");
	soloNumerosPostvalidate('', decimal, maxDigitos, maxDecimales, false, true);
}