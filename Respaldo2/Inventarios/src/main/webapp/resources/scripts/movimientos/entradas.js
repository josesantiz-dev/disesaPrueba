
var almacenAnterior =0;
var almacenNuevo=0;

function buscar(v_bool, pnlMsg) {
	if (v_bool==true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function guardar(operacionCompleta, pnlOperacion, pnlMensaje, validador) {
	console.log('--------------------------------------------------------------');
	console.log('funcion guardar(operacionCompleta, pnlOperacion, pnlMensaje, validador)');
	console.log('   operacionCompleta : ' + operacionCompleta);
	console.log('   pnlOperacion      : ' + pnlOperacion);
	console.log('   pnlMensaje        : ' + pnlMensaje);
	console.log('   validador         : ');
	console.log(validador);

	console.log('');
	if (campos_requeridos(validador)) {
		console.log('REQUERIDOS');
		console.log('--------------------------------------------------------------');
		return;
	}
	
	if (operacionCompleta == false) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR');
		console.log('--------------------------------------------------------------');
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	window.open('./reportes/ReporteGeneric.faces', 'Entrada a Almacen', 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
	console.log('OK');
	console.log('--------------------------------------------------------------');
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

function validaGuardarMovimiento(operacionCompleta, pnlError, pnlprincipal, pnlOK, btnSalvar, listErr){
	
	
	/*if(campos_requeridos(listErr))
		return ;
	*/
	if (operacionCompleta == false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlError);
	}else{
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlprincipal);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOK);
	}
	
	$(btnSalvar).prop('disabled',operacionCompleta);
	console.log('funcion terminada');
}

function mensaje(v_bool, pnlOperacion, pnlMsg) {
	
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
	console.log('---> v_bool:' + v_bool);
	console.log('function mensaje DONE\n------------------------------');
}

function buscarObras(v_bool, pnlMsg) {
	//console.log('function buscarObras\n------------------------------');
	//console.log(v_bool);
	//console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	//console.log('function buscarObras DONE\n------------------------------');
}

function buscarProductos(v_bool, pnlMsg) {
	//console.log('function buscarObras\n------------------------------');
	//console.log(v_bool);
	//console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	//console.log('function buscarObras DONE\n------------------------------');
}

function puedeBuscarTraspaso(puedeBuscar, pnlBusquedaTraspasos, pnlAviso, validador) {
	if (campos_requeridos(validador))
		return;
	
	if(puedeBuscar==true){
		console.log('puedeBuscarTraspaso = true');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlBusquedaTraspasos);
	}else{
		console.log('puedeBuscarTraspaso = false');
		//seleccione almacen
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAviso);
	}
}

function validaBusquedaTraspaso(puedeBuscar, pnlOperacion, pnlMensajes, validador) {
	console.log('------------------------------');
	console.log('validaBusquedaTraspaso(puedeBuscar, pnlOperacion, pnlMensajes, validador)');
	console.log('  puedeBuscar : ' + puedeBuscar);
	console.log(' pnlOperacion : ' + pnlOperacion);
	console.log('  pnlMensajes : ' + pnlMensajes);
	console.log('    validador : ' + validador);

	console.log('validando requeridos ... ');
	if (campos_requeridos(validador)) {
		console.log('function validaBusquedaTraspaso VALIDADOR\n------------------------------');
		return;
	}
	
	if(puedeBuscar == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	else
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function validaBusquedaTraspaso DONE\n------------------------------');
}

function validaBusqueda(hasError, pnlOperacion, pnlMensajes, validador) {
	console.log('------------------------------');
	console.log('validaBusqueda(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	console.log('    hasError     : ' + hasError);
	console.log('    pnlOperacion : ' + pnlOperacion);
	console.log('    pnlMensajes  : ' + pnlMensajes);
	console.log('    validador    : ');
	console.log(validador);

	console.log('validando requeridos ... ');
	if (campos_requeridos(validador)) {
		console.log('\nREQUERIDOS\n------------------------------');
		return;
	}
	
	if (hasError == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('\nERROR\n------------------------------');
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('\nOK\n------------------------------');
}

function validaCambioAlmacen(esTraspaso, pnlAvisoCambio, idAlmacenOrigen, numeroItems){
	
	if(esTraspaso==false) return;

	almacenAnterior = idAlmacenOrigen;

	console.log('numeroItems: '+numeroItems + ', idAlmacenOrigen: '+idAlmacenOrigen );
	
	if(numeroItems > 0){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCambio);	//Al cambiar el almacen de salida, informar que la lista se borrará
	}
	
}

function regresarAlmacenAnterior(idAlmacen, cboAlmacen){
	$(cboAlmacen).prop('value',idAlmacen);
}

function validaCantidadProducto(cantidadSolicitada, cantidadRecibida, cantidadYaEntregada, pnlAvisoCantidad){
	
	console.log('Cantidad Solicitada: '+cantidadSolicitada+', Cantidad Recibida: '+cantidadRecibida + ', cantidad ya Entregada: '+cantidadYaEntregada);
	if(cantidadRecibida>cantidadSolicitada){//Dejo en blanco el campo
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCantidad);
	}
	
	if( cantidadRecibida > cantidadYaEntregada ){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCantidad);
		console.log('Ya se ha entregado ('+cantidadYaEntregada+') del producto');
	}
}

function soloEnteros(){
	console.log("Tecla: "+key);
	
	if ( key == 8 ){	//Aqui se pueden anexar mas caracteres Ascii permitidos
		return true;
	}
	
	if( key >= 48 && key <= 57 ){	//Números del 0 al 9
		return true;
	}
	
	return false;
}

function soloDecimal(key){
	console.log("Tecla: "+key);
	
	if( key >= 48 && key <= 57 ){	//Números del 0 al 9
		return true;
	}else{
		if ( key == 8 || key == 46 ){	// 8: backspace, 46: punto
			return true;
		}
	}
	
	return false;
}

function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('------------------------------');
	console.log('seleccionar(operacionCancelada, pnlOperacion, pnlMensajes)');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('      pnlOperacion : ' + pnlOperacion);
	console.log('       pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('function seleccionar ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('function seleccionar DONE\n------------------------------');
}
