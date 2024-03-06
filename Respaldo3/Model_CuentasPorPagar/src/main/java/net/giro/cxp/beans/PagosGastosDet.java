package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.cxp.beans.PagosGastos;

/**
 * pagos_gastos_det
 * @author javaz
 */
public class PagosGastosDet implements Serializable {
	private static final long serialVersionUID = -8788796846813172225L;
	private Long id;
	private PagosGastos idPagosGastos;
	private Date fecha;
	private Long idConcepto;
	private String descripcionConcepto; // sin uso:: descripion de concepto
	private Long idObra; 
	private String nombreObra;  
	private Long idOrdenCompra;
	private String folioOrdenCompra;  
	private int esCredito;
	private int manoObra;
	private int subcontrato;
	// ---------------------------------------------------------------------
	private int facturado;
	private Long idCfdi;
	private int estatusCfdi; // 0 - Sin Comprobar, 1 - Activo, 2 - Cancelado
	private String referencia; // folio de documento
	private Long idProveedor;
	private String tipoPersonaProveedor; // P:Persona, N:Negocio
	private String nombreProveedor; 
	private String rfcProveedor; 
	private String facturaConcepto; // * descripcion de concepto en CFDI
	private Double importeConcepto; // * importe de concepto en CFDI
	// ---------------------------------------------------------------------
	private Double subtotal;
	private Double totalImpuestos;
	private Double totalRetenciones;
	private Double total;  // *
	private String observaciones;
	private String estatus; 
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	// ---------------------------------------------------------------------
	private String uniqueValue;
	
	public PagosGastosDet() {
		this.fecha = Calendar.getInstance().getTime();
		this.importeConcepto = 0D;
		this.subtotal = 0D;
		this.totalImpuestos = 0D;
		this.totalRetenciones = 0D;
		this.total = 0D;
		this.descripcionConcepto = "";
		this.nombreObra = "";
		this.folioOrdenCompra = "";
		this.referencia = "";
		this.tipoPersonaProveedor = "";
		this.nombreProveedor = "";
		this.rfcProveedor = "";
		this.facturaConcepto = "";
		this.observaciones = "";
		this.estatus = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		// ---------------------------------------------------------------------
		//generarUniqueValue();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public PagosGastos getIdPagosGastos() {
		return idPagosGastos;
	}
	
