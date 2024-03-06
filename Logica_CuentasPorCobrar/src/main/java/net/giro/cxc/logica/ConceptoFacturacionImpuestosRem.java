package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.ConceptoFacturacion;
import net.giro.cxc.beans.ConceptoFacturacionImpuestos;
import net.giro.cxc.beans.ConceptoFacturacionImpuestosExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ConceptoFacturacionImpuestosRem {
	public void showSystemOuts(boolean value);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ConceptoFacturacionImpuestos entity) throws Exception;
	
	public List<ConceptoFacturacionImpuestos> saveOrUpdateList(List<ConceptoFacturacionImpuestos> entities) throws Exception;

	public void update(ConceptoFacturacionImpuestos entity) throws Exception;
	
	public void delete(long idConceptoFacturacionImpuestos) throws Exception;
	
	public ConceptoFacturacionImpuestos findById(long idConceptoFacturacionImpuestos);
	
	public List<ConceptoFacturacionImpuestos> findAll(long idConceptoFacturacion);

	public List<ConceptoFacturacionImpuestos> findByProperty(String propertyName, Object value);
	
	public List<ConceptoFacturacionImpuestos> findLikeProperty(String propertyName, Object value);
	
	public List<ConceptoFacturacionImpuestos> findByConcepto(ConceptoFacturacion conceptoFacturacion);

	public ConceptoFacturacionImpuestos convertir(ConceptoFacturacionImpuestosExt extendido);
	
	public ConceptoFacturacionImpuestosExt convertir(ConceptoFacturacionImpuestos entity);
	
	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------

	public long save(ConceptoFacturacionImpuestosExt entityExt) throws Exception;

	public void update(ConceptoFacturacionImpuestosExt entityExt) throws Exception;

	public List<ConceptoFacturacionImpuestosExt> findAllExt(long idConceptoFacturacion);

	public List<ConceptoFacturacionImpuestosExt> findByPropertyExt(String propertyName, Object value);

	public List<ConceptoFacturacionImpuestosExt> findLikePropertyExt(String propertyName, Object value);
}
