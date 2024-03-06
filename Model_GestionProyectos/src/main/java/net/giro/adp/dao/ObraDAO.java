package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.Obra;

@Remote
public interface ObraDAO extends DAO<Obra> {
	public long save(Obra entity, long codigoEmpresa) throws Exception;
	
	public List<Obra> saveOrUpdateList(List<Obra> entities, long codigoEmpresa) throws Exception;
	
	public List<Obra> findAll(long idObraPrincipal, boolean incluyeCanceladas, int revisadas, int autorizadas, long idEmpresa, String orderBy) throws Exception;

	public List<Obra> findLike(String value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, int revisadas, int autorizadas, int jerarquia, long idEmpresa, String orderBy, int limite) throws Exception; 

	public List<Obra> findLikeProperty(String propertyName, Object value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, int revisadas, int autorizadas, int jerarquia, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<Obra> findByProperty(String propertyName, Object value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, int revisadas, int autorizadas, int jerarquia, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Obra> findInProperty(String propertyName, List<Object> values, long idEmpresa, String orderBy) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-17 | Javier Tirado 	| Añado los metodos orderBy, findByProperties y findLikeProperties. Normal y extendido
 * 1.2 | 2017-01-12 | Javier Tirado 	| Añado los metodos findByMultiProperties y findLikeMultiProperties.
 */