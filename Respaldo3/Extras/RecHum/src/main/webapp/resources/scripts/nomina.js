function salvarNomina(v_bool, tipoMensaje, pnlOperacion, pnlMsg, pnlConfirma, listErrores){
	console.log('function salvarNomina(v_bool, mensaje, pnlOperacion, pnlMsg, pnlConfirma, listErrores)\n------------------------------');
	console.log('      v_bool: ' + v_bool);
	console.log(' tipoMensaje: ' + tipoMensaje);
	console.log('pnlOperacion: ' + pnlOperacion);
	console.log('      pnlMsg: ' + pnlMsg);
	console.log(' pnlConfirma: ' + pnlConfirma);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> exit function salvarNomina on campos_requeridos');
		return;
	}
	
	if (v_bool == true) {
		if (tipoMensaje == 6)
			RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirma);
		else
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
		
	} else {
		if (pnlOperacion != '') {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
		} else {
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
		}
	}

	console.log('end function salvarNomina\n------------------------------');
}