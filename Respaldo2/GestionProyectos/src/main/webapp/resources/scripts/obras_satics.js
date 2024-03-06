function nuevaSatic(v_bool, pnlOperacion, pnlMensaje) {
	console.log('function nuevaSatic(v_bool, pnlOperacion, pnlMensaje)\n------------------------------');
	console.log('v_bool       : ' + v_bool);
	console.log('pnlOperacion : ' + pnlOperacion);
	console.log('pnlMensaje   : ' + pnlMensaje);
	
	if(v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensaje);
		return;
	}

	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	console.log('function nuevaSatic DONE\n------------------------------');
}

function analizarArchivo(v_bool, pnlUpload, pnlMsg){
	console.log('function analizarArchivo(v_bool, pnlUpload, pnlMsg)\n------------------------------');
	console.log('v_bool       : ' + v_bool);
	console.log('pnlUpload    : ' + pnlUpload);
	console.log('pnlMsg       : ' + pnlMsg);
	
	if(v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlUpload);
	console.log('function analizarArchivo DONE\n------------------------------');
}

function archivoSatic(v_bool, modulo, pnlMsg) {
	console.log('archivoSatic(v_bool, modulo, pnlMsg)\n------------------------------');
	console.log('v_bool : ' + v_bool);
	console.log('modulo : ' + modulo);
	console.log('pnlMsg : ' + pnlMsg);
	
	if (v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
		return;
	}
	
	window.open(modulo + '/reportes/ReporteGeneric.faces', 'Satic', 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
	console.log('end function archivoSatic\n------------------------------');
}