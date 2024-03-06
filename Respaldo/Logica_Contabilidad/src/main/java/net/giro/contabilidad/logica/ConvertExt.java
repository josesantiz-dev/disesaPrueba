package net.giro.contabilidad.logica;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.contabilidad.beans.AsignacionGrupos;
import net.giro.contabilidad.beans.AsignacionGruposExt;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.beans.MensajeTransaccionExt;
import net.giro.contabilidad.beans.Operaciones;
import net.giro.contabilidad.beans.OperacionesExt;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.beans.TransaccionesExt;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.logica.FormasPagosRem;

import org.apache.log4j.Logger;

public class ConvertExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ConvertExt.class);
	private InitialContext ctx = null;
	private AdministracionRem ifzAdmon;
	private FormasPagosRem ifzFormasPagos;
	private ConGrupoValoresRem ifzGruposValores;
	private ConValoresRem ifzConValores;
	private String from;
	private boolean mostrarSystemOut;
	
	
	public ConvertExt() {
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            
    		this.ctx = new InitialContext(p);

    		this.ifzAdmon = (AdministracionRem) this.ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");
    		this.ifzFormasPagos = (FormasPagosRem) this.ctx.lookup("ejb:/Logica_TYG//FormasPagosFac!net.giro.tyg.logica.FormasPagosRem");
    		this.ifzGruposValores = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
    		this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
    		
    		this.setFrom("Default");
    		this.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear ConvertExt", e);
			this.ctx = null;
		}
	}
	
	
	public String getFrom() { 
		return from; 
	}
	
	public void setFrom(String from) { 
		this.from = from; 
	}
	
	public boolean getMostrarSystemOut() { 
		return mostrarSystemOut; 
	}
	
	public void setMostrarSystemOut(boolean mostrarSystemOut) { 
		this.mostrarSystemOut = mostrarSystemOut; 
	}
	
	public OperacionesExt OperacionesToOperacionesExt(Operaciones pojoTarget) throws Exception {
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: From " + this.from + " :: FacturaPagosExt To FacturaPagos");
		OperacionesExt pojoResult = new OperacionesExt();
		
		try {
			if (pojoTarget == null)
				return null;
						
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setDescripcion(pojoTarget.getDescripcion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando Modulo(Aplicacion)");
			if (pojoTarget.getIdModulo() != null && pojoTarget.getIdModulo() > 0L) {
				Aplicacion pojoAux = this.ifzAdmon.findAplicacionById(pojoTarget.getIdModulo());
				
				if (pojoAux == null)
					pojoAux = new Aplicacion();
				
				pojoResult.setIdModulo(pojoAux);
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando Modulo(Aplicacion) terminado");
			}
		} catch (Exception ex) {
			log.error("Error en Logica_Contabilidad.ConvertExt :: ERROR al convertir OperacionesToOperacionesExt", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: FacturaPagosExt To FacturaPagos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public Operaciones OperacionesExtToOperaciones(OperacionesExt pojoTarget) {
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: From " + this.from + " ::  OperacionesExtToOperaciones");
		Operaciones pojoResult = new Operaciones();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setDescripcion(pojoTarget.getDescripcion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdModulo(0L);
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando Modulo(Aplicacion)");
			if (pojoTarget.getIdModulo() != null && pojoTarget.getIdModulo().getId() > 0L) {
				pojoResult.setIdModulo(pojoTarget.getIdModulo().getId());
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando Modulo(Aplicacion) terminado");
			}
		} catch (Exception ex) {
			log.error("Error en Logica_Contabilidad.ConvertExt :: ERROR al convertir OperacionesExtToOperaciones", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: OperacionesExtToOperaciones :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public TransaccionesExt TransaccionesToTransaccionesExt(Transacciones pojoTarget) throws Exception {
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: From " + this.from + " :: TransaccionesExt To Transacciones");
		TransaccionesExt pojoResult = new TransaccionesExt();
		
		try {
			if (pojoTarget == null)
				return null;
						
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setDescripcion(pojoTarget.getDescripcion());
			pojoResult.setCodigo(pojoTarget.getCodigo());
			pojoResult.setGlosa(pojoTarget.getGlosa());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando Operacion");
			if (pojoTarget.getIdOperacion() != null && pojoTarget.getIdOperacion().getId() > 0L) {
				pojoResult.setIdOperacion(this.OperacionesToOperacionesExt(pojoTarget.getIdOperacion()));
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando Operacion terminado");
			}
		} catch (Exception ex) {
			log.error("Error en Logica_Contabilidad.ConvertExt :: ERROR al convertir TransaccionesToTransaccionesExt", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: FacturaPagosExt To FacturaPagos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public Transacciones TransaccionesExtToTransacciones(TransaccionesExt pojoTarget) {
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: From " + this.from + " ::  TransaccionesExtToTransacciones");
		Transacciones pojoResult = new Transacciones();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setDescripcion(pojoTarget.getDescripcion());
			pojoResult.setCodigo(pojoTarget.getCodigo());
			pojoResult.setGlosa(pojoTarget.getGlosa());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando Operacion");
			if (pojoTarget.getIdOperacion() != null && pojoTarget.getIdOperacion().getId() > 0L) {
				pojoResult.setIdOperacion(this.OperacionesExtToOperaciones(pojoTarget.getIdOperacion()));
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando Operacion terminado");
			}
		} catch (Exception ex) {
			log.error("Error en Logica_Contabilidad.ConvertExt :: ERROR al convertir TransaccionesExtToTransacciones", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: TransaccionesExtToTransacciones :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public AsignacionGruposExt AsignacionGruposToAsignacionGruposExt(AsignacionGrupos pojoTarget) throws Exception {
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: From " + this.from + " :: AsignacionGruposExt To AsignacionGrupos");
		AsignacionGruposExt pojoResult = new AsignacionGruposExt();
		
		try {
			if (pojoTarget == null)
				return null;
						
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdTransaccion(pojoTarget.getIdTransaccion());
			pojoResult.setIdGrupoDebito(pojoTarget.getIdGrupoDebito());
			pojoResult.setIdGrupoCredito(pojoTarget.getIdGrupoCredito());
			pojoResult.setIdConcepto(pojoTarget.getIdConcepto());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setTipoPoliza(null);
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando FormaPago");
			if (pojoTarget.getIdFormaPago() != null && pojoTarget.getIdFormaPago() > 0L) {
				FormasPagos pojoAux = this.ifzFormasPagos.findById(pojoTarget.getIdFormaPago());
				
				if (pojoAux == null)
					pojoAux = new FormasPagos();
				
				pojoResult.setIdFormaPago(pojoAux);
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando FormaPago terminado");
			}
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando TipoPoliza");
			if (pojoTarget.getTipoPoliza() != null && pojoTarget.getTipoPoliza() > 0L) {
				ConGrupoValores pojoGpoTiposPolizas = this.ifzGruposValores.findByName("SYS_TIPO_POLIZA");
				if (pojoGpoTiposPolizas != null && pojoGpoTiposPolizas.getId() > 0L) {
					ConValores pojoAux = this.ifzConValores.findByValorGrupo(pojoTarget.getTipoPoliza().toString(), pojoGpoTiposPolizas);
					
					if (pojoAux == null)
						pojoAux = new ConValores();
					
					pojoResult.setTipoPoliza(pojoAux);
				}
				
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando TipoPoliza terminado");
			}
		} catch (Exception ex) {
			log.error("Error en Logica_Contabilidad.ConvertExt :: ERROR al convertir AsignacionGruposToAsignacionGruposExt", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: FacturaPagosExt To FacturaPagos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public AsignacionGrupos AsignacionGruposExtToAsignacionGrupos(AsignacionGruposExt pojoTarget) {
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: From " + this.from + " ::  AsignacionGruposExtToAsignacionGrupos");
		AsignacionGrupos pojoResult = new AsignacionGrupos();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdTransaccion(pojoTarget.getIdTransaccion());
			pojoResult.setIdGrupoDebito(pojoTarget.getIdGrupoDebito());
			pojoResult.setIdGrupoCredito(pojoTarget.getIdGrupoCredito());
			pojoResult.setIdConcepto(pojoTarget.getIdConcepto());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdFormaPago(0L);
			pojoResult.setTipoPoliza(0L);
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando FormaPago");
			if (pojoTarget.getIdFormaPago() != null && pojoTarget.getIdFormaPago().getId() > 0L) {
				pojoResult.setIdFormaPago(pojoTarget.getIdFormaPago().getId());
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando FormaPago terminado");
			}
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando TipoPoliza");
			if (pojoTarget.getTipoPoliza() != null && pojoTarget.getTipoPoliza().getId() > 0L) {
				pojoResult.setTipoPoliza(Long.parseLong(pojoTarget.getTipoPoliza().getValor()));
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando TipoPoliza terminado");
			}
		} catch (Exception ex) {
			log.error("Error en Logica_Contabilidad.ConvertExt :: ERROR al convertir AsignacionGruposExtToAsignacionGrupos", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: AsignacionGruposExtToAsignacionGrupos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public MensajeTransaccion MensajeTransaccionExtToMensajeTransaccion(MensajeTransaccionExt pojoTarget) throws Exception {
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: MensajeTransaccionExtToMensajeTransaccion");
			if (pojoTarget == null)
				return null;

			MensajeTransaccion pojoResult = new MensajeTransaccion();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdTransaccion(pojoTarget.getIdTransaccion());
			pojoResult.setIdOperacion(pojoTarget.getIdOperacion());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setDescripcionMoneda(pojoTarget.getDescripcionMoneda());
			pojoResult.setImporte(pojoTarget.getImporte());
			pojoResult.setIdPersonaReferencia(pojoTarget.getIdPersonaReferencia());
			pojoResult.setNombrePersonaReferencia(pojoTarget.getNombrePersonaReferencia());
			pojoResult.setTipoPersona(pojoTarget.getTipoPersona());
			pojoResult.setReferencia(pojoTarget.getReferenciaFormaPago());
			pojoResult.setIdFormaPago(pojoTarget.getIdFormaPago());
			pojoResult.setReferenciaFormaPago(pojoTarget.getReferenciaFormaPago());
			pojoResult.setIdUsuarioCreacionRegistro(pojoTarget.getIdUsuarioCreacionRegistro());
			pojoResult.setIdSucursal(pojoTarget.getIdSucursal());
			pojoResult.setPoliza(pojoTarget.getPoliza());
			pojoResult.setLote(pojoTarget.getLote());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setAnuladoPor(pojoTarget.getAnuladoPor());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setEstatusMensaje(pojoTarget.getEstatusMensaje());
			
			if (pojoTarget.getFechaCreacion() != null && ! "".equals(pojoTarget.getFechaCreacion()))
				pojoResult.setFechaCreacion(formatter.parse(pojoTarget.getFechaCreacion()));
			if (pojoTarget.getFechaAnulacion() != null && ! "".equals(pojoTarget.getFechaAnulacion()))
				pojoResult.setFechaAnulacion(formatter.parse(pojoTarget.getFechaAnulacion()));
			if (pojoTarget.getFechaRegistro() != null && ! "".equals(pojoTarget.getFechaRegistro()))
				pojoResult.setFechaRegistro(formatter.parse(pojoTarget.getFechaRegistro()));
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			return pojoResult;
		} catch (Exception e) {
			log.error("Error en Logica_Contabilidad.ConvertExt :: ERROR al convertir MensajeTransaccionExtToMensajeTransaccion", e);
			throw e;
		}
	}
	
	public MensajeTransaccionExt MensajeTransaccionToMensajeTransaccionExt(MensajeTransaccion pojoTarget) throws Exception {
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: MensajeTransaccionToMensajeTransaccionExt");
			if (pojoTarget == null)
				return null;

			MensajeTransaccionExt pojoResult = new MensajeTransaccionExt();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdTransaccion(pojoTarget.getIdTransaccion());
			pojoResult.setIdOperacion(pojoTarget.getIdOperacion());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setDescripcionMoneda(pojoTarget.getDescripcionMoneda());
			pojoResult.setImporte(pojoTarget.getImporte());
			pojoResult.setIdPersonaReferencia(pojoTarget.getIdPersonaReferencia());
			pojoResult.setNombrePersonaReferencia(pojoTarget.getNombrePersonaReferencia());
			pojoResult.setTipoPersona(pojoTarget.getTipoPersona());
			pojoResult.setReferencia(pojoTarget.getReferenciaFormaPago());
			pojoResult.setIdFormaPago(pojoTarget.getIdFormaPago());
			pojoResult.setReferenciaFormaPago(pojoTarget.getReferenciaFormaPago());
			pojoResult.setIdUsuarioCreacionRegistro(pojoTarget.getIdUsuarioCreacionRegistro());
			pojoResult.setIdSucursal(pojoTarget.getIdSucursal());
			pojoResult.setPoliza(pojoTarget.getPoliza());
			pojoResult.setLote(pojoTarget.getLote());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setAnuladoPor(pojoTarget.getAnuladoPor());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setEstatusMensaje(pojoTarget.getEstatusMensaje());
			
			if (pojoTarget.getFechaCreacion() != null)
				pojoResult.setFechaCreacion(formatter.format(pojoTarget.getFechaCreacion()));
			if (pojoTarget.getFechaAnulacion() != null)
				pojoResult.setFechaAnulacion(formatter.format(pojoTarget.getFechaAnulacion()));
			if (pojoTarget.getFechaRegistro() != null)
				pojoResult.setFechaRegistro(formatter.format(pojoTarget.getFechaRegistro()));
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			return pojoResult;
		} catch (Exception e) {
			log.error("Error en Logica_Contabilidad.ConvertExt :: ERROR al convertir MensajeTransaccionToMensajeTransaccionExt", e);
			throw e;
		}
	}
}
