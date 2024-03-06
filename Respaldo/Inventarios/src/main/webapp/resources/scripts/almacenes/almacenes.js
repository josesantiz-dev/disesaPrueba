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
