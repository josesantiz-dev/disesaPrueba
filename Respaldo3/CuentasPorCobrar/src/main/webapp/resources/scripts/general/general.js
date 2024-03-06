function campos_requeridos(listErr) {
	var mensajes = listErr.getElementsByTagName("*");
	return mensajes.length > 1;
}

function keyF9(e, obj) {
	var unicode=e.keyCode? e.keyCode : e.charCode;
	if(unicode == 120){
		obj.callSuggestion(true);
	}
}

function goUrl(url) {
	window.location = url;
}

function newUrl(url) {
	window.open(url, 'Nueva ventana', 'width=700, height=500, menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
}
 
function cambiarPass(valor, pnlOrigen, pnlDestino, listErrors, origenModal, pnlResponsabilidades) {
	if (campos_requeridos(listErrors))
		return;

	if (valor != '')
		RichFaces.ui.PopupPanel.showPopupPanel(pnlDestino);
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOrigen);
	if (origenModal == 'login')
		RichFaces.ui.PopupPanel.showPopupPanel(pnlResponsabilidades);
	else
		RichFaces.ui.PopupPanel.showPopupPanel(pnlDestino);
}
