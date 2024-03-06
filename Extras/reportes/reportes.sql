-- rs_aplicaciones (id, aplicacion)
INSERT INTO rs_aplicaciones VALUES
	(-1, 'Internas'),
	( 1, 'PLATAFORMA OFICINAS'),
	( 2, 'BACK OFFICE'),
	( 3, 'REPORTEADOR FUERA DE LINEA'),
	( 4, 'SEGUIMIENTO'),
	( 5, 'Cuentas por Pagar'),
	( 6, 'Cuentas por Cobrar'),
	( 7, 'Gestion de Proyectos'),
	( 8, 'Recursos Humanos'),
	( 9, 'Inventarios'),
	(10, 'Compras'),
	(11, 'Tesoreria');

-- rs_grupos(id, grupo, descripcion)
INSERT INTO rs_grupos VALUES
	(-1, 'Subgerente de Contratacion', 'Procesos de la subgerente de verificacion y contratacion de la sucursal'),
	( 1, 'Subgerente de Contratacion', 'Procesos de la subgerente de verificacion y contratacion de la sucursal'),
	( 2, 'Proceso de JBPM', 'Proceso de JBPM'),
	( 3, 'CUENTAS POR PAGAR', 'Cuentas por Pagar'),
	( 4, 'CUENTAS POR COBRAR', 'Cuentas por Cobrar'),
	( 5, 'GESTION DE PROYECTOS', 'Gestion de Proyectos'),
	( 6, 'RECURSOS HUMANOS', 'Recursos Humanos'),
	( 7, 'INVENTARIOS', 'Inventarios'),
	( 8, 'COMPRAS', 'Compras'),
	( 9, 'ADMINISTRACION', 'Administracion');

-- rs_subgrupos (id, subgrupo, descripcion)
INSERT INTO rs_subgrupos VALUES 
	( 1, 'SIC', 'Sociedades de Informacion Credicticia'),
	( 2, 'Procesos de traspaso de informacion', 'Procesos de traspaso de informacion de izel online a izel offline'),
	( 3, 'Ahorro documentacoion', 'Ahorro documentos'),
	( 4, 'Reportes', 'Reportes de Credito'),
	( 5, 'Reporte JBPM', 'Reportes de JBMP'),
	( 6, 'CXP', 'Cuentas por Pagar'),
	( 7, 'CXC', 'Cuentas por Cobrar'),
	( 8, 'GP', 'Gestion de Proyectos'),
	( 9, 'RH', 'Recursos Humanos'),
	(10, 'Inventarios', 'Inventarios'),
	(11, 'Compras', 'Compras'),
	(12, 'Admon', 'Administracion'),
	(13, 'CXP', 'Cuentas por Pagar (Administracion)'),
	(14, 'CXC', 'Cuentas por Cobrar (Administracion)'),
	(15, 'Gestion de Obras', 'Gestion de Proyectos (Administracion)'),
	(16, 'RH', 'Recursos Humanos (Administracion)');

-- rs_grupos_subgrupos(id, id_grupo, id_subgrupo)
INSERT INTO rs_grupos_subgrupos VALUES
	( 1, 1,  1),
	( 2, 1,  2),
	( 3, 1,  3),
	( 4, 1,  4),
	( 5, 2,  5),
	( 6, 3,  6),
	( 7, 4,  7),
	( 8, 5,  8),
	( 9, 6,  9),
	(10, 7, 10),
	(11, 8, 11),
	(12, 9, 12),
	(13, 9, 13),
	(14, 9, 14),
	(15, 9, 15),
	(16, 9, 16);

-- rs_asignacion_grupos (id, id_role, id_grupo)
INSERT INTO rs_asignacion_grupos VALUES
	( 1, 1, 1),
	( 2, 2, 1),
	( 3, 3, 1),
	( 4, 4, 2),
	( 5, 5, 1),
	( 6, 3, 3),
	( 7, 3, 4),
	( 8, 3, 5),
	( 9, 3, 6),
	(10, 3, 7),
	(11, 3, 8),
	(12, 3, 9);

-- rs_aplicaciones_tipos(id, id_tipo, ruta, id_aplicacion)
INSERT INTO rs_aplicaciones_tipos VALUES
	( 1, 1, '/opt/rsprocesos/ejecutables/credito/reportes/', 1),
	( 2, 2, '/opt/rsprocesos/ejecutables/credito/tos/', 1),
	( 3, 3, '/opt/rsprocesos/ejecutables/credito/word/', 1),
	( 4, 1, '/opt/rsprocesos/ejecutables/seguimiento/reportes/', 4),
	( 5, 4, '/opt/rsprocesos/ejecutables/credito/word/', 1),
	( 6, 5, '/opt/rsprocesos/ejecutables/seguimiento/reportes/', 4),
	( 7, 2, '/opt/rsprocesos/ejecutables/seguimiento/tos/', 4),
	( 8, 5, '/opt/rsprocesos/ejecutables/credito/reportes/', 1),
	( 9, 6, '/opt/rsprocesos/ejecutables/credito/reportes/', 1),
	(10, 1, '/opt/rsprocesos/ejecutables/cxp/reportes/', 5),
	(11, 5, '/opt/rsprocesos/ejecutables/cxp/reportes/', 5),
	(12, 6, '/opt/rsprocesos/ejecutables/cxp/reportes/', 5),
	(13, 1, '/opt/rsprocesos/ejecutables/cxc/reportes/', 6),
	(14, 5, '/opt/rsprocesos/ejecutables/cxc/reportes/', 6),
	(15, 6, '/opt/rsprocesos/ejecutables/cxc/reportes/', 6),
	(16, 1, '/opt/rsprocesos/ejecutables/gp/reportes/', 7),
	(17, 5, '/opt/rsprocesos/ejecutables/gp/reportes/', 7),
	(18, 6, '/opt/rsprocesos/ejecutables/gp/reportes/', 7),
	(19, 1, '/opt/rsprocesos/ejecutables/rh/reportes/', 8),
	(20, 5, '/opt/rsprocesos/ejecutables/rh/reportes/', 8),
	(21, 6, '/opt/rsprocesos/ejecutables/rh/reportes/', 8),
	(22,-1, '', -1),
	(23, 7, '', -1),
	(24, 1, '/opt/rsprocesos/ejecutables/inventarios/reportes/', 9),
	(25, 5, '/opt/rsprocesos/ejecutables/inventarios/reportes/', 9),
	(26, 6, '/opt/rsprocesos/ejecutables/inventarios/reportes/', 9),
	(27, 1, '/opt/rsprocesos/ejecutables/compras/reportes/', 10),
	(28, 5, '/opt/rsprocesos/ejecutables/compras/reportes/', 10),
	(29, 6, '/opt/rsprocesos/ejecutables/compras/reportes/', 10),
	(30, 1, '/opt/rsprocesos/ejecutables/tesoreria/reportes/', 11),
	(31, 5, '/opt/rsprocesos/ejecutables/tesoreria/reportes/', 11),
	(32, 6, '/opt/rsprocesos/ejecutables/tesoreria/reportes/', 11),
	(33, 2, '/opt/rsprocesos/ejecutables/gp/tos/', 7),
	(34, 2, '/opt/rsprocesos/ejecutables/compras/tos/', 10);

