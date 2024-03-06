package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDet;

@Remote
public interface PagosGastosDetDAO extends DAO<PagosGastosDet> {
	//public long save(PagosGastosDet entity);
	
	//public void delete(PagosGastosDet entity);
	
	//public void update(PagosGastosDet entity);

	//public PagosGastosDet findById(Long id);

	public List<PagosGastosDet> findByPropertyALL(String propertyName,final Object value);
	
	public List<PagosGastosDet> findByProperty(String propertyName, Object value);

	public List<PagosGastosDet> findAll();
	
	public List<PagosGastosDet> findLikePojoCompleto(Object value,int max);
	
	public List<PagosGastosDet> findDetGtos2MovtoCtas(Object value,int max);
	
	public Double saldoRepCajaChica(Object value);
	
	public List<PagosGastosDet> findPagoProvisionado(String propertyName, Object value, String sucursales);
	
	public List<PagosGastosDet> findByPagosGastos(PagosGastos entity, int limite) throws Exception;
}
