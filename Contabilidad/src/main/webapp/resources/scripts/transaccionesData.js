function buscarSucursales(v_bool, pnlMsg) {
	console.log('function buscarSucursales\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarSucursales DONE\n------------------------------');
}