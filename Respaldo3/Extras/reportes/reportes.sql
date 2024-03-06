-- rs_aplicaciones (id, aplicacion)
insert into rs_aplicaciones values
	(-1, 'Internas'),
	( 5, 'Cuentas por Pagar'),
	( 6, 'Cuentas por Cobrar'),
	( 7, 'Gestion de Proyectos'),
	( 8, 'Recursos Humanos'),
	( 9, 'Inventarios'),
	(10, 'Compras'),
	(11, 'Tesoreria');

-- rs_grupos(id, grupo, descripcion)
insert into rs_grupos values
	(-1, 'Subgerente de Contratacion', 'Procesos de la subgerente de verificacion y contratacion de la sucursal'),
	( 3, 'CUENTAS POR PAGAR', 'Cuentas por Pagar'),
	( 4, 'CUENTAS POR COBRAR', 'Cuentas por Cobrar'),
	( 5, 'GESTION DE PROYECTOS', 'Gestion de Proyectos'),
	( 6, 'RECURSOS HUMANOS', 'Recursos Humanos'),
	( 7, 'INVENTARIOS', 'Inventarios'),
	( 8, 'COMPRAS', 'Compras'),
	( 9, 'TESORERIA', 'Tesoreria');

-- rs_subgrupos (id, subgrupo, descripcion)
insert into rs_subgrupos values
	( 6, 'Cxp', 'Cuentas por Pagar'),
	( 7, 'Cxc', 'Cuentas por Cobrar'),
	( 8, 'GP', 'Gestion de Proyectos'),
	( 9, 'RH', 'Recursos Humanos'),
	(10, 'INVENTARIOS', 'Inventarios'),
	(11, 'COMPRAS', 'Compras'),
	(12, 'TESORERIA', 'Tesoreria');

-- rs_grupos_subgrupos(id, id_grupo, id_subgrupo)
insert into rs_grupos_subgrupos values
	( 6, 3,  6),
	( 7, 4,  7),
	( 8, 5,  8),
	( 9, 6,  9),
	(10, 7, 10),
	(11, 8, 11),
	(12, 9, 12);

-- rs_asignacion_grupos (id, id_role, id_grupo)
insert into rs_asignacion_grupos values
	( 6, 3, 3),
	( 7, 3, 4),
	( 8, 3, 5),
	( 9, 3, 6),
	(10, 3, 7),
	(11, 3, 8),
	(12, 3, 9);

-- rs_aplicaciones_tipos(id, id_tipo, ruta, id_aplicacion)
insert into rs_aplicaciones_tipos values
	(10,  1, '/opt/rsprocesos/ejecutables/cxp/reportes/', 5),
	(11,  5, '/opt/rsprocesos/ejecutables/cxp/reportes/', 5),
	(12,  6, '/opt/rsprocesos/ejecutables/cxp/reportes/', 5),
	(13,  1, '/opt/rsprocesos/ejecutables/cxc/reportes/', 6),
	(14,  5, '/opt/rsprocesos/ejecutables/cxc/reportes/', 6),
	(15,  6, '/opt/rsprocesos/ejecutables/cxc/reportes/', 6),
	(16,  1, '/opt/rsprocesos/ejecutables/gp/reportes/', 7),
	(17,  5, '/opt/rsprocesos/ejecutables/gp/reportes/', 7),
	(18,  6, '/opt/rsprocesos/ejecutables/gp/reportes/', 7),
	(19,  1, '/opt/rsprocesos/ejecutables/rh/reportes/', 8),
	(20,  5, '/opt/rsprocesos/ejecutables/rh/reportes/', 8),
	(21,  6, '/opt/rsprocesos/ejecutables/rh/reportes/', 8),
	(22, -1, '', -1),
	(23,  7, '', -1),
	(24,  1, '/opt/rsprocesos/ejecutables/inventarios/reportes/', 9),
	(25,  5, '/opt/rsprocesos/ejecutables/inventarios/reportes/', 9),
	(26,  6, '/opt/rsprocesos/ejecutables/inventarios/reportes/', 9),
	(27,  1, '/opt/rsprocesos/ejecutables/compras/reportes/', 10),
	(28,  5, '/opt/rsprocesos/ejecutables/compras/reportes/', 10),
	(29,  6, '/opt/rsprocesos/ejecutables/compras/reportes/', 10),
	(30,  1, '/opt/rsprocesos/ejecutables/tesoreria/reportes/', 11),
	(31,  5, '/opt/rsprocesos/ejecutables/tesoreria/reportes/', 11),
	(32,  6, '/opt/rsprocesos/ejecutables/tesoreria/reportes/', 11);
	(33,  2, '/opt/rsprocesos/ejecutables/gp/tos/', 7);

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
	(257, 'id comparativa', 'idComparativa', 'i', 's', null, null, 1, 'idComparativa', null, 'id comparativa', null, '0', 1);

/* ----------------------------------------------------------------- */ /* ------------------------------------------------------------ */
-- rs_ejecutables (id, ejecutable, id_tipo, id_aplicacion, SALIDA)		/* 		MODULO			DESCRIPCION 							*/
insert into rs_ejecutables values 										/* ------------------------------------------------------------ */
	(152, 'CXP-001-01-DA.jasper', 				1,  5, ''),					/* CUENTAS POR PAGAR 	REPOSICION DE CAJA CHICA 			 	*/
	(153, 'CXP-002-01-DA.jasper', 				1,  5, ''),					/* CUENTAS POR PAGAR 	REPOSICION DE VIATICOS 				 	*/
	(154, 'CXC-001-01-DA.jasper', 				5,  6, ''),					/* CUENTAS POR COBRAR 	CONCENTRADO DE INGRESOS 			 	*/
	(155, 'CXC-002-01-DA.jasper', 				5,  6, ''),					/* CUENTAS POR COBRAR 	Cobranza 							 	*/
	(156, 'facturas_concentrado.jasper', 		5,  6, ''), 				/* CUENTAS POR COBRAR 	Facturas activas y canceladas		 	*/
	(157, 'PPT-01.jasper', 						5,  7, ''),		 			/* GESTION PROYECTOS 	Formato PPT-01 V-5 					 	*/
	(158, 'obras_satics.jasper', 				5,  7, ''),		 			/* GESTION PROYECTOS 	Satics por Obra 					 	*/
	(159, 'estado_cuenta_cobranza_obra.jasper', 5,  7, ''),		 			/* GESTION PROYECTOS 	Estado de cuentas Cobranza por Obra 	*/
	(160, 'infonavit.jasper', 					5,  8, ''),		 			/* RECURSOS HUMANOS 	Infonavit empleados 				 	*/
	(161, 'lista_raya.jasper', 					5,  8, ''),		 			/* RECURSOS HUMANOS 	Lista de raya de empleados 			 	*/
	(162, 'nomina.jasper', 						5,  8, ''),		 			/* RECURSOS HUMANOS 	Nomina de empleados 				 	*/
	(163, 'existencias_almacen.jasper', 		5,  9, ''),		 			/* INVENTARIOS 			Existencias de productos			 	*/
	(164, 'orden_compra.jasper', 				5, 10, ''),		 			/* COMPRAS 			 	Ordenes de compras 					 	*/
	(165, 'requisicion_material.jasper', 		5, 10, ''),		 			/* COMPRAS 			 	Requisicion de materiales 			 	*/
	(166, 'cuentas_bancarias.jasper', 			5, 11, ''),		 			/* TESORERIA 			CUENTAS_BANCARIAS 						*/
	(167, 'cotizacion_material.jasper', 		5, 10, ''),		 			/* COMPRAS 				Cotizacion de materiales				*/
	(168, 'factura.jasper', 					1,  6, ''),		 			/* COMPRAS 				Cotizacion de materiales				*/
	(169, 'net.giro.comparativa.Comparativa', 	2,  7, 'Comparativa.xls');	/* GESTION PROYECTOS 	Comparativa								*/

