
function busquedaDinamica(pnl) {
	RichFaces.ui.PopupPanel.showPopupPanel(pnl);
}

function seleccionar(v_bool, pnlOperacion, pnlMsg) {
	console.log('function seleccionar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else 
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);

	console.log('function seleccionar DONE\n------------------------------');
}
