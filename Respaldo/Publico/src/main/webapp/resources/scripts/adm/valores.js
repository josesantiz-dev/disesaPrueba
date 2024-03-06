function checaResultado(valor, pnlOperacion, pnlMsg, operacion, listErrores){
	if(campos_requeridos(listErrores))
		return ;
	
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);	
	else if(operacion == "guardar"){
		Richfaces.hideModalPanel(pnlOperacion);
		Richfaces.showModalPanel(pnlMsg);
	}
	else if(operacion == "editar"){		
		Richfaces.showModalPanel(pnlOperacion);
	}	
	else if(operacion == "agregar"){		
		Richfaces.showModalPanel(pnlOperacion);
	}
}

function eliminar(result, pnlOp, pnlMsg){
	if(result!="")
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOp);
}

function buscar(valor, pnlMsg){
	
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
}