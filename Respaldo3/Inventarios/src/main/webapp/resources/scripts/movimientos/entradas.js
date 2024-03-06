
var almacenAnterior =0;
var almacenNuevo=0;

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

function buscar(v_bool, pnlMsg) {
	if (v_bool==true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function guardar(operacionCancelada, pnlOperacion, pnlMensaje, validador) {
	var debugging = isDebugging();
	
	if (debugging) console.log('--------------------------------------------------------------');
	if (debugging) console.log('funcion guardar(operacionCancelada, pnlOperacion, pnlMensaje, validador)');
	if (debugging) console.log('   operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('   pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('   pnlMensaje         : ' + pnlMensaje);
	if (debugging) console.log('   validador          : ', validador);

	if (debugging) console.log('validando requeridos ... ');
	if (campos_requeridos(validador)) {
		if (debugging) console.log('REQUERIDOS\n--------------------------------------------------------------');
		return;
	}

	if (debugging) console.log('comprobando error ... ');
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR\n--------------------------------------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	window.open('./reportes/ReporteGeneric.faces', 'Entrada a Almacen', 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
	if (debugging) console.log('OK');
	if (debugging) console.log('--------------------------------------------------------------');
}

function reporte(operacionCancelada, pnlMensaje) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------')
	if (debugging) console.log('function reporte(operacionCancelada, pnlMensaje)');
	if (debugging) console.log('    operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('            pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
		return;
	}
	
	javascript:window.open('./reportes/ReporteGeneric.faces', 'Reporte', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	if (debugging) console.log('DONE\n------------------------------');
}

function validaGuardarMovimiento(operacionCompleta, pnlError, pnlprincipal, pnlOK, btnSalvar, listErr){
	var debugging = isDebugging();
	
	if (operacionCompleta == false) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlError);
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlprincipal);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOK);
	}
	
	$(btnSalvar).prop('disabled',operacionCompleta);
	if (debugging) console.log('funcion terminada');
}

function mensaje(v_bool, pnlOperacion, pnlMsg) {
	var debugging = isDebugging();
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
	if (debugging) console.log('---> v_bool:' + v_bool);
	if (debugging) console.log('function mensaje DONE\n------------------------------');
}

function buscarObras(v_bool, pnlMsg) {
	var debugging = isDebugging();
	
	if (debugging) console.log('function buscarObras\n------------------------------');
	if (debugging) console.log(v_bool);
	if (debugging) console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	if (debugging) console.log('function buscarObras DONE\n------------------------------');
}

function buscarProductos(v_bool, pnlMsg) {
	var debugging = isDebugging();
	
	if (debugging) console.log('function buscarObras\n------------------------------');
	if (debugging) console.log(v_bool);
	if (debugging) console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	if (debugging) console.log('function buscarObras DONE\n------------------------------');
}

function puedeBuscarTraspaso(puedeBuscar, pnlBusquedaTraspasos, pnlAviso, validador) {
	var debugging = isDebugging();
	
	if (campos_requeridos(validador))
		return;
	if (puedeBuscar==true) {
		if (debugging) console.log('puedeBuscarTraspaso = true');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlBusquedaTraspasos);
	} else {
		if (debugging) console.log('puedeBuscarTraspaso = false');
		//seleccione almacen
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAviso);
	}
}

