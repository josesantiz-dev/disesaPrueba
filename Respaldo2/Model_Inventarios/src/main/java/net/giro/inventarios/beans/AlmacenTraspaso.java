package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * almacen_traspaso
 * @author javaz
 *
 */
public class AlmacenTraspaso implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private int tipo; // TIPO: 1:traspaso, 2:devolucion, 3:Solicitud a bodega
	private Date fecha;
	private long idAlmacenOrigen;
	private long idAlmacenDestino;
	private long entrega;
	private String entregaNombre;
	private long recibe;
	private String recibeNombre;
	private long idOrdenCompra;
	private int completo; // 0:En Transito, 1:Completo
	private int estatus; // 0:Activo, 1:Eliminado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int sistema; // 0:Normal, 1:Automatico
	
	
	public AlmacenTraspaso() {
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

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 1:traspaso, 2:devolucion, 3:Solicitud a bodega
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * 1:traspaso, 2:devolucion, 3:Solicitud a bodega
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
	 * 0:Activo, 1:Eliminado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * 0:Activo, 1:Eliminado
	 * @param estatus
	 */
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

	/**
	 * 0:Normal, 1:Automatico
	 * @return
	 */
	public int getSistema() {
		return sistema;
	}

	/**
	 * 0:Normal, 1:Automatico
	 * @param sistema
	 */
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