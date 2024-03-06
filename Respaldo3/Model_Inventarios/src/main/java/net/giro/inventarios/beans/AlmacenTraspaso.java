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
	private int tipo; // TIPO: 1-traspaso, 2-devolucion, 3-Solicitud a bodega
	private Date fecha;
	private Almacen idAlmacenOrigen;
	private long entrega;
	private String entregaNombre;
	private long idObra;
	private String nombreObra;
	private Almacen idAlmacenDestino;
	private long recibe;
	private String recibeNombre;
	private AlmacenTraspaso idTraspaso; // ID traspaso que se genero con esta solicitud
	private long solicitudCotizacion; // Solicitud a bodega :: ID Cotizacion
	private long solicitudRequisicion; // Solicitud a bodega :: ID Requisicion
	private int completo; // COMPLETO: 0-En Transito, 1-Completo
	private long owner; // AlmacenMovimiento que origino este registro
	private long idEmpresa;
	private int sistema; // SISTEMA: 0-Normal, 1-Automatico
	private int estatus; // ESTATUS: 0-Activo, 1-Eliminado, 2-Traspaso
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	// -----------------------------------------------
	private String folio;
	private int consecutivo;
	// -----------------------------------------------
	private int recibido; // RECIBIDO: 0-NO, 1-SI
	private long recibidoPor;
	private Date fechaRecibido;
	
	public AlmacenTraspaso() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.entregaNombre = "";
		this.recibeNombre = "";
		this.nombreObra = "";
		this.folio = "";
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
	 * TIPO: 1-traspaso, 2-devolucion, 3-Solicitud a bodega
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * TIPO: 1-traspaso, 2-devolucion, 3-Solicitud a bodega
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

	public Almacen getIdAlmacenOrigen() {
		return idAlmacenOrigen;
	}

	public void setIdAlmacenOrigen(Almacen idAlmacenOrigen) {
		this.idAlmacenOrigen = idAlmacenOrigen;
	}

	public long getEntrega() {
		return entrega;
	}

	public void setEntrega(long entrega) {
		this.entrega = entrega;
	}

	public String getEntregaNombre() {
		return entregaNombre;
	}

	public void setEntregaNombre(String entregaNombre) {
		entregaNombre = (entregaNombre != null ? entregaNombre.trim() : "");
		this.entregaNombre = entregaNombre;
	}

	public long getIdObra() {
		return idObra;
	}

	public void setIdObra(long idObra) {
		this.idObra = idObra;
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		nombreObra = (nombreObra != null ? nombreObra.trim() : "");
		this.nombreObra = nombreObra;
	}

	public Almacen getIdAlmacenDestino() {
		return idAlmacenDestino;
	}

	public void setIdAlmacenDestino(Almacen idAlmacenDestino) {
		this.idAlmacenDestino = idAlmacenDestino;
	}

	public long getRecibe() {
		return recibe;
	}

	public void setRecibe(long recibe) {
		this.recibe = recibe;
	}

	public String getRecibeNombre() {
		return recibeNombre;
	}

	public void setRecibeNombre(String recibeNombre) {
		recibeNombre = (recibeNombre != null ? recibeNombre.trim() : "");
		this.recibeNombre = recibeNombre;
	}

	/**
	 * Traspaso que se genero con la SBO
	 * @return
	 */
	public AlmacenTraspaso getIdTraspaso() {
		return idTraspaso;
	}

	/**
	 * Traspaso que se genero con la SBO
	 * @param idTraspaso
	 */
	public void setIdTraspaso(AlmacenTraspaso idTraspaso) {
		this.idTraspaso = idTraspaso;
	}

	public long getSolicitudCotizacion() {
		return solicitudCotizacion;
	}

	public void setSolicitudCotizacion(long solicitudCotizacion) {
		this.solicitudCotizacion = solicitudCotizacion;
	}

	/**
	 * Solicitud a bodega :: ID de Requisicion
	 * @return
	 */
	public long getSolicitudRequisicion() {
		return solicitudRequisicion;
	}

	/**
	 * Solicitud a bodega :: ID de Requisicion
	 * @param solicitudRequisicion
	 */
	public void setSolicitudRequisicion(long solicitudRequisicion) {
		this.solicitudRequisicion = solicitudRequisicion;
	}

	/**
	 * AlmacenMovimiento que origino este registro
	 * @return
	 */
	public long getOwner() {
		return owner;
	}

	/**
	 * AlmacenMovimiento que origino este registro
	 * @return
	 */
	public void setOwner(long owner) {
		this.owner = owner;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	/**
	 * COMPLETO: 0-En Transito, 1-Completo
	 * @return
	 */
	public int getCompleto() {
		return completo;
	}

	/**
	 * COMPLETO: 0-En Transito, 1-Completo
	 * @param completo
	 */
	public void setCompleto(int completo) {
		this.completo = completo;
	}

	/**
	 * ESTATUS: 0-Activo, 1-Eliminado, 2-Traspaso
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 0-Activo, 1-Eliminado, 2-Traspaso
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	/**
	 * SISTEMA: 0-Normal, 1-Automatico
	 * @return
	 */
	public int getSistema() {
		return sistema;
	}

	/**
	 * SISTEMA: 0-Normal, 1-Automatico
	 * @param sistema
	 */
	public void setSistema(int sistema) {
		this.sistema = sistema;
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

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		folio = (folio != null ? folio.trim() : "");
		this.folio = folio;
	}

	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * RECIBIDO: 0-NO, 1-SI
	 * @return
	 */
	public int getRecibido() {
		return recibido;
	}

	/**
	 * RECIBIDO: 0-NO, 1-SI
	 * @param recibido
	 */
	public void setRecibido(int recibido) {
		this.recibido = recibido;
	}

	public long getRecibidoPor() {
		return recibidoPor;
	}

	public void setRecibidoPor(long recibidoPor) {
		this.recibidoPor = recibidoPor;
	}

	public Date getFechaRecibido() {
		return fechaRecibido;
	}

	public void setFechaRecibido(Date fechaRecibido) {
		this.fechaRecibido = fechaRecibido;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */