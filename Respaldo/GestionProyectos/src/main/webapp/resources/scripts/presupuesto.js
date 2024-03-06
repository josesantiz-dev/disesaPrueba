function buscaPresupuestoPorObra(v_bool, pnlPresupuesto, pnlMsg) {
	console.log('function buscaPresupuestoPorObra(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
	console.log('         v_bool: ' + v_bool);
	console.log(' pnlPresupuesto: ' + pnlPresupuesto);
	console.log('         pnlMsg: ' + pnlMsg);
	
	if(v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlPresupuesto);
	}
	
	console.log('end function buscaPresupuestoPorObra DONE\n------------------------------');
}

function verificarCalculoPresupuesto(v_bool, pnlMsg){
	console.log('function verificarCalculoPresupuesto(v_bool, pnlMsg)\n------------------------------');
	console.log(' v_bool: ' + v_bool);
	console.log(' pnlMsg: ' + pnlMsg);
	
	if(v_bool == false)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	
	console.log('end function verificarCalculoPresupuesto DONE\n------------------------------');
}

function analizarExcel(v_bool, pnlUpload, pnlOperacion, pnlMsg) {
	console.log('function analizarExcel(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
	console.log('      v_bool : ' + v_bool);
	console.log('   pnlUpload : ' + pnlUpload);
	console.log('pnlOperacion : ' + pnlOperacion);
	console.log('      pnlMsg : ' + pnlMsg);
	
	if(v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlUpload);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	}
	
	console.log('function analizarExcel DONE\n------------------------------');
}

