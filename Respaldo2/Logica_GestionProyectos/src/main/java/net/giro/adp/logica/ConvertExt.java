package net.giro.adp.logica;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.adp.beans.Insumos;
import net.giro.adp.beans.InsumosDetalles;
import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.adp.beans.InsumosExt;
import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.ObraAlmacenesExt;
import net.giro.adp.beans.ObraEmpleado;
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.adp.beans.ObraExt;
import net.giro.adp.beans.Presupuesto;
import net.giro.adp.beans.PresupuestoDetalle;
import net.giro.adp.beans.PresupuestoDetalleExt;
import net.giro.adp.beans.PresupuestoExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.dao.NegocioDAO;
import net.giro.clientes.dao.PersonaDAO;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.log4j.Logger;

public class ConvertExt {
	private static Logger log = Logger.getLogger(ConvertExt.class);
	private InitialContext ctx = null;
	
	// Interfaces
	private ObraRem				 	ifzObras;
	private SucursalesRem 		 	ifzSucursales;
	private EmpleadoRem 		 	ifzEmpleados;
	private ProductoRem 		 	ifzProductos;
	private InsumosRem 			 	ifzInsumos;
	private PersonaDAO 			 	ifzPersonas;
	private NegocioDAO 			 	ifzNegocios;
	private ConValoresDAO 		 	ifzConValores;
	private LocalidadDAO 		 	ifzLocalidad;
	private ColoniaDAO 			 	ifzColonia;
	private AlmacenRem				ifzAlmacenes;
	private PresupuestoRem			ifzPresupuesto;
	private ConceptoPresupuestoRem	ifzConceptoPresupuesto;
	// Variables de control
	private String from;
	private boolean mostrarSystemOut;
	
	
	public ConvertExt() {
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            
    		this.ctx = new InitialContext(p);

    		this.ifzInsumos 	= (InsumosRem)				this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
    		this.ifzObras		= (ObraRem)					this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
    		this.ifzPresupuesto	= (PresupuestoRem)			this.ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoFac!net.giro.adp.logica.PresupuestoRem");
    		this.ifzSucursales 	= (SucursalesRem)			this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
    		this.ifzEmpleados	= (EmpleadoRem)				this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
    		this.ifzProductos 	= (ProductoRem)				this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
    		this.ifzNegocios 	= (NegocioDAO) 				this.ctx.lookup("ejb:/Model_Clientes//NegocioImp!net.giro.clientes.dao.NegocioDAO");
    		this.ifzPersonas 	= (PersonaDAO) 				this.ctx.lookup("ejb:/Model_Clientes//PersonaImp!net.giro.clientes.dao.PersonaDAO");
    		this.ifzColonia 	= (ColoniaDAO) 				this.ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");
    		this.ifzConValores 	= (ConValoresDAO) 			this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
    		this.ifzLocalidad 	= (LocalidadDAO) 			this.ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
    		this.ifzAlmacenes	= (AlmacenRem)				this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
    		this.ifzConceptoPresupuesto = (ConceptoPresupuestoRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ConceptoPresupuestoFac!net.giro.adp.logica.ConceptoPresupuestoRem");
    		
    		this.from = "default";
    		this.mostrarSystemOut = false;
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear Logica_GestionProyectos.ConvertExt", e);
			ctx = null;
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
	
	public Obra ObraExtToObra(ObraExt pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: ObraExt To Obra");
		Obra pojoResult = new Obra();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setNombre(pojoTarget.getNombre());
			pojoResult.setTipoObra(pojoTarget.getTipoObra());
			pojoResult.setPorcentajeAnticipo(pojoTarget.getPorcentajeAnticipo());
			pojoResult.setMontoAnticipo(pojoTarget.getMontoAnticipo());
			pojoResult.setMontoContratado(pojoTarget.getMontoContratado());
			pojoResult.setDomicilio(pojoTarget.getDomicilio());
			pojoResult.setFechaInicio(pojoTarget.getFechaInicio());
			pojoResult.setFechaTerminacion(pojoTarget.getFechaTerminacion());
			pojoResult.setNombreContrato(pojoTarget.getNombreContrato());
			pojoResult.setObjetoContrato(pojoTarget.getObjetoContrato());
			pojoResult.setSatic02(pojoTarget.getSatic02());
			pojoResult.setTipoCliente(pojoTarget.getTipoCliente());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setNombreResponsable(pojoTarget.getNombreResponsable());
			pojoResult.setNombreObraPrincipal(pojoTarget.getNombreObraPrincipal());
			pojoResult.setEstatusContrato(pojoTarget.getEstatusContrato());
			pojoResult.setAutorizado(pojoTarget.getAutorizado());
			pojoResult.setIdUsuarioAutorizo(pojoTarget.getIdUsuarioAutorizo() );
			pojoResult.setFechaAutorizacion(pojoTarget.getFechaAutorizacion());
			pojoResult.setRevisado(pojoTarget.getRevisado());
			pojoResult.setRevisadoPor(pojoTarget.getRevisadoPor());
			pojoResult.setFechaRevisado(pojoTarget.getFechaRevisado());
			pojoResult.setMontoDeductiva(pojoTarget.getMontoDeductiva());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setDescripcionMoneda(pojoTarget.getDescMoneda());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setIdObraPrincipal(0L);
			pojoResult.setIdCliente(0L);
			pojoResult.setIdDomicilio(0L);
			pojoResult.setIdSucursal(0L);
			pojoResult.setIdResponsable(0L);
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando ObraPrincipal");
			if (pojoTarget.getIdObraPrincipal() != null && pojoTarget.getIdObraPrincipal().getId() != null && pojoTarget.getIdObraPrincipal().getId() > 0) {
				pojoResult.setIdObraPrincipal(pojoTarget.getIdObraPrincipal().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando ObraPrincipal terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cliente");
			if (pojoTarget.getIdCliente() != null && pojoTarget.getIdCliente().getId() > 0) {
				pojoResult.setIdCliente(pojoTarget.getIdCliente().getId());
				pojoResult.setNombreCliente(pojoTarget.getIdCliente().getNombre());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cliente terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Domicilio");
			if (pojoTarget.getPojoDomicilio() != null && pojoTarget.getPojoDomicilio().getId() > 0) {
				pojoResult.setIdDomicilio(pojoTarget.getPojoDomicilio().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Domicilio terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Sucursal");
			if (pojoTarget.getIdSucursal() != null && pojoTarget.getIdSucursal().getId() > 0) {
				pojoResult.setIdSucursal(pojoTarget.getIdSucursal().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Sucursal terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Responsable");
			if (pojoTarget.getIdResponsable() != null && pojoTarget.getIdResponsable().getId() > 0) {
				pojoResult.setIdResponsable(pojoTarget.getIdResponsable().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Responsable terminado");
			}
		} catch (Exception ex) {
			log.error("Logica_GestionProyectos.ConvertExt :: ERROR al convertir ObraExt To Obra :: " + ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: ObraExt To Obra :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ObraExt ObraToObraExt(Obra pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: Obra To ObraExt");
		ObraExt pojoResult = new ObraExt();
		
		try {
			if (pojoTarget.getTipoObra() <= 0)
				pojoTarget.setTipoObra(1);
			
			if (pojoTarget.getTipoCliente() == null)
				pojoTarget.setTipoCliente("P");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setNombre(pojoTarget.getNombre());
			pojoResult.setTipoObra(pojoTarget.getTipoObra());
			pojoResult.setMontoContratado(pojoTarget.getMontoContratado());
			pojoResult.setMontoAnticipo(pojoTarget.getMontoAnticipo());
			pojoResult.setPorcentajeAnticipo(pojoTarget.getPorcentajeAnticipo());
			pojoResult.setDomicilio(pojoTarget.getDomicilio());
			pojoResult.setFechaInicio(pojoTarget.getFechaInicio());
			pojoResult.setFechaTerminacion(pojoTarget.getFechaTerminacion());
			pojoResult.setNombreContrato(pojoTarget.getNombreContrato());
			pojoResult.setObjetoContrato(pojoTarget.getObjetoContrato());
			pojoResult.setSatic02(pojoTarget.getSatic02());
			pojoResult.setNombreCliente(pojoTarget.getNombreCliente());
			pojoResult.setTipoCliente(pojoTarget.getTipoCliente());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setNombreResponsable(pojoTarget.getNombreResponsable());
			pojoResult.setNombreObraPrincipal(pojoTarget.getNombreObraPrincipal());			
			pojoResult.setAutorizado(pojoTarget.getAutorizado());
			pojoResult.setIdUsuarioAutorizo(pojoTarget.getIdUsuarioAutorizo() );
			pojoResult.setFechaAutorizacion(pojoTarget.getFechaAutorizacion());
			pojoResult.setEstatusContrato(pojoTarget.getEstatusContrato());
			pojoResult.setRevisado(pojoTarget.getRevisado());
			pojoResult.setRevisadoPor(pojoTarget.getRevisadoPor());
			pojoResult.setFechaRevisado(pojoTarget.getFechaRevisado());
			pojoResult.setMontoDeductiva(pojoTarget.getMontoDeductiva());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setDescMoneda(pojoTarget.getDescripcionMoneda());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");

			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando ObraPrincipal");
			if (pojoTarget.getIdObraPrincipal() != null && pojoTarget.getIdObraPrincipal() > 0) {
				Obra pojoObra = this.ifzObras.findById(pojoTarget.getIdObraPrincipal());
				
				pojoResult.setIdObraPrincipal(pojoObra);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando ObraPrincipal terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cliente");
			if (pojoTarget.getIdCliente() > 0) {
				Persona pojoPersona = new Persona();
				
				if (pojoTarget.getTipoCliente() == null || "P".equals(pojoTarget.getTipoCliente())) {
					pojoPersona = this.ifzPersonas.findById(pojoTarget.getIdCliente());
					if (pojoPersona == null) 
						pojoPersona = new Persona();
					
					pojoPersona.setTipoPersona(1L);
					pojoResult.setTipoCliente("P");
				} else {
					Negocio pojoNegocio = this.ifzNegocios.findById(pojoTarget.getIdCliente());
					if (pojoNegocio != null) 
						pojoPersona = this.NegocioToPersona(pojoNegocio);
					
					pojoPersona.setTipoPersona(0L);
					pojoResult.setTipoCliente("N");
				}
				
				pojoResult.setIdCliente(PersonaToPersonaExt(pojoPersona));
				
				if (pojoResult.getNombreCliente() == null || "".equals(pojoResult.getNombreCliente()))
					pojoResult.setNombreCliente(pojoPersona.getNombre());
				
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Cliente terminado");
			}

			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Sucursal");
			if (pojoTarget.getIdSucursal() != null && pojoTarget.getIdSucursal() > 0) {
				Sucursal pojoAux = this.ifzSucursales.findById(pojoTarget.getIdSucursal());
				
				if (pojoAux == null)
					pojoAux = new Sucursal();
				
				pojoResult.setIdSucursal(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Sucursal terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Responsable");
			if (pojoTarget.getIdResponsable() != null && pojoTarget.getIdResponsable() > 0) {
				EmpleadoExt pojoAux = this.ifzEmpleados.findByIdExt(pojoTarget.getIdResponsable());
				
				if (pojoAux == null)
					pojoAux = new EmpleadoExt();
				
				pojoResult.setIdResponsable(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Responsable terminado");
			}
		} catch (Exception ex) {
			log.error("Logica_GestionProyectos.ConvertExt :: ERROR al convertir Obra To ObraExt :: ", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: Obra To ObraExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public PersonaExt PersonaToPersonaExt(Persona pojoTarget) throws Exception{
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: PersonaToPersonaExt");
		PersonaExt pojoResult =  new PersonaExt();

		pojoResult.setId(pojoTarget.getId());
		pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
		pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
		pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
		pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
		if(pojoTarget.getHomonimo() > 0){
			pojoResult.setHomonimo(true);
		}else{
			pojoResult.setHomonimo(false);
		}
		pojoResult.setNombre(pojoTarget.getNombre());
		pojoResult.setPrimerNombre(pojoTarget.getPrimerNombre());
		pojoResult.setSegundoNombre(pojoTarget.getSegundoNombre());
		pojoResult.setNombresPropios(pojoTarget.getNombresPropios());
		pojoResult.setPrimerApellido(pojoTarget.getPrimerApellido());
		pojoResult.setSegundoApellido(pojoTarget.getSegundoApellido());
		pojoResult.setRfc(pojoTarget.getRfc());
		pojoResult.setSexo(pojoTarget.getSexo());
		pojoResult.setFechaNacimiento(pojoTarget.getFechaNacimiento());
		pojoResult.setNumeroHijos(pojoTarget.getNumeroHijos());
		pojoResult.setDomicilio(pojoTarget.getDomicilio());
		pojoResult.setTelefono(pojoTarget.getTelefono());
		pojoResult.setCorreo(pojoTarget.getCorreo());
		
		if(pojoTarget.getEstadoCivil() != null){
			pojoResult.setEstadoCivil(ifzConValores.findById(pojoTarget.getEstadoCivil()));
		}
		
		if(pojoTarget.getLocalidad() != null){
			pojoResult.setLocalidad(ifzLocalidad.findById(pojoTarget.getLocalidad()));
		}
		
		if(pojoTarget.getNacionalidad () != null){
			pojoResult.setNacionalidad(ifzConValores.findById(pojoTarget.getNacionalidad()));
		}
		
		if(pojoTarget.getConyuge() != null)
			pojoResult.setConyuge(PersonaToPersonaExt(pojoTarget.getConyuge()));
		
		if(pojoTarget.getFinado() > 0){
			pojoResult.setFinado(true);
		}else{
			pojoResult.setFinado(false);
		}
		
		if(pojoTarget.getTipoSangre() != null){
			pojoResult.setTipoSangre(ifzConValores.findById(pojoTarget.getTipoSangre()));
		}
		
		if(pojoTarget.getColonia() != null){
			pojoResult.setColonia(ifzColonia.findById(pojoTarget.getColonia()));
		}
		
		pojoResult.setTipoPersona(pojoTarget.getTipoPersona());
		
		String res = pojoTarget.getPrimerNombre() != null && pojoTarget.getPrimerNombre().length() > 0 ? pojoTarget.getPrimerNombre() : "";
		res += pojoTarget.getSegundoNombre()	!= null && pojoTarget.getSegundoNombre().length() > 0 ? " " + pojoTarget.getSegundoNombre() : "";
		res += pojoTarget.getNombresPropios()	!= null && pojoTarget.getNombresPropios().length() > 0 ? " " + pojoTarget.getNombresPropios() : "";
		res += pojoTarget.getPrimerApellido()	!= null && pojoTarget.getPrimerApellido().length() > 0 ? " " + pojoTarget.getPrimerApellido() : "";
		res += pojoTarget.getSegundoApellido()	!= null && pojoTarget.getSegundoApellido().length() > 0 ? " " + pojoTarget.getSegundoApellido() : "";
		pojoResult.setNombreCompleto(res);
		pojoResult.setNumeroCuenta(pojoTarget.getNumeroCuenta());
		pojoResult.setClabeInterbancaria(pojoTarget.getClabeInterbancaria());
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: Persona To PersonaExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public Persona NegocioToPersona(Negocio pojoNegocio) {
		Persona pojoPersona = new Persona();
		
		try {
			pojoPersona.setId(pojoNegocio.getId());
			pojoPersona.setNombre(pojoNegocio.getNombre());
			pojoPersona.setRfc(pojoNegocio.getRfc());
			pojoPersona.setBanco(pojoNegocio.getBanco());
			pojoPersona.setClabeInterbancaria(pojoNegocio.getClabeInterbancaria());
			pojoPersona.setTipoPersona(0L);
			pojoPersona.setDomicilio(pojoNegocio.getDomicilio());
			pojoPersona.setColonia(pojoNegocio.getColonia());
			pojoPersona.setTelefono(pojoNegocio.getTelefono());
			pojoPersona.setCorreo(pojoNegocio.getCorreoContacto());
		} catch (Exception e) {
			log.info("Logica_GestionProyectos.ConvertExt :: ERROR al convertir Negocio To Persona :: " + e.getMessage());
			log.error("Logica_GestionProyectos.ConvertExt :: ERROR al convertir Negocio To Persona", e);
			throw e;
		}
		
		return pojoPersona;
	}
	
	public ObraEmpleado ObraEmpleadoExtToObraEmpleado(ObraEmpleadoExt pojoTarget) {
		ObraEmpleado pojoResult = new ObraEmpleado();
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: ObraExt To Obra");
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0L) {
				pojoResult.setIdObra(ObraExtToObra(pojoTarget.getIdObra()));
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Empleado");
			if (pojoTarget.getIdEmpleado() != null && pojoTarget.getIdEmpleado().getId() != null && pojoTarget.getIdEmpleado().getId() > 0L) {
				pojoResult.setIdEmpleado(pojoTarget.getIdEmpleado().getId());
			}
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.ConvertExt.ObraEmpleadoExtToObraEmpleado", e);
			throw e;
		}
		
		return pojoResult;
	}
	
	public ObraEmpleadoExt ObraEmpleadoToObraEmpleadoExt(ObraEmpleado pojoTarget) throws Exception {
		ObraEmpleadoExt pojoResult = new ObraEmpleadoExt();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0L) {
				pojoResult.setIdObra(ObraToObraExt(pojoTarget.getIdObra()));
				/*Obra pojoAux = this.ifzObras.findById(pojoTarget.getIdObra());
				if (pojoAux != null)
					pojoResult.setIdObra(this.ObraToObraExt(pojoAux));
				else
					pojoResult.setIdObra(new ObraExt());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");*/
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Empleado");
			if (pojoTarget.getIdEmpleado() != null && pojoTarget.getIdEmpleado() > 0L) {
				EmpleadoExt pojoAux = this.ifzEmpleados.findByIdExt(pojoTarget.getIdEmpleado());
				if (pojoAux != null)
					pojoResult.setIdEmpleado(pojoAux);
				else
					pojoResult.setIdEmpleado(new EmpleadoExt());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Empleado terminado");
			}
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.ConvertExt.ObraEmpleadoToObraEmpleadoExt", e);
			throw e;
		}
		
		return pojoResult;
	}
	
	public Insumos InsumosExtToInsumos(InsumosExt pojoTarget) {
		Insumos pojoResult = new Insumos();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setTotalMateriales(pojoTarget.getMontoMateriales());
			pojoResult.setTotalManoDeObra(pojoTarget.getMontoManoDeObra());
			pojoResult.setTotalHerramientas(pojoTarget.getMontoHerramientas());
			pojoResult.setTotalOtros(pojoTarget.getMontoOtros());
			pojoResult.setTotal(pojoTarget.getTotal());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setNombreArchivo(pojoTarget.getNombreArchivo());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			pojoResult.setIdPresupuesto(0L);
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Presupuesto");
			if(pojoTarget.getIdPresupuesto() != null && pojoTarget.getIdPresupuesto().getId() > 0L) {
				pojoResult.setIdPresupuesto(pojoTarget.getIdPresupuesto().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Presupuesto terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if(pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0L) {
				pojoResult.setIdObra(pojoTarget.getIdObra().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.ConvertExt.ObraEmpleadoToObraEmpleadoExt", e);
			throw e;
		}
		
		return pojoResult;
	}
	
	public InsumosExt InsumosToInsumosExt(Insumos pojoTarget) throws Exception {
		InsumosExt pojoResult = new InsumosExt();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setMontoMateriales(pojoTarget.getTotalMateriales());
			pojoResult.setMontoManoDeObra(pojoTarget.getTotalManoDeObra());
			pojoResult.setMontoHerramientas(pojoTarget.getTotalHerramientas());
			pojoResult.setMontoOtros(pojoTarget.getTotalOtros());
			pojoResult.setTotal(pojoTarget.getTotal());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setNombreArchivo(pojoTarget.getNombreArchivo());
			pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Presupuesto");
			if(pojoTarget.getIdPresupuesto() != null && pojoTarget.getIdPresupuesto() > 0L) {
				Presupuesto pojoAux = this.ifzPresupuesto.findById(pojoTarget.getIdPresupuesto());
				
				if (pojoAux == null)
					pojoAux = new Presupuesto();
				
				pojoResult.setIdPresupuesto(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Presupuesto terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if(pojoTarget.getIdObra() != null && pojoTarget.getIdObra() > 0L) {
				Obra pojoAux = this.ifzObras.findById(pojoTarget.getIdObra());
				
				if (pojoAux == null)
					pojoAux = new Obra();
				
				pojoResult.setIdObra(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.ConvertExt.ObraEmpleadoToObraEmpleadoExt", e);
			throw e;
		}
		
		return pojoResult;
	}
	
	public InsumosDetalles InsumosDetallesExtToInsumosDetalles(InsumosDetallesExt pojoTarget) {
		InsumosDetalles pojoResult = new InsumosDetalles();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setNombreProducto(pojoTarget.getNombreProducto());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setPrecioUnitario(pojoTarget.getPrecioUnitario());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setPorcentaje(pojoTarget.getPorcentaje());
			pojoResult.setCantidadSurtida(pojoTarget.getCantidadSurtida());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Presupuesto");
			if(pojoTarget.getIdInsumos() != null && pojoTarget.getIdInsumos().getId() != null && pojoTarget.getIdInsumos().getId() > 0L) {
				pojoResult.setIdInsumo(pojoTarget.getIdInsumos().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Presupuesto terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto");
			if(pojoTarget.getIdProducto() != null && pojoTarget.getIdProducto().getId() != null && pojoTarget.getIdProducto().getId() > 0L) {
				pojoResult.setIdProducto(pojoTarget.getIdProducto().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto terminado");
			}
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.ConvertExt.InsumosDetallesExtToInsumosDetalles", e);
			throw e;
		}
		
		return pojoResult;
	}
	
	public InsumosDetallesExt InsumosDetallesToInsumosDetallesExt(InsumosDetalles pojoTarget) throws Exception {
		InsumosDetallesExt pojoResult = new InsumosDetallesExt();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setNombreProducto(pojoTarget.getNombreProducto());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setPrecioUnitario(pojoTarget.getPrecioUnitario());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setPorcentaje(pojoTarget.getPorcentaje());
			pojoResult.setCantidadSurtida(pojoTarget.getCantidadSurtida());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Presupuesto");
			if(pojoTarget.getIdInsumo() != null && pojoTarget.getIdInsumo() > 0L) {
				InsumosExt pojoAux = this.ifzInsumos.findByIdExt(pojoTarget.getIdInsumo());
				
				if (pojoAux == null)
					pojoAux = new InsumosExt();
				
				pojoResult.setIdInsumos(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Presupuesto terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto");
			if(pojoTarget.getIdProducto() != null && pojoTarget.getIdProducto() > 0L) {
				ProductoExt pojoAux = this.ifzProductos.findExtById(pojoTarget.getIdProducto());
				
				if (pojoAux == null)
					pojoAux = new ProductoExt();
				
				pojoResult.setIdProducto(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Producto terminado");
			}
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.ConvertExt.InsumosDetallesToInsumosDetallesExt", e);
			throw e;
		}
		
		return pojoResult;
	}
	
	public Presupuesto PresupuestoExtToPresupuesto(PresupuestoExt pojoTarget) {
		Presupuesto pojoResult = new Presupuesto();
		
		try {
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdObra(pojoTarget.getObra());
			pojoResult.setTema(pojoTarget.getTema());
			pojoResult.setRuta(pojoTarget.getRuta());
			pojoResult.setMontoTotal(pojoTarget.getMontoTotal());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setNombreArchivo(pojoTarget.getNombreArchivo());
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.ConvertExt.PresupuestoExtToPresupuesto", e);
			throw e;
		}
		
		return pojoResult;
	}
	
	public PresupuestoExt PresupuestoToPresupuestoExt(Presupuesto pojoTarget) throws Exception {
		PresupuestoExt pojoResult = new PresupuestoExt();
		
		try {
			pojoResult.setId(pojoTarget.getId());
			if(pojoTarget.getIdObra().getId() > 0)
				pojoResult.setObra(pojoTarget.getIdObra());
			pojoResult.setTema(pojoTarget.getTema());
			pojoResult.setRuta(pojoTarget.getRuta());
			pojoResult.setMontoTotal(pojoTarget.getMontoTotal());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setNombreArchivo(pojoTarget.getNombreArchivo());
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.ConvertExt.PresupuestoToPresupuestoExt", e);
			throw e;
		}
		
		return pojoResult;
	}
	
	public PresupuestoDetalleExt PresupuestoDetalleToPresupuestoDetalleExt(PresupuestoDetalle pojoTarget){
		PresupuestoDetalleExt pojoResult = new PresupuestoDetalleExt();
		
		try{
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdPresupuesto(pojoTarget.getIdPresupuesto());
			pojoResult.setConceptoPresupuesto( this.ifzConceptoPresupuesto.findById(pojoTarget.getIdConceptoPresupuesto()) );
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setPorcentaje(pojoTarget.getPorcentaje());
			pojoResult.setPorcentajeTotales(pojoTarget.getPorcentajeTotales());
			pojoResult.setNotas(pojoTarget.getNotas());
			pojoResult.setFormula(pojoTarget.getFormula());
			pojoResult.setEjercido(pojoTarget.getEjercido());
			pojoResult.setTransito(pojoTarget.getTransito());
			pojoResult.setAditiva(pojoTarget.getAditiva());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.ConvertExt.PresupuestoDetalleToPresupuestoDetalleExt", e);
			throw e;
		}
		
		return pojoResult;
	}
	
	public PresupuestoDetalle PresupuestoDetalleExtToPresupuestoDetalle(PresupuestoDetalleExt pojoTarget){
		PresupuestoDetalle pojoResult = new PresupuestoDetalle();
		
		try{
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdPresupuesto(pojoTarget.getIdPresupuesto());
			pojoResult.setIdConceptoPresupuesto(pojoTarget.getConceptoPresupuesto().getId());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setPorcentaje(pojoTarget.getPorcentaje());
			pojoResult.setPorcentajeTotales(pojoTarget.getPorcentajeTotales());
			pojoResult.setNotas(pojoTarget.getNotas());
			pojoResult.setFormula(pojoTarget.getFormula());
			pojoResult.setEjercido(pojoTarget.getEjercido());
			pojoResult.setTransito(pojoTarget.getTransito());
			pojoResult.setAditiva(pojoTarget.getAditiva());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.ConvertExt.PresupuestoDetalleToPresupuestoDetalleExt", e);
			throw e;
		}
		
		return pojoResult;
	}

	public ObraAlmacenesExt ObraAlmacenesToObraAlmacenesExt(ObraAlmacenes pojoTarget){
		if (this.mostrarSystemOut) log.info("Logica_GestionProyectos.ConvertExt :: From " + this.from + " ::  ObraAlmacenesToObraAlmacenesExt");
		ObraAlmacenesExt pojoResult = new ObraAlmacenesExt();
		
		try {
			if (pojoTarget == null)
				return null;
						
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setNombreAlmacen(pojoTarget.getNombreAlmacen());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setAlmacenPrincipal(pojoTarget.getAlmacenPrincipal());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0L) {
				pojoResult.setIdObra(pojoTarget.getIdObra());
				/*Obra pojoAux = this.ifzObras.findById(pojoTarget.getIdObra());
				
				if (pojoAux == null)
					pojoAux = new Obra();
				
				pojoResult.setIdObra(pojoAux);*/
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdAlmacen() != null && pojoTarget.getIdAlmacen() > 0L) {
				Almacen pojoAux = this.ifzAlmacenes.findById(pojoTarget.getIdAlmacen());
				
				if (pojoAux == null)
					pojoAux = new Almacen();
				
				pojoResult.setIdAlmacen(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
		} catch (Exception ex) {
			log.error("Logica_GestionProyectos.ConvertExt :: ERROR al convertir ObraAlmacenesToObraAlmacenesExt :: ", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("Logica_GestionProyectos.ConvertExt :: ObraAlmacenesToObraAlmacenesExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ObraAlmacenes ObraAlmacenesExtToObraAlmacenes(ObraAlmacenesExt pojoTarget){
		if (this.mostrarSystemOut) log.info("Logica_GestionProyectos.ConvertExt :: From " + this.from + " ::  ObraAlmacenesExtToObraAlmacenes");
		ObraAlmacenes pojoResult = new ObraAlmacenes();
		
		try {
			if (pojoTarget == null)
				return null;
						
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setNombreAlmacen(pojoTarget.getNombreAlmacen());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdAlmacen(0L);
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0L) {
				pojoResult.setIdObra(pojoTarget.getIdObra());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Almacen");
			if (pojoTarget.getIdAlmacen() != null && pojoTarget.getIdAlmacen().getId() != null && pojoTarget.getIdAlmacen().getId() > 0L) {
				pojoResult.setIdAlmacen(pojoTarget.getIdAlmacen().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Almacen terminado");
			}
		} catch (Exception ex) {
			log.error("Logica_GestionProyectos.ConvertExt :: ERROR al convertir ObraAlmacenesExtToObraAlmacenes :: ", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("Logica_GestionProyectos.ConvertExt :: ObraAlmacenesExtToObraAlmacenes :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Cambiando los tipos PresupuestoDetalleRem a Presupuesto
//2.2		|19/05/2016		|Javier Tirado	|Creamos la conversion de ObraAlmacenes a ObraAlmacenesExt y viceversa
//2.2		|22/06/2016		|Javier Tirado	| Agrego la propiedad porcentajeTotales a la conversion PresupuestoDetalle a PresupuestoDetalleExt y viceversa

