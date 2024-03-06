
function busquedaDeCotizacionesByObra(v_bool, pnlOperacion, pnlMsg) {
	console.log('function busquedaDeCotizacionesByObra\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
	else 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	
	console.log('function busquedaDeCotizacionesByObra DONE\n------------------------------');
}

function buscarRequisiciones(operacionCancelada, pnlMensaje) {
	console.log('------------------------------\nfunction buscarRequisiciones START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	console.log('function buscarRequisiciones DONE\n------------------------------');
}

function buscarObras(v_bool, pnlMsg) {
	console.log('function buscarObras\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarObras DONE\n------------------------------');
}

function buscarEmpleados(v_bool, pnlMsg) {
	console.log('function buscarEmpleados\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarEmpleados DONE\n------------------------------');
}

function buscarPersonas(operacionCancelada, pnlMensaje) {
	console.log('------------------------------\nfunction buscarPersonas START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	console.log('function buscarPersonas DONE\n------------------------------');
}

function agregarPersona(operacionCancelada, pnlOperacion, pnlMensaje) {
	console.log('------------------------------\nfunction agregarPersona START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	else 
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('function agregarPersona DONE\n------------------------------');
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

function autorizar(v_bool, pnlOperacion, pnlMsg) {
	console.log('function autorizar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	console.log('function autorizar DONE\n------------------------------');
}

function evaluaNuevo(operacionCancelada, esAdministrativo, pnlOrigen, pnlOperacion, pnlMensaje) {
	console.log('------------------------------\nfunction evaluaNuevo START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('   esAdministrativo : ' + esAdministrativo);
	console.log('          pnlOrigen : ' + pnlOrigen);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlMensaje);
	} else { 
		if (esAdministrativo == true) {
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOrigen);
		} else {
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
		}
	}
	
	console.log('function evaluaNuevo DONE\n------------------------------');
}

function nuevoFromRequisicion(operacionCancelada, pnlBusqueda, pnlOperacion, pnlMensaje) {
	console.log('------------------------------\nfunction nuevoFromRequisicion START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('        pnlBusqueda : ' + pnlBusqueda);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		return;
	}

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlBusqueda);
	/*RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);*/
	console.log('function nuevoFromRequisicion DONE\n------------------------------');
}

function conversion(operacionCancelada, pnlMensajes) {
	console.log('------------------------------\nfunction conversion(operacionCancelada, pnlMensajes) START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log(' pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function conversion DONE\n------------------------------');
}

