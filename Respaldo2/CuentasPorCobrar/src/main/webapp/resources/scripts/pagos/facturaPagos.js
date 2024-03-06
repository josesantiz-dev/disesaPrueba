function msgRevisando(listErrores, pnl1, pnl2, v_bool, invocacion) {
	console.log('function msgRevisando\n------------------------------');
	console.log(listErrores);
	console.log(pnl1);
	console.log(pnl2);
	console.log(v_bool);
	console.log(invocacion);
	
	if(listErrores.hasChildNodes()) {
		console.log('---> on hasChildNodes');
		return;	
	}
		
	if(campos_requeridos(listErrores)) {
		console.log('---> on campos_requeridos');
		return;
	}
	
	if(invocacion == 'editar'){
		console.log('---> on editar');
		if(v_bool == true){
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		} else {
			RichFaces.ui.PopupPanel.showPopupPanel(pnl1);
		}
	}
	
	if(invocacion == 'guardar'){
		console.log('---> on guardar');
		if(v_bool == true){
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		} else {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		}
	}
		
	if(invocacion == 'buscar'){
		console.log('---> on buscar');
		if(v_bool == true){
			Richfaces.ui.PopupPanel.showPopupPanel(pnl2);
		}		 
	}
	 
	if(invocacion == 'boton'){	
		console.log('---> on boton');
		if(v_bool == true){
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		} else {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		}		
	}	
}

function buscarFacturas(v_bool, pnlMsg){
	console.log('function buscarFacturas\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	
	console.log('function buscarFacturas DONE\n------------------------------');
}

function buscarFacturas2(v_bool, pnlOperacion, pnlMsg, listErrores) {
	if(campos_requeridos(listErrores)){
		console.log('---> on campos_requeridos');
		return;
	}
	
	if (! v_bool) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
		console.log('---> v_bool false');
	}
}

function salvar(v_bool, pnlOperacion, pnlMsg, listErrores){
	console.log('function salvar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> on campos_requeridos');
		return;
	}
	
	if (! v_bool) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		console.log('---> v_bool false');
	}
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function evaluaTimbrar(operacion, pnlOperacion, pnlMensajes) {
	console.log('------------------------------');
	console.log('function evaluaTimbrar(operacion, pnlOperacion, pnlMensajes)');
	console.log('    operacion    : ' + operacion);
	console.log('    pnlOperacion : ' + pnlOperacion);
	console.log('    pnlMensajes  : ' + pnlMensajes);

	console.log('  --> validando ... ');
	if (operacion == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('\nERROR\n------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('\nOK\n------------------------------');
}

function timbrar(operacion, pnlOperacion, pnlMensajes) {
	console.log('------------------------------');
	console.log('function timbrar(operacion, pnlOperacion, pnlMensajes)');
	console.log('    operacion    : ' + operacion);
	console.log('    pnlOperacion : ' + pnlOperacion);
	console.log('    pnlMensajes  : ' + pnlMensajes);

	console.log('  --> validando ... ');
	if (operacion == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('\nERROR\n------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('\nOK\n------------------------------');
}

function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes, multiSeleccion) {
	console.log('------------------------------');
	console.log('function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes, multiSeleccion)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	console.log('    multiSeleccion     : ' + multiSeleccion);

	console.log('  --> validando ... ');
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('\nERROR\n------------------------------');
		return;
	}
	
	if (multiSeleccion == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('\nOK\n------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('\nOK\n------------------------------');
}

function multiSeleccion(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('------------------------------\nfunction seleccionar(operacionCancelada, pnlOperacion, pnlMensajes)\n');
	console.log('  operacionCancelada : ' + operacionCancelada);
	console.log('  pnlOperacion       : ' + pnlOperacion);
	console.log('  pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('function seleccionar ERROR\n------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('function seleccionar OK\n------------------------------');
}

function mensaje(v_bool, pnlOperacion, pnlMsg, listErrores) {
	console.log('function mensaje\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> on campos_requeridos');
		return;
	}
	
	if (! v_bool) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
		console.log('---> v_bool false');
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
		console.log('---> v_bool true');
	}
}

function reporte(operacionCancelada, pnlMensajes) {
	console.log('------------------------------');
	console.log('function reporte(operacionCancelada, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlMensajes        : ' + pnlMensajes);

	console.log('  --> validando ... ');
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('\nERROR\n------------------------------');
		return;
	}

	javascript:window.open('./reportes/ReporteGeneric.faces', 'Reporte', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	console.log('\nOK\n------------------------------');
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