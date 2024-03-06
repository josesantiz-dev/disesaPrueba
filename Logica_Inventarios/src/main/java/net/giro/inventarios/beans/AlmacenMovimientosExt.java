package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.rh.admon.beans.Empleado;

/**
 * almacen_movimientos
 * @author javaz
 *
 */
public class AlmacenMovimientosExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date fecha;
	private int tipo; // 0:Entrada, 1:Salida
	private String tipoEntrada; // OC: Entrada x Orden de Compra, DE: Entrada x Devolucion, TR: Entrada x Traspaso
	private String folioFactura;
	private Almacen almacen;
	private Empleado entrega;
	private String entregaNombre;
	private Empleado recibe;
	private String recibeNombre;
	private long idObra;
	private String nombreObra;
	private long idOrdenCompra;
	private String folioOrdenCompra;
	private long idTraspaso;
	private AlmacenMovimientosExt idSalida;
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
	
	public AlmacenMovimientosExt() {
		this.tipoEntrada = "";
		this.folioFactura = "";
		this.folioOrdenCompra = "";
		this.nombreObra = "";
		this.recibeNombre = "";
		this.entregaNombre = "";
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
	 * OC: Entrada x Orden de Compra, DE: Entrada x Devolucion, TR: Entrada x Traspaso
	 * @return
	 */
	public String getTipoEntrada() {
		return tipoEntrada;
	}

	/**
	 * OC: Entrada x Orden de Compra, DE: Entrada x Devolucion, TR: Entrada x Traspaso
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

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

	public Empleado getRecibe() {
		return recibe;
	}

	public void setRecibe(Empleado recibe) {
		this.recibe = recibe;
		if (recibe != null)
			this.recibeNombre = recibe.getNombre();
	}

	public String getRecibeNombre() {
		return recibeNombre;
	}

	public void setRecibeNombre(String recibeNombre) {
		recibeNombre = (recibeNombre != null ? recibeNombre.trim() : "");
		this.recibeNombre = recibeNombre;
	}

	public Empleado getEntrega() {
		return entrega;
	}

	public void setEntrega(Empleado entrega) {
		this.entrega = entrega;
		if (entrega != null)
			this.entregaNombre = entrega.getNombre();
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

	public long getIdTraspaso() {
		return idTraspaso;
	}

	public void setIdTraspaso(long idTraspaso) {
		this.idTraspaso = idTraspaso;
	}

	public AlmacenMovimientosExt getIdSalida() {
		return idSalida;
	}

	public void setIdSalida(AlmacenMovimientosExt idSalida) {
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