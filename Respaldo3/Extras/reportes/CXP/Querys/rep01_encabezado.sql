
	-- (Select perfilvalor.ai as efectivo from b761110ccfe perfil inner join b761110ccfe perfilvalor on perfil.aa = perfilvalor.af where perfil.aa = 10000033) as efectivo,

CXP-001-01-DA

Select
	monto_limite as efectivo,
	pg.area as area, o.id_obra, o.nombre, pg.agente as sucursal, pg.monto as reembolso, 

	pg.consecutivo, no_beneficiario, case pg.tipo_benef when 'P' then persona.ag else negocio.af end as beneficiario
From
	pagos_gastos pg inner join obra o on pg.id_obra = o.id_obra
	
	left join c81498d458 persona on pg.no_beneficiario = persona.aa
	left join e769c352b7 negocio on pg.no_beneficiario = negocio.aa
	
Where
	pg.pagos_gastos_id = 10000070
	-- 


Select
	monto_limite as efectivo,
	pg.area as area, o.id_obra, o.nombre, agente.af as sucursal, pg.monto as reembolso,

	pg.consecutivo, no_beneficiario, case pg.tipo_benef when 'P' then persona.ag else negocio.af end as beneficiario
From
	pagos_gastos pg inner join obra o on pg.id_obra = o.id_obra

	left join c81498d458 persona on pg.no_beneficiario = persona.aa
	left join e769c352b7 negocio on pg.no_beneficiario = negocio.aa

	left join a535303dbc agente on agente.aa = cast(pg.agente as integer)

Where
	pg.pagos_gastos_id = $P{pagosGastosId}




select * from pagos_gastos_det --conceptoid -> relacion de la misma tabla


-- tipo_egreso
comvalores atributo1 correspondo a otro valor de la convalores, al id (aa)

cat_gastos cg cambiar por convalores

-- Estoy suponiendo que voy a buscar por pg.pagos_gastos_id = 10000069

	-- and perfiles.aa = 10000033
/*
Select * from pagos_gastos where id_obra = 1
select * from obra
Select * from d7729f32ba7 where aa = 10000033 -- Perfiles -> LIMITE_CAJA_CHICA

Select * from b761110ccfe where af = 10000033 -- PerfilValor

Select perfilvalor.ai as efectivo from b761110ccfe perfil inner join b761110ccfe perfilvalor on perfil.aa = perfilvalor.af where perfil.aa = 10000033 -- > Obtener el valor 'efectivo'

Select * from dc8deac2731

Select * from d7729f32ba7 perfil where aa = 10000033 -- Perfiles -> LIMITE_CAJA_CHICA
Select * from b2a12997b2b perfil_acceso where aa = 10000033 -- perfil_acceso 

Select aa as id, ag as nivel, ah as valornivel, ai as valorperfil, ab as creadopor, ad as modificadopor from b761110ccfe


Select pv.ai as efectivo from b761110ccfe p inner join b761110ccfe pv on p.aa = pv.af where p.aa = 10000033 -- > Obtener el valor 'efectivo'

*/