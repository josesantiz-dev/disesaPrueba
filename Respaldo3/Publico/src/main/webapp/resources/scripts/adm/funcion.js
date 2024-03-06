function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores){

	if(campos_requeridos(listErrores))
		return ;

	if (hidden != "")
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