-- rs_parametros (id, descripcion, parametro, tipo, tipo_entrada, formato_entrada, formato_salida, requerido, etiqueta, valores, ayuda, mensaje_error, valor_default, orden, grupo)
INSERT INTO rs_parametros VALUES
	(235, 'pagos gastos id','pagosGastosId','s','N','','',1,'','','','',''),
	(236, 'Ruta subreportes CXP','SUBREPORT_DIR','s','N','','',1,'','','','','/opt/rsprocesos/ejecutables/cxp/reportes/'),
	(237, 'URL Conexion base de giro','pgUrl','s','N','','',1,'','','','','jdbc:postgresql://localhost:9450/giro'),
	(238, 'Ruta subreportes CXC','SUBREPORT_DIR','s','N','','',1,'','','','','/opt/rsprocesos/ejecutables/cxc/reportes/'),
	(239, 'Ruta subreportes GP','SUBREPORT_DIR','s','N','','',1,'','','','','/opt/rsprocesos/ejecutables/gp/reportes/', 1),
	(241, 'fecha desde', 'fechaDesde', 's', 's', null, null, 1, 'fechaDesde', null, 'fecha desde', null, null, 1), 
	(242, 'fecha hasta', 'fechaHasta', 's', 's', null, null, 1, 'fechaHasta', null, 'fecha hasta', null, null, 1), 
	(243, 'id almacen', 'idAlmacen', 'i', 's', null, null, 1, 'idAlmacen', null, 'id del almacen', null, '0', 1), 
	(244, 'id familia', 'idFamilia', 'i', 's', null, null, 1, 'idFamilia', null, 'id de la Familia', null, '0', 1), 
	(245, 'id obra', 'idObra', 'i', 's', null, null, 1, 'idObra', null, 'id de la obra', null, '0', 1), 
	(246, 'id orden compra', 'idOrdenCompra', 'i', 's', null, null, 1, 'idOrdenCompra', null, 'id de Orden de Compra', null, '0', 1), 
	(247, 'id requisicion', 'idRequisicion', 'i', 's', null, null, 1, 'idRequisicion', null, 'id de la Requisicion', null, '0', 1), 
	(253, 'id cotizacion', 'idCotizacion', 'i', 's', null, null, 1, 'idCotizacion', null, 'id de la cotizacion', null, '0', 1), 
	(254, 'id factura', 'idFactura', 'i', 's', null, null, 1, 'idFactura', null, 'id de la factura', null, '0', 1), 
	(255, 'id gasto', 'idGasto', 'i', 's', null, null, 1, 'idGasto', null, 'id Gasto (Pagos Gastos)', null, '0', 1), 
	(256, 'id sucursal', 'idSucursal', 'i', 's', null, null, 1, 'idSucursal', null, 'id sucursal', null, '0', 1), 
	(257, 'id comparativa', 'idComparativa', 'i', 's', null, null, 1, 'idComparativa', null, 'id comparativa', null, '0', 1),
	(267, 'Ruta subreportes Compras','SUBREPORT_DIR','s','N','','',1,'','','','','/opt/rsprocesos/ejecutables/compras/reportes/'),
	(268, 'id tipo de maestro','idTipoMaestro','i','s','','',1,'','','','','1', 1),
	(269, 'id proveedor','idProveedor','i','g',null,null,1,'idProveedor',null,'id proveedor',null,'0', 12, 1),
	(270, 'id pago factura','idPagoFactura','i','g',null,null,1,'idPagoFactura',null,'id pago factura',null,'0', 16, 1);

-- Grupo de parametros
INSERT INTO rs_parametros_grupos (id, parametro_grupo, id_origen_datos) VALUES 
	( 1, 'SQL para cuentas bancarias', 1),
	( 2, 'SQL para facturas', 1),
	( 3, 'SQL para familias de productos', 1),
	( 4, 'SQL para almacenes', 1),
	( 5, 'SQL para cotizaciones', 1),
	( 6, 'SQL para requisicion', 1),
	( 7, 'SQL para ordenes de compra', 1),
	( 8, 'SQL para obras', 1),
	( 9, 'SQL para sucursales', 1),
	(10, 'SQL para caja chica (pagos gastos tipo C', 1),
	(11, 'SQL para gastos (pagos gastos tipo G', 1),
	(12, 'SQL para personas', 1),
	(13, 'SQL para empleados', 1),
	(14, 'SQL para movimientos de almacen (entradas y salidas', 1),
	(15, 'SQL para comparativas', 1),
	(16, 'SQL para pagos de facturas', 1);

