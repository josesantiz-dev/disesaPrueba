function buscar(v_bool, pnlMsg){
	
	/*if (v_bool==true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	*/
	
}

function mensaje(v_bool, pnlOperacion, pnlMsg) {
	
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
	console.log('---> v_bool:' + v_bool);
	console.log('function mensaje DONE\n------------------------------');
}

function buscarObras(v_bool, pnlMsg) {
	//console.log('function buscarObras\n------------------------------');
	//console.log(v_bool);
	//console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	//console.log('function buscarObras DONE\n------------------------------');
}
/*
function buscarClientes(v_bool, pnlMsg) {
	console.log('function buscarClientes\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarClientes DONE\n------------------------------');
}

function salvar(v_bool, pnlOperacion, pnlMsg, listErrores){
	console.log('function salvar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> on campos_requeridos');
		return;
	}
	
	if (! v_bool) {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	}
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('---> v_bool:' + v_bool);
	console.log('function salvar DONE\n------------------------------');
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

function agregarEmpleado(v_bool, pnlOperacion, pnlMsg) {
	console.log('function agregarEmpleado\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
		return;
	}

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function agregarEmpleado DONE\n------------------------------');
}*/