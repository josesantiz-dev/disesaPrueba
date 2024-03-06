function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores){

	if(listErrores.hasChildNodes()){
		
		if(operacion = "salvar")
			Richfaces.showModalPanel('pnlDatFaltantesCtaBan');
		return ;
	}
	
	if (hidden.value != "")
		Richfaces.showModalPanel(pnlMsg);
	else if(operacion == "nuevo")
		Richfaces.showModalPanel(pnlOperacion);
	else if(operacion == "salvar"){
		Richfaces.showModalPanel(pnlMsg);
		Richfaces.hideModalPanel(pnlOperacion);
	}
	else if(operacion == "eliminar"){
		Richfaces.hideModalPanel(pnlOperacion);
	}
	else if(operacion == "reporte"){
		Richfaces.hideModalPanel(pnlOperacion);
		javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	}
	
	function buscar(valor, pnlMsg){	
		if (valor != "")
			Richfaces.showModalPanel(pnlMsg);
	}
}