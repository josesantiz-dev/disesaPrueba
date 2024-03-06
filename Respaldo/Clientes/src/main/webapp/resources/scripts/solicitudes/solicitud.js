function buscar(valor, pnlMsg){
	if (valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
}

function salvar(valor, pnlOperacion, pnlMsg, listErrores){

	if(campos_requeridos(listErrores))
		return ;
	
	if (valor == "")
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function salvarSolicitud(valor, pnlOperacion, pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;
	
	if (valor == ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}else{
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
}

function cancelarSolicitud(valor, pnlOperacion, pnlMsg){
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function enviarSolicitud(valor, pnlOperacion, pnlMsg){
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function guardarImpSocial(valor, pnlOperacion, pnlMsg){
	if (valor == "")
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function procesar(valor,pnlOperacion,pnlMsg){
	if(valor=="")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	else
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}