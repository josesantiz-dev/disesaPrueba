package net.giro.tyg.admon;

import java.util.Date;
import javax.persistence.Column;

/**
 * fae7c9d463
 * @author javaz
 *
 */
public class GastoDeCobranza implements java.io.Serializable {
	private static final long serialVersionUID = -2078421507654547586L;
	// Fields
	private long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private String nombre;
	private String cuentaContable;
	private Long costo;
	// Constructors

	/** default constructor */
	public GastoDeCobranza() {
	}

	/** minimal constructor */
	public GastoDeCobranza(Long id, String descripcion) {
		this.id = id;
	}

public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

public Long getCreadoPor() {
		return this.creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Column(name = "ad", unique = false, nullable = true, insertable = true, updatable = true)
	public Long getModificadoPor() {
		return this.modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}
	public Long getCosto() {
		return costo;
	}

	public void setCosto(Long costo) {
		this.costo = costo;
	}
}