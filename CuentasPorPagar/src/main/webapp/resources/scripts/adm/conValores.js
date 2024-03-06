function guardar(result, pnlOp, pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;
	if(result!="")
		Richfaces.showModalPanel(pnlMsg);
	else{
		Richfaces.hideModalPanel(pnlOp);
		Richfaces.showModalPanel(pnlMsg);
	}
}

function buscar(result, pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;
	
	if(result!="")
		Richfaces.showModalPanel(pnlMsg);
}

function eliminar(result, pnlOp, pnlMsg){
	
	if(result!="")
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOp);
}