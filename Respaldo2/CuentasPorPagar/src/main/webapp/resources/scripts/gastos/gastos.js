function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion) {
	
	if(listErrores.hasChildNodes()){
		if(operacion = "guardar")
			Richfaces.showModalPanel('pnlDatFaltantesGastos');
		return ;
	}
		
 	 
	 if(invocacion == 'guardar'){
			 Richfaces.showModalPanel(pnl2);
	 }
	 
	 if(invocacion == 'buscar'){
		 if(v_bool == true){
			 Richfaces.showModalPanel(pnl2);
		 }		 
	 }
	 
	 if(invocacion == 'boton'){	
		 
		 if(v_bool == true){
			 Richfaces.hideModalPanel(pnl2);
		 }
		 else{
			 Richfaces.hideModalPanel(pnl1);
			 Richfaces.hideModalPanel(pnl2);
		 }		
	 }		
}

function salvar(operacionCancelada, pnlOperacion, pnlMensajes, listErrores) {
	console.log('\nfunction salvar------------------------------\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlOperacion       : ' + pnlOperacion);
	console.log('pnlMensajes        : ' + pnlMensajes);
	console.log('validador          : ' + listErrores);

	console.log('\nValidando requeridos');
	if(campos_requeridos(listErrores)) {
		console.log('function salvar REQUERIDOS\n------------------------------');
		return;
	}
	
	if (operacionCancelada == false)
		Richfaces.hideModalPanel(pnlOperacion);
	Richfaces.showModalPanel(pnlMensajes);
	console.log('function salvar DONE\n------------------------------');
}

function editar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\nfunction editar\n------------------------------\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlOperacion       : ' + pnlOperacion);
	console.log('pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	else
		Richfaces.showModalPanel(pnlOperacion);
	console.log('function editar DONE\n------------------------------');
}

function eliminar(operacionCancelada, pnlOperacion, pnlMensajes) {
	console.log('\nfunction eliminar\n------------------------------\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlOperacion       : ' + pnlOperacion);
	console.log('pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	Richfaces.hideModalPanel(pnlOperacion);
	console.log('function eliminar DONE\n------------------------------');
}

function mensaje(operacionCancelada, pnlMensajes) {
	console.log('\nfunction mensaje\n------------------------------\n');
	console.log('operacionCancelada : ' + operacionCancelada);
	console.log('pnlMensajes        : ' + pnlMensajes);
	
	if (operacionCancelada == true)
		Richfaces.showModalPanel(pnlMensajes);
	console.log('function mensaje DONE\n------------------------------');
}