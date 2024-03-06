 function msgRevisando(listErrores,pnl1,pnl2,v_bool,invocacion,msj) {
	 console.log('function msgRevisando START\n-------------------------------'); // aceptar
	 console.log('invocacion : ' + invocacion); // aceptar
	 console.log('    v_bool : ' + v_bool);
	 console.log('      pnl1 : ' + pnl1);
	 console.log('      pnl2 : ' + pnl2);
	 console.log('       msj : ' + msj); 
	 //console.log(listErrores);
	 
	if (invocacion == 'agregarObra') {
		//console.log('Richfaces.showModalPanel("' + pnl2 + '")');
		Richfaces.showModalPanel(pnl2);
		 console.log('function agregarObra DONE\n-------------------------------'); // aceptar
		return;
	} 

	if(invocacion == 'buscarObras'){
		//console.log('Richfaces.showModalPanel("' + pnl1 + '")');
		if (v_bool == true)
			Richfaces.showModalPanel(pnl2);
		 console.log('function buscarObra DONE\n-------------------------------'); // aceptar
		return; 
	}
	
	if(invocacion == 'agregarOrdenCompra'){
		nuevaBusqueda(v_bool, pnl2, pnl1);
		 console.log('function agregarOrdenCompra DONE\n-------------------------------'); // aceptar
		return; 
	}
	
	 if(listErrores.hasChildNodes()) {
		 console.log('function REQUERIDOS DONE\n-------------------------------'); // aceptar
		 return;
	 }
	 
	 if(invocacion == 'guardar'){		
		 if(v_bool == true){
			 if((msj.indexOf('SE CANCELA EL FOLIO ')!= -1)  || (msj.indexOf('SE CANCELAN LOS FOLIOS ')!= -1 ))
				 Richfaces.showModalPanel(pnl1);
			 else
				 Richfaces.showModalPanel(pnl2);
		 } else {
			 if(msj =='C'){
				 Richfaces.hideModalPanel('pnlNuevoEditarGasto');
				 Richfaces.showModalPanel(pnl2);
				 //javascript:window.open('/VIEWConsol/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			 } else {
				 Richfaces.showModalPanel(pnl2);
			 }
		 }
	} else 
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
		 } else 
			 if(invocacion == 'boton2'){
				 Richfaces.hideModalPanel(pnl1);
				 Richfaces.showModalPanel(pnl2);
				 
				 if(msj == 'C')
					 javascript:window.open('/reportes/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
			 } else 
				 if(invocacion == 'eliminarGasto'){
					 if(msj != '')
						 Richfaces.showModalPanel(pnl2);			 
					 else
						 Richfaces.hideModalPanel(pnl1);
				 } else 
					 if(invocacion == 'guardarFactura'){
						 if(msj != '')
							 Richfaces.showModalPanel(pnl2);
						 else{
							 Richfaces.hideModalPanel(pnl1);
							 Richfaces.showModalPanel(pnl2);
						 }			 
					 } else 
						 if(invocacion == 'reporte'){
							 if(msj != '')
								Richfaces.showModalPanel(pnl2);
							 else
								javascript:window.open('/reportes/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
						 } else 
							 if(invocacion == 'buscar'){
								 if(msj != '') {
									Richfaces.showModalPanel(pnl2);
								}
							 } else 
								 if(invocacion == 'cancelar'){
									 if(msj != '')
										Richfaces.showModalPanel(pnl1);
									 else
										Richfaces.showModalPanel(pnl2);
								 } else
									 if(invocacion == 'agregar'){
										 if(msj != '')
											 Richfaces.showModalPanel(pnl1);			 
										 else
											 Richfaces.showModalPanel(pnl2);
									 } else
										 if(invocacion == 'aceptar'){
												 if(msj != ''){
													 Richfaces.showModalPanel(pnl2);
												 } else
													 Richfaces.hideModalPanel(pnl1);
											 } else/*para todo lo demas si estubo bien muestra el panel 2*/	
												 Richfaces.showModalPanel(pnl2);
	 
}
 
 function agregaRet(pnl1,pnl2,msj){
	if(msj != ''){
		Richfaces.showModalPanel(pnl2);
	} else{
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
 
 function imprimeRep(pnlRep){
	javascript:window.open('/reportes/ReporteGeneric.faces', 'Registro Gastos', 'menubar=0, toolbar=0, scrollbars=1, location=0, status=1');
 	Richfaces.hideModalPanel(pnlRep);
 }
 
 function nuevaBusqueda(v_bool, pnlOperacion, pnlMsg) {
 	console.log('function nuevaBusqueda(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
 	console.log('       v_bool: ' + v_bool);
 	console.log(' pnlOperacion: ' + pnlOperacion);
 	console.log('       pnlMsg: ' + pnlMsg);
 	
 	if (v_bool == true)
 		Richfaces.showModalPanel(pnlMsg);
 	else 
 		Richfaces.showModalPanel(pnlOperacion);

 	console.log('fin function nuevaBusqueda DONE\n------------------------------');
 }

 function buscar(v_bool, pnlMsg) {
 	console.log('function buscar(v_bool, pnlMsg)\n------------------------------');
 	console.log('       v_bool: ' + v_bool);
 	console.log('       pnlMsg: ' + pnlMsg);
 	
 	if (v_bool == true)
 		Richfaces.showModalPanel(pnlMsg);
 	console.log('fin function buscar DONE\n------------------------------');
 }

 function editar(v_bool, pnlOperacion, pnlMsg) {
 	console.log('function editar(v_bool, pnlOperacion, pnlMsg)\n------------------------------');
 	console.log('       v_bool: ' + v_bool);
 	console.log(' pnlOperacion: ' + pnlOperacion);
 	console.log('       pnlMsg: ' + pnlMsg);
 	
 	if (v_bool == true)
 		Richfaces.showModalPanel(pnlMsg);
 	else 
 		Richfaces.showModalPanel(pnlOperacion);
 	console.log('fin function editar DONE\n------------------------------');
 }

 function guardar(v_bool, pnlOperacion, pnlMensaje, listErrores) {
	 console.log('funcion guardar(v_bool, pnlOperacion, pnlMensaje, listErrores)\n----------------------------------------');
	 console.log('      v_bool : ' + v_bool);
	 console.log('pnlOperacion : ' + pnlOperacion);
	 console.log('  pnlMensaje : ' + pnlMensaje);
	 console.log(listErrores);
	 console.log(''); 

	 console.log("Evalua REQUERIDOS");
	 if (listErrores.hasChildNodes()) {
		 console.log("function ERROR - REQUERIDOS");
		 return;
	 }
	 
	 if(v_bool == false) {
		 Richfaces.hideModalPanel(pnlOperacion);
	 }
	 Richfaces.showModalPanel(pnlMensaje);
	 console.log('funcion DONE\n----------------------------------------');
 }
 
 /* USO: onkeypress="return soloDecimales(event, this.value, 10, 2)" */
 function soloDecimales(event, decimal, maxDigitos, maxDecimales, permiteNegativos, debug) {
 	var resultEvent = false;
 	var matcherString = '^([0-9]{0,MAXDIGIT})$|^([0-9]{0,MAXDIGIT})?([.][0-9]{1,MAXDECIMAL})?$';
 	var matcher = null;
 	var caracteresValidos = "0123456789.";
 	var key = event.key;
 	var keyCode = event.keyCode;
 	var indexKey = event.target.selectionStart;
 	
 	// Inicializamos
 	decimal 		  = decimal || '';
 	maxDigitos 		  = maxDigitos   || 10;
 	maxDecimales 	  = maxDecimales ||  2;
 	permiteNegativos  = permiteNegativos || false;
 	debug 			  = debug || false;
 	caracteresValidos = (permiteNegativos == true) ? caracteresValidos + '-' : caracteresValidos;
 	matcherString 	  = matcherString.replace(/MAXDIGIT/g, maxDigitos).replace('MAXDECIMAL', maxDecimales)
 	decimal 		  = decimal.replace(/,/g, '');
 	matcher 		  = new RegExp(matcherString);

 	if (debug == true) {
 		console.log("-> soloDecimales(event, decimal, maxDigitos, maxDecimales)\n----------------------------------");
 		console.log("->          Key : " + key);
 		console.log("->      KeyCode : " + keyCode);
 		console.log("->      decimal : " + decimal);
 		console.log("->   maxDigitos : " + maxDigitos);
 		console.log("-> maxDecimales : " + maxDecimales);
 		console.log("->     indexKey : " + indexKey);
 		console.log("->matcherString : " + matcherString);
 		console.log("->  validString : " + caracteresValidos);
 		console.log(event);
 	}

 	if (event.ctrlKey == true && "CVXcvx".includes(key)) {
 		if (debug == true)
 			console.log("-----------\nControl + " + key + "\n-----------");
 		return true;
 	}
 	
 	if (keyCode ==  8 || keyCode ==  9 || keyCode == 35 
 		|| keyCode == 36 || keyCode == 37 || keyCode == 38 
 		|| keyCode == 39 || keyCode == 40 || keyCode == 46 )
 		return true;
 	
 	// Validaciones contra el valor actual del input
 	if (decimal != undefined && decimal != '') {
 		if ((caracteresValidos.indexOf(key) > -1) == false)
 			return false;

 		// Comprobamos que solo exista un punto en la cadena
 		if (key == '.') {
 			// Si ya existe en la cadena
 			if (decimal.indexOf(key) > -1)
 				return false;
 			return true;
 		}

 		// Comprobamos que solo exista un guion y que este al inicio de la cadena
 		if (key == '-') {
 			// Si no permite negativos
 			if (permiteNegativos == false)
 				return false;

 			// si ya existe en la cadena
 			if (decimal.indexOf(key) > -1)
 				return false;

 			// Si no esta al inicio de la cadena
 			if (indexKey > 0)
 				return false;

 			return true;
 		}

 		// Agregamos el caracter al valor actual del input (decimal) y evaluamos formato y longitud de las partes (digitos y decimales)
 		decimal = decimal.slice(0, indexKey) + key + decimal.slice(indexKey);
 		if (debug == true) {
 			console.log("-> ----------------------------------");
 			console.log("->valor updated : " + decimal);
 			console.log("-> matcher.test : " + matcher.test(decimal));
 		}

 		return matcher.test(decimal);
 	}
 	
 	return (caracteresValidos.indexOf(key) > -1);
 }