	public void setIdPagosGastos(PagosGastos idPagosGastos) {
		this.idPagosGastos = idPagosGastos;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Long getIdConcepto() {
		return idConcepto;
	}
	
	public void setIdConcepto(Long idConcepto) {
		this.idConcepto = idConcepto;
	}
	
	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}
	
	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}
	
	public Long getIdObra() {
		return idObra;
	}
	
	public void setIdObra(Long idObra) {
		this.idObra = idObra;
	}
	
	public String getNombreObra() {
		return nombreObra;
	}
	
	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}
	
	public Long getIdOrdenCompra() {
		return idOrdenCompra;
	}
	
	public void setIdOrdenCompra(Long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}
	
	public String getFolioOrdenCompra() {
		return folioOrdenCompra;
	}
	
	public void setFolioOrdenCompra(String folioOrdenCompra) {
		this.folioOrdenCompra = folioOrdenCompra;
	}
	
	public int getEsCredito() {
		return esCredito;
	}
	
	public void setEsCredito(int esCredito) {
		this.esCredito = esCredito;
	}
	
	public int getManoObra() {
		return manoObra;
	}
	
	public void setManoObra(int manoObra) {
		this.manoObra = manoObra;
	}

	public int getSubcontrato() {
		return subcontrato;
	}

	public void setSubcontrato(int subcontrato) {
		this.subcontrato = subcontrato;
	}
	
	public int getFacturado() {
		return facturado;
	}
	
	public void setFacturado(int facturado) {
		this.facturado = facturado;
	}
	
	public Long getIdCfdi() {
		return idCfdi;
	}
	
	public void setIdCfdi(Long idCfdi) {
		this.idCfdi = idCfdi;
	}
	
	/**
	 * Estatus CFDI: 0 - Sin Comprobar, 1 - Activo, 2 - Cancelado
	 * @return
	 */
	public int getEstatusCfdi() {
		return estatusCfdi;
	}
	
	/**
	 * Estatus CFDI: 0 - Sin Comprobar, 1 - Activo, 2 - Cancelado
	 * @param estatusCfdi
	 */
	public void setEstatusCfdi(int estatusCfdi) {
		this.estatusCfdi = estatusCfdi;
	}
	
	public String getReferencia() {
		return referencia;
	}
	
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	public Long getIdProveedor() {
		return idProveedor;
	}
	
	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}
	
	/**
	 * P:Persona, N:Negocio
	 * @return
	 */
	public String getTipoPersonaProveedor() {
		return tipoPersonaProveedor;
	}
	
	/**
	 * P:Persona, N:Negocio
	 * @param tipoPersonaProveedor
	 */
	public void setTipoPersonaProveedor(String tipoPersonaProveedor) {
		this.tipoPersonaProveedor = tipoPersonaProveedor;
	}
	
	public String getNombreProveedor() {
		return nombreProveedor;
	}
	
	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}
	
	public String getRfcProveedor() {
		return rfcProveedor;
	}
	
	public void setRfcProveedor(String rfcProveedor) {
		this.rfcProveedor = rfcProveedor;
	}
	
	public String getFacturaConcepto() {
		return facturaConcepto;
	}

	public void setFacturaConcepto(String facturaConcepto) {
		this.facturaConcepto = facturaConcepto;
	}

	public Double getImporteConcepto() {
		return importeConcepto;
	}

	public void setImporteConcepto(Double importeConcepto) {
		this.importeConcepto = importeConcepto;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTotalImpuestos() {
		return totalImpuestos;
	}

	public void setTotalImpuestos(Double totalImpuestos) {
		this.totalImpuestos = totalImpuestos;
	}

	public Double getTotalRetenciones() {
		return totalRetenciones;
	}

	public void setTotalRetenciones(Double totalRetenciones) {
		this.totalRetenciones = totalRetenciones;
	}

	public Double getTotal() {
		if (this.subtotal == null)
			this.subtotal = 0D;
		if (this.totalImpuestos == null)
			this.totalImpuestos = 0D;
		if (this.totalRetenciones == null)
			this.totalRetenciones = 0D;
		this.total = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
		
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUniqueValue() {
		return uniqueValue;
	}

	public void setUniqueValue(String uniqueValue) {
		this.uniqueValue = uniqueValue;
	}

	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------
	
	public void generarUniqueValue() {
		Calendar fecha = Calendar.getInstance();
		String value = "";

		value += ((fecha.get(Calendar.MONTH) < 10 ? "0" : "") + (fecha.get(Calendar.MONTH) + 1));
		value += ((fecha.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + fecha.get(Calendar.DAY_OF_MONTH));
		value += ((fecha.get(Calendar.HOUR) < 10 ? "0" : "") + fecha.get(Calendar.HOUR));
		value += ((fecha.get(Calendar.MINUTE) < 10 ? "0" : "") + fecha.get(Calendar.MINUTE));
		value += ((fecha.get(Calendar.SECOND) < 10 ? "0" : "") + fecha.get(Calendar.SECOND));
	
		this.uniqueValue = this.genHash(value);
	}
	
	private String genHash(String value) {
		java.security.MessageDigest md = null;
		StringBuffer sb = null;
		byte[] array = null;
		
		try {
            md = java.security.MessageDigest.getInstance("MD5");
            array = md.digest(value.getBytes());
            sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) 
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
		
		return null;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 * 	2.2 | 2017-08-05 | Javier Tirado 	| Añado propiedades idObra y idOrdenCompra
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */