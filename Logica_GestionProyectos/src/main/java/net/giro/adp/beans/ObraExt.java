package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.PersonaExt;
import net.giro.comun.TiposObra;
import net.giro.ne.beans.Sucursal;
import net.giro.rh.admon.beans.EmpleadoExt;

/**
 * obra
 * @author javaz
 */
public class ObraExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int tipoObra;
	private String nombre;
	private Obra idObraPrincipal;
	private String nombreObraPrincipal;
	private PersonaExt idCliente;
	private String nombreCliente;
	private String rfcCliente;
	private String tipoCliente;
	private Sucursal idSucursal;
	private String nombreSucursal;
	private EmpleadoExt idResponsable;
	private String nombreResponsable;
	private DomicilioExt pojoDomicilio; 
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
	private String descMoneda;
	private Long idEmpresa;
	private Date fechaEstatus;
	private Long estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
 	public ObraExt() {
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
		this.descMoneda = "";
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

	public Obra getIdObraPrincipal() {
		return idObraPrincipal;
	}

	public void setIdObraPrincipal(Obra idObraPrincipal) {
		this.idObraPrincipal = idObraPrincipal;
		if (idObraPrincipal != null)
			this.nombreObraPrincipal = idObraPrincipal.getNombre();
	}

	public String getNombreObraPrincipal() {
		return nombreObraPrincipal;
	}

	public void setNombreObraPrincipal(String nombreObraPrincipal) {
		this.nombreObraPrincipal = nombreObraPrincipal;
	}

	public PersonaExt getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(PersonaExt idCliente) {
		this.idCliente = idCliente;
		if (idCliente != null) {
			this.nombreCliente = idCliente.getNombre();
			this.rfcCliente = idCliente.getRfc();
		}
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

	public EmpleadoExt getIdResponsable() {
		return idResponsable;
	}

	public void setIdResponsable(EmpleadoExt idResponsable) {
		this.idResponsable = idResponsable;
		if (idResponsable != null)
			this.nombreResponsable = idResponsable.getNombre();
	}

	public String getNombreResponsable() {
		return nombreResponsable;
	}

	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}

	public DomicilioExt getPojoDomicilio() {
		return pojoDomicilio;
	}

	public void setPojoDomicilio(DomicilioExt pojoDomicilio) {
		this.pojoDomicilio = pojoDomicilio;
		if (pojoDomicilio != null)
			this.domicilio = this.pojoDomicilio.getDomicilio();
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

	public String getDescMoneda() {
		return descMoneda;
	}

	public void setDescMoneda(String descMoneda) {
		this.descMoneda = descMoneda;
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

	// --------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------

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
		return (this.autorizado == 1);
	}

	public void setAutorizar(boolean autorizar) {
		this.autorizado = (autorizar ? 1 : 0);
	}
	
	public String getObraPrincipal() {
		if (this.idObraPrincipal != null && this.idObraPrincipal.getId() != null && this.idObraPrincipal.getNombre() != null)
			return this.idObraPrincipal.getId() + " - " + this.idObraPrincipal.getNombre();
		return "";
	}
	
	public void setObraPrincipal(String value) {}
	
	public String getCliente() {
		if (this.idCliente != null && this.idCliente.getId() > 0L && this.idCliente.getNombre() != null) 
			return this.idCliente.getId() + " - " + this.idCliente.getNombre();
		return "";
	}
	
	public void setCliente(String value) {}
	
	public String getClienteNombre() {
		if (this.idCliente != null && this.idCliente.getNombre() != null) 
			return this.idCliente.getNombre();
		return "";
	}
	
	public void setClienteNombre(String value) {}
	
	public String getSucursal() {
		if (this.idSucursal != null && this.idSucursal.getId() > 0L && this.idSucursal.getSucursal() != null)
			return this.idSucursal.getId() + " - " + this.idSucursal.getSucursal();
		return "";
	}
	
	public void setSucursal(String value) {}
	
	public String getSucursalNombre() {
		if (this.idSucursal != null && this.idSucursal.getSucursal() != null)
			return this.idSucursal.getSucursal();
		return "";
	}
	
	public void setSucursalNombre(String value) {}
	
	public String getResponsable() {
		if (this.idResponsable != null && this.idResponsable.getId() != null && this.idResponsable.getNombre() != null)
			return this.idResponsable.getId() + " - " + this.idResponsable.getNombre();
		return "";
	}
	
	public void setResponsable(String value) {}
	
	public String getResponsableNombre() {
		if (this.idResponsable != null && this.idResponsable.getNombre() != null)
			return this.idResponsable.getNombre();
		return "";
	}
	
	public void setResponsableNombre(String value) {}
	
	// --------------------------------------------------------------------------
	// METODOS
	// --------------------------------------------------------------------------
	
	public ObraExt getCopia() {
		return Copia();
	}
	
	public ObraExt Copia() {
		ObraExt dest = new ObraExt();
		
		try {
			dest.setId(this.id);
			dest.setNombre(this.nombre);
			dest.setTipoObra(this.tipoObra);
			dest.setTipoCambio(this.tipoCambio);
			dest.setMontoContratado(this.montoContratado);
			dest.setTipoCambioAnticipo(this.tipoCambioAnticipo);
			dest.setMontoAnticipo(this.montoAnticipo);
			dest.setPorcentajeAnticipo(this.porcentajeAnticipo);
			dest.setTipoCambioDeductiva(this.tipoCambioDeductiva);
			dest.setMontoDeductiva(this.montoDeductiva);
			dest.setDomicilio(this.domicilio);
			dest.setFechaInicio(this.fechaInicio);
			dest.setFechaTerminacion(this.fechaTerminacion);
			dest.setNombreContrato(this.nombreContrato);
			dest.setObjetoContrato(this.objetoContrato);
			dest.setSatic02(this.satic02);
			dest.setNombreCliente(this.nombreCliente);
			dest.setTipoCliente(this.tipoCliente);
			dest.setObservaciones(this.observaciones);
			dest.setNombreResponsable(this.nombreResponsable);
			dest.setNombreObraPrincipal(this.nombreObraPrincipal);
			dest.setAutorizado(this.autorizado);
			dest.setIdUsuarioAutorizo(this.idUsuarioAutorizo);
			dest.setFechaAutorizacion(this.fechaAutorizacion);
			dest.setRevisado(this.revisado);
			dest.setRevisadoPor(this.revisadoPor);
			dest.setFechaRevisado(this.fechaRevisado);
			dest.setIdMoneda(this.idMoneda);
			dest.setDescMoneda(this.descMoneda);
			dest.setIdEmpresa(this.idEmpresa);
			dest.setFechaEstatus(this.fechaEstatus);
			dest.setEstatus(this.estatus);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			dest.setModificadoPor(this.modificadoPor);
			
			if (this.getIdObraPrincipal() != null)
				dest.setIdObraPrincipal(this.getIdObraPrincipal());
			
			if (this.getIdCliente() != null)
				dest.setIdCliente(this.getIdCliente());
			
			if (this.getPojoDomicilio() != null)
				dest.setPojoDomicilio(this.getPojoDomicilio());
			
			if (this.getIdSucursal() != null)
				dest.setIdSucursal(this.getIdSucursal());
			
			if (this.getIdResponsable() != null)
				dest.setIdResponsable(this.getIdResponsable());
			
			if (this.getEstatusContrato() != null)
				dest.setEstatusContrato(this.getEstatusContrato());
		} catch (Exception e) {
			throw e;
		}
		
		return dest;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2	| 14/06/2016 | Javier Tirado	| Se agrega propiedad para almacenar EstatusObra
 *  2.2	| 23/06/2016 | Javier Tirado	| Se agrega propiedad Revisado (revisado, revisadoPor y fechaRevisado)
 *  1.2	| 27/10/2016 | Javier Tirado	| Agrego la propiedad MONTO DEDUCTIVA
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */