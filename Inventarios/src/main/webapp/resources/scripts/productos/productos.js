function buscar(valor, pnlMsg){
	if (valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}		
}

function busqueda(operacionCancelada, pnlMensajes) {
	console.log('------------------------------\n busqueda(operacionCancelada, pnlMensajes)');
	console.log(' operacionCancelada : ' + operacionCancelada);
	console.log('        pnlMensajes : ' + pnlMensajes);
	
	if (operacionCancelada == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	
	console.log('DONE\n------------------------------');
}

function nuevo(operacionCompleta, esAdministrativo, pnlOrigen, pnlOperacion, pnlMensajes) {
	console.log('------------------------------\n function nuevo START');
	console.log(' operacionCompleta : ' + operacionCompleta);
	console.log('  esAdministrativo : ' + esAdministrativo);
	console.log('         pnlOrigen : ' + pnlOrigen);
	console.log('      pnlOperacion : ' + pnlOperacion);
	console.log('       pnlMensajes : ' + pnlMensajes);
	
	if (esAdministrativo == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOrigen);
		console.log('ORIGEN\n------------------------------');
		return;
	} 
	
	if (operacionCompleta == false) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('ERROR\n------------------------------');
		return;
	} 
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('DONE\n------------------------------');
}

function validaGuardarProducto(operacionCompleta, pnlOperacion, pnlMensajes, validadorRequeridos) {
	console.log('function validaGuardarProducto(operacionCompleta, pnlOperacion, pnlMensajes, validadorRequeridos)\n------------------------------');
	console.log(operacionCompleta);
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	console.log(validadorRequeridos);
	
	if(campos_requeridos(validadorRequeridos)){
		console.log('function validaGuardarProducto REQUERIDOS\n------------------------------');
		return;
	}
	
	if(operacionCompleta == false) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		console.log('function validaGuardarProducto ERROR\n------------------------------');
		return;
	};

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function validaGuardarProducto DONE\n------------------------------');
}

function analizarExcel(v_bool, pnlOperacion, pnlMsg){
	console.log('function analizarExcel(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
	console.log('v_bool ' + v_bool);
	console.log('pnlOperacion ' + pnlOperacion);
	console.log('pnlMsg ' + pnlMsg);
	
	/*if(v_bool == false) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}*/
	
	if(v_bool == true) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function analizarExcel DONE\n------------------------------');
}

function exportarMaestro(operacionTerminada, pnlOperacion, pnlMensajes) {
	console.log('exportarMaestro(operacionTerminada, pnlOperacion, pnlMensajes)\n------------------------------');
	console.log('  operacionTerminada : ' + operacionTerminada);
	console.log('  pnlOperacion       : ' + pnlOperacion);
	console.log('  pnlMensajes        : ' + pnlMensajes);
	
	if (operacionTerminada == false) {
		console.log('end function exportarMaestro ERROR\n------------------------------');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		return;
	}

	console.log('--> Mostramos popup');
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	window.open('./reportes/ReporteGeneric.faces', 'Exportar Maestro', 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
	console.log('end function exportarMaestro\n------------------------------');
}

function soloEnteros(){
	console.log("Tecla: "+key);
	
	if ( key == 8 ){	//Aqui se pueden anexar mas caracteres Ascii permitidos
		return true;
	}
	
	if( key >= 48 && key <= 57 ){	//Números del 0 al 9
		return true;
	}
	
	return false;
}

function soloDecimal(key){
	console.log("Tecla: "+key);
	
	if( key >= 48 && key <= 57 ){	//Números del 0 al 9
		return true;
	}else{
		if ( key == 8 || key == 46 ){	// 8: backspace, 46: punto
			return true;
		}
	}
	
	return false;
}

function productosCaducos(operacionCompleta, pnlMsg) {
	console.log('function productosCaducos(operacionCompleta, pnlMsg)\n------------------------------');
	console.log('operacionCompleta : ' + operacionCompleta);
	console.log('            pnlMsg : ' + pnlMsg);
	
	if (operacionCompleta == false)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('end function productosCaducos\n------------------------------');
}

function generadorDeCodigo(operacionCompleta, pnlMensajes) {
	console.log('------------------------------\nfunction generadorDeCodigo START');
	console.log(' operacionCompleta : ' + operacionCompleta);
	console.log(' pnlMensajes       : ' + pnlMensajes);
	
	if (operacionCompleta == false)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function generadorDeCodigo DONE\n------------------------------');
}