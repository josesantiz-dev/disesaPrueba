function comprobarAlmacenObra(v_bool, pnlMsg) {
	console.log('function comprobarAlmacenObra\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if(v_bool == true){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}
	
	console.log('function comprobarAlmacenObra DONE\n------------------------------');
}


function salvarObraAlmacen(v_bool, tipo_mensaje, pnlOperacion, pnlConfirmar, pnlMensaje, listErrores) {
	console.log('function salvar\n------------------------------');
	console.log('      v_bool: ' + v_bool);
	console.log('pnlOperacion: ' + pnlOperacion);
	console.log('pnlConfirmar: ' + pnlConfirmar);
	console.log('  pnlMensaje: ' + pnlMensaje);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> exit on campos_requeridos');
		return;
	}
	
	if (v_bool == true) {
		if (tipo_mensaje == 16)
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmar);
		else
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
	}
	
	console.log('function salvar DONE\n------------------------------');
}