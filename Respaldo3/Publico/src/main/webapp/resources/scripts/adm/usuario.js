function salvar(hidden, pnlOperacion, pnlMsg,listErrores) {	
	if (campos_requeridos(listErrores)) {		
		return ;
	} else {
		if (hidden != "") {
			Richfaces.showModalPanel(pnlMsg);
		} else {
			Richfaces.hideModalPanel(pnlOperacion);
			Richfaces.showModalPanel(pnlMsg);
		}
	}
}

function salvarUsuario(hidden, pnlOperacion, pnlMsg,listErrores) {
	if (campos_requeridos(listErrores)) {		
		return ;
	} else {
		if (hidden != "") {
			Richfaces.showModalPanel(pnlMsg);
		} else{
			Richfaces.showModalPanel(pnlMsg);
		}
	}
}

function buscar(valor, pnlMsg){
	if (valor != "") {
		Richfaces.showModalPanel(pnlMsg);
	}	
}

function eliminar(valor, pnlOperacion, pnlMsg) {
	if (valor != "") {
		Richfaces.hideModalPanel(pnlOperacion);
		Richfaces.showModalPanel(pnlMsg);
	} else {
		Richfaces.hideModalPanel(pnlOperacion);
	}
}

function seleccionar(v_bool, pnlOperacion, pnlMsg) {
	console.log('function seleccionar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true) {
		Richfaces.showModalPanel(pnlMsg);
		console.log('function seleccionar EEROR\n------------------------------');
		return;
	} 
	
	Richfaces.hideModalPanel(pnlOperacion);
	console.log('function seleccionar DONE\n------------------------------');
}
