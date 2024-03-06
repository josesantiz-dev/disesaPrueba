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

function buscar(operacionCancelada, pnlMensajes) {
	console.log('function buscar\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	console.log('function buscar DONE\n------------------------------');
}

function nuevaBusqueda(operacionCancelada, pnlOperacion, pnlMensajes) {
 	console.log('function nuevaBusqueda(operacionCancelada, pnlOperacion, pnlMensajes)\n------------------------------');
 	console.log(' operacionCancelada : ' + operacionCancelada);
 	console.log(' pnlOperacion       : ' + pnlOperacion);
 	console.log(' pnlMensajes        : ' + pnlMensajes);
 	
 	if (operacionCancelada == true) {
 		Richfaces.showModalPanel(pnlMensajes);
 	 	console.log('ERROR\n------------------------------');
 		return;
 	} 
 	
 	Richfaces.showModalPanel(pnlOperacion);
 	console.log('DONE\n------------------------------');
}

function agregar(operacionCancelada, pnlMensajes, validador) {
	console.log('function agregar(operacionCancelada, pnlMensajes, validador)\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlMensajes);
	console.log(validador);
	
	if(campos_requeridos(validador)){
		console.log('---> exit on campos_requeridos');
		return;
	}
	
	if (operacionCancelada == true)
		Richfaces.hideModalPanel(pnlMensajes);
	
	console.log('function agregar DONE\n------------------------------');
}

function salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('\nfunction salvar------------------------------\n');
	console.log('  operacionCancelada : ' + operacionCancelada);
	console.log('  pnlOperacion       : ' + pnlOperacion);
	console.log('  pnlMensajes        : ' + pnlMensajes);
	console.log('  validador          : ', validador);

	console.log('\nValidando requeridos ... ');
	if (campos_requeridos(validador)) {
		console.log('REQUERIDOS\n------------------------------');
		return;
	}

	console.log('\nValidando requeridos ... OK');
	if (operacionCancelada == false)
		Richfaces.hideModalPanel(pnlOperacion);
	Richfaces.showModalPanel(pnlMensajes);
	if (operacionCancelada == true) {
		console.log('ERROR\n------------------------------');
		return;
	}
	
	console.log('DONE\n------------------------------');
}

function salvarNoCerrar(pnlMensajes, validador) {
	console.log('\nfunction salvarNoCerrar------------------------------\n');
	console.log('pnlMensajes        : ' + pnlMensajes);
	console.log('validador          : ' + validador);

	console.log('\nValidando requeridos');
	if(campos_requeridos(validador)) {
		console.log('function salvarNoCerrar REQUERIDOS\n------------------------------');
		return;
	}

	Richfaces.showModalPanel(pnlMensajes);
	console.log('function salvarNoCerrar DONE\n------------------------------');
}

function salvarTipoMensaje(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, validador) {
	console.log('\nfunction salvar------------------------------\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('       tipoMensaje : ' + tipoMensaje);
	console.log('      pnlOperacion : ' + pnlOperacion);
	console.log('       pnlMensajes : ' + pnlMensajes);
	console.log('         validador : ' + validador);

	console.log('\nValidando requeridos');
	if(campos_requeridos(validador)) {
		console.log('function salvar REQUERIDOS\n------------------------------');
		return;
	}
	
	if (operacionCancelada == false)
		Richfaces.hideModalPanel(pnlOperacion);
	
	if (tipoMensaje == 0)
		Richfaces.showModalPanel(pnlMensajes);
	console.log('function salvar(operacionCancelada, tipo_mensaje, pnlOperacion, pnlMensajes, validador) DONE\n------------------------------');
}

function eliminar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('function eliminar\n------------------------------');
	console.log('  operacionCancelada : ' + operacionCancelada);
	console.log('  pnlOperacion       : ' + pnlOperacion);
	console.log('  pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensajes);
		Richfaces.hideModalPanel(pnlOperacion);
		console.log('ERROR\n------------------------------');
		return;
	}
	
	Richfaces.hideModalPanel(pnlOperacion);
	console.log('DONE\n------------------------------');
}

function eliminarCancelar(operacionCancelada, pnlOperacion, pnlMensajes) {
	 console.log('----------------------------------------');
	 console.log('function eliminarCancelar(operacionCancelada, pnlOperacion, pnlMensajes)');
	 console.log('  operacionCancelada : ' + operacionCancelada);
	 console.log('  pnlOperacion       : ' + pnlOperacion);
	 console.log('  pnlMensajes        : ' + pnlMensajes);
	 	
 	if (operacionCancelada == true)
 		Richfaces.showModalPanel(pnlMensajes);
 	Richfaces.hideModalPanel(pnlOperacion);
	console.log((operacionCancelada == true ? 'ERROR' : 'DONE') + '\n----------------------------------------');
}

function mensaje(operacionCancelada, pnlMensajes) {
	console.log('function mensaje(operacionCancelada, pnlMensajes)\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	else
		Richfaces.hideModalPanel(pnlMensajes);
	console.log('function mensaje(operacionCancelada, pnlMensajes) DONE\n------------------------------');
}

function validar(operacionCancelada, pnlMensajes) {
	console.log('function validar(operacionCancelada, pnlMensajes)\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	console.log('DONE\n------------------------------');
}

function reporte(operacionCancelada, pnlMensajes) {
	console.log('------------------------------\nfunction reporte(operacionCancelada, pnlMensajes)\n');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('        pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensajes);
		return;
	}
	
	javascript:window.open('./reportes/ReporteGeneric.faces', 'Reporte', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	console.log('end function: DONE\n------------------------------');
}

function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('function seleccionar\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	else 
		Richfaces.hideModalPanel(pnlOperacion);

	console.log('function seleccionar DONE\n------------------------------');
}

function redireccionar(url) {
	console.log('redireccionar a: ' + url);
	window.location = url;
	console.log('redireccionar a: ' + url + " :: DONE");
}

function verEditar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('function verEditar\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	else 
		Richfaces.showModalPanel(pnlOperacion);

	console.log('function verEditar DONE\n------------------------------');
}

function validarNuevaComprobacion(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	 console.log('funcion validarNuevaComprobacion(operacionCancelada, pnlOperacion, pnlMensajes, validador)\n----------------------------------------');
	 console.log('  operacionCancelada :', operacionCancelada);
	 console.log('  pnlOperacion       :', pnlOperacion);
	 console.log('  pnlMensajes         :', pnlMensajes);
	 console.log('  ---------------------------------------------------------------------');
	 console.log('  validador          :', validador);

	 console.log("Evalua REQUERIDOS");
	 if (validador.hasChildNodes()) {
		 console.log("REQUERIDOS\n----------------------------------------");
		 return;
	 }

	 if (operacionCancelada == true)  {
		 Richfaces.showModalPanel(pnlMensajes);
		 console.log("ERROR\n----------------------------------------");
		 return;
	 }
	 
	 Richfaces.showModalPanel(pnlOperacion);
	 console.log('DONE\n----------------------------------------');
}

function evaluaEditar(operacionCancelada, conFactura, pnlOperacion1, pnlOperacion2, pnlMensajes) {
	console.log('---------------------------------------------------------------------------');
	console.log('function evaluaEditar(operacionCancelada, conFactura, pnlOperacion1, pnlOperacion2, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    conFactura         : ' + conFactura);
	console.log('    pnlOperacion1      : ' + pnlOperacion1);
	console.log('    pnlOperacion2      : ' + pnlOperacion2);
	console.log('    pnlMensajes         : ' + pnlMensajes);
	
	if (operacionCancelada == true) { 
		Richfaces.showModalPanel(pnlMensajes);
		console.log('ERROR\n---------------------------------------------------------------------------');
		return;
	} 
	
	if (conFactura == true) 
		Richfaces.showModalPanel(pnlOperacion1);
	else 
		Richfaces.showModalPanel(pnlOperacion2);
	console.log('OK\n---------------------------------------------------------------------------');
}

function cargarXML(operacionCancelada, tipoMensaje, multiplesConceptos, pnlUpload, pnlComprobacion, pnlFactura, pnlMensajes) {
	console.log('---------------------------------------------------------------------------');
	console.log('function cargarXML(operacionCancelada, tipoMensaje, multiplesConceptos, pnlUpload, pnlComprobacion, pnlFactura, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    tipoMensaje        : ' + tipoMensaje);
	console.log('    multiplesConceptos : ' + multiplesConceptos);
	console.log('    pnlUpload          : ' + pnlUpload);
	console.log('    pnlComprobacion    : ' + pnlComprobacion);
	console.log('    pnlFactura         : ' + pnlFactura);
	console.log('    pnlMensajes         : ' + pnlMensajes);
	
	if (operacionCancelada == true) { 
		Richfaces.showModalPanel(pnlMensajes);
		console.log('ERROR\n---------------------------------------------------------------------------');
		return;
	} 
	
	Richfaces.hideModalPanel(pnlUpload);
	if (multiplesConceptos == true)
		Richfaces.showModalPanel(pnlFactura); 
	else
		Richfaces.showModalPanel(pnlComprobacion); 
	if (tipoMensaje == 30) 
		Richfaces.showModalPanel(pnlMensajes);
	if (tipoMensaje == 31) 
		Richfaces.showModalPanel(pnlMensajes);
	console.log('OK\n---------------------------------------------------------------------------');
}

function descargar(operacionCancelada, modulo, pnlMensajes) {
	console.log('\n------------------------------');
	console.log('function descargar(operacionCancelada, modulo, pnlMensajes)');
	console.log('      operacionCancelada : ' + operacionCancelada);
	console.log('      modulo : ' + modulo);
	console.log(' pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensajes);
		console.log('\nERROR\n------------------------------');
		return;
	}
	
	window.open(modulo + '/reportes/ReporteGeneric.faces', 'Satic', 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
	console.log('\nDONE\n------------------------------');
}

function evalua(operacionCancelada, pnlOperacion, pnlMensajes) {
	 console.log('----------------------------------------');
	 console.log('evalua(operacionCancelada, pnlOperacion, pnlMensajes)');
	 console.log('      operacionCancelada : ' + operacionCancelada);
	 console.log('pnlOperacion : ' + pnlOperacion);
	 console.log('  pnlMensajes : ' + pnlMensajes);
	 	
 	if (operacionCancelada == true) {
 		Richfaces.showModalPanel(pnlMensajes);
 		console.log('funcion ERROR\n----------------------------------------');
 		return;
 	}
 	
 	Richfaces.showModalPanel(pnlOperacion);
	console.log('funcion DONE\n----------------------------------------');
}

function cambiaValor(event, itemId, itemValue, defaultValue) {
	var key = event.key;
	var keyCode = event.keyCode;

	console.log("cambiaValor\n-----------------");
	console.log("->       Key: " + key);
	console.log("->   KeyCode: " + keyCode);
	console.log("->    itemId: " + itemId);
	console.log("-> itemValue: " + itemValue);
	console.log(event);
	
	if (itemValue == '' || isNaN(itemValue)) {
		itemValue = defaultValue;
	} else {
		itemValue = parseInt(itemValue);
		if (itemId != undefined && itemValue != undefined) {
			if (keyCode == 38)
				itemValue = itemValue + 1;
			if (keyCode == 40)
				itemValue = itemValue - 1;
			if (itemValue < 0)
				itemValue = 0;
		}
	}
	
	document.getElementById(itemId).value = itemValue;
}

function soloHora(event, itemValue) {
	var caracteresValidos = "0123456789:";
	var key = '';
	var keyCode = 0;
	var shiftKey = false;
	var hora = 0;
	var mins = 0;
	
	if (window.event) {
		key = window.event.key;
		keyCode = window.event.keyCode;
		shiftKey = !!window.event.shiftKey;
	} else {
		key = event.key;
		keyCode = event.keyCode;
		shiftKey = !!event.shiftKey;
	}

	console.log("soloHora\n-----------------");
	console.log("->       Key: " + key);
	console.log("->   KeyCode: " + keyCode);
	console.log("-> itemValue: " + itemValue);
	
	// caracteres especiales validos
	if ( keyCode ==  8 || keyCode ==  9 || keyCode == 35 || 
		 keyCode == 36 || keyCode == 37 || keyCode == 38 || 
		 keyCode == 39 || keyCode == 40 || keyCode == 46 ) {
		console.log("caracteres especiales");
		return true;
	}
	
	if (shiftKey) {
		switch (keyCode) {
	      case 16: // ignore shift key
	        break;
	      default:
	    	  if ( itemValue != undefined && (key == ":" && itemValue.indexOf(key) > -1)) {
	    			console.log("caracter especifico");
	    			return false;
	    		}
	        break;
	    }
	} else {
		// si la tecla es "caracterEspecial" y el valor del item ya lo contiene, nos salimos
		if ( itemValue != undefined && (key == ":" && itemValue.indexOf(key) > -1)) {
			console.log("caracter especifico");
			return false;
		}
	}

	// validamos caracteres
	return (caracteresValidos.indexOf(key) > -1);
}

function validaHora(itemId, itemValue, mensaje, clearItem) {
	var item = document.getElementById(itemId);
	var patron=/^(0[1-9]|1\d|2[0-3]):([0-5]\d)(:([0-5]\d))?$/;
	
	console.log("validaHora\n-----------------");
	console.log("->    itemId: " + itemId);
	console.log("-> itemValue: " + itemValue);
	console.log("->   mensaje: " + mensaje);
	console.log("-> clearItem: " + clearItem);
	
	if(itemValue == "")
		return true;
	
	if(itemValue.length < 5 && itemValue.indexOf(":") > -1) {
		var splitted = itemValue.split(":");
		
		if (isNaN(splitted[0]) == false && isNaN(splitted[1]) == false) {
			if (parseInt(splitted[0]) < 10)
				itemValue = "0";
			itemValue += splitted[0] + ":";
			
			if (parseInt(splitted[1]) < 10)
				itemValue += "0";
			itemValue += splitted[1];
			
			item.value = itemValue;
		}
	}
	  
	if (! patron.test(itemValue)) {
		console.log("->   mensaje: " + mensaje);
		alert(mensaje);
		if (clearItem == undefined || clearItem == true)
			item.value = "";
		item.focus();
		return false;
	}

	console.log("->   formato valido!");
	return true;
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
