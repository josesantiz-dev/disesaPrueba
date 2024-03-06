package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.adp.beans.ObraExt;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Sucursal;
import net.giro.plataforma.beans.ConValores;

public class FacturaExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String folioFactura;
	private Long idCliente;
	private String cliente;
	private String tipoCliente;
	private Date fechaEmision;
	private Date fechaVencimiento;
	private ObraExt idObra;
	private BigDecimal subtotal;
	private double impuestos;
	private double retenciones;
	private Sucursal idSucursal;
	private String nombreSucursal;
	private Empresa idEmpresa;
	private String domicilio;
	private String ciudad;
	private String rfc;
	private BigDecimal retensionIva;
	private BigDecimal retensionIsr;
	private BigDecimal total;
	private BigDecimal saldo;
	private Date fechaCancelacion;
	private long canceladoPor;
	private Long idFormaPago;
	private String serie;
	private String tipoComprobante;
	private String municipio;
	private String estado;
	private String pais;
	private String nocertificado;
	private String folio;
	private Long idFolio;
	private Long idCertificado;
	private String colonia;
	private int cp;
	private String noExterno;
	private String noInterno;
	private byte[] xml;
	private byte[] cadenaOriginal;
	private byte[] sello;
	private double tasaIva;
	private BigDecimal descuento;
	private String motivoDescuento;
	private String tipo; // C: Credito, X: Contado
	private String condicionesPago;
	private ConValores idMetodoPago;
	private String cuenta;
	private byte[] cfdi;
	private byte[] timbre;
	private double state;
	private String descripcion;
	private double estadoCancelacion;
	private String mensajeCancelacion;
	private byte[] acuseCancelacion;
	private String uuid;
	private Date fechaTimbrado;
	private String selloSat;
	private String noCertificadoSat;
	private boolean actualizarObra;
	private String observaciones;
	private Long idMoneda;
	private String descripcionMoneda;
	private String abreviaturaMoneda;
	private double tipoCambio;
	private int provisionada;
	private long provisionadaPor;
	private Date fechaProvisionada;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private String version;
	private String usoCfdi;
	private int cfdiRelacionado;
	private String cfdiRelacionadoUuid;
	private String cfdiTipoRelacion;
	
	
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
		this.version = "";
		this.usoCfdi = "";
		this.cfdiRelacionadoUuid = "";
		this.cfdiTipoRelacion = "";
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

	public FacturaExt(Long id) {
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
	
	public ObraExt getIdObra() {
		return this.idObra;
	}
	
	public void setIdObra(ObraExt idObra) {
		this.idObra = idObra;
	}
	
	public String getNombreObra() {
		if (this.idObra != null && this.idObra.getNombre() != null) 
			return this.idObra.getNombre();
		return "";
	}
	
	public void setNombreObra(String nombreObra) {}
	
	public String getFolioFactura() {
		if (folioFactura == null) folioFactura = "";
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
	
	public double getTotal() {
		return (this.subtotal.doubleValue() + this.impuestos - this.retenciones);
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public Sucursal getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Sucursal idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public Empresa getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Empresa idEmpresa) {
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

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
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
	 * @return
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

	public String getCondicionesPago() {
		return condicionesPago;
	}

	public void setCondicionesPago(String condicionesPago) {
		this.condicionesPago = condicionesPago;
	}

	public ConValores getIdMetodoPago() {
		return idMetodoPago;
	}

	public void setIdMetodoPago(ConValores idMetodoPago) {
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

	public void setTotal(BigDecimal total) {
		this.total = total;
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

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
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
			dest.setSerie(this.serie);
			dest.setTipoComprobante(this.tipoComprobante);
			dest.setNocertificado(this.nocertificado);
			dest.setFolio(this.folio);
			dest.setIdFolio(this.idFolio);
			dest.setIdCertificado(this.idCertificado);
			dest.setXml(this.xml);
			dest.setCadenaOriginal(this.cadenaOriginal);
			dest.setSello(this.sello);
			dest.setTasaIva(this.tasaIva);
			dest.setDescuento(this.descuento);
			dest.setMotivoDescuento(this.motivoDescuento);
			dest.setTipo(this.tipo);
			dest.setCondicionesPago(this.condicionesPago);
			dest.setIdMetodoPago(this.idMetodoPago);
			dest.setCuenta(this.cuenta);
			dest.setCfdi(this.cfdi);
			dest.setTimbre(this.timbre);
			dest.setState(this.state);
			dest.setDescripcion(this.descripcion);
			dest.setEstadoCancelacion(this.estadoCancelacion);
			dest.setMensajeCancelacion(this.mensajeCancelacion);
			dest.setAcuseCancelacion(this.acuseCancelacion);
			dest.setUuid(this.uuid);
			dest.setFechaTimbrado(this.fechaTimbrado);
			dest.setSelloSat(this.selloSat);
			dest.setNoCertificadoSat(this.noCertificadoSat);
			dest.setObservaciones(this.observaciones);
			dest.setIdMoneda(this.idMoneda);
			dest.setDescripcionMoneda(this.descripcionMoneda);
			dest.setAbreviaturaMoneda(this.abreviaturaMoneda);
			dest.setProvisionada(this.provisionada);
			dest.setProvisionadaPor(this.provisionadaPor);
			dest.setFechaProvisionada(this.fechaProvisionada);
			dest.setTipoCambio(this.tipoCambio);
			dest.setVersion(this.version);
			dest.setUsoCfdi(this.usoCfdi);
			dest.setCfdiRelacionado(this.cfdiRelacionado);
			dest.setCfdiRelacionadoUuid(this.cfdiRelacionadoUuid);
			dest.setCfdiTipoRelacion(this.cfdiTipoRelacion);
			
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