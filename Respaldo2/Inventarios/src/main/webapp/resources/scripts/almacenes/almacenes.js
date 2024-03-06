function buscar(valor, pnlMsg){
	if (valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}		
}

function buscarObras(v_bool, pnlMsg) {
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function validaGuardar(operacionCompleta, pnlActivo, pnlMensajes, btnSalvar){
	
	if(operacionCompleta==false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		$(btnSalvar).prop('disabled',false);	//rehabilitar el boton si no se pudo guardar
		return;
	};
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlActivo);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	
}

function salvarDomicilio(v_bool, pnlOperacion, pnlMsg){
	console.log('function salvarDomicilio\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == false)
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function salvarDomicilio DONE\n------------------------------');
}

function verEditar(operacionCancelada, pnlOperacion, pnlMensaje) {
	console.log('------------------------------');
	console.log('verEditar(operacionCancelada, pnlOperacion, pnlMensaje)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

function seleccionar(operacionCancelada, pnlOperacion, pnlMensaje) {
	console.log('------------------------------');
	console.log('seleccionar(operacionCancelada, pnlOperacion, pnlMensaje)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

function processQuery(operacionCancelada, pnlMensajes) {
	console.log('------------------------------');
	console.log('processQuery(operacionCancelada, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == false) {
		console.log('OK\n------------------------------');
		return;
	} 

	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('ERROR\n------------------------------');
}

function processToShow(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('------------------------------');
	console.log('processToShow(operacionCancelada, pnlOperacion, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

function processToHide(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('------------------------------');
	console.log('processToHide(operacionCancelada, pnlOperacion, pnlMensajes)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

function validateToShow(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('------------------------------');
	console.log('validateToShow(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	console.log('    validador          : ');
	console.log(validador);
	
	if (campos_requeridos(validador)) {
		console.log('REQUERIDOS\n------------------------------');
		return;
	} 
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

function validateToHide(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('------------------------------');
	console.log('validateToHide(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	console.log('    validador          : ');
	console.log(validador);
	
	if (campos_requeridos(validador)) {
		console.log('REQUERIDOS\n------------------------------');
		return;
	} 
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}

function validateToSave(operacionCancelada, pnlOperacion, pnlMensajes, validador) {
	console.log('------------------------------');
	console.log('validateToSave(operacionCancelada, pnlOperacion, pnlMensajes, validador)');
	console.log('    operacionCancelada : ' + operacionCancelada);
	console.log('    pnlOperacion       : ' + pnlOperacion);
	console.log('    pnlMensajes        : ' + pnlMensajes);
	console.log('    validador          : ');
	console.log(validador);
	
	if (campos_requeridos(validador)) {
		console.log('REQUERIDOS\n------------------------------');
		return;
	} 
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 

	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('OK\n------------------------------');
}
