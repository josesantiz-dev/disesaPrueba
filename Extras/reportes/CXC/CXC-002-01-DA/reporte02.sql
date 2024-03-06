-- Select * from factura_pagos
-- cuenta_deposito --> con ese agrupamos
select 
	f.fecha_emision as fecha,  banco_del.nombre_corto as del_banco, persona.ag as concepto,  
	formpag.forma_pago as referencia, f.subtotal + f.impuestos as monto,
	f.folio_factura as comprobante, fp.observaciones, 
	case coalesce(banco_grupo.nombre_corto,'') when '' then 'sin_grupo' else banco_grupo.nombre_corto end as grupo
from
	factura_pagos fp inner join factura f on f.id = fp.id_factura
	inner join obra o on f.id_obra = o.id_obra
	left join c81498d458 persona on o.id_cliente = persona.aa
	left join formas_pagos formpag on fp.id_forma_pago = formpag.forma_pago_id
	left join cat_bancos banco_del on banco_del.cat_banco_id = fp.id_banco_origen
	left join a95f1327c6 cuentas_banco on cuentas_banco.af = fp.cuenta_deposito
	left join cat_bancos banco_grupo on banco_grupo.cat_banco_id = cuentas_banco.ah
	-- 
	-- left join a95f1327c6 cuentas_banco on cuentas_banco.af = cuenta_deposito

	-- left join cat_bancos bancos_en on bancos_en.cat_banco_id = cuentas_banco.ah
	-- left join cat_bancos bancos_del on bancos_del.cat_banco_id = fp.id_banco_origen




