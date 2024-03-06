package net.giro.contabilidad.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.ImpuestoEquivalencia;

@Remote
public interface ImpuestoEquivalenciaDAO extends DAO<ImpuestoEquivalencia> {
	public long save(ImpuestoEquivalencia entity, long codigoEmpresa) throws Exception;
	
	public List<ImpuestoEquivalencia> saveOrUpdateList(List<ImpuestoEquivalencia> entities, long codigoEmpresa) throws Exception;

	public List<ImpuestoEquivalencia> findAll(long codigoTransaccion, String orderBy) throws Exception;

	public List<ImpuestoEquivalencia> findByProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ImpuestoEquivalencia> findLikeProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<ImpuestoEquivalencia> findInProperty(String propertyName, List<Object> values, long idEmpresa, String orderBy, int limite) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2019-05-17 | Javier Tirado 	| Creacion de ImpuestoEquivalenciaDAO
 */
