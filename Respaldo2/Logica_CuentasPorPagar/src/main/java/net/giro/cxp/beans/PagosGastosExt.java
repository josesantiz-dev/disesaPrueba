package net.giro.cxp.beans;

import java.util.Calendar;
import java.util.Date;

import net.giro.adp.beans.Obra;
import net.giro.ne.beans.Sucursal;
import net.giro.plataforma.beans.ConValores;

/**
 * pagos_gastos
 * @author javaz
 *
 */
public class PagosGastosExt implements java.io.Serializable {
	private static final long serialVersionUID = 3928105225092464813L;

	private Long id;
	private Date fecha;
	private String tipo;
	private String estatus;
	private String beneficiario;
	private Integer noCheque;
	private Double monto;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private String operacion;
	private String tipoBeneficiario;
	private Long idPagosGastosRef;
	private Integer idPagoMultiple;
	private String nota;
	private String concepto;
	private String folioAutorizacion;
	private Double tipoCambio;
	private Long segmento;
	private String area;
	private Long idSpeiOutgoing; //SpeiOutgoing
	private Integer detallePolizaId;
	private CtasBancoExt idCuentaOrigen; // CtasBanco
	private CtasBancoExt idCuentaDestino;
	private PersonaExt idCuentaOrigenTerceros; //Tygpersonas
	private PersonaExt idCuentaDestinoTerceros; //Tygpersonas
	private PersonaExt idBeneficiario; //Tygpersonas
	private Sucursal idSucursal; //SucursalesView
	private ConValores idTiposMovimiento; //ConValores
	private Obra idObra; // Obra --> Model_GestionProyectos
	private int consecutivo;
	private Double montoLimite;
	private long voboPor;
	private Date fechaVobo;
	private long autorizadoPor;
	private Date fechaAutorizado;
	private long idOrdenCompra; //OrdenCompraExt idOrdenCompra;
	private String numeroCuentaOrigen;
	private Long idEmpresa;

	
	// Constructors
	public PagosGastosExt() { 
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.monto = 0D;
		this.tipoCambio = 0D;
	}

	public PagosGastosExt(Long id) {
		this.id = id;
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.monto = 0D;
		this.tipoCambio = 0D;
	}

	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CtasBancoExt getIdCuentaOrigen() {
		return this.idCuentaOrigen;
	}

	public void setIdCuentaOrigen(CtasBancoExt idCuentaOrigen) {
		this.idCuentaOrigen = idCuentaOrigen;
		if (this.idCuentaOrigen != null)
			this.numeroCuentaOrigen = idCuentaOrigen.getNumeroDeCuenta();
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getBeneficiario() {
		return this.beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public Integer getNoCheque() {
		return this.noCheque;
	}

	public void setNoCheque(Integer noCheque) {
		this.noCheque = noCheque;
	}

	public Double getMonto() {
		return this.monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public long getCreadoPor() {
		return this.creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return this.modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	public Sucursal getIdSucursal() {
		return this.idSucursal;
	}

	public void setIdSucursal(Sucursal idSucursal) {
		this.idSucursal = idSucursal;
	}

	public PersonaExt getIdBeneficiario() {
		return this.idBeneficiario;
	}

	public void setIdBeneficiario(PersonaExt idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public String getOperacion() {
		return this.operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getTipoBeneficiario() {
		return this.tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}
	
	public CtasBancoExt getIdCuentaDestino() {
		return this.idCuentaDestino;
	}

	public void setIdCuentaDestino(CtasBancoExt idCuentaDestino) {
		this.idCuentaDestino = idCuentaDestino;
	}
	
	public PersonaExt getIdCuentaDestinoTerceros() {
		return this.idCuentaDestinoTerceros;
	}

	public void setIdCuentaDestinoTerceros(PersonaExt idCuentaDestinoTerceros) {
		this.idCuentaDestinoTerceros = idCuentaDestinoTerceros;
	}

	public Long getIdPagosGastosRef() {
		return this.idPagosGastosRef;
	}

	public void setIdPagosGastosRef(Long idPagosGastosRef) {
		this.idPagosGastosRef = idPagosGastosRef;
	}
	
	public ConValores getIdTiposMovimiento() {
		return this.idTiposMovimiento;
	}

	public void setIdTiposMovimiento(ConValores idTiposMovimiento) {
		this.idTiposMovimiento = idTiposMovimiento;
	}

	public Integer getIdPagoMultiple() {
		return this.idPagoMultiple;
	}

	public void setIdPagoMultiple(Integer idPagoMultiple) {
		this.idPagoMultiple = idPagoMultiple;
	}

	public String getNota() {
		return this.nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getConcepto() {
		return this.concepto;
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
	
	public PersonaExt getIdCuentaOrigenTerceros() {
		return idCuentaOrigenTerceros;
	}

	public void setIdCuentaOrigenTerceros(PersonaExt idCuentaOrigenTerceros) {
		this.idCuentaOrigenTerceros = idCuentaOrigenTerceros;
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

	public Obra getIdObra() {
		return idObra;
	}

	public void setIdObra(Obra idObra) {
		this.idObra = idObra;
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

	public long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public String getNumeroCuentaOrigen() {
		return numeroCuentaOrigen;
	}

	public void setNumeroCuentaOrigen(String numeroCuentaOrigen) {
		this.numeroCuentaOrigen = numeroCuentaOrigen;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.1 | 2016-10-03 | Javier Tirado 	| Añadimos las propiedades  VISTO BUENO (vobo) y AUTORIZADO
 *  1.1 | 2016-10-28 | Javier Tirado 	| Añado propiedad idOrdenCompra
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */