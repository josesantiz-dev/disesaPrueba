package net.giro.cxc.logica;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.cxc.fe.v33.Comprobante;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface FacturaPagosRem {
	public void showSystemOuts(boolean value);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public long save(FacturaPagos entity) throws Exception;
	
	public void update(FacturaPagos entity)  throws Exception;

	public void delete(FacturaPagos entity) throws Exception;

	public FacturaPagos findById(long id);
	
	public List<FacturaPagos> findAll();

	public List<FacturaPagos> findByProperty(String propertyName, Object value);
	
    public List<FacturaPagos> findLikeProperty(String propertyName, String value);

	public List<FacturaPagos> findByFactura(Long idFactura) throws Exception;
	
	public List<FacturaPagos> findByFactura(Factura idFactura) throws Exception;
	
	public BigDecimal findLiquidado(Long idFactura) throws Exception;
	
	public BigDecimal findLiquidado(Factura idFactura) throws Exception;
	
    public Respuesta enviarTransaccion(Long entityId) throws Exception;
    
    public Respuesta enviarTransaccion(FacturaPagos entity) throws Exception;
    
    public int findParcialidad(long idFactura) throws Exception;
    
    public Comprobante generarComprobante(long idFacturaPago) throws Exception;
    
    public long folioComplementoPago(String folioComplementoPago) throws Exception;
    
    public long folioActualComplementoPago(String folioComplementoPago) throws Exception;
    
    public String folioComplementoPago(long idEmpresa, String folioComplementoPago) throws Exception;

    public FacturaPagos convertir(FacturaPagosExt pojoTarget) throws Exception;
    
    public FacturaPagosExt convertir(FacturaPagos pojoTarget) throws Exception;
    
	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------

	public long save(FacturaPagosExt entityExt) throws Exception;
	
	public void update(FacturaPagosExt entityExt)  throws Exception;
	
	public void delete(FacturaPagosExt entityExt) throws Exception;
	
	public FacturaPagosExt findByIdExt(long id) throws Exception;

	public BigDecimal findLiquidado(FacturaExt idFactura) throws Exception;

	public List<FacturaPagos> findByFactura(FacturaExt idFactura) throws Exception;
	
	public List<FacturaPagosExt> findByPropertyExt(String propertyName, Object value) throws Exception;
	
    public List<FacturaPagosExt> findLikePropertyExt(String propertyName, String value) throws Exception;

    public List<FacturaPagosExt> findLikeFolioFactura(String value) throws Exception;
    
    public List<FacturaPagosExt> findLikeBeneficiario(String value) throws Exception;
    
    public List<FacturaPagosExt> findLikeCuentaBancaria(String value) throws Exception;

    public Respuesta enviarTransaccion(FacturaPagosExt entity) throws Exception;
}
