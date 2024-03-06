package net.giro.cxc.logica;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.respuesta.Respuesta;

@Remote
public interface FacturaPagosRem {
	public void showSystemOuts(boolean value);
	
	public long save(FacturaPagos entity) throws ExcepConstraint;
	
	public long save(FacturaPagosExt entityExt) throws ExcepConstraint;
	
	public void update(FacturaPagos entity)  throws ExcepConstraint;
	
	public void update(FacturaPagosExt entityExt)  throws ExcepConstraint;
	
	public void delete(FacturaPagos entity) throws ExcepConstraint;
	
	public void delete(FacturaPagosExt entityExt) throws ExcepConstraint;
	
	public FacturaPagos findById(long id);
	
	public FacturaPagosExt findByIdExt(long id) throws Exception;
	
	public List<FacturaPagos> findAll();

	public List<FacturaPagos> findByProperty(String propertyName, Object value);
	
	public List<FacturaPagosExt> findByPropertyExt(String propertyName, Object value) throws Exception;
	
    public List<FacturaPagos> findLikeProperty(String propertyName, String value);
    
    public List<FacturaPagosExt> findLikePropertyExt(String propertyName, String value) throws Exception;
    
    public BigDecimal findSaldoByFactura(Long facturaId) throws Exception;
    
    public BigDecimal findSaldoByFactura(Factura factura) throws Exception;
    
    public BigDecimal findSaldoByFactura(FacturaExt factura) throws Exception;

    public List<FacturaPagosExt> findLikeFolioFactura(String value) throws Exception;
    
    public List<FacturaPagosExt> findLikeBeneficiario(String value) throws Exception;
    
    public List<FacturaPagosExt> findLikeCuentaBancaria(String value) throws Exception;
    
    public Respuesta enviarTransaccion(Long entityId, Long idEmpresa) throws Exception;
    
    public Respuesta enviarTransaccion(FacturaPagos entity, Long idEmpresa) throws Exception;
    
    public Respuesta enviarTransaccion(FacturaPagosExt entity, Long idEmpresa) throws Exception;
}
