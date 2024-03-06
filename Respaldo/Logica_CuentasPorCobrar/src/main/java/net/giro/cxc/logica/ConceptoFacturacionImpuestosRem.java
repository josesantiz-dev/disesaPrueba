package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.ConceptoFacturacion;
import net.giro.cxc.beans.ConceptoFacturacionImpuestos;
import net.giro.cxc.beans.ConceptoFacturacionImpuestosExt;

@Remote
public interface ConceptoFacturacionImpuestosRem {
	public void showSystemOuts(boolean value);
	
	public long save(ConceptoFacturacionImpuestos entity) throws Exception;
	public long save(ConceptoFacturacionImpuestosExt entityExt) throws Exception;
	
	public void delete(Long id) throws Exception;
	
	public void update(ConceptoFacturacionImpuestos entity) throws Exception;
	public void update(ConceptoFacturacionImpuestosExt entityExt) throws Exception;

	public ConceptoFacturacionImpuestos findById(Long id);
	
	public List<ConceptoFacturacionImpuestos> findAll();

	public List<ConceptoFacturacionImpuestos> findByProperty(String propertyName, Object value);
	public List<ConceptoFacturacionImpuestosExt> findByPropertyExt(String propertyName, Object value);

	public List<ConceptoFacturacionImpuestos> findLikeProperty(String propertyName, Object value);
	public List<ConceptoFacturacionImpuestosExt> findLikePropertyExt(String propertyName, Object value);
	
	public List<ConceptoFacturacionImpuestos> findByConcepto(ConceptoFacturacion idConcepto);
}
