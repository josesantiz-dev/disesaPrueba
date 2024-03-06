function salvar(valor, pnlOperacion, pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;
	
	if (valor == "")
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function salvarSinCerrar(valor, pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function descontar(valor, pnlMsg){	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function buscar(valor, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function eliminar(valor, pnlOperacion, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
}

function importarPago(valor, pnlOperacion, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
}

function imprimeReporte(urlapp, resultadoOperacion, pnlMensaje){
	if(resultadoOperacion != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	else
		window.open(urlapp + '/reportes/ReporteGeneric.faces', 'Documentos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
}

function guardarImportacion(valor, pnlMsg, urlapp){
	if(valor == "")
		window.open(urlapp + '/reportes/ReporteGeneric.faces', 'Documentos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function imprimirImportacion(valor, pnlOperacion, urlapp, pnlMsgGral, pnlOperacion2){
	if(valor == ""){
		window.open(urlapp + '/reportes/ReporteGeneric.faces', 'Documentos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	} else{
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsgGral);
	}
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion2);
		
}