function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion) {
	
	if(listErrores.hasChildNodes())
		return ;
 	 
	 if(invocacion == 'guardar'){

			 Richfaces.showModalPanel(pnl2);
			 Richfaces.hideModalPanel(pnl1);
		
	 }
	 if(invocacion == 'eliminar'){
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

function checaResultado(hidden, pnlOperacion, pnlMsg){

	alert(hidden.value);
	
	if (hidden.value != "")
		Richfaces.showModalPanel(pnlMsg);
	else if(operacion == "eliminar"){
		Richfaces.hideModalPanel(pnlOperacion);
	}
}