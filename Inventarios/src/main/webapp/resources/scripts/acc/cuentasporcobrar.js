function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion) {
	console.log('function msgRevisando\n------------------------------');
	console.log(listErrores);
	console.log(pnl1);
	console.log(pnl2);
	console.log(v_bool);
	console.log(invocacion);
	
	if(campos_requeridos(listErrores))
		return ;
	
	if(invocacion == 'guardar'){
		console.log('---> on guardar');
		if(v_bool==true){
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		}else{
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.showPopupPanel(pnl2);
		}
	}
		
	if(invocacion == 'buscar'){
		console.log('---> on buscar');
		if(v_bool == true){
			Richfaces.ui.PopupPanel.showPopupPanel(pnl2);
		}		 
	}
	 
	if(invocacion == 'boton'){	
		console.log('---> on boton');
		if(v_bool == true){
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		}
		else{
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);
			RichFaces.ui.PopupPanel.hidePopupPanel(pnl2);
		}		
	}	
}

function campos_requeridos(listErr){
	var mensajes = listErr.getElementsByTagName("*");
	console.log(mensajes);
	return mensajes.length > 1;
}

function salvar(valor, pnlOperacion, pnlMsg, listErrores){
	if(campos_requeridos(listErrores))
		return ;
	
	if (valor == "")
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function salvarCuentaPorCobrar(valor, pnlMsg, listErrores){

	if(campos_requeridos(listErrores))
		return ;
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function salvarParametros(valor, pnlMsg, listErrores){

	if(campos_requeridos(listErrores))
		return ;
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function salvarGral(valor, pnlOperacion, pnlMsg){
	if (valor == "")
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
	
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function buscar(valor, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function buscarProyectos(valor, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function eliminar(valor, pnlOperacion, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else
		RichFaces.ui.PopupPanel.hidePopupPanel(pnlOperacion);
}

function buscar(valor, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}

function imprimeRep(valor, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	else
		 window.open("../../reportes/ReporteGeneric.faces");
}