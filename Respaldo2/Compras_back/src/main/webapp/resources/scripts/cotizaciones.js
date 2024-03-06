function evaluaNuevaCotizacion(operacionCancelada, esAdministrativo, pnlOrigen, pnlOperacion, pnlMensaje) {
	console.log('------------------------------\nfunction evaluaNuevaCotizacion START');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('   esAdministrativo : ' + esAdministrativo);
	console.log('          pnlOrigen : ' + pnlOrigen);
	console.log('       pnlOperacion : ' + pnlOperacion);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlMensaje);
	} else { 
		if (esAdministrativo == true) {
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
		} else {
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOrigen);
		}
	}
	
	console.log('function evaluaNuevaCotizacion DONE\n------------------------------');
}

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

function buscarObras(v_bool, pnlMsg) {
	console.log('function buscarObras\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarObras DONE\n------------------------------');
}

function buscarRequisiciones(v_bool, pnlMsg) {
	console.log('function buscarRequisiciones\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarRequisiciones DONE\n------------------------------');
}

function buscarPersonas(v_bool, pnlMsg) {
	console.log('function buscarPersonas\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	
	console.log('function buscarPersonas DONE\n------------------------------');
}

function agregarPersona(v_bool, pnlOperacion, pnlMsg) {
	console.log('function agregarPersona\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
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

function nuevoEnvioCorreo(v_bool, pnlOperacion, pnlMsg) {
	console.log('function nuevoEnvioCorreo(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('function nuevoEnvioCorreo DONE\n------------------------------');
}

function enviarCorreo(v_bool, pnlOperacion, pnlMsg, listErrores) {
	console.log('function enviarCorreo(v_bool, pnlOperacion, pnlMsg, listErrores)\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> exit on campos_requeridos');
		return;
	}

	if (v_bool == false)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('exit function enviarCorreo\n------------------------------');
}

function nuevoFromRequisicion(v_bool, pnlBusqueda, pnlOperacion, pnlMensaje) {
	console.log('function nuevoFromRequisicion(v_bool, pnlBusqueda, pnlOperacion, pnlMensaje)\n------------------------------');
	console.log(v_bool);
	console.log(pnlBusqueda);
	console.log(pnlOperacion);
	console.log(pnlMensaje);
	
	if (v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		return;
	}

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlBusqueda);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('end function nuevoFromRequisicion DONE\n------------------------------');
}

function copiarCotizacion(v_bool, pnlOperacion, pnlMsg) {
	console.log('function copiarCotizacion(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('function copiarCotizacion DONE\n------------------------------');
}

function verificaAlmacenesObra(v_bool, pnlOperacion, pnlMsg) {
	console.log('function verificaAlmacenesObra(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('function verificaAlmacenesObra DONE\n------------------------------');
}