  function msgRevisando(listErrores,pnl1,pnl2,invocacion,msj) {
	  
	 if(listErrores.hasChildNodes())
			return ;
	 
	 if (msj.value != ""){
		 if(invocacion == 'guardar'){		
			 if (msj.value =='C'){
				 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Generacion Reembolso', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			 	 Richfaces.showModalPanel(pnl2);
			 }
			 else if ((msj.value.indexOf('SE CANCELA EL FOLIO ')!= -1)  || (msj.value.indexOf('SE CANCELAN LOS FOLIOS ')!= -1 )){
					 Richfaces.showModalPanel(pnl1);
				 }
				 else{
					 Richfaces.showModalPanel(pnl2);				 
					 
				 }	 
		 }
		 if(invocacion == 'buscar'){
			 Richfaces.showModalPanel(pnl2);
		 }
	 }
	 else if (invocacion == 'guardar'){
		 Richfaces.showModalPanel(pnl2);
	 }
	
} 
 