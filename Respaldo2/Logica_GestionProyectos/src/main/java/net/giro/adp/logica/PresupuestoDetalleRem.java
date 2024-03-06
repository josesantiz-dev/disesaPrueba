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
	
	public void delete(Long id) throws Exception;
	
	public List<PresupuestoDetalle> findAll();

	public PresupuestoDetalle findById(Long id);

	public List<PresupuestoDetalle> findByProperty(String propertyName, Object value, int max);

	public List<PresupuestoDetalle> findLikeProperty(String propertyName, final Object value, int max);

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	public Long save(PresupuestoDetalleExt entityExt) throws Exception;
	
	public void update(PresupuestoDetalleExt entityExt) throws Exception;
	
	public PresupuestoDetalleExt findByIdExt(Long id) throws Exception;
	
	public List<PresupuestoDetalleExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception;
	
	public List<PresupuestoDetalleExt> findLikePropertyExt(String propertyName, final Object value, int max) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase
