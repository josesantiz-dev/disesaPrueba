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