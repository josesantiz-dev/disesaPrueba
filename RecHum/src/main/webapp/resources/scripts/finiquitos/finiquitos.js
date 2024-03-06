function buscar(operacionCompleta, pnlMsg){
	if(operacionCompleta==false)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function buscarEmpleados(operacionCompleta, pnlMensajes){ 
	if(operacionCompleta==false)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function editar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('------------------------------');
	console.log('function validarEmpleadoSeleccionado(operacionCancelada, pnlOperacion, pnlMensajes)\n------------------------------');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion 		: ' + pnlOperacion);
	console.log('    pnlMensajes 		: ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

function validaGuardarFiniquito(operacionCompleta, pnlActivo, pnlMensajes){
	/*if(operacionCompleta==false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlActivo);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);*/
	
	if(operacionCompleta == true)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlActivo);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
}

function salvarFiniquito(operacionCompleta, pnlOperacion, pnlMensajes, validador) {
	if(campos_requeridos(validador))
		return ;
	
	if (operacionCompleta == true)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
}

function validarBotonSalvar(estatusFiniquito, btnSalvarFiniquito){
	//console.log(boton);
	if(estatusFiniquito==1){
		$(btnSalvarFiniquito).prop('disabled',true);			//deshabilitar el boton
	}
}

function validarEmpleadoSeleccionado(operacionCancelada, pnlOperacion, pnlMensaje) {
	console.log('------------------------------');
	console.log('function validarEmpleadoSeleccionado(operacionCancelada, pnlOperacion, pnlMensaje)\n------------------------------');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion 		: ' + pnlOperacion);
	console.log('    pnlMensaje 		: ' + pnlMensaje);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		console.log('ERROR\n------------------------------');
		return;
	} 

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

/******************************************/
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

function validarDatosContrato(pnlActivo, pnlConfirmacion, mensaje, pnlErrorContrato, bandera){
	
	if(bandera==false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlErrorContrato);
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlActivo);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmacion);
	

}

function validarBeneficiario(band, resultado, pnl1, pnlErrores){
	
	if(band == true){
		RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);	//ocultamos el panel si la validación fue correcta
		//RichFaces.ui.PopupPanel.showPopupPanel(pnl1);	//ocultamos el panel si la validación fue correcta
	}else{
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