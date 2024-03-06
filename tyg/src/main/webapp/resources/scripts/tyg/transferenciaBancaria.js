 function msgMonto(id, action, listErrores) {
	 if (action == "onblur")
	 {
		 if (id.value == "Monto Invalido")
		 	Richfaces.showModalPanel('pnlMsg');
	 }
	 
	 if(listErrores.hasChildNodes())
		return ;
	 
	 if(action == "guardar")
		 Richfaces.showModalPanel('pnlMsg');
	 if (action == "buscar")
	 {
		 if (id.value != "")
			 Richfaces.showModalPanel('pnlMsg');
	 }
			 		 
} 
 
 function msgGuardar(id, action, listErrores,pnl) {
	 if(listErrores.hasChildNodes())
		return ;
	 
	 if(action == "guardar")
		 Richfaces.showModalPanel('pnlMsg');
	 	 Richfaces.hideModalPanel(pnl);
	
} 
 
 
 function busqueda(mensaje, pnl){
	 if(mensaje.value != "")
		Richfaces.showModalPanel(pnl);
 }
 
 
 