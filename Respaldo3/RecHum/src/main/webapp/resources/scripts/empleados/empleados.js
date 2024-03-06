function buscar(valor, pnlMsg){
	if (valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}		
}

function validarDatosFamiliar(bandera, resultado, pnlActivo, pnlConfirmacion, pnlErrores){
	if(bandera==false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlErrores);
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlActivo);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmacion);
}

function campos_requeridos(listErr){
	console.log('function campos_requeridos\n------------------------------');
	console.log('listErr: \n');
	console.log(listErr);
	console.log('**********: \n');
	
	var mensajes = listErr.getElementsByTagName("*");
	console.log('listErr: ' + mensajes.length);
	console.log(mensajes);
	
	return mensajes.length > 1;
}

function validarBeneficiario(band, resultado, pnl1, pnlErrores){
	if(band == true){
		RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);	//ocultamos el panel si la validación fue correcta
		//RichFaces.ui.PopupPanel.showPopupPanel(pnl1);	//ocultamos el panel si la validación fue correcta
	} else {
		//alert(resultado);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlErrores);
	}
}

function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion, resultado) {
	
	if(campos_requeridos(listErrores))
		return ;
	
	if(invocacion == 'guardar'){
		console.log('---> on guardar');
		if(v_bool==false){
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		}else{
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		}
	}
		
	if(invocacion == 'buscar'){
		console.log('---> on buscar');
		if(v_bool == true){
			Richfaces.ui.PopupPanel.showPopupPanel(pnl2);
		}		 
	}
	 
	if(invocacion == 'boton'){	
		console.log('---> on boton');
		if(v_bool == true){
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		}
		else{
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		}		
	}	
}

function buscarPersonas(valor, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
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

function verificaPersonaSeleccionada(operacionCancelada, confirmaReingreso, pnlOperacion, pnlConfirma, pnlMensajes) { 
	console.log("-----------------------------------");
	console.log("verificaPersonaSeleccionada(operacionCancelada, confirmaReingreso, pnlOperacion, pnlConfirma, pnlMensajes)");
	console.log("   operacionCancelada : " + operacionCancelada);
	console.log("   confirmaReingreso  : " + confirmaReingreso);
	console.log("   pnlOperacion       : " + pnlOperacion);
	console.log("   pnlConfirma        : " + pnlConfirma);
	console.log("   pnlMensajes        : " + pnlMensajes);
	
	if (confirmaReingreso == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirma);
		console.log("CONFIRMAR\n---------------------------------------");
		return;
	}
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log("ERROR\n---------------------------------------");
		return;
	} 
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log("OK\n---------------------------------------");
}


//-------------------------------------------------------------------------------
// CONTRATOS
// ------------------------------------------------------------------------------

function nuevoContrato(operacionTerminada, pnlOperacion, pnlMensajes) { 
	console.log("\n-----------------------------------");
	console.log("nuevoContrato(operacionTerminada, pnlOperacion, pnlMensajes)");
	console.log("operacionTerminada : " + operacionTerminada);
	console.log("      pnlOperacion : " + pnlOperacion);
	console.log("       pnlMensajes : " + pnlMensajes);
	
	if (operacionTerminada == true) {
		console.log("function nuevoContrato DONE\n---------------------------------------");
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	} else {
		console.log("function nuevoContrato ERROR\n---------------------------------------");
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	}
}

function editarContrato(operacionTerminada, pnlOperacion, pnlMensaje) {
	console.log("\n-----------------------------------");
	console.log("function editarContrato(operacionTerminada, pnlOperacion, pnlMensaje)");
	console.log("operacionTerminada : " + operacionTerminada);
	console.log("      pnlOperacion : " + pnlOperacion);
	console.log("        pnlMensaje : " + pnlMensaje);
	
	if (operacionTerminada == true) {
		console.log("function editarContrato DONE\n---------------------------------------");
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	} else {
		console.log("function editarContrato ERROR\n---------------------------------------");
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	}
}

function salvarContrato(operacionTerminada, pnlOperacion, pnlMensajes, validador) {
	console.log("\n-----------------------------------");
	console.log('function salvarContrato(operacionTerminada, pnlOperacion, pnlMensajes, listErrores)');
	console.log("operacionTerminada : " + operacionTerminada);
	console.log("      pnlOperacion : " + pnlOperacion);
	console.log("       pnlMensajes : " + pnlMensajes);
	console.log("         validador : " + validador);

	console.log("\nValidando ... " );
	if (campos_requeridos(validador)){
		console.log("function salvarContrato REQUERIDOS\n---------------------------------------");
		return;
	}
	
	if (operacionTerminada == true) {
		console.log("function salvarContrato DONE\n---------------------------------------");
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	} else {
		console.log("function salvarContrato ERROR\n---------------------------------------");
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	}
}

function eliminarContrato(operacionTerminada, pnlOperacion, pnlMensaje) {
	console.log("\n-----------------------------------");
	console.log("function editarContrato(v_bool, pnlOperacion, pnlMensaje)");
	console.log("operacionTerminada : " + operacionTerminada);
	console.log("      pnlOperacion : " + pnlOperacion);
	console.log("        pnlMensaje : " + pnlMensaje);
	
	if (operacionTerminada == false) {
		console.log("function eliminarContrato ERROR\n---------------------------------------");
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	} 
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log("function eliminarContrato CLOSE\n---------------------------------------");
}

function validarDatosContrato(pnlActivo, pnlConfirmacion, mensaje, pnlErrorContrato, operacionTerminada) {
	if (operacionTerminada == false) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlErrorContrato);
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlActivo);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmacion);
}