-- rs_programas (id, programa, titulo, descripcion, id_ejecutable)
insert into rs_programas values
	(152, 'Caja Chica reposicion','Caja Chica', 'Caja Chica', 152),
	(153, 'Viaticos reposicion','Viaticos', 'Reporte de reposicion de viaticos', 153),
	(154, 'Concentrado de ingresos','Ingresos', 'Reporte de concentrado de ingresos', 154),
	(155, 'Cobranza','Cobranza', 'Reporte de Cobranza', 155),
	(156, 'Concentrado de Facturas', 'Concentrado de Facturas', 'Reporte de Concentrado de Facturas', 156),
	(157, 'Formato PPT-01 V-5', 'Formato PPT-01 V-5', 'Reporte de Formato PPT-01 V-5', 157),
	(158, 'Satics de Obras', 'Satics de Obras', 'Reporte de Satics de Obras', 158),
	(159, 'Estado de Cuenta de Cobranza por Obra', 'Estado de Cuenta de Cobranza por Obra', 'Reporte de Estado de Cuenta de Cobranza por Obra', 159),
	(160, 'Infonavit', 'Infonavit', 'Reporte de Infonavit', 160),
	(161, 'Lista de Raya', 'Lista de Raya', 'Reporte de Lista de Raya', 161),
	(162, 'Nomina', 'Nomina', 'Reporte de Nomina', 162),
	(163, 'Existencias por Almacen', 'Existencias por Almacen', 'Reporte de Existencias por Almacen', 163),
	(164, 'Orden de Compra', 'Orden de Compra', 'Reporte de Orden de Compra', 164),
	(165, 'Requisicion de Material', 'Requisicion de Material', 'Reporte de Requisicion de Material', 165),
	(166, 'Cuentas Bancarias', 'Cuentas Bancarias', 'Reporte de Cuentas Bancarias', 166),
	(167, 'Cotizacion de Material', 'Cotizacion de Material', 'Reporte de Cotizacion de Material', 167),
	(168, 'Factura', 'Factura', 'Reporte de Factura', 168),
	(169, 'Comparativa', 'Comparatia', 'Reporte de Comparativa', 169);

-- rs_programas_parametros (id, id_programa, id_parametro)
insert into rs_programas_parametros values
	/* pgUsuario      pgPassword        pgDriver         ruta_img            pgUrl       SUBREPORT_DIR(CXP)  pagosGastosId 					*/
	(1055, 152, 5), (1056, 152, 6), (1057, 152, 11), (1058, 152, 186), (1059, 152, 237), (1060, 152, 236), (1061, 152, 235),
	/* pgUsuario      pgPassword        pgDriver         ruta_img            pgUrl       SUBREPORT_DIR(CXP)  idGasto 					*/
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
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 idOrdenCompra 										*/
	(1140, 164, 5), (1141, 164, 6), (1142, 164, 11), (1143, 164, 237), (1144, 164, 186), (1145, 164, 246),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 idRequisicion 										*/
	(1146, 165, 5), (1147, 165, 6), (1148, 165, 11), (1149, 165, 237), (1150, 165, 186), (1151, 165, 247),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 	fechaDesde 		  fechaHasta 					*/
	(1152, 166, 5), (1153, 166, 6), (1154, 166, 11), (1155, 166, 237), (1156, 166, 186), (1157, 166, 241), (1158, 166, 242),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 idCotizacion										*/
	(1164, 167, 5), (1165, 167, 6), (1166, 167, 11), (1167, 167, 237), (1168, 167, 186), (1169, 167, 253),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	 idFactura											*/
	(1170, 168, 5), (1171, 168, 6), (1172, 168, 11), (1173, 168, 237), (1174, 168, 186), (1175, 168, 254),
	/* pgUsuario 	  pgPassword 		pgDriver 		   pgUrl 		   ruta_img 	      idObra		  idFamilia		  idRequisicion	*/
	(1181, 169, 5), (1182, 169, 6), (1183, 169, 11), (1184, 169, 237), (1185, 169, 186), (1186, 169, 245), (1187, 169, 244), (1188, 169, 247), (1188, 169, 257);

