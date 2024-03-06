
listarObras();

$("#btnAutorizar").click(function(){
	$('#mdlAutorizar').modal('show');
});

function listarObras(){
	$.ajax({
        type: 'post',
        url: 'Obras',
        async: true,
        data: {operacion: 'listarObras', param2:'segundo parametro'},
        beforeSend: function () {},
        success: function (resultado) {
            cargarObras(resultado);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error en la llamada de Ajax: ["+xhr.status+"] ["+thrownError+"]");
        }
    });
}

function cargarObras(obras){ 
	var listaObras = new Array();

	$.each(obras, function(i, obra) {
		var sTotal = '$' + parseFloat(obra.montoContratado, 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString();
		var datosObra = { id: 0, obra: "" , autorizada: 0 };
		datosObra.id = obra.id;
		datosObra.obra = obra.nombre;
		datosObra.total = sTotal;
        listaObras.push(datosObra);
    });
	
	$('#tblObras').bootstrapTable('load', listaObras);
	//console.log('Carga de obras.... done');
}

function confirmar(){
	"use strict";

    var elem,
        hideHandler,
        that = {};

    that.init = function(options) {
        elem = $(options.selector);
    };

    that.show = function(text) {
        clearTimeout(hideHandler);

        elem.find("span").html(text);
        elem.delay(200).fadeIn().delay(4000).fadeOut();
    };

    return that;
}

function autorizarObras(){
	var permisos = true;
	$.each($('#tblObras').bootstrapTable('getSelections'), function(i, obra) {
		//console.log('Autorizar obra, id: '+obra.id);
		//autorizarObra(obra.id);
		var datos = {operacion: 'autorizarObra', idObra: obra.id};
		$.ajax({
	        type: 'post',
	        url: 'Obras',
	        async: false,
	        data: datos,
	        beforeSend: function () {},
	        success: function (resultado) {
	            if(resultado==1){
	            	
	            }else{
	            	if(resultado==-1){
	            		//mostrarSinPermisos();
	            		permisos = false;
	            		console.log('sali del ciclo');
	            		return false;
	            	}
	            }
	        },
	        error: function (xhr, ajaxOptions, thrownError) {
	            alert("Error en la llamada de Ajax: ["+xhr.status+"] ["+thrownError+"]");
	        }
	    });
		
	});
	
	$("#mdlAutorizar").modal('toggle');
	
	if(permisos==false){
		mostrarPermisosInsuficientes();
	}else{
		listarObras();
	}
	
	
}

function mostrarPermisosInsuficientes(){
	$('#mdlPermisos').modal('show');
}

/*
function autorizarObra(xIdObra){
	var datos = {operacion: 'autorizarObra', idObra: 0};
	datos.idObra = xIdObra;
	$.ajax({
        type: 'post',
        url: 'Obras',
        async: false,
        data: datos,
        beforeSend: function () {},
        success: function (resultado) {
            if(resultado==1){
            	listarObras();
            }else{
            	if(resultado==-1){
            		mostrarSinPermisos();
            	}
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error en la llamada de Ajax: ["+xhr.status+"] ["+thrownError+"]");
        }
    });
}*/