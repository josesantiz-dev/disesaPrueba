

var idOrden = 0;

function autorizarOrdenCompra(){
	console.log('Autorizando obra: '+idOrden);
	$.ajax({
        type: 'post', async: true, beforeSend: function () {},
        url: 'OrdenesCompra',
        data: {operacion: 'autorizarOrdenCompra', idOrdenCompra: idOrden},
        success: function (orden) {
        	//console.log('Autorizar obra done... Res: '+orden);
        	$("#mdlOrden").modal('toggle');
        	
        	if(orden==-1){
        		mostrarPermisosInsuficientes();
        	}else{
        		listarOrdenesCompra();
        	}
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error: ["+xhr.status+"] ["+thrownError+"]");
        }
    });
}

function mostrarOrden(row){
	
	idOrden = $(row).attr( "id" ) ;
	
	$.ajax({
        type: 'post', async: true, beforeSend: function () {},
        url: 'OrdenesCompra',
        data: {operacion: 'getOrdenCompra', idOrdenCompra: idOrden},
        success: function (orden) {
        	
        	idObraAutorizar = orden.id;
        	
			asignarDatosOrden( orden.folio, orden.nombreObra, orden.nombreSolicita, orden.nombreProveedor, 
					orden.nombreContacto, orden.fecha, orden.subtotal, orden.anticipo, orden.iva, orden.plazo, orden.total);
			
        	//console.log(orden);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error: ["+xhr.status+"] ["+thrownError+"]");
        }
    });
	
}

function mostrarPermisosInsuficientes(){
	$('#mdlPermisos').modal('show');
}

function asignarDatosOrden( folio, nombreObra, nombreSolicita, nombreProveedor, nombreContacto, fecha, subtotal, anticipo, iva, plazo, total){
	
	$("#spnFolio").text(folio);
	$("#spnNombreObra").text(nombreObra);
	$("#spnNombreSolicita").text(nombreSolicita);
	$("#spnNombreProveedor").text(nombreProveedor);
	$("#spnNombreContacto").text(nombreContacto);
	
	$("#spnFecha").text(fecha);
	
	var sSubtotal = '$' + parseFloat(subtotal, 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString();
	$("#spnSubtotal").text(sSubtotal);
	
	var sAnticipo = '$' + parseFloat(anticipo, 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString();
	$("#spnAnticipo").text(sAnticipo);
	
	var sIva = '$' + parseFloat(iva, 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString();
	$("#spnIVA").text(sIva);
	
	$("#spnPlazo").text(plazo);
	
	var sTotal = '$' + parseFloat(total, 10).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1,").toString();
	$("#spnTotal").text(sTotal);
	
	$('#mdlOrden').modal('show');
	
}

var $table = $('#tblOrdenesCompra');

$('#tblOrdenesCompra').bootstrapTable({
	
});

listarOrdenesCompra();

function listarOrdenesCompra(){
	$.ajax({
        type: 'post', async: true, beforeSend: function () {},
        url: 'OrdenesCompra',
        data: {operacion: 'listarOrdenesCompra', param2:''},
        success: function (OrdenesCompra) {
        	$('#tblOrdenesCompra').bootstrapTable('load', OrdenesCompra);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error: ["+xhr.status+"] ["+thrownError+"]");
        }
    });
}

function addRow(orden){
	$table.bootstrapTable('insertRow', {
        index: 0,
        row: {
        	folio: orden.folio,
        	nombreObra: orden.nombreObra,
        	id: orden.id
        }
    });
}

function formatButton(value, row, index) {
    //return '<input type="button" id="'+row.id+'" onclick="mostrarOrden(this)" />';
	//return '<button id="'+row.id+'" onclick="mostrarOrden(this)" ><img src="img/consultar.png"></button>';
	return '<img id="'+row.id+'" src="img/consultar.png" width="20" onclick="mostrarOrden(this)"/>';
}