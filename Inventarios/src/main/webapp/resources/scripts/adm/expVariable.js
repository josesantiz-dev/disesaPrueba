function guardar(valor, pnlOp, pnlMsg, listErrores) {
	
	if(campos_requeridos(listErrores))
		return ;
 	 
	if(valor=='')
		Richfaces.hideModalPanel(pnlOp);
	
	Richfaces.showModalPanel(pnlMsg);
}

function buscar(pnlMsg, valor) {
 	
	if(valor != '')
		Richfaces.showModalPanel(pnlMsg);
}

function eliminar(pnlOrigen, pnlMsg, valor) {
 	
	if(valor != '')
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOrigen);
}