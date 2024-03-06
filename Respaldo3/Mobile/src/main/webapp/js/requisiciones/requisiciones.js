
$('#tblRequisiciones').bootstrapTable({});		//inicializar la bootstrap table


listarRequisiciones();

function formatButton(value, row, index) {
    return '<img id="'+row.id+'" obra = "'+ row.nombreObra +'" comprador="' + row.nombreSolicita + '" src="img/consultar.png" width="20" onclick="mostrarRequisicion(this)"/>';
}

$("#btnAutorizar").click(function(){
	$('#mdlAutorizar').modal('show');
});
