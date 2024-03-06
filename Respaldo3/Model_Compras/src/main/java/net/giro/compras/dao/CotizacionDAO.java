package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.Cotizacion;

@Remote
public interface CotizacionDAO extends DAO<Cotizacion> {
    public long save(Cotizacion entity, long codigoEmpresa) throws Exception;
    
    public List<Cotizacion> saveOrUpdateList(List<Cotizacion> entities, long codigoEmpresa) throws Exception;

	public List<Cotizacion> findAll(long idObra, int estatus, String orderBy) throws Exception;

	public List<Cotizacion> findLike(String value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Cotizacion> findLikeProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Cotizacion> findByProperty(String propertyName, final Object value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception;

	public int findConsecutivoByProveedor(long idProveedor, long idEmpresa) throws Exception;
	
	public List<Cotizacion> busquedaDinamica(String value, long idEmpresa) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. 
 */