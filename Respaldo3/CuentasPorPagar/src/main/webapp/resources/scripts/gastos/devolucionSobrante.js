function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores){
	if(listErrores.hasChildNodes())
		return ;
	
	if (hidden.value != "")
		Richfaces.showModalPanel(pnlMsg);
	else if(operacion == "nuevo")
		Richfaces.showModalPanel(pnlOperacion);
	else if(operacion == "salvar"){
		Richfaces.hideModalPanel(pnlOperacion);
		Richfaces.showModalPanel(pnlMsg);
	}
	else if(operacion == "eliminar"){
		Richfaces.hideModalPanel(pnlOperacion);
	}
}

function guardar(hidden, pnlOperacion, pnlOperacion2, pnlMsg, operacion, listErrores){
	
	if(listErrores.hasChildNodes())
		return ;
	
	if(operacion == "salvar"){
		Richfaces.showModalPanel(pnlMsg);
		Richfaces.hideModalPanel(pnlOperacion);
		Richfaces.hideModalPanel(pnlOperacion2);
		
	}
}