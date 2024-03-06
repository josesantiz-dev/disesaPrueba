package net.giro.inventarios.beans; 

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.rh.admon.beans.Empleado;

public class AlmacenTraspasoExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private int tipo; // TIPO: 1 - traspaso, 2 - devolucion, 3 - Solicitud a Bodega
	private Date fecha;
	private Almacen almacenOrigen;
	private Almacen almacenDestino;
	private Empleado entrega;
	private String entregaNombre;
	private Empleado recibe;
	private String recibeNombre;
	private long idOrdenCompra;
	private int completo; // 0:En Transito, 1:Completo
	private int estatus; // 0:Activo, 1:En Transito
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int sistema; // 0:Normal, 1:Automatico
	
	
	public AlmacenTraspasoExt() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.entregaNombre = "";
		this.recibeNombre = "";
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Almacen getAlmacenOrigen() {
		return almacenOrigen;
	}

	public void setAlmacenOrigen(Almacen almacenOrigen) {
		this.almacenOrigen = almacenOrigen;
	}

	public Almacen getAlmacenDestino() {
		return almacenDestino;
	}

	public void setAlmacenDestino(Almacen almacenDestino) {
		this.almacenDestino = almacenDestino;
	}
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public Empleado getEntrega() {
		return entrega;
	}

	public void setEntrega(Empleado entrega) {
		this.entrega = entrega;
		if (entrega != null)
			this.entregaNombre = entrega.getNombre();
	}

	public Empleado getRecibe() {
		return recibe;
	}

	public void setRecibe(Empleado recibe) {
		this.recibe = recibe;
		if (recibe != null)
			this.recibeNombre = recibe.getNombre();
	}

	public long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	/**
	 * 0:En Transito, 1:Completo
	 * @return
	 */
	public int getCompleto() {
		return completo;
	}

	/**
	 * 0:En Transito, 1:Completo
	 * @param completo
	 */
	public void setCompleto(int completo) {
		this.completo = completo;
	}

	/**
	 * TIPO: 1 - traspaso, 2 - devolucion, 3 - Solicitud a Bodega
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * TIPO: 1 - traspaso, 2 - devolucion, 3 - Solicitud a Bodega
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getSistema() {
		return sistema;
	}

	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	public String getEntregaNombre() {
		return entregaNombre;
	}

	public void setEntregaNombre(String entregaNombre) {
		this.entregaNombre = entregaNombre;
	}

	public String getRecibeNombre() {
		return recibeNombre;
	}

	public void setRecibeNombre(String recibeNombre) {
		this.recibeNombre = recibeNombre;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */