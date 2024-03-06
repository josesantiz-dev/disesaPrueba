/*
	SCRIPT: Inserta grupo de valores para parametros.
 */
INSERT INTO rs_parametros VALUES
	(255, 'id gasto', 'idGasto', 'i', 's', null, null, 1, 'idGasto', null, 'id Gasto (Pagos Gastos)', null, '0', 1);


INSERT INTO rs_parametros_grupos (id,parametro_grupo,id_origen_datos) VALUES 
	( 1, 'SQL para cuentas bancarias', 1),
	( 2, 'SQL para facturas', 1),
	( 3, 'SQL para familias de productos', 1),
	( 4, 'SQL para almacenes', 1),
	( 5, 'SQL para cotizaciones', 1),
	( 6, 'SQL para requisicion', 1),
	( 7, 'SQL para ordenes de compra', 1),
	( 8, 'SQL para obras', 1),
	( 9, 'SQL para sucursales', 1),
	(10, 'SQL para caja chica (pagos gastos tipo C)', 1),
	(11, 'SQL para gastos (pagos gastos tipo G)', 1);

INSERT INTO rs_parametros_grupos_propiedades (id, id_parametro_grupo, llave, valor) VALUES 
	( 1,  1, 'sql',   'select aa, af from  a95f1327c6'), 
	( 2,  1, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	( 3,  1, 'clase', 'SqlValores'), 
	( 4,  2, 'sql',   'SELECT id as clave, folio_factura || COALESCE('' - '' || cliente, '''') as valor FROM factura WHERE estatus = 1;'), 
	( 5,  2, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	( 6,  2, 'clase', 'SqlValores'), 
	( 7,  3, 'sql',   'SELECT COALESCE(aa, 0) AS clave, COALESCE(af, '''') AS valor FROM de7a4d94446 WHERE ai = 10000040;'), 
	( 8,  3, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	( 9,  3, 'clase', 'SqlValores'), 
	(10,  4, 'sql',   'SELECT id, nombre FROM almacen WHERE estatus = 0 ORDER BY nombre;'), 
	(11,  4, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	(12,  4, 'clase', 'SqlValores'), 
	(13,  5, 'sql',   'SELECT c.id as clave, p.ag || '' - $ '' || trim(to_char(c.total, ''999,999,999,999,990.00'')) AS valor FROM cotizacion c INNER JOIN obra o ON o.id_obra = c.id_obra INNER JOIN c81498d458 p ON p.aa = c.id_proveedor WHERE c.estatus = 0 AND o.estatus <> 10000798;'), 
	(14,  5, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	(15,  5, 'clase', 'SqlValores'), 
	(16,  6, 'sql',   'SELECT r.id as clave, ''OBRA: '' || o.nombre AS valor FROM requisicion r INNER JOIN obra o ON o.id_obra = r.id_obra INNER JOIN c5f822917f p ON p.aa = r.id_solicita WHERE r.estatus = 0 AND o.estatus <> 10000798;'), 
	(17,  6, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	(18,  6, 'clase', 'SqlValores'), 
	(19,  7, 'sql',   'SELECT c.id as clave, p.ag || '' - $ '' || trim(to_char(c.total, ''999,999,999,999,990.00'')) AS valor FROM orden_compra c INNER JOIN obra o ON o.id_obra = c.id_obra INNER JOIN c81498d458 p ON p.aa = c.id_proveedor WHERE c.estatus = 0 AND o.estatus <> 10000798;'), 
	(20,  7, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	(21,  7, 'clase', 'SqlValores'), 
	(22,  8, 'sql',   'SELECT o.id_obra as clave, o.nombre || '' - $ '' || trim(to_char(o.monto_contratado, ''999,999,999,999,990.00'')) || CASE o.tipo WHEN 1 THEN '' (OBRA)'' WHEN 2 THEN '' (PROYECTO)'' WHEN 3 THEN '' (ORDEN TRABAJO)'' ELSE '''' END AS valor FROM obra o WHERE o.estatus <> 10000798 ORDER BY o.nombre;'), 
	(23,  8, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	(24,  8, 'clase', 'SqlValores'), 
	(25,  9, 'sql',   'SELECT s.aa AS clave, s.af AS valor FROM a535303dbc s ORDER BY s.aa;'), 
	(26,  9, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	(27,  9, 'clase', 'SqlValores'), 
	(28, 10, 'sql',   'SELECT o.pagos_gastos_id as clave, UPPER(o.beneficiario || '' - $ '' || trim(to_char(o.monto, ''999,999,999,999,990.00''))) AS valor FROM pagos_gastos o WHERE o.tipo = ''C'' AND o.estatus <> ''X'' ORDER BY o.beneficiario;'), 
	(29, 10, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	(30, 10, 'clase', 'SqlValores'), 
	(31, 11, 'sql',   'SELECT o.pagos_gastos_id as clave, UPPER(o.beneficiario || '' - $ '' || trim(to_char(o.monto, ''999,999,999,999,990.00''))) AS valor FROM pagos_gastos o WHERE o.tipo = ''G'' AND o.estatus = ''C'' ORDER BY o.beneficiario;');
	(32, 11, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	(33, 11, 'clase', 'SqlValores');

-- Actualizamos el parametro para el ejecutable 153 (reporte de viaticos), antes tenia el parametro 235(pagosGastosId), asignamos el 255 (idGasto).
UPDATE rs_programas_parametros SET id_parametro = 255 WHERE id_programa = 153 AND id_parametro = 235;


-- Actualizamos cada parametro con su grupo y tipos.
UPDATE rs_parametros SET tipo = 's', tipo_entrada = 'g', grupo =  2 WHERE id = 254; -- idFactura
UPDATE rs_parametros SET tipo = 's', tipo_entrada = 'g', grupo =  3 WHERE id = 244; -- idFamilia
UPDATE rs_parametros SET tipo = 's', tipo_entrada = 'g', grupo =  4 WHERE id = 243; -- idAlmacen
UPDATE rs_parametros SET tipo = 's', tipo_entrada = 'g', grupo =  5 WHERE id = 253; -- idCotizacion
UPDATE rs_parametros SET tipo = 's', tipo_entrada = 'g', grupo =  6 WHERE id = 247; -- idRequisicion
UPDATE rs_parametros SET tipo = 's', tipo_entrada = 'g', grupo =  7 WHERE id = 246; -- idOrdenCompra
UPDATE rs_parametros SET tipo = 's', tipo_entrada = 'g', grupo =  8 WHERE id = 245; -- idObra
UPDATE rs_parametros SET tipo = 's', tipo_entrada = 'g', grupo =  9 WHERE id = 233; -- idSucursal
UPDATE rs_parametros SET tipo = 's', tipo_entrada = 'g', grupo = 10 WHERE id = 235; -- pagosGastosId
UPDATE rs_parametros SET tipo = 's', tipo_entrada = 'g', grupo = 11 WHERE id = 255; -- idGasto