-- rs_schedulers_programas (id, id_scheduler, id_programa)
-- NOTA: Antes, el scheduler era el 3, ahora debe ser el 1: 1=DISESA, 3=ICAPITAL
insert into rs_schedulers_programas values
	(140, 1, 152),
	(141, 1, 153),
	(142, 1, 154),
	(143, 1, 155),
	(144, 1, 156),
	(145, 1, 157),
	(146, 1, 158),
	(147, 1, 159),
	(148, 1, 160),
	(149, 1, 161),
	(150, 1, 162),
	(151, 1, 163),
	(152, 1, 164),
	(153, 1, 165),
	(154, 1, 166),
	(155, 1, 167),
	(156, 1, 168),
	(157, 1, 169);

-- rs_subgrupos_programas (id, id_subgrupo, id_programa)
insert into rs_subgrupos_programas values
	(143,  6, 152), /*  6: CXP 			*/
	(144,  6, 153), /*  6: CXP 			*/
	(145,  7, 154), /*  7: CXC 			*/
	(146,  7, 155), /*  7: CXC 			*/
	(147,  7, 156), /*  7: CXC 			*/
	(148,  8, 157), /*  8: GP 			*/
	(149,  8, 158), /*  8: GP 			*/
	(150,  8, 159), /*  8: GP 			*/
	(151,  9, 160), /*  9: RH 			*/
	(152,  9, 161), /*  9: RH 			*/
	(153,  9, 162), /*  9: RH 			*/
	(154, 10, 163), /* 10: INVENTARIOS 	*/
	(155, 11, 164), /* 11: COMPRAS 		*/
	(156, 11, 165), /* 11: COMPRAS 		*/
	(157, 12, 166), /* 12: TESORERIA 	*/
	(158, 11, 167), /* 11: COMPRAS 		*/
	(159,  7, 168), /*  7: CXC 			*/
	(160,  8, 169); /*  8: GP 			*/


-- Grupo de parametros
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
	(11, 'SQL para gastos (pagos gastos tipo G)', 1),
	(12, 'SQL para comparativas', 1);

-- propiedades de grupo de parametros
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
	(31, 11, 'sql',   'SELECT o.pagos_gastos_id as clave, UPPER(o.beneficiario || '' - $ '' || trim(to_char(o.monto, ''999,999,999,999,990.00''))) AS valor FROM pagos_gastos o WHERE o.tipo = ''G'' AND o.estatus = ''C'' ORDER BY o.beneficiario;'),
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

### COMPARATIVA :: Agrego parametro idComparativa a Reporte de comparativa
INSERT INTO rs_parametros_grupos (id,parametro_grupo,id_origen_datos) VALUES (15, 'SQL para comparativas', 1);
INSERT INTO rs_parametros_grupos_propiedades (id, id_parametro_grupo, llave, valor) VALUES 
	(43, 15, 'sql',   'SELECT id AS clave, descripcion || '' - '' || nombre_obra AS valor FROM comparativa ORDER BY id DESC;'),
	(44, 15, 'jar',   '/opt/rsprocesos/ext/datos/parametros/sql/SQL-1.0.jar'), 
	(45, 15, 'clase', 'SqlValores');
INSERT INTO rs_parametros VALUES (266, 'id comparativa', 'idComparativa', 's', 'g', null, null, 1, 'idComparativa', null, 'id comparativa', null, '0', 15, 1);
INSERT INTO rs_programas_parametros VALUES (1247, 169, 266, 1);