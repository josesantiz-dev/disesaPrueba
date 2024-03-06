function msgRevisando(listErrores, pnl1, pnl2, v_bool, invocacion) {
	console.log('function msgRevisando\n------------------------------');
	console.log(listErrores);
	console.log(pnl1);
	console.log(pnl2);
	console.log(v_bool);
	console.log(invocacion);
	
	if(listErrores.hasChildNodes()) {
		console.log('---> on hasChildNodes');
		return;	
	}
		
	if(campos_requeridos(listErrores)) {
		console.log('---> on campos_requeridos');
		return;
	}
	
	if(invocacion == 'editar'){
		console.log('---> on editar');
		if(v_bool == true){
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		} else {
			RichFaces.ui.PopupPanel.showPopupPanel(pnl1);
		}
	}
	
	if(invocacion == 'guardar'){
		console.log('---> on guardar');
		if(v_bool == true){
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		} else {
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
		} else {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		}		
	}	
}

function campos_requeridos(listErr){
	console.log('function campos_requeridos\n------------------------------');
	
	var mensajes = listErr.getElementsByTagName("*");
	console.log('listErr: ' + mensajes.length);
	console.log(mensajes);
	
	return mensajes.length > 1;
}

function buscarFacturas(valor, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}