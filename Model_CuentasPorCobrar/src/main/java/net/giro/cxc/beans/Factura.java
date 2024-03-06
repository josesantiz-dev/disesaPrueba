package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.cxc.util.FACTURA_ESTATUS;

/**
 * factura
 * @author javaz
 *
 */
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String folioFactura;
	private Date fechaEmision;
	private Date fechaVencimiento;
	private String tipo; // TIPO: C-Credito, X-Contado
	private String tipoComprobante;
	private long idObra;
	private String nombreObra;
	private int tipoObra;
	private Long idSucursal;
	private String nombreSucursal;
	private Long idEmpresa;
	private Long idCliente;
	private String cliente;
	private String tipoCliente;
	private String rfc;
	private String domicilio;
	private String noExterno;
	private String noInterno;
	private int cp;
	private String ciudad;
	private String colonia;
	private String municipio;
	private String estado;
	private String pais;
	private Long idMoneda;
	private String descripcionMoneda;
	private double tipoCambio;
	private double tasaIva;
	private BigDecimal retensionIva;
	private BigDecimal retensionIsr;
	private BigDecimal subtotal;
	private double impuestos;
	private double retenciones;
	private BigDecimal total;
	private BigDecimal saldo;
	private Long idFormaPago;
	private String formaPago;
	private Long idMetodoPago;
	private String metodoPago;
	private Long idFolio;
	private String serie;
	private String folio;
	private BigDecimal descuento;
	private String motivoDescuento;
	private String condicionesPago;
	private String cuenta;
	private String observaciones;
	private int estatus; // ESTATUS: 0-Cancelada, 1:Activo, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private long canceladoPor;
	private Date fechaCancelacion;
	// -----------------------------------------------------------------------------------------
	private int timbrado;
	private long timbradoPor;
	private Date fechaTimbrado;
	private long idTimbre;
	private String uuid;
	// -----------------------------------------------------------------------------------------
	private String version;
	private String usoCfdi;
	private byte[] cfdi;
	private byte[] timbre;
	private String selloSat;
	private String noCertificadoSat;
	private double state;
	private String descripcion;
	private double estadoCancelacion;
	private String mensajeCancelacion;
	private byte[] acuseCancelacion;
	private int cfdiRelacionado;
	private String cfdiRelacionadoUuid;
	private String cfdiTipoRelacion;
	private long idFacturaRelacionada;
	private Long idCertificado;
	private String noCertificado;
	private byte[] xml;
	private byte[] cadenaOriginal;
	private byte[] sello;
	// -----------------------------------------------------------------------------------------
	private int provisionada; // provisionada: 0-Sin provision, 1-Provisionada, 2-Provision Cancelada
	private long provisionadaPor;
	private Date fechaProvisionada;
	
	public Factura() {
		this.folioFactura = "";
		this.nombreObra = "";
		this.nombreSucursal = "";
		this.cliente = "";
		this.tipoCliente = "";
		this.rfc = "";
		this.domicilio = "";
		this.noExterno = "";
		this.noInterno = "";
		this.colonia = "";
		this.ciudad = "";
		this.municipio = "";
		this.estado = "";
		this.pais = "";
		this.serie = "";
		this.folio = "";
		this.tipoComprobante = "";
		this.noCertificado = "";
		this.motivoDescuento = "";
		this.tipo = "";
		this.condicionesPago = "";
		this.descripcion = "";
		this.mensajeCancelacion = "";
		this.observaciones = "";
		this.uuid = "";
		this.selloSat = "";
		this.noCertificadoSat = "";
		this.descripcionMoneda = "";
		this.version = "";
		this.usoCfdi = "";
		this.cfdiRelacionadoUuid = "";
		this.cfdiTipoRelacion = "";
		this.formaPago = "";
		this.metodoPago = "";
		this.subtotal = BigDecimal.ZERO;
		this.retensionIva = BigDecimal.ZERO;
		this.retensionIsr = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.saldo = BigDecimal.ZERO;
		this.descuento = BigDecimal.ZERO;
		this.fechaEmision = Calendar.getInstance().getTime();
		this.fechaVencimiento = Calendar.getInstance().getTime();
		this.fechaTimbrado = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.fechaCancelacion = Calendar.getInstance().getTime();
		this.fechaProvisionada = Calendar.getInstance().getTime();
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

	public String getFolioFactura() {
		return folioFactura;
	}

	public void setFolioFactura(String folioFactura) {
		this.folioFactura = folioFactura;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	/**
	 * TIPO: X - Contado, C - Credito
	 * @param tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * TIPO: X - Contado, C - Credito
	 * @param tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public long getIdObra() {
		return idObra;
	}

	public void setIdObra(long idObra) {
		this.idObra = idObra;
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public int getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(int tipoObra) {
		this.tipoObra = tipoObra;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getNoExterno() {
		return noExterno;
	}

	public void setNoExterno(String noExterno) {
		this.noExterno = noExterno;
	}

	public String getNoInterno() {
		return noInterno;
	}

	public void setNoInterno(String noInterno) {
		this.noInterno = noInterno;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public Long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getDescripcionMoneda() {
		return descripcionMoneda;
	}

	public void setDescripcionMoneda(String descripcionMoneda) {
		this.descripcionMoneda = descripcionMoneda;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public double getTasaIva() {
		return tasaIva;
	}

	public void setTasaIva(double tasaIva) {
		this.tasaIva = tasaIva;
	}

	public BigDecimal getRetensionIva() {
		return retensionIva;
	}

	public void setRetensionIva(BigDecimal retensionIva) {
		this.retensionIva = retensionIva;
	}

	public BigDecimal getRetensionIsr() {
		return retensionIsr;
	}

	public void setRetensionIsr(BigDecimal retensionIsr) {
		this.retensionIsr = retensionIsr;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public double getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(double impuestos) {
		this.impuestos = impuestos;
	}

	public double getRetenciones() {
		return retenciones;
	}

	public void setRetenciones(double retenciones) {
		this.retenciones = retenciones;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public Long getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(Long idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public Long getIdMetodoPago() {
		return idMetodoPago;
	}

	public void setIdMetodoPago(Long idMetodoPago) {
		this.idMetodoPago = idMetodoPago;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public Long getIdFolio() {
		return idFolio;
	}

	public void setIdFolio(Long idFolio) {
		this.idFolio = idFolio;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public BigDecimal getDescuento() {
		return descuento;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	public String getMotivoDescuento() {
		return motivoDescuento;
	}

	public void setMotivoDescuento(String motivoDescuento) {
		this.motivoDescuento = motivoDescuento;
	}

	public String getCondicionesPago() {
		return condicionesPago;
	}

	public void setCondicionesPago(String condicionesPago) {
		this.condicionesPago = condicionesPago;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * // ESTATUS:  0-Cancelada, 1:Activo, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * // ESTATUS: 0-Cancelada, 1:Activo, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	/**
	 * // ESTATUS: 0-Cancelada, 1:Activo, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	 * @param estatus
	 */
	public void setEstatus(FACTURA_ESTATUS estatus) {
		if (estatus != null)
			this.estatus = estatus.ordinal();
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

	public long getCanceladoPor() {
		return canceladoPor;
	}

	public void setCanceladoPor(long canceladoPor) {
		this.canceladoPor = canceladoPor;
	}

	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public int getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(int timbrado) {
		this.timbrado = timbrado;
	}

	public long getTimbradoPor() {
		return timbradoPor;
	}

	public void setTimbradoPor(long timbradoPor) {
		this.timbradoPor = timbradoPor;
	}

	public Date getFechaTimbrado() {
		return fechaTimbrado;
	}

	public void setFechaTimbrado(Date fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}

	public long getIdTimbre() {
		return idTimbre;
	}

	public void setIdTimbre(long idTimbre) {
		this.idTimbre = idTimbre;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUsoCfdi() {
		return usoCfdi;
	}

	public void setUsoCfdi(String usoCfdi) {
		this.usoCfdi = usoCfdi;
	}

	public byte[] getCfdi() {
		return cfdi;
	}

	public void setCfdi(byte[] cfdi) {
		this.cfdi = cfdi;
	}

	public byte[] getTimbre() {
		return timbre;
	}

	public void setTimbre(byte[] timbre) {
		this.timbre = timbre;
	}

	public String getSelloSat() {
		return selloSat;
	}

	public void setSelloSat(String selloSat) {
		this.selloSat = selloSat;
	}

	public String getNoCertificadoSat() {
		return noCertificadoSat;
	}

	public void setNoCertificadoSat(String noCertificadoSat) {
		this.noCertificadoSat = noCertificadoSat;
	}

	public double getState() {
		return state;
	}

	public void setState(double state) {
		this.state = state;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getEstadoCancelacion() {
		return estadoCancelacion;
	}

	public void setEstadoCancelacion(double estadoCancelacion) {
		this.estadoCancelacion = estadoCancelacion;
	}

	public String getMensajeCancelacion() {
		return mensajeCancelacion;
	}

	public void setMensajeCancelacion(String mensajeCancelacion) {
		this.mensajeCancelacion = mensajeCancelacion;
	}

	public byte[] getAcuseCancelacion() {
		return acuseCancelacion;
	}

	public void setAcuseCancelacion(byte[] acuseCancelacion) {
		this.acuseCancelacion = acuseCancelacion;
	}

	public int getCfdiRelacionado() {
		return cfdiRelacionado;
	}

	public void setCfdiRelacionado(int cfdiRelacionado) {
		this.cfdiRelacionado = cfdiRelacionado;
	}

	public String getCfdiRelacionadoUuid() {
		return cfdiRelacionadoUuid;
	}

	public void setCfdiRelacionadoUuid(String cfdiRelacionadoUuid) {
		this.cfdiRelacionadoUuid = cfdiRelacionadoUuid;
	}

	public String getCfdiTipoRelacion() {
		return cfdiTipoRelacion;
	}

	public void setCfdiTipoRelacion(String cfdiTipoRelacion) {
		this.cfdiTipoRelacion = cfdiTipoRelacion;
	}

	public long getIdFacturaRelacionada() {
		return idFacturaRelacionada;
	}

	public void setIdFacturaRelacionada(long idFacturaRelacionada) {
		this.idFacturaRelacionada = idFacturaRelacionada;
	}

	public Long getIdCertificado() {
		return idCertificado;
	}

	public void setIdCertificado(Long idCertificado) {
		this.idCertificado = idCertificado;
	}

	public String getNoCertificado() {
		return noCertificado;
	}

	public void setNoCertificado(String nocertificado) {
		this.noCertificado = nocertificado;
	}

	public byte[] getXml() {
		return xml;
	}

	public void setXml(byte[] xml) {
		this.xml = xml;
	}

	public byte[] getCadenaOriginal() {
		return cadenaOriginal;
	}

	public void setCadenaOriginal(byte[] cadenaOriginal) {
		this.cadenaOriginal = cadenaOriginal;
	}

	public byte[] getSello() {
		return sello;
	}

	public void setSello(byte[] sello) {
		this.sello = sello;
	}

	public int getProvisionada() {
		return provisionada;
	}

	public void setProvisionada(int provisionada) {
		this.provisionada = provisionada;
	}

	public long getProvisionadaPor() {
		return provisionadaPor;
	}

	public void setProvisionadaPor(long provisionadaPor) {
		this.provisionadaPor = provisionadaPor;
	}

	public Date getFechaProvisionada() {
		return fechaProvisionada;
	}

	public void setFechaProvisionada(Date fechaProvisionada) {
		this.fechaProvisionada = fechaProvisionada;
	}
	
	// -------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------
	
	public double getSaldoFactura() {
		if (this.saldo != null && this.saldo.doubleValue() > 0)
			return this.saldo.doubleValue();
		return 0;
	}

	public void setSaldoFactura(double saldo) {}
	
	public String getEstatusDescripcion() {
		if (this.estatus == FACTURA_ESTATUS.Cancelada.ordinal())
			return FACTURA_ESTATUS.Cancelada.toString().toUpperCase();
		if (this.estatus == FACTURA_ESTATUS.PendienteCancelacion.ordinal())
			return "PENDIENTE CANCELACION";
		if (this.estatus == FACTURA_ESTATUS.PendienteAprobacion.ordinal())
			return "PENDIENTE APROBACION";
		if ("E".equals(this.tipoComprobante))
			return FACTURA_ESTATUS.Activa.toString().toUpperCase();
		if (this.saldo.doubleValue() >= this.total.doubleValue())
			return  FACTURA_ESTATUS.Activa.toString().toUpperCase();
		if (this.saldo.doubleValue() > 0 && this.saldo.doubleValue() < this.total.doubleValue())
			return "ABONADA";
		if (this.saldo.doubleValue() <= 0) 
			return "PUE".equals(this.metodoPago) && this.timbrado == 0 ? FACTURA_ESTATUS.Activa.toString().toUpperCase() : "PAGADA";
		return FACTURA_ESTATUS.Activa.toString().toUpperCase();
	}
	
	public void setEstatusDescripcion(String value) {}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */