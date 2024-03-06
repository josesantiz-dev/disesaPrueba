package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class PagosGastos implements Serializable {
	private static final long serialVersionUID = 3928105225092464813L;

	// Fields
	private Long id;
	private Date fecha;
	private String tipo;
	private String estatus;
	private String beneficiario;
	private Integer noCheque;
	private Double monto;
	private String operacion;
	private String tipoBeneficiario;
	private Long idPagosGastosRef;
	private Integer idPagoMultiple;
	private String nota;
	private String concepto;
	private String folioAutorizacion;
	private Double tipoCambio;
	private Long segmento;
	private Long idSpeiOutgoing; //SpeiOutgoing
	private Integer detallePolizaId;
	private Long idCuentaOrigen; // CtasBanco
	private Long idCuentaDestino;
	private Long idCuentaOrigenTerceros; //Tygpersonas
	private Long idCuentaDestinoTerceros; //Tygpersonas
	private Long idBeneficiario; //Tygpersonas
	private Long idSucursal; //SucursalesView
	private Long idTiposMovimiento; //ConValores
	private Long idObra; //  corresponde a Obra del modelo gestion proyectos
	private Long idOrdenCompra;
	private String area;
	private int consecutivo;
	private Double montoLimite;
	private String numeroCuentaOrigen;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private long voboPor;
	private Date fechaVobo;
	private long autorizadoPor;
	private Date fechaAutorizado;

	// Constructors	
	public PagosGastos() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.fechaVobo = Calendar.getInstance().getTime();
		this.fechaAutorizado = Calendar.getInstance().getTime();
		this.monto = 0D;
		this.tipoCambio = 0D;
		this.montoLimite = 0D;
		this.idOrdenCompra = 0L;
		this.tipo = "";
		this.estatus = "";
		this.beneficiario = "";
		this.operacion = "";
		this.tipoBeneficiario = "";
		this.nota = "";
		this.concepto = "";
		this.folioAutorizacion = "";
		this.area = "";
		this.numeroCuentaOrigen = "";
	}
	
	public PagosGastos(Long id) {
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public Integer getNoCheque() {
		return noCheque;
	}

	public void setNoCheque(Integer noCheque) {
		this.noCheque = noCheque;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}

	public Long getIdPagosGastosRef() {
		return idPagosGastosRef;
	}

	public void setIdPagosGastosRef(Long idPagosGastosRef) {
		this.idPagosGastosRef = idPagosGastosRef;
	}

	public Integer getIdPagoMultiple() {
		return idPagoMultiple;
	}

	public void setIdPagoMultiple(Integer idPagoMultiple) {
		this.idPagoMultiple = idPagoMultiple;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getFolioAutorizacion() {
		return folioAutorizacion;
	}

	public void setFolioAutorizacion(String folioAutorizacion) {
		this.folioAutorizacion = folioAutorizacion;
	}

	public Double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(Double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public Long getSegmento() {
		return segmento;
	}

	public void setSegmento(Long segmento) {
		this.segmento = segmento;
	}

	public Long getIdSpeiOutgoing() {
		return idSpeiOutgoing;
	}

	public void setIdSpeiOutgoing(Long idSpeiOutgoing) {
		this.idSpeiOutgoing = idSpeiOutgoing;
	}

	public Integer getDetallePolizaId() {
		return detallePolizaId;
	}

	public void setDetallePolizaId(Integer detallePolizaId) {
		this.detallePolizaId = detallePolizaId;
	}

	public Long getIdCuentaOrigen() {
		return idCuentaOrigen;
	}

	public void setIdCuentaOrigen(Long idCuentaOrigen) {
		this.idCuentaOrigen = idCuentaOrigen;
	}

	public Long getIdCuentaDestino() {
		return idCuentaDestino;
	}

	public void setIdCuentaDestino(Long idCuentaDestino) {
		this.idCuentaDestino = idCuentaDestino;
	}

	public Long getIdCuentaOrigenTerceros() {
		return idCuentaOrigenTerceros;
	}

	public void setIdCuentaOrigenTerceros(Long idCuentaOrigenTerceros) {
		this.idCuentaOrigenTerceros = idCuentaOrigenTerceros;
	}

	public Long getIdCuentaDestinoTerceros() {
		return idCuentaDestinoTerceros;
	}

	public void setIdCuentaDestinoTerceros(Long idCuentaDestinoTerceros) {
		this.idCuentaDestinoTerceros = idCuentaDestinoTerceros;
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public Long getIdTiposMovimiento() {
		return idTiposMovimiento;
	}

	public void setIdTiposMovimiento(Long idTiposMovimiento) {
		this.idTiposMovimiento = idTiposMovimiento;
	}

	public Long getIdObra() {
		return idObra;
	}

	public void setIdObra(Long idObra) {
		this.idObra = idObra;
	}

	public Long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(Long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Double getMontoLimite() {
		return montoLimite;
	}

	public void setMontoLimite(Double montoLimite) {
		this.montoLimite = montoLimite;
	}

	public String getNumeroCuentaOrigen() {
		return numeroCuentaOrigen;
	}

	public void setNumeroCuentaOrigen(String numeroCuentaOrigen) {
		this.numeroCuentaOrigen = numeroCuentaOrigen;
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

	public long getVoboPor() {
		return voboPor;
	}

	public void setVoboPor(long voboPor) {
		this.voboPor = voboPor;
	}

	public Date getFechaVobo() {
		return fechaVobo;
	}

	public void setFechaVobo(Date fechaVobo) {
		this.fechaVobo = fechaVobo;
	}

	public long getAutorizadoPor() {
		return autorizadoPor;
	}

	public void setAutorizadoPor(long autorizadoPor) {
		this.autorizadoPor = autorizadoPor;
	}

	public Date getFechaAutorizado() {
		return fechaAutorizado;
	}

	public void setFechaAutorizado(Date fechaAutorizado) {
		this.fechaAutorizado = fechaAutorizado;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.1 | 2016-10-03 | Javier Tirado    | Añadimos las propiedades  VISTO BUENO (vobo) y AUTORIZADO
 *  1.1 | 2016-10-28 | Javier Tirado    | Añado propiedad idOrdenCompra
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */