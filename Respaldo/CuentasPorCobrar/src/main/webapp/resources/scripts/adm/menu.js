function buscar(valor, pnlMsg){
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
}

function guardar(valor, pnlOrigen, pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;
	
	if(valor == '')
		Richfaces.hideModalPanel(pnlOrigen);
	
	Richfaces.showModalPanel(pnlMsg);
	
}

function eliminar(valor, pnlOperacion, pnlMsg){
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOperacion);
}