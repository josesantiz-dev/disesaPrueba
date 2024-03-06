package net.giro.tyg.admon;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * e436f99d92
 * @author javaz
 *
 */
public class ValoresTasas implements Serializable {
	private static final long serialVersionUID = -8324236219855331826L;

	long	id;
	private Long	creadoPor;
	private Date	fechaCreacion;
	private Long	modificadoPor;
	private Date	fechaModificacion;
	private Long 		anio;
	private BigDecimal enero;
	private BigDecimal febrero;
	private BigDecimal marzo;
	private BigDecimal abril;
	private BigDecimal mayo;
	private BigDecimal junio;
	private BigDecimal julio;
	private BigDecimal agosto;
	private BigDecimal septiembre;
	private BigDecimal octubre;
	private BigDecimal noviembre;
	private BigDecimal diciembre;
	private Tasas 	tasas;
	
	
	/** constructor */
	public ValoresTasas(){}
	
	/** minimal constructor */
	public ValoresTasas(long id, Long creadoPor, Date fechaCreacion, Long modificadoPor, Date fechaModificacion,Tasas tasas) {
		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.tasas = tasas;
	}

	// Getters and Setters
	
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

	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
	}

	public BigDecimal getEnero() {
		return enero;
	}

	public void setEnero(BigDecimal enero) {
		this.enero = enero;
	}

	public BigDecimal getFebrero() {
		return febrero;
	}

	public void setFebrero(BigDecimal febrero) {
		this.febrero = febrero;
	}

	public BigDecimal getMarzo() {
		return marzo;
	}

	public void setMarzo(BigDecimal marzo) {
		this.marzo = marzo;
	}

	public BigDecimal getAbril() {
		return abril;
	}

	public void setAbril(BigDecimal abril) {
		this.abril = abril;
	}

	public BigDecimal getMayo() {
		return mayo;
	}

	public void setMayo(BigDecimal mayo) {
		this.mayo = mayo;
	}
	public BigDecimal getJunio() {
		return junio;
	}

	public void setJunio(BigDecimal junio) {
		this.junio = junio;
	}

	public BigDecimal getJulio() {
		return julio;
	}

	public void setJulio(BigDecimal julio) {
		this.julio = julio;
	}

		public BigDecimal getAgosto() {
		return agosto;
	}

	public void setAgosto(BigDecimal agosto) {
		this.agosto = agosto;
	}

	public BigDecimal getSeptiembre() {
		return septiembre;
	}

	public void setSeptiembre(BigDecimal septiembre) {
		this.septiembre = septiembre;
	}

		public BigDecimal getOctubre() {
		return octubre;
	}

	public void setOctubre(BigDecimal octubre) {
		this.octubre = octubre;
	}

	public BigDecimal getNoviembre() {
		return noviembre;
	}

	public void setNoviembre(BigDecimal noviembre) {
		this.noviembre = noviembre;
	}

	public BigDecimal getDiciembre() {
		return diciembre;
	}

	public void setDiciembre(BigDecimal diciembre) {
		this.diciembre = diciembre;
	}

	public Tasas getTasas() {
		return tasas;
	}

	public void setTasas(Tasas tasas) {
		this.tasas = tasas;
	}
	
}
