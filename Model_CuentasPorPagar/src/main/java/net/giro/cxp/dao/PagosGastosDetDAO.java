package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.cxp.beans.PagosGastosDet;

@Remote
public interface PagosGastosDetDAO extends DAO<PagosGastosDet> {
	public List<PagosGastosDet> findAll(long idPagosGastos);
	
	public List<PagosGastosDet> findByProperty(String propertyName, Object value, String orderBy);
	
	public List<PagosGastosDet> findByFactura(long idFactura, boolean incluyeCanceladas) throws Exception;
	
	public List<PagosGastosDet> findPagoProvisionado(String propertyName, Object value, String sucursales);

	public List<PagosGastosDet> findAll();
	
	public List<PagosGastosDet> findByPropertyALL(String propertyName,final Object value);

	public List<PagosGastosDet> findLikePojoCompleto(Object value,int max);
	
	public List<PagosGastosDet> findDetGtos2MovtoCtas(Object value,int max);
	
	public Double saldoRepCajaChica(Object value);
}
