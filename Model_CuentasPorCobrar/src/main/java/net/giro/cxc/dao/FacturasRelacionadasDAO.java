package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturasRelacionadas;

@Remote
public interface FacturasRelacionadasDAO extends DAO<FacturasRelacionadas> {
    static final long serialVersionUID = 1L;
	
	public long save(FacturasRelacionadas entity, long codigoEmpresa) throws Exception;
	
	public List<FacturasRelacionadas> findFacturasById(long idFactura) throws Exception;

	public void deleteFacturasRelacionadas(long idFactura) throws Exception;
}
