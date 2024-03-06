package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Date;

public class AlmacenTraspaso implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private long idAlmacenOrigen;
	private long idAlmacenDestino;
	private Date fecha;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int estatus;
	private long entrega;
	private long recibe;
	private int completo;
	private int tipo;
	
	
	public AlmacenTraspaso() {}

	
	public Long getId() {
		return id;
	}

	public void setId(long id) {	//meotodo necesatio para tomar el correlativo
		this.id = id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public long getIdAlmacenOrigen() {
		return idAlmacenOrigen;
	}

	public void setIdAlmacenOrigen(long idAlmacenOrigen) {
		this.idAlmacenOrigen = idAlmacenOrigen;
	}

	public long getIdAlmacenDestino() {
		return idAlmacenDestino;
	}

	public void setIdAlmacenDestino(long idAlmacenDestino) {
		this.idAlmacenDestino = idAlmacenDestino;
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

	public long getEntrega() {
		return entrega;
	}

	public void setEntrega(long entrega) {
		this.entrega = entrega;
	}

	public long getRecibe() {
		return recibe;
	}

	public void setRecibe(long recibe) {
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