function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores){
	
	if(listErrores.hasChildNodes())
		return ;

	if(operacion == "reporte" && hidden == ''){
		javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Conciliacion Cuentas Bancarias', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	}else{
		Richfaces.showModalPanel(pnlMsg);
	}
}