function validaBusquedaTraspaso(puedeBuscar, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------');
	if (debugging) console.log('validaBusquedaTraspaso(puedeBuscar, pnlOperacion, pnlMensajes, validador)');
	if (debugging) console.log('  puedeBuscar : ' + puedeBuscar);
	if (debugging) console.log(' pnlOperacion : ' + pnlOperacion);
	if (debugging) console.log('  pnlMensajes : ' + pnlMensajes);
	if (debugging) console.log('    validador : ', validador);

	console.log('validando requeridos ... ');
	if (campos_requeridos(validador)) {
		if (debugging) console.log('function validaBusquedaTraspaso VALIDADOR\n------------------------------');
		return;
	}
	
	if (puedeBuscar == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	else
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('function validaBusquedaTraspaso DONE\n------------------------------');
}

function validaBusqueda(hasError, pnlOperacion, pnlMensajes, validador) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------');
	if (debugging) console.log('validaBusqueda(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	if (debugging) console.log('    hasError     : ' + hasError);
	if (debugging) console.log('    pnlOperacion : ' + pnlOperacion);
	if (debugging) console.log('    pnlMensajes  : ' + pnlMensajes);
	if (debugging) console.log('    validador    : ', validador);

	if (debugging) console.log('validando requeridos ... ');
	if (campos_requeridos(validador)) {
		console.log('\nREQUERIDOS\n------------------------------');
		return;
	}
	
	if (hasError == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('\nERROR\n------------------------------');
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('\nOK\n------------------------------');
}

function validaCambioAlmacen(esTraspaso, pnlAvisoCambio, idAlmacenOrigen, numeroItems) {
	var debugging = isDebugging();
	
	if (esTraspaso == false) 
		return;
	almacenAnterior = idAlmacenOrigen;
	if (debugging) console.log('numeroItems: '+numeroItems + ', idAlmacenOrigen: '+idAlmacenOrigen );
	if(numeroItems > 0)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCambio);	//Al cambiar el almacen de salida, informar que la lista se borrará
}

function regresarAlmacenAnterior(idAlmacen, cboAlmacen){
	$(cboAlmacen).prop('value',idAlmacen);
}

function validaCantidadProducto(cantidadSolicitada, cantidadRecibida, cantidadEntregada, pnlAvisoCantidad) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------');
	if (debugging) console.log('validaCantidadProducto(cantidadSolicitada, cantidadRecibida, cantidadEntregada, pnlAvisoCantidad)');
	if (debugging) console.log('    cantidadSolicitada  : ' + cantidadSolicitada);
	if (debugging) console.log('    cantidadRecibida    : ' + cantidadRecibida);
	if (debugging) console.log('    cantidadEntregada   : ' + cantidadEntregada);
	if (debugging) console.log('    pnlAvisoCantidad    : ' + pnlAvisoCantidad);
	if (debugging) console.log('---------------------------------------------');
	if (debugging) console.log('  cantidadRecibida > cantidadSolicitada  : ' + (cantidadRecibida > cantidadSolicitada));
	if (debugging) console.log('  cantidadRecibida > cantidadEntregada : ' + (cantidadEntregada > 0 && cantidadRecibida > cantidadEntregada));
	
	if (cantidadRecibida > cantidadSolicitada) // Dejo en blanco el campo
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCantidad);
	if (cantidadEntregada > 0 && cantidadRecibida > cantidadEntregada)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCantidad);
	if (debugging) console.log('Ya se ha entregado (' + cantidadEntregada + ') del producto');
	if (debugging) console.log('------------------------------');
}

function soloEnteros() {
	var debugging = isDebugging();
	
	if (debugging) console.log("Tecla: " + key);
	
	if ( key == 8 )	//Aqui se pueden anexar mas caracteres Ascii permitidos
		return true;
	if( key >= 48 && key <= 57 )	//Números del 0 al 9
		return true;
	return false;
}

function soloDecimal(key) {
	var debugging = isDebugging();
	
	if (debugging) console.log("Tecla: "+key);
	
	if( key >= 48 && key <= 57 ) {	//Números del 0 al 9
		return true;
	} else {
		if ( key == 8 || key == 46 )	// 8: backspace, 46: punto
			return true;
	}
	
	return false;
}

function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------');
	if (debugging) console.log('seleccionar(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('      pnlOperacion : ' + pnlOperacion);
	if (debugging) console.log('       pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('function seleccionar ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (debugging) console.log('function seleccionar DONE\n------------------------------');
}

function quitar(operacionCancelada, pnlMensajes) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------');
	if (debugging) console.log('quitar(operacionCancelada, pnlMensajes)');
	if (debugging) console.log('    operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('    pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('quitar :: ERROR');
		return;
	} 
	
	if (debugging) console.log('DONE\n------------------------------');
}

function validaNuevo(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------');
	if (debugging) console.log('validaNuevo(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('function seleccionar ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('function seleccionar DONE\n------------------------------');
}

function delayAutoClick(btnId) {
	var debugging = isDebugging();
	
	btnId = btnId || '';
	if (btnId === '')
		return;
	
	setTimeout(function(){
		if (debugging) console.log(btnId);
		btnId = $(document.getElementById(btnId));
		btnId.click();
	}, 300);
}
