function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores) {
	
	if(listErrores.hasChildNodes()) {
		return ;
	}
	if (hidden.value != "")
		Richfaces.showModalPanel(pnlMsg);
	else if(operacion == "nuevo") {
		Richfaces.showModalPanel(pnlOperacion);
	}
	else if(operacion == "salvar"){
		Richfaces.hideModalPanel(pnlOperacion);
		Richfaces.showModalPanel(pnlMsg);
	}
	else if(operacion == "eliminar"){
		Richfaces.hideModalPanel(pnlOperacion);
	}
}