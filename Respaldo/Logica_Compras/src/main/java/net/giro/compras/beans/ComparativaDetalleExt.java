package net.giro.compras.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.clientes.beans.PersonaExt;
import net.giro.inventarios.beans.ProductoExt;

public class ComparativaDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private ComparativaExt idComparativa;
	private PersonaExt idProveedor;
	private ProductoExt idProducto;
	private int cantidad;
	private BigDecimal precioUnitario;
	private BigDecimal importe;
	private BigDecimal subtotal;
	private BigDecimal impuesto;
	private BigDecimal total;
	private BigDecimal flete;
	private BigDecimal margen;
	private int tiempoEntrega;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	
	public ComparativaDetalleExt() {
		this.precioUnitario = BigDecimal.ZERO;
		this.importe = BigDecimal.ZERO;
		this.subtotal = BigDecimal.ZERO;
		this.impuesto = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.flete = BigDecimal.ZERO;
		this.margen = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ComparativaDetalleExt(Long id) {
		this.id = id;
		this.precioUnitario = BigDecimal.ZERO;
		this.importe = BigDecimal.ZERO;
		this.subtotal = BigDecimal.ZERO;
		this.impuesto = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.flete = BigDecimal.ZERO;
		this.margen = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ComparativaDetalleExt(Long id, ComparativaExt idComparativa, PersonaExt idProveedor,
			ProductoExt idProducto, int cantidad, BigDecimal precioUnitario,
			BigDecimal importe, BigDecimal subtotal, BigDecimal impuesto,
			BigDecimal total, BigDecimal flete, BigDecimal margen, int tiempoEntrega, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.idComparativa = idComparativa;
		this.idProveedor = idProveedor;
		this.idProducto = idProducto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.importe = importe;
		this.subtotal = subtotal;
		this.impuesto = impuesto;
		this.total = total;
		this.flete = flete;
		this.margen = margen;
		this.tiempoEntrega = tiempoEntrega;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ComparativaExt getIdComparativa() {
		return idComparativa;
	}

	public void setIdComparativa(ComparativaExt idComparativa) {
		this.idComparativa = idComparativa;
	}

	public PersonaExt getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(PersonaExt idProveedor) {
		this.idProveedor = idProveedor;
	}

	public ProductoExt getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(ProductoExt idProducto) {
		this.idProducto = idProducto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(BigDecimal impuesto) {
		this.impuesto = impuesto;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getFlete() {
		return flete;
	}

	public void setFlete(BigDecimal flete) {
		this.flete = flete;
	}

	public BigDecimal getMargen() {
		return margen;
	}

	public void setMargen(BigDecimal margen) {
		this.margen = margen;
	}

	public int getTiempoEntrega() {
		return tiempoEntrega;
	}

	public void setTiempoEntrega(int tiempoEntrega) {
		this.tiempoEntrega = tiempoEntrega;
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

	
	
	public String getProveedor() {
		if (this.idProveedor != null && this.idProveedor.getId() > 0L && this.idProveedor.getNombre() != null)
			return this.idProveedor.getId() + " - " + this.idProveedor.getNombre();
		return "";
	}
	
	public void setProveedor(String value) {}
	
	public String getProveedorNombre() {
		if (this.idProveedor != null && this.idProveedor.getId() > 0L && this.idProveedor.getNombre() != null)
			return this.idProveedor.getNombre();
		return "";
	}
	
	public void setProveedorNombre(String value) {}
	
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
	
	
	public ComparativaDetalleExt getCopia() {
		return this.Copia();
	}
	
	public ComparativaDetalleExt Copia() {
		ComparativaDetalleExt dest = new ComparativaDetalleExt();
		
		try {
			dest.setId(this.id);
			dest.setIdComparativa(this.idComparativa);
			dest.setIdProveedor(this.idProveedor);
			dest.setIdProducto(this.idProducto);
			dest.setCantidad(this.cantidad);
			dest.setPrecioUnitario(this.precioUnitario);
			dest.setImporte(this.importe);
			dest.setSubtotal(this.subtotal);
			dest.setImpuesto(this.impuesto);
			dest.setTotal(this.total);
			dest.setFlete(this.flete);
			dest.setMargen(this.margen);
			dest.setTiempoEntrega(this.tiempoEntrega);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
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