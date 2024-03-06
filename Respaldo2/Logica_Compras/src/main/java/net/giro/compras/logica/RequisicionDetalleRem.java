package net.giro.compras.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.beans.RequisicionDetalleExt;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface RequisicionDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void OrderBy(String orderBy);

	public Long save(RequisicionDetalle entity) throws ExcepConstraint;
	
	public void update(RequisicionDetalle entity) throws ExcepConstraint;
	
	public List<RequisicionDetalle> saveOrUpdateList(List<RequisicionDetalle> entities) throws Exception;
	
	public void delete(Long entity) throws ExcepConstraint;

	public RequisicionDetalle findById(Long id);
	
	public List<RequisicionDetalle> findAll(Long idRequisicion) throws Exception;
	
	public List<RequisicionDetalle> findByProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<RequisicionDetalle> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<RequisicionDetalle> findInProperty(String propertyName, List<Object> values) throws Exception;

	public List<RequisicionDetalle> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<RequisicionDetalle> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public RequisicionDetalle convertir(RequisicionDetalleExt pojoTarget) throws Exception;

	public RequisicionDetalleExt convertir(RequisicionDetalle pojoTarget) throws Exception;
	
	// ----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------------------------------------------------------------
	
	public Long save(RequisicionDetalleExt entityExt) throws ExcepConstraint;
	
	public void update(RequisicionDetalleExt entityExt) throws ExcepConstraint;
	
	public RequisicionDetalleExt findExtById(Long id) throws Exception;
	
	public List<RequisicionDetalleExt> findExtAll(Long idRequisicion) throws Exception;
	
	public List<RequisicionDetalleExt> findExtByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<RequisicionDetalleExt> findExtLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<RequisicionDetalleExt> findExtInProperty(String propertyName, List<Object> values) throws Exception;
	
	public List<RequisicionDetalleExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<RequisicionDetalleExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
