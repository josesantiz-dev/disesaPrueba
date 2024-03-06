function buscar(valor, pnlMsg, listErrores){
	
	if(campos_requeridos(listErrores))
		return ;
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
	else if(operacion == "salvar"){
		Richfaces.showModalPanel(pnlMsg);
	}
}

function guardar(pnlMsg){
	Richfaces.hideModalPanel(pnlOperacion);
}

function guardarValor(valor, pnlOperacion, pnlMsg){
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOperacion);
}

function verificaPerfil(valor, pnlOperacion, pnlMsg){
	if (valor != "")
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.showModalPanel(pnlOperacion);
}