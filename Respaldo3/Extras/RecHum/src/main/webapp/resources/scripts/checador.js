/*function buscar(v_bool, pnlMsg){
	console.log('function buscar\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscar DONE\n------------------------------');
}*/

function buscarObras(v_bool, pnlMsg) {
	console.log('function buscarObras\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	console.log('function buscarObras DONE\n------------------------------');
}

function salvarAsistencia(v_bool, tipoMensaje, pnlOperacion, pnlMsg, listErrores){
	console.log('function salvarAsistencia(v_bool, tipoMensaje, pnlOperacion, pnlMsg, listErrores)\n------------------------------');
	console.log(v_bool);
	console.log(tipoMensaje);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> exit on campos_requeridos');
		return;
	}
	
	if (v_bool == true) {
		if (tipoMensaje == 7) 
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
		else
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}

	console.log('exit function\n------------------------------');
}

function eliminar(v_bool, pnlOperacion, pnlMsg) {
	console.log('function eliminar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	console.log('function eliminar DONE\n------------------------------');
}

function mensaje(v_bool, pnlMsg) {
	console.log('function mensaje\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
	
	console.log('function mensaje DONE\n------------------------------');
}

function mensajeDetalles(pnlOperacion, pnlMsg) {
	console.log('function mensajeDetalles\n------------------------------');
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMsg);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	
	console.log('function mensajeDetalles DONE\n------------------------------');
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
/*
function campos_requeridos(listErr){
	console.log('function campos_requeridos\n------------------------------');
	console.log('listErr: \n');
	console.log(listErr);
	console.log('**********: \n');
	
	var mensajes = listErr.getElementsByTagName("*");
	console.log('listErr: ' + mensajes.length);
	console.log(mensajes);
	
	return mensajes.length > 1;
}*/

function analizarArchivo(v_bool, tipoMensaje, pnlUpload, pnlOperacion, pnlMsg){
	console.log('function analizarArchivo(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
	console.log('v_bool       : ' + v_bool);
	console.log('tipoMensaje  : ' + tipoMensaje);
	console.log('pnlUpload    : ' + pnlUpload);
	console.log('pnlOperacion : ' + pnlOperacion);
	console.log('pnlMsg       : ' + pnlMsg);
	
	if(v_bool == true) {
		if (tipoMensaje == 7) {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnlUpload);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
		} else {
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
		}
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlUpload);
	}
	
	console.log('function analizarArchivo DONE\n------------------------------');
}

function getNewValue(itemId) {
	var item = null;
	var value = '00:00';
	
	if (itemId == '' || itemId == null || itemId == undefined) {
		console.log("ID not exist\n------------------------------");
		return "";
	}
	
	console.log("From getNewValue\n------------------------------\nGetting value for id: " + itemId);
	item = document.getElementById(itemId);
	if (item == null || item == undefined) {
		console.log("Item not found: " + itemId + "\n------------------------------");
		return value;
	}
	
	console.log("Returning value: " + item.value + "\n------------------------------");
	return itemId + "-" + item.value;
}