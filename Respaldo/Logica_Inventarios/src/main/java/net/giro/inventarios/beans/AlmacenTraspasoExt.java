package net.giro.inventarios.beans; 

import java.io.Serializable;
import java.util.Date;

import net.giro.rh.admon.beans.Empleado;

public class AlmacenTraspasoExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Almacen almacenOrigen;
	private Almacen almacenDestino;
	private Date fecha;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int estatus;
	private Empleado entrega;
	private Empleado recibe;
	private int completo;
	private int tipo;
	
	
	public AlmacenTraspasoExt() {}

	
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
	}

	public Empleado getRecibe() {
		return recibe;
	}

	public void setRecibe(Empleado recibe) {
		this.recibe = recibe;
	}

	public int getCompleto() {
		return completo;
	}

	public void setCompleto(int completo) {
		this.completo = completo;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */