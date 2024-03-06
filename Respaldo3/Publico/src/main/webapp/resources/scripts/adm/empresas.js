function guardar(valor, pnlOperacion, pnlMsg, listErrores){

	if(campos_requeridos(listErrores))
		return ;
	
	if (valor == "")
		Richfaces.hideModalPanel(pnlOperacion);
	
	Richfaces.showModalPanel(pnlMsg);
}

function eliminar(valor, pnlOperacion, pnlMsg){
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOperacion);
}

function buscar(valor, pnlMsg){
	
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
}

function validaRfc(paramRFC) {
	var personaFisica = '^(([A-Z]|[a-z]|\s){1})(([A-Z]|[a-z]){3})([0-9]{6})((([A-Z]|[a-z]|[0-9]){3}))';
	var personaMoral  = '^(([A-Z]|[a-z]){3})([0-9]{6})((([A-Z]|[a-z]|[0-9]){3}))';
	var validaRFC = '';
	
	if(paramRFC.trim() == '') 
		return;
	
	if (paramRFC.length == 12)
		validaRFC = personaMoral;
	
	if (paramRFC.length == 13)
		validaRFC = personaFisica;
	
	if (validaRFC == ''){
		alert('El RFC no tiene el formato correcto');
		return;
	}
	
	var validador = new RegExp(validaRFC);
	if (paramRFC.match(validador) == null) {
		alert('El RFC no tiene el formato correcto');
		return;
	}
}

function analizarArchivo(valor, pnlUpload, pnlMsg){
	console.log('function analizarArchivo(valor, pnlUpload, pnlMsg)\n------------------------------');
	console.log('valor     : ' + valor);
	console.log('pnlUpload : ' + pnlUpload);
	console.log('pnlMsg    : ' + pnlMsg);
	
	if(valor == '' || valor == 'OK') {
		console.log('hide pnlUpload');
		Richfaces.hideModalPanel(pnlUpload);
	} else {
		console.log('show pnlMsg');
		Richfaces.showModalPanel(pnlMsg);
	}

	console.log('function analizarArchivo DONE\n------------------------------');
}