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
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function buscar DONE\n------------------------------');
}

function salvar(operacionCancelada, pnlOperacion, pnlMensajes, listErrores){
	console.log('function salvar(operacionCancelada, pnlOperacion, pnlMensajes, listErrores)\n------------------------------');
	console.log('      operacionCancelada: ' + operacionCancelada);
	console.log('pnlOperacion: ' + pnlOperacion);
	console.log('      pnlMensajes: ' + pnlMensajes);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> exit on campos_requeridos');
		return;
	}
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	} else {
		if (pnlOperacion != '') {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		} else {
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		}
	}

	console.log('end function salvar\n------------------------------');
}

function eliminar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('function eliminar\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	console.log('function eliminar DONE\n------------------------------');
}

function mensaje(operacionCancelada, pnlMensajes) {
	console.log('function mensaje\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlMensajes);
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMensajes);
	
	console.log('function mensaje DONE\n------------------------------');
}

function mensajeDetalles(pnlOperacion, pnlMensajes) {
	console.log('function mensajeDetalles\n------------------------------');
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMensajes);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	
	console.log('function mensajeDetalles DONE\n------------------------------');
}

function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log("-----------------------------------");
	console.log("seleccionar(operacionCancelada, pnlOperacion, pnlMensajes)");
	console.log("   operacionCancelada : " + operacionCancelada);
	console.log("   pnlOperacion       : " + pnlOperacion);
	console.log("   pnlMensajes        : " + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

function redireccionar(url) {
	console.log('redireccionar a: ' + url);
	window.location = url;
	console.log('redireccionar a: ' + url + " :: DONE");
}

function reporte(operacionCancelada, pnlMensajes) {
	console.log('------------------------------\nfunction reporte(operacionCancelada, pnlMensajes)\n');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('        pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	}

	reportWindow = window.open('./reportes/ReporteGeneric.faces', 'RH', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	if (window.focus) 
		reportWindow.focus();
	console.log('end function: DONE\n------------------------------');
}

function verEditar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('function verEditar\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	else 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);

	console.log('function verEditar DONE\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx');
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
	var patron=/^(0[0-9]|1\d|2[0-3]):([0-5]\d)(:([0-5]\d))?$/;
	
	console.log("function validaHora('" + itemId + "', '" + itemValue + "', '" + mensaje + "', " + clearItem + ")\n----------------------------------");
	
	if(itemValue == "" || itemValue == "00:00") {
		console.log("\n------------------------------------");
		console.log("->   Return true :: Formato valido!");
		//return true;
	}
	
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
	  
	if (patron.test(itemValue) == false) {
		console.log("->   mensaje: " + mensaje);
		alert(mensaje);
		if (clearItem == undefined || clearItem == true)
			item.value = "";
		item.focus();
		console.log("\n------------------------------------");
		console.log("->   Return false :: Formato invalido!");
		//return false;
	}

	console.log("\n------------------------------------");
	console.log("->   Return true :: Formato valido!");
	//return true;
}

function validaCaracteres(event, permitirEspacio, permitirGuionMedio, permitirGuionBajo, debug) {
	var key = event.key;
	var keyCode = event.keyCode;
	var caracteresValidos = "abcdefghihjklmnñopqrstuvwxyzABCDEFGHIHJKLMNÑOPQRSTUVWXYZ0123456789";
	
	permitirEspacio 	= permitirEspacio || false;
	permitirGuionMedio 	= permitirGuionMedio || false;
	permitirGuionBajo 	= permitirGuionBajo || false;
	debug 				= debug || false;

	caracteresValidos 	+= (permitirEspacio == true) 	? ' ' : '';
	permitirGuionMedio 	+= (permitirGuionMedio == true) ? '-' : '';
	permitirGuionBajo 	+= (permitirGuionBajo == true)  ? '_' : '';

	if (debug == true) {
		console.log("-> validaCaracteres(event, permitirEspacio, permitirGuionMedio, permitirGuionBajo, debug)\n----------------------------------");
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
