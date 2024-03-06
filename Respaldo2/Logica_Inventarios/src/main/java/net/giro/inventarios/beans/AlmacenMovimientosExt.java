package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.rh.admon.beans.Empleado;

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
	private int estatus; // 0:Activo, 1:Eliminado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int sistema; // 0:Normal, 1:Automatico
	private ProductoExt producto;
	private double cantidad;
	
	
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
		this.tipoEntrada = tipoEntrada;
	}

	public String getFolioFactura() {
		return folioFactura;
	}

	public void setFolioFactura(String folioFactura) {
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
	}

	public Empleado getEntrega() {
		return entrega;
	}

	public void setEntrega(Empleado entrega) {
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

	public AlmacenMovimientosExt getIdSalida() {
		return idSalida;
	}

	public void setIdSalida(AlmacenMovimientosExt idSalida) {
		this.idSalida = idSalida;
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

	public ProductoExt getProducto() {
		return producto;
	}

	public void setProducto(ProductoExt producto) {
		this.producto = producto;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
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