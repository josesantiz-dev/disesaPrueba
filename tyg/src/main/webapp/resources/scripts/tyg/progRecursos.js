function checaReporte(result, pnlMsg, listErrores){
	
	if(listErrores.hasChildNodes())
		return ;
	
	if(result.value=="")
		javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Programacion Recursos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	else
		Richfaces.showModalPanel(pnlMsg);
}
 