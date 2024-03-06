package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String folioFactura;
	private Long idObra;
	private String nombreObra;
	private int tipoObra;
	private Date fechaVencimiento;
	private Date fechaEmision;
	private BigDecimal subtotal;
	private double impuestos;
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
	private BigDecimal retensionIva;
	private BigDecimal retensionIsr;
	private BigDecimal total;
	private BigDecimal saldo;
	private Long idFormaPago;
	private Long idFolio;
	private String serie;
	private String folio;
	private String tipoComprobante;
	private Long idCertificado;
	private String nocertificado;
	private byte[] xml;
	private byte[] cadenaOriginal;
	private byte[] sello;
	private double tasaIva;
	private BigDecimal descuento;
	private String motivoDescuento;
	private String tipo; // C: Credito, X: Contado
	private Long idMetodoPago;
	private String cuenta;
	private byte[] cfdi;
	private byte[] timbre;
	private double state;
	private String descripcion;
	private double estadoCancelacion;
	private String mensajeCancelacion;
	private byte[] acuseCancelacion;
	private String observaciones;
	private String uuid;
	private Date fechaTimbrado;
	private String selloSat;
	private String noCertificadoSat;
	private Long idMoneda;
	private String descripcionMoneda;
	private long canceladoPor;
	private Date fechaCancelacion;
	private int estatus;
	private long creadoPor;
	private double retenciones;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int provisionada; // 0 : Sin provision, 1: Provisionada, 2: Provision Cancelada
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
		this.nocertificado = "";
		this.motivoDescuento = "";
		this.tipo = "";
		this.descripcion = "";
		this.mensajeCancelacion = "";
		this.observaciones = "";
		this.uuid = "";
		this.selloSat = "";
		this.noCertificadoSat = "";
		this.descripcionMoneda = "";
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

	public Factura(Long id) {
		this();
		this.id = id;
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

	public void setSubtotal(BigDecimal subtotal){
		this.subtotal = subtotal;
	}
	
	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void BigDecimal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public double getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(double impuestos) {
		this.impuestos = impuestos;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public int getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(int tipoObra) {
		this.tipoObra = tipoObra;
	}

	public double getRetenciones() {
		return retenciones;
	}

	public void setRetenciones(double retenciones) {
		this.retenciones = retenciones;
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

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
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

	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public long getCanceladoPor() {
		return canceladoPor;
	}

	public void setCanceladoPor(long canceladoPor) {
		this.canceladoPor = canceladoPor;
	}

	public Long getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(Long idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
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

	public String getNocertificado() {
		return nocertificado;
	}

	public void setNocertificado(String nocertificado) {
		this.nocertificado = nocertificado;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public Long getIdFolio() {
		return idFolio;
	}

	public void setIdFolio(Long idFolio) {
		this.idFolio = idFolio;
	}

	public Long getIdCertificado() {
		return idCertificado;
	}

	public void setIdCertificado(Long idCertificado) {
		this.idCertificado = idCertificado;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
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

	public double getTasaIva() {
		return tasaIva;
	}

	public void setTasaIva(double tasaIva) {
		this.tasaIva = tasaIva;
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

	/**
	 * C: Credito, X: Contado
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * C: Credito, X: Contado
	 * @param tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Long getIdMetodoPago() {
		return idMetodoPago;
	}

	public void setIdMetodoPago(Long idMetodoPago) {
		this.idMetodoPago = idMetodoPago;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getFechaTimbrado() {
		return fechaTimbrado;
	}

	public void setFechaTimbrado(Date fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * 0 : Sin provision, 1: Provisionada, 2: Provision Cancelada
	 * @return
	 */
	public int getProvisionada() {
		return provisionada;
	}
	
	/**
	 * 0 : Sin provision, 1: Provisionada, 2: Provision Cancelada
	 * @param provisionada
	 */
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

	public Long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getDescripcionMoneda() {
		return descripcionMoneda;
	}

	public void setDescripcionMoneda(String descMoneda) {
		this.descripcionMoneda = descMoneda;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */