function buscar(valor, pnlMsg){
	if (valor != ""){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
	}		
}

function validaGuardarProducto(operacionCompleta, pnlActivo, pnlMensajes, btnSalvar){
	
	if(operacionCompleta==false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
		$(btnSalvar).prop('disabled',false);	//rehabilitar el boton si no se pudo guardar
		return;
	};
	//#{rich:element('cmdSalvar')}.disabled = true;
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlActivo);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlMensajes);
	
	
}

/*
function validarDatosContrato(pnlActivo, pnlConfirmacion, mensaje, pnlErrorContrato, bandera){
	
	if(bandera==false){
		RichFaces.ui.PopupPanel.showPopupPanel(pnlErrorContrato);
		return;
	}
	
	RichFaces.ui.PopupPanel.hidePopupPanel(pnlActivo);
	RichFaces.ui.PopupPanel.showPopupPanel(pnlConfirmacion);
	

}

function validarBeneficiario(band, resultado, pnl1, pnlErrores){
	
	if(band == true){
		RichFaces.ui.PopupPanel.hidePopupPanel(pnl1);	//ocultamos el panel si la validación fue correcta
		//RichFaces.ui.PopupPanel.showPopupPanel(pnl1);	//ocultamos el panel si la validación fue correcta
	}else{
		//alert(resultado);
		RichFaces.ui.PopupPanel.showPopupPanel(pnlErrores);
	}
	
}

function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion, resultado) {
	
	if(campos_requeridos(listErrores))
		return ;
	
	if(invocacion == 'guardar'){
		console.log('---> on guardar');
		if(v_bool==false){
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


function buscarPersonas(valor, pnlMsg){
	if (valor != "")
		RichFaces.ui.PopupPanel.showPopupPanel(pnlMsg);
}*/