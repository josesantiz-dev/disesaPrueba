 function msgRevisando(listErrores,pnl1,pnl2,invocacion,msj) {
	 if(listErrores.hasChildNodes())
			return ;
	 
	 if(invocacion == 'guardar'){		
		 if(msj != ''){
			 if(msj =='C'){
				 Richfaces.hideModalPanel(pnl1);
				 Richfaces.hideModalPanel('pnlBuscarFact');
				 Richfaces.showModalPanel(pnl2);
				 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Pago Provision Factura', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			 }
			 else{
				 Richfaces.showModalPanel(pnl2);
			 }
		 }
		 else{
			 Richfaces.hideModalPanel(pnl1);
			 Richfaces.hideModalPanel('pnlBuscarFact');
			 Richfaces.showModalPanel(pnl2);
		 }
	 }
	 else if(invocacion == 'buscar'){
			 if(msj != ''){
				Richfaces.showModalPanel(pnl2);
			 }
	}
	 else if(invocacion == 'eliminar'){
		 if(msj != ''){
			 Richfaces.showModalPanel(pnl2);
			 Richfaces.hideModalPanel(pnl1);
		 }
		 else
			 Richfaces.hideModalPanel(pnl1);	
	 }
	 else if(invocacion == 'seleccionar'){
			 if(msj != '')
				 Richfaces.showModalPanel(pnl2);				
	 }
	 else if(invocacion == 'aplicar'){
			 if(msj != '')
				 Richfaces.showModalPanel(pnl2);	
			 else
				 Richfaces.showModalPanel(pnl1);	
	 }
	 else if(invocacion == 'cancelaCheque'){
		 Richfaces.hideModalPanel(pnl1);
		 Richfaces.showModalPanel(pnl2);
		 
		 if(msj == 'C')
			 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Pago Provision Factura', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
	 }				
} 
 
 function imprimeRep(){
	 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Pago Provision Factura', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
 }