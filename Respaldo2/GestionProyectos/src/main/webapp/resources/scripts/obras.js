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

function verificarCargaInsumos(cargaInsumosValidada, pnlMsg, pnlPresupuesto){
	if(cargaInsumosValidada == false) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlPresupuesto);
	}
	
	console.log('function verificarCargaInsumos DONE\n------------------------------');
}

function verificarCalculoPresupuesto(cargaInsumosValidada, pnlMsg){
	if(cargaInsumosValidada==false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
	console.log('function verificarCalculoPresupuesto DONE\n------------------------------');
}

function buscarObras(v_bool, pnlMsg) {
	console.log('function buscarObras\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarObras DONE\n------------------------------');
}

function buscarClientes(v_bool, pnlMsg) {
	console.log('function buscarClientes\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarClientes DONE\n------------------------------');
}

function buscarEmpleados(v_bool, pnlMsg) {
	console.log('function buscarEmpleados\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarEmpleados DONE\n------------------------------');
}

function buscarResponsables(v_bool, pnlMsg) {
	console.log('function buscarResponsables\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarResponsables DONE\n------------------------------');
}

function salvarDomicilio(v_bool, pnlOperacion, pnlMsg){
	console.log('function salvarDomicilio\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == false)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function salvarDomicilio DONE\n------------------------------');
}

function agregarEmpleado(v_bool, pnlOperacion, pnlMsg) {
	console.log('function agregarEmpleado\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
		return;
	}

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function agregarEmpleado DONE\n------------------------------');
}

function mensaje(v_bool, pnlOperacion, pnlMsg, listErrores) {
	console.log('function mensaje(v_bool, pnlOperacion, pnlMsg, listErrores)\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	console.log(listErrores);
	
	/*if(campos_requeridos(listErrores)){
		console.log('---> exit on campos_requeridos');
		return;
	}*/
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
	console.log('function mensaje(v_bool, pnlOperacion, pnlMsg, listErrores) DONE\n------------------------------');
}

function comprobarAlmacenObra(v_bool, pnlMsg) {
	console.log('function comprobarAlmacenObra\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if(v_bool == true){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
	
	console.log('function comprobarAlmacenObra DONE\n------------------------------');
}

function analizarArchivo(v_bool, pnlUpload, pnlMsg){
	console.log('function analizarArchivo(v_bool, pnlUpload, pnlMsg)\n------------------------------');
	console.log('v_bool       : ' + v_bool);
	console.log('pnlUpload    : ' + pnlUpload);
	console.log('pnlMsg       : ' + pnlMsg);
	
	if(v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlUpload);
	console.log('function analizarArchivo DONE\n------------------------------');
}

function nuevaSatic(v_bool, pnlOperacion, pnlMensaje) {
	console.log('function nuevaSatic(v_bool, pnlOperacion, pnlMensaje)\n------------------------------');
	console.log('v_bool       : ' + v_bool);
	console.log('pnlOperacion : ' + pnlOperacion);
	console.log('pnlMensaje   : ' + pnlMensaje);
	
	if(v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('function nuevaSatic DONE\n------------------------------');
}

function salvarConfirmar(operacionCancelada, confirmar, pnlConfirmacion, pnlOperacion, pnlMensajes, validador) {
	console.log('\n------------------------------\nfunction salvarConfirmar(operacionCancelada, confirmar, pnlOperacion, pnlConfirmacion, pnlMensajes, validador)');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('         confirmar : ' + confirmar);
	console.log('   pnlConfirmacion : ' + pnlConfirmacion);
	console.log('      pnlOperacion : ' + pnlOperacion);
	console.log('       pnlMensajes : ' + pnlMensajes);
	console.log('         validador : ' + validador);
	
	if (confirmar == true) {
		console.log('EXIT: CONFIRMACION\n------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmacion);
		return;
	}

	console.log('salvarConfirmar goTo salvar\n------------------------------');
	salvar(operacionCancelada, pnlOperacion, pnlMensajes, validador);
}
