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

function imprimeReporte(urlapp, resultadoOperacion, pnlMensaje){
	if(resultadoOperacion != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	else{
		var ventana = window.open(urlapp + '/reportes/ReporteGeneric.faces', 'Documentos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
		ventana.focus();
	}
}

function analizarFactura(valor, pnlOperacion, pnlAbrir, pnlMsg){
	if(valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else{
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlAbrir);
	}
}

function analizarArchivo(v_bool, pnlUpload, pnlMsg){
	console.log('function analizarArchivo(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
	console.log('    v_bool: ' + v_bool);
	console.log(' pnlUpload: ' + pnlUpload);
	console.log('    pnlMsg: ' + pnlMsg);
	
	if(v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlUpload);
	}
	
	console.log('function analizarArchivo DONE\n------------------------------');
}