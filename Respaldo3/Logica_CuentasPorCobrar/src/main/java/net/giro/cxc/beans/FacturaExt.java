package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.adp.beans.ObraExt;
import net.giro.cxc.util.FACTURA_ESTATUS;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Sucursal;
import net.giro.plataforma.beans.ConValores;

public class FacturaExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String folioFactura;
	private Date fechaEmision;
	private Date fechaVencimiento;
	private String tipo; // C: Credito, X: Contado
	private String tipoComprobante;
	private ObraExt idObra;
	private Sucursal idSucursal;
	private String nombreSucursal;
	private Long idCliente;
	private String cliente;
	private String tipoCliente;
	private String rfc;
	private String domicilio;
	private String noExterno;
	private String noInterno;
	private String ciudad;
	private String municipio;
	private String estado;
	private String pais;
	private String colonia;
	private int cp;
	private Empresa idEmpresa;
	private Long idMoneda;
	private String descripcionMoneda;
	private String abreviaturaMoneda;
	private double tipoCambio;
	private double tasaIva;
	private BigDecimal subtotal;
	private double impuestos;
	private double retenciones;
	private BigDecimal retensionIva;
	private BigDecimal retensionIsr;
	private BigDecimal total;
	private BigDecimal saldo;
	private String serie;
	private String folio;
	private Long idFolio;
	private BigDecimal descuento;
	private String motivoDescuento;
	private String condicionesPago;
	private Long idFormaPago;
	private String formaPago;
	private ConValores idMetodoPago;
	private String metodoPago;
	private String cuenta;
	private boolean actualizarObra;
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
	private FacturaTimbre idTimbre;
	private String uuid;
	private Factura idFacturaRelacionada;
	private Long idCertificado;
	// -----------------------------------------------------------------------------------------
	// Borrables
	/*private String version;
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
	private String noCertificado;
	private byte[] xml;
	private byte[] cadenaOriginal;
	private byte[] sello;*/
	// -----------------------------------------------------------------------------------------
	private int provisionada; // 0 : Sin provision, 1: Provisionada, 2: Provision Cancelada
	private long provisionadaPor;
	private Date fechaProvisionada;
	
	public FacturaExt() {
		this.folioFactura = "";
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
		this.motivoDescuento = "";
		this.tipo = "";
		this.observaciones = "";
		this.uuid = "";
		this.descripcionMoneda = "";
		this.formaPago = "";
		this.metodoPago = "";
		/*this.noCertificado = "";
		this.descripcion = "";
		this.mensajeCancelacion = "";
		this.selloSat = "";
		this.noCertificadoSat = "";
		this.version = "";
		this.usoCfdi = "";
		this.cfdiRelacionadoUuid = "";
		this.cfdiTipoRelacion = "";*/
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

	public ObraExt getIdObra() {
		return idObra;
	}

	public void setIdObra(ObraExt idObra) {
		this.idObra = idObra;
	}

	public Sucursal getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Sucursal idSucursal) {
		this.idSucursal = idSucursal;
		if (idSucursal != null)
			this.nombreSucursal = idSucursal.getSucursal();
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
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

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
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

	public Empresa getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Empresa idEmpresa) {
		this.idEmpresa = idEmpresa;
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

	public String getAbreviaturaMoneda() {
		return abreviaturaMoneda;
	}

	public void setAbreviaturaMoneda(String abreviaturaMoneda) {
		this.abreviaturaMoneda = abreviaturaMoneda;
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

	public Long getIdFolio() {
		return idFolio;
	}

	public void setIdFolio(Long idFolio) {
		this.idFolio = idFolio;
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

	public ConValores getIdMetodoPago() {
		return idMetodoPago;
	}

	public void setIdMetodoPago(ConValores idMetodoPago) {
		this.idMetodoPago = idMetodoPago;
		if (idMetodoPago != null)
			this.metodoPago = idMetodoPago.getValor();
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
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
	 * ESTATUS: 0-Cancelada, 1:Activo, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 0-Cancelada, 1:Activo, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
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

	public FacturaTimbre getIdTimbre() {
		return idTimbre;
	}

	public void setIdTimbre(FacturaTimbre idTimbre) {
		this.idTimbre = idTimbre;
		populateTimbre();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public byte[] getCfdi() {
		if (this.idTimbre != null)
			return this.idTimbre.getCfdi();
		return null;
	}
	
	public void setCfdi(byte[] cfdi) {}

	public Factura getIdFacturaRelacionada() {
		return this.idFacturaRelacionada;
	}

	public void setIdFacturaRelacionada(Factura idFacturaRelacionada) {
		this.idFacturaRelacionada = idFacturaRelacionada;
	}

	public Long getIdCertificado() {
		return this.idCertificado;
	}

	public void setIdCertificado(Long idCertificado) {
		if (idCertificado == null)
			idCertificado = 0L;
		this.idCertificado = idCertificado;
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

	public String getVersion() {
		if (this.idTimbre != null)
			return this.idTimbre.getVersion();
		return "";
	}

	public void setVersion(String version) {}

	public String getUsoCfdi() {
		if (this.idTimbre != null)
			return this.idTimbre.getUsoCfdi();
		return "";
	}

	public void setUsoCfdi(String usoCfdi) {}

	public byte[] getTimbre() {
		if (this.idTimbre != null)
			return this.idTimbre.getTimbre();
		return null;
	}

	public void setTimbre(byte[] timbre) {}

	public String getSelloSat() {
		if (this.idTimbre != null)
			return this.idTimbre.getSelloSat();
		return "";
	}

	public void setSelloSat(String selloSat) {}

	public String getNoCertificadoSat() {
		if (this.idTimbre != null)
			return this.idTimbre.getNoCertificadoSat();
		return "";
	}

	public void setNoCertificadoSat(String noCertificadoSat) {}

	public double getState() {
		if (this.idTimbre != null)
			return this.idTimbre.getState();
		return 0;
	}

	public void setState(double state) {}

	public String getDescripcion() {
		if (this.idTimbre != null)
			return this.idTimbre.getDescripcion();
		return "";
	}

	public void setDescripcion(String descripcion) {}

	public int getCfdiRelacionado() {
		if (this.idTimbre != null)
			return this.idTimbre.getCfdiRelacionado();
		return 0;
	}

	public void setCfdiRelacionado(int cfdiRelacionado) {}

	public String getCfdiRelacionadoUuid() {
		if (this.idTimbre != null)
			return this.idTimbre.getCfdiRelacionadoUuid();
		return "";
	}

	public void setCfdiRelacionadoUuid(String cfdiRelacionadoUuid) {}

	public String getCfdiTipoRelacion() {
		if (this.idTimbre != null)
			return this.idTimbre.getCfdiTipoRelacion();
		return "";
	}

	public void setCfdiTipoRelacion(String cfdiTipoRelacion) {}

	public String getNoCertificado() {
		if (this.idTimbre != null)
			return this.idTimbre.getNoCertificado();
		return "";
	}

	public void setNoCertificado(String nocertificado) {}

	public byte[] getXml() {
		if (this.idTimbre != null)
			return this.idTimbre.getXml();
		return null;
	}

	public void setXml(byte[] xml) {}

	public byte[] getCadenaOriginal() {
		if (this.idTimbre != null)
			return this.idTimbre.getCfdi();
		return null;
	}

	public void setCadenaOriginal(byte[] cadenaOriginal) {}

	public byte[] getSello() {
		if (this.idTimbre != null)
			return this.idTimbre.getCfdi();
		return null;
	}

	public void setSello(byte[] sello) {}

	public double getEstadoCancelacion() {
		if (this.idTimbre != null)
			return this.idTimbre.getEstadoCancelacion();
		return 0;
	}

	public void setEstadoCancelacion(double estadoCancelacion) {}

	public String getMensajeCancelacion() {
		if (this.idTimbre != null)
			return this.idTimbre.getMensajeCancelacion();
		return "";
	}

	public void setMensajeCancelacion(String mensajeCancelacion) {}

	public byte[] getAcuseCancelacion() {
		if (this.idTimbre != null)
			return this.idTimbre.getAcuseCancelacion();
		return null;
	}

	public void setAcuseCancelacion(byte[] acuseCancelacion) {}

	// -----------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------
	
	public Long getIdBeneficiario() {
		if (this.idObra != null && this.idObra.getIdCliente() != null && this.idObra.getIdCliente().getId() > 0L)
			return this.idObra.getIdCliente().getId();
		return 0L;
	}

	public void setIdBeneficiario(Long idBeneficiario) {}

	public String getBeneficiario() {
		if (this.idObra != null && this.idObra.getIdCliente() != null && this.idObra.getIdCliente().getNombre() != null)
			return this.idObra.getIdCliente().getNombre();
		return "";
	}

	public void setBeneficiario(String beneficiario) {}

	public String getTipoBeneficiario() {
		if (this.idObra != null && this.idObra.getTipoCliente() != null)
			return this.idObra.getTipoCliente();
		return "";
	}

	public void setTipoBeneficiario(String tipoBeneficiario)  {}

	public boolean getActualizarObra() {
		return actualizarObra;
	}

	public void setActualizarObra(boolean value) {
		this.actualizarObra = value;
	}
	
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
		/*if(this.estatus == 0)
			return "CANCELADA";
		if (this.saldo.doubleValue() >= this.total.doubleValue())
			return "ACTIVA";
		if (this.saldo.doubleValue() > 0 && this.saldo.doubleValue() < this.total.doubleValue())
			return "ABONADA";
		if (this.saldo.doubleValue() <= 0) 
			return "PUE".equals(this.metodoPago) && this.timbrado == 0 ? "ACTIVA" : "PAGADA";
		return "ACTIVA";*/
	}
	
	public void setEstatusDescripcion(String value) {}
	
	private void populateTimbre() {
		if (this.idTimbre == null)
			return;
		this.setSerie(this.idTimbre.getSerie());
		this.setFolio(this.idTimbre.getFolio());
		this.setTimbrado(this.idTimbre.getTimbrado());
		this.setTimbradoPor(this.idTimbre.getTimbradoPor());
		if (this.timbrado == 1) {
			this.setUuid(this.idTimbre.getUuid());
			this.setFechaTimbrado(this.idTimbre.getFechaTimbrado());
		}
	}

	// -----------------------------------------------------------------
	// COPIA
	// -----------------------------------------------------------------

	public FacturaExt getCopia() {
		return this.copia();
	}
	
	public FacturaExt copia() {
		try {
			FacturaExt dest = new FacturaExt();
			dest.setId(this.id);
			dest.setFolioFactura(this.folioFactura);
			dest.setFechaEmision(this.fechaEmision);
			dest.setFechaVencimiento(this.fechaVencimiento);
			dest.setIdObra(this.idObra);
			dest.setSubtotal(this.subtotal);
			dest.setImpuestos(this.impuestos);
			dest.setRetenciones(this.retenciones);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setEstatus(this.estatus);
			dest.setIdSucursal(this.idSucursal);
			dest.setNombreSucursal(this.nombreSucursal);
			dest.setIdEmpresa(this.idEmpresa);
			// Cliente
			dest.setIdCliente(this.idCliente);
			dest.setCliente(this.cliente);
			dest.setTipoCliente(this.tipoCliente);
			dest.setRfc(this.rfc);
			dest.setDomicilio(this.domicilio);
			dest.setNoExterno(this.noExterno);
			dest.setNoInterno(this.noInterno);
			dest.setColonia(this.colonia);
			dest.setCp(this.cp);
			dest.setCiudad(this.ciudad);
			dest.setMunicipio(this.municipio);
			dest.setEstado(this.estado);
			dest.setPais(this.pais);
			// ---------------------------------------
			dest.setRetensionIva(this.retensionIva);
			dest.setRetensionIsr(this.retensionIsr);
			dest.setTotal(this.total);
			dest.setSaldo(this.saldo);
			dest.setFechaCancelacion(this.fechaCancelacion);
			dest.setCanceladoPor(this.canceladoPor);
			dest.setIdFormaPago(this.idFormaPago);
			dest.setFormaPago(this.formaPago);
			dest.setSerie(this.serie);
			dest.setTipoComprobante(this.tipoComprobante);
			dest.setFolio(this.folio);
			dest.setIdFolio(this.idFolio);
			dest.setTasaIva(this.tasaIva);
			dest.setDescuento(this.descuento);
			dest.setMotivoDescuento(this.motivoDescuento);
			dest.setTipo(this.tipo);
			dest.setCondicionesPago(this.condicionesPago);
			dest.setIdMetodoPago(this.idMetodoPago);
			dest.setMetodoPago(this.metodoPago);
			dest.setCuenta(this.cuenta);
			dest.setTimbrado(this.timbrado);
			dest.setTimbradoPor(this.timbradoPor);
			dest.setFechaTimbrado(this.fechaTimbrado);
			dest.setIdTimbre(this.idTimbre);
			dest.setUuid(this.uuid);
			dest.setObservaciones(this.observaciones);
			dest.setIdMoneda(this.idMoneda);
			dest.setDescripcionMoneda(this.descripcionMoneda);
			dest.setAbreviaturaMoneda(this.abreviaturaMoneda);
			dest.setProvisionada(this.provisionada);
			dest.setProvisionadaPor(this.provisionadaPor);
			dest.setFechaProvisionada(this.fechaProvisionada);
			dest.setTipoCambio(this.tipoCambio);
			
			return dest;
		} catch (Exception e) {
			throw e;
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */