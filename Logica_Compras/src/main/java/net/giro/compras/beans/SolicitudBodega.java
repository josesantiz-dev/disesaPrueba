package net.giro.compras.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class SolicitudBodega implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idAlmacen;
	private String almacen;
	private String identificador;
	private long idSolicitud;
	private int estatus; // ESTATUS: 0-pendiente, 1-cancelada, 2-enviada, 3-recibida
	private List<SolicitudBodegaProducto> listProductos;
	private boolean actualizada;
	
	public SolicitudBodega() {
		this.idAlmacen = 0L;
		this.almacen = "";
		this.identificador = "";
		this.idSolicitud = 0L;
		this.estatus = 0;
		this.listProductos = new ArrayList<SolicitudBodegaProducto>();
		this.actualizada = false;
	}
	
	public long getIdAlmacen() {
		return idAlmacen;
	}
	
	public void setIdAlmacen(long idAlmacen) {
		this.idAlmacen = idAlmacen;
	}
	
	public String getAlmacen() {
		return almacen;
	}
	
	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}
	
	public String getIdentificador() {
		return identificador;
	}
	
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	public long getIdSolicitud() {
		return idSolicitud;
	}
	
	public void setIdSolicitud(long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	
	/**
	 * ESTATUS: 0-pendiente, 1-cancelada, 2-enviada, 3-recibida
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}
	
	/**
	 * ESTATUS: 0-pendiente, 1-cancelada, 2-enviada, 3-recibida
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	
	public List<SolicitudBodegaProducto> getListProductos() {
		return listProductos;
	}
	
	public void setListProductos(List<SolicitudBodegaProducto> listProductos) {
		this.listProductos = listProductos;
	}

	public boolean isActualizada() {
		return actualizada;
	}

	public void setActualizada(boolean actualizada) {
		this.actualizada = actualizada;
	}

	// --------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------
	
	public String getDescripcion() {
		if (this.idSolicitud > 0L && this.almacen != null && ! "".equals(this.almacen.trim()) && this.identificador != null && ! "".equals(this.identificador.trim()))
			return this.identificador + " | " + this.almacen + " | SBO: " + this.idSolicitud;
		if (this.almacen != null && ! "".equals(this.almacen.trim()) && this.identificador != null && ! "".equals(this.identificador.trim()))
			return this.identificador + " | " + this.almacen;
		return "";
	}
	
	public void setDescripcion(String value) {}
	
 	public List<Long> getIdsProductos() {
		List<Long> productos = new ArrayList<Long>();
		
		if (this.listProductos == null || this.listProductos.isEmpty())
			return productos;
		for (SolicitudBodegaProducto producto : this.listProductos)
			productos.add(producto.getIdProducto());
		return productos;
	}
	
	public HashMap<Long, Integer> getMapaProductos() {
		HashMap<Long, Integer> productos = new HashMap<Long, Integer>();
		
		if (this.listProductos == null || this.listProductos.isEmpty())
			return productos;
		for (SolicitudBodegaProducto producto : this.listProductos)
			productos.put(producto.getIdProducto(), this.listProductos.indexOf(producto));
		return productos;
	}
	
	/**
	 * Metodo para comparar la lista de productos con la lista interna del objeto
	 * @param items Lista de productos externa
	 * @return
	 */
	public void actualizarProductos(List<SolicitudBodegaProducto> listProductosReferencia) {
		HashMap<Long, Integer> refInterna = new HashMap<Long, Integer>();
		HashMap<Long, Integer> refExterna = new HashMap<Long, Integer>();
		SolicitudBodegaProducto item = null;
		boolean resultado = false;

		// Generamos mapa de productos del listado interno
		for (SolicitudBodegaProducto producto : this.listProductos)
			refInterna.put(producto.getIdProducto(), this.listProductos.indexOf(producto));

		// Generamos mapa de productos del listado externo
		for (SolicitudBodegaProducto externo : listProductosReferencia)
			refExterna.put(externo.getIdProducto(), listProductosReferencia.indexOf(externo));
		
		// Iteramos sobre el mapa de productos internos y modificamos la lista de productos si corresponde
		for (Entry<Long, Integer> interno : refInterna.entrySet()) {
			if (! refExterna.containsKey(interno.getKey())) {
				this.listProductos.remove(interno.getValue().intValue());
				resultado = true;
				continue;
			}

			if (this.listProductos.get(interno.getValue()).getDisponible() != listProductosReferencia.get(interno.getValue()).getDisponible()) {
				item = this.listProductos.get(interno.getValue());
				item.setDisponible(listProductosReferencia.get(interno.getValue()).getDisponible());
				item.setActualizado(true);
				this.listProductos.set(interno.getValue(), item);
				resultado = true;
			}
		}

		// Reconstruimos el mapa de productos del listado interno, solo si fue modificada previamente
		if (resultado) {
			refInterna.clear();
			for (SolicitudBodegaProducto producto : this.listProductos)
				refInterna.put(producto.getIdProducto(), this.listProductos.indexOf(producto));
		}

		// Iteramos sobre el mapa de productos externos y añadimos los que no esten en la lista de productos
		for (Entry<Long, Integer> externo : refExterna.entrySet()) {
			if (! refInterna.containsKey(externo.getKey())) {
				this.listProductos.add(listProductosReferencia.get(externo.getValue()));
				resultado = true;
			}
		}
		
		this.actualizada = resultado;
	}

	public List<SolicitudBodegaProducto> getProductosActualizados() {
		List<SolicitudBodegaProducto> actualizados = new ArrayList<SolicitudBodegaProducto>();
		
		for (SolicitudBodegaProducto producto : this.listProductos) {
			if (producto.isActualizado())
				actualizados.add(producto);
		}
		
		return actualizados;
	}
	
	public int getProductosActualizadosCount() {
		return getProductosActualizados().size();
	}

	public void ordenarProductos() {
		if (this.listProductos == null || this.listProductos.isEmpty())
			return;
		Collections.sort(this.listProductos, new Comparator<SolicitudBodegaProducto>() {
			@Override
			public int compare(SolicitudBodegaProducto o1, SolicitudBodegaProducto o2) {
				return o1.getClave().compareTo(o2.getClave());
			}
		});
	}

	// ---------------------------------------------------------------
	// METODOS
	// ---------------------------------------------------------------
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SolicitudBodega) {
			if (this.idAlmacen != ((SolicitudBodega) o).getIdAlmacen())
				return false;
			if (this.listProductos != ((SolicitudBodega) o).getListProductos())
				return false;
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int valObject = 0;
		int hash = 7; // factor hash
		
		if (this.listProductos == null)
			return 0;
		
		@SuppressWarnings("rawtypes")
		Iterator i = this.listProductos.iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			valObject = 31 * valObject + (obj == null ? 0 : obj.hashCode());
		}
		
		valObject = (int) (this.idAlmacen + valObject);
		hash = 97 * hash + valObject;
		
		return hash;
	}
}
