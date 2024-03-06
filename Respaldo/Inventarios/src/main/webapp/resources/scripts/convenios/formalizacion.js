function salvar(hidden, pnlOperacion, pnlMsg,listErrores){	
	 
	if(campos_requeridos(listErrores)){		
		return ;
	}
	else {
		if (hidden != ""){
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
		}
		else{
			RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
		}
	
	}
}

function salvarBasic(valor, pnlOperacion, pnlMsg){	
	if (valor == ""){
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	}
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function guardarSeg(valor, pnlOperacion, pnlMsg, listErrores){
	if(campos_requeridos(listErrores)){		
		return ;
	}
	
	if (valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
	else{		
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	}
}

function buscar(valor, pnlMsg){
	if (valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
}

function muestraModal(valor, pnlOp, pnlMsg){
	if (valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else{
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOp);
	}
}

function descargaArchivo(valor, url, pnlMsg){
	if (valor == ""){
		newUrl(url);
	} else{
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
}

function eliminar(valor, pnlOperacion, pnlMsg){
	if (valor != ""){
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}		
	else{
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		
	}
}