function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion) {
	console.log('function msgRevisando\n------------------------------');
	console.log(listErrores);
	console.log(pnl1);
	console.log(pnl2);
	console.log(v_bool);
	console.log(invocacion);
	
	if (campos_requeridos(listErrores))
		return ;
	
	if (invocacion == 'guardar') {
		console.log('---> on guardar');
		if (v_bool == true) {
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		} else {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		}
	}
	
	if (invocacion == 'guardarConcepto'){
		console.log('---> on guardarConcepto');
		if (v_bool == true) {
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		} else {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		}
	}
		
	if (invocacion == 'buscar') {
		console.log('---> on buscar');
		if(v_bool == true){
			Richfaces.ui.PopupPanel.showPopupPanel(pnl2);
		}		 
	}
	 
	if (invocacion == 'boton') {	
		console.log('---> on boton');
		if (v_bool == true) {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		} else {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		}		
	}	
}

function salvar(valor, pnlOperacion, pnlMensajes, validador) {
	console.log('\n------------------------------\nfunction salvar(valor, pnlOperacion, pnlMensajes, validador)\n');
	console.log('operacionCancelada : ' + valor);
	console.log('pnlOperacion       : ' + pnlOperacion);
	console.log('pnlMensajes        : ' + pnlMensajes);
	console.log('validador          : ');
	console.log(validador);

	console.log(' ... validando campos requeridos');
	if (campos_requeridos(validador)) {
		console.log('exit on campos_requeridos\n------------------------------');
		return;
	}
	
	if (valor == "")
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('exit salvar\n------------------------------');
}

function guardar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('\n------------------------------\nfunction guardar(valor, pnlOperacion, pnlMensajes, validador)\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlOperacion       : ' + pnlOperacion);
	console.log('pnlMensajes        : ' + pnlMensajes);
	console.log('validador          : ');
	console.log(validador);

	console.log(' ... validando campos requeridos');
	if (campos_requeridos(validador)) {
		console.log('exit on campos_requeridos\n------------------------------');
		return;
	}

	if (operacionCancelada == true) {
		console.log('ERROR\n------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('exit guardar\n------------------------------');
}

function salvarSinCerrar(pnlMsg, listErrores) {
	console.log('function salvarSinCerrar(pnlMsg, listErrores)\n------------------------------');
	console.log(pnlMsg);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)) {
		console.log('exit on campos_requeridos\n------------------------------');
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('exit function\n------------------------------');
}

function salvarCuentaPorCobrar(valor, pnlMsg, listErrores){

	if(campos_requeridos(listErrores))
		return ;
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function salvarParametros(valor, pnlMsg, listErrores){

	if(campos_requeridos(listErrores))
		return ;
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function salvarGral(valor, pnlOperacion, pnlMsg){
	if (valor == "")
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function buscar(valor, pnlMsg){
	console.log('function buscar\n------------------------------');
	console.log(valor);
	console.log(pnlMsg);

	if (valor != "" && valor != "OK")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function buscar2(operacionCancelada, pnlMensajes, validador) {
	console.log('\n------------------------------\nfunction buscar2(operacionCancelada, pnlMensajes, listErrores)\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlMensajes        : ' + pnlMensajes);
	console.log('validador        : ');
	console.log(validador);

	console.log(' ... validando campos requeridos');
	if(campos_requeridos(validador)) {
		console.log('exit on campos_requeridos\n------------------------------');
		return;
	}

	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('exit buscar2\n------------------------------');
}

function evaluaMensaje(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\n------------------------------\nfunction evaluaMensaje(operacionCancelada, pnlOperacion, pnlMensajes)\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlOperacion       : ' + pnlOperacion);
	console.log('pnlMensajes        : ' + pnlMensajes);

	if (operacionCancelada == true) {
		console.log('ERROR\n------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('exit evaluaProvision\n------------------------------');
}

function evaluaProvision(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\n------------------------------\nfunction evaluaProvision(operacionCancelada, pnlOperacion, pnlMensajes)\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlOperacion       : ' + pnlOperacion);
	console.log('pnlMensajes        : ' + pnlMensajes);

	if (operacionCancelada == true) {
		console.log('ERROR\n------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('exit evaluaProvision\n------------------------------');
}

function provisionar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\n------------------------------\nfunction provisionar(operacionCancelada, pnlOperacion, pnlMensajes)\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlOperacion       : ' + pnlOperacion);
	console.log('pnlMensajes        : ' + pnlMensajes);

	if (operacionCancelada == true) {
		console.log('ERROR\n------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('exit provisionar\n------------------------------');
}

function agregar(operacionCancelada, pnlMensajes, validador) {
	console.log('\n------------------------------\nfunction agregar(operacionCancelada, pnlMensajes, validador)\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlMensajes        : ' + pnlMensajes);
	console.log('validador          : ');
	console.log(validador);

	console.log(' ... validando campos requeridos');
	if (campos_requeridos(validador)) {
		console.log('ERROR validador\n------------------------------');
		return;
	}

	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('OK agregar\n------------------------------');
}

function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\n------------------------------\nfunction seleccionar(operacionCancelada, pnlOperacion, pnlMensajes)\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlOperacion       : ' + pnlOperacion);
	console.log('pnlMensajes        : ' + pnlMensajes);

	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	else
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('OK seleccionar\n------------------------------');
}

function buscarProyectos(valor, pnlMsg) {
	console.log('function buscarProyectos(valor, pnlMsg)\n------------------------------');
	console.log(valor);
	console.log(pnlMsg);
	
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('exit function buscarProyectos\n------------------------------');
}

function buscarConceptos(v_bool, pnlMsg) {
	console.log('function buscarConceptos(v_bool, pnlMsg)\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('exit function buscarConceptos\n------------------------------');
}

function eliminar(valor, pnlOperacion, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
}

function eliminarFactura(v_bool, pnlMsg) {
	console.log('function eliminarFactura\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);

	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function eliminarFactura DONE\n------------------------------');
}

function eliminarDetalle(v_bool, pnlMsg) {
	console.log('function eliminarDetalle\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);

	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function eliminarDetalle DONE\n------------------------------');
}

function buscar(valor, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function imprimeRep(valor, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else
		 window.open("../../reportes/ReporteGeneric.faces");
}

function timbrar(pnlMsg) {
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function enviarCorreo(v_bool, pnlOperacion, pnlMsg, listErrores) {
	console.log('function enviarCorreo(v_bool, pnlOperacion, pnlMsg, listErrores)\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> exit on campos_requeridos');
		return;
	}

	if (v_bool == false)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('exit function enviarCorreo\n------------------------------');
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
