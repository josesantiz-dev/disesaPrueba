 function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion,msj) {
	 
	 if(listErrores.hasChildNodes())
			return ;
	 
	 
	 
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
				 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Gastos Comprobar', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			 }
			 else{
				 Richfaces.showModalPanel(pnl2);
			 }	
		 }	
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
	 
	 if(invocacion == 'boton2'){
			
		 if(v_bool == true){
			 Richfaces.hideModalPanel(pnl1);
			 Richfaces.showModalPanel(pnl2);
		 }
		 else{
			 Richfaces.hideModalPanel(pnl1);			
		 }
		
	 }	
	 
	 
	 if(invocacion == 'boton3'){
			
		 Richfaces.hideModalPanel(pnl1);
		 Richfaces.showModalPanel(pnl2);
		 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Gastos Comprobar', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
		
	 }	
	 
	 if(invocacion == 'cancelar'){
		 if(v_bool == true){
			 Richfaces.showModalPanel(pnl1);
		 }
		 else{
			 Richfaces.showModalPanel(pnl2);
		 }		
	 }	 
} 
 
function imprimeRep(){
	 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Gastos Comprobar', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
 }