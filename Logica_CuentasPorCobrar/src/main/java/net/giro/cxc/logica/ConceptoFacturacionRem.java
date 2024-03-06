package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.ConceptoFacturacion;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ConceptoFacturacionRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ConceptoFacturacion entity) throws Exception;
	
	public void delete(ConceptoFacturacion entity) throws Exception;
	
	public void update(ConceptoFacturacion entity) throws Exception;

	public ConceptoFacturacion findById(Long idConceptoFacturacion) throws Exception;

	public List<ConceptoFacturacion> findAll() throws Exception;

	public List<ConceptoFacturacion> findAll(boolean incluyeCanceladas, String orderBy) throws Exception;
	
	public List<ConceptoFacturacion> findLike(String value, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<ConceptoFacturacion> findLikeProperty(String propertyName, String value, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<ConceptoFacturacion> findLikeProperty(String propertyName, String value, int limite) throws Exception;

	public List<ConceptoFacturacion> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<ConceptoFacturacion> findByProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<ConceptoFacturacion> findByPropertyPojoCompleto(String propertyName, String tipo, long value) throws Exception;
}
