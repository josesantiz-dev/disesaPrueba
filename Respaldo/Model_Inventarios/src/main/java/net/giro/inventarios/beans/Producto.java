package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String clave;
	private String descripcion;
	private long unidadMedida;
	private long especialidad;
	private long familia;
	private long subfamilia;
	private double precioCompra;
	private double precioVenta;
	private double precioUnitario;
	private double precioCompraPesos;
	private long idMoneda;
	private double tipoCambio;
	private double existencia;
	private int maximo;
	private int minimo;
	private int permiteExcedente;
	private String origenCodigo;
	private int estatus; // 0:Activo, 1:Eliminado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private String descEspecialidad;
	private String descFamilia;
	private String descSubfamilia;
	private String descUnidadMedida;
	private String descMoneda;
	private String descMonedaAbreviatura;
	private int tipoInsumo; // 1:Material, 2:Mano de Obra, 3:Herramienta, 4:Otros
	private int tipo; // 1:Inventarios, 2:Egresos de Operacion
	private int oculto; // 0: Normal, 1:Oculto
	private String claveSat;
	
	
	public Producto() {
		this.clave = "";
		this.claveSat = "";
		this.descripcion = "";
		this.origenCodigo = "";
		this.descEspecialidad = "";
		this.descFamilia = "";
		this.descSubfamilia = "";
		this.descUnidadMedida = "";
		this.descMoneda = "";
		this.descMonedaAbreviatura = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public Producto(Long id) {
		this();
		this.id = id;
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

	/**
	 * 0:Activo, 1:Eliminado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * 0:Activo, 1:Eliminado
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public long getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(long especialidad) {
		this.especialidad = especialidad;
	}

	public long getFamilia() {
		return familia;
	}

	public void setFamilia(long familia) {
		this.familia = familia;
	}

	public long getSubfamilia() {
		return subfamilia;
	}

	public void setSubfamilia(long subfamilia) {
		this.subfamilia = subfamilia;
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

	public String getOrigenCodigo() {
		return origenCodigo;
	}

	public void setOrigenCodigo(String origenCodigo) {
		this.origenCodigo = origenCodigo;
	}

	public long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public double getPrecioCompraPesos() {
		return precioCompraPesos;
	}

	public void setPrecioCompraPesos(double precioCompraPesos) {
		this.precioCompraPesos = precioCompraPesos;
	}

	public String getDescFamilia() {
		return descFamilia;
	}

	public void setDescFamilia(String descFamilia) {
		this.descFamilia = descFamilia;
	}

	public String getDescUnidadMedida() {
		return descUnidadMedida;
	}

	public void setDescUnidadMedida(String descUnidadMedida) {
		this.descUnidadMedida = descUnidadMedida;
	}

	public String getDescMoneda() {
		return descMoneda;
	}

	public void setDescMoneda(String descMoneda) {
		this.descMoneda = descMoneda;
	}

	public String getDescMonedaAbreviatura() {
		return descMonedaAbreviatura;
	}

	public void setDescMonedaAbreviatura(String descMonedaAbreviatura) {
		this.descMonedaAbreviatura = descMonedaAbreviatura;
	}

	/**
	 * TIPO INSUMO - 1:Material, 2:Mano de Obra, 3:Herramienta, 4:Otros
	 * @return
	 */
	public int getTipoInsumo() {
		return tipoInsumo;
	}

	/**
	 * TIPO INSUMO - 1:Material, 2:Mano de Obra, 3:Herramienta, 4:Otros
	 * @param tipoInsumo
	 */
	public void setTipoInsumo(int tipoInsumo) {
		this.tipoInsumo = tipoInsumo;
	}

	/**
	 * TIPO MAESTRO - 1:Inventarios, 2:Egresos de Operacion
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * TIPO MAESTRO - 1:Inventarios, 2:Egresos de Operacion
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getDescEspecialidad() {
		return descEspecialidad;
	}

	public void setDescEspecialidad(String descEspecialidad) {
		this.descEspecialidad = descEspecialidad;
	}

	public String getDescSubfamilia() {
		return descSubfamilia;
	}

	public void setDescSubfamilia(String descSubfamilia) {
		this.descSubfamilia = descSubfamilia;
	}

	/**
	 * OCULTO: 0 - Normal, 1 - Oculto
	 * @return
	 */
	public int getOculto() {
		return oculto;
	}

	/**
	 * OCULTO: 0 - Normal, 1 - Oculto
	 * @param oculto
	 */
	public void setOculto(int oculto) {
		this.oculto = oculto;
	}

	public String getClaveSat() {
		return claveSat;
	}

	public void setClaveSat(String claveSat) {
		this.claveSat = claveSat;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-07-06 | Javier Tirado 	| Añado propiedades idMoneda, precioCompraPesos, tipoCambio, tipoInsumo y tipo
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */