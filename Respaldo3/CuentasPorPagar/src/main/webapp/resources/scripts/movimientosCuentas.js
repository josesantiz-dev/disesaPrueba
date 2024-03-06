function eliminar(pnlOp, pnlMsg, valor){
	if(valor!='')
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.hideModalPanel(pnlOp);
}

function edita(pnlOp, pnlMsg, valor, mens, listErrores){
	if(listErrores.hasChildNodes())
		return ;
	if(valor == '')
		Richfaces.hideModalPanel(pnlOp);
	else
		Richfaces.showModalPanel(pnlMsg);
	if (mens != '')
		Richfaces.hideModalPanel(pnlOp);
}

function buscar(pnlMsg, valor){
	if(valor!='')
		Richfaces.showModalPanel(pnlMsg);
}

function nuevoimp(pnlMsg, valor){
	if(valor!='')
		Richfaces.showModalPanel(pnlMsg);
}

function nuevo(pnlOp, pnlMsg, valor){
	if(valor!='')
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.showModalPanel(pnlOp);
}

function msgRevisando(listErrores, pnl1, pnl2, v_bool, invocacion) {
	console.log('function msgRevisando(listErrores, pnl1, pnl2, v_bool, invocacion)\n------------------------------');
	console.log('    v_bool: ' + v_bool);
	console.log('      pnl1: ' + pnl1);
	console.log('      pnl2: ' + pnl2);
	console.log('invocacion: ' + invocacion);
	console.log(listErrores);
	
	if(listErrores.hasChildNodes()) {
		console.log('---> exit on listErrores');
		return;
	}

	if(invocacion == 'guardar') {
		if (v_bool == false)
			Richfaces.hideModalPanel(pnl1);
		Richfaces.showModalPanel(pnl2);
	}

	if(invocacion == 'buscar'){
		if(v_bool == true)
			Richfaces.showModalPanel(pnl2);
	}

	if(invocacion == 'boton') {
		if(v_bool == true) {
			Richfaces.hideModalPanel(pnl2);
		} else {
			Richfaces.hideModalPanel(pnl1);
			Richfaces.hideModalPanel(pnl2);
		}
	}	

	if(invocacion == 'boton2') {
		if(v_bool == true){
			Richfaces.hideModalPanel(pnl1);
			Richfaces.showModalPanel(pnl2);
		} else {
			Richfaces.hideModalPanel(pnl1);
		}
	}	

	if(invocacion == 'cancelar') {
		if(v_bool == true)
			Richfaces.showModalPanel(pnl1);
		else
			Richfaces.showModalPanel(pnl2);
	}
	
	console.log('function msgRevisando(listErrores, pnl1, pnl2, v_bool, invocacion) DONE\n------------------------------');
}