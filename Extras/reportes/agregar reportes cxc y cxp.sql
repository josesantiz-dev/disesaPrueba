
insert into rs_aplicaciones values(5,'Cuentas por Pagar');
insert into rs_aplicaciones values(6,'Cuentas por Cobrar');
insert into rs_aplicaciones values(7,'Gestion de Proyectos');
insert into rs_aplicaciones values(8,'Recursos Humanos');

insert into rs_subgrupos values(6,'Cxp','Cuentas por Pagar');
insert into rs_subgrupos values(7,'Cxc','Cuentas por Cobrar');
insert into rs_subgrupos values(8,'GP','Gestion de Proyectos');
insert into rs_subgrupos values(9,'RH','Recursos Humanos');

insert into rs_grupos values(3,'Cxp','Cuentas por Pagar');
insert into rs_grupos values(4,'Cxc','Cuentas por Cobrar');
insert into rs_grupos values(5,'GP','Gestion de Proyectos');
insert into rs_grupos values(6,'RH','Recursos Humanos');

insert into rs_grupos_subgrupos values(6,3,6);
insert into rs_grupos_subgrupos values(7,4,7);
insert into rs_grupos_subgrupos values(8,5,8);
insert into rs_grupos_subgrupos values(9,6,9);

insert into rs_asignacion_grupos values(6,3,3);
insert into rs_asignacion_grupos values(7,3,4);
insert into rs_asignacion_grupos values(8,3,5);
insert into rs_asignacion_grupos values(9,3,6);

insert into rs_aplicaciones_tipos values(10,1,'/opt/rsprocesos/ejecutables/cxp/reportes/',5);
insert into rs_aplicaciones_tipos values(11,5,'/opt/rsprocesos/ejecutables/cxp/reportes/',5);
insert into rs_aplicaciones_tipos values(12,6,'/opt/rsprocesos/ejecutables/cxp/reportes/',5);

insert into rs_aplicaciones_tipos values(13,1,'/opt/rsprocesos/ejecutables/cxc/reportes/',6);
insert into rs_aplicaciones_tipos values(14,5,'/opt/rsprocesos/ejecutables/cxc/reportes/',6);
insert into rs_aplicaciones_tipos values(15,6,'/opt/rsprocesos/ejecutables/cxc/reportes/',6);

insert into rs_aplicaciones_tipos values(16,1,'/opt/rsprocesos/ejecutables/gp/reportes/',7);
insert into rs_aplicaciones_tipos values(17,5,'/opt/rsprocesos/ejecutables/gp/reportes/',7);
insert into rs_aplicaciones_tipos values(18,6,'/opt/rsprocesos/ejecutables/gp/reportes/',7);

insert into rs_aplicaciones_tipos values(19,1,'/opt/rsprocesos/ejecutables/rh/reportes/',8);
insert into rs_aplicaciones_tipos values(20,5,'/opt/rsprocesos/ejecutables/rh/reportes/',8);
insert into rs_aplicaciones_tipos values(21,6,'/opt/rsprocesos/ejecutables/rh/reportes/',8);

insert into rs_parametros values(235,'pagos gastos id','pagosGastosId','s','N','','',1,'','','','','');
insert into rs_parametros values(236,'Ruta subreportes CXP','SUBREPORT_DIR','s','N','','',1,'','','','','/opt/rsprocesos/ejecutables/cxp/reportes/');
insert into rs_parametros values(237,'URL Conexion base de giro','pgUrl','s','N','','',1,'','','','','jdbc:postgresql://localhost:9450/giro');
insert into rs_parametros values(238,'Ruta subreportes CXC','SUBREPORT_DIR','s','N','','',1,'','','','','/opt/rsprocesos/ejecutables/cxc/reportes/');

-- --------------------------------------------------------------------------------------------------------------
-- MODULO 		CUENTAS POR PAGAR
-- DESCRIPCION 	REPORTE DE REPOSICION DE CAJA CHICA
-- REPORTE 		CXP-001-01-DA
-- --------------------------------------------------------------------------------------------------------------
insert into rs_ejecutables values(152,'CXP-001-01-DA.jasper',1,5);

insert into rs_programas values(152,'Caja Chica reposicion','Caja Chica', 'Caja Chica', 152);

insert into rs_programas_parametros values(1055,152,5); 	-- pgUsuario
insert into rs_programas_parametros values(1056,152,6); 	-- pgPassword
insert into rs_programas_parametros values(1057,152,11); 	-- pgDriver
insert into rs_programas_parametros values(1058,152,186); 	-- ruta_img
insert into rs_programas_parametros values(1059,152,237);	-- pgUrl
insert into rs_programas_parametros values(1060,152,236);	-- SUBREPORT_DIR
insert into rs_programas_parametros values(1061,152,235);	-- pagosGastosId

insert into rs_schedulers_programas  values(140,3,152);

insert into rs_subgrupos_programas values(143,6,152);

-- --------------------------------------------------------------------------------------------------------------
-- MODULO 		CUENTAS POR PAGAR
-- DESCRIPCION 	REPORTE DE REPOSICION DE VIATICOS
-- REPORTE 		CXP-002-01-DA
-- --------------------------------------------------------------------------------------------------------------
insert into rs_ejecutables values(153,'CXP-002-01-DA.jasper',1,5);

insert into rs_programas values(153,'Viaticos reposicion','Viaticos', 'Reporte de reposicion de viaticos', 153);

insert into rs_programas_parametros values(1062,153,5); 	-- pgUsuario
insert into rs_programas_parametros values(1063,153,6); 	-- pgPassword
insert into rs_programas_parametros values(1064,153,11); 	-- pgDriver
insert into rs_programas_parametros values(1065,153,186); 	-- ruta_img
insert into rs_programas_parametros values(1066,153,237);	-- pgUrl
insert into rs_programas_parametros values(1067,153,236);	-- SUBREPORT_DIR
insert into rs_programas_parametros values(1068,153,235);	-- pagosGastosId

insert into rs_schedulers_programas values(141,3,153);

insert into rs_subgrupos_programas  values(144,6,153);

-- --------------------------------------------------------------------------------------------------------------
-- MODULO 		CUENTAS POR COBRAR
-- DESCRIPCION 	CONCENTRADO DE INGRESOS
-- REPORTE 		CXC-001-01-DA
-- --------------------------------------------------------------------------------------------------------------
insert into rs_ejecutables values(154,'CXC-001-01-DA.jasper',1,6);

insert into rs_programas values(154,'Concentrado de ingresos','Ingresos', 'Reporte de concentrado de ingresos', 154);

insert into rs_programas_parametros values(1069,154,5); 	-- pgUsuario
insert into rs_programas_parametros values(1070,154,6); 	-- pgPassword
insert into rs_programas_parametros values(1071,154,11); 	-- pgDriver
insert into rs_programas_parametros values(1072,154,237); 	-- pgUrl
insert into rs_programas_parametros values(1073,154,186); 	-- ruta_img
insert into rs_programas_parametros values(1074,154,207); 	-- FECHA_INICIAL 	
insert into rs_programas_parametros values(1075,154,208); 	-- FECHA_FINAL		

insert into rs_schedulers_programas values(142,3,154);

insert into rs_subgrupos_programas  values(145,6,154);

-- --------------------------------------------------------------------------------------------------------------
-- MODULO 		CUENTAS POR COBRAR
-- DESCRIPCION 	COBRANZA
-- REPORTE 		CXC-002-01-DA
-- --------------------------------------------------------------------------------------------------------------
insert into rs_ejecutables values(155,'CXC-002-01-DA.jasper',1,6);

insert into rs_programas values(155,'Cobranza','Cobranza', 'Reporte de Cobranza', 155);

insert into rs_programas_parametros values(1076,155,5); 	-- pgUsuario
insert into rs_programas_parametros values(1077,155,6); 	-- pgPassword
insert into rs_programas_parametros values(1078,155,11); 	-- pgDriver
insert into rs_programas_parametros values(1079,155,237); 	-- pgUrl
insert into rs_programas_parametros values(1080,155,186); 	-- ruta_img
insert into rs_programas_parametros values(1081,155,207); 	-- FECHA_INICIAL
insert into rs_programas_parametros values(1082,155,208); 	-- FECHA_FINAL

insert into rs_schedulers_programas values(143,3,155);

insert into rs_subgrupos_programas  values(146,6,155);