package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class Obra implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private int tipoObra;
	private Long idObraPrincipal;
	private Long idCliente;
	private String nombreCliente;
	private String tipoCliente;
	private String nombre;
	private String domicilio;
	private Long idDomicilio;
	private String nombreContrato;
	private String objetoContrato;
	private String satic02;
	private BigDecimal montoContratado;
	private BigDecimal montoAnticipo;
	private double porcentajeAnticipo;
	private Date fechaInicio;
	private Date fechaTerminacion;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private Long estatus;
	private String observaciones;
	private Long idSucursal;
	private Long idResponsable;
	private String nombreResponsable;
	private String nombreObraPrincipal;
	private int autorizado;
	private long idUsuarioAutorizo;
	private Date fechaAutorizacion;
	private Long estatusContrato;
	private int revisado;
	private long revisadoPor;
	private Date fechaRevisado;
	private BigDecimal montoDeductiva;
	
	
	public Obra() {
		this.montoContratado = BigDecimal.ZERO;
		this.montoAnticipo = BigDecimal.ZERO;
		this.montoDeductiva = BigDecimal.ZERO;
		this.fechaInicio = Calendar.getInstance().getTime();
		this.fechaTerminacion = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.nombre = "";
		this.nombreObraPrincipal = "";
		this.nombreCliente = "";
		this.tipoCliente = "";
		this.domicilio = "";
		this.nombreContrato = "";
		this.objetoContrato = "";
		this.satic02 = "";
		this.observaciones = "";
		this.nombreResponsable = "";
	}
		
	public Obra(Long id){
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

	public Long getIdObraPrincipal() {
		return idObraPrincipal;
	}

	public void setIdObraPrincipal(Long idObraPrincipal) {
		this.idObraPrincipal = idObraPrincipal;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getSatic02() {
		return satic02;
	}

	public void setSatic02(String satic02) {
		this.satic02 = satic02;
	}

	public BigDecimal getMontoContratado() {
		return montoContratado;
	}

	public void setMontoContratado(BigDecimal montoContratado) {
		this.montoContratado = montoContratado;
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

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public int getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(int tipoObra) {
		this.tipoObra = tipoObra;
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

	public Long getEstatus() {
		return estatus;
	}

	public void setEstatus(Long estatus) {
		this.estatus = estatus;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public Long getIdDomicilio() {
		return idDomicilio;
	}
	
	public void setIdDomicilio(Long idDomicilio) {
		this.idDomicilio = idDomicilio;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
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

	public String getNombreObraPrincipal() {
		return nombreObraPrincipal;
	}

	public void setNombreObraPrincipal(String nombreObraPrincipal) {
		this.nombreObraPrincipal = nombreObraPrincipal;
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
	
	public boolean isAutorizar() {
		return this.autorizado==1 ? true: false;
	}

	public void setAutorizar(boolean autorizar) {
		if(autorizar){
			this.autorizado = 1;
		}else{
			this.autorizado = 0;
		}
	}

	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public Long getEstatusContrato() {
		return estatusContrato;
	}

	public void setEstatusContrato(Long estatusContrato) {
		this.estatusContrato = estatusContrato;
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

	public BigDecimal getMontoDeductiva() {
		return montoDeductiva;
	}

	public void setMontoDeductiva(BigDecimal montoDeductiva) {
		this.montoDeductiva = montoDeductiva;
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