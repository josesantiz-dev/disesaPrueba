function procesar(mensaje, mostrarPopupMsg, popupMsg, popupCerrar, popupDestino, listErrores){
	if(listErrores != null && listErrores.hasChildNodes())
		return ;
	
	if(mensaje == '' && popupCerrar != null){
		RichFaces.ui.PopupPanel.hidePopupPanel(popupCerrar);
	}

	if(mostrarPopupMsg == 'si' || (mensaje != '' && mensaje != null)){
		RichFaces.ui.PopupPanel.showPopupPanel(popupMsg, {});
	}else if(popupDestino != null){
		RichFaces.ui.PopupPanel.showPopupPanel(popupDestino, {});
	}
}

function keyF9(e, obj){
	var unicode=e.keyCode? e.keyCode : e.charCode;
	if(unicode == 120){
		obj.callSuggestion(true);
	}
}

function goUrl(url){
	window.location = url;
}

function buscar(operacionCompleta, pnlMensajes) {
	if (operacionCompleta == false)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
}

function seleccionar(operacionCompleta, pnlOperacion, pnlMensajes) {
	console.log("function seleccionar(operacionCompleta, pnlOperacion, pnlMensajes)\n--------------------------------------------");
	console.log(operacionCompleta);
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	
	if (operacionCompleta == false) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
}

function busquedaDinamica(pnl) {
	RichFaces.ui.PopupPanel.showPopupPanel(pnl);
}

function obtenerSalida(mensaje, pnl, url, titulo) {
	if (mensaje != "") {
		RichFaces.ui.PopupPanel.showPopupPanel(pnl);
		return;
	}
	
	window.open(url, titulo, 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
}