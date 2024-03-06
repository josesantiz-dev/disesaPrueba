
function guardar(pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;

	Richfaces.showModalPanel(pnlMsg);
}