-- propiedades de grupo de parametros
INSERT INTO rs_parametros_grupos_propiedades (id, id_parametro_grupo, llave, valor) VALUES 
	(1, 1, 'sql', 'select aa, af from  a95f1327c6'),
	(2, 1, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(3, 1, 'clase', 'SqlValores'),
	(4, 2, 'sql', 'SELECT id as clave, folio_factura || COALESCE('' - '' || cliente, '''') as valor FROM factura WHERE estatus = 1;'),
	(5, 2, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(6, 2, 'clase', 'SqlValores'),
	(7, 3, 'sql', 'SELECT COALESCE(aa, 0) AS clave, COALESCE(ag, '''') AS valor FROM de7a4d94446 WHERE ai = 10000040;'),
	(8, 3, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(9, 3, 'clase', 'SqlValores'),
	(10, 4, 'sql', 'SELECT id, nombre FROM almacen WHERE estatus = 0 ORDER BY nombre;'),
	(11, 4, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(12, 4, 'clase', 'SqlValores'),
	(13, 5, 'sql', 'SELECT c.id as clave, c.nombre_proveedor || '' - $ '' || trim(to_char(c.total, ''999,999,999,999,990.00'')) AS valor FROM cotizacion c INNER JOIN obra o ON o.id_obra = c.id_obra WHERE c.estatus = 0 AND o.estatus > 0;'),
	(14, 5, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(15, 5, 'clase', 'SqlValores'),
	(16, 6, 'sql', 'SELECT r.id as clave, (SELECT COUNT(id) FROM requisicion_detalle WHERE id_requisicion = r.id) || '' productos por '' || UPPER(TRIM(p.nombre)) || '' para obra '' || UPPER(TRIM(o.nombre)) AS valor FROM requisicion r INNER JOIN obra o ON o.id_obra = r.id_obra INNER JOIN c5f822917f p ON p.aa = r.id_solicita WHERE r.estatus = 0 AND o.estatus > 0 ORDER BY r.id DESC;'),
	(17, 6, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(18, 6, 'clase', 'SqlValores'),
	(19, 7, 'sql', 'SELECT 200 as clave, ''ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem-findLike(java.lang.String)-java.lang.String-?-getFolio,getNombreObra,getNombreProveedor,getTotal'' AS valor;'),
	(20, 7, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(21, 7, 'clase', 'SqlValores'),
	(22, 8, 'sql', 'select 200 as clave, ''ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem-busquedaDinamica(java.lang.String)-java.lang.String-?-getNombre'' as valor;'),
	(23, 8, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(24, 8, 'clase', 'SqlValores'),
	(25, 9, 'sql', 'SELECT s.aa AS clave, s.af AS valor FROM a535303dbc s ORDER BY s.aa;'),
	(26, 9, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(27, 9, 'clase', 'SqlValores'),
	(28, 10, 'sql', 'SELECT o.pagos_gastos_id as clave, UPPER(o.beneficiario || '' - $ '' || trim(to_char(o.monto, ''999,999,999,999,990.00''))) AS valor FROM pagos_gastos o WHERE o.tipo = ''C'' AND o.estatus <> ''X'' ORDER BY o.beneficiario;'),
	(29, 10, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(30, 10, 'clase', 'SqlValores'),
	(31, 11, 'sql', 'SELECT o.pagos_gastos_id as clave, UPPER(o.beneficiario || '' - $ '' || trim(to_char(o.monto, ''999,999,999,999,990.00''))) AS valor FROM pagos_gastos o WHERE o.tipo = ''G'' AND o.estatus = ''C'' ORDER BY o.beneficiario;'),
	(32, 11, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(33, 11, 'clase', 'SqlValores'),
	(34, 12, 'sql', 'select 200 as clave, ''ejb:/Logica_Clientes//PersonaNegocioVistaFac!net.giro.clientes.logica.PersonaNegocioVistaRem-busquedaDinamica(java.lang.String)-java.lang.String-?-getNombre,getRfc,getTipoCliente'' as valor;'),
	(35, 12, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(36, 12, 'clase', 'SqlValores'),
	(37, 13, 'sql', 'select aa AS clave, nombre AS valor from c5f822917f'),
	(38, 13, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(39, 13, 'clase', 'SqlValores'),
	(40, 14, 'sql', 'select id AS clave, CASE tipo WHEN 0 THEN ''ENTRADA'' WHEN 1 THEN ''SALIDA'' ELSE ''NA'' END AS valor from almacen_movimientos'),
	(41, 14, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(42, 14, 'clase', 'SqlValores'),
	(43, 15, 'sql', 'SELECT id AS clave, descripcion || '' - '' || nombre_obra AS valor FROM comparativa ORDER BY id DESC;'),
	(44, 15, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(45, 15, 'clase', 'SqlValores'),
	(46, 16, 'sql', 'SELECT b.id as clave, a.folio_factura || '', $ '' || trim(to_char(b.monto, ''999,999,999,999,990.00'')) || '' fecha '' || date(b.fecha) as valor FROM factura a inner join factura_pagos b on b.id_factura = a.id WHERE b.estatus = 1;'),
	(47, 16, 'jar', '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'),
	(48, 16, 'clase', 'SqlValores');

INSERT INTO rs_parametros VALUES (266, 'id comparativa', 'idComparativa', 's', 'g', null, null, 1, 'idComparativa', null, 'id comparativa', null, '0', 15, 1);
INSERT INTO rs_programas_parametros VALUES (1247, 169, 266, 1);

-- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- rs_ejecutables (id, ejecutable, id_tipo, id_aplicacion, SALIDA)		/* 		MODULO			DESCRIPCION 							*/
INSERT INTO rs_ejecutables (id, ejecutable, id_tipo, id_aplicacion, salida) VALUES 
	( -1, 'JobMail dummy', 7, -1, 'NA'),					/* INTERNAS					dummy de declaracion de jobmail interno	*/
	( 22, 'anxinvgar_cc.docx', 3, 1, 'doc'),				/* PLATAFORMA OFICINAS		Reporte de Anexo B Inventario de Garantias	*/
	( 34, 'tablapagosanxa_ca.docx', 3, 1, 'doc'),			/* PLATAFORMA OFICINAS		Tabla de Amortizacion Global Anexo A	*/
	(152, 'CXP-001-01-DA.jasper', 1, 5, 'pdf'),				/* CUENTAS POR PAGAR		Caja Chica	*/
	(153, 'CXP-002-01-DA.jasper', 1, 5, 'pdf'),				/* CUENTAS POR PAGAR		Reporte de reposicion de viaticos	*/
	(154, 'CXC-001-01-DA.jasper', 5, 6, 'xls'),				/* CUENTAS POR COBRAR		Reporte de concentrado de ingresos	*/
	(155, 'CXC-002-01-DA.jasper', 5, 6, 'xls'),				/* CUENTAS POR COBRAR		Reporte de Cobranza	*/
	(156, 'facturas_concentrado.jasper', 5, 6, 'xls'),		/* CUENTAS POR COBRAR		Reporte de Concentrado de Facturas	*/
	(157, 'PPT-01.jasper', 5, 7, 'xls'),					/* GESTION DE PROYECTOS		Reporte de Formato PPT-01 V-5	*/
	(158, 'obras_satics.jasper', 5, 7, 'xls'),				/* GESTION DE PROYECTOS		Reporte de Satics de Obras	*/
	(159, 'estado_cuenta_cobranza_obra.jasper', 5, 7, 'xls'), /* GESTION DE PROYECTOS	Reporte de Estado de Cuenta de Cobranza por Obra	*/
	(160, 'infonavit.jasper', 5, 8, 'xls'),					/* RECURSOS HUMANOS			Reporte de Infonavit	*/
	(161, 'lista_raya.jasper', 5, 8, 'xls'),				/* RECURSOS HUMANOS			Reporte de Lista de Raya	*/
	(162, 'nomina.jasper', 5, 8, 'xls'),					/* RECURSOS HUMANOS			Reporte de Nomina	*/
	(163, 'existencias_almacen.jasper', 5, 9, 'xls'),		/* INVENTARIOS				Reporte de Existencias por Almacen	*/
	(164, 'orden_compra.jasper', 5, 10, 'xls'),				/* COMPRAS					Reporte de Orden de Compra	*/
	(165, 'requisicion_material.jasper', 5, 10, 'xls'),		/* COMPRAS					Reporte de Requisicion de Material	*/
	(166, 'cuentas_bancarias.jasper', 5, 11, 'xls'),		/* TESORERIA				Reporte de Cuentas Bancarias	*/
	(167, 'cotizacion_material.jasper', 5, 10, 'xls'),		/* COMPRAS					Reporte de Cotizacion de Material	*/
	(168, 'factura.jasper', 1, 6, 'pdf'),					/* CUENTAS POR COBRAR		Reporte de Factura	*/
	(169, 'net.giro.comparativa.Comparativa', 2, 7, 'XLS'),	/* GESTION DE PROYECTOS		Reporte de Comparativa	*/
	(170, 'alta_empleado.jasper', 5, 8, 'xls'),				/* RECURSOS HUMANOS			Reporte de Alta de Empleados	*/
	(171, 'back_order.jasper', 5, 9, 'xls'),				/* INVENTARIOS				Reporte de Back Order	*/
	(172, 'baja_empleado.jasper', 5, 8, 'xls'),				/* RECURSOS HUMANOS			Reporte de Baja de Empleados	*/
	(173, 'control_obra.jasper', 5, 7, 'xls'),				/* GESTION DE PROYECTOS		Reporte de Control de Obras	*/
	(174, 'cuentas_por_pagar.jasper', 5, 5, 'xls'),			/* CUENTAS POR PAGAR		Reporte de Cuentas por Pagar	*/
	(175, 'entrada_almacen.jasper', 1, 9, 'pdf'),			/* INVENTARIOS				Reporte de Entradas a Almacen	*/
	(176, 'margen_obra.jasper', 5, 7, 'xls'),				/* GESTION DE PROYECTOS		Reporte de Margen por Obra	*/
	(177, 'MGO-01.jasper', 5, 7, 'xls'),					/* GESTION DE PROYECTOS		Reporte de Margenes de Obras	*/
	(178, 'salida_almacen.jasper', 1, 9, 'pdf'),			/* INVENTARIOS				Reporte de Salidas de Almacen	*/
	(179, 'F-AD-06-REV01.jasper', 5, 6, 'xls'),				/* CUENTAS POR COBRAR		Reporte de Cartera Vencida	*/
	(180, 'F-CO-03-REV01.jasper', 5, 10, 'xls'),			/* COMPRAS					Reporte de Ordenes de Compra de Exp. Insumos	*/
	(181, 'F-CO-04-REV01.jasper', 5, 10, 'xls'),			/* COMPRAS					Reporte de Ordenes de Compra por Obra	*/
	(182, 'F-CO-05-REV01.jasper', 5, 10, 'xls'),			/* COMPRAS					Reporte de Ordenes de Compra por Proveedor	*/
	(183, 'estado_cuenta_cobranza_general.jasper', 5, 7, 'xls'), /* GESTION DE PROYECTOS	Reporte de Estado de Cuenta de Cobranza General	*/
	(184, 'F-AD-08-REV01.jasper', 5, 7, 'xls'),				/* GESTION DE PROYECTOS		Reporte de Registro de Obras	*/
	(185, 'net.giro.backorder.ordenes.BackOrderOrdenes', 2, 10, 'XLS'), /* COMPRAS		Reporte BackOrder: Orden de Compra	*/
	(186, 'inventario_final.jasper', 5, 9, 'xls'),			/* INVENTARIOS				Reporte Inventario final	*/
	(187, 'solicitud_bodega.jasper', 5, 9, 'xls'),			/* INVENTARIOS				Reporte Solicitud a Bodega	*/
	(188, 'maestro.jasper', 5, 9, 'xls'),					/* INVENTARIOS				Reporte de Maestro de productos	*/
	(189, 'explosion_insumos.jasper', 5, 7, 'xls'),			/* GESTION DE PROYECTOS		Reporte de Explosion de Insumos	*/
	(190, 'control_obra_corporativo.jasper', 5, 7, 'xls'),	/* GESTION DE PROYECTOS		Reporte de Control de Obra Corporativo	*/
	(191, 'lista_raya_preliminar.jasper', 5, 8, 'xls'),		/* RECURSOS HUMANOS			Reporte de Lista de Raya	*/
	(192, 'reporte_satic_siroc_obras.jasper', 5, 7, 'xls'),	/* GESTION DE PROYECTOS		Reporte de SATIC-SIROC de Obras	*/
	(193, 'complemento_pago.jasper', 1, 6, 'pdf'),			/* CUENTAS POR COBRAR		Reporte Complemento de Pago	*/
	(194, 'requisiciones_estatus.jasper', 5, 10, 'xls'),	/* COMPRAS					Reporte de Estatus de Requisiciones por Obra	*/
	(195, 'salidas_obras.jasper', 5, 9, 'xls'),				/* INVENTARIOS				Reporte de Salidas de Material a Obra	*/
	(196, 'entradas_bodega.jasper', 5, 9, 'xls'),			/* INVENTARIOS				Reporte de Entradas de Material a Almacen/Bodega	*/
	(197, 'salidas_almacen.jasper', 5, 9, 'xls');			/* INVENTARIOS				Reporte de Salidas de Material de Almacen/Bodega	*/

-- rs_programas (id, programa, titulo, descripcion, id_ejecutable)
INSERT INTO rs_programas VALUES 
	( -1, 'JobMail dummy', 'JobMail Dummy', ' dummy de declaracion de jobmail interno', -1),
	(152, 'Caja Chica reposicion', 'Caja Chica', 'Caja Chica', 152),
	(153, 'Viaticos reposicion', 'Viaticos', 'Reporte de reposicion de viaticos', 153),
	(154, 'Concentrado de ingresos', 'Ingresos', 'Reporte de concentrado de ingresos', 154),
	(155, 'Cobranza', 'Cobranza', 'Reporte de Cobranza', 155),
	(156, 'Concentrado de Facturas', 'F-AD-02-REV01', 'Reporte de Concentrado de Facturas', 156),
	(157, 'Formato PPT-01 V-5', 'Formato PPT-01 V-5', 'Reporte de Formato PPT-01 V-5', 157),
	(158, 'Satics de Obras', 'F-AD-07-REV01', 'Reporte de Satics de Obras', 158),
	(159, 'Estado de Cuenta de Cobranza por Obra', 'F-OB-02-REV01  ', 'Reporte de Estado de Cuenta de Cobranza por Obra', 159),
	(160, 'Infonavit', 'F-RH-04-REV01', 'Reporte de Infonavit', 160),
	(161, 'Lista de Raya', 'Lista de Raya', 'Reporte de Lista de Raya', 161),
	(162, 'Nomina', 'Nomina', 'Reporte de Nomina', 162),
	(163, 'Existencias por Almacen', 'F-RH-02-REV01', 'Reporte de Existencias por Almacen', 163),
	(164, 'Orden de Compra', 'Orden de Compra', 'Reporte de Orden de Compra', 164),
	(165, 'Requisicion de Material', 'Requisicion de Material', 'Reporte de Requisicion de Material', 165),
	(166, 'Cuentas Bancarias', 'F-AD-05-REV01', 'Reporte de Cuentas Bancarias', 166),
	(167, 'Cotizacion de Material', 'Cotizacion de Material', 'Reporte de Cotizacion de Material', 167),
	(168, 'Factura', 'Factura', 'Reporte de Factura', 168),
	(169, 'Comparativa', 'Comparativa', 'Reporte de Comparativa', 169),
	(170, 'Alta de Empleados', 'F-RH-03-REV01', 'Reporte de Alta de Empleados', 170),
	(171, 'Back Order', 'F-CO-01-REV01', 'Reporte de Back Order', 171),
	(172, 'Baja de Empleados', 'F-RH-05-REV01', 'Reporte de Baja de Empleados', 172),
	(173, 'Control de Obras', 'F-OB-01-REV01', 'Reporte de Control de Obras', 173),
	(174, 'Cuentas por Pagar', 'F-AD-01-REV01', 'Reporte de Cuentas por Pagar', 174),
	(175, 'Entradas a Almacen', 'F-AL-01-REV01', 'Reporte de Entradas a Almacen', 175),
	(176, 'Margen por Obra', 'F-OB-04-REV01', 'Reporte de Margen por Obra', 176),
	(177, 'Margenes de Obras', 'F-OB-05-REV01', 'Reporte de Margenes de Obras', 177),
	(178, 'Salidas de Almacen', 'F-AL-02-REV01', 'Reporte de Salidas de Almacen', 178),
	(179, 'Cartera Vencida', 'F-AD-06-REV01', 'Reporte de Cartera Vencida', 179),
	(180, 'Ordenes de Compra de Exp. Insumos', 'F-CO-03-REV01', 'Reporte de Ordenes de Compra de Exp. Insumos', 180),
	(181, 'Ordenes de Compra por Obra', 'F-CO-04-REV01', 'Reporte de Ordenes de Compra por Proveedor', 181),
	(182, 'Ordenes de Compra por Proveedor', 'Ordenes de Compra por Proveedor', 'Reporte de Ordenes de Compra por Proveedor', 182),
	(183, 'Estado de Cuenta de Cobranza General', 'F-OB-03-REV01  ', 'Reporte de Estado de Cuenta de Cobranza General', 183),
	(184, 'Registro de Obras', 'F-AD-08-REV01', 'Reporte de Registro de Obras', 184),
	(185, 'BackOrder: Orden de Compra', 'F-CO-02-REV01', 'Reporte BackOrder: Orden de Compra', 185),
	(186, 'Inventario Final', 'F-AL-03-REV01', 'Reporte Inventario final', 186),
	(187, 'Solicitud a Bodega', 'F-AL-04-REV01', 'Reporte Solicitud a Bodega', 187),
	(188, 'Maestro', 'F-PL-05-REV01', 'Reporte de Maestro de productos', 188),
	(189, 'Explosion de Insumos', 'Explosion de Insumos', 'Reporte de Explosion de Insumos', 189),
	(190, 'Control de Obra Corporativo', 'F-OB-11-REV01', 'Reporte de Control de Obra Corporativo', 190),
	(191, 'Lista de Raya Preliminar', 'F-RH-01-REV01', 'Reporte de Lista de Raya', 191),
	(192, 'SATIC-SIROC de Obras', 'SATIC-SIROC de Obras', 'Reporte de SATIC-SIROC de Obras', 192),
	(193, 'Complemento de Pago (Facturas)', 'F-AD-23-REV01', 'Reporte Complemento de Pago', 193),
	(194, 'Estatus de Requisiciones por Obra', 'F-CO-06-REV01', 'Reporte de Estatus de Requisiciones por Obra', 194),
	(195, 'Salidas de Obra', 'F-AL-19-REV01', 'Reporte de Salidas de Material a Obra', 195),
	(196, 'Entradas a Almacen/Bodega', 'F-AL-20-REV01', 'Reporte de Entradas de Material a Almacen/Bodega', 196),
	(197, 'Salidas a Almacen/Bodega', 'F-AL-20-REV01', 'Reporte de Salidas de Material de Almacen/Bodega', 197);

-- rs_programas_parametros (id, id_programa, id_parametro)
INSERT INTO rs_programas_parametros VALUES 
	/* pgUsuario      pgPassword        pgDriver         ruta_img            pgUrl       SUBREPORT_DIR(CXP)  pagosGastosId 					*/
	(1055, 152, 5), (1056, 152, 6), (1057, 152, 11), (1058, 152, 186), (1059, 152, 237), (1060, 152, 236), (1061, 152, 235),
	/* pgUsuario      pgPassword        pgDriver         ruta_img            pgUrl       SUBREPORT_DIR(CXP)  idGasto 						*/
	(1062, 153, 5), (1063, 153, 6), (1064, 153, 11), (1065, 153, 186), (1066, 153, 237), (1067, 153, 236), (1068, 153, 255),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 FECHA_INICIAL 		  FECHA_FINAL 					*/
	(1069, 154, 5), (1070, 154, 6), (1071, 154, 11), (1072, 154, 237), (1073, 154, 186), (1074, 154, 207), (1075, 154, 208), 
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 FECHA_INICIAL 		  FECHA_FINAL 					*/
	(1076, 155, 5), (1077, 155, 6), (1078, 155, 11), (1079, 155, 237), (1080, 155, 186), (1081, 155, 207), (1082, 155, 208), 	
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 SUBREPORT_DIR(CXC)   fechaDesde 	   fechaHasta 	*/
	(1083, 156, 5), (1084, 156, 6), (1085, 156, 11), (1086, 156, 237), (1087, 156, 186), (1088, 156, 238), (1089, 156, 241), (1090, 156, 242),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 SUBREPORT_DIR(GP) 		idObra 						*/
	(1091, 157, 5), (1092, 157, 6), (1093, 157, 11), (1094, 157, 237), (1095, 157, 186), (1096, 157, 239), (1097, 157, 245),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	fechaDesde 		  fechaHasta 					*/
	(1098, 158, 5), (1099, 158, 6), (1100, 158, 11), (1101, 158, 237), (1102, 158, 186), (1103, 158, 241), (1104, 158, 242),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	  idObra 										*/
	(1105, 159, 5), (1106, 159, 6), (1107, 159, 11), (1108, 159, 237), (1109, 159, 186), (1110, 159, 245),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	fechaDesde 		  fechaHasta 	 	idSucursal 	*/
	(1111, 160, 5), (1112, 160, 6), (1113, 160, 11), (1114, 160, 237), (1115, 160, 186), (1116, 160, 241), (1117, 160, 242), (1118, 160, 233),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	fechaDesde 		  fechaHasta 					*/
	(1119, 161, 5), (1120, 161, 6), (1121, 161, 11), (1122, 161, 237), (1123, 161, 186), (1124, 161, 241), (1125, 161, 242),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	fechaDesde 		  fechaHasta 					*/
	(1126, 162, 5), (1127, 162, 6), (1128, 162, 11), (1129, 162, 237), (1130, 162, 186), (1131, 162, 241), (1132, 162, 242),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	idAlmacen 		  idFamilia 					*/
	(1133, 163, 5), (1134, 163, 6), (1135, 163, 11), (1136, 163, 237), (1137, 163, 186), (1138, 163, 243), (1139, 163, 244),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 idOrdenCompra 		SUBREPORT_DIR(COMPRAS) 			*/
	(1140, 164, 5), (1141, 164, 6), (1142, 164, 11), (1143, 164, 237), (1144, 164, 186), (1145, 164, 246), (1248, 164, 267, 1)
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 idRequisicion 										*/
	(1146, 165, 5), (1147, 165, 6), (1148, 165, 11), (1149, 165, 237), (1150, 165, 186), (1151, 165, 247),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	fechaDesde 		  fechaHasta 					*/
	(1152, 166, 5), (1153, 166, 6), (1154, 166, 11), (1155, 166, 237), (1156, 166, 186), (1157, 166, 241), (1158, 166, 242),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 idCotizacion										*/
	(1164, 167, 5), (1165, 167, 6), (1166, 167, 11), (1167, 167, 237), (1168, 167, 186), (1169, 167, 253),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 idFactura											*/
	(1170, 168, 5), (1171, 168, 6), (1172, 168, 11), (1173, 168, 237), (1174, 168, 186), (1175, 168, 254),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	      idObra		  idFamilia		  idRequisicion	*/
	(1181, 169, 5), (1182, 169, 6), (1183, 169, 11), (1184, 169, 237), (1185, 169, 186), (1186, 169, 245), (1187, 169, 244), (1188, 169, 247), (1188, 169, 257),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	fechaDesde 		  fechaHasta 					*/
	(1249, 179, 5), (1250, 179, 6), (1251, 179, 11), (1252, 179, 237), (1253, 179, 186), (1254, 179, 241), (1255, 179, 242),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	  idObra 										*/
	(1256, 180, 5), (1257, 180, 6), (1258, 180, 11), (1259, 180, 237), (1260, 180, 186), (1261, 180, 245),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	  idObra 										*/
	(1262, 181, 5), (1263, 181, 6), (1264, 181, 11), (1265, 181, 237), (1266, 181, 186), (1267, 181, 245),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	  idPersona 	  fechaDesde 		fechaHasta	*/
	(1268, 182, 5), (1269, 182, 6), (1270, 182, 11), (1271, 182, 237), (1272, 182, 186), (1273, 182, 265), (1274, 182, 241), (1275, 182, 242),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	  idObra 										*/
	(1276, 183, 5), (1277, 183, 6), (1278, 183, 11), (1279, 183, 237), (1280, 183, 186), (1281, 183, 245),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	  idObra 										*/
	(1282, 184, 5), (1283, 184, 6), (1284, 184, 11), (1285, 184, 237), (1286, 184, 186), (1287, 184, 245),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 idOrdenCompra 										*/
	(1288, 185, 5), (1289, 185, 6), (1290, 185, 11), (1291, 185, 237), (1292, 185, 186), (1293, 185, 246),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	idAlmacen 		  idFamilia 					*/
	(1294, 186, 5), (1295, 186, 6), (1296, 186, 11), (1297, 186, 237), (1298, 186, 186), (1299, 186, 243), (1300, 186, 244),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	idObra 		  				 					*/
	(1301, 187, 5), (1302, 187, 6), (1303, 187, 11), (1304, 187, 237), (1305, 187, 186), (1313, 187, 245),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	idFamilia 		idTipoMaestro					*/
	(1306, 188, 5), (1307, 188, 6), (1308, 188, 11), (1309, 188, 237), (1310, 188, 186), (1311, 188, 244), (1312, 188, 268),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	idObra 		  				 					*/
	(1314, 189, 5), (1315, 189, 6), (1316, 189, 11), (1317, 189, 237), (1318, 189, 186), (1319, 189, 245),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	   idSucursal 	  	fechaDesde 		fechaHasta	*/
	(1320,190,5,1), (1321,190,241,1), (1322,190,242,1), (1323,190,237,1), (1324,190,186,1), (1325,190,233,1), (1326,190,241,1), (1327,190,242,1),

	(1328, 191, 242, 1), (1329, 191, 241, 1), (1330, 191, 186, 1), (1331, 191, 237, 1), (1332, 191,  11, 1), (1333, 191,   6, 1), (1334, 191,   5, 1),
	
	(1335, 192, 245), (1336, 192, 186), (1337, 192, 237), (1338, 192, 11), (1339, 192, 6), (1340, 192, 5), 
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 idPagoFactura											*/
	(1341, 193, 5), (1342, 193, 6), (1343, 193, 11), (1344, 193, 237), (1345, 193, 186), (1346, 193, 270),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	  idObra 											*/
	(1262, 194, 5), (1263, 194, 6), (1264, 194, 11), (1265, 194, 237), (1266, 194, 186), (1267, 194, 245),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	idObra  	     fechaDesde 		fechaHasta		*/
	(1314, 195, 5), (1315, 195, 6), (1316, 195, 11), (1317, 195, 237), (1318, 195, 186), (1319, 195, 245), (1274, 195, 241), (1275, 195, 242),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	idAlmacen  	   	 fechaDesde 		fechaHasta	    */
	(1275, 196, 5), (1275, 196, 6), (1275, 196, 11), (1275, 196, 237), (1275, 196, 186), (1275, 196, 243), (1375, 196, 241, 2), (1376, 196, 242, 3),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	idAlmacen  	   	 fechaDesde 		fechaHasta	    */
	(1369, 197, 5), (1370, 197, 6), (1371, 197, 11), (1372, 197, 237), (1373, 197, 186), (1374, 197, 243), (1377, 197, 241, 2), (1378, 197, 242, 3);
	

-- rs_schedulers_programas (id, id_scheduler, id_programa)
-- NOTA: Antes, el scheduler era el 3, ahora debe ser el 1: 1=DISESA, 3=ICAPITAL
INSERT INTO rs_schedulers_programas 
VALUES( -1,  1,  -1)
	, (140,  1, 152)
	, (141,  1, 153)
	, (142,  1, 154)
	, (143,  1, 155)
	, (144,  1, 156)
	, (145,  1, 157)
	, (146,  1, 158)
	, (147,  1, 159)
	, (148,  1, 160)
	, (149,  1, 161)
	, (150,  1, 162)
	, (151,  1, 163)
	, (152,  1, 164)
	, (153,  1, 165)
	, (154,  1, 166)
	, (155,  1, 167)
	, (156,  1, 168)
	, (157,  1, 169)
	, (158,  1, 170)
	, (159,  1, 171)
	, (160,  1, 172)
	, (161,  1, 173)
	, (162,  1, 174)
	, (163,  1, 175)
	, (164,  1, 176)
	, (165,  1, 177)
	, (166,  1, 178)
	, (167,  1, 179)
	, (168,  1, 180)
	, (169,  1, 181)
	, (170,  1, 182)
	, (171,  1, 183)
	, (172,  1, 184)
	, (173,  1, 185)
	, (174,  1, 186)
	, (175,  1, 187)
	, (176,  1, 188)
	, (177,  1, 189)
	, (178,  1, 190)
	, (179,  1, 191)
	, (180,  1, 192)
	, (181,  1, 193)
	, (182,  1, 194)
	, (183,  1, 195)
	, (184,  1, 196)
	, (185,  1, 197)
;

-- rs_subgrupos_programas (id, id_subgrupo, id_programa)
INSERT INTO rs_subgrupos_programas 
VALUES(143,  6, 152)	/* 	 6: CXP 			*/
	, (144,  6, 153)	/* 	 6: CXP 			*/
	, (145,  7, 154)	/* 	 7: CXC 			*/
	, (146,  7, 155)	/* 	 7: CXC 			*/
	, (147,  7, 156)	/* 	 7: CXC 			*/
	, (148,  8, 157)	/* 	 8: GP 				*/
	, (149,  8, 158)	/* 	 8: GP 				*/
	, (150,  8, 159)	/* 	 8: GP 				*/
	, (151,  9, 160)	/* 	 9: RH 				*/
	, (152,  9, 161)	/* 	 9: RH 				*/
	, (153,  9, 162)	/* 	 9: RH 				*/
	, (154, 10, 163)	/* 	10: Inventarios 	*/
	, (155, 11, 164)	/* 	11: Compras 		*/
	, (156, 11, 165)	/* 	11: Compras 		*/
	, (157, 12, 166)	/* 	12: Admon 			*/
	, (158, 11, 167)	/* 	11: Compras 		*/
	, (159,  7, 168)	/* 	 7: CXC 			*/
	, (160,  8, 169)	/* 	 8: GP 				*/
	, (161,  9, 170)	/* 	 9: RH 				*/
	, (162, 10, 171)	/* 	10: Inventarios 	*/
	, (163,  9, 172)	/* 	 9: RH 				*/
	, (164,  8, 173)	/* 	 8: GP 				*/
	, (165,  6, 174)	/* 	 6: CXP 			*/
	, (166, 10, 175)	/* 	10: Inventarios 	*/
	, (167,  8, 176)	/* 	 8: GP 				*/
	, (168,  8, 177)	/* 	 8: GP 				*/
	, (169, 10, 178)	/* 	10: Inventarios 	*/
	, (170,  7, 179)	/* 	 7: CXC 			*/
	, (171, 11, 180)	/* 	11: Compras 		*/
	, (172, 11, 181)	/* 	11: Compras 		*/
	, (173, 11, 182)	/* 	11: Compras 		*/
	, (174,  8, 183)	/* 	 8: GP 				*/
	, (175,  8, 184)	/* 	 8: GP 				*/
	, (176, 11, 185)	/* 	11: Compras 		*/
	, (177, 10, 186)	/* 	10: Inventarios 	*/
	, (178, 10, 187)	/* 	10: Inventarios 	*/
	, (196, 10, 188)	/* 	10: Inventarios 	*/
	, (197,  8, 189)	/* 	 8: GP 				*/
	, (198,  8, 190)	/* 	 8: GP 				*/
	, (199,  9, 191)	/* 	 9: RH 				*/
	, (200,  8, 192)	/* 	 8: GP 				*/
	, (201,  7, 193)	/* 	 7: CXC 			*/
	, (202, 11, 194)	/* 	11: Compras 		*/
	, (177, 10, 195)	/* 	10: Inventarios 	*/
	, (178, 10, 196)	/* 	10: Inventarios 	*/
	, (205, 10, 197)	/* 	10: Inventarios 	*/
;

-- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO rs_ejecutables (id, ejecutable, id_tipo, id_aplicacion, salida) VALUES (197, 'salidas_almacen.jasper', 5, 9, '');
INSERT INTO rs_programas VALUES (197, 'Salidas a Almacen/Bodega', 'F-AL-20-REV01', 'Reporte de Salidas de Material de Almacen/Bodega', 197);
INSERT INTO rs_programas_parametros VALUES (1369, 197, 5), (1370, 197, 6), (1371, 197, 11), (1372, 197, 237), (1373, 197, 186), (1374, 197, 243);
INSERT INTO rs_schedulers_programas VALUES (185, 1, 197);
INSERT INTO rs_subgrupos_programas  VALUES (205, 10, 197);
-- ----------------------------------------------------------------------------------------------------------------------------------------------
INSERT INTO rs_programas_parametros VALUES (1375,196,241), (1376,196,242), (1377,197,241), (1378,197,242);

-- -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

/*
AÑADIR USUARIO A REPORTES
SELECT * FROM rs_usuarios ORDER BY id DESC;
SELECT * FROM rs_usuarios_ROLES ORDER BY id DESC;

INSERT INTO rs_usuarios VALUES (59, 'OIBARRA');
INSERT INTO rs_usuarios_ROLES VALUES (65,3,59);
 */


### seleccionar PROGRAMA con PARAMETROS
SELECT a.id
	, a.titulo
	, a.programa
	, a.id_ejecutable
	, b.ejecutable
	, c.id_parametro
	, d.parametro
	, d.descripcion
	, d.valor_default
FROM rs_programas a
	INNER JOIN rs_ejecutables b ON b.id = a.id_ejecutable
	INNER JOIN rs_programas_parametros c ON c.id_programa = a.id
	INNER JOIN rs_parametros d ON d.id = c.id_parametro
WHERE a.id = 0
ORDER BY c.id_parametro DESC;

