package net.giro.rh.admon.logica;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.clientes.beans.Persona;
import net.giro.clientes.dao.PersonaDAO;
import net.giro.ne.dao.SucursalDAO;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.rh.admon.beans.AreaExt;
import net.giro.rh.admon.beans.CategoriaExt;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.ChecadorDetalleExt;
import net.giro.rh.admon.beans.ChecadorExt;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.beans.EmpleadoFiniquitoExt;
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.beans.Categoria;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoBeneficiario;
import net.giro.rh.admon.beans.EmpleadoBeneficiarioExt;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoContratoExt;
import net.giro.rh.admon.beans.EmpleadoPariente;
import net.giro.rh.admon.beans.EmpleadoParienteExt;
import net.giro.rh.admon.beans.EmpleadoReferencia;
import net.giro.rh.admon.beans.EmpleadoReferenciaExt;
import net.giro.rh.admon.beans.EmpleadoDescuento;
import net.giro.rh.admon.beans.EmpleadoDescuentoExt;
import net.giro.rh.admon.beans.EmpleadoTrabajoAnterior;
import net.giro.rh.admon.beans.EmpleadoTrabajoAnteriorExt;
import net.giro.rh.admon.beans.Puesto;
import net.giro.rh.admon.beans.PuestoCategoria;
import net.giro.rh.admon.beans.PuestoCategoriaExt;
import net.giro.rh.admon.beans.PuestoExt;
import net.giro.rh.admon.dao.AreaDAO;
import net.giro.rh.admon.dao.CategoriaDAO;
import net.giro.rh.admon.dao.EmpleadoDAO;
import net.giro.rh.admon.dao.PuestoCategoriaDAO;
import net.giro.rh.admon.dao.PuestoDAO;

public class ConvertExt {
	
	private static Logger log = Logger.getLogger(ConvertExt.class);
	private InitialContext ctx = null;
	
	private EmpleadoDAO 		ifzEmpleado;
	private AreaDAO 			ifzArea; 
	private PersonaDAO 			ifzPersona;
	private PuestoDAO 			ifzPuesto;
	private CategoriaDAO 		ifzCategoria;
	private ConValoresDAO 		ifzConValores;
	private SucursalDAO 		ifzSucursal;	
	private PuestoCategoriaDAO ifzPuestoCategoria;
	//private ChecadorDAO	ifzChecador;
	
