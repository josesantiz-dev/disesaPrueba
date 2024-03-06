package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.adp.beans.ObraExt;
import net.giro.clientes.beans.PersonaExt;
import net.giro.rh.admon.beans.EmpleadoExt;

public class CotizacionExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private ObraExt idObra;
	private String nombreObra;
	private RequisicionExt idRequisicion;
	private String folio;
	private Date fecha;
	private double flete;
	private double margen;
	private int tiempoEntrega;
	private double porcentajeIva;
	private double subtotal;
	private double iva;
	private double total;
	private PersonaExt idProveedor;
	private String nombreProveedor;
	private String rfcProveedor;
	private String tipoPersonaProveedor;
	private int consecutivoProveedor;
	private PersonaExt idContacto;
	private String nombreContacto;
	private EmpleadoExt idComprador;
	private String nombreComprador;
	private int tipo;
	private long idMoneda;
	private String moneda;
	private double tipoCambio;
	private long idEmpresa;
	//private int usoParcial;
	private int sistema;
	private int cerrada; // // 0 - No, 1 - SI
	private int estatus; // 0: Activo, 1:Cancelada, 2:Suministrada
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public CotizacionExt() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.folio = "";
		this.tipoPersonaProveedor = "";
		this.nombreObra = "";
		this.nombreProveedor = "";
		this.nombreContacto = "";
		this.nombreComprador = "";
		this.moneda = "";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ObraExt getIdObra() {
		return idObra;
	}

	public void setIdObra(ObraExt idObra) {
		this.idObra = idObra;
		this.nombreObra = (idObra == null) ? "" : idObra.getNombre();
	}

	public PersonaExt getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(PersonaExt idProveedor) {
		this.idProveedor = idProveedor;
		this.nombreProveedor = (idProveedor == null) ? "" : idProveedor.getNombre();
		this.rfcProveedor = (idProveedor == null) ? "" : idProveedor.getRfc();
	}

	public PersonaExt getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(PersonaExt idContacto) {
		this.idContacto = idContacto;
		this.nombreContacto = (idContacto == null) ? "" : idContacto.getNombre();
	}

	public EmpleadoExt getIdComprador() {
		return idComprador;
	}

	public void setIdComprador(EmpleadoExt idComprador) {
		this.idComprador = idComprador;
		this.nombreComprador = (idComprador == null) ? "" : idComprador.getNombre();
	}

	public RequisicionExt getIdRequisicion() {
		return idRequisicion;
	}

	public void setIdRequisicion(RequisicionExt idRequisicion) {
		this.idRequisicion = idRequisicion;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getFlete() {
		return flete;
	}

	public void setFlete(double flete) {
		this.flete = flete;
	}

	public double getMargen() {
		return margen;
	}

	public void setMargen(double margen) {
		this.margen = margen;
	}

	public int getTiempoEntrega() {
		return tiempoEntrega;
	}

	public void setTiempoEntrega(int tiempoEntrega) {
		this.tiempoEntrega = tiempoEntrega;
	}

	public double getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(double porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getConsecutivoProveedor() {
		return consecutivoProveedor;
	}

	public void setConsecutivoProveedor(int consecutivoProveedor) {
		this.consecutivoProveedor = consecutivoProveedor;
	}

	/**
	 * P: Persona, N: Negocio
	 * @return
	 */
	public String getTipoPersonaProveedor() {
		return tipoPersonaProveedor;
	}

	/**
	 * P: Persona, N: Negocio
	 * @param tipoPersonaProveedor
	 */
	public void setTipoPersonaProveedor(String tipoPersonaProveedor) {
		this.tipoPersonaProveedor = tipoPersonaProveedor;
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public String getRfcProveedor() {
		return rfcProveedor;
	}

	public void setRfcProveedor(String rfcProveedor) {
		this.rfcProveedor = rfcProveedor;
	}

	public String getNombreContacto() {
		return nombreContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public String getNombreComprador() {
		return nombreComprador;
	}

	public void setNombreComprador(String nombreComprador) {
		this.nombreComprador = nombreComprador;
	}

	/**
	 * Tipo: 1 - Inventario 2 - Concepto de Operacion
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * Tipo: 1 - Inventario 2 - Concepto de Operacion
	 * @return
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}


	/**
	 * SISTEMA : 0 - No, 1 - Si
	 * @return
	 */
	public int getSistema() {
		return sistema;
	}

	/**
	 * SISTEMA : 0 - No, 1 - Si
	 * @param sistema
	 */
	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	/**
	 * CERRADA: 0 - Activa/Abierta, 1 - Cerrada
	 * @return
	 */
	public int getCerrada() {
		return cerrada;
	}

	/**
	 * CERRADA: 0 - Activa/Abierta, 1 - Cerrada
	 * @param usoParcial
	 */
	public void setCerrada(int cerrada) {
		this.cerrada = cerrada;
	}

	/**
	 * 0: Activo, 1:Eliminado, 2:Suministrada
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * 0: Activo, 1:Eliminado, 2:Suministrada
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
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

	// -----------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------
	
	public String getObra() {
		if (this.idObra != null && this.idObra.getId() != null && this.idObra.getId() > 0L && this.idObra.getNombre() != null)
			return this.idObra.getId() + " - " + this.idObra.getNombre();
		return "";
	}
	
	public void setObra(String value) {}
	
	public String getObraNombre() {
		if (this.idObra != null && this.idObra.getId() != null && this.idObra.getId() > 0L && this.idObra.getNombre() != null)
			return this.idObra.getNombre();
		return "";
	}
	
	public void setObraNombre(String value) {}
	
	public String getProveedor() {
		if (this.idProveedor != null && this.idProveedor.getId() > 0L && this.idProveedor.getNombre() != null)
			return this.idProveedor.getId() + " - " + this.idProveedor.getNombre();
		return "";
	}
	
	public void setProveedor(String value) {}
	
	public String getProveedorNombre() {
		if (this.idProveedor != null && this.idProveedor.getId() > 0L && this.idProveedor.getNombre() != null)
			return this.idProveedor.getNombre();
		return "";
	}
	
	public void setProveedorNombre(String value) {}
	
	public String getComprador() {
		if (this.idComprador != null && this.idComprador.getId() > 0L && this.idComprador.getNombre() != null)
			return this.idComprador.getId() + " - " + this.idComprador.getNombre();
		return "";
	}
	
	public void setComprador(String value) {}
	
	public String getCompradorNombre() {
		if (this.idComprador != null && this.idComprador.getId() > 0L && this.idComprador.getNombre() != null)
			return this.idComprador.getNombre();
		return "";
	}
	
	public void setCompradorNombre(String value) {}
	
	public String getContacto() {
		if (this.idContacto != null && this.idContacto.getId() > 0L && this.idContacto.getNombre() != null)
			return this.idContacto.getId() + " - " + this.idContacto.getNombre();
		return "";
	}
	
	public void setContacto(String value) {}
	
	public String getContactoNombre() {
		if (this.idContacto != null && this.idContacto.getId() > 0L && this.idContacto.getNombre() != null)
			return this.idContacto.getNombre();
		return "";
	}
	
	public void setContactoNombre(String value) {}

	// -----------------------------------------------------------------------------------------
	// METODOS
	// -----------------------------------------------------------------------------------------
	
	public CotizacionExt getCopia() {
		return this.Copia();
	}
	
	public CotizacionExt Copia() {
		CotizacionExt dest = new CotizacionExt();
		
		try {
			dest.setId(this.id);
			dest.setIdObra(this.idObra);
			dest.setIdProveedor(this.idProveedor);
			dest.setTipoPersonaProveedor(this.tipoPersonaProveedor);
			dest.setConsecutivoProveedor(this.consecutivoProveedor);
			dest.setIdContacto(this.idContacto);
			dest.setIdComprador(this.idComprador);
			dest.setIdRequisicion(this.idRequisicion);
			dest.setFolio(this.folio);
			dest.setFecha(this.fecha);
			dest.setFlete(this.flete);
			dest.setMargen(this.margen);
			dest.setTiempoEntrega(this.tiempoEntrega);
			dest.setPorcentajeIva(this.porcentajeIva);
			dest.setSubtotal(this.subtotal);
			dest.setIva(this.iva);
			dest.setTotal(this.total);
			dest.setTipo(this.tipo);
			dest.setIdMoneda(this.idMoneda);
			dest.setMoneda(this.moneda);
			dest.setTipoCambio(this.tipoCambio);
			dest.setIdEmpresa(this.idEmpresa);
			dest.setSistema(this.sistema);
			dest.setCerrada(this.cerrada);
			dest.setEstatus(this.estatus);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
		} catch (Exception e) {
			throw e;
		}
		
		return dest;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */