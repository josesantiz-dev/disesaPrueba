function guardar(valor, dir, pnlMsg){
	if(valor != '')
		Richfaces.showModalPanel(pnlMsg);
	else{
		javascript:window.open(dir + '/reportes/ReporteGeneric.faces', 'Reporte de lavado', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	}
}