	private String from;
	private boolean mostrarSystemOut;
	
	
	public ConvertExt() {		
		try {			
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);

    		String ejbName;
    		
    		//Checador
    		ejbName= "ejb:/Model_RecHum//ChecadorImp!net.giro.rh.admon.dao.ChecadorDAO";
            //this.ifzChecador = (ChecadorDAO) ctx.lookup(ejbName);
    		
    		//Empleado
    		ejbName= "ejb:/Model_RecHum//EmpleadoImp!net.giro.rh.admon.dao.EmpleadoDAO";
            this.ifzEmpleado = (EmpleadoDAO) ctx.lookup(ejbName);
            
            //Area
            ejbName= "ejb:/Model_RecHum//AreaImp!net.giro.rh.admon.dao.AreaDAO";
            this.ifzArea = (AreaDAO) ctx.lookup(ejbName);
            
            //Persona
            ejbName = "ejb:/Model_Clientes//PersonaImp!net.giro.clientes.dao.PersonaDAO";
            this.ifzPersona =  (PersonaDAO) ctx.lookup(ejbName);
            
            //Puesto
            ejbName= "ejb:/Model_RecHum//PuestoImp!net.giro.rh.admon.dao.PuestoDAO";
            this.ifzPuesto = (PuestoDAO) ctx.lookup(ejbName);
            
            //Categoria
            ejbName= "ejb:/Model_RecHum//CategoriaImp!net.giro.rh.admon.dao.CategoriaDAO";
            this.ifzCategoria = (CategoriaDAO) ctx.lookup(ejbName);
            
            //PuestoCategoria
            ejbName= "ejb:/Model_RecHum//PuestoCategoriaImp!net.giro.rh.admon.dao.PuestoCategoriaDAO";
            this.ifzPuestoCategoria = (PuestoCategoriaDAO) ctx.lookup(ejbName);
            
            
            //ConValores
            ejbName = "ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO";
            this.ifzConValores= (ConValoresDAO)  this.ctx.lookup(ejbName);

    		ejbName = "ejb:/Model_Publico//SucursalImp!net.giro.ne.dao.SucursalDAO";
    		this.ifzSucursal = (SucursalDAO) ctx.lookup(ejbName);

    		this.setFrom("Default");
    		this.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear ConvertExt", e);
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
	
	public Area AreaExtToArea(AreaExt pojoTarget){
		Area pojoResult = new Area();
		try {
			
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setDescripcion( pojoTarget.getDescripcion() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			pojoResult.setIdResponsable(0); 
			
			if (pojoTarget.getResponsable() != null && pojoTarget.getResponsable().getId() != null && pojoTarget.getResponsable().getId() > 0L)
				pojoResult.setIdResponsable(pojoTarget.getResponsable().getId().intValue());
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public AreaExt AreaToAreaExt(Area pojoTarget){
		AreaExt pojoResult = new AreaExt();
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setDescripcion( pojoTarget.getDescripcion() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			
			if (pojoTarget.getIdResponsable() > 0 ){
				Empleado empleado = this.ifzEmpleado.findById( pojoTarget.getIdResponsable() );
				EmpleadoExt empleadoExt = this.EmpleadoToEmpleadoExt(empleado);
				pojoResult.setResponsable( empleadoExt );
			} 
			
			pojoResult.setEstatus( pojoTarget.getEstatus() );
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
		
	//Metodos que serán implementados cuando necesiten ser llamados
	public Categoria CategoriaExtToCategoria(CategoriaExt pojoTarget){
		Categoria pojoResult = new Categoria();
		try {
			//esta clase no posee objetos compuestos
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public CategoriaExt CategoriaToCategoriaExt(Categoria pojoTarget){
		CategoriaExt pojoResult = new CategoriaExt();
		try {
			//esta clase no posee objetos compuestos
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	//EmpleadoExtToEmpleado
	public Empleado EmpleadoExtToEmpleado(EmpleadoExt pojoTarget){
		Empleado pojoResult = new Empleado();
		
		try {
			pojoResult.setId( pojoTarget.getId()  );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			
			pojoResult.setIdArea(pojoTarget.getArea().getId());
			pojoResult.setIdPuestoCategoria( pojoTarget.getPuestoCategoria().getId());
			pojoResult.setFechaIngreso( pojoTarget.getFechaIngreso() );
			pojoResult.setNumeroSeguridadSocial( pojoTarget.getNumeroSeguridadSocial() );
			pojoResult.setNombreCasoEmergencia( pojoTarget.getNombreCasoEmergencia() );
			pojoResult.setExterno( pojoTarget.getExterno() );
			pojoResult.setIdPersona(pojoTarget.getPersona().getId());
			
			//Los campos que Augusto me pidio agregar a la tabla empleados
			pojoResult.setHomonimo( pojoTarget.getHomonimo() );
			pojoResult.setNombre( pojoTarget.getNombre() );
			pojoResult.setPrimerNombre( pojoTarget.getPrimerNombre() );
			pojoResult.setSegundoNombre( pojoTarget.getSegundoNombre() );
			pojoResult.setNombresPropios( pojoTarget.getNombresPropios() );
			pojoResult.setPrimerApellido( pojoTarget.getPrimerApellido() );
			pojoResult.setSegundoApellido( pojoTarget.getSegundoApellido() );
			
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			pojoResult.setIdSucursal(pojoTarget.getSucursal().getId());
			pojoResult.setClave(pojoTarget.getClave());			
			pojoResult.setEmail(pojoTarget.getEmail());
			
			pojoResult.setAreaDescripcion(pojoTarget.getAreaDescripcion());
			pojoResult.setCategoriaDescripcion(pojoTarget.getCategoriaDescripcion());
			pojoResult.setPuestoDescripcion(pojoTarget.getPuestoDescripcion());
			pojoResult.setNombreSucursal(pojoTarget.getNombreSucursal());
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public EmpleadoExt EmpleadoToEmpleadoExt(Empleado pojoTarget){ 
		EmpleadoExt pojoResult = new EmpleadoExt();
		
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			
			if ( pojoTarget.getIdArea() > 0 ){
				Area area = this.ifzArea.findById( pojoTarget.getIdArea() );
				pojoResult.setArea( area );
			}
			if( pojoTarget.getIdPuestoCategoria() > 0 ){
				if ( pojoTarget.getIdPuestoCategoria() > 0 ){
					PuestoCategoria pc = this.ifzPuestoCategoria.findById(pojoTarget.getIdPuestoCategoria() );
					PuestoCategoriaExt pce = this.PuestoCategoriaToPuestoCategoriaExt(pc);
					pojoResult.setPuestoCategoria( pce );
				}
				
			}
			pojoResult.setFechaIngreso( pojoTarget.getFechaIngreso() );
			pojoResult.setNumeroSeguridadSocial( pojoTarget.getNumeroSeguridadSocial() );
			pojoResult.setNombreCasoEmergencia( pojoTarget.getNombreCasoEmergencia() );
			pojoResult.setExterno( pojoTarget.getExterno() );
			if( pojoTarget.getIdPersona() > 0 ){
				
				Persona persona = this.ifzPersona.findById( pojoTarget.getIdPersona() );
				pojoResult.setPersona( persona );
			}

			//Los campos que Augusto me pidio agregar a la tabla empleados
			pojoResult.setHomonimo( pojoTarget.getHomonimo() );
			pojoResult.setNombre( pojoTarget.getNombre() );
			pojoResult.setPrimerNombre( pojoTarget.getPrimerNombre() );
			pojoResult.setSegundoNombre( pojoTarget.getSegundoNombre() );
			pojoResult.setNombresPropios( pojoTarget.getNombresPropios() );
			pojoResult.setPrimerApellido( pojoTarget.getPrimerApellido() );
			pojoResult.setSegundoApellido( pojoTarget.getSegundoApellido() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
			if(pojoTarget.getIdSucursal()>0){
				pojoResult.setSucursal( this.ifzSucursal.findById(pojoTarget.getIdSucursal()) );
			}
			
			pojoResult.setClave(pojoTarget.getClave());
			pojoResult.setEmail(pojoTarget.getEmail());
			pojoResult.setAreaDescripcion(pojoTarget.getAreaDescripcion());
			pojoResult.setCategoriaDescripcion(pojoTarget.getCategoriaDescripcion());
			pojoResult.setPuestoDescripcion(pojoTarget.getPuestoDescripcion());
			pojoResult.setNombreSucursal(pojoTarget.getNombreSucursal());
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}

	public EmpleadoBeneficiario EmpBeneficiarioExtToEmpBeneficiario(EmpleadoBeneficiarioExt pojoTarget){
		EmpleadoBeneficiario pojoResult = new EmpleadoBeneficiario();
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setIdEmpleado( pojoTarget.getEmpleado().getId().intValue() );
			pojoResult.setIdPersona( (int) pojoTarget.getPersona().getId() );
			pojoResult.setPorcentaje( pojoTarget.getPorcentaje() );
			pojoResult.setIdRelacion( (int) pojoTarget.getRelacion().getId() );
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public EmpleadoBeneficiarioExt EmpBeneficiarioToEmpBeneficiarioExt(EmpleadoBeneficiario pojoTarget){
		EmpleadoBeneficiarioExt pojoResult = new EmpleadoBeneficiarioExt(); 
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			if( pojoTarget.getIdEmpleado() > 0 ){
				Empleado empleado = this.ifzEmpleado.findById( pojoTarget.getIdEmpleado() );
				pojoResult.setEmpleado( this.EmpleadoToEmpleadoExt(empleado) );
			}
			if( pojoTarget.getIdPersona() > 0 ){
				Persona persona = this.ifzPersona.findById( pojoTarget.getIdPersona() );
				pojoResult.setPersona( persona );				
			}
			pojoResult.setPorcentaje( pojoTarget.getPorcentaje() );
			if( pojoTarget.getIdRelacion() > 0 ){
				ConValores convalores = this.ifzConValores.findById( pojoTarget.getIdRelacion() );
				pojoResult.setRelacion( convalores );
			}
			
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public EmpleadoPariente EmpleadoParienteExtToEmpleadoPariente(EmpleadoParienteExt pojoTarget){
		EmpleadoPariente pojoResult = new EmpleadoPariente();
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setIdEmpleado( pojoTarget.getEmpleado().getId().intValue() );
			pojoResult.setIdPersona( (int) pojoTarget.getPersona().getId() );
			pojoResult.setIdRelacion( (int) pojoTarget.getRelacion().getId() );
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public EmpleadoParienteExt EmpleadoParienteToEmpleadoParienteExt(EmpleadoPariente pojoTarget){
		EmpleadoParienteExt pojoResult = new EmpleadoParienteExt();
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			if( pojoTarget.getIdEmpleado() > 0 ){
				Empleado empleado = this.ifzEmpleado.findById( pojoTarget.getIdEmpleado() );
				EmpleadoExt empleadoExt = this.EmpleadoToEmpleadoExt( empleado );
				pojoResult.setEmpleado( empleadoExt );
			}
			if( pojoTarget.getIdPersona() > 0 ){
				Persona persona = this.ifzPersona.findById( pojoTarget.getIdPersona() );
				pojoResult.setPersona( persona );				
			}
			if( pojoTarget.getIdRelacion() > 0 ){
				ConValores convalores = this.ifzConValores.findById( pojoTarget.getIdRelacion() );
				pojoResult.setRelacion( convalores );
			}
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public EmpleadoReferencia EmpleadoReferenciaExtToEmpleadoReferencia(EmpleadoReferenciaExt pojoTarget){
		EmpleadoReferencia pojoResult = new EmpleadoReferencia();
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setIdEmpleado( pojoTarget.getEmpleado().getId().intValue() );
			pojoResult.setIdPersona( (int) pojoTarget.getPersona().getId() );
			pojoResult.setTiempoDeConocerlo( pojoTarget.getTiempoDeConocerlo() );
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public EmpleadoReferenciaExt EmpleadoReferenciaToEmpleadoReferenciaExt(EmpleadoReferencia pojoTarget){
		EmpleadoReferenciaExt pojoResult = new EmpleadoReferenciaExt();
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			if( pojoTarget.getIdEmpleado() > 0 ){
				Empleado empleado = this.ifzEmpleado.findById( pojoTarget.getIdEmpleado() );
				pojoResult.setEmpleado( empleado );
			}
			if( pojoTarget.getIdPersona() > 0 ){
				Persona persona = this.ifzPersona.findById( pojoTarget.getIdPersona() );
				pojoResult.setPersona( persona );				
			}
			pojoResult.setTiempoDeConocerlo( pojoTarget.getTiempoDeConocerlo() );
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public EmpleadoTrabajoAnterior EmpleadoTrabajoAnteriorExtToEmpleadoTrabajoAnterior(EmpleadoTrabajoAnteriorExt pojoTarget){
		EmpleadoTrabajoAnterior pojoResult = new EmpleadoTrabajoAnterior();
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setIdEmpleado( pojoTarget.getEmpleado().getId().intValue() );
			pojoResult.setTiempo( pojoTarget.getTiempo() );
			pojoResult.setCompania( pojoTarget.getCompania() );
			pojoResult.setDireccion( pojoTarget.getDireccion() );
			pojoResult.setPuesto( pojoTarget.getPuesto() );
			pojoResult.setTelefono( pojoTarget.getTelefono() );
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public EmpleadoTrabajoAnteriorExt EmpleadoTrabajoAnteriorToEmpleadoTrabajoAnteriorExt(EmpleadoTrabajoAnterior pojoTarget){
		EmpleadoTrabajoAnteriorExt pojoResult = new EmpleadoTrabajoAnteriorExt();
		try {
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			if( pojoTarget.getIdEmpleado() > 0 ){
				Empleado empleado = this.ifzEmpleado.findById( pojoTarget.getIdEmpleado() );
				pojoResult.setEmpleado( empleado );
			}
			pojoResult.setTiempo( pojoTarget.getTiempo() );
			pojoResult.setCompania( pojoTarget.getCompania() );
			pojoResult.setDireccion( pojoTarget.getDireccion() );
			pojoResult.setPuesto( pojoTarget.getPuesto() );
			pojoResult.setTelefono( pojoTarget.getTelefono() );
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	//PuestoCategoria
	public PuestoCategoria PuestoCategoriaExtToPuestoCategoria(PuestoCategoriaExt pojoTarget){
		PuestoCategoria pojoResult = new PuestoCategoria();
		try {
			
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setIdPuesto( pojoTarget.getPuesto().getId().intValue() );
			pojoResult.setIdCategoria( pojoTarget.getCategoria().getId().intValue() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public PuestoCategoriaExt PuestoCategoriaToPuestoCategoriaExt(PuestoCategoria pojoTarget){
		PuestoCategoriaExt pojoResult = new PuestoCategoriaExt();
		try {
			
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			
			if(pojoTarget.getIdPuesto() > 0){
				Puesto puesto = this.ifzPuesto.findById( pojoTarget.getIdPuesto() )  ;
				pojoResult.setPuesto(puesto);
			}
			if(pojoTarget.getIdCategoria() > 0){
				Categoria categoria = this.ifzCategoria.findById( pojoTarget.getIdCategoria() ) ;
				pojoResult.setCategoria(categoria);
			}
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	//Puesto
	public Puesto PuestoExtToPuesto(PuestoExt pojoTarget){
		Puesto pojoResult = new Puesto();
		try {
			
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public PuestoExt PuestoToPuestoExt(Puesto pojoTarget){
		PuestoExt pojoResult = new PuestoExt();
		try {
			//Esta clase no necesita conversion
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	//EmpleadoContrato
	public EmpleadoContratoExt EmpleadoContratoToEmpleadoContratoExt(EmpleadoContrato pojoTarget){
		EmpleadoContratoExt pojoResult = new EmpleadoContratoExt();
		try {
			pojoResult.setId( pojoTarget.getId() );
			if(pojoTarget.getIdEmpleado() != null && pojoTarget.getIdEmpleado() > 0){
				Empleado entity = this.ifzEmpleado.findById( pojoTarget.getId() );
				pojoResult.setEmpleado( this.EmpleadoToEmpleadoExt(entity) );
			}
			pojoResult.setFechaInicio( pojoTarget.getFechaInicio() );
			pojoResult.setFechaFin( pojoTarget.getFechaFin() );
			pojoResult.setSueldo( pojoTarget.getSueldo() );
			pojoResult.setPeridiocidadPago( pojoTarget.getPeridiocidadPago() );
			pojoResult.setDiaDescanso( pojoTarget.getDiaDescanso() );
			pojoResult.setCentroTrabajo( pojoTarget.getCentroTrabajo() );
			pojoResult.setDeterminado( pojoTarget.getDeterminado() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			pojoResult.setHoraEntrada(pojoTarget.getHoraEntrada());
			pojoResult.setHoraSalida(pojoTarget.getHoraSalida());
			pojoResult.setHoraEntradaComplemento(pojoTarget.getHoraEntradaComplemento());
			pojoResult.setHoraSalidaComplemento(pojoTarget.getHoraSalidaComplemento());
			pojoResult.setDescuentoInfonavit(pojoTarget.getDescuentoInfonavit());
			pojoResult.setTipoHorario(pojoTarget.getTipoHorario());
			pojoResult.setSueldoHora( pojoTarget.getSueldoHora() );
			pojoResult.setSueldoHoraExtra( pojoTarget.getSueldoHoraExtra() );
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			
			if (pojoTarget.getFormaPago() != null && pojoTarget.getFormaPago() > 0L) {
				ConValores entity = this.ifzConValores.findById(pojoTarget.getFormaPago());
				if (entity == null)
					entity = new ConValores();
				pojoResult.setFormaPago(entity);
			}
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public EmpleadoContrato EmpleadoContratoExtToEmpleadoContrato(EmpleadoContratoExt pojoTarget){
		EmpleadoContrato pojoResult = new EmpleadoContrato();
		try {
			pojoResult.setId( pojoTarget.getId() );
			if(pojoTarget.getEmpleado() != null) {
				pojoResult.setIdEmpleado( pojoTarget.getEmpleado().getId() );
			}
			pojoResult.setFechaInicio( pojoTarget.getFechaInicio() );
			pojoResult.setFechaFin( pojoTarget.getFechaFin() );
			pojoResult.setSueldo( pojoTarget.getSueldo() );
			pojoResult.setPeridiocidadPago( pojoTarget.getPeridiocidadPago() );
			pojoResult.setDiaDescanso( pojoTarget.getDiaDescanso() );
			pojoResult.setCentroTrabajo( pojoTarget.getCentroTrabajo() );
			pojoResult.setDeterminado( pojoTarget.getDeterminado() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			pojoResult.setDescuentoInfonavit(pojoTarget.getDescuentoInfonavit());
			pojoResult.setHoraEntrada(pojoTarget.getHoraEntrada());
			pojoResult.setHoraSalida(pojoTarget.getHoraSalida());
			pojoResult.setHoraEntradaComplemento(pojoTarget.getHoraEntradaComplemento());
			pojoResult.setHoraSalidaComplemento(pojoTarget.getHoraSalidaComplemento());
			pojoResult.setTipoHorario(pojoTarget.getTipoHorario());
			pojoResult.setSueldoHora( pojoTarget.getSueldoHora() );
			pojoResult.setSueldoHoraExtra( pojoTarget.getSueldoHoraExtra() );
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setFormaPago(0L);
			
			if (pojoTarget.getFormaPago() != null && pojoTarget.getFormaPago().getId() > 0L) {
				pojoResult.setFormaPago(pojoTarget.getFormaPago().getId());
			}
			
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public EmpleadoFiniquitoExt EmpleadoFiniquitoToEmpleadoFiniquitoExt(EmpleadoFiniquito pojoTarget){
		EmpleadoFiniquitoExt pojoResult = new EmpleadoFiniquitoExt();
		
		try{
			pojoResult.setId( pojoTarget.getId() );
			if(pojoTarget.getIdEmpleado() > 0){
				Empleado e = this.ifzEmpleado.findById( pojoTarget.getIdEmpleado() );
				pojoResult.setEmpleado( this.EmpleadoToEmpleadoExt(e) );
			}
			pojoResult.setFechaSolicitudBaja( pojoTarget.getFechaSolicitudBaja() );
			pojoResult.setSolicitadoPor( pojoTarget.getSolicitadoPor() );
			pojoResult.setFechaElaboracionEnvio( pojoTarget.getFechaElaboracionEnvio() );
			pojoResult.setFirmaRenuncia( pojoTarget.getFirmaRenuncia() );
			pojoResult.setVoBoRh( pojoTarget.getVoBoRh() );
			pojoResult.setAprobacion( pojoTarget.getAprobacion() );
			pojoResult.setObservaciones( pojoTarget.getObservaciones() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
			pojoResult.setMonto(pojoTarget.getMonto());
			
		}catch(Exception e){
			throw e;
		}
		return pojoResult;
	}
	
	public EmpleadoFiniquito EmpleadoFiniquitoExtToEmpleadoFiniquito(EmpleadoFiniquitoExt pojoTarget){
		EmpleadoFiniquito pojoResult = new EmpleadoFiniquito();
		
		try{
			pojoResult.setId( pojoTarget.getId() );
			pojoResult.setIdEmpleado( pojoTarget.getEmpleado().getId() );
			pojoResult.setFechaSolicitudBaja( pojoTarget.getFechaSolicitudBaja() );
			pojoResult.setSolicitadoPor( pojoTarget.getSolicitadoPor() );
			pojoResult.setFechaElaboracionEnvio( pojoTarget.getFechaElaboracionEnvio() );
			pojoResult.setFirmaRenuncia( pojoTarget.getFirmaRenuncia() );
			pojoResult.setVoBoRh( pojoTarget.getVoBoRh() );
			pojoResult.setAprobacion( pojoTarget.getAprobacion() );
			pojoResult.setObservaciones( pojoTarget.getObservaciones() );
			pojoResult.setCreadoPor( pojoTarget.getCreadoPor() );
			pojoResult.setFechaCreacion( pojoTarget.getFechaCreacion() );
			pojoResult.setModificadoPor( pojoTarget.getModificadoPor() );
			pojoResult.setFechaModificacion( pojoTarget.getFechaModificacion() );
			pojoResult.setEstatus( pojoTarget.getEstatus() );
			
			pojoResult.setMonto(pojoTarget.getMonto());
			
		}catch(Exception e){
			throw e;
		}
		
		return pojoResult;
	}
	
	
	// --------------------------------------------------------------------------------------------->
	
	// Checador
	public Checador ChecadorExtToChecador(ChecadorExt pojoTarget) {
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: From " + this.from + " ::  ChecadorExtToChecador");
		Checador pojoResult = new Checador();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdObra(pojoTarget.getIdObra());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setNombreArchivo(pojoTarget.getNombreArchivo());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			//pojoResult.setIdObra(0L);
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos terminado");
			
			/*if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0L) {
				pojoResult.setIdObra(pojoTarget.getIdObra().getId());
				if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Obra terminado");
			}*/
		} catch (Exception ex) {
			System.out.println("Logica_RecHum.ConvertExt :: ERROR al convertir ChecadorExtToChecador :: " + ex.getMessage());
			log.error("Error en Logica_RecHum.ConvertExt :: ERROR al convertir ChecadorExtToChecador", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: ChecadorExtToChecador :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ChecadorExt ChecadorToChecadorExt(Checador pojoTarget) {
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: From " + this.from + " ::  ChecadorToChecadorExt");
		ChecadorExt pojoResult = new ChecadorExt();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdObra(pojoTarget.getIdObra());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setNombreArchivo(pojoTarget.getNombreArchivo());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos terminado");

			/*if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra() > 0L) {
				ObraExt pojoAux = this.ifzObras.findByIdExt(pojoTarget.getIdObra());
				
				if (pojoAux == null)
					pojoAux = new ObraExt();
				
				pojoResult.setIdObra(pojoAux);
				if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Obra terminado");
			}*/
		} catch (Exception ex) {
			System.out.println("Logica_RecHum.ConvertExt :: ERROR al convertir ChecadorToChecadorExt :: " + ex.getMessage());
			log.error("Error en Logica_RecHum.ConvertExt :: ERROR al convertir ChecadorToChecadorExt", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: ChecadorToChecadorExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ChecadorDetalle ChecadorDetalleExtToChecadorDetalle(ChecadorDetalleExt pojoTarget) {
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: From " + this.from + " ::  ChecadorDetalleExtToChecadorDetalle");
		ChecadorDetalle pojoResult = new ChecadorDetalle();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setNombreEmpleado(pojoTarget.getNombreEmpleado());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setHoraEntrada(pojoTarget.getHoraEntrada());
			pojoResult.setHoraSalida(pojoTarget.getHoraSalida());
			pojoResult.setHorasTrabajadas(pojoTarget.getHorasTrabajadas());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setHorasExtra(pojoTarget.getHorasExtra());
			pojoResult.setHorasExtraAutorizadas(pojoTarget.getHorasExtraAutorizadas());
			pojoResult.setUsuarioAutoriza(pojoTarget.getUsuarioAutoriza());
			//pojoResult.setIdChecador(0L);
			pojoResult.setIdEmpleado(0L);
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Checador");
			if (pojoTarget.getIdChecador() != null && pojoTarget.getIdChecador().getId() != null && pojoTarget.getIdChecador().getId() > 0L) {
				pojoResult.setIdChecador(this.ChecadorExtToChecador(pojoTarget.getIdChecador()));
				if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Checador terminado");
			}
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Empleado");
			if (pojoTarget.getIdEmpleado() != null && pojoTarget.getIdEmpleado().getId() != null && pojoTarget.getIdEmpleado().getId() > 0L) {
				pojoResult.setIdEmpleado(pojoTarget.getIdEmpleado().getId());
				if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Empleado terminado");
			}
		} catch (Exception ex) {
			System.out.println("Logica_RecHum.ConvertExt :: ERROR al convertir ChecadorDetalleExtToChecadorDetalle :: " + ex.getMessage());
			log.error("Error en Logica_RecHum.ConvertExt :: ERROR al convertir ChecadorDetalleExtToChecadorDetalle", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: ChecadorDetalleExtToChecadorDetalle :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ChecadorDetalleExt ChecadorDetalleToChecadorDetalleExt(ChecadorDetalle pojoTarget) {
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: From " + this.from + " ::  ChecadorDetalleExtToChecadorDetalle");
		ChecadorDetalleExt pojoResult = new ChecadorDetalleExt();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setNombreEmpleado(pojoTarget.getNombreEmpleado());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setHoraEntrada(pojoTarget.getHoraEntrada());
			pojoResult.setHoraSalida(pojoTarget.getHoraSalida());
			pojoResult.setHorasTrabajadas(pojoTarget.getHorasTrabajadas());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setHorasExtra(pojoTarget.getHorasExtra());
			pojoResult.setHorasExtraAutorizadas(pojoTarget.getHorasExtraAutorizadas());
			pojoResult.setUsuarioAutoriza(pojoTarget.getUsuarioAutoriza());
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Checador");
			if (pojoTarget.getIdChecador() != null && pojoTarget.getIdChecador().getId() > 0L) {
				/*Checador pojoAux = this.ifzChecador.findById(pojoTarget.getIdChecador());
				
				if (pojoAux == null)
					pojoAux = new Checador();*/
				
				pojoResult.setIdChecador(this.ChecadorToChecadorExt(pojoTarget.getIdChecador()));
				if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Checador terminado");
			}
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Empleado");
			if (pojoTarget.getIdEmpleado() != null && pojoTarget.getIdEmpleado() > 0L) {
				Empleado pojoAux = this.ifzEmpleado.findById(pojoTarget.getIdEmpleado());
				
				if (pojoAux == null)
					pojoAux = new Empleado();
				
				pojoResult.setIdEmpleado(this.EmpleadoToEmpleadoExt(pojoAux));
				if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Empleado terminado");
			}
		} catch (Exception ex) {
			System.out.println("Logica_RecHum.ConvertExt :: ERROR al convertir ChecadorDetalleExtToChecadorDetalle :: " + ex.getMessage());
			log.error("Error en Logica_RecHum.ConvertExt :: ERROR al convertir ChecadorDetalleExtToChecadorDetalle", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: ChecadorDetalleExtToChecadorDetalle :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	
	// EmpleadoDescuento
	public EmpleadoDescuento EmpleadoDescuentoExtToEmpleadoDescuento(EmpleadoDescuentoExt pojoTarget) {
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: From " + this.from + " ::  EmpleadoDescuentoExtToEmpleadoDescuento");
		EmpleadoDescuento pojoResult = new EmpleadoDescuento();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdEmpleado(pojoTarget.getIdEmpleado());
			pojoResult.setNumeroPagos(pojoTarget.getNumeroPagos());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			//pojoResult.setIdRegistro(pojoTarget.getIdRegistro());
			pojoResult.setIdDescuento(0L);
			pojoResult.setIdPeriodo(0L);
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Descuento");
			if (pojoTarget.getIdDescuento() != null && pojoTarget.getIdDescuento().getId() > 0L) {
				pojoResult.setIdDescuento(pojoTarget.getIdDescuento().getId());
				if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Descuento terminado");
			}
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Periodo");
			if (pojoTarget.getIdPeriodo() != null && pojoTarget.getIdPeriodo().getId() > 0L) {
				pojoResult.setIdPeriodo(pojoTarget.getIdPeriodo().getId());
				if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Periodo terminado");
			}
		} catch (Exception ex) {
			System.out.println("Logica_RecHum.ConvertExt :: ERROR al convertir EmpleadoDescuentoExtToEmpleadoDescuento :: " + ex.getMessage());
			log.error("Error en Logica_RecHum.ConvertExt :: ERROR al convertir EmpleadoDescuentoExtToEmpleadoDescuento", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: EmpleadoDescuentoExtToEmpleadoDescuento :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public EmpleadoDescuentoExt EmpleadoDescuentoToEmpleadoDescuentoExt(EmpleadoDescuento pojoTarget) {
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: From " + this.from + " ::  EmpleadoDescuentoToEmpleadoDescuentoExt");
		EmpleadoDescuentoExt pojoResult = new EmpleadoDescuentoExt();
		
		try {
			if (pojoTarget == null)
				return null;
			
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdEmpleado(pojoTarget.getIdEmpleado());
			pojoResult.setNumeroPagos(pojoTarget.getNumeroPagos());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			//pojoResult.setIdRegistro(pojoTarget.getIdRegistro());
			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando de tipos nativos terminado");

			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Descuento");
			if (pojoTarget.getIdDescuento() != null && pojoTarget.getIdDescuento() > 0L) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getIdDescuento());
				
				if (pojoAux == null)
					pojoAux = new ConValores();
				
				pojoResult.setIdDescuento(pojoAux);
				if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Descuento terminado");
			}

			if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Periodo");
			if (pojoTarget.getIdPeriodo() != null && pojoTarget.getIdPeriodo() > 0L) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getIdPeriodo());
				
				if (pojoAux == null)
					pojoAux = new ConValores();
				
				pojoResult.setIdPeriodo(pojoAux);
				if (this.mostrarSystemOut) System.out.println("---- -- ----> Comprobando Periodo terminado");
			}
		} catch (Exception ex) {
			System.out.println("Logica_RecHum.ConvertExt :: ERROR al convertir EmpleadoDescuentoToEmpleadoDescuentoExt :: " + ex.getMessage());
			log.error("Error en Logica_RecHum.ConvertExt :: ERROR al convertir EmpleadoDescuentoToEmpleadoDescuentoExt", ex);
			throw ex;
		}
		
		if (this.mostrarSystemOut) System.out.println("ConvertExt :: EmpleadoDescuentoToEmpleadoDescuentoExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
}
