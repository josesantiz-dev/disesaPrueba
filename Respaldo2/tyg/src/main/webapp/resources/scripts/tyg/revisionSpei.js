function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores){

	if(listErrores.hasChildNodes())
		return ;
	
	if (hidden != "")
		Richfaces.showModalPanel(pnlMsg);
	else if(operacion ==  "select")
		Richfaces.showModalPanel(pnlOperacion);
	else if(operacion == "refrescar")
		Richfaces.showModalPanel(pnlOperacion);
	else if(operacion != "buscar" && operacion != "eliminar"){
		Richfaces.showModalPanel(pnlMsg);
	}
}

function cancela(valor, pnlOp, pnlMsg){
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOp);
}