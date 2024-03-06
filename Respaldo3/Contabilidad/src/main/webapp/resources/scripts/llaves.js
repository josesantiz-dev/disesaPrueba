function comprobarPosicion(v_bool, pnlMsg) {
	console.log('function comprobarPosicion(v_bool, pnlMsg)\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('-------> comprobarPosicion(v_bool, pnlMsg) DONE\n------------------------------');
}