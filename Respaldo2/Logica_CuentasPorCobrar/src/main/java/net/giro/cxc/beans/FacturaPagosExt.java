package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.tyg.admon.Banco;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.admon.Moneda;

public class FacturaPagosExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Date fecha;
	private FacturaExt idFactura;
	private Long idBeneficiario;
	private String beneficiario;
	private String tipoBeneficiario;
	private FormasPagos idFormaPago; 
	private String referenciaFormaPago;
	private Banco idBancoOrigen; 
	private BigDecimal monto;
	private Moneda idMoneda;
	private double tipoCambio;
	private CuentaBancaria idCuentaDeposito; 
	private int parcialidad;
	private BigDecimal saldoAnterior;
	private BigDecimal saldoInsoluto;
	private String observaciones;
	private int timbrado;
	private Date fechaTimbrado;
	private long codigo;
	private String mensaje;
	private String serie;
	private String folio;
	private String uuid;
	private byte[] xmlPrevio;
	private byte[] xmlTimbrado;
	private String cadenaOriginal;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public FacturaPagosExt() {
		this.monto = BigDecimal.ZERO;
		this.saldoAnterior = BigDecimal.ZERO;
		this.saldoInsoluto = BigDecimal.ZERO;
		this.beneficiario = "";
		this.tipoBeneficiario = "";
		this.referenciaFormaPago = "";
		this.observaciones = "";
		this.mensaje = "";
		this.serie = "";
		this.folio = "";
		this.uuid = "";
		this.cadenaOriginal = "";
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public FacturaPagosExt(Long id) {
		this();
		this.id = id;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public FacturaExt getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(FacturaExt idFactura) {
		this.idFactura = idFactura;
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}

	public FormasPagos getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(FormasPagos idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public String getReferenciaFormaPago() {
		return referenciaFormaPago;
	}

	public void setReferenciaFormaPago(String referenciaFormaPago) {
		this.referenciaFormaPago = referenciaFormaPago;
	}

	public Banco getIdBancoOrigen() {
		return idBancoOrigen;
	}

	public void setIdBancoOrigen(Banco idBancoOrigen) {
		this.idBancoOrigen = idBancoOrigen;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public Moneda getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Moneda idMoneda) {
		this.idMoneda = idMoneda;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public CuentaBancaria getIdCuentaDeposito() {
		return idCuentaDeposito;
	}

	public void setIdCuentaDeposito(CuentaBancaria idCuentaDeposito) {
		this.idCuentaDeposito = idCuentaDeposito;
	}

	public int getParcialidad() {
		return parcialidad;
	}

	public void setParcialidad(int parcialidad) {
		this.parcialidad = parcialidad;
	}

	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}

	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}

	public BigDecimal getSaldoInsoluto() {
		return saldoInsoluto;
	}

	public void setSaldoInsoluto(BigDecimal saldoInsoluto) {
		this.saldoInsoluto = saldoInsoluto;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(int timbrado) {
		this.timbrado = timbrado;
	}

	public Date getFechaTimbrado() {
		return fechaTimbrado;
	}

	public void setFechaTimbrado(Date fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}

	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public byte[] getXmlPrevio() {
		return xmlPrevio;
	}

	public void setXmlPrevio(byte[] xmlPrevio) {
		this.xmlPrevio = xmlPrevio;
	}

	public byte[] getXmlTimbrado() {
		return xmlTimbrado;
	}

	public void setXmlTimbrado(byte[] xmlTimbrado) {
		this.xmlTimbrado = xmlTimbrado;
	}

	public String getCadenaOriginal() {
		return cadenaOriginal;
	}

	public void setCadenaOriginal(String cadenaOriginal) {
		this.cadenaOriginal = cadenaOriginal;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
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

	// -------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------------

	public String getFacturaDescripcion() {
		if (this.idFactura != null && this.idFactura.getId() != null && this.idFactura.getIdObra() != null)
			return this.idFactura.getFolioFactura() + " - " + this.idFactura.getIdObra().getClienteNombre();
		return "";
	}
	
	public void setFacturaDescripcion(String value) { }

	public String getFormaPago() {
		if (this.idFormaPago != null && this.idFormaPago.getId() != null && this.idFormaPago.getId() > 0L)
			return this.idFormaPago.getFormaPago();
		return "";
	}
	
	public void setFormaPago(String value) {}
	
	public String getCuentaDeposito() {
		if (this.idCuentaDeposito != null && this.idCuentaDeposito.getId() > 0L)
			return this.idCuentaDeposito.getDescripcion();
		return "";
	}
	
	public void setCuentaDeposito(String value) {}
	
	public String getCuentaBanco() {
		if (this.idCuentaDeposito != null && this.idCuentaDeposito.getInstitucionBancaria() != null && this.idCuentaDeposito.getInstitucionBancaria().getId() > 0L)
			return this.idCuentaDeposito.getInstitucionBancaria().getNombreCorto();
		return "";
	}
	
	public void setCuentaBanco(String banco) {}
	
	public String getCuentaNumero() {
		if (this.idCuentaDeposito != null && this.idCuentaDeposito.getId() > 0L)
			return this.idCuentaDeposito.getNumeroDeCuenta();
		return "";
	}
	
	public void setCuentaNumero(String numeroDeCuenta) {}
	
	public String getMoneda() {
		if (this.idMoneda != null && this.idMoneda.getId() > 0L)
			return this.idMoneda.getAbreviacion();
		return "";
	}
	
	public void setMoneda(String value) {}
	
	// -------------------------------------------------------------------------------------------------
	// COPIA
	// -------------------------------------------------------------------------------------------------
	
	public FacturaPagosExt getCopia() {
		return this.Copia();
	}
	
	public FacturaPagosExt Copia() {
		FacturaPagosExt dest = new FacturaPagosExt();

		dest.setId(this.id);
		dest.setFecha(this.fecha);
		dest.setIdFactura(this.idFactura);
		dest.setIdBeneficiario(this.idBeneficiario);
		dest.setBeneficiario(this.beneficiario);
		dest.setTipoBeneficiario(this.tipoBeneficiario);
		dest.setIdFormaPago(this.idFormaPago);
		dest.setReferenciaFormaPago(this.referenciaFormaPago);
		dest.setIdBancoOrigen(this.idBancoOrigen);
		dest.setMonto(this.monto);
		dest.setIdMoneda(this.idMoneda);
		dest.setTipoCambio(this.tipoCambio);
		dest.setIdCuentaDeposito(this.idCuentaDeposito);
		dest.setParcialidad(this.parcialidad);
		dest.setSaldoAnterior(this.saldoAnterior);
		dest.setSaldoInsoluto(this.saldoInsoluto);
		dest.setObservaciones(this.observaciones);
		dest.setTimbrado(this.timbrado);
		dest.setFechaTimbrado(this.fechaTimbrado);
		dest.setCodigo(this.codigo);
		dest.setMensaje(this.mensaje);
		dest.setSerie(this.serie);
		dest.setFolio(this.folio);
		dest.setUuid(this.uuid);
		dest.setXmlPrevio(this.xmlPrevio);
		dest.setXmlTimbrado(this.xmlTimbrado);
		dest.setCadenaOriginal(this.cadenaOriginal);
		dest.setEstatus(this.estatus);
		dest.setCreadoPor(this.creadoPor);
		dest.setFechaCreacion(this.fechaCreacion);
		dest.setModificadoPor(this.modificadoPor);
		dest.setFechaModificacion(this.fechaModificacion);
		
		return dest;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 * 	2.1 | 2018-03-28 | javier Tirado 	| Anado nuevas propiedades: idMoneda, tipoCambio, parcialidad, saldoAnterior, saldoInsoluto, timbrado, codigo, mensaje, xmlPrevio, xmlTimbrado y cadenaOriginal 
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */