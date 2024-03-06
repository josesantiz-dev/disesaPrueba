function mensaje(operacionCancelada, pnlMensajes) {
	console.log('\n------------------------------\nfunction mensaje START\n------------------------------');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log(' pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	console.log('function mensaje DONE\n------------------------------');
}

function buscar(operacionCancelada, pnlMensajes) {
	console.log('\n------------------------------\nfunction buscar START\n------------------------------');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log(' pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	console.log('function buscar DONE\n------------------------------');
}

function nuevo(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\n------------------------------\nfunction nuevo START\n------------------------------');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log(' pnlOperacion       : ' + pnlOperacion);
	console.log(' pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == false)
		Richfaces.showModalPanel(pnlOperacion);
	else
		Richfaces.showModalPanel(pnlMensajes);
	console.log('function nuevo DONE\n------------------------------');
}

function editar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\n------------------------------\nfunction editar START\n------------------------------');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log(' pnlOperacion       : ' + pnlOperacion);
	console.log(' pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == false)
		Richfaces.showModalPanel(pnlOperacion);
	else
		Richfaces.showModalPanel(pnlMensajes);
	console.log('function editar DONE\n------------------------------');
}

function eliminar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\n------------------------------\nfunction eliminar START\n------------------------------');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log(' pnlOperacion       : ' + pnlOperacion);
	console.log(' pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == false)
		Richfaces.hideModalPanel(pnlOperacion);
	else
		Richfaces.showModalPanel(pnlMensajes);
	console.log('function eliminar DONE\n------------------------------');
}

function guardar(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('\n------------------------------\nfunction guardar START\n------------------------------');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log(' pnlOperacion       : ' + pnlOperacion);
	console.log(' pnlMensajes        : ' + pnlMensajes);
	console.log(validador);

	console.log('\nValidando requeridos ... ');
	if(campos_requeridos(validador)) {
		console.log('function salvar REQUERIDOS\n------------------------------');
		return;
	}
	
	if (operacionCancelada == false)
		Richfaces.hideModalPanel(pnlOperacion);
	Richfaces.showModalPanel(pnlMensajes);
	console.log('function salvar DONE\n------------------------------');
}

function guardarNoCerrar(operacionCancelada, pnlMensajes, validador) {
	console.log('\n------------------------------\nfunction guardarNoCerrar START\n------------------------------');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log(' pnlMensajes        : ' + pnlMensajes);
	console.log(' validador          : ', validador);

	console.log('\nValidando requeridos ... ');
	if (campos_requeridos(validador)) {
		console.log('function salvar REQUERIDOS\n------------------------------');
		return;
	}
	
	Richfaces.showModalPanel(pnlMensajes);
	console.log('function guardarNoCerrar DONE\n------------------------------');
}

function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\n------------------------------\nfunction seleccionar START\n------------------------------');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log(' pnlOperacion       : ' + pnlOperacion);
	console.log(' pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == false)
		Richfaces.hideModalPanel(pnlOperacion);
	else
		Richfaces.showModalPanel(pnlMensajes);
	console.log('function seleccionar DONE\n------------------------------');
}

function reporte(operacionCancelada, pnlMensajes) {
	console.log('------------------------------\nfunction reporte(operacionCancelada, pnlMensajes)\n');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('        pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		Richfaces.showModalPanel(pnlMensajes);
		return;
	}
	
	javascript:window.open('../reportes/ReporteGeneric.faces', 'Reporte', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	console.log('end function: DONE\n------------------------------');
}
