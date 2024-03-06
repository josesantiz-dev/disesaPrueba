
function msgRevisando(listErrores,pnl1,pnl2,operacionCancelada,invocacion,msj) {
	console.log('function msgRevisando START\n-------------------------------'); // aceptar
	console.log('invocacion : ' + invocacion); // aceptar
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('      pnl1 : ' + pnl1);
	console.log('      pnl2 : ' + pnl2);
	console.log('       msj : ' + msj); 
	//console.log(listErrores);
	
	if (invocacion == 'agregarObra') {
		//console.log('Richfaces.showModalPanel("' + pnl2 + '")');
		Richfaces.showModalPanel(pnl2);
		console.log('function agregarObra DONE\n-------------------------------'); // aceptar
		return;
	} 

	if (invocacion == 'buscarObras') {
		//console.log('Richfaces.showModalPanel("' + pnl1 + '")');
		if (operacionCancelada == true)
			Richfaces.showModalPanel(pnl2);
		console.log('function buscarObra DONE\n-------------------------------'); // aceptar
		return; 
	}
	
	if (invocacion == 'agregarOrdenCompra') {
		nuevaBusqueda(operacionCancelada, pnl2, pnl1);
		console.log('function agregarOrdenCompra DONE\n-------------------------------'); // aceptar
		return; 
	}
	
	if (listErrores.hasChildNodes()) {
		console.log('function REQUERIDOS DONE\n-------------------------------'); // aceptar
		return;
	}
	
	if (invocacion == 'guardar') {		
		if (operacionCancelada == true) {
			if ((msj.indexOf('SE CANCELA EL FOLIO ')!= -1)  || (msj.indexOf('SE CANCELAN LOS FOLIOS ')!= -1 ))
				Richfaces.showModalPanel(pnl1);
			else
				Richfaces.showModalPanel(pnl2);
		} else {
			if (msj =='C') {
				Richfaces.hideModalPanel('pnlNuevoEditarGasto');
				Richfaces.showModalPanel(pnl2);
				//javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			} else {
				Richfaces.showModalPanel(pnl2);
			}
		}
	} else 
		if (invocacion == 'boton') {
			if (operacionCancelada == true)
				Richfaces.hideModalPanel(pnl2);
			else{
				if (msj=='C' || msj=='T') {
					Richfaces.hideModalPanel('pnlNuevoEditarGasto');
					Richfaces.hideModalPanel(pnl1);
					Richfaces.hideModalPanel(pnl2);
				}
				else if (msj.indexOf('El gasto involucra retenciones, favor de capturalas') != -1 || msj.indexOf('Esta retencion ya ha sido agregada') != -1)
					Richfaces.hideModalPanel(pnl2);
				else{
				Richfaces.hideModalPanel(pnl1);
				Richfaces.hideModalPanel(pnl2);
				}
			}
		} else 
			if (invocacion == 'boton2') {
				Richfaces.hideModalPanel(pnl1);
				Richfaces.showModalPanel(pnl2);
				
				if (msj == 'C')
					javascript:window.open('/reportes/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			} else 
				if (invocacion == 'eliminarGasto') {
					if (msj != '')
						Richfaces.showModalPanel(pnl2);			
					else
						Richfaces.hideModalPanel(pnl1);
				} else 
					if (invocacion == 'guardarFactura') {
						if (msj != '')
							Richfaces.showModalPanel(pnl2);
						else{
							Richfaces.hideModalPanel(pnl1);
							Richfaces.showModalPanel(pnl2);
						}			
					} else 
						if (invocacion == 'reporte') {
							if (msj != '')
								Richfaces.showModalPanel(pnl2);
							else
								javascript:window.open('/reportes/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
						} else 
							if (invocacion == 'buscar') {
								if (msj != '') {
									Richfaces.showModalPanel(pnl2);
								}
							} else 
								if (invocacion == 'cancelar') {
									if (msj != '')
										Richfaces.showModalPanel(pnl1);
									else
										Richfaces.showModalPanel(pnl2);
								} else
									if (invocacion == 'agregar') {
										if (msj != '')
											Richfaces.showModalPanel(pnl1);			
										else
											Richfaces.showModalPanel(pnl2);
									} else
										if (invocacion == 'aceptar') {
												if (msj != '') {
													Richfaces.showModalPanel(pnl2);
												} else
													Richfaces.hideModalPanel(pnl1);
											} else/*para todo lo demas si estubo bien muestra el panel 2*/	
												Richfaces.showModalPanel(pnl2);
}

/* OK */
function agregaRet(pnl1,pnl2,msj) {
	if (msj != '') {
		Richfaces.showModalPanel(pnl2);
	} else{
		Richfaces.hideModalPanel(pnl1);
	}
}

/* OK */
function analizarArchivo(operacionCancelada, pnlUpload, pnlMensajes) {
	console.log('------------------------------');
	console.log('function analizarArchivo(operacionCancelada, pnlUpload, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlUpload          : ' + pnlUpload);
	console.log('    pnlMensajes         : ' + pnlMensajes);

	Richfaces.hideModalPanel(pnlUpload);
	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	console.log('OK\n------------------------------');
}

function imprimeRep(pnlRep) {
	javascript:window.open('/reportes/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
 	Richfaces.hideModalPanel(pnlRep);
}

/* OK */
function nuevaBusqueda(operacionCancelada, pnlOperacion, pnlMensajes) {
 	console.log('function nuevaBusqueda(operacionCancelada, pnlOperacion, pnlMensajes)\n------------------------------');
 	console.log('       operacionCancelada: ' + operacionCancelada);
 	console.log(' pnlOperacion: ' + pnlOperacion);
 	console.log('       pnlMensajes: ' + pnlMensajes);
 	
 	if (operacionCancelada == true) {
 		Richfaces.showModalPanel(pnlMensajes);
 		return;
 	} 
 		
 	Richfaces.showModalPanel(pnlOperacion);
 	console.log('DONE\n------------------------------');
}

/* OK */
function buscar(operacionCancelada, pnlMensajes) {
 	console.log('function buscar(operacionCancelada, pnlMensajes)\n------------------------------');
 	console.log('       operacionCancelada: ' + operacionCancelada);
 	console.log('       pnlMensajes: ' + pnlMensajes);
 	
 	if (operacionCancelada == true)
 		Richfaces.showModalPanel(pnlMensajes);
 	console.log('fin function buscar DONE\n------------------------------');
}

function editar(operacionCancelada, pnlOperacion, pnlMensajes) {
 	console.log('function editar(operacionCancelada, pnlOperacion, pnlMensajes)\n------------------------------');
 	console.log('       operacionCancelada: ' + operacionCancelada);
 	console.log(' pnlOperacion: ' + pnlOperacion);
 	console.log('       pnlMensajes: ' + pnlMensajes);
 	
 	if (operacionCancelada == true)
 		Richfaces.showModalPanel(pnlMensajes);
 	else 
 		Richfaces.showModalPanel(pnlOperacion);
 	console.log('fin function editar DONE\n------------------------------');
}

/* OK */
function guardar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('funcion guardar(operacionCancelada, pnlOperacion, pnlMensajes, validador)\n----------------------------------------');
	console.log('  operacionCancelada :', operacionCancelada);
	console.log('  pnlOperacion       :', pnlOperacion);
	console.log('  pnlMensajes         :', pnlMensajes);
	console.log('  ---------------------------------------------------------------------');
	console.log('  validador          :', validador);

	console.log("Evalua REQUERIDOS");
	if (validador.hasChildNodes()) {
		console.log("REQUERIDOS\n----------------------------------------");
		return;
	}

	if (operacionCancelada == true)  {
		Richfaces.showModalPanel(pnlMensajes);
		console.log("ERROR\n----------------------------------------");
		return;
	}
	
	Richfaces.hideModalPanel(pnlOperacion);
	Richfaces.showModalPanel(pnlMensajes);
	console.log('DONE\n----------------------------------------');
}

/* OK */
function evalua(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('----------------------------------------');
	console.log('evalua(operacionCancelada, pnlOperacion, pnlMensajes)');
	console.log('      operacionCancelada : ' + operacionCancelada);
	console.log('pnlOperacion : ' + pnlOperacion);
	console.log('  pnlMensajes : ' + pnlMensajes);
		
 	if (operacionCancelada == true) {
 		Richfaces.showModalPanel(pnlMensajes);
 		console.log('funcion ERROR\n----------------------------------------');
 		return;
 	}
 	
 	Richfaces.showModalPanel(pnlOperacion);
	console.log('funcion DONE\n----------------------------------------');
}

/* OK */
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

/* OK */
function reporte(operacionCancelada, pnlMensajes, url) {
	console.log('------------------------------');
	console.log('function reporte(operacionCancelada, pnlMensajes, url)');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('         pnlMensajes : ' + pnlMensajes);
	console.log('                url : ' + url);
	
	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	}
	
	javascript:window.open('./reportes/ReporteGeneric.faces', 'Reporte', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	console.log('DONE\n------------------------------');
}

/* OK */
function borrar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('----------------------------------------');
	console.log('funcion borrar(operacionCancelada, pnlOperacion, pnlMensajes)');
	console.log('      operacionCancelada : ' + operacionCancelada);
	console.log('pnlOperacion : ' + pnlOperacion);
	console.log('  pnlMensajes : ' + pnlMensajes);
		
 	if (operacionCancelada == true) {
 		Richfaces.showModalPanel(pnlMensajes);
 		console.log('funcion ERROR\n----------------------------------------');
 		return;
 	}
 	
 	Richfaces.hideModalPanel(pnlOperacion);
	console.log('funcion DONE\n----------------------------------------');
}
 
function confirmarEliminarCancelar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('----------------------------------------');
	console.log('function confirmarEliminarCancelar(operacionCancelada, pnlOperacion, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes         : ' + pnlMensajes);
		
 	if (operacionCancelada == true)
 		Richfaces.showModalPanel(pnlMensajes);
 	else 
 		Richfaces.showModalPanel(pnlOperacion);
	console.log('funcion DONE\n----------------------------------------');
}
 
function eliminarCancelar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('----------------------------------------');
	console.log('function confirmaEliminarCancelar(operacionCancelada, pnlOperacion, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes         : ' + pnlMensajes);
		
 	if (operacionCancelada == true)
 		Richfaces.showModalPanel(pnlMensajes);
 	Richfaces.hideModalPanel(pnlOperacion);
	console.log('OK\n----------------------------------------');
}

/* OK */
function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes) {
 	console.log("------------------------------------");
 	console.log("seleccionar(operacionCancelada, pnlOperacion, pnlMensajes)");
 	console.log("------------------------------------");
 	console.log(" operacionCancelada : " + operacionCancelada);
 	console.log("       pnlOperacion : " + pnlOperacion);
 	console.log("         pnlMensajes : " + pnlMensajes);

 	if (operacionCancelada == true)
 		Richfaces.showModalPanel(pnlMensajes);
 	else
 		Richfaces.hideModalPanel(pnlOperacion);
 	console.log("end of seleccionar(operacionCancelada, pnlMensajes, pnlOperacion)\n------------------------------------");
}

/* OK */
function validarNuevaComprobacion(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('funcion validarNuevaComprobacion(operacionCancelada, pnlOperacion, pnlMensajes, validador)\n----------------------------------------');
	console.log('  operacionCancelada :', operacionCancelada);
	console.log('  pnlOperacion       :', pnlOperacion);
	console.log('  pnlMensajes         :', pnlMensajes);
	console.log('  ---------------------------------------------------------------------');
	console.log('  validador          :', validador);

	console.log("Evalua REQUERIDOS");
	if (validador.hasChildNodes()) {
		console.log("REQUERIDOS\n----------------------------------------");
		return;
	}

	if (operacionCancelada == true)  {
		Richfaces.showModalPanel(pnlMensajes);
		console.log("ERROR\n----------------------------------------");
		return;
	}
	
	Richfaces.showModalPanel(pnlOperacion);
	console.log('DONE\n----------------------------------------');
}

/* OK */
function evaluaEditar(operacionCancelada, conFactura, pnlOperacion1, pnlOperacion2, pnlMensajes) {
	console.log('---------------------------------------------------------------------------');
	console.log('function evaluaEditar(operacionCancelada, conFactura, pnlOperacion1, pnlOperacion2, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    conFactura         : ' + conFactura);
	console.log('    pnlOperacion1      : ' + pnlOperacion1);
	console.log('    pnlOperacion2      : ' + pnlOperacion2);
	console.log('    pnlMensajes         : ' + pnlMensajes);
	
	if (operacionCancelada == true) { 
		Richfaces.showModalPanel(pnlMensajes);
		console.log('ERROR\n---------------------------------------------------------------------------');
		return;
	} 
	
	if (conFactura == true) 
		Richfaces.showModalPanel(pnlOperacion1);
	else 
		Richfaces.showModalPanel(pnlOperacion2);
	console.log('OK\n---------------------------------------------------------------------------');
}

/* OK */
function cargarXML(operacionCancelada, tipoMensaje, multiplesConceptos, pnlUpload, pnlComprobacion, pnlFactura, pnlMensajes) {
	console.log('---------------------------------------------------------------------------');
	console.log('function cargarXML(operacionCancelada, tipoMensaje, multiplesConceptos, pnlUpload, pnlComprobacion, pnlFactura, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    tipoMensaje        : ' + tipoMensaje);
	console.log('    multiplesConceptos : ' + multiplesConceptos);
	console.log('    pnlUpload          : ' + pnlUpload);
	console.log('    pnlComprobacion    : ' + pnlComprobacion);
	console.log('    pnlFactura         : ' + pnlFactura);
	console.log('    pnlMensajes         : ' + pnlMensajes);
	
	if (operacionCancelada == true) { 
		Richfaces.showModalPanel(pnlMensajes);
		console.log('ERROR\n---------------------------------------------------------------------------');
		return;
	} 
	
	Richfaces.hideModalPanel(pnlUpload);
	if (multiplesConceptos == true)
		Richfaces.showModalPanel(pnlFactura); 
	else
		Richfaces.showModalPanel(pnlComprobacion); 
	if (tipoMensaje == 30) 
		Richfaces.showModalPanel(pnlMensajes);
	if (tipoMensaje == 31) 
		Richfaces.showModalPanel(pnlMensajes);
	console.log('OK\n---------------------------------------------------------------------------');
}

/* USO: onkeypress="return soloDecimales(event, this.value, 10, 2)" */
function soloDecimales(event, decimal, maxDigitos, maxDecimales, permiteNegativos, debug) {
 	var resultEvent = false;
 	var matcherString = '^([0-9]{0,MAXDIGIT})$|^([0-9]{0,MAXDIGIT})?([.][0-9]{1,MAXDECIMAL})?$';
 	var matcher = null;
 	var caracteresValidos = "0123456789.";
 	var key = event.key;
 	var keyCode = event.keyCode;
 	var indexKey = event.target.selectionStart;
 	
 	// Inicializamos
 	decimal 		= decimal || '';
 	maxDigitos 		= maxDigitos   || 10;
 	maxDecimales 	= maxDecimales ||  2;
 	permiteNegativos  = permiteNegativos || false;
 	debug 			= debug || false;
 	caracteresValidos = (permiteNegativos == true) ? caracteresValidos + '-' : caracteresValidos;
 	matcherString 	= matcherString.replace(/MAXDIGIT/g, maxDigitos).replace('MAXDECIMAL', maxDecimales)
 	decimal 		= decimal.replace(/,/g, '');
 	matcher 		= new RegExp(matcherString);

 	if (debug == true) {
 		console.log("-> soloDecimales(event, decimal, maxDigitos, maxDecimales)\n----------------------------------");
 		console.log("->          Key : " + key);
 		console.log("->      KeyCode : " + keyCode);
 		console.log("->      decimal : " + decimal);
 		console.log("->   maxDigitos : " + maxDigitos);
 		console.log("-> maxDecimales : " + maxDecimales);
 		console.log("->     indexKey : " + indexKey);
 		console.log("->matcherString : " + matcherString);
 		console.log("->  validString : " + caracteresValidos);
 		console.log(event);
 	}

 	if (event.ctrlKey == true && "CVXcvx".includes(key)) {
 		if (debug == true)
 			console.log("-----------\nControl + " + key + "\n-----------");
 		return true;
 	}
 	
 	if (keyCode ==  8 || keyCode ==  9 || keyCode == 35 
 		|| keyCode == 36 || keyCode == 37 || keyCode == 38 
 		|| keyCode == 39 || keyCode == 40 || keyCode == 46 )
 		return true;
 	
 	// Validaciones contra el valor actual del input
 	if (decimal != undefined && decimal != '') {
 		if ((caracteresValidos.indexOf(key) > -1) == false)
 			return false;

 		// Comprobamos que solo exista un punto en la cadena
 		if (key == '.') {
 			// Si ya existe en la cadena
 			if (decimal.indexOf(key) > -1)
 				return false;
 			return true;
 		}

 		// Comprobamos que solo exista un guion y que este al inicio de la cadena
 		if (key == '-') {
 			// Si no permite negativos
 			if (permiteNegativos == false)
 				return false;

 			// si ya existe en la cadena
 			if (decimal.indexOf(key) > -1)
 				return false;

 			// Si no esta al inicio de la cadena
 			if (indexKey > 0)
 				return false;

 			return true;
 		}

 		// Agregamos el caracter al valor actual del input (decimal) y evaluamos formato y longitud de las partes (digitos y decimales)
 		decimal = decimal.slice(0, indexKey) + key + decimal.slice(indexKey);
 		if (debug == true) {
 			console.log("-> ----------------------------------");
 			console.log("->valor updated : " + decimal);
 			console.log("-> matcher.test : " + matcher.test(decimal));
 		}

 		return matcher.test(decimal);
 	}
 	
 	return (caracteresValidos.indexOf(key) > -1);
}
