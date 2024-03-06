function buscarEmpleados(v_bool, pnlMsg) {
	console.log('function buscarEmpleados(v_bool, pnlMsg)\n------------------------------');
	console.log(' v_bool: ' + v_bool);
	console.log(' pnlMsg: ' + pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	
	console.log('fin function buscarEmpleados DONE\n------------------------------');
}

function seleccionarEmpleado(v_bool, pnlOperacion, pnlMensaje) {
	console.log('function seleccionarEmpleado(v_bool, pnlOperacion, pnlMensaje)\n------------------------------');
	console.log('       v_bool: ' + v_bool);
	console.log(' pnlOperacion: ' + pnlOperacion);
	console.log('   pnlMensaje: ' + pnlMensaje);
	
	if (v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		return;
	}

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	console.log('fin seleccionarEmpleado DONE\n------------------------------');
}
