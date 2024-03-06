function buscar(valor, pnlMsg){
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
}

function guardar(valor, pnlOp, pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;

	Richfaces.hideModalPanel(pnlOp);
	Richfaces.showModalPanel(pnlMsg);
}

function guardarGral(valor, pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;

	Richfaces.showModalPanel(pnlMsg);
}