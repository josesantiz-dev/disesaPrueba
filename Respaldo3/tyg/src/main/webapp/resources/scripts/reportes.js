
function busquedaDinamica(pnl) {
	RichFaces.ui.PopupPanel.showPopupPanel(pnl);
}

function reporte(operacionCancelada, pnlMensaje) {
	console.log('------------------------------\nfunction reporte(operacionCancelada, pnlMensaje)\n');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensaje);
	else
		javascript:window.open('./reportes/ReporteGeneric.faces', 'Reporte', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	console.log('end function: DONE\n------------------------------');
}
