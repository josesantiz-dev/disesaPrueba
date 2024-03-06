
function nuevaCotizacion3(operacionCancelada, origenValor, pnlOrigen, pnlOperacion, pnlBusqueda, pnlMensaje) {
	var debugging = isDebugging();

	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function nuevaCotizacion3(operacionCancelada, origenValor, pnlOrigen, pnlOperacion, pnlBusqueda, pnlMensaje)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  origenValor        : ' + origenValor);
	if (debugging) console.log('  pnlOrigen          : ' + pnlOrigen);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlBusqueda        : ' + pnlBusqueda);
	if (debugging) console.log('  pnlMensaje         : ' + pnlMensaje);

	if (debugging) console.log('  Validando Error ... ');
	if (operacionCancelada == true) {
		if (debugging) console.log('  Mostrando          : ' + pnlMensaje);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
		return;
	} 

	if (debugging) console.log('  Validando origen ... ');
	if (origenValor == '' || origenValor == '?' || origenValor == undefined) {
		if (debugging) console.log('  Mostrando          : ' + pnlOrigen);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOrigen);
		console.log('DONE\n------------------------------');
		return;
	} 

	if (debugging) console.log('  Terminando ... ');
	if (origenValor == 'R') {
		if (debugging) console.log('  Mostrando          : ' + pnlBusqueda);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlBusqueda);
		if (debugging) console.log('DONE\n------------------------------');
		return;
	}
	
	if (debugging) console.log('  Mostrando          : ' + pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('DONE\n------------------------------');
}

function nuevaCotizacion2(operacionCancelada, pnlOperacion, pnlMensaje) {
	var debugging = isDebugging();

	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function nuevaCotizacion2(operacionCancelada, pnlOperacion, pnlMensaje)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlMensaje         : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		if (debugging) console.log('  Mostrando          : ' + pnlMensaje);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
		return;
	} 

	if (debugging) console.log('  Mostrando          : ' + pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (debugging) console.log('DONE\n------------------------------');
}

function nuevaCotizacion(operacionCancelada, tipoCotizacion, pnlOrigen, pnlOperacion, pnlMensaje) {
	var debugging = isDebugging();

	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function nuevaCotizacion(operacionCancelada, tipoCotizacion, pnlOrigen, pnlOperacion, pnlMensaje)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  tipoCotizacion     : ' + tipoCotizacion);
	if (debugging) console.log('  pnlOrigen          : ' + pnlOrigen);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlMensaje         : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		if (debugging) console.log('  Mostrando          : ' + pnlMensaje);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
		return;
	} 

	if (tipoCotizacion == 0 || tipoCotizacion == 2) {
		if (debugging) console.log('  Mostrando          : ' + pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	} else {
		if (debugging) console.log('  Mostrando          : ' + pnlOrigen);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOrigen);
	}
	
	if (debugging) console.log('DONE\n------------------------------');
}

function evaluaNuevaCotizacion(operacionCancelada, esAdministrativo, pnlOrigen, pnlOperacion, pnlBusqueda, pnlMensaje) {
	var debugging = isDebugging();

	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function evaluaNuevaCotizacion(operacionCancelada, esAdministrativo, pnlOrigen, pnlOperacion, pnlBusqueda, pnlMensaje)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  esAdministrativo   : ' + esAdministrativo);
	if (debugging) console.log('  pnlOrigen          : ' + pnlOrigen);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlBusqueda        : ' + pnlBusqueda);
	if (debugging) console.log('  pnlMensaje         : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		if (debugging) console.log('  Mostrando          : ' + pnlMensaje);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
	} else { 
		if (esAdministrativo == true) {
			/*if (debugging) console.log('  Mostrando          : ' + pnlOperacion);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);*/
			if (debugging) console.log('  Mostrando          : ' + pnlBusqueda);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlBusqueda);
		} else {
			if (debugging) console.log('  Mostrando          : ' + pnlOrigen);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOrigen);
		}
	}
	
	if (debugging) console.log('DONE\n------------------------------');
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

function nuevoFromRequisicion(v_bool, tipoMensaje, pnlBusqueda, pnlOperacion, pnlMensaje, pnlConfirmarSBO) {
	var debugging = isDebugging();
	var itemPopUp = null;

	if (debugging) console.log('----------------------------------------------------------------------------');
	if (debugging) console.log('function nuevoFromRequisicion(v_bool, tipoMensaje, pnlBusqueda, pnlOperacion, pnlMensaje, pnlConfirmarSBO)');
	if (debugging) console.log('  operacionCancelada : ' + v_bool);
	if (debugging) console.log('  tipoMensaje        : ' + tipoMensaje);
	if (debugging) console.log('  pnlBusqueda        : ' + pnlBusqueda);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlMensaje         : ' + pnlMensaje);
	if (debugging) console.log('  pnlConfirmarSBO    : ' + pnlConfirmarSBO);
	
	if (v_bool == true) {
		if (tipoMensaje == 100) {
			console.log('ERROR 100 - Material en SBO\n------------------------------');
			if (debugging) console.log('\n\nDEBUG LOG --->');
			if (debugging) console.log(' ---> Ocultando : ' + pnlBusqueda);
			RichFaces.ui.PopupPanel.hidePopupPanel(pnlBusqueda);
			if (debugging) console.log(' ---> Mostrando : ' + pnlConfirmarSBO);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmarSBO);
			return;
		}

		if (tipoMensaje == 101) {
			console.log('ERROR 101 - Obra sin Bodega\n------------------------------');
			if (debugging) console.log('\n\nDEBUG LOG --->');
			if (debugging) console.log(' ---> Ocultando : ' + pnlBusqueda);
			RichFaces.ui.PopupPanel.hidePopupPanel(pnlBusqueda);
			/*if (debugging) console.log(' ---> Mostrando : ' + pnlOperacion);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);*/
			if (debugging) console.log(' ---> Mostrando : ' + pnlMensaje);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
			return;
		}

		if (debugging) console.log('\n\nDEBUG LOG --->');
		if (debugging) console.log(' ---> Mostrando : ' + pnlMensaje);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
		return;
	}

	if (debugging) console.log('\n\nDEBUG LOG --->');
	if (debugging) console.log(' ---> ValidateToHide : ' + pnlBusqueda);
	itemPopUp = $(document.getElementById(pnlBusqueda));
	if (debugging) console.log(' ---> itemPopUp : ', itemPopUp);
	if (itemPopUp !== undefined && itemPopUp.size() > 0 && itemPopUp.prop('style').display === 'block') {
		if (debugging) console.log(' ---> Ocultando : ' + pnlBusqueda);
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlBusqueda);
	}

	if (debugging) console.log(' ---> ValidateToShow : ' + pnlOperacion);
	itemPopUp = $(document.getElementById(pnlOperacion));
	if (debugging) console.log(' ---> itemPopUp : ', itemPopUp);
	if (debugging) console.log(' ---> condision : ', (itemPopUp !== undefined && itemPopUp.size() > 0 && itemPopUp.prop('style').display === 'none'));
	if (debugging) console.log(' ---> undefined : ', itemPopUp !== undefined);
	if (debugging) console.log(' ---> size : ', itemPopUp.size() > 0);
	if (debugging) console.log(' ---> display : ', itemPopUp.prop('style').display);
	if (itemPopUp !== undefined && itemPopUp.size() > 0 && (itemPopUp.prop('style').display === 'none' || itemPopUp.prop('style').display === '')) {
		if (debugging) console.log(' ---> Mostrando : ' + pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	}
	console.log('DONE\n------------------------------');
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

function lanzarSolicitud(operacionCancelada, pnlOperacion, pnlMensajes) {
	var debugging = isDebugging();

	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function lanzarSolicitud(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		if (debugging) console.log('  Mostrando          : ' + pnlMensajes);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (debugging) console.log('DONE\n------------------------------');
}

function comprobarOperacion(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, pnlConfirmarSBO) {
	var debugging = isDebugging();

	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function comprobarOperacion(operacionCancelada, pnlOperacion, pnlMensajes)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  tipoMensaje        : ' + tipoMensaje);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ' + pnlMensajes);
	if (debugging) console.log('  pnlConfirmarSBO    : ' + pnlConfirmarSBO);
	
	if (operacionCancelada == true) {
		if (tipoMensaje == 100) {
			if (debugging) console.log(' ---> 100 - Material en SBO ');
			if (debugging) console.log(' ---> Mostrando : ' + pnlConfirmarSBO);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmarSBO);
			return;
		}

		if (tipoMensaje == 101) {
			if (debugging) console.log(' ---> 101 - Obra sin Bodega ');
			if (debugging) console.log(' ---> Ocultando : ' + pnlBusqueda);
			RichFaces.ui.PopupPanel.hidePopupPanel(pnlBusqueda);
			if (debugging) console.log(' ---> Mostrando : ' + pnlOperacion);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
			if (debugging) console.log(' ---> Mostrando : ' + pnlMensaje);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
			return;
		}
		
		if (debugging) console.log('  Mostrando          : ' + pnlMensajes);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (tipoMensaje == -1)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('DONE\n------------------------------');
}

function actualizarExistencia(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, pnlConfirmarSBO) {
	var debugging = isDebugging();

	if (debugging) console.log('------------------------------');
	if (debugging) console.log('function actualizarExistencia(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, pnlConfirmarSBO)');
	if (debugging) console.log('  operacionCancelada : ' + operacionCancelada);
	if (debugging) console.log('  tipoMensaje        : ' + tipoMensaje);
	if (debugging) console.log('  pnlOperacion       : ' + pnlOperacion);
	if (debugging) console.log('  pnlMensajes        : ' + pnlMensajes);
	if (debugging) console.log('  pnlConfirmarSBO    : ' + pnlConfirmarSBO);
	
	if (operacionCancelada == true) {
		if (tipoMensaje == 100) {
			if (debugging) console.log(' ---> 100 - Material en SBO ');
			if (debugging) console.log(' ---> Mostrando : ' + pnlConfirmarSBO);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmarSBO);
			return;
		}
		
		if (debugging) console.log('  Mostrando          : ' + pnlMensajes);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	if (tipoMensaje == -1)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	if (debugging) console.log('DONE\n------------------------------');
}
