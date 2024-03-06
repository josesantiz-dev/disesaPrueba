-- Los parametros son FECHA_INICIAL, FECHA_FINAL, idSucursal 

-- factura.estatus = 1 es cancelada

-- fp.id_cuenta_deposito lo cambie por fp.id_banco_deposito

-- javier va cambiar el nombre
-- de bancoDeposito a cuentaDeposito, esa cuentaDeposito mapea con CtasBanco del tyg y bancoOrigen mapea con catBancos

-- en ese reporte vas a comparar la fechaEmision de la factura con esos parametros de inicial y final

-- es decir debe mostrar las facturas con fecha de emision >= fecha_inicial y <=fecha_final la sucursal pendiente


select 
	f.fecha_emision as fecha,  f.folio_factura as factura, persona.ag as concepto, f.subtotal, f.impuestos, f.subtotal + f.impuestos as total,

	bancos_en.nombre_corto as depositado_en, 

	fp.fecha as fecha_pago, formpag.forma_pago as referencia, bancos_del.nombre_corto as del_banco
from 
	factura f inner join obra o on f.id_obra = o.id_obra
	left join c81498d458 persona on o.id_cliente = persona.aa

	left join factura_pagos fp on f.id = fp.id_factura
	left join formas_pagos formpag on fp.id_forma_pago = formpag.forma_pago_id
	left join a95f1327c6 cuentas_banco on cuentas_banco.af = cuenta_deposito

	left join cat_bancos bancos_en on bancos_en.cat_banco_id = cuentas_banco.ah
	left join cat_bancos bancos_del on bancos_del.cat_banco_id = fp.id_banco_origen



-- Select * from c81498d458 persona --> campo ag
-- Select * from a95f1327c6 cuentas_banco mapea la columna ah con cat_bancos
-- Select * from a95f1327c6 cuentas_banco-- CtasBanco --af -> numeroDeCuenta