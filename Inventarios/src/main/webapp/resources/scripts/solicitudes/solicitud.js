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

function eliminar(valor, pnlOperacion, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
}

function imprimeReporte(urlapp, resultadoOperacion, pnlMensaje){
	if(resultadoOperacion != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	else{
		var ventana = window.open(urlapp + '/reportes/ReporteGeneric.faces', 'Documentos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
		ventana.focus();
	}
}