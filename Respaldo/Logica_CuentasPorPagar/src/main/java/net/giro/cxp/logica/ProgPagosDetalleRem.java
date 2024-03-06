package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxp.beans.ProgPagosDetalle;
import net.giro.cxp.beans.ProgPagosDetalleExt;

@Remote
public interface ProgPagosDetalleRem {
	public Long save(ProgPagosDetalleExt entity) throws Exception;
	
	public void update(ProgPagosDetalle entity) throws Exception;
	public void update(ProgPagosDetalleExt entity) throws Exception;

	public void delete(ProgPagosDetalleExt entity) throws Exception;

	public ProgPagosDetalleExt findById(Long id) throws Exception;
	
	public List<ProgPagosDetalleExt> findByProperty(String propertyName,Object value) throws Exception;	
	
	public List<ProgPagosDetalleExt> findByPropertyPojoCompleto(String propertyName,final Object value) throws Exception;
	
	public List<ProgPagosDetalleExt> findByPropertyPojoCompletoMontoNoCero(String propertyName,final Object value) throws Exception;
	
	public List<ProgPagosDetalleExt> findByAgenteEstatusMontoConcepto(String agenteValue, String estatusValue, Long conceptoValue) throws Exception;
	
	public List<ProgPagosDetalleExt> findAll() throws Exception;
	
	public List<ProgPagosDetalle> findByMontoConceptoEstatus(Double montoValue, Long conceptoValue, String estatusValue) throws Exception;
	
	public List<ProgPagosDetalleExt> findByMontoConceptoEstatusExt(Double montoValue, Long conceptoValue, String estatusValue) throws Exception;
}
