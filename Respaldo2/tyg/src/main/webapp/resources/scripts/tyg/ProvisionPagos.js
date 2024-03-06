function guardar(errores,pnl1,pnl2,msj){
	 if(errores.hasChildNodes())
			return ;
	 if(msj==''){
		 Richfaces.hideModalPanel(pnl1);
		 Richfaces.showModalPanel(pnl2);
	 }
	 else{
		 Richfaces.showModalPanel(pnl2);
	 }
}

function eliminar(pnl1,pnl2,msj){
	 if(msj != ''){
		 Richfaces.hideModalPanel(pnl1);
		 Richfaces.showModalPanel(pnl2);
	 }	 	 
	 else{
		 Richfaces.hideModalPanel(pnl1);
	 }
}

function buscar(errores,pnl1,msj){
	 if(errores.hasChildNodes())
		return ;
	 if(msj != '')
		Richfaces.showModalPanel(pnl1);
}

function cancelar(pnl1,pnl2,msj){
	 if(msj != '')
			Richfaces.showModalPanel(pnl1);
		 else
			Richfaces.showModalPanel(pnl2);
}

function agregar(pnl1,pnl2,msj){
	 if(msj != '')
		 Richfaces.showModalPanel(pnl2);			 
	 else
		 Richfaces.showModalPanel(pnl1);
}

function guardarFactura(errores,pnl1,pnl2,msj){
	if(errores.hasChildNodes())
		return ;
	 if(msj != '')
		 Richfaces.showModalPanel(pnl2);
	 else{
		 Richfaces.hideModalPanel(pnl1);
		 Richfaces.showModalPanel(pnl2);
	 }			 
}

function eliminarFactura(pnl1,pnl2,msj){
	 if(msj != '')
		 Richfaces.showModalPanel(pnl2);			 
	 else
		 Richfaces.hideModalPanel(pnl1);
}

function editar(pnl1,pnl2,msj){
	 if(msj != '')
		 Richfaces.showModalPanel(pnl1);
	 else
		 Richfaces.showModalPanel(pnl2);
}


