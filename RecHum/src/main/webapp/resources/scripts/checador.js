/*function buscar(operacionCancelada, pnlMensajes){
	console.log('function buscar\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function buscar DONE\n------------------------------');
}*/

function controlFocus(itemFocus) {
	console.log('controlFocus:' + itemFocus);
	if (itemFocus !== null && itemFocus != undefined) {
		window.itemFocus = itemFocus;
		console.log(itemFocus + ': INSERTED!');
	} else if (window.itemFocus != undefined) {
		document.getElementById(window.itemFocus).focus();
		console.log(window.itemFocus + ': DROPIT!');
		window.itemFocus = undefined;
	} else {
		console.log('Do Nothing!');
	}
}

function buscarObras(operacionCancelada, pnlMensajes, itemFocus) {
	console.log('function buscarObras\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true) 
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	console.log('function buscarObras DONE\n------------------------------');
}

function salvarAsistencia(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, listErrores){
	console.log('function salvarAsistencia(operacionCancelada, tipoMensaje, pnlOperacion, pnlMensajes, listErrores)\n------------------------------');
	console.log(operacionCancelada);
	console.log(tipoMensaje);
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	console.log(listErrores);
	
	if(campos_requeridos(listErrores)){
		console.log('---> exit on campos_requeridos');
		return;
	}
	
	if (operacionCancelada == true) {
		if (tipoMensaje == 7) 
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
		else
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	}

	console.log('exit function\n------------------------------');
}

function eliminar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('function eliminar\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	console.log('function eliminar DONE\n------------------------------');
}

function mensaje(operacionCancelada, pnlMensajes) {
	console.log('function mensaje\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlMensajes);
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMensajes);
	
	console.log('function mensaje DONE\n------------------------------');
}

function mensajeDetalles(pnlOperacion, pnlMensajes) {
	console.log('function mensajeDetalles\n------------------------------');
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlMensajes);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
	
	console.log('function mensajeDetalles DONE\n------------------------------');
}

function seleccionar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('function seleccionar\n------------------------------');
	console.log(operacionCancelada);
	console.log(pnlOperacion);
	console.log(pnlMensajes);
	
	if (operacionCancelada == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
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
	console.log('listErr:', mensajes.length);
	console.log(mensajes);
	
	return mensajes.length > 1;
}*/

function analizarArchivo(operacionCancelada, tipoMensaje, pnlUpload, pnlOperacion, pnlMensajes){
	console.log('function analizarArchivo(operacionCancelada, pnlOperacion, pnlMensajes)\n------------------------------');
	console.log('  operacionCancelada :', operacionCancelada);
	console.log('  tipoMensaje        :', tipoMensaje);
	console.log('  pnlUpload          :', pnlUpload);
	console.log('  pnlOperacion       :', pnlOperacion);
	console.log('  pnlMensajes        :', pnlMensajes);
	
	if (operacionCancelada == true) {
		console.log('function analizarArchivo ERROR\n------------------------------');
		if (tipoMensaje == 7)
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		/*if (tipoMensaje == 7) {
			RichFaces.ui.PopupPanel.hidePopupPanel(pnlUpload);
			RichFaces.ui.PopupPanel.showPopupPanel(pnlOperacion);
		} else {
			RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		}*/
		return;
	} 

	RichFaces.ui.PopupPanel.hidePopupPanel(pnlUpload);
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