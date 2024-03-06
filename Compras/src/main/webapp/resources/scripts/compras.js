
function isDebugging() {
	var param = '';

	param = document.location.search;
	param = param || '';
	if (param == '')
		return false;
	param = param.replace('?','');
	if (param == '')
		return false;
	param = param.toUpperCase();
	param = param.split('&');
	if (param.length <= 0)
		return false;
	return (param[0] == 'DEBUG');
}

function buscar(operacionCancelada, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function buscar(operacionCancelada, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.buscar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 
	
	if (debugging) console.log('fun.buscar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes) { 
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.seleccionar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (debugging) console.log('fun.seleccionar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function nuevo(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function nuevo(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.nuevo :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('fun.nuevo :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function nuevoOrigen(operacionCancelada, esAdministrativo, pnlOrigen, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function nuevoOrigen(operacionCancelada, esAdministrativo, pnlOrigen, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOrigen          : ', pnlOrigen);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.nuevoOrigen :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 
	
	if (esAdministrativo == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOrigen);
		if (debugging) console.log('fun.nuevoOrigen :: DONE (Confirm Origen)');
		if (debugging) console.log('--------------------------------------------------------');
		return;
	}
		
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('fun.nuevoOrigen :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function editar(operacionCancelada, pnlOperacion, pnlMensajes) {
	verEditar(operacionCancelada, pnlOperacion, pnlMensajes);
}

function verEditar(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function verEditar(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.verEditar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 
		
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('fun.verEditar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function eliminar(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function eliminar(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (operacionCancelada == true) {
		console.log('fun.eliminar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 
	
	if (debugging) console.log('fun.eliminar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();
	
	if (debugging) console.log('\n------------------------------\nfunction salvar START');
	if (debugging) console.log(' operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('       pnlOperacion : ', pnlOperacion);
	if (debugging) console.log('        pnlMensajes : ', pnlMensajes);
	if (debugging) console.log(validador);

	if (debugging) console.log('\nValidando requeridos ... ');
	if(campos_requeridos(validador)) {
		if (debugging) console.log('function salvar REQUERIDOS\n------------------------------');
		return;
	}
	
	if (operacionCancelada == false)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('function salvar DONE\n------------------------------');
}

function salvarConfirmaCerrar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();
	
	if (debugging) console.log('\n------------------------------\nfunction salvarConfirmaCerrar START');
	if (debugging) console.log(' operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('       pnlOperacion : ', pnlOperacion);
	if (debugging) console.log('        pnlMensajes : ', pnlMensajes);
	if (debugging) console.log('          validador : ', validador);

	if (debugging) console.log('\nValidando requeridos ... ');
	if(campos_requeridos(validador)) {
		if (debugging) console.log('function salvar REQUERIDOS\n------------------------------');
		return;
	}
	
	if (operacionCancelada == false)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	else
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('function salvar DONE\n------------------------------');
}

function salvarSinCerrar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();
	
	if (debugging) console.log('\n------------------------------\nfunction salvarSinCerrar START');
	if (debugging) console.log(' operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('       pnlOperacion : ', pnlOperacion);
	if (debugging) console.log('        pnlMensajes : ', pnlMensajes);
	if (debugging) console.log('          validador : ', validador);

	if (debugging) console.log('\nValidando requeridos ... ');
	if(campos_requeridos(validador)) {
		if (debugging) console.log('function salvarSinCerrar REQUERIDOS\n------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('function salvarSinCerrar DONE\n------------------------------');
}

function mensaje(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();
	
	if (debugging) console.log('function mensaje\n------------------------------');
	if (debugging) console.log(operacionCancelada);
	if (debugging) console.log(pnlOperacion);
	if (debugging) console.log(pnlMensajes);
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMensajes);
	if (debugging) console.log('function mensaje DONE\n------------------------------');
}

function validar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();
	
	if (debugging) console.log('\n------------------------------\n');
	if (debugging) console.log('function validar(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	if (debugging) console.log(' operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('       pnlOperacion : ', pnlOperacion);
	if (debugging) console.log('        pnlMensajes : ', pnlMensajes);
	if (debugging) console.log('          validador : ', validador);

	if (debugging) console.log('\nValidando requeridos ... ');
	if(campos_requeridos(validador)) {
		if (debugging) console.log('REQUERIDOS\n------------------------------');
		return;
	}
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		if (debugging) console.log('ERROR\n------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('DONE\n------------------------------');
}

function soloNumeros(event, permiteNegativos, debug) {
	var key = event.key;
	var keyCode = event.keyCode;
	var caracteresValidos = "0123456789";

	debug 			  = debug || false;
	permiteNegativos  = permiteNegativos || false;
	caracteresValidos = (permiteNegativos == true) ? (caracteresValidos + '-') : caracteresValidos;

	if (debug == true) {
		if (debugging) console.log("-> soloNumeros(event, permiteNegativos, debug)\n----------------------------------");
		if (debugging) console.log("->     Key: ", key);
		if (debugging) console.log("-> KeyCode: ", keyCode);
		if (debugging) console.log("-> Matcher: ", caracteresValidos);
		if (debugging) console.log(event);
	}

	if (event.ctrlKey == true && "CVXcvx".includes(key)) {
		if (debug == true)
			if (debugging) console.log("-----------\nControl + " + key + "\n-----------");
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
	var debugging = false;
	var resultEvent = false;
	var matcherString = '^([0-9]{0,MAXDIGIT})$|^([0-9]{0,MAXDIGIT})?([.][0-9]{1,MAXDECIMAL})?$';
	var matcher = null;
	var caracteresValidos = "0123456789.";
	var key = event.key;
	var keyCode = event.keyCode;
	var indexKey = event.target.selectionStart;
	
	// Inicializamos
	debugging 		  = isDebugging();
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
		if (debugging) console.log("-> soloDecimales(event, decimal, maxDigitos, maxDecimales)\n----------------------------------");
		if (debugging) console.log("->          Key : ", key);
		if (debugging) console.log("->      KeyCode : ", keyCode);
		if (debugging) console.log("->      decimal : ", decimal);
		if (debugging) console.log("->   maxDigitos : ", maxDigitos);
		if (debugging) console.log("-> maxDecimales : ", maxDecimales);
		if (debugging) console.log("->     indexKey : ", indexKey);
		if (debugging) console.log("->matcherString : ", matcherString);
		if (debugging) console.log("->  validString : ", caracteresValidos);
		if (debugging) console.log(event);
	}

	if (event.ctrlKey == true && "CVXcvx".includes(key)) {
		if (debug == true)
			if (debugging) console.log("-----------\nControl + " + key + "\n-----------");
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
		
		decimal = decimal.slice(0, indexKey) + key + decimal.slice(indexKey); // Agregamos el caracter al valor actual del input (decimal) y evaluamos formato y longitud de las partes (digitos y decimales)
		if (debug == true) {
			if (debugging) console.log("-> ----------------------------------");
			if (debugging) console.log("->valor updated : ", decimal);
			if (debugging) console.log("-> matcher.test : ", matcher.test(decimal));
		}

		return matcher.test(decimal);
	}
	
	return (caracteresValidos.indexOf(key) > -1);
}

function reporte(operacionCancelada, pnlMensajes) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------\nfunction reporte(operacionCancelada, pnlMensajes)\n');
	if (debugging) console.log(' operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('        pnlMensajes : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	}

	reportWindow = window.open('./reportes/ReporteGeneric.faces', 'COMPRAS', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	if (window.focus) 
		reportWindow.focus();
	if (debugging) console.log('end function: DONE\n------------------------------');
}

function conversion(operacionCancelada, pnlMensajes) {
	console.log('------------------------------\nfunction conversion(operacionCancelada, pnlMensajes) START');
	console.log(' operacionCancelada : ', operacionCancelada);
	console.log(' pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function conversion DONE\n------------------------------');
}

function autoClicButton(row, btnId) {
	var debugging = isDebugging();
	
	console.log('funcion autoClicButton(row, btnId)');
	if (debugging) console.log(row);
	if (debugging) console.log('funcion');
	selector = row.attr('id');
	selector += ':' + btnId;
	btn = $(document.getElementById(selector));
	if (btn.prop('disabled') === false)
		btn.click();
}

function toggleAvanzado(btnId, panelId, itemOnId, itemOffId) {
	var debugging = false;
	var base = '';
	var selector = '';
	var splitted = [];
	var panel = '';
	var itemFocus = '';

	console.log('funcion toggleAvanzado(btnId, panelId, itemOnId, itemOffId)');
	debugging = isDebugging();
	splitted = btnId.split(":");
	splitted.pop();
	base = splitted.join(":") + ":";
	itemOnId  = itemOnId  || '';
	itemOffId = itemOffId || '';
	if (debugging) console.log('  btnId     : ', btnId);
	if (debugging) console.log('  panelId   : ', panelId);
	if (debugging) console.log('  itemOnId  : ', itemOnId);
	if (debugging) console.log('  itemOffId : ', itemOffId);
	if (debugging) console.log('  ------------------------- ');
	if (debugging) console.log('  base      : ', base);
	
	// Panel Toggle;
	selector = base + panelId;
	panel = $(document.getElementById(selector));
	if (debugging) console.log('Toggling ... ', selector);
	if (debugging) console.log('panel ', panel);
	panel.toggle();

	if (debugging) console.log('Validating to Focus ... ');
	selector = '';
	if (panel.prop('style').display == 'none' && itemOffId != '') 
		selector = base + itemOffId;
	else if (panel.prop('style').display != 'none' && itemOnId != '') 
		selector = base + itemOnId;
	if (selector != '') {
		if (debugging) console.log('Focusing ... ', selector);
		if (debugging) console.log('itemFocus ', itemFocus);
		itemFocus = $(document.getElementById(selector));
		itemFocus.focus();
	}
}

/* COTIZACIONES */

function nuevaCotizacion(operacionCancelada, origenValor, pnlOrigen, pnlOperacion, pnlBusqueda, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('----------------------------------------------------------------------------------------------------------');
	if (debugging) console.log('function nuevaCotizacion(operacionCancelada, origenValor, pnlOrigen, pnlOperacion, pnlBusqueda, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  origenValor        : ', origenValor);
	if (debugging) console.log('  pnlOrigen          : ', pnlOrigen);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlBusqueda        : ', pnlBusqueda);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);

	if (debugging) console.log('  Validando Error ... ');
	if (operacionCancelada == true) {
		console.log('fun.nuevaCotizacion :: ERROR\n------------------------------');
		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log('---> Mostrando pnlMensajes : ', pnlMensajes);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	if (debugging) console.log('  Validando origen ... ');
	if (origenValor == '' || origenValor == '?' || origenValor == undefined) {
		console.log('fun.nuevaCotizacion :: DONE\n------------------------------');
		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log('---> Mostrando pnlOrigen : ', pnlOrigen);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOrigen);
		return;
	} 

	if (debugging) console.log('  Terminando ... ');
	if (origenValor == 'R' || origenValor == 'A') {
		console.log('fun.nuevaCotizacion :: DONE\n------------------------------');
		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log('---> Mostrando pnlBusqueda : ', pnlBusqueda);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlBusqueda);
		return;
	}

	console.log('fun.nuevoFromRequisicion :: DONE\n------------------------------');
	if (debugging) console.log('\n\nDEBUG LOG --->');
	if (debugging) console.log('---> Mostrando pnlOperacion : ', pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
}

function seleccionarRequisicion(tipoMensaje, pnlBusqueda, pnlConfirmarSBO, pnlOperacion, pnlMensajes) { 
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('seleccionarRequisicion(tipoMensaje, pnlBusqueda, pnlConfirmarSBO, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  tipoMensaje     : ', tipoMensaje);
	if (debugging) console.log('  pnlBusqueda     : ', pnlBusqueda);
	if (debugging) console.log('  pnlConfirmarSBO : ', pnlConfirmarSBO);
	if (debugging) console.log('  pnlOperacion    : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes     : ', pnlMensajes);
	
	if (tipoMensaje === 100) {
		console.log('fun.seleccionarRequisicion :: 100 - CONFIRMAR SBO');
		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log('---> Ocultando pnlBusqueda : ', pnlBusqueda);
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlBusqueda);
		if (debugging) console.log('---> Mostrando pnlConfirmarSBO : ', pnlConfirmarSBO);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmarSBO);
		console.log('--------------------------------------------------------');
		return;
	} 

	if (tipoMensaje !== 0) {
		console.log('fun.seleccionarRequisicion :: ERROR');
		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log('---> Mostrando pnlMensajes : ', pnlMensajes);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('--------------------------------------------------------');
		return;
	} 

	console.log('fun.seleccionarRequisicion :: DONE');
	if (debugging) console.log('\n\nDEBUG LOG --->');
	if (debugging) console.log('---> Ocultando pnlBusqueda : ', pnlBusqueda);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlBusqueda);
	if (debugging) console.log('---> Mostrando pnlOperacion : ', pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('--------------------------------------------------------');
}

function lanzarSolicitud(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('-----------------------------------------------------------------------');
	if (debugging) console.log('function lanzarSolicitud(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);

	if (debugging) console.log('  Validando Error ... ');
	if (operacionCancelada == true) {
		console.log('fun.lanzarSolicitud :: ERROR\n------------------------------');
		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log('---> Mostrando pnlMensajes : ', pnlMensajes);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	console.log('fun.lanzarSolicitud :: DONE\n------------------------------');
	if (debugging) console.log('\n\nDEBUG LOG --->');
	if (debugging) console.log('---> Ocultando pnlOperacion : ', pnlOperacion);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
}

function actualizarExistencia(operacionCancelada, tipoMensaje, pnlConfirmarSBO, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('----------------------------------------------------------------------------------------------------------');
	if (debugging) console.log('function actualizarExistencia(operacionCancelada, tipoMensaje, pnlConfirmarSBO, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  tipoMensaje        : ', tipoMensaje);
	if (debugging) console.log('  pnlConfirmarSBO    : ', pnlConfirmarSBO);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);

	if (debugging) console.log('  Validando Error ... ');
	if (operacionCancelada == true) {
		if (tipoMensaje == 100) {
			console.log('fun.actualizarExistencia :: ERROR 100 - Material en SBO\n------------------------------');
			if (debugging) console.log('\n\nDEBUG LOG --->');
			if (debugging) console.log('---> Mostrando pnlConfirmarSBO : ', pnlConfirmarSBO);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmarSBO);
			if (debugging) console.log('---> Ocultando pnlOperacion : ', pnlOperacion);
			RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
			return;
		}

		console.log('fun.actualizarExistencia :: ERROR\n------------------------------');
		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log('---> Mostrando pnlMensajes : ', pnlMensajes);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	/*RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (tipoMensaje == -1)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('DONE\n------------------------------');*/
	console.log('fun.actualizarExistencia :: DONE\n------------------------------');
}

function nuevoEnvioCorreo(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('------------------------------------------------------------------------');
	if (debugging) console.log('function nuevoEnvioCorreo(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);

	if (debugging) console.log('  Validando Error ... ');
	if (operacionCancelada == true) {
		console.log('fun.nuevoEnvioCorreo :: ERROR\n------------------------------');
		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log('---> Mostrando pnlMensajes : ', pnlMensajes);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	console.log('fun.nuevoEnvioCorreo :: DONE\n------------------------------');
	if (debugging) console.log('\n\nDEBUG LOG --->');
	if (debugging) console.log('---> Mostrando pnlOperacion : ', pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
}

function enviarCorreo(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();

	if (debugging) console.log('------------------------------------------------------------------------');
	if (debugging) console.log('function nuevoEnvioCorreo(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	if (debugging) console.log('  validador          : ', validador);

	if (debugging) console.log('  Validando Requeridos ... ');
	if (campos_requeridos(validador)) {
		console.log('fun.enviarCorreo :: REQUERIDOS\n------------------------------');
		return;
	}

	if (debugging) console.log('  Validando Error ... ');
	if (operacionCancelada == true) {
		console.log('fun.enviarCorreo :: ERROR\n------------------------------');
		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log('---> Mostrando pnlMensajes : ', pnlMensajes);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}
	
	console.log('fun.enviarCorreo :: DONE\n------------------------------');
	if (debugging) console.log('\n\nDEBUG LOG --->');
	if (debugging) console.log('---> Ocultando pnlOperacion : ', pnlOperacion);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (debugging) console.log('---> Mostrando pnlMensajes : ', pnlMensajes);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
}

function comprobarOperacion(operacionCancelada, tipoMensaje, pnlConfirmarSBO, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('-------------------------------------------------------------------------------------------------------');
	if (debugging) console.log('function comprobarOperacion(operacionCancelada, tipoMensaje, pnlConfirmarSBO, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  tipoMensaje        : ', tipoMensaje);
	if (debugging) console.log('  pnlConfirmarSBO    : ', pnlConfirmarSBO);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);

	if (debugging) console.log('  Validando Error ... ');
	if (operacionCancelada == true) {
		if (tipoMensaje == 100) {
			console.log('fun.comprobarOperacion :: ERROR 100 - Material en SBO\n------------------------------');
			if (debugging) console.log('\n\nDEBUG LOG --->');
			if (debugging) console.log('---> Mostrando pnlConfirmarSBO : ', pnlConfirmarSBO);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmarSBO);
			return;
		}

		console.log('fun.comprobarOperacion :: ERROR\n------------------------------');
		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log('---> Mostrando pnlMensajes : ', pnlMensajes);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	console.log('fun.comprobarOperacion :: DONE\n------------------------------');
	if (debugging) console.log('\n\nDEBUG LOG --->');
	if (debugging) console.log('---> Mostrando pnlOperacion : ', pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
}

