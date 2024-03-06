function msgRevisando(listErrores,pnl1,pnl2,invocacion) {
	
	if(listErrores.hasChildNodes()){
		if(invocacion = "guardar")
			Richfaces.showModalPanel('pnl2');
		return ;
	}
		
 	 
	 if(invocacion == 'guardar'){
			 Richfaces.showModalPanel(pnl2);
			 Richfaces.hideModalPanel(pnl1);
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

function checaResultado(hidden, pnlConfirmacion,pnlOperacion, pnlMsg){

	if (hidden.value != ""){
		Richfaces.showModalPanel(pnlMsg);
		Richfaces.hideModalPanel(pnlConfirmacion);
	}else{
		Richfaces.showModalPanel(pnlMsg);
		Richfaces.hideModalPanel(pnlConfirmacion);
		Richfaces.hideModalPanel(pnlOperacion);
	}
}