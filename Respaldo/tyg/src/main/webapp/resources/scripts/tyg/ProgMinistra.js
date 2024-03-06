function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores){

	if(listErrores.hasChildNodes())
		return ;
	
	if (hidden.value != "")
		Richfaces.showModalPanel(pnlMsg);
	else if(operacion == "agregar")
		Richfaces.showModalPanel(pnlOperacion);
	else if(operacion == "agregarSol"){
		Richfaces.showModalPanel(pnlOperacion);
	}
	else if(operacion == "agregarSolicitud"){
		Richfaces.hideModalPanel(pnlOperacion);
	}
	else if (operacion == "editar"){
		Richfaces.showModalPanel(pnlOperacion);
	}
	else if (operacion == "buscarSolicitud"){
		Richfaces.showModalPanel(pnlOperacion);
	}
	else if (operacion == "cancelar"){
		Richfaces.hideModalPanel(pnlOperacion);
	}
	else if(operacion == "guardar"){
		Richfaces.hideModalPanel(pnlOperacion);
		Richfaces.showModalPanel(pnlMsg);
	}
	else if(operacion == "eliminar")
		Richfaces.showModalPanel(pnlMsg);		
	else if(operacion == "msgEliminar")
		Richfaces.hideModalPanel(pnlOperacion);		
}

function msgCancelar(estatus, pnlOp, pnlMsg){
	if(estatus.value != '')
		Richfaces.showModalPanel(pnlMsg);
	else
		Richfaces.showModalPanel(pnlOp);
}