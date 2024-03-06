function busquedaPopup(origen, operacionCancelada, pnlMensajes) {
	console.log('\n------------------------------\nfunction busquedaPopup START');
	console.log('             origen : ' + origen);
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('        pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function busquedaPopup DONE\n------------------------------');
}

function buscarObras(operacionCancelada, pnlMensajes) {
	console.log('\n------------------------------\nfunction buscarObras START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('        pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function buscarObras DONE\n------------------------------');
}

function buscarProductos(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\n------------------------------\nfunction buscarProductos START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('        pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function buscarProductos DONE\n------------------------------');
}

function agregarReqDetalle(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('\n------------------------------\n');
	console.log('function agregarReqDetalle(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('        pnlMensajes : ' + pnlMensajes);
	console.log('          validador : ');
	console.log(validador);

	console.log('\nValidando requeridos ... ');
	if (campos_requeridos(validador)) {
		console.log('REQUERIDOS\n------------------------------');
		return;
	}
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	}

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('DONE\n------------------------------');
}

function eliminarDetalle(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('function eliminarDetalle\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	console.log('function eliminarDetalle DONE\n------------------------------');
}

function nuevaBusquedaProductos(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('\n------------------------------\nfunction nuevaBusquedaProductos START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('        pnlMensajes : ' + pnlMensajes);
	console.log(validador);

	console.log('\nValidando requeridos ... ');
	if(campos_requeridos(validador)) {
		console.log('---> function nuevaBusquedaProductos REQUERIDOS');
		return;
	}
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	else
		//RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
		console.log('function nuevaBusquedaProductos DONE\n------------------------------');
	RichFaces.$(pnlOperacion).switchToItem('true');
	//document.getElementById("#{rich:clientId('txtValorBusquedaProductos')}").focus();
}

function nuevoConfirmaOrigen(operacionCancelada, perfilEgresos, pnlConfirmar, pnlOperacion, pnlMensajes) {
	console.log('---------------------------------------------------------------\n');
	console.log('function nuevoConfirmaOrigen(operacionCancelada, perfilEgresos, pnlConfirmar, pnlOperacion, pnlMensajes)');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('      perfilEgresos : ' + perfilEgresos);
	console.log('       pnlConfirmar : ' + pnlConfirmar);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('        pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	} else {
		if (perfilEgresos == true)
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmar);
		else
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	}
	RichFaces.$('frmPrincipal:panelColapsedBusquedaProductos').switchToItem('false');
	console.log('function nuevoConfirmaOrigen DONE\n------------------------------');
}

function validaGuardarProducto(operacionCancelada, pnlOperacion, pnlOperacionParent, pnlMensajes, validador) {
	console.log('------------------------------\nfunction validaGuardarProducto START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log(' pnlOperacionParent : ' + pnlOperacionParent);
	console.log('        pnlMensajes : ' + pnlMensajes);
	console.log('         Requeridos : ');
	
	console.log(validador);
	if (campos_requeridos(validador)) {
		console.log('function validaGuardarProducto REQUERIDOS \n------------------------------');
		return;
	}
	
	if (operacionCancelada == false) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		//RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacionParent);
	}
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function validaGuardarProducto DONE\n------------------------------');
}

function soloDecimal(key){
	console.log("Tecla: "+key);
	
	if( key >= 48 && key <= 57 ){	//Números del 0 al 9
		return true;
	}else{
		if ( key == 8 || key == 46 ){	// 8: backspace, 46: punto
			return true;
		}
	}
	
	return false;
}

function agregarProducto(operacionCancelada, pnlMensajes) {
	console.log('\n------------------------------\n');
	console.log('function agregarProducto(operacionCancelada, pnlMensajes)');
	console.log('  operacionCancelada : ' + operacionCancelada);
	console.log('         pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true)
		console.log('ERROR\n------------------------------');
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('DONE\n------------------------------');
}

function verEditar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('---------------------------------------------------------------\n');
	console.log('function verEditar(operacionCancelada, pnlOperacion, pnlMensajes)');
	console.log('  operacionCancelada : ', operacionCancelada);
	console.log('        pnlOperacion : ', pnlOperacion);
	console.log('         pnlMensajes : ', pnlMensajes);

	console.log('---> Validating ...');
	if (operacionCancelada == true) {
		console.log('ERROR\n------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	console.log('---> popup ', document.getElementById(pnlOperacion));
	console.log('---> showing ' + pnlOperacion + " ...");
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	var panelElement = document.getElementById("#{rich:clientId('panelColapsedBusquedaProductos')}");
    if (panelElement) {
        RichFaces.$(panelElement).switchToItem('false');
    }
    console.log('DONE\n------------------------------');
}
