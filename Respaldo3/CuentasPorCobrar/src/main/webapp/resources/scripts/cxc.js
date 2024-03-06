
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

function salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	if (debugging) console.log('  validador          : ', validador);

	if (debugging) console.log('\nValidando requeridos ... ');
	if (campos_requeridos(validador)) {
		if (debugging) console.log('fun.salvarSinCerrar :: REQUERIDOS');
		if (debugging) console.log('--------------------------------------------------------');
		return;
	}
	
	if (operacionCancelada == true) {
		console.log('fun.salvar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 
		
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('fun.salvar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function salvarSinCerrar(operacionCancelada, pnlMensajes, validador) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function salvarSinCerrar(operacionCancelada, pnlMensajes, validador)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	if (debugging) console.log('  validador          : ', validador);

	if (debugging) console.log('\nValidando requeridos ... ');
	if (campos_requeridos(validador)) {
		if (debugging) console.log('fun.salvarSinCerrar :: REQUERIDOS');
		if (debugging) console.log('--------------------------------------------------------');
		return;
	}
	
	if (operacionCancelada == true) {
		console.log('fun.salvarSinCerrar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 
		
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('fun.salvarSinCerrar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

//watch 
/*function salvarSinCerrarOLD(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function salvarSinCerrar(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	if (debugging) console.log('  validador          : ', validador);

	if (debugging) console.log('\nValidando requeridos ... ');
	if (campos_requeridos(validador)) {
		if (debugging) console.log('fun.salvarSinCerrar :: REQUERIDOS');
		if (debugging) console.log('--------------------------------------------------------');
		return;
	}
	
	if (operacionCancelada == true) {
		console.log('fun.salvarSinCerrar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 
		
	//RichFaces.ui.PopupPanel.hidePopupPanel(pnlMensajes);
	if (debugging) console.log('fun.salvarSinCerrar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}*/

function evaluaCancelar(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function evaluaCancelar(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.evaluaCancelar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('fun.evaluaCancelar :: CANCELABLE');
	if (debugging) console.log('--------------------------------------------------------');
}

function evaluaCancelarPago(operacionCancelada, tipoMensaje, pnlConfirmar, pnlCancelar, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function evaluaCancelarPago(operacionCancelada, tipoMensaje, pnlConfirmar, pnlCancelar, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  tipoMensaje        : ', tipoMensaje);
	if (debugging) console.log('  pnlConfirmar       : ', pnlConfirmar);
	if (debugging) console.log('  pnlCancelar        : ', pnlCancelar);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.evaluaCancelarPago :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}
	
	if (tipoMensaje == -2) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmar);
		if (debugging) console.log('fun.evaluaCancelarPago :: CONFIRMAR');
		if (debugging) console.log('--------------------------------------------------------');
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlCancelar);
	if (debugging) console.log('fun.evaluaCancelarPago :: CANCELABLE');
	if (debugging) console.log('--------------------------------------------------------');
}

function eliminarOperacion(operacionCancelada, pnlOperacion, pnlMensajes) { 
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function eliminarOperacion(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.eliminarOperacion :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (debugging) console.log('fun.eliminarOperacion :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function eliminar(operacionCancelada, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function eliminar(operacionCancelada, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.eliminar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	if (debugging) console.log('fun.eliminar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function cancelarFacturaRefacturar(operacionCancelada, refacturar, pnlEliminar, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function cancelarFacturaRefacturar(operacionCancelada, refacturar, pnlEliminar, pnlOperacion, pnlMensajes, validador)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  refacturar         : ', refacturar);
	if (debugging) console.log('  pnlEliminar        : ', pnlEliminar);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	if (debugging) console.log('  validador          : ', validador);

	if (debugging) console.log('\nValidando requeridos ... ');
	if (campos_requeridos(validador)) {
		if (debugging) console.log('fun.cancelarFacturaRefacturar :: REQUERIDOS');
		if (debugging) console.log('--------------------------------------------------------');
		return;
	}
	
	if (operacionCancelada == true) {
		console.log('fun.cancelarFacturaRefacturar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlEliminar);
	if (refacturar == true) {
		if (debugging) console.log('fun.cancelarFacturaRefacturar :: REFACTURAR');
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
		return;
	}
	
	if (debugging) console.log('fun.cancelarFacturaRefacturar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

//watch
/*function cancelarPago(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function cancelarPago(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.cancelarPago :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (debugging) console.log('fun.cancelarPago :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}*/

function refacturar(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function refacturar(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.refacturar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('fun.refacturar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function evaluaProvision(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function evaluaProvision(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.evaluaProvision :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('fun.evaluaProvision :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function provisionar(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function provisionar(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.provisionar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (debugging) console.log('fun.provisionar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function busquedaDinamica(pnl) {
	var debugging = isDebugging();
	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function busquedaDinamica(pnlOperacion)');
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnl);
	if (debugging) console.log('fun.busquedaDinamica :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function reporte(operacionCancelada, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function reporte(operacionCancelada, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.reporte :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	} 

	reportWindow = window.open('./reportes/ReporteGeneric.faces', 'CXC', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	if (window.focus) 
		reportWindow.focus();
	if (debugging) console.log('fun.reporte :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function evaluaTimbrar(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function evaluaTimbrar(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.evaluaTimbrar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('fun.evaluaTimbrar :: TIMBRABLE');
	if (debugging) console.log('--------------------------------------------------------');
}

function timbrar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function timbrar(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	if (debugging) console.log('  validador          : ', validador);

	if (debugging) console.log('\nValidando requeridos ... ');
	if (campos_requeridos(validador)) {
		if (debugging) console.log('fun.timbrar :: REQUERIDOS');
		if (debugging) console.log('--------------------------------------------------------');
		return;
	}
	
	if (operacionCancelada == true) {
		console.log('fun.timbrar :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (debugging) console.log('fun.timbrar :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function timbrarPago(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function timbrarPago(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('fun.timbrarPago :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (debugging) console.log('fun.timbrarPago :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
}

function enviarCorreo(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();

	if (debugging) console.log('--------------------------------------------------------');
	if (debugging) console.log('function enviarCorreo(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	if (debugging) console.log('  operacionCancelada : ', operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ', pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ', pnlMensajes);
	if (debugging) console.log('  validador          : ', validador);

	if (debugging) console.log('\nValidando requeridos ... ');
	if (campos_requeridos(validador)) {
		if (debugging) console.log('fun.enviarCorreo :: REQUERIDOS');
		if (debugging) console.log('--------------------------------------------------------');
		return;
	}
	
	if (operacionCancelada == true) {
		console.log('fun.enviarCorreo :: ERROR');
		console.log('--------------------------------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('fun.enviarCorreo :: DONE');
	if (debugging) console.log('--------------------------------------------------------');
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

