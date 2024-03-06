package net.giro.compras.beans;

import java.io.Serializable;

import net.giro.inventarios.beans.ProductoExt;

public class SolicitudBodegaProducto implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idAlmacen;
	private long idProducto;
	private String clave;
	private String descripcion;
	private long idFamilia;
	private String familia;
	private long idUnidadMedida;
	private String unidadMedida;
	private double requeridos;
	private double disponible;
	private double solicitud;
	private boolean actualizado;
	// -------------------------
	private ProductoExt pojoProducto;
	private long idOrigen;
	private int origen;
	private double suministrados;
	private double precioUnitario;
	
	public SolicitudBodegaProducto() {
		this.idAlmacen = 0L;
		this.idProducto = 0L;
		this.clave = "";
		this.descripcion = "";
		this.idFamilia= 0L;
		this.familia = "";
		this.idUnidadMedida = 0L;
		this.unidadMedida = "";
		this.requeridos = 0L;
		this.disponible = 0L;
		this.solicitud = 0L;
		this.actualizado = false;
		this.pojoProducto = new ProductoExt();
		this.idOrigen = 0L;
		this.origen = 0;
		this.suministrados = 0;
		this.precioUnitario = 0;
	}

	
	public long getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(long idAlmacen) {
		this.idAlmacen = idAlmacen;
	}

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public long getIdFamilia() {
		return idFamilia;
	}

	public void setIdFamilia(long idFamilia) {
		this.idFamilia = idFamilia;
	}

	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public long getIdUnidadMedida() {
		return idUnidadMedida;
	}

	public void setIdUnidadMedida(long idUnidadMedida) {
		this.idUnidadMedida = idUnidadMedida;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public double getRequeridos() {
		return requeridos;
	}

	public void setRequeridos(double requeridos) {
		this.requeridos = requeridos;
	}

	public double getDisponible() {
		return disponible;
	}

	public void setDisponible(double disponible) {
		this.disponible = disponible;
	}

	public double getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(double solicitud) {
		this.solicitud = solicitud;
	}

	public boolean isActualizado() {
		return actualizado;
	}

	public void setActualizado(boolean actualizado) {
		this.actualizado = actualizado;
	}

	// ---------------------------------------------------------------
	
	public ProductoExt getPojoProducto() {
		return pojoProducto;
	}

	public void setPojoProducto(ProductoExt pojoProducto) {
		this.pojoProducto = pojoProducto;
	}

	public long getIdOrigen() {
		return idOrigen;
	}

	public void setIdOrigen(long idOrigen) {
		this.idOrigen = idOrigen;
	}

	public int getOrigen() {
		return origen;
	}

	public void setOrigen(int origen) {
		this.origen = origen;
	}

	public double getSuministrados() {
		return suministrados;
	}

	public void setSuministrados(double suministrados) {
		this.suministrados = suministrados;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	// ---------------------------------------------------------------
	// METODOS
	// ---------------------------------------------------------------
	
	public void calcular() {
		if (this.requeridos > this.disponible)
			this.solicitud = this.disponible;
		else
			this.solicitud = this.disponible - this.requeridos;
	}
	
	public PreCotizacion getPreCotizacion() {
		PreCotizacion preCotizacion = null;
		
		try {
			preCotizacion = new PreCotizacion();
			preCotizacion.setId(0L);
			preCotizacion.setIdOrigen(this.idOrigen);
			preCotizacion.setOrigen(this.origen);
			preCotizacion.setIdProducto(this.pojoProducto);
			preCotizacion.setRequeridos(this.requeridos); 	 
			preCotizacion.setSuministrado(this.suministrados); 
			preCotizacion.setExistencias(this.disponible);  
			preCotizacion.setCantidad(this.solicitud); 	 
			preCotizacion.setPrecioUnitario(this.precioUnitario);
		} catch (Exception e) {
			preCotizacion = null;
		}
		
		return preCotizacion;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SolicitudBodegaProducto) {
			if (this.idAlmacen != ((SolicitudBodegaProducto) o).getIdAlmacen())
				return false;
			if (this.idProducto != ((SolicitudBodegaProducto) o).getIdProducto())
				return false;
			if (this.requeridos != ((SolicitudBodegaProducto) o).getRequeridos())
				return false;
			if (this.disponible != ((SolicitudBodegaProducto) o).getDisponible())
				return false;
			if (this.solicitud != ((SolicitudBodegaProducto) o).getSolicitud())
				return false;
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int valObject = 0;
		int hash = 7; // factor hash
		
		valObject = (int) (this.idAlmacen + this.idProducto + this.solicitud);
		hash = 97 * hash + valObject;
		
		return hash;
	}
}
