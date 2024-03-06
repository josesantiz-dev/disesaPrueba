package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoPariente;
import net.giro.rh.admon.beans.EmpleadoParienteExt;
import net.giro.rh.admon.dao.EmpleadoParienteDAO;

@Stateless
public class EmpleadoParienteFac implements EmpleadoParienteRem{


	private static Logger log = Logger.getLogger(EmpleadoParienteFac.class);
	
	InitialContext ctx;
	private EmpleadoParienteDAO ifzEmpleadoPariente;
	private ConvertExt convertidor;
	
	public EmpleadoParienteFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//EmpleadoParienteImp!net.giro.rh.admon.dao.EmpleadoParienteDAO";
            this.ifzEmpleadoPariente = (EmpleadoParienteDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear EmpleadoParienteFac", e);
			ctx = null;
		}
	}
	
	public Long save(EmpleadoPariente entity) throws ExcepConstraint {
		try {
			return this.ifzEmpleadoPariente.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(EmpleadoParienteExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoPariente entity = this.convertidor.EmpleadoParienteExtToEmpleadoPariente(entityExt);
			return this.ifzEmpleadoPariente.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoPariente entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoPariente.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoParienteExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoPariente entity = this.convertidor.EmpleadoParienteExtToEmpleadoPariente(entityExt);
			this.ifzEmpleadoPariente.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoPariente update(EmpleadoPariente entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoPariente.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoPariente update(EmpleadoParienteExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoPariente entity = this.convertidor.EmpleadoParienteExtToEmpleadoPariente(entityExt);
			this.ifzEmpleadoPariente.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoPariente findById(Long id) {
		try {
			return this.ifzEmpleadoPariente.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<EmpleadoPariente> findByProperty(String propertyName, final Object value) {
		//System.out.println("Pasando por logica findbyproperty de EmpleadoPariente");
		try {
			return this.ifzEmpleadoPariente.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<EmpleadoPariente> findAll() {
		try {
			return this.ifzEmpleadoPariente.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	

	public List<EmpleadoParienteExt> findByIdEmpleadoParentesco(long idEmpleado) {
		List<EmpleadoParienteExt> listaExt = new ArrayList<>();
		try {
			
			List<EmpleadoPariente> lista = this.ifzEmpleadoPariente.findByIdEmpleadoParentesco(idEmpleado);
			for(EmpleadoPariente ep: lista){
				listaExt.add(  this.convertidor.EmpleadoParienteToEmpleadoParienteExt( ep )  );
			}
		} catch (RuntimeException re) {		
			throw re;
		}
		return listaExt;
	}
	
	public List<EmpleadoPariente> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			//System.out.println("---> LOGICA :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<EmpleadoPariente> lista = this.ifzEmpleadoPariente.findByPropertyPojoCompleto(propertyName, tipo, value);
			//System.out.println("---> LOGICA :: listFactura(" + lista.size() + ")");
			return lista;
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	public List<EmpleadoParienteExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<EmpleadoParienteExt> listaExt = new ArrayList<EmpleadoParienteExt>();
		
		try{
			System.out.println("---> LOGICA EmpleadoParienteEXT :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<EmpleadoPariente> lista = this.ifzEmpleadoPariente.findByPropertyPojoCompleto(propertyName, tipo, value);
			System.out.println("---> LOGICA EmpleadoParienteEXT :: Lista :: " + lista.size());
			
			for (EmpleadoPariente EmpleadoPariente : lista) {
				EmpleadoParienteExt pojoAux = this.convertidor.EmpleadoParienteToEmpleadoParienteExt(EmpleadoPariente);
				System.out.println("---> LOGICA :: FOR :: pojoAux :: " + "pojoAux.getId() sin implementar" + " :: " );
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					System.out.println("No se agregó este item a la lista: "+EmpleadoPariente.getId() );
			}
			System.out.println("---> LOGICA :: Lista Extendida :: " + listaExt.size());
		}catch(RuntimeException re){
			throw re;
		}

		return listaExt;
	}

	
	
}
