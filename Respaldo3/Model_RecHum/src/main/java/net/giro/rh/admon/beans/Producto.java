package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String clave;
	private String descripcion;
	private double precioVenta;
	private double precioCompra;
	private int maximo;
	private int minimo;
	private double existencia;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int estatus;
	private long familia;
	private long unidadMedida;
	private double precioUnitario;
	private int permiteExcedente;
	
	public Producto() {}
	
	public Producto(Long id, String clave, String descripcion, double precioVenta, double precioCompra, int maximo, int minimo,
			double existencia, long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion,
			int estatus, long familia, long unidadMedida, double precioUnitario, int permiteExcedente) {
		
		this.id = id;
		this.descripcion = descripcion;
		this.precioVenta = precioVenta;
		this.precioCompra = precioCompra;
		this.maximo = maximo;
		this.minimo = minimo;
		this.existencia = existencia;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.estatus = estatus;
		this.familia = familia;
		this.unidadMedida = unidadMedida;
		this.clave = clave;
		this.precioUnitario = precioUnitario;
		this.permiteExcedente = permiteExcedente;
	}



	public Long getId() {
		return id;
	}

	public void setId(long id) {	//meotodo necesatio para tomar el correlativo
		this.id = id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public int getMaximo() {
		return maximo;
	}

	public void setMaximo(int maximo) {
		this.maximo = maximo;
	}

	public int getMinimo() {
		return minimo;
	}

	public void setMinimo(int minimo) {
		this.minimo = minimo;
	}

	public double getExistencia() {
		return existencia;
	}

	public void setExistencia(double existencia) {
		this.existencia = existencia;
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

	public long getFamilia() {
		return familia;
	}

	public void setFamilia(long familia) {
		this.familia = familia;
	}

	public long getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(long unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public int getPermiteExcedente() {
		return permiteExcedente;
	}

	public void setPermiteExcedente(int permiteExcedente) {
		this.permiteExcedente = permiteExcedente;
	}
}


/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */