function cargar(v_bool, pnlMsg){
	console.log('function cargar\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else
		initTimer(pnlMsg);
	console.log('function cargar DONE\n------------------------------');
}

function initTimer(pnlMsg) {
	console.log('Starting timer');
	if (window.ocTimer == undefined) {
		window.ocTimer = setInterval(procesamiento, 500);
		window.pnlMsg = pnlMsg;
		console.log('Timer started');
	}
}

function stopTimer() {
	console.log('Stopping timer');
	if (window.ocTimer == undefined || window.ocTimer == '' || window.ocTimer <= -1)
		return;
	
	clearInterval(window.ocTimer);
	window.ocTimer = -1;
	if (window.pnlMsg != undefined) {
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