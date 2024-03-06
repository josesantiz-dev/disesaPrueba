
function isDebugging() {
	var param = '';
	
	param = document.location.search || '';
	param = param.replace('?','');
	if (param == '')
		return false;
	
	param = param.toUpperCase();
	param = param.split('&');
	if (param.length <= 0)
		return false;
	
	return (param[0] == 'DEBUG');
}

function salvarNomina(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, pnlConfirma, validador) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function salvarNomina(operacionCancelada, mensaje, pnlOperacion, pnlMensajes, pnlConfirma, validador)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  tipoMensaje        : ' + tipoMensaje);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ' + pnlMensajes);
	if (debugging) console.log('  pnlConfirma        : ' + pnlConfirma);
	if (debugging) console.log('  validador          : ', validador);
	
	if (campos_requeridos(validador)) {
		console.log('salvarNomina(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, pnlConfirma, validador)::REQUERIDOS');
		if (debugging) console.log('------------------------------');
		return;
	}
	
	if (operacionCancelada == true) {
		if (tipoMensaje == 6) {
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirma);
			if (debugging) console.log('salvarNomina(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, pnlConfirma, validador)::CONFIRMAR\n---------------------------------');
			return;
		} 
		
		if (tipoMensaje == 10) {
			if (debugging) console.log('salvarNomina(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, pnlConfirma, validador)::OK-NOTIFY\n---------------------------------');
			return;
		} 
		
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('salvarNomina(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, pnlConfirma, validador)::ERROR', operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, pnlConfirma, validador);
		if (debugging) console.log('------------------------------');
		return;
	} 
	
	//RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('salvarNomina(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, pnlConfirma, validador)::DONE\n------------------------------');
}

function procesoNomina(operacionCancelada, tipoMensaje, procesando, pnlMensajes) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function procesoNomina(operacionCancelada, tipoMensaje, procesando, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  tipoMensaje        : ' + tipoMensaje);
	if (debugging) console.log('  procesando         : ' + procesando);
	if (debugging) console.log('  pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		if (debugging) console.log('... validando tipoMensaje');
		if (tipoMensaje != 0 && tipoMensaje != 10) {
			if (debugging) console.log('... mostrando Mensaje ERROR');
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
			console.log('procesoNomina(operacionCancelada, tipoMensaje, procesando, pnlMensajes)::ERROR', operacionCancelada, tipoMensaje, procesando, pnlMensajes);
			if (debugging) console.log('------------------------------');
			return;
		} 
	}
	
	if (procesando == false) {
		javascript:window.open('./reportes/ReporteGeneric.faces', 'Nomina', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
		if (debugging) console.log('procesoNomina()::DONE\n---------------------------------');
		return;
	};
	
	if (debugging) console.log('do nothing!\n---------------------------------');
}

function reporte(operacionCancelada, pnlMensaje) {
	var debugging = isDebugging();
	
	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function reporte(operacionCancelada, pnlMensaje)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  pnlMensaje         : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('reporte(operacionCancelada, pnlMensaje)::ERROR', operacionCancelada, pnlMensaje);
		if (debugging) console.log('------------------------------');
		return;
	}

	javascript:window.open('./reportes/ReporteGeneric.faces', 'Nomina', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	if (debugging) console.log('reporte()::DONE\n------------------------------');
}

function cargaNominaQuincenal(operacionCancelada, tipoMensaje, pnlOperacion, pnlEmpleados, pnlMensaje, validador) {
	var debugging = isDebugging();

	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function cargaNominaQuincenal(operacionCancelada, pnlOperacion, pnlEmpleados, pnlMensaje, validador)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlEmpleados       : ' + pnlEmpleados);
	if (debugging) console.log('  pnlMensaje         : ' + pnlMensaje);
	if (debugging) console.log('  validador          : ', validador);
	
	if (campos_requeridos(validador)) {
		console.log('cargaNominaQuincenal::REQUERIDOS\n------------------------------');
		return;
	}

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (operacionCancelada == true) {
		console.log('cargaNominaQuincenal::ERROR\n------------------------------');
		if (tipoMensaje == -2) 
			RichFaces.ui.PopupPanel.showPopupPanel(pnlEmpleados);
		else
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	if (debugging) console.log('DONE\n------------------------------');
	javascript:window.open('./reportes/ReporteGeneric.faces', 'Nomina', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	/*if (operacionCancelada == false)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);*/
}
