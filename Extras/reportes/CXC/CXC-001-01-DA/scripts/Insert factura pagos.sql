-- Insert factura pagos

truncate table factura_pagos;

INSERT INTO factura_pagos(
            id, id_factura, id_forma_pago, id_banco_origen, monto, fecha, 
            estatus, observaciones, fecha_creacion, creado_por, fecha_modificacion, 
            modificado_por, cuenta_deposito)
    VALUES ( 1, 10000001, 10000031 , 10000005, 4000.00, current_date, 
            0, 'NINGUNA', current_date, 1, current_date, 
            1, '247536981');



INSERT INTO factura_pagos(
            id, id_factura, id_forma_pago, id_banco_origen, monto, fecha, 
            estatus, observaciones, fecha_creacion, creado_por, fecha_modificacion, 
            modificado_por, cuenta_deposito)
    VALUES ( 2, 10000002, 10000032 , 10000004, 2500.23, current_date, 
            0, 'NINGUNA', current_date, 1, current_date, 
            1, '247536982');


INSERT INTO factura_pagos(
            id, id_factura, id_forma_pago, id_banco_origen, monto, fecha, 
            estatus, observaciones, fecha_creacion, creado_por, fecha_modificacion, 
            modificado_por, cuenta_deposito)
    VALUES ( 3, 10000004, 10000033, 10000005, 2500.28, current_date, 
            1, 'NINGUNA', current_date, 1, current_date, 
            1, '247536983');

INSERT INTO factura_pagos(
            id, id_factura, id_forma_pago, id_banco_origen, monto, fecha, 
            estatus, observaciones, fecha_creacion, creado_por, fecha_modificacion, 
            modificado_por, cuenta_deposito)
    VALUES ( 4, 10000017, 10000033, 10000006, 2500.28, current_date, 
            1, 'NINGUNA', current_date, 1, current_date, 
            1, '247536984');


update obra set id_cliente = 10000027 where id_obra = 1
update obra set id_cliente = 10000028 where id_obra = 2
update obra set id_cliente = 10000029 where id_obra = 3


-- cuentas_banco
-- delete from a95f1327c6 where aa != 10000043;
INSERT INTO a95f1327c6( aa, ab, ac, ad, ae, af, ah ) VALUES (10000042, 1, current_date, 10017002, current_date, '247536981', 10000004);
INSERT INTO a95f1327c6( aa, ab, ac, ad, ae, af, ah ) VALUES (10000046, 1, current_date, 10017002, current_date, '247536982', 10000005);
INSERT INTO a95f1327c6( aa, ab, ac, ad, ae, af, ah ) VALUES (10000044, 1, current_date, 10017002, current_date, '247536983', 10000003);
INSERT INTO a95f1327c6( aa, ab, ac, ad, ae, af, ah ) VALUES (10000045, 1, current_date, 10017002, current_date, '247536984', 10000002);
/*

Select * from factura
select * from formas_pagos -- 10000031, 10000032, 10000033
select * from cat_bancos
Select * from a95f1327c6 cuentas_banco


Select * from factura_pagos
*/