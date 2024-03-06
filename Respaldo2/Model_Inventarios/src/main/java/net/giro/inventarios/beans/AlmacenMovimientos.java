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
	private long idAlmacen;
	private long recibe;
	private String recibeNombre;
	private long entrega;
	private String entregaNombre;
	private long idObra;
	private String nombreObra;
	private long idOrdenCompra;
	private String folioOrdenCompra;
	private long idTraspaso;
	private long idSalida;
	private int estatus; // 0:Activo, 1:Eliminado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int sistema; // 0:Normal, 1:Automatico
	
	
	public AlmacenMovimientos() {
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
	 * OC: Orden de Compra, DE: Devolucion, TR: Traspaso, OB: Obra, SO: Sobrante
	 * @return
	 */
	public String getTipoEntrada() {
		return tipoEntrada;
	}

	/**
	 * OC: Orden de Compra, DE: Devolucion, TR: Traspaso, OB: Obra, SO: Sobrante
	 * @param tipoEntrada
	 */
	public void setTipoEntrada(String tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}

	public String getFolioFactura() {
		return folioFactura;
	}

	public void setFolioFactura(String folioFactura) {
		this.folioFactura = folioFactura;
	}

	public long getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(long idAlmacen) {
		this.idAlmacen = idAlmacen;
	}

	public long getRecibe() {
		return recibe;
	}

	public void setRecibe(long recibe) {
		this.recibe = recibe;
	}

	public long getEntrega() {
		return entrega;
	}

	public void setEntrega(long entrega) {
		this.entrega = entrega;
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
		this.nombreObra = nombreObra;
	}

	public long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public long getIdTraspaso() {
		return idTraspaso;
	}

	public void setIdTraspaso(long idTraspaso) {
		this.idTraspaso = idTraspaso;
	}

	public long getIdSalida() {
		return idSalida;
	}

	public void setIdSalida(long idSalida) {
		this.idSalida = idSalida;
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

	public int getSistema() {
		return sistema;
	}

	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	public String getRecibeNombre() {
		return recibeNombre;
	}

	public void setRecibeNombre(String recibeNombre) {
		this.recibeNombre = recibeNombre;
	}

	public String getEntregaNombre() {
		return entregaNombre;
	}

	public void setEntregaNombre(String entregaNombre) {
		this.entregaNombre = entregaNombre;
	}

	public String getFolioOrdenCompra() {
		return folioOrdenCompra;
	}

	public void setFolioOrdenCompra(String folioOrdenCompra) {
		this.folioOrdenCompra = folioOrdenCompra;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */