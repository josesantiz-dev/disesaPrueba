package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.cxp.beans.ProgPagosDetalle;

@Remote
public interface ProgPagosDetalleDAO extends DAO<ProgPagosDetalle> {
	//public long save(ProgPagosDetalle entity);
	
	//public void update(ProgPagosDetalle entity);

	//public void delete(ProgPagosDetalle entity);

	public ProgPagosDetalle findById(Long id);
	
	public List<ProgPagosDetalle> findByProperty(String propertyName,Object value);	
	
	public List<ProgPagosDetalle> findByPropertyPojoCompleto(String propertyName,final Object value);
	
	public List<ProgPagosDetalle> findByPropertyPojoCompletoMontoNoCero(String propertyName,final Object value);
	
	public List<ProgPagosDetalle> findByAgenteEstatusMontoConcepto(String agenteValue, String estatusValue, Long conceptoValue);
	
	public List<ProgPagosDetalle> findAll();
	
	public List<ProgPagosDetalle> findByMontoConceptoEstatus(Double montoValue, Long conceptoValue, String estatusValue);
}
