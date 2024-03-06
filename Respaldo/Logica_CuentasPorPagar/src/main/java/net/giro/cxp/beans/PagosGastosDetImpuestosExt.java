package net.giro.cxp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;

import net.giro.plataforma.beans.ConValores;

public class PagosGastosDetImpuestosExt implements Serializable {
	private static final long serialVersionUID = -1640753517898900332L;
	
	private Long id;
	private PagosGastosDetExt idPagosGastosDet;
	private ConValores idImpuesto;
	private BigDecimal valor;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;


	public PagosGastosDetImpuestosExt() {
		this.valor = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public PagosGastosDetImpuestosExt(Long id) {
		this();
		this.id = id;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PagosGastosDetExt getIdPagosGastosDet() {
		return idPagosGastosDet;
	}

	public void setIdPagosGastosDet(PagosGastosDetExt idPagosGastosDet) {
		this.idPagosGastosDet = idPagosGastosDet;
	}

	public ConValores getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(ConValores idImpuesto) {
		this.idImpuesto = idImpuesto;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
	
	@Override
	public Object clone(){  
	    try{
	    	PagosGastosDetImpuestosExt dest = new PagosGastosDetImpuestosExt(); 
	    	BeanUtils.copyProperties(dest, this); 
	        return dest;
	    }catch(Exception e){ 
	        return null; 
	    }
	}
}
