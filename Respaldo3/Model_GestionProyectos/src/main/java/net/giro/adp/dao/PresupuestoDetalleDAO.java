package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.PresupuestoDetalle;

@Remote
public interface PresupuestoDetalleDAO extends DAO<PresupuestoDetalle> {
	public long save(PresupuestoDetalle entity, long codigoEmpresa) throws Exception;
	
	public List<PresupuestoDetalle> saveOrUpdateList(List<PresupuestoDetalle> entities, long codigoEmpresa) throws Exception;

	public List<PresupuestoDetalle> findAll(long idPresupuesto, String orderBy);

	public List<PresupuestoDetalle> findByProperty(String propertyName, final Object value, String orderBy, int limite);

	public List<PresupuestoDetalle> findLikeProperty(String propertyName, final Object value, String orderBy, int limite);
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase PresupuestoDetalleDAO