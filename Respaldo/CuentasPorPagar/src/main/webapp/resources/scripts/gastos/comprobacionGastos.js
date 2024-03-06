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
	 if(v_bool == true) {
		 Richfaces.showModalPanel(pnl);
	 } else {
		 window.open(url);
	 }
 }
 
 function imprimeReporte(v_bool, pnl, url, title) {
	 if (v_bool == true) {
		 Richfaces.showModalPanel(pnl);
		 return;
	 }
	 
	 // Abrimos ventana
	 window.open(url, title, 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
 }
 
 function agregaRet(pnl1,pnl2,msj) {
	if(msj != 0) {
		 Richfaces.showModalPanel(pnl2);
	} else {
		 Richfaces.hideModalPanel(pnl1);
	}
}
 
 function analizarArchivo(v_bool, pnlUpload, pnlMsg){
	console.log('function analizarArchivo(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
	console.log('    v_bool: ' + v_bool);
	console.log(' pnlUpload: ' + pnlUpload);
	console.log('    pnlMsg: ' + pnlMsg);
	
	if(v_bool == true) {
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	} else {
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlUpload);
	}
	
	console.log('function analizarArchivo DONE\n------------------------------');
}

function descargar(v_bool, modulo, pnlMsg) {
	console.log('descargar(v_bool, modulo, pnlMsg)\n------------------------------');
 	console.log('v_bool : ' + v_bool);
 	console.log('modulo : ' + modulo);
 	console.log('pnlMsg : ' + pnlMsg);
 	
 	if (v_bool == true) {
 		RichFaces.showModalPanel(pnlMsg);
 		return;
 	}
 	
 	window.open(modulo + '/reportes/ReporteGeneric.faces', 'Satic', 'menubar=0, toolbar=0, scrollbars=1,location=0, status=1');
 	console.log('end function descargar\n------------------------------');
}
