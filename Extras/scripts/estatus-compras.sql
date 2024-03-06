/*
select * from pagos_gastos where pagos_gastos_id = 10004875;
select * from pagos_gastos_det where pagos_gastos_id = 10004875;

select * from requisicion_detalle where id_requisicion = 10007910;

select c.id_requisicion, b.id_producto, sum(b.cantidad) as cantidad_orden_compra
from orden_compra a inner join orden_compra_detalle b on b.id_orden_compra = a.id 
    inner join cotizacion c on c.id = a.id_cotizacion
    inner join requisicion d on d.id = c.id_requisicion
where a.estatus in (0,2)
group by c.id_requisicion, b.id_producto
order by c.id_requisicion desc, b.id_producto;

select a.id_cotizacion, b.id_producto, sum(b.cantidad) as cantidad_orden_compra
from orden_compra a inner join orden_compra_detalle b on b.id_orden_compra = a.id 
    inner join cotizacion c on c.id = a.id_cotizacion
where a.estatus in (0,2)
group by a.id_cotizacion, b.id_producto
order by a.id_cotizacion desc, b.id_producto;
*/
-- -----------------------------------------------------------------------------------------------------------------------------------------------
/*
COMMENT ON COLUMN cotizacion_detalle.estatus IS '0: ACTIVA,1: EN OC';
*/
update cotizacion_detalle a set cantidad_orden_compra = x.cantidad_orden_compra
from ( 
    select a.id_cotizacion, b.id_producto, sum(b.cantidad) as cantidad_orden_compra
    from orden_compra a inner join orden_compra_detalle b on b.id_orden_compra = a.id 
        inner join cotizacion c on c.id = a.id_cotizacion
    where a.estatus in (0,2)
    group by a.id_cotizacion, b.id_producto) x
where a.id_cotizacion = x.id_cotizacion and a.id_producto = x.id_producto;

update cotizacion_detalle set estatus = case cantidad_orden_compra >= cantidad when true then 1 else 0 end
where estatus <> 2;

update cotizacion a
set estatus = case (x.requerido > x.suministrado) when true then 0 else 2 end
from (
    select id_cotizacion, sum(cotizar) as requerido, sum(cantidad_suministrada) as suministrado
    from cotizacion_detalle 
    group by id_cotizacion
) x
where a.id = x.id_cotizacion and a.id_empresa = 1 and a.estatus in (0,2);

-- -----------------------------------------------------------------------------------------------------------------------------------------------
/*
COMMENT ON COLUMN requisicion_detalle.estatus IS '0: ACTIVA,1: EN OC';
*/
update requisicion_detalle a set cantidad_orden_compra = x.cantidad_orden_compra, id_cotizacion = 0, cotizacion_folio = '', cantidad_cotizada = 0
from ( 
    select c.id_requisicion, b.id_producto, sum(b.cantidad) as cantidad_orden_compra
    from orden_compra a inner join orden_compra_detalle b on b.id_orden_compra = a.id 
        inner join cotizacion c on c.id = a.id_cotizacion
        inner join requisicion d on d.id = c.id_requisicion
    where a.estatus in (0,2)
    group by c.id_requisicion, b.id_producto) x
where a.id_requisicion = x.id_requisicion and a.id_producto = x.id_producto;

update requisicion_detalle set estatus = case cantidad_orden_compra >= cantidad when true then 1 else 0 end
where estatus <> 2;

update requisicion a
set estatus = case (x.requerido > x.suministrado) when true then 0 else 2 end
from (
    select id_requisicion, sum(cantidad) as requerido, sum(cantidad_suministrada) as suministrado
    from requisicion_detalle --where id_requisicion in (10007876,10007303)
    group by id_requisicion
) x
where a.id = x.id_requisicion and a.id_empresa = 1 and a.estatus in (0,2);

-- -----------------------------------------------------------------------------------------------------------------------------------------------

/*
select id, fecha, id_obra, estatus from requisicion where id in (10007876,10007303);
select id_requisicion, id_producto, cantidad, cantidad_suministrada
from requisicion_detalle where id_requisicion = 10007303;

select id, fecha, id_obra, id_requisicion, estatus from cotizacion where id_requisicion = 10007303;
select id_cotizacion, id_producto, cotizar, cantidad_suministrada
from cotizacion_detalle where id_cotizacion in (select id from cotizacion where id_requisicion = 10007303);

select id, fecha, id_obra, id_cotizacion, estatus from orden_compra where id_cotizacion in (select id from cotizacion where id_requisicion = 10007303);
select id_orden_compra, id_producto, cantidad, cantidad_recibida
from orden_compra_detalle where id_orden_compra in (select id from orden_compra where id_cotizacion in (select id from cotizacion where id_requisicion = 10007303));
*/

select * from requisicion where id = 10007902;
select * from requisicion_detalle where id_requisicion = 10007902;

select * from almacen_traspaso where id = 10012428;
select * from traspaso_detalle where id_almacen_traspaso = 10012428;


SELECT * FROM orden_compra where folio in ('0161-19-196','0161-19-197');



