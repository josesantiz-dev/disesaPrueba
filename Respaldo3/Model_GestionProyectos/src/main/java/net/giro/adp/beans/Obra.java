package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.giro.comun.TiposObra;

/**
 * obra
 * @author javaz
 */
public class Obra implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int tipoObra;
	private String nombre;
	private Long idObraPrincipal;
	private String nombreObraPrincipal;
	private Long idCliente;
	private String nombreCliente;
	private String rfcCliente;
	private String tipoCliente;
	private Long idSucursal;
	private String nombreSucursal;
	private Long idResponsable;
	private String nombreResponsable;
	private Long idDomicilio;
	private String domicilio;
	private String nombreContrato;
	private String objetoContrato;
	private Long estatusContrato;
	private String satic02;
	private double tipoCambio;
	private BigDecimal montoContratado;
	private double tipoCambioAnticipo;
	private BigDecimal montoAnticipo;
	private double porcentajeAnticipo;
	private double tipoCambioDeductiva;
	private BigDecimal montoDeductiva;
	private Date fechaInicio;
	private Date fechaTerminacion;
	private String observaciones;
	private int autorizado;
	private long idUsuarioAutorizo;
	private Date fechaAutorizacion;
	private int revisado;
	private long revisadoPor;
	private Date fechaRevisado;
	private Long idMoneda;
	private String descripcionMoneda;
	private Long idEmpresa;
	private Date fechaEstatus;
	private Long estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public Obra() {
		this.montoContratado = BigDecimal.ZERO;
		this.montoAnticipo = BigDecimal.ZERO;
		this.montoDeductiva = BigDecimal.ZERO;
		this.nombre = "";
		this.nombreObraPrincipal = "";
		this.nombreCliente = "";
		this.rfcCliente = "";
		this.tipoCliente = "";
		this.domicilio = "";
		this.nombreContrato = "";
		this.objetoContrato = "";
		this.satic02 = "";
		this.observaciones = "";
		this.nombreResponsable = "";
		this.descripcionMoneda = "";
		this.fechaInicio = Calendar.getInstance().getTime();
		this.fechaTerminacion = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
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

	public int getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(int tipoObra) {
		this.tipoObra = tipoObra;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getIdObraPrincipal() {
		return idObraPrincipal;
	}

	public void setIdObraPrincipal(Long idObraPrincipal) {
		this.idObraPrincipal = idObraPrincipal;
	}

	public String getNombreObraPrincipal() {
		return nombreObraPrincipal;
	}

	public void setNombreObraPrincipal(String nombreObraPrincipal) {
		this.nombreObraPrincipal = nombreObraPrincipal;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getRfcCliente() {
		return rfcCliente;
	}

	public void setRfcCliente(String rfcCliente) {
		this.rfcCliente = rfcCliente;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
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

	public Long getIdResponsable() {
		return idResponsable;
	}

	public void setIdResponsable(Long idResponsable) {
		this.idResponsable = idResponsable;
	}

	public String getNombreResponsable() {
		return nombreResponsable;
	}

	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}

	public Long getIdDomicilio() {
		return idDomicilio;
	}

	public void setIdDomicilio(Long idDomicilio) {
		this.idDomicilio = idDomicilio;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getNombreContrato() {
		return nombreContrato;
	}

	public void setNombreContrato(String nombreContrato) {
		this.nombreContrato = nombreContrato;
	}

	public String getObjetoContrato() {
		return objetoContrato;
	}

	public void setObjetoContrato(String objetoContrato) {
		this.objetoContrato = objetoContrato;
	}

	public Long getEstatusContrato() {
		return estatusContrato;
	}

	public void setEstatusContrato(Long estatusContrato) {
		this.estatusContrato = estatusContrato;
	}

	public String getSatic02() {
		return satic02;
	}

	public void setSatic02(String satic02) {
		this.satic02 = satic02;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public BigDecimal getMontoContratado() {
		return montoContratado;
	}

	public void setMontoContratado(BigDecimal montoContratado) {
		this.montoContratado = montoContratado;
	}

	public double getTipoCambioAnticipo() {
		return tipoCambioAnticipo;
	}

	public void setTipoCambioAnticipo(double tipoCambioAnticipo) {
		this.tipoCambioAnticipo = tipoCambioAnticipo;
	}

	public BigDecimal getMontoAnticipo() {
		return montoAnticipo;
	}

	public void setMontoAnticipo(BigDecimal montoAnticipo) {
		this.montoAnticipo = montoAnticipo;
	}

	public double getPorcentajeAnticipo() {
		return porcentajeAnticipo;
	}

	public void setPorcentajeAnticipo(double porcentajeAnticipo) {
		this.porcentajeAnticipo = porcentajeAnticipo;
	}

	public double getTipoCambioDeductiva() {
		return tipoCambioDeductiva;
	}

	public void setTipoCambioDeductiva(double tipoCambioDeductiva) {
		this.tipoCambioDeductiva = tipoCambioDeductiva;
	}

	public BigDecimal getMontoDeductiva() {
		return montoDeductiva;
	}

	public void setMontoDeductiva(BigDecimal montoDeductiva) {
		this.montoDeductiva = montoDeductiva;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaTerminacion() {
		return fechaTerminacion;
	}

	public void setFechaTerminacion(Date fechaTerminacion) {
		this.fechaTerminacion = fechaTerminacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(int autorizado) {
		this.autorizado = autorizado;
	}

	public long getIdUsuarioAutorizo() {
		return idUsuarioAutorizo;
	}

	public void setIdUsuarioAutorizo(long idUsuarioAutorizo) {
		this.idUsuarioAutorizo = idUsuarioAutorizo;
	}

	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public int getRevisado() {
		return revisado;
	}

	public void setRevisado(int revisado) {
		this.revisado = revisado;
	}

	public long getRevisadoPor() {
		return revisadoPor;
	}

	public void setRevisadoPor(long revisadoPor) {
		this.revisadoPor = revisadoPor;
	}

	public Date getFechaRevisado() {
		return fechaRevisado;
	}

	public void setFechaRevisado(Date fechaRevisado) {
		this.fechaRevisado = fechaRevisado;
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

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Date getFechaEstatus() {
		return fechaEstatus;
	}

	public void setFechaEstatus(Date fechaEstatus) {
		this.fechaEstatus = fechaEstatus;
	}

	public Long getEstatus() {
		return estatus;
	}

	public void setEstatus(Long estatus) {
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
	
	// -------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------
	
	public boolean isAdministrativa() {
		return (this.tipoObra == TiposObra.Administrativa.ordinal());
	}
	
	public void setAdministrativa(boolean value) {}

	public String getFechaEstatusFormato() {
		if (this.fechaEstatus != null)
			return (new SimpleDateFormat("yyyy-MM-dd")).format(this.fechaEstatus);
		return "";
	}

	public void setFechaEstatusFormato(String value) {}

	public boolean isAutorizar() {
		return (getAutorizado() == 1);
	}

	public void setAutorizar(boolean autorizar) {
		setAutorizado(autorizar ? 1 : 0);
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2	| 14/06/2016 | Javier Tirado	| Se agrega propeiedad para almacenar EstatusObra
 *  2.2	| 23/06/2016 | Javier Tirado	| Se agrega propiedad Revisado (revisado, revisadoPor y fechaRevisado)
 *  1.2	| 27/10/2016 | Javier Tirado	| Agrego la propiedad MONTO DEDUCTIVA
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */