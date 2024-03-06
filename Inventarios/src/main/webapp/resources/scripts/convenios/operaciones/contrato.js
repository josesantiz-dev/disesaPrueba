function salvar(hidden, pnlOperacion, pnlMsg,listErrores){	
	 
	if(campos_requeridos(listErrores)){		
		return ;
	}
	else {
		if (hidden != ""){
			Richfaces.showModalPanel(pnlMsg);
		}
		else{
			Richfaces.hideModalPanel(pnlOperacion);
			Richfaces.showModalPanel(pnlMsg);
		}
	
	}
}

function renovar(hidden, pnlOperacion, pnlMsg){	
	if (hidden != ""){
		Richfaces.showModalPanel(pnlMsg);
	}
	else{		
		Richfaces.showModalPanel(pnlOperacion);
	}
}

function buscar(valor, pnlMsg){
	if (valor != ""){
		Richfaces.showModalPanel(pnlMsg);
	}	
}

function eliminar(valor, pnlOperacion, pnlMsg){
	if (valor != ""){
		Richfaces.hideModalPanel(pnlOperacion);
		Richfaces.showModalPanel(pnlMsg);
	}		
	else{
		Richfaces.hideModalPanel(pnlOperacion);
	}	
}