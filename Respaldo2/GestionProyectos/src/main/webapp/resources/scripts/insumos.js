function buscarObras(v_bool, pnlMsg) {
	console.log('function buscarObras\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarObras DONE\n------------------------------');
}

function analizarExcel(v_bool, tipoMsg, pnlUpload, pnlOperacion, pnlMsg, pnlProds) {
	console.log('function analizarExcel(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
	console.log('      v_bool : ' + v_bool);
	console.log('   pnlUpload : ' + pnlUpload);
	console.log('pnlOperacion : ' + pnlOperacion);
	console.log('      pnlMsg : ' + pnlMsg);
	console.log('    pnlProds : ' + pnlProds);
	
	if(v_bool == true) {
		if (tipoMsg == -1)
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
		else
			RichFaces.ui.PopupPanel.showPopupPanel(pnlProds);
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlUpload);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	}
	console.log('function analizarExcel DONE\n------------------------------');
}

function mensaje(v_bool, pnlOperacion, pnlMsg) {
	console.log('function mensaje\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	/*console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> exit on campos_requeridos');
		return;
	}*/
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
	console.log('function mensaje DONE\n------------------------------');
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

function seleccionarProducto(operacionCancelada, pnlOperacion, pnlMensajes, listErrores) {
	console.log('function seleccionarProducto\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlOperacion);
	console.log(pnlMensajes);

	console.log('---> Comprobando requeridos ... ');
	if(campos_requeridos(listErrores)) {
		console.log('---> exit on campos_requeridos');
		return;
	}

	console.log('---> requeridos OK');
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	else 
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	console.log('function seleccionarProducto DONE\n------------------------------');
}