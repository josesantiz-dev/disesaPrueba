package net.giro.tyg.admon;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ea047eb7cc
 * @author javaz
 *
 */
public class TiposTasas implements Serializable {
	private static final long serialVersionUID = 2438869862516496789L;
	
	private long	   id;
	private Long	   creadoPor;
	private Date	   fechaCreacion;
	private Long	   modificadoPor;
	private Date	   fechaModificacion;
	private String 	   clave;
	private BigDecimal valorTasa;
	private Tasas	   tasas;
	
	/** constructor */
	public TiposTasas(){}
	
	/** minimal constructor */
	public TiposTasas(long id, Long creadoPor, Date fechaCreacion, Long modificadoPor, Date fechaModificacion, String clave, BigDecimal valorTasa, Tasas tasas) {
		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.clave = clave;
		this.valorTasa = valorTasa;
		this.tasas = tasas;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public BigDecimal getValorTasa() {
		return valorTasa;
	}

	public void setValorTasa(BigDecimal valorTasa) {
		this.valorTasa = valorTasa;
	}

	public Tasas getTasas() {
		return tasas;
	}

	public void setTasas(Tasas tasas) {
		this.tasas = tasas;
	}

}
