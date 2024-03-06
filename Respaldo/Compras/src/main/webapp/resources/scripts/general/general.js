function campos_requeridos(listErr){
	var mensajes = listErr.getElementsByTagName("*");
	return mensajes.length > 1;
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

function cerrarVentana() {
	window.close();
}

function cambiarModulo(event) {
	var url = document.referrer; // "https://disesa.condese.net:9443/DISESA/index.faces;jsessionid=1nlu9mG7YdHT4sRRJKMy-Urr.undefined";
	var currentUrl = document.location.href;
	var prevUrl = "";
	var curModule = "";
	var preModule = "";
	var puerto = "";
	var splitted = null;
	var result = "";
		
	result = "cambiarModulo :: BACK TO '" + url + "' FROM '" + currentUrl + "'";
	console.log(result);
	console.log(event);

	splitted = url.split(";");
	prevUrl = splitted[0];
	
	preModule = getModuloFromUrl(prevUrl);
	curModule = getModuloFromUrl(currentUrl);
	puerto = getPuertoFromUrl(currentUrl);
	
	result += " || " + "preModule: '" + preModule + "' curModule: '" + curModule + "'";
	console.log("cambiarModulo :: preModule: '" + preModule + "' curModule: '" + curModule + "'");

	if (prevUrl == "https://disesa.condese.net:" + puerto + "/DISESA/index.faces") {
		result += " || " + "do: 'frmPrincipal:cmdCambiarModulo').click()";
	    document.getElementById('frmPrincipal:cmdCambiarModulo').click();
	} else {
	    if ((preModule != undefined && curModule != undefined) && (preModule != curModule)) {
			result += " || " + "do: 'frmPrincipal:cmdCambiarModulo').click()";
	    	document.getElementById('frmPrincipal:cmdCambiarModulo').click();
		} else {
			result += " || " + "do nothing";
		}
	}

	console.log(result);
}

function getPuertoFromUrl(url) {
	var splitted = null;
	
	if (url == undefined || url == "") return "";
	splitted = url.split(":");
	
	return splitted[splitted.length - 2].substring(0, 4);
}

function getModuloFromUrl(url) {
	var splitted = null;
	
	if (url == undefined || url == "") return "";
	splitted = url.split("/");
	
	return splitted[splitted.length - 2];
}