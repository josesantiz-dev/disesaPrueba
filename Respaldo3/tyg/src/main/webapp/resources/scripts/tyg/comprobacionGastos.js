function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion) {
	 
	 if(listErrores.hasChildNodes())
			return ;
	 	 
	 if(invocacion == 'guardar'){
			 Richfaces.showModalPanel(pnl2);
	 }
	 
	 if(invocacion == 'buscar'){
		 if(v_bool == true){
			 Richfaces.showModalPanel(pnl2);
		 }		
	 }
	 
	 if(invocacion == 'comprobar'){
		 if(v_bool == true){
			 Richfaces.showModalPanel(pnl2);
		 }
		 else{
			 Richfaces.showModalPanel(pnl1);
		 }
	 }
	 
	 if(invocacion == 'finalizar'){
		 if(v_bool == true){
			 Richfaces.showModalPanel(pnl2);
		 }
		 else{
			 Richfaces.showModalPanel(pnl1);
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

 function finaliza(pnlCerrar, pnlAbrir, url){
	 Richfaces.hideModalPanel(pnlCerrar);
	 Richfaces.showModalPanel(pnlAbrir);
	 window.open(url);
 }
 
 
 function detallesComprobacion(url,v_bool,pnl){
	 if(v_bool == true){
		 Richfaces.showModalPanel(pnl);
	 }
	 else{
		 window.open(url);
	 }
	
 }
 
 function agregaRet(pnl1,pnl2,msj){
		if(msj != 0){
			 Richfaces.showModalPanel(pnl2);
		}
		else{
			 Richfaces.hideModalPanel(pnl1);
		}
}