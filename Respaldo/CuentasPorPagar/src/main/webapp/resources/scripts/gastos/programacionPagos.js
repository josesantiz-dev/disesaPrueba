function checaResultado(hidden, pnlOperacion, pnlMsg, operacion, listErrores){
	console.log("INICIO DEBUG JS");
	console.log("--------------------------------------------");
	console.log(hidden);
	console.log("pnlOperacion: " + pnlOperacion);
	console.log("pnlMsg: " + pnlMsg);
	console.log("operacion: " + operacion);
	console.log(listErrores);
	
	if(listErrores.hasChildNodes())
		return ;
	
	if (hidden != "")
		Richfaces.showModalPanel(pnlMsg);	
	else if(operacion == "guardar"){
		Richfaces.hideModalPanel(pnlOperacion);
		Richfaces.showModalPanel(pnlMsg);
	}
	else if(operacion == "editar"){		
		Richfaces.showModalPanel(pnlOperacion);
	}
	else if(operacion == "eliminar"){		
		Richfaces.showModalPanel(pnlOperacion);
	}
	
	console.log("--------------------------------------------");
	console.log("FIN DEBUG JS");
}

function printObjectsSelected(output, sgcomponent){
    output.innerHTML=sgcomponent.getSelectedItems().pluck('state');
}

function buscar(v_bool, pnlMsg) {
	console.log('function buscar(v_bool, pnlMsg)\n------------------------------');
	console.log(v_bool);
	console.log(pnlMsg);
	
	if (v_bool == true)
		Richfaces.showModalPanel(pnlMsg);
	console.log('function buscar(v_bool, pnlMsg) DONE\n------------------------------');
}

function seleccionar(v_bool, pnlOperacion, pnlMsg) {
	console.log('function seleccionar\n------------------------------');
	console.log(v_bool);
	console.log(pnlOperacion);
	console.log(pnlMsg);
	
	if (v_bool == true)
		Richfaces.showModalPanel(pnlMsg);
	else 
		Richfaces.hideModalPanel(pnlOperacion);

	console.log('function seleccionar DONE\n------------------------------');
}