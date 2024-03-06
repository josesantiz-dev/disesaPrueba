
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

function validaAgregarProducto(operacionCompleta, pnlError, pnlprincipal){
	if (operacionCompleta == false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlError);
	}else{
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlprincipal)
	}
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