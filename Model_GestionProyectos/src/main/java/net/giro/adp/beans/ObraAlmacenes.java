package net.giro.adp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * obra_almacenes
 * @author javaz
 *
 */
public class ObraAlmacenes implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Obra idObra;
	private String nombreObra;
	private long idAlmacen;
	private String nombreAlmacen;
	private int tipo; // TIPO: 1 - Principal, 2 - Obra
	private int almacenPrincipal; // almacenPrincipal: 0 - Normal, 1 - Principal
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public ObraAlmacenes() {
		this.nombreObra = "";
		this.nombreAlmacen = "";
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

	public Obra getIdObra() {
		return idObra;
	}

	public void setIdObra(Obra idObra) {
		this.idObra = idObra;
		if (idObra != null)
			setNombreObra(idObra.getNombre());
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		nombreObra = (nombreObra != null ? nombreObra : "");
		this.nombreObra = nombreObra;
	}

	public long getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(long idAlmacen) {
		this.idAlmacen = idAlmacen;
	}

	public String getNombreAlmacen() {
		return nombreAlmacen;
	}

	public void setNombreAlmacen(String nombreAlmacen) {
		nombreAlmacen = (nombreAlmacen != null ? nombreAlmacen : "");
		this.nombreAlmacen = nombreAlmacen;
	}

	/**
	 * TIPO: 1 - Principal, 2 - Obra
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * TIPO: 1 - Principal, 2 - Obra
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	/**
	 * almacenPrincipal: 0 - Normal, 1 - Principal
	 * @return
	 */
	public int getAlmacenPrincipal() {
		return almacenPrincipal;
	}

	/**
	 * almacenPrincipal: 0 - Normal, 1 - Principal
	 * @param almacenPrincipal
	 */
	public void setAlmacenPrincipal(int almacenPrincipal) {
		this.almacenPrincipal = almacenPrincipal;
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
	
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.0 | 19/05/2016 | Javier Tirado	| Creando la clase ObraAlmacenes
 *  1.2 | 07/11/2016 | Javier Tirado	| Agrego propiedad almacenPrincipal
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */