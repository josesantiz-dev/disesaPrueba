function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion,msj) {
	console.log("msgRevisando");
	console.log("------------------------------------");
	console.log(listErrores);
	console.log("pnl1: " + pnl1);
	console.log("pnl2: " + pnl2);
	console.log("v_bool: " + v_bool);
	console.log("invocacion: " + invocacion);
	console.log("msj: " + msj);
	
	if(listErrores.hasChildNodes()) {
		if (invocacion != 'agregarObra')
			return;	
	}

	if(invocacion == 'guardar'){
		if(v_bool == true) {
			if((msj.indexOf('SE CANCELA EL FOLIO ')!= -1)  || (msj.indexOf('SE CANCELAN LOS FOLIOS ')!= -1 )){
				Richfaces.showModalPanel(pnl1);
			} else {
				Richfaces.showModalPanel(pnl2);
			}
		} else {
			if(msj =='BIEN'){
				Richfaces.showModalPanel(pnl2);
				javascript:window.open('/reportes/ReporteGeneric.faces', 'Caja Chica', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			} else {
				Richfaces.showModalPanel(pnl2);
			}	
		}
	}

	if(invocacion == 'editar'){
		if(v_bool == true){
			Richfaces.showModalPanel(pnl1);
		} else {
			Richfaces.showModalPanel(pnl2);
		}		
	}

	if(invocacion == 'buscar'){
		if(v_bool == true){
			Richfaces.showModalPanel(pnl2);
		}		 	
	}

	if(invocacion == 'guardarFactura'){
		Richfaces.hideModalPanel(pnl1); 
		Richfaces.showModalPanel(pnl2);		 
	}

	if(invocacion == 'buscarObras'){
		Richfaces.showModalPanel(pnl1); 
		return;
	}

	if(invocacion == 'agregarObra'){
		Richfaces.showModalPanel(pnl2); 
		return;
	}

	if(invocacion == 'agregar'){
		if(v_bool == true){
			Richfaces.showModalPanel(pnl1);
		} else {
			Richfaces.showModalPanel(pnl2);
		}
	}

	if(invocacion == 'eliminar'){
		if(v_bool == true){
			Richfaces.showModalPanel(pnl1);
		} else {
			Richfaces.showModalPanel(pnl2);
		}		
	}	

	if(invocacion == 'boton'){
		if(v_bool == true){
			Richfaces.hideModalPanel(pnl2);
		} else {
			if(msj =='BIEN' || msj =='OK'){
				Richfaces.hideModalPanel('pnlNuevoEditarCajaChica');
			}
			
			Richfaces.hideModalPanel(pnl1);
			Richfaces.hideModalPanel(pnl2);
		}
	}

	if(invocacion == 'boton2'){			
		Richfaces.hideModalPanel(pnl1);
		Richfaces.showModalPanel(pnl2);
		javascript:window.open('/reportes/ReporteGeneric.faces', 'Caja Chica', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	}
} 

function checaCancelacion(msg, pnlMsg, pnlError){
	console.log("checaCancelacion");
	console.log("------------------------------------");
	console.log("msg: " + msg);
	console.log("pnlMsg: " + pnlMsg);
	console.log("pnlError: " + pnlError);

	if(msg == "")
		Richfaces.hideModalPanel(pnlMsg);
	else
		Richfaces.showModalPanel(pnlError);
}

function cancelar(procesoCancelado, pnlOperacion, pnlMensaje) {
	console.log('------------------------------');
	console.log('function cancelar(procesoCancelado, pnlOperacion, pnlMensaje)');
	console.log('    procesoCancelado : ' + procesoCancelado);
	console.log('    pnlOperacion     : ' + pnlOperacion);
	console.log('    pnlMensaje       : ' + pnlMensaje);

	Richfaces.hideModalPanel(pnlOperacion);
	if (procesoCancelado == true) {
		Richfaces.showModalPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
		return;
	}

	console.log('OK\n------------------------------');
}

function detallesCajaChica(url, msg,pnl){
	console.log("detallesCajaChica");
	console.log("------------------------------------");
	console.log("url: " + url);
	console.log("msg: " + msg);
	console.log("pnl: " + pnl);
	
	if(msg == "")
		Richfaces.showModalPanel(pnl);
	else
		window.open(url);
}

function agregaRet(pnl1,pnl2,msj){
	console.log("agregaRet");
	console.log("------------------------------------");
	console.log("pnl1: " + pnl1);
	console.log("pnl2: " + pnl2);
	console.log("msj: " + msj);
	
	if(msj != 0){
		Richfaces.showModalPanel(pnl2);
	} else {
		Richfaces.hideModalPanel(pnl1);
	}
}

function imprimeRep(pnlRep){
	console.log("imprimeRep");
	console.log("------------------------------------");
	console.log("pnlRep: " + pnlRep);
	
	javascript:window.open('/reportes/ReporteGeneric.faces', 'Caja Chica', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	Richfaces.hideModalPanel(pnlRep);
}

function reporte(urlapp, resultadoOperacion, pnlMensaje) {
	console.log('function reporte(urlapp, resultadoOperacion, pnlMensaje)\n------------------------------');
	console.log('             urlapp : ' + urlapp);
	console.log(' resultadoOperacion : ' + resultadoOperacion);
	console.log('         pnlMensaje : ' + pnlMensaje);
	
	if (resultadoOperacion == true) {
		console.log('Mostramos mensaje');
		Richfaces.showModalPanel(pnlMensaje);
		console.log('fin reporte mensaje \n------------------------------');
		return;
	}

	console.log('Mostramos reporte');
	var ventana = window.open(urlapp + '/reportes/ReporteGeneric.faces', 'Documentos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	ventana.focus();
	console.log('fin reporte ventana\n------------------------------');
}

function buscarProyectos(valor, pnlMsg){
	console.log("buscarProyectos");
	console.log("------------------------------------");
	console.log("valor: " + valor);
	console.log("pnlMsg: " + pnlMsg);
		
	Richfaces.showModalPanel(pnlMsg);
}

function analizarArchivo(v_bool, pnlUpload, pnlMsg) {
	console.log('------------------------------');
	console.log('function analizarArchivo(v_bool, pnlUpload, pnlMsg)');
	console.log('    v_bool : ' + v_bool);
	console.log(' pnlUpload : ' + pnlUpload);
	console.log('    pnlMsg : ' + pnlMsg);
	
	if (v_bool == true)
		Richfaces.showModalPanel(pnlMsg);
	Richfaces.hideModalPanel(pnlUpload);
	console.log('function analizarArchivo DONE\n------------------------------');
}

function descargar(v_bool, modulo, pnlMensajes) {
	console.log('\n------------------------------');
	console.log('function descargar(v_bool, modulo, pnlMensajes)');
	console.log('      v_bool : ' + v_bool);
	console.log('      modulo : ' + modulo);
	console.log(' pnlMensajes : ' + pnlMensajes);
	
	if (v_bool == true) {
		Richfaces.showModalPanel(pnlMensajes);
		console.log('\nERROR\n------------------------------');
		return;
	}
	
	window.open(modulo + '/reportes/ReporteGeneric.faces', 'Satic', 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
	console.log('\nDONE\n------------------------------');
}

function editar(v_bool, pnlMensaje, pnlOperacion) {
	console.log('function editar(' + v_bool + ', ' + pnlMensaje + ', ' + pnlOperacion + ')');
	
	if (v_bool == true) {
		console.log('Show modal pnlMensaje [' + pnlMensaje + ']');
		RichFaces.showModalPanel(pnlMensaje);
	} else {
		console.log('Show modal pnlOperacion [' + pnlOperacion + ']');
		RichFaces.showModalPanel(pnlOperacion);
	}
}

function nuevaBusqueda(operacionCancelada, pnlMensaje, pnlOperacion) {
	console.log("------------------------------------\nnuevaBusqueda(operacionCancelada, pnlMensaje, pnlOperacion)\n------------------------------------");
	console.log("operacionCancelada : " + operacionCancelada);
	console.log("pnlMensaje         : " + pnlMensaje);
	console.log("pnlOperacion       : " + pnlOperacion);

	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensaje);
		return;
	}
	
	Richfaces.showModalPanel(pnlOperacion);
	console.log("end of nuevaBusqueda(operacionCancelada, pnlMensaje, pnlOperacion)\n------------------------------------");
}

function buscar(v_bool, pnlMsg) {
	console.log("buscar(v_bool, pnlMsg)\n------------------------------------");
	console.log("v_bool: " + v_bool);
	console.log("pnlMsg: " + pnlMsg);

	if (v_bool == true)
		Richfaces.showModalPanel(pnlMsg);
	console.log("end of buscar(v_bool, pnlMsg)\n------------------------------------");
}

function salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log("------------------------------------");
	console.log("salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador)");
	console.log("    operacionCancelada : " + operacionCancelada);
	console.log("    pnlOperacion       : " + pnlOperacion);
	console.log("    pnlMensajes        : " + pnlMensajes);
	console.log("    validador          : ");
	console.log(validador);
	
	if (validador.hasChildNodes()) {
		console.log("REQUERIDOS\n------------------------------------");
		return;
	}

	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensajes);
		console.log("ERROR\n------------------------------------");
		return;
	} 
	
	Richfaces.hideModalPanel(pnlOperacion);
	console.log("OK\n------------------------------------");
}

function seleccionar(operacionCancelada, pnlMensaje, pnlOperacion) {
	console.log("------------------------------------\nseleccionar(operacionCancelada, pnlMensaje, pnlOperacion)\n------------------------------------");
	console.log("operacionCancelada : " + operacionCancelada);
	console.log("pnlMensaje         : " + pnlMensaje);
	console.log("pnlOperacion       : " + pnlOperacion);

	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensaje);
	else
		Richfaces.hideModalPanel(pnlOperacion);
	console.log("end of seleccionar(operacionCancelada, pnlMensaje, pnlOperacion)\n------------------------------------");
}

function buscarPersonaNegocio(v_bool, pnlMsg) {
	console.log("buscarPersonaNegocio(v_bool, pnlMsg)\n------------------------------------");
	console.log("v_bool: " + v_bool);
	console.log("pnlMsg: " + pnlMsg);

	if (v_bool == true)
		Richfaces.showModalPanel(pnlMsg);
	console.log("end of buscarPersonaNegocio(v_bool, pnlMsg)\n------------------------------------");
}

function seleccionarPersonaNegocio(v_bool, pnlMsg, pnlOperacion) {
	console.log("seleccionarPersonaNegocio(v_bool, pnlMsg, pnlOperacion)\n------------------------------------");
	console.log("      v_bool: " + v_bool);
	console.log("      pnlMsg: " + pnlMsg);
	console.log("pnlOperacion: " + pnlOperacion);

	if (v_bool == true)
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOperacion);
	console.log("end of seleccionarPersonaNegocio(v_bool, pnlMsg, pnlOperacion)\n------------------------------------");
}

function descartarXML(v_bool, pnlMsg, pnlOperacion)  {
	console.log("------------------------------------");
	console.log("descartarXML(v_bool, pnlMsg, pnlOperacion)\n------------------------------------");
	console.log("      v_bool: " + v_bool);
	console.log("      pnlMsg: " + pnlMsg);
	console.log("pnlOperacion: " + pnlOperacion);

	if (v_bool == false) {
		Richfaces.hideModalPanel(pnlOperacion);
		console.log("DONE descartarXML\n------------------------------------");
		return;
	}

	Richfaces.showModalPanel(pnlMsg);
	console.log("DONE descartarXML\n------------------------------------");
}

function borrarXML(v_bool, pnlMsg, pnlOperacion) {
	console.log("------------------------------------");
	console.log("borrarXML(v_bool, pnlMsg, pnlOperacion)\n------------------------------------");
	console.log("      v_bool: " + v_bool);
	console.log("      pnlMsg: " + pnlMsg);
	console.log("pnlOperacion: " + pnlOperacion);

	if (v_bool == true) {
		Richfaces.showModalPanel(pnlMsg);
		console.log("ERROR borrarXML\n------------------------------------");
	} else {
		Richfaces.hideModalPanel(pnlOperacion);
		console.log("DONE borrarXML\n------------------------------------");
	}
}