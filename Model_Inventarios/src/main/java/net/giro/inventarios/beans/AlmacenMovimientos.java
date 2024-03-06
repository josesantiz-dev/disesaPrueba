package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * almacen_movimientos
 * @author javaz
 *
 */
public class AlmacenMovimientos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date fecha;
	private int tipo; // 0:Entrada, 1:Salida
	private String tipoEntrada; // OC: Orden de Compra, DE: Devolucion, TR: Traspaso, OB: Obra, SO: Sobrante
	private String folioFactura;
	private Almacen idAlmacen;
	private long recibe;
	private String recibeNombre;
	private long entrega;
	private String entregaNombre;
	private long idObra;
	private String nombreObra;
	private long idOrdenCompra;
	private String folioOrdenCompra;
	private long idTraspaso; // AlmacenTraspaso
	private long idSalida; // AlmacenMovimientos
	private long owner; // AlmacenMovimiento que origino este registro
	private long idEmpresa;
	private int sistema; // 0:Normal, 1:Automatico
	private int estatus; // 0:Activo, 1:Eliminado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private String folio;
	private int consecutivo;
	
	public AlmacenMovimientos() {
		this.tipoEntrada = "";
		this.folioFactura = "";
		this.folioOrdenCompra = "";
		this.nombreObra = "";
		this.recibeNombre = "";
		this.entregaNombre = "";
		this.folio = "";
		this.fecha = Calendar.getInstance().getTime();
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * 0:Entrada, 1:Salida
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * 0:Entrada, 1:Salida
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	/**
	 * OC: Orden Compra, TR:Traspaso, DE: Devolucion, SO: Instalacion
	 * @return
	 */
	public String getTipoEntrada() {
		return tipoEntrada;
	}

	/**
	 * OC: Orden Compra, TR:Traspaso, DE: Devolucion, SO: Instalacion
	 * @param tipoEntrada
	 */
	public void setTipoEntrada(String tipoEntrada) {
		tipoEntrada = (tipoEntrada != null ? tipoEntrada.trim() : "");
		this.tipoEntrada = tipoEntrada;
	}

	public String getFolioFactura() {
		return folioFactura;
	}

	public void setFolioFactura(String folioFactura) {
		folioFactura = (folioFactura != null ? folioFactura.trim() : "");
		this.folioFactura = folioFactura;
	}

	public Almacen getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(Almacen idAlmacen) {
		this.idAlmacen = idAlmacen;
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

	public long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public String getFolioOrdenCompra() {
		return folioOrdenCompra;
	}

	public void setFolioOrdenCompra(String folioOrdenCompra) {
		folioOrdenCompra = (folioOrdenCompra != null ? folioOrdenCompra.trim() : "");
		this.folioOrdenCompra = folioOrdenCompra;
	}

	/**
	 * AlmacenTraspaso
	 * @return
	 */
	public long getIdTraspaso() {
		return idTraspaso;
	}

	/**
	 * AlmacenTraspaso
	 * @param idTraspaso
	 */
	public void setIdTraspaso(long idTraspaso) {
		this.idTraspaso = idTraspaso;
	}

	/**
	 * AlmacenMovimientos
	 * @return
	 */
	public long getIdSalida() {
		return idSalida;
	}

	/**
	 * AlmacenMovimientos
	 * @param idSalida
	 */
	public void setIdSalida(long idSalida) {
		this.idSalida = idSalida;
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
	 * @param owner
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */