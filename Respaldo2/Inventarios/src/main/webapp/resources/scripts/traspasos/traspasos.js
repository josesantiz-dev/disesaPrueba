
var almacenAnterior =0;
var almacenNuevo=0;

function validaGuardarTraspaso(operacionCompleta, pnlError, pnlprincipal, pnlOK, btnSalvar, listErr) {
	if(campos_requeridos(listErr)) {
		console.log('funcion terminada');
		return;
	}
		
	if (operacionCompleta == false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlError);
	}else{
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlprincipal);
	}
	
	$(btnSalvar).prop('disabled',operacionCompleta);
	console.log('funcion terminada');
}

function campos_requeridos(listErr){
	var mensajes = listErr.getElementsByTagName("*");
	return mensajes.length > 1;
}

function salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('------------------------------');
	console.log('function salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	console.log('  operacionCancelada : ' + operacionCancelada);
	console.log('        pnlOperacion : ' + pnlOperacion);
	console.log('         pnlMensajes : ' + pnlMensajes);
	console.log('           validador : ');
	console.log(validador);
	
	if (campos_requeridos(validador)) {
		console.log('REQUERIDOS\n------------------------------');
		return;
	} 
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	window.open('./reportes/ReporteGeneric.faces', 'Salida de Almacen', 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
	console.log('OK\n------------------------------');
}

function salvarLanzarReporte(operacionCancelada, lanzarReporte, pnlOperacion, pnlMensajes, validador) {
	console.log('------------------------------');
	console.log('salvarLanzarReporte(operacionCancelada, lanzarReporte, pnlOperacion, pnlMensajes, validador)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    lanzarReporte      : ' + lanzarReporte);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	console.log('    validador          : ');
	console.log(validador);
	
	if (campos_requeridos(validador)) {
		console.log('REQUERIDOS\n------------------------------');
		return;
	} 
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	if (lanzarReporte == true)
		window.open('./reportes/ReporteGeneric.faces', 'Salida de Almacen', 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
	console.log('OK\n------------------------------');
}

function eliminarCancelar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('------------------------------');
	console.log('eliminarCancelar(operacionCancelada, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('\nERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('\nOK\n------------------------------');
}

function reporte(operacionCancelada, pnlMensaje) {
	console.log('------------------------------')
	console.log('function reporte(operacionCancelada, pnlMensaje)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('            pnlMensaje : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
		return;
	}
	
	javascript:window.open('./reportes/ReporteGeneric.faces', 'Reporte', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	console.log('DONE\n------------------------------');
}

function buscar(v_bool, pnlMsg){
	if (v_bool==true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function mensaje(v_bool, pnlOperacion, pnlMsg) {
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
	console.log('---> v_bool:' + v_bool);
	console.log('function mensaje DONE\n------------------------------');
}

function buscarObras(v_bool, pnlMsg) {
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function buscarProductos(v_bool, pnlMsg) {
	console.log('Evaluando busqueda de productos, v_bool: '+v_bool+', pnlMsg: '+pnlMsg);
	// frmPrincipal:pnlBusquedaProductos
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	
	console.log('done...');
}

function validaAgregarProducto(operacionCompleta, pnlError, pnlprincipal){
	if (operacionCompleta == false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlError);
	}else{
		//RichFaces.ui.PopupPanel.hidePopupPanel(pnlprincipal)
	}
}

function validaCabeceraTraspaso(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('------------------------------');
	console.log('function validaCabeceraTraspaso(operacionCancelada, pnlError, pnlOperacion)');
	console.log('  operacionCancelada : ' + operacionCancelada);
	console.log('        pnlOperacion : ' + pnlOperacion);
	console.log('         pnlMensajes : ' + pnlMensajes);

	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

function mostrarCantidadProducto(){
	
}

function validaCambioAlmacen(numeroItems, pnlAvisoCambio, idAlmacenOrigen){

	almacenAnterior = idAlmacenOrigen;
	
	if(numeroItems > 0){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCambio);	//Al cambiar el almacen de salida, informar que la lista se borrará
	}
}

function validaRegresarAlmacen(idAlmacen, cboAlmacen){
	almacenNuevo = idAlmacen;
	$(cboAlmacen).prop('value',almacenAnterior);	
	almacenNuevo = almacenAnterior;
}

function validaCantidadProducto(cantidad, cantidadProducto, pnlAvisoCantidad, pnlAvisoCantidadCero) {
	console.log('----------------------------------');
	console.log('function validaCantidadProducto(cantidad, cantidadProducto, pnlAvisoCantidad, pnlAvisoCantidadCero)');
	console.log('             cantidad : ' + cantidad);
	console.log('     cantidadProducto : ' + cantidadProducto);
	console.log('     pnlAvisoCantidad : ' + pnlAvisoCantidad);
	console.log(' pnlAvisoCantidadCero : ' + pnlAvisoCantidadCero);
	
	if (cantidad == 0) {//Dejo en blanco el campo
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCantidadCero);
		console.log('VALIDACION: cantidad = 0\n----------------------------------');
		return;
	}
	
	if (cantidad > cantidadProducto) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCantidad);
		console.log('VALIDACION: cantidad > cantidadProducto\n----------------------------------');
		return;
	}
	
	console.log('OK\n----------------------------------');
}

function soloNumeros(event, permiteNegativos, debug) {
	var key = event.key;
	var keyCode = event.keyCode;
	var caracteresValidos = "0123456789";

	debug 			  = debug || false;
	permiteNegativos  = permiteNegativos || false;
	caracteresValidos = (permiteNegativos == true) ? (caracteresValidos + '-') : caracteresValidos;

	if (debug == true) {
		console.log("-> soloNumeros(event, permiteNegativos, debug)\n----------------------------------");
		console.log("->     Key: " + key);
		console.log("-> KeyCode: " + keyCode);
		console.log("-> Matcher: " + caracteresValidos);
		console.log(event);
	}

	if (event.ctrlKey == true && "CVXcvx".includes(key)) {
		if (debug == true)
			console.log("-----------\nControl + " + key + "\n-----------");
		return true;
	}
	
	/*
	 *  8 = BACKSPACE
	 *  9 = TAB
	 * 35 = FIN
	 * 36 = INICIO
	 * 37 = ARROWLEFT
	 * 38 = ARROWUP
	 * 39 = ARROWRIGHT
	 * 40 = ARROWDOWN
	 * 46 = DELETE
	 */
	
	if (keyCode ==  8 || keyCode ==  9 || keyCode == 35 
		|| keyCode == 36 || keyCode == 37 || keyCode == 38 
		|| keyCode == 39 || keyCode == 40 || keyCode == 46 )
		return true;

	return (caracteresValidos.indexOf(key) > -1);
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
	decimal 		  = decimal || '';
	maxDigitos 		  = maxDigitos   || 10;
	maxDecimales 	  = maxDecimales ||  2;
	permiteNegativos  = permiteNegativos || false;
	debug 			  = debug || false;
	caracteresValidos = (permiteNegativos == true) ? caracteresValidos + '-' : caracteresValidos;
	matcherString 	  = matcherString.replace(/MAXDIGIT/g, maxDigitos).replace('MAXDECIMAL', maxDecimales)
	decimal 		  = decimal.replace(/,/g, '');
	matcher 		  = new RegExp(matcherString);

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

