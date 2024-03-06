package net.giro.cxc.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Provisiones implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private int grupo;
	private Factura idFactura;
	private double monto;
	private double montoOriginal;
	private String observaciones;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	/*private Long id;
	private Long idSucursal;
	private String descripcionSucursal;
	private Date fecha;
	private double total;
	private String observaciones;
	private Long idTransferencia;
	private double montoAutorizado;
	private double saldoActual;
	private double montoRevisado;
	private Long revisadoPor;
	private Date fechaRevision;
	private Long idEmpresa;
	private Long numLote;
	private String tipoTransaccion;
	private Long idBeneficiario;
	private String tipoBeneficiario;
	private String estatus;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;*/
	
	public Provisiones() {
		this.observaciones = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		/*this.descripcionSucursal = "";
		this.observaciones = "";
		this.tipoTransaccion = "";
		this.tipoBeneficiario = "";
		this.estatus = "";
		this.fecha = Calendar.getInstance().getTime();
		this.fechaRevision = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();*/
	}
	
	public Provisiones(Long id) {
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

	public int getGrupo() {
		return grupo;
	}

	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}

	public Factura getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Factura idFactura) {
		this.idFactura = idFactura;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public double getMontoOriginal() {
		return montoOriginal;
	}

	public void setMontoOriginal(double montoOriginal) {
		this.montoOriginal = montoOriginal;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
}
