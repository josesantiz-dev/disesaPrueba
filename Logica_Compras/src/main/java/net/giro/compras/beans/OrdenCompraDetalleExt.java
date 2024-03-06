package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.inventarios.beans.ProductoExt;

/**
 * orden_compra_detalle
 * @author javaz
 *
 */
public class OrdenCompraDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private OrdenCompraExt idOrdenCompra;
	private ProductoExt idProducto;
	private double cantidad;
	private double precioUnitario;
	private double importe;
	private double cantidadSuministrada;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
    private int estatus;
    private String comentariosCancelacion;
	
	public OrdenCompraDetalleExt() {
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

	public OrdenCompraExt getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(OrdenCompraExt idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public ProductoExt getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(ProductoExt idProducto) {
		this.idProducto = idProducto;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public double getCantidadSuministrada() {
		return cantidadSuministrada;
	}

	public void setCantidadSuministrada(double cantidad) {
		this.cantidadSuministrada = cantidad;
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
    public String getComentariosCancelacion() {
        return comentariosCancelacion;
    }

    public void setComentariosCancelacion(String comentariosCancelacion) {
        this.comentariosCancelacion = comentariosCancelacion;
    }
	// -------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------
	
	/**
	 * Cantidad pendiente por suministrar
	 * @return
	 */
	public double getPendiente() {
		return ((this.cantidad - this.cantidadSuministrada) > 0) ? (this.cantidad - this.cantidadSuministrada) : 0;
	}
	
	public void setPendiente(double cantidad) {}
	
	public void addSuministrada(double cantidad) {
		this.cantidadSuministrada += cantidad;
		this.cantidadSuministrada = (this.cantidadSuministrada > 0 ? this.cantidadSuministrada : 0);
	}

	// -------------------------------------------------------------------

	public String getProducto() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getId() > 0 && this.idProducto.getDescripcion() != null)
			return this.idProducto.getId() + " - " + this.idProducto.getDescripcion();
		return "";
	}
	
	public void setProducto(String value) {}
	
	public String getProductoNombre() {
		if (this.idProducto != null && this.idProducto.getDescripcion() != null)
			return this.idProducto.getDescripcion();
		return "";
	}
	
	public void setProductoNombre(String value) {}
	
	public double getProductoPrecioUnitario() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.idProducto.getPrecioUnitario() > 0)
			return this.idProducto.getPrecioUnitario();
		return 0;
	}
	
	public void setProductoPrecioUnitario(double precioUnitario) {}
	
	public String getProductoUnidad() {
		if (this.idProducto != null && this.idProducto.getUnidadMedida() != null && this.idProducto.getUnidadMedida().getId() > 0)
			return this.idProducto.getUnidadMedida().getDescripcion();
		return "";
	}
	
	public void setProductoUnidad(String value) {}
	
	public String getFamilia() {
		if (this.idProducto != null && this.idProducto.getFamilia() != null && this.idProducto.getFamilia().getId() > 0)
			return this.idProducto.getFamilia().getDescripcion();
		return "";
	}
	
	public void setFamilia(String value) {}
		
	public double getProductoImporte() {
		if (this.idProducto != null && this.idProducto.getId() != null && this.cantidad > 0)
			return (this.idProducto.getPrecioUnitario() * this.cantidad);
		return 0;
	}
	
	public void setProductoImporte(double value) {}

	// -------------------------------------------------------------------
	
	public OrdenCompraDetalleExt getCopia() {
		return this.Copia();
	}
	
	public OrdenCompraDetalleExt Copia() {
		OrdenCompraDetalleExt dest = null;
		
		try {
			dest = new OrdenCompraDetalleExt();
			dest.setId(this.id);
			dest.setIdOrdenCompra(this.idOrdenCompra);
			dest.setIdProducto(this.idProducto);
			dest.setCantidad(this.cantidad);
			dest.setCantidadSuministrada(this.cantidadSuministrada);
			dest.setPrecioUnitario(this.precioUnitario);
			dest.setImporte(this.importe);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			dest.setEstatus(this.estatus);
			dest.setComentariosCancelacion(this.comentariosCancelacion);
		} catch (Exception e) {
			throw e;
		}
		
		return dest;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */