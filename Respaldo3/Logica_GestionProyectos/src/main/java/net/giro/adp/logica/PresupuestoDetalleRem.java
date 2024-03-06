package net.giro.adp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.PresupuestoDetalle;
import net.giro.adp.beans.PresupuestoDetalleExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface PresupuestoDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(PresupuestoDetalle entity) throws Exception;
	
	public List<PresupuestoDetalle> saveOrUpdateList(List<PresupuestoDetalle> entities) throws Exception;
	
	public void update(PresupuestoDetalle entity) throws Exception;
	
	public void delete(long idPresupuestoDetalle) throws Exception;

	public PresupuestoDetalle findById(long idPresupuestoDetalle);

	public List<PresupuestoDetalle> findAll(long idPresupuesto);

	public List<PresupuestoDetalle> findAll(long idPresupuesto, String orderBy);

	public List<PresupuestoDetalle> findByProperty(String propertyName, Object value, int limite);

	public List<PresupuestoDetalle> findByProperty(String propertyName, Object value, String orderBy, int limite);

	public List<PresupuestoDetalle> findLikeProperty(String propertyName, Object value, int limite);

	public List<PresupuestoDetalle> findLikeProperty(String propertyName, Object value, String orderBy, int limite);

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	public Long save(PresupuestoDetalleExt entityExt) throws Exception;
	
	public void update(PresupuestoDetalleExt entityExt) throws Exception;
	
	public PresupuestoDetalleExt findByIdExt(long idPresupuestoDetalle) throws Exception;

	public List<PresupuestoDetalleExt> findAllExt(long idPresupuesto);

	public List<PresupuestoDetalleExt> findByPropertyExt(String propertyName, Object value, int limite) throws Exception;

	public List<PresupuestoDetalleExt> findByPropertyExt(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	public List<PresupuestoDetalleExt> findLikePropertyExt(String propertyName, Object value, int limite) throws Exception;
	
	public List<PresupuestoDetalleExt> findLikePropertyExt(String propertyName, Object value, String orderBy, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase
