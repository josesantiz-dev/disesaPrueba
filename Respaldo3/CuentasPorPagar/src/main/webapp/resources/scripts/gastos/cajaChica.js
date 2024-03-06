function msgRevisando(validador,pnl1,pnl2,operacionCancelada,invocacion,msj) {
	console.log("msgRevisando");
	console.log("------------------------------------");
	console.log(validador);
	console.log("pnl1: " + pnl1);
	console.log("pnl2: " + pnl2);
	console.log("operacionCancelada: " + operacionCancelada);
	console.log("invocacion: " + invocacion);
	console.log("msj: " + msj);
	
	if (validador.hasChildNodes()) {
		if (invocacion != 'agregarObra')
			return;	
	}

	if (invocacion == 'guardar') {
		if (operacionCancelada == true) {
			if ((msj.indexOf('SE CANCELA EL FOLIO ')!= -1)  || (msj.indexOf('SE CANCELAN LOS FOLIOS ')!= -1 )) {
				Richfaces.showModalPanel(pnl1);
			} else {
				Richfaces.showModalPanel(pnl2);
			}
		} else {
			if (msj =='BIEN') {
				Richfaces.showModalPanel(pnl2);
				javascript:window.open('/reportes/ReporteGeneric.faces', 'Caja Chica', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			} else {
				Richfaces.showModalPanel(pnl2);
			}	
		}
	}

	if (invocacion == 'editar') {
		if (operacionCancelada == true) {
			Richfaces.showModalPanel(pnl1);
		} else {
			Richfaces.showModalPanel(pnl2);
		}		
	}

	if (invocacion == 'buscar') {
		if (operacionCancelada == true) {
			Richfaces.showModalPanel(pnl2);
		}		 	
	}

	if (invocacion == 'guardarFactura') {
		Richfaces.hideModalPanel(pnl1); 
		Richfaces.showModalPanel(pnl2);		 
	}

	if (invocacion == 'buscarObras') {
		Richfaces.showModalPanel(pnl1); 
		return;
	}

	if (invocacion == 'agregarObra') {
		Richfaces.showModalPanel(pnl2); 
		return;
	}

	if (invocacion == 'agregar') {
		if (operacionCancelada == true) {
			Richfaces.showModalPanel(pnl1);
		} else {
			Richfaces.showModalPanel(pnl2);
		}
	}

	if (invocacion == 'eliminar') {
		if (operacionCancelada == true) {
			Richfaces.showModalPanel(pnl1);
		} else {
			Richfaces.showModalPanel(pnl2);
		}		
	}	

	if (invocacion == 'boton') {
		if (operacionCancelada == true) {
			Richfaces.hideModalPanel(pnl2);
		} else {
			if (msj =='BIEN' || msj =='OK') {
				Richfaces.hideModalPanel('pnlNuevoEditarCajaChica');
			}
			
			Richfaces.hideModalPanel(pnl1);
			Richfaces.hideModalPanel(pnl2);
		}
	}

	if (invocacion == 'boton2') {			
		Richfaces.hideModalPanel(pnl1);
		Richfaces.showModalPanel(pnl2);
		javascript:window.open('/reportes/ReporteGeneric.faces', 'Caja Chica', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	}
} 

function checaCancelacion(msg, pnlMensajes, pnlError) {
	console.log("checaCancelacion");
	console.log("------------------------------------");
	console.log("msg: " + msg);
	console.log("pnlMensajes: " + pnlMensajes);
	console.log("pnlError: " + pnlError);

	if (msg == "")
		Richfaces.hideModalPanel(pnlMensajes);
	else
		Richfaces.showModalPanel(pnlError);
}

function evaluEditar(procesoCancelado, pnlOperacion, pnlMensaje) {
	console.log('------------------------------');
	console.log('function evaluEditar(procesoCancelado, pnlOperacion, pnlMensaje)');
	console.log('    procesoCancelado : ' + procesoCancelado);
	console.log('    pnlOperacion     : ' + pnlOperacion);
	console.log('    pnlMensaje       : ' + pnlMensaje);

	if (procesoCancelado == true) {
		Richfaces.showModalPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
		return;
	}

	Richfaces.showModalPanel(pnlOperacion);
	console.log('OK\n------------------------------');
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

function detallesCajaChica(url, msg,pnl) {
	console.log("detallesCajaChica");
	console.log("------------------------------------");
	console.log("url: " + url);
	console.log("msg: " + msg);
	console.log("pnl: " + pnl);
	
	if (msg == "")
		Richfaces.showModalPanel(pnl);
	else
		window.open(url);
}

function agregaRet(pnl1,pnl2,msj) {
	console.log("agregaRet");
	console.log("------------------------------------");
	console.log("pnl1: " + pnl1);
	console.log("pnl2: " + pnl2);
	console.log("msj: " + msj);
	
	if (msj != 0) {
		Richfaces.showModalPanel(pnl2);
	} else {
		Richfaces.hideModalPanel(pnl1);
	}
}

function imprimeRep(pnlRep) {
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

function buscarProyectos(valor, pnlMensajes) {
	console.log("buscarProyectos");
	console.log("------------------------------------");
	console.log("valor: " + valor);
	console.log("pnlMensajes: " + pnlMensajes);
		
	Richfaces.showModalPanel(pnlMensajes);
}

function analizarArchivo(operacionCancelada, pnlUpload, pnlMensajes) {
	console.log('------------------------------');
	console.log('function analizarArchivo(operacionCancelada, pnlUpload, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log(' pnlUpload : ' + pnlUpload);
	console.log('    pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	Richfaces.hideModalPanel(pnlUpload);
	console.log('function analizarArchivo DONE\n------------------------------');
}

function descargar(operacionCancelada, modulo, pnlMensajes) {
	console.log('\n------------------------------');
	console.log('function descargar(operacionCancelada, modulo, pnlMensajes)');
	console.log('      operacionCancelada : ' + operacionCancelada);
	console.log('      modulo : ' + modulo);
	console.log(' pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensajes);
		console.log('\nERROR\n------------------------------');
		return;
	}
	
	window.open(modulo + '/reportes/ReporteGeneric.faces', 'Satic', 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
	console.log('\nDONE\n------------------------------');
}

function editar(operacionCancelada, pnlMensaje, pnlOperacion) {
	console.log('function editar(' + operacionCancelada + ', ' + pnlMensaje + ', ' + pnlOperacion + ')');
	
	if (operacionCancelada == true) {
		console.log('Show modal pnlMensaje [' + pnlMensaje + ']');
		RichFaces.showModalPanel(pnlMensaje);
	} else {
		console.log('Show modal pnlOperacion [' + pnlOperacion + ']');
		RichFaces.showModalPanel(pnlOperacion);
	}
}

function nuevaBusqueda(operacionCancelada, pnlMensaje, pnlOperacion) {
	console.log("------------------------------------");
	console.log("nuevaBusqueda(operacionCancelada, pnlMensaje, pnlOperacion)\n------------------------------------");
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

function buscar(operacionCancelada, pnlMensajes) {
	console.log("buscar(operacionCancelada, pnlMensajes)\n------------------------------------");
	console.log("operacionCancelada: " + operacionCancelada);
	console.log("pnlMensajes: " + pnlMensajes);

	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	console.log("end of buscar(operacionCancelada, pnlMensajes)\n------------------------------------");
}

function salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log("------------------------------------");
	console.log("salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador)");
	console.log("    operacionCancelada : " + operacionCancelada);
	console.log("    pnlOperacion       : " + pnlOperacion);
	console.log("    pnlMensajes        : " + pnlMensajes);
	console.log("    validador          : ",  validador);

	console.log("Validamos REQUERIDOS ...");
	if (validador.hasChildNodes()) {
		console.log("REQUERIDOS\n------------------------------------");
		return;
	}

	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensajes);
		console.log("ERROR\n------------------------------------");
		return;
	} 
	
	Richfaces.showModalPanel(pnlMensajes);
	Richfaces.hideModalPanel(pnlOperacion);
	console.log("OK\n------------------------------------");
}

function guardar(operacionCancelada, pnlOperacion, pnlMensaje, validador) {
	 console.log('funcion guardar(operacionCancelada, pnlOperacion, pnlMensaje, validador)\n----------------------------------------');
	 console.log('  operacionCancelada :', operacionCancelada);
	 console.log('  pnlOperacion       :', pnlOperacion);
	 console.log('  pnlMensaje         :', pnlMensaje);
	 console.log('  ---------------------------------------------------------------------');
	 console.log('  validador          :', validador);

	 console.log("Evalua REQUERIDOS");
	 if (validador.hasChildNodes()) {
		 console.log("REQUERIDOS\n----------------------------------------");
		 return;
	 }

	 if (operacionCancelada == true) {
		 Richfaces.showModalPanel(pnlMensaje);
		 console.log("ERROR\n----------------------------------------");
		 return;
	 }
	 
	 Richfaces.hideModalPanel(pnlOperacion);
	 Richfaces.showModalPanel(pnlMensaje);
	 console.log('DONE\n----------------------------------------');
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

function buscarPersonaNegocio(operacionCancelada, pnlMensajes) {
	console.log("buscarPersonaNegocio(operacionCancelada, pnlMensajes)\n------------------------------------");
	console.log("operacionCancelada: " + operacionCancelada);
	console.log("pnlMensajes: " + pnlMensajes);

	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	console.log("end of buscarPersonaNegocio(operacionCancelada, pnlMensajes)\n------------------------------------");
}

function seleccionarPersonaNegocio(operacionCancelada, pnlMensajes, pnlOperacion) {
	console.log("seleccionarPersonaNegocio(operacionCancelada, pnlMensajes, pnlOperacion)\n------------------------------------");
	console.log("      operacionCancelada: " + operacionCancelada);
	console.log("      pnlMensajes: " + pnlMensajes);
	console.log("pnlOperacion: " + pnlOperacion);

	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	else
		Richfaces.hideModalPanel(pnlOperacion);
	console.log("end of seleccionarPersonaNegocio(operacionCancelada, pnlMensajes, pnlOperacion)\n------------------------------------");
}

function descartarXML(operacionCancelada, pnlMensajes, pnlOperacion) {
	console.log("------------------------------------");
	console.log("descartarXML(operacionCancelada, pnlMensajes, pnlOperacion)\n------------------------------------");
	console.log("      operacionCancelada: " + operacionCancelada);
	console.log("      pnlMensajes: " + pnlMensajes);
	console.log("pnlOperacion: " + pnlOperacion);

	if (operacionCancelada == false) {
		Richfaces.hideModalPanel(pnlOperacion);
		console.log("DONE descartarXML\n------------------------------------");
		return;
	}

	Richfaces.showModalPanel(pnlMensajes);
	console.log("DONE descartarXML\n------------------------------------");
}

function borrarXML(operacionCancelada, pnlMensajes, pnlOperacion) {
	console.log("------------------------------------");
	console.log("borrarXML(operacionCancelada, pnlMensajes, pnlOperacion)\n------------------------------------");
	console.log("      operacionCancelada: " + operacionCancelada);
	console.log("      pnlMensajes: " + pnlMensajes);
	console.log("pnlOperacion: " + pnlOperacion);

	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensajes);
		console.log("ERROR borrarXML\n------------------------------------");
	} else {
		Richfaces.hideModalPanel(pnlOperacion);
		console.log("DONE borrarXML\n------------------------------------");
	}
}

function validarNuevaComprobacion(operacionCancelada, pnlOperacion, pnlMensaje, validador) {
	 console.log('funcion validarNuevaComprobacion(operacionCancelada, pnlOperacion, pnlMensaje, validador)\n----------------------------------------');
	 console.log('  operacionCancelada :', operacionCancelada);
	 console.log('  pnlOperacion       :', pnlOperacion);
	 console.log('  pnlMensaje         :', pnlMensaje);
	 console.log('  ---------------------------------------------------------------------');
	 console.log('  validador          :', validador);

	 console.log("Evalua REQUERIDOS");
	 if (validador.hasChildNodes()) {
		 console.log("REQUERIDOS\n----------------------------------------");
		 return;
	 }

	 if (operacionCancelada == true) {
		 Richfaces.showModalPanel(pnlMensaje);
		 console.log("ERROR\n----------------------------------------");
		 return;
	 }
	 
	 Richfaces.showModalPanel(pnlOperacion);
	 console.log('DONE\n----------------------------------------');
}

function evaluaEditar(operacionCancelada, conFactura, pnlOperacion1, pnlOperacion2, pnlMensaje) {
	console.log('---------------------------------------------------------------------------');
	console.log('function evaluaEditar(operacionCancelada, conFactura, pnlOperacion1, pnlOperacion2, pnlMensaje)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    conFactura         : ' + conFactura);
	console.log('    pnlOperacion1      : ' + pnlOperacion1);
	console.log('    pnlOperacion2      : ' + pnlOperacion2);
	console.log('    pnlMensaje         : ' + pnlMensaje);
	
	if (operacionCancelada == true) { 
		Richfaces.showModalPanel(pnlMensaje);
		console.log('ERROR\n---------------------------------------------------------------------------');
		return;
	} 
	
	if (conFactura == true) 
		Richfaces.showModalPanel(pnlOperacion1);
	else 
		Richfaces.showModalPanel(pnlOperacion2);
	console.log('OK\n---------------------------------------------------------------------------');
}

function cargarXML(operacionCancelada, tipoMensaje, multiplesConceptos, pnlUpload, pnlComprobacion, pnlFactura, pnlMensaje) {
	console.log('---------------------------------------------------------------------------');
	console.log('function cargarXML(operacionCancelada, tipoMensaje, multiplesConceptos, pnlUpload, pnlComprobacion, pnlFactura, pnlMensaje)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    tipoMensaje        : ' + tipoMensaje);
	console.log('    multiplesConceptos : ' + multiplesConceptos);
	console.log('    pnlUpload          : ' + pnlUpload);
	console.log('    pnlComprobacion    : ' + pnlComprobacion);
	console.log('    pnlFactura         : ' + pnlFactura);
	console.log('    pnlMensaje         : ' + pnlMensaje);
	
	if (operacionCancelada == true) { 
		Richfaces.showModalPanel(pnlMensaje);
		console.log('ERROR\n---------------------------------------------------------------------------');
		return;
	} 
	
	Richfaces.hideModalPanel(pnlUpload);
	if (multiplesConceptos == true)
		Richfaces.showModalPanel(pnlFactura); 
	else
		Richfaces.showModalPanel(pnlComprobacion); 
	if (tipoMensaje == 30) 
		Richfaces.showModalPanel(pnlMensaje);
	if (tipoMensaje == 31) 
		Richfaces.showModalPanel(pnlMensaje);
	console.log('OK\n---------------------------------------------------------------------------');
}

