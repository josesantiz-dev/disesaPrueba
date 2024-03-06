package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxp.beans.GastosImpuesto;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface GastosImpuestoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(GastosImpuesto entity) throws Exception;

	public List<GastosImpuesto> saveOrUpdateList(List<GastosImpuesto> entities) throws Exception;

	public void update(GastosImpuesto entity) throws Exception;
	
	public void delete(GastosImpuesto entity) throws Exception;
	
	public GastosImpuesto findById(Long idGastosImpuesto);

	public List<GastosImpuesto> findAll();

	public List<GastosImpuesto> findAll(long idGasto, String orderBy);

	public List<GastosImpuesto> findAllByImpuesto(long idImpuesto, String orderBy);
	
	public List<GastosImpuesto> findByProperty(String propertyName, Object value);
	
	public List<GastosImpuesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value);

	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------
	
	public Long save(GastosImpuestoExt entityExt) throws Exception;
	
	public void update(GastosImpuestoExt entity) throws Exception;

	public void delete(GastosImpuestoExt entityExt) throws Exception;

	public List<GastosImpuestoExt> findAllExt(long idGasto, String orderBy);

	public List<GastosImpuestoExt> findAllByImpuestoExt(long idImpuesto, String orderBy);
	
	public List<GastosImpuestoExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);

}
