function campos_requeridos(listErr){
	var mensajes = listErr.getElementsByTagName("*");
	return mensajes.length > 1;
}

function keyF9(e, obj){
	var unicode=e.keyCode ? e.keyCode : e.charCode;

	console.log(e);
	console.log(obj);
	console.log("unicode: " + unicode);
	
	if(unicode == 120){
		console.log("calling suggestion");
		obj.callSuggestion(true);
	} else {
		console.log("not call suggestion");
	}
}

function goUrl(url){
	window.location = url;
}

function newUrl(url){
	window.open(url, 'Nueva ventana', 'width=700, height=500, menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
}
 
function cambiarPass(valor, pnlOrigen, pnlDestino, listErrors, origenModal, pnlResponsabilidades){
	if(campos_requeridos(listErrors))
		return;

	if(valor != '')
		Richfaces.showModalPanel(pnlDestino);
	
	Richfaces.hideModalPanel(pnlOrigen);
	if(origenModal == 'login')
		Richfaces.showModalPanel(pnlResponsabilidades);
	else
		Richfaces.showModalPanel(pnlDestino);
}
