 function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion,msj) {
	/*
	 alert(listErrores);
	 alert(pnl1);
	 alert(pnl2);
	 alert(v_bool);
	 alert(invocacion);
	 alert(msj);
	 */
	 if(listErrores.hasChildNodes())
			return ;
	 
	 if(invocacion == 'guardar'){		
		 if(v_bool == true){
			 if((msj.indexOf('SE CANCELA EL FOLIO ')!= -1)  || (msj.indexOf('SE CANCELAN LOS FOLIOS ')!= -1 ))
				 Richfaces.showModalPanel(pnl1);
			 else
				 Richfaces.showModalPanel(pnl2);
		 }
		 else{
			 if(msj =='C'){
				 Richfaces.hideModalPanel('pnlNuevoEditarGasto');
				 Richfaces.showModalPanel(pnl2);
				 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			 }
			 else{				
					 Richfaces.showModalPanel(pnl2);
			 }		
		 } 	
	 }
	 else 
		 if(invocacion == 'boton'){
			 if(v_bool == true)
				 Richfaces.hideModalPanel(pnl2);
			 else{
				 if(msj=='C' || msj=='T'){
					 Richfaces.hideModalPanel('pnlNuevoEditarGasto');
					 Richfaces.hideModalPanel(pnl1);
					 Richfaces.hideModalPanel(pnl2);
				 }
				 else if(msj.indexOf('El gasto involucra retenciones, favor de capturalas') != -1 || msj.indexOf('Esta retencion ya ha sido agregada') != -1)
					 Richfaces.hideModalPanel(pnl2);
				 else{
				 Richfaces.hideModalPanel(pnl1);
				 Richfaces.hideModalPanel(pnl2);
				 }
			 }
		 }
		 else 
			 if(invocacion == 'boton2'){
				 Richfaces.hideModalPanel(pnl1);
				 Richfaces.showModalPanel(pnl2);
				 
				 if(msj == 'C')
					 javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
				 
			 }
			 else 
				 if(invocacion == 'eliminarGasto'){
					 if(msj != '')
						 Richfaces.showModalPanel(pnl2);			 
					 else
						 Richfaces.hideModalPanel(pnl1);
				 }
				 else 
					 if(invocacion == 'guardarFactura'){
						 if(msj != '')
							 Richfaces.showModalPanel(pnl2);
						 else{
							 Richfaces.hideModalPanel(pnl1);
							 Richfaces.showModalPanel(pnl2);
						 }			 
					 }		
					 else 
						 if(invocacion == 'reporte'){
							 if(msj != '')
								Richfaces.showModalPanel(pnl2);
							 else
								javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
						 }else 
							 if(invocacion == 'buscar'){
								 if(msj != '')
									Richfaces.showModalPanel(pnl2);
							 }
							 else 
								 if(invocacion == 'cancelar'){
									 if(msj != '')
										Richfaces.showModalPanel(pnl1);
									 else
										Richfaces.showModalPanel(pnl2);
								 }
								 else
									 if(invocacion == 'agregar'){
										 if(msj != '')
											 Richfaces.showModalPanel(pnl1);			 
										 else
											 Richfaces.showModalPanel(pnl2);
									 }
									 else
										 if(invocacion == 'aceptar'){
												 if(msj != ''){
													 Richfaces.showModalPanel(pnl2);
												 }
												 else
													 Richfaces.hideModalPanel(pnl1);
											 }
											 else/*para todo lo demas si estubo bien muestra el panel 2*/	
												 Richfaces.showModalPanel(pnl2);
	 
}
 
 function agregaRet(pnl1,pnl2,msj){
		if(msj != ''){
			 Richfaces.showModalPanel(pnl2);
		}
		else{
			 Richfaces.hideModalPanel(pnl1);
		}
 }
 
 function imprimeRep(pnlRep){
	javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
 	Richfaces.hideModalPanel(pnlRep);
 }
 