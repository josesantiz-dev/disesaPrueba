


function listarRequisiciones(){
	$.ajax({
        type: 'post', async: true, beforeSend: function () {},
        url: 'Requisiciones',
        data: {operacion: 'listarRequisiciones', param2:''},
        success: function (requisiciones) {
        	$('#tblRequisiciones').bootstrapTable('load', requisiciones);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error: ["+xhr.status+"] ["+thrownError+"]");
        }
    });
}

function autorizarRequisiciones(){

	var permisos = true;
	
	$.each($('#tblRequisiciones').bootstrapTable('getSelections'), function(i, requisicion) {
		
		//autorizarRequisicion(requisicion.id);
		$.ajax({
	        type: 'post', async: false, beforeSend: function () {},
	        url: 'Requisiciones',
	        data: {operacion: 'autorizarRequisicion', idRequisicion: requisicion.id},
	        success: function (autorizado) {
	        	if(autorizado==1){
	            	
	            }else{
	            	if(autorizado==-1){
	            		permisos = false;
	            		return false;
	            	}
	            }
	        },
	        error: function (xhr, ajaxOptions, thrownError) {
	            alert("Error: ["+xhr.status+"] ["+thrownError+"]");
	        }
	    });
		
	});

	$("#mdlAutorizar").modal('toggle');
	
	if(permisos==false){
		mostrarPermisosInsuficientes();
	}else{
		listarRequisiciones();
	}
	
	
}

function mostrarPermisosInsuficientes(){
	$('#mdlPermisos').modal('show');
}

/*
function autorizarRequisicion(idRequisicion){
	$.ajax({
        type: 'post', async: false, beforeSend: function () {},
        url: 'Requisiciones',
        data: {operacion: 'autorizarRequisicion', idRequisicion: idRequisicion},
        success: function (autorizado) {
        	console.log('autorizado: '+autorizado);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error: ["+xhr.status+"] ["+thrownError+"]");
        }
    });
}*/

function mostrarPanelRequisicion(requisicionDetalles){
	console.log('idRequisicion: '+idRequisicion);

	$("#spnObra").text(obra);
	$("#spnComprador").text(comprador);
	
	$('#tblDetalle').bootstrapTable('load', requisicionDetalles);
	
	$('#mdlRequisicion').modal('show');
}
