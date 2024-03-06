
Select
	pgd.referencia as factura, case pg.tipo_benef when 'P' then persona.ag else negocio.af end as empresa, 
	convalores.ag as descripcion, 
	'convalores.atributo1' as tipo_egreso, pgd.fecha, 
	coalesce(pgd.subtotal,0) + coalesce(pgd.total_impuestos, 0) - coalesce(pgd.total_retenciones,0)  as importe, 
	pgd.facturado, cast('' as varchar) as observaciones
From
	pagos_gastos pg inner join obra o on o.id_obra = pg.id_obra
	inner join pagos_gastos_det pgd on pg.pagos_gastos_id = pgd.pagos_gastos_id
	-- left join cat_gastos cg on cg.clave = pgd.concepto_id

	left join c81498d458 persona on pgd.proveedor_id = persona.aa
	left join e769c352b7 negocio on pgd.proveedor_id = negocio.aa
	left join de7a4d94446 convalores on pgd.concepto_id = convalores.aa
Where 
	pg.pagos_gastos_id = 10000070 
and pgd.facturado = 1	




detalles van los que sean facturado = 1


-- ANTERIOR
Select
	pgd.referencia as factura, case pg.tipo_benef when 'P' then persona.ag else negocio.af end as empresa, 
	cg.nombre as descripcion, 'convalores.atributo1' as tipo_egreso, pgd.fecha, 
	coalesce(pgd.subtotal,0) + coalesce(pgd.total_impuestos, 0) - coalesce(pgd.total_retenciones,0)  as importe, 
	pgd.facturado, cast('' as varchar) as observaciones
From
	pagos_gastos pg inner join obra o on o.id_obra = pg.id_obra
	inner join pagos_gastos_det pgd on pg.pagos_gastos_id = pgd.pagos_gastos_id
	left join cat_gastos cg on cg.clave = pgd.concepto_id

	left join c81498d458 persona on pgd.proveedor_id = persona.aa
	left join e769c352b7 negocio on pgd.proveedor_id = negocio.aa
	
Where 
	pg.pagos_gastos_id = 10000070 
-- Select * from pagos_gastos_det order by pagos_gastos_id

-- select * from a858c9f4c5

--  Select * from cat_gastos



Tabla persona = c81498d458
Tabla negocio = e769c352b7

tendrias que buscar en persona o negocio dependiendo del tipo
usando el dato de pagos_gastos.tipo_benef


Select * from de7a4d94446 convalores where aa = 6405

Select * from de7a4d94446 convalores where ag like '%egre%' -- 6089



tendrias que sacar de tu concepto de gasto el valor de atributo1 y cruzarlo con la convalores.id para buscar el tipo de egreso
de ese registro se muestra la descripcion
me explico