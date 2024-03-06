
var almacenAnterior =0;
var almacenNuevo=0;

function validaGuardarMovimiento(operacionCompleta, pnlError, pnlprincipal, pnlOK, btnSalvar, listErr){
	
	
	/*if(campos_requeridos(listErr))
		return ;
	*/
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
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function validaAgregarProducto(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('------------------------------');
	console.log('validaAgregarProducto(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	console.log('    operacionCancelada : ' + operacionCancelada);
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
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

function validaCabeceraSalida(operacionCompleta, pnlError, pnlprincipal){
	if (operacionCompleta == false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlError);
	}else{
		RichFaces.ui.PopupPanel.showPopupPanel(pnlprincipal)
	}
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

function validaCantidadProducto(cantidad, cantidadProducto, pnlAvisoCantidad, pnlAvisoCantidadCero){
	
	if(cantidad==0){//Dejo en blanco el campo
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCantidadCero);
	}
	
	if( cantidad > cantidadProducto ){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAvisoCantidad);
		console.log('No hay suficiente cantidad')
	}
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

function salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('------------------------------');
	console.log('salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('      pnlOperacion : ' + pnlOperacion);
	console.log('       pnlMensajes : ' + pnlMensajes);
	console.log('         validador : ');
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

function verSalida(operacionCancelada, baseTraspaso, pnlSalida, pnlTraspaso, pnlMensaje) {
	console.log('------------------------------')
	console.log('verSalida(operacionCancelada, baseTraspaso, pnlSalida, pnlTraspaso, pnlMensaje)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    baseTraspaso       : ' + baseTraspaso);
	console.log('    pnlSalida          : ' + pnlSalida);
	console.log('    pnlTraspaso        : ' + pnlTraspaso);
	console.log('    pnlMensaje         : ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
		return;
	}

	if (baseTraspaso == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlTraspaso);
	else
		RichFaces.ui.PopupPanel.showPopupPanel(pnlSalida);
	console.log('DONE\n------------------------------');
}

function validacion(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('---------------------------------------------------------');
	console.log('validacion(operacionCancelada, pnlOperacion, pnlMensajes)');
	console.log('	operacionCancelada : ' + operacionCancelada);
	console.log('   pnlOperacion 	   : ' + pnlOperacion);
	console.log('   pnlMensajes 	   : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n---------------------------------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('DONE\n---------------------------------------------------------');
}

