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
		if(v_bool){
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		} else {
			RichFaces.ui.PopupPanel.showPopupPanel(pnl1);
		}
		return;
	}
	
	if(invocacion == 'guardar'){
		console.log('---> on guardar');
		if(v_bool){
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		} else {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		}
		return;
	}
		
	if(invocacion == 'buscar'){
		console.log('---> on buscar');
		if(v_bool){
			Richfaces.ui.PopupPanel.showPopupPanel(pnl2);
		}
		return;
	}
	 
	if(invocacion == 'boton'){	
		console.log('---> on boton');
		if(v_bool){
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		} else {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		}
		return;
	}	
}

function salvar(v_bool, pnlOperacion, pnlMsg, listErrores){
	console.log('function salvar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> on campos_requeridos');
		return;
	}
	
	if (! v_bool) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		console.log('---> v_bool false');
	}
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function mensaje(v_bool, pnlOperacion, pnlMsg, listErrores) {
	if(campos_requeridos(listErrores)){
		console.log('---> on campos_requeridos');
		return;
	}
	
	//if (! v_bool) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
		console.log('---> v_bool:' + v_bool);
	//}
}

function autocompleteKeyDownEvent(event) {
	console.log(event);
}

function autocompleteFocusEvent(component, event) {
	console.log(event);
	console.log(RichFaces.ui);
	RichFaces.ui.AutocompleteBase.showPopup(component);
}

function autocompleteBlurEvent(component, event) {
	console.log(event);
	RichFaces.ui.AutocompleteBase.hidePopup(component);
}
