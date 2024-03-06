function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores){
	if(listErrores.hasChildNodes())
		return ;
	
	if (hidden.value != "")
		Richfaces.showModalPanel(pnlMsg);	
	else if(operacion == "guardar"){
		Richfaces.hideModalPanel(pnlOperacion);
		Richfaces.showModalPanel(pnlMsg);
	}
	else if(operacion == "editar"){		
		Richfaces.showModalPanel(pnlOperacion);
	}
	else if(operacion == "eliminar"){		
		Richfaces.showModalPanel(pnlOperacion);
	}
	else if(operacion == "aplicar"){		
		Richfaces.showModalPanel(pnlMsg);
	}
}