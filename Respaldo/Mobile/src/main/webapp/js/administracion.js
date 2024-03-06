

$("#refSalir").on('click', function() {
	salir($(this).attr('data-id'));
});

function salir(urlFrom){
	urlFrom = urlFrom || 'Administracion';
	$.ajax({
        type: 'post',
        url: urlFrom,
        async: true,
        data: {operacion: 'salir'},
        beforeSend: function () {},
        success: function (resultado) {
        	window.location = "../../cas/logout";
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error en la llamada de Ajax: ["+xhr.status+"] ["+thrownError+"]");
        }
    });
}