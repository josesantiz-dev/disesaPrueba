package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

public class EmpleadoRow implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Propiedades de layout
	private Double consecutivo;
	private String tipoPersona;
	private String primerNombre;
	private String segundoNombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String clave;
	private String rfc;
	private String numeroCuenta;
	private String banco;
	private String sucursal;
	private String area;
	private String puesto;
	private String categoria;
	private String nss;
	private Date fechaIngreso;
	private Date fechaTermino;
	private String centroTrabajo;
	private String periodicidadPago;
	private String diaDescanso;
	private Double sueldo;
	private Double sueldoHora;
	private String modalidadPago;
	private Double descuentoInfonavit;
	private String contratoTipo;
	private String horarioTipo;
	private Double sueldoHoraExtra;
	// Propiedades de control
	private Long idPersona;
	private Long idEmpleado;
	private Long idContrato;
	private boolean nuevaPersona;
	private boolean nuevoEmpleado;
	private boolean nuevoContrato;
	
	public EmpleadoRow() {
		this.tipoPersona = "";
		this.primerNombre = "";
		this.segundoNombre = "";
		this.apellidoPaterno = "";
		this.apellidoMaterno = "";
		this.clave = "";
		this.rfc = "";
		this.numeroCuenta = "";
		this.nss = "";
		this.centroTrabajo = "";
		this.diaDescanso = "";
	}

	// ----------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public Long getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(Long idContrato) {
		this.idContrato = idContrato;
	}
	
	public Double getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Double consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		if (tipoPersona == null) tipoPersona = "";
		this.tipoPersona = tipoPersona.trim();
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		if (primerNombre == null) primerNombre = "";
		this.primerNombre = primerNombre.trim();
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		if (segundoNombre == null) segundoNombre = "";
		this.segundoNombre = segundoNombre.trim();
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		if (apellidoPaterno == null) apellidoPaterno = "";
		this.apellidoPaterno = apellidoPaterno.trim();
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		if (apellidoMaterno == null) apellidoMaterno = "";
		this.apellidoMaterno = apellidoMaterno.trim();
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		if (clave == null) clave = "";
		this.clave = clave.trim();
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		if (rfc == null) rfc = "";
		rfc = rfc.replace("-", "").replace(".","").replace("_","").replace(" ","");
		this.rfc = rfc.trim();
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		if (numeroCuenta == null) numeroCuenta = "";
		numeroCuenta = numeroCuenta.replace("-", "").replace(".","").replace("_","").replace(" ","");
		this.numeroCuenta = numeroCuenta.trim();
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		if (banco == null) banco = "";
		this.banco = banco.trim();
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		if (sucursal == null) sucursal = "";
		this.sucursal = sucursal.trim();
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		if (area == null) area = "";
		this.area = area.trim();
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		if (puesto == null) puesto = "";
		this.puesto = puesto.trim();
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		if (categoria == null) categoria = "";
		this.categoria = categoria.trim();
	}

	public String getNss() {
		return nss;
	}

	public void setNss(String nss) {
		if (nss == null) nss = "";
		nss = nss.replace("-", "").replace(".","").replace("_","").replace(" ","");
		this.nss = nss.trim();
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Date getFechaTermino() {
		return fechaTermino;
	}

	public void setFechaTermino(Date fechaTermino) {
		this.fechaTermino = fechaTermino;
	}

	public String getCentroTrabajo() {
		return centroTrabajo;
	}

	public void setCentroTrabajo(String centroTrabajo) {
		if (centroTrabajo == null) centroTrabajo = "";
		this.centroTrabajo = centroTrabajo.trim();
	}

	public String getPeriodicidadPago() {
		return periodicidadPago;
	}

	public void setPeriodicidadPago(String periodicidadPago) {
		this.periodicidadPago = periodicidadPago;
	}

	public String getDiaDescanso() {
		return diaDescanso;
	}

	public void setDiaDescanso(String diaDescanso) {
		if (diaDescanso == null) diaDescanso = "";
		this.diaDescanso = diaDescanso.trim();
	}

	public Double getSueldo() {
		return sueldo;
	}

	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}

	public Double getSueldoHora() {
		return sueldoHora;
	}

	public void setSueldoHora(Double sueldoHora) {
		this.sueldoHora = sueldoHora;
	}

	public String getModalidadPago() {
		return modalidadPago;
	}

	public void setModalidadPago(String modalidadPago) {
		if (modalidadPago == null) modalidadPago = "";
		this.modalidadPago = modalidadPago.trim();
	}

	public Double getDescuentoInfonavit() {
		return descuentoInfonavit;
	}

	public void setDescuentoInfonavit(Double descuentoInfonavit) {
		this.descuentoInfonavit = descuentoInfonavit;
	}

	public String getContratoTipo() {
		return contratoTipo;
	}

	public void setContratoTipo(String contratoTipo) {
		if (contratoTipo == null) contratoTipo = "";
		this.contratoTipo = contratoTipo.trim();
	}

	public String getHorarioTipo() {
		return horarioTipo;
	}

	public void setHorarioTipo(String horarioTipo) {
		if (horarioTipo == null) horarioTipo = "";
		this.horarioTipo = horarioTipo.trim();
	}

	public Double getSueldoHoraExtra() {
		return sueldoHoraExtra;
	}

	public void setSueldoHoraExtra(Double sueldoHoraExtra) {
		this.sueldoHoraExtra = sueldoHoraExtra;
	}

	public boolean isNuevaPersona() {
		return nuevaPersona;
	}

	public void setNuevaPersona(boolean nuevaPersona) {
		this.nuevaPersona = nuevaPersona;
	}

	public boolean isNuevoEmpleado() {
		return nuevoEmpleado;
	}

	public void setNuevoEmpleado(boolean nuevoEmpleado) {
		this.nuevoEmpleado = nuevoEmpleado;
	}

	public boolean isNuevoContrato() {
		return nuevoContrato;
	}

	public void setNuevoContrato(boolean nuevoContrato) {
		this.nuevoContrato = nuevoContrato;
	}
}
