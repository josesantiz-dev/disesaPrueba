function cargar(v_bool, pnlMsg){
	console.log('function cargar\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true) {
		console.log('Ocurrio un problema, mostramos Popup');
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else {
		console.log('Iniciamos timer');
		initTimer(pnlMsg);
	}
	console.log('function cargar DONE\n------------------------------');
}

function initTimer(pnlMsg) {
	console.log('Starting timer');
	if (window.ocTimer == undefined || window.ocTimer == '' || window.ocTimer <= -1) {
		window.ocTimer = setInterval(procesamiento, 500);
		window.pnlMsg = pnlMsg;
		console.log('Timer started');
	}
}

function stopTimer() {
	console.log('Stopping timer');
	if (window.ocTimer == undefined || window.ocTimer == '' || window.ocTimer <= -1) {
		console.log('No timer to stop');
		return;
	}
	
	clearInterval(window.ocTimer);
	window.ocTimer = -1;
	if (window.pnlMsg != undefined) {
		console.log('Show popup');
		RichFaces.ui.PopupPanel.showPopupPanel(window.pnlMsg);
		window.pnlMsg = undefined;
	}
	console.log('Timer stoped');
}

function reporta(procesando) {
	if (procesando == false) {
		console.log('Reporta lanzado | procesando: ' + procesando + ' | do Stop Timer');
		stopTimer();
	} else {
		console.log('Reporta lanzado | procesando: ' + procesando + ' | do nothing');
	}
}