function ver(operacionCancelada, pnlOperacion, pnlMensaje) {
	console.log('------------------------------\nfunction ver(operacionCancelada, pnlOperacion, pnlMensaje)\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('      pnlOperacion : ' + pnlOperacion);
	console.log('        pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlMensaje);
	else 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('end function. DONE\n------------------------------');
}

function nuevaBusquedaCotizaciones(operacionCancelada, pnlOperacion, pnlMensaje, listErrores) {
	console.log('------------------------------\nfunction nuevaBusquedaCotizaciones(operacionCancelada, pnlOperacion, pnlMensaje, listErrores)\n');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('         pnlMensaje : ' + pnlMensaje);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> end function: REQUERIDOS');
		return;
	}

	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	else 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('end function: DONE\n------------------------------');
}

/*function reporte(operacionCancelada, pnlMensaje) {
	console.log('------------------------------\nfunction reporte(operacionCancelada, pnlMensaje)\n');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	else
		javascript:window.open('./reportes/ReporteGeneric.faces', 'Comparativa', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	console.log('end function: DONE\n------------------------------');
}*/

function salvarComparativa(operacionCancelada, pnlOperacion, pnlMensaje, listErrores) {
	console.log('------------------------------\nsalvarComparativa(operacionCancelada, pnlOperacion, pnlMensaje, listErrores)\n');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('         pnlMensaje : ' + pnlMensaje);
	console.log(listErrores);
	
	if (campos_requeridos(listErrores)) {
		console.log('end function. REQUERIDOS\n------------------------------');
		return;
	}
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('end function. ERROR\n------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	javascript:window.open('./reportes/ReporteGeneric.faces', 'Comparativa', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	console.log('end function. DONE\n------------------------------');
}
