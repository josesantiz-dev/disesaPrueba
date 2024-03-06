function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores){

	if(listErrores.hasChildNodes())
		return ;
	
	if (hidden.value != "")
		Richfaces.showModalPanel(pnlMsg);
	else if(operacion ==  "select")
		Richfaces.showModalPanel(pnlOperacion);
	else if(operacion == "refrescar")
		Richfaces.showModalPanel(pnlOperacion);
	else if(operacion != "buscar" && operacion != "eliminar"){
		Richfaces.showModalPanel(pnlMsg);
	}
}

function checaCancelar(valor, pnlOp, pnlMsg){
	if (valor.value != "")
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.showModalPanel(pnlOp);
}

function cancelaProgMin(valor, pnlOp, pnlMsg){
	if (valor.value != "")
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOp);
}