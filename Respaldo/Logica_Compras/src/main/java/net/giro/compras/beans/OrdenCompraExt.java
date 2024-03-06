package net.giro.compras.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import net.giro.adp.beans.ObraExt;
import net.giro.clientes.beans.PersonaExt;
import net.giro.rh.admon.beans.EmpleadoExt;

public class OrdenCompraExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String folio;
	private Date fecha;
	private CotizacionExt idCotizacion;
	private ObraExt idObra;
	private PersonaExt idProveedor;
	private PersonaExt idContacto;
	private EmpleadoExt idSolicita;
	private Long idMoneda;
	private BigDecimal anticipo;
	private BigDecimal tipoCambio;
	private int plazo;
	private int tiempoEntrega;
	private String lugarEntrega;
	private double subtotal;
	private double iva;
	private double total;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int estatus;
	private String nombreObra;
	private String nombreProveedor;
	private String nombreSolicita;
	private String nombreContacto;
	private int consecutivoProveedor;
	private int completa;	
	private int autorizado;
	private long idUsuarioAutorizo;
	private Date fechaAutorizacion;
	private String tipoPersonaProveedor;
	private int tipo;
	
	
	public OrdenCompraExt() {
		this.anticipo = BigDecimal.ZERO;
		this.tipoCambio = BigDecimal.ZERO;
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.fechaAutorizacion = Calendar.getInstance().getTime();
		this.folio = "";
		this.lugarEntrega = "";
		this.nombreObra = "";
		this.nombreProveedor = "";
		this.nombreSolicita = "";
		this.nombreContacto = "";
		this.tipoPersonaProveedor = "";
	}
	
	public OrdenCompraExt(Long id) {
		this();
		this.id = id;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public CotizacionExt getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(CotizacionExt idCotizacion) {
		this.idCotizacion = idCotizacion;
		
		if (idCotizacion.getIdObra() != null)
			this.setIdObra(idCotizacion.getIdObra());
		if (idCotizacion.getIdProveedor() != null)
			this.setIdProveedor(idCotizacion.getIdProveedor());
		if (idCotizacion.getIdContacto() != null)
			this.setIdContacto(idCotizacion.getIdContacto());
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
		
		if (idProveedor.getRfc().trim().length() == 13)
			this.tipoPersonaProveedor = "P";
		if (idProveedor.getRfc().trim().length() == 12)
			this.tipoPersonaProveedor = "N";
	}

	public EmpleadoExt getIdSolicita() {
		return idSolicita;
	}

	public void setIdSolicita(EmpleadoExt idSolicita) {
		this.idSolicita = idSolicita;
		this.nombreSolicita = (idSolicita == null) ? "" : idSolicita.getNombre();
	}

	public PersonaExt getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(PersonaExt idContacto) {
		this.idContacto = idContacto;
		this.nombreContacto = (idContacto == null) ? "" : idContacto.getNombre();
	}

	public BigDecimal getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(BigDecimal anticipo) {
		this.anticipo = anticipo;
	}

	public Long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public BigDecimal getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(BigDecimal tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public int getPlazo() {
		return plazo;
	}

	public void setPlazo(int plazo) {
		this.plazo = plazo;
	}

	public String getLugarEntrega() {
		return lugarEntrega;
	}

	public void setLugarEntrega(String lugarEntrega) {
		this.lugarEntrega = lugarEntrega;
	}

	public int getTiempoEntrega() {
		return tiempoEntrega;
	}

	public void setTiempoEntrega(int tiempoEntrega) {
		this.tiempoEntrega = tiempoEntrega;
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

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
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

	public String getNombreSolicita() {
		return nombreSolicita;
	}

	public void setNombreSolicita(String nombreSolicita) {
		this.nombreSolicita = nombreSolicita;
	}

	public String getNombreContacto() {
		return nombreContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public int getCompleta() {
		return completa;
	}

	public void setCompleta(int completa) {
		this.completa = completa;
	}

	public int getConsecutivoProveedor() {
		return consecutivoProveedor;
	}

	public void setConsecutivoProveedor(int consecutivoProveedor) {
		this.consecutivoProveedor = consecutivoProveedor;
	}

	public int getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(int autorizado) {
		this.autorizado = autorizado;
	}

	public long getIdUsuarioAutorizo() {
		return idUsuarioAutorizo;
	}

	public void setIdUsuarioAutorizo(long idUsuarioAutorizo) {
		this.idUsuarioAutorizo = idUsuarioAutorizo;
	}

	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}
	
	public boolean isAutorizar() {
		return this.autorizado==1 ? true: false;
	}

	public void setAutorizar(boolean autorizar) {
		if (autorizar) {
			this.autorizado = 1;
		} else {
			this.autorizado = 0;
		}
	}

	public String getTipoPersonaProveedor() {
		return tipoPersonaProveedor;
	}

	public void setTipoPersonaProveedor(String tipoPersonaProveedor) {
		this.tipoPersonaProveedor = tipoPersonaProveedor;
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

	// -----------------------------------------------------------------------------------------
	
	public String getCotizacion() {
		if (this.idCotizacion != null && this.idCotizacion.getId() != null && this.idCotizacion.getId() > 0L && this.idCotizacion.getFolio() != null)
			return this.idCotizacion.getFolio() + " - $ " + (new DecimalFormat("#,###,###,###.00")).format(this.idCotizacion.getTotal());
		return "";
	}
	
	public void setCotizacion(String value) {}
	
	public String getCotizacionFolio() {
		if (this.idCotizacion != null && this.idCotizacion.getId() != null && this.idCotizacion.getId() > 0L && this.idCotizacion.getFolio() != null)
			return this.idCotizacion.getFolio();
		return "";
	}
	
	public void setCotizacionFolio(String value) {}
	
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
	
	public String getSolicita() {
		if (this.idSolicita != null && this.idSolicita.getId() > 0L && this.idSolicita.getNombre() != null)
			return this.idSolicita.getId() + " - " + this.idSolicita.getNombre();
		return "";
	}
	
	public void setSolicita(String value) {}
	
	public String getSolicitaNombre() {
		if (this.idSolicita != null && this.idSolicita.getId() > 0L && this.idSolicita.getNombre() != null)
			return this.idSolicita.getNombre();
		return "";
	}
	
	public void setSolicitaNombre(String value) {}
	
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
	
	public OrdenCompraExt getCopia() {
		return this.Copia();
	}
	
	public OrdenCompraExt Copia() {
		OrdenCompraExt dest = new OrdenCompraExt();
		
		try {
			dest.setId(this.id);
			dest.setFolio(this.folio);
			dest.setFecha(this.fecha);
			dest.setIdCotizacion(this.idCotizacion);
			dest.setIdObra(this.idObra);
			dest.setIdProveedor(this.idProveedor);
			dest.setIdSolicita(this.idSolicita);
			dest.setIdContacto(this.idContacto);
			dest.setAnticipo(this.anticipo);
			dest.setIdMoneda(this.idMoneda);
			dest.setTipoCambio(this.tipoCambio);
			dest.setPlazo(this.plazo);
			dest.setLugarEntrega(this.lugarEntrega);
			dest.setTiempoEntrega(this.tiempoEntrega);
			dest.setSubtotal(this.subtotal);
			dest.setIva(this.iva);
			dest.setTotal(this.total);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			dest.setEstatus(this.estatus);
			dest.setNombreObra(this.nombreObra);
			dest.setNombreProveedor(this.nombreProveedor);
			dest.setNombreSolicita(this.nombreSolicita);
			dest.setNombreContacto(this.nombreContacto);
			dest.setConsecutivoProveedor(this.consecutivoProveedor);
			dest.setTipoPersonaProveedor(this.tipoPersonaProveedor);
			dest.setTipo(this.tipo);
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