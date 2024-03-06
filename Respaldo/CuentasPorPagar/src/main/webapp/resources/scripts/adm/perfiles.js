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