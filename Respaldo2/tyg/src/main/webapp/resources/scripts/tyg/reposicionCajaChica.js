 function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion,msj) {
	
	 
	 
	 if(listErrores.hasChildNodes()){
		return;	
	 }
			
 
	 if(invocacion == 'guardar'){					 
		 if(v_bool == true){
			 if((msj.indexOf('SE CANCELA EL FOLIO ')!= -1)  || (msj.indexOf('SE CANCELAN LOS FOLIOS ')!= -1 )){
				 Richfaces.showModalPanel(pnl1);
			 }
			 else{
				 Richfaces.showModalPanel(pnl2);
			 }
		 }
		 else{
			 if(msj =='BIEN'){
				 Richfaces.showModalPanel(pnl2);
				 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Reposición Caja Chica', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			 }
			 else{
				 Richfaces.showModalPanel(pnl2);
			 }	
		 }	
		 
					
	 }
	 
	 if(invocacion == 'editar'){
		 if(v_bool == true){
			 Richfaces.showModalPanel(pnl1);
		 }
		 else{
			 Richfaces.showModalPanel(pnl2);
		 }		
	 }
	 
	 if(invocacion == 'buscar'){
		 if(v_bool == true){
			 Richfaces.showModalPanel(pnl2);
		 }		 	
	 }
	 
	     if(invocacion == 'guardarFactura'){
			Richfaces.showModalPanel(pnl2);		 
		 }
		 
		 if(invocacion == 'agregar'){
			 if(v_bool == true){
				 Richfaces.showModalPanel(pnl1);
			 }
			 else{
				 Richfaces.showModalPanel(pnl2);
			 }		
		 }
		 
	 if(invocacion == 'eliminar'){
		 if(v_bool == true){
			 Richfaces.showModalPanel(pnl1);
		 }
		 else{
			 Richfaces.showModalPanel(pnl2);
		 }		
	 }	
	 
	 if(invocacion == 'boton'){
		 if(v_bool == true){
			 Richfaces.hideModalPanel(pnl2);
		 }
		 else{
			 if(msj =='BIEN'){
				 Richfaces.hideModalPanel('pnlNuevoEditarCajaChica');
			 }
			 Richfaces.hideModalPanel(pnl1);
			 Richfaces.hideModalPanel(pnl2);
		 }
		
	 }
	 
	 if(invocacion == 'boton2'){			
		 Richfaces.hideModalPanel(pnl1);
		 Richfaces.showModalPanel(pnl2);
		 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Reposición Caja Chica', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
		
	 }
	 	 		 
} 
 
 function checaCancelacion(msg, pnlMsg, pnlError){
	 if(msg == "")
		 Richfaces.hideModalPanel(pnlMsg);
	 else
		 Richfaces.showModalPanel(pnlError);
		 
 }
 
 function detallesCajaChica(url, msg,pnl){
	 if(msg == "")
		 Richfaces.showModalPanel(pnl);
	 else
		 window.open(url);
 }
 function agregaRet(pnl1,pnl2,msj){
		if(msj != 0){
			 Richfaces.showModalPanel(pnl2);
		}
		else{
			 Richfaces.hideModalPanel(pnl1);
		}
}
 
 function imprimeRep(pnlRep){
		javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Reposicion Caja Chica', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	 	Richfaces.hideModalPanel(pnlRep);
	 }