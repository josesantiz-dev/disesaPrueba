function comprobarAlmacenObra(v_bool, pnlMsg) {
	console.log('function comprobarAlmacenObra\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if(v_bool == true){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
	
	console.log('function comprobarAlmacenObra DONE\n------------------------------');
}

function salvarObraAlmacen(v_bool, tipo_mensaje, pnlOperacion, pnlConfirmar, pnlMensaje, listErrores) {
	console.log('function salvar\n------------------------------');
	console.log('      v_bool: ' + v_bool);
	console.log('pnlOperacion: ' + pnlOperacion);
	console.log('pnlConfirmar: ' + pnlConfirmar);
	console.log('  pnlMensaje: ' + pnlMensaje);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> exit on campos_requeridos');
		return;
	}
	
	if (v_bool == true) {
		if (tipo_mensaje == 16)
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmar);
		else
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	}
	
	console.log('function salvar DONE\n------------------------------');
}

function habilitarAnticipoCobranza(idCobranza, valAnticipo, valEstimacion) {
	console.log('------------------------------\habilitarAnticipoCobranza(idCobranza, valAnticipo, valEstimacion)');
	console.log('    idCobranza    : ' + idCobranza);
	console.log('    valAnticipo   : ' + valAnticipo);
	console.log('    valEstimacion : ' + valEstimacion);
	
	if (idCobranza > 0) {
		return (valEstimacion == 0 && valAnticipo == 0)
	}
	
	return (valAnticipo > 0);
}

function habilitarEstimacionCobranza(idCobranza, valAnticipo, valEstimacion) {
	console.log('------------------------------\habilitarEstimacionCobranza(idCobranza, valAnticipo, valEstimacion)');
	console.log('    idCobranza    : ' + idCobranza);
	console.log('    valAnticipo   : ' + valAnticipo);
	console.log('    valEstimacion : ' + valEstimacion);
	
	if (idCobranza > 0) {
		return (valEstimacion == 0 && valAnticipo == 0)
	}
	
	return (valEstimacion > 0);
}

function eliminarDetalle(operacionCancelada, pnlOperacion, pnlMensaje) {
	console.log('-------------------------------------------------');
	console.log('function eliminarDetalle(operacionCancelada, pnlOperacion, pnlMensaje)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensaje         : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		console.log('ERROR\n------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}
