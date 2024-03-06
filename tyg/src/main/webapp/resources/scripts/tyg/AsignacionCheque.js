function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores){
	if(listErrores.hasChildNodes())
		return ;
	
	if (hidden.value != "")
		Richfaces.showModalPanel(pnlMsg);
	else if(operacion == "nuevo")
		Richfaces.showModalPanel(pnlOperacion);
	else if(operacion == "salvar"){
		Richfaces.hideModalPanel(pnlOperacion);
		Richfaces.showModalPanel(pnlMsg);
	}
}

function imprimeRep(valor, pnlMsg){
	if(valor != "")
		Richfaces.showModalPanel(pnlMsg);
	else
		javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Asignacion Cheques', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
}