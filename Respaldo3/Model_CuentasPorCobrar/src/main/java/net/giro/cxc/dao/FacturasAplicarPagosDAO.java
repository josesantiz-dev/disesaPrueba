package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.FacturasAplicarPagos;

@Remote
public interface FacturasAplicarPagosDAO extends DAO<FacturasAplicarPagos> {
	public long save(FacturasAplicarPagos entity, long codigoEmpresa) throws Exception;
	
	public List<FacturasAplicarPagos> saveOrUpdateList(List<FacturasAplicarPagos> entities, long codigoEmpresa) throws Exception;
	
	public List<FacturasAplicarPagos> pagosAplicables(long idFactura) throws Exception;
	
	public FacturasAplicarPagos comprobarPagoAplicable(long idFacturaOriginal, long idFactura) throws Exception;
}
