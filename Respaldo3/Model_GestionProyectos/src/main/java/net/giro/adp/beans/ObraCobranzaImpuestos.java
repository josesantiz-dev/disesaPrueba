package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * obra_cobranza_impuestos
 * @author javaz
 */
public class ObraCobranzaImpuestos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private ObraCobranza idObraCobranza;
	private long idImpuesto;
	private String nombreImpuesto;
	private double tasaImpuesto; // Porcentaje impuesto
	private BigDecimal baseImpuesto; // Monto base para calculo de importe del impuesto
	private BigDecimal importe; // Importe impuesto
	private int retencion; // Indica si es retencion: 0-No, 1-Si
	private int excluir; // Excluir impuesto: 0-No, 1-Si
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public ObraCobranzaImpuestos() {
		this.nombreImpuesto = "";
		this.baseImpuesto = BigDecimal.ZERO;
		this.importe = BigDecimal.ZERO;
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
	
	public ObraCobranza getIdObraCobranza() {
		return idObraCobranza;
	}
	
	public void setIdObraCobranza(ObraCobranza idObraCobranza) {
		this.idObraCobranza = idObraCobranza;
	}
	
	public long getIdImpuesto() {
		return idImpuesto;
	}
	
	public void setIdImpuesto(long idImpuesto) {
		this.idImpuesto = idImpuesto;
	}
	
	public String getNombreImpuesto() {
		return nombreImpuesto;
	}
	
	public void setNombreImpuesto(String nombreImpuesto) {
		this.nombreImpuesto = nombreImpuesto;
	}
	
	/**
	 * Porcentaje impuesto
	 * @return
	 */
	public double getTasaImpuesto() {
		return tasaImpuesto;
	}
	
	/**
	 * Porcentaje impuesto
	 * @param tasaImpuesto
	 */
	public void setTasaImpuesto(double tasaImpuesto) {
		tasaImpuesto = (tasaImpuesto > 0 ? tasaImpuesto : 0);
		this.tasaImpuesto = tasaImpuesto;
	}
	
	/**
	 * Monto base para calculo de importe del impuesto
	 * @return
	 */
	public BigDecimal getBaseImpuesto() {
		return baseImpuesto;
	}
	
	/**
	 * Monto base para calculo de importe del impuesto
	 * @param baseImpuesto
	 */
	public void setBaseImpuesto(BigDecimal baseImpuesto) {
		baseImpuesto = (baseImpuesto != null ? baseImpuesto : BigDecimal.ZERO);
		this.baseImpuesto = baseImpuesto;
	}
	
	/**
	 * Importe impuesto
	 * @return
	 */
	public BigDecimal getImporte() {
		return importe;
	}
	
	/**
	 * Importe impuesto
	 * @param importe
	 */
	public void setImporte(BigDecimal importe) {
		importe = (importe != null ? importe : BigDecimal.ZERO);
		this.importe = importe;
	}

	/**
	 * Indica si es retencion: 0-No, 1-Si
	 * @return
	 */
	public int getRetencion() {
		return retencion;
	}

	/**
	 * Indica si es retencion: 0-No, 1-Si
	 * @param retencion
	 */
	public void setRetencion(int retencion) {
		this.retencion = retencion;
	}

	/**
	 * Excluir impuesto: 0-No, 1-Si
	 * @return
	 */
	public int getExcluir() {
		return excluir;
	}

	/**
	 * Excluir impuesto: 0-No, 1-Si
	 * @param excluir
	 */
	public void setExcluir(int excluir) {
		this.excluir = excluir;
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
