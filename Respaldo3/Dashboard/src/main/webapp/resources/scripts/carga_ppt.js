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
	if (window.pptTimer == undefined || window.pptTimer == '' || window.pptTimer <= -1) {
		window.pptTimer = setInterval(procesamientoPPT, 500);
		window.pptPnlMsg = pnlMsg;
		console.log('Timer started');
	}
}

function stopTimer() {
	console.log('Stopping timer');
	if (window.pptTimer == undefined || window.pptTimer == '' || window.pptTimer <= -1) {
		console.log('No timer to stop');
		return;
	}
	
	clearInterval(window.pptTimer);
	window.pptTimer = -1;
	if (window.pptPnlMsg != undefined) {
		console.log('Show popup');
		RichFaces.ui.PopupPanel.showPopupPanel(window.pptPnlMsg);
		window.pptPnlMsg = undefined;
	}
	console.log('Timer stoped');
}

function reporta(procesando) {
	console.log(procesando);
	if( (typeof procesando === "object") && (procesando !== null) ) {
		procesando = procesando.data;
		console.log(procesando);
	}
	
	if (procesando == false) {
		console.log('Reporta lanzado | procesando: ' + procesando + ' | do Stop Timer');
		stopTimer();
	} else {
		console.log('Reporta lanzado | procesando: ' + procesando + ' | do nothing');
	}
}