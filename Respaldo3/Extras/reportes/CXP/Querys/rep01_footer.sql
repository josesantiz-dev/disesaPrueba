
Select
	(Select sum( coalesce(pgd.subtotal,0) + coalesce(pgd.total_impuestos, 0) - coalesce(pgd.total_retenciones,0))
		From pagos_gastos_det pgd Where pgd.pagos_gastos_id = pg.pagos_gastos_id ) as total,
	(Select sum( facturado ) From pagos_gastos_det pgd Where pgd.pagos_gastos_id = pg.pagos_gastos_id ) as total_sin_facturas,	
	pg.monto as reembolso
From
	pagos_gastos pg
Where
	pg.pagos_gastos_id = 10000070



facturado 1 o 0, sumar el total del detalle donde facturado = 0


	 $P{pagosGastosId}