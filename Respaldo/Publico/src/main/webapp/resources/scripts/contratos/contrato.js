function buscar(valor, pnlMsg){
	if (valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
}

function salvar(valor, pnlOperacion, pnlMsg){
	if (valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
	else{		
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	}
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

function cambioPantalla(pnlOperacion,pnlDestino){
	RichFaces.ui.PopupPanel.showPopupPanel(pnlDestino);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
}