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

function agregarReqDetalle(operacionCancelada, pnlMensajes, listErrores) {
	console.log('\n------------------------------\nfunction agregarReqDetalle START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('        pnlMensajes : ' + pnlMensajes);
	console.log(listErrores);

	console.log('\nValidando requeridos ... ');
	if(campos_requeridos(listErrores)){
		console.log('---> function agregarReqDetalle REQUERIDOS');
		return;
	}
	
	if (operacionCancelada == true) 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function nuevaBusquedaProductos DONE\n------------------------------');
}

function eliminarDetalle(v_bool, pnlOperacion, pnlMsg) {
	console.log('function eliminarDetalle\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	console.log('function eliminarDetalle DONE\n------------------------------');
}

function nuevaBusquedaProductos(operacionCancelada, pnlOperacion, pnlMensajes, listErrores) {
	console.log('\n------------------------------\nfunction nuevaBusquedaProductos START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('        pnlMensajes : ' + pnlMensajes);
	console.log(listErrores);

	console.log('\nValidando requeridos ... ');
	if(campos_requeridos(listErrores)) {
		console.log('---> function nuevaBusquedaProductos REQUERIDOS');
		return;
	}
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	else
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('function nuevaBusquedaProductos DONE\n------------------------------');
}

function nuevoConfirmaOrigen(operacionCancelada, perfilEgresos, pnlConfirmar, pnlOperacion, pnlMensaje) {
	console.log('------------------------------\nfunction nuevoConfirmaOrigen START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('      perfilEgresos : ' + perfilEgresos);
	console.log('       pnlConfirmar : ' + pnlConfirmar);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else {
		if (perfilEgresos == true)
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmar);
		else
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	}
	console.log('function nuevoConfirmaOrigen DONE\n------------------------------');
}

function validaGuardarProducto(operacionCancelada, pnlOperacion, pnlOperacionParent, pnlMensaje, listErrores) {
	console.log('------------------------------\nfunction validaGuardarProducto START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log(' pnlOperacionParent : ' + pnlOperacionParent);
	console.log('         pnlMensaje : ' + pnlMensaje);
	console.log('         Requeridos : ');
	
	console.log(listErrores);
	if (campos_requeridos(listErrores)) {
		console.log('function validaGuardarProducto REQUERIDOS \n------------------------------');
		return;
	}
	
	if (operacionCancelada == false) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacionParent);
	}
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
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
