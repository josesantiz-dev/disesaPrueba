
package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoTrabajoAnterior;
import net.giro.rh.admon.beans.EmpleadoTrabajoAnteriorExt;
import net.giro.rh.admon.dao.EmpleadoTrabajoAnteriorDAO;

@Stateless
public class EmpleadoTrabajoAnteriorFac implements EmpleadoTrabajoAnteriorRem {
	
	private static Logger log = Logger.getLogger(EmpleadoTrabajoAnteriorFac.class);
	
	InitialContext ctx;
	private EmpleadoTrabajoAnteriorDAO ifzEmpleadoTrabajoAnterior;
	private ConvertExt convertidor;
	
	public EmpleadoTrabajoAnteriorFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//EmpleadoTrabajoAnteriorImp!net.giro.rh.admon.dao.EmpleadoTrabajoAnteriorDAO";
            this.ifzEmpleadoTrabajoAnterior = (EmpleadoTrabajoAnteriorDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear EmpleadoTrabajoAnteriorFac", e);
			ctx = null;
		}
	}
	
	public Long save(EmpleadoTrabajoAnterior entity) throws ExcepConstraint {
		try {
			return this.ifzEmpleadoTrabajoAnterior.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(EmpleadoTrabajoAnteriorExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoTrabajoAnterior entity = this.convertidor.EmpleadoTrabajoAnteriorExtToEmpleadoTrabajoAnterior(entityExt);
			return this.ifzEmpleadoTrabajoAnterior.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoTrabajoAnterior entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoTrabajoAnterior.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoTrabajoAnteriorExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoTrabajoAnterior entity = this.convertidor.EmpleadoTrabajoAnteriorExtToEmpleadoTrabajoAnterior(entityExt);
			this.ifzEmpleadoTrabajoAnterior.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoTrabajoAnterior update(EmpleadoTrabajoAnterior entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoTrabajoAnterior.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoTrabajoAnterior update(EmpleadoTrabajoAnteriorExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoTrabajoAnterior entity = this.convertidor.EmpleadoTrabajoAnteriorExtToEmpleadoTrabajoAnterior(entityExt);
			this.ifzEmpleadoTrabajoAnterior.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoTrabajoAnterior findById(Long id) {
		try {
			return this.ifzEmpleadoTrabajoAnterior.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<EmpleadoTrabajoAnterior> findByProperty(String propertyName, final Object value) {
		System.out.println("Pasando por logica findbyproperty de EmpleadoTrabajoAnterior");
		try {
			return this.ifzEmpleadoTrabajoAnterior.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<EmpleadoTrabajoAnterior> findAll() {
		try {
			return this.ifzEmpleadoTrabajoAnterior.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<EmpleadoTrabajoAnterior> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			//System.out.println("---> LOGICA :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<EmpleadoTrabajoAnterior> lista = this.ifzEmpleadoTrabajoAnterior.findByPropertyPojoCompleto(propertyName, tipo, value);
			//System.out.println("---> LOGICA :: listFactura(" + lista.size() + ")");
			return lista;
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	public List<EmpleadoTrabajoAnteriorExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<EmpleadoTrabajoAnteriorExt> listaExt = new ArrayList<EmpleadoTrabajoAnteriorExt>();
		
		try{
			System.out.println("---> LOGICA EmpleadoTrabajoAnteriorEXT :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<EmpleadoTrabajoAnterior> lista = this.ifzEmpleadoTrabajoAnterior.findByPropertyPojoCompleto(propertyName, tipo, value);
			System.out.println("---> LOGICA EmpleadoTrabajoAnteriorEXT :: Lista :: " + lista.size());
			
			for (EmpleadoTrabajoAnterior EmpleadoTrabajoAnterior : lista) {
				EmpleadoTrabajoAnteriorExt pojoAux = this.convertidor.EmpleadoTrabajoAnteriorToEmpleadoTrabajoAnteriorExt(EmpleadoTrabajoAnterior);
				System.out.println("---> LOGICA :: FOR :: pojoAux :: " + "pojoAux.getId() <-- no implementado"+ " :: " );
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					System.out.println("No se agregó este item a la lista: "+EmpleadoTrabajoAnterior.getId() );
			}
			System.out.println("---> LOGICA :: Lista Extendida :: " + listaExt.size());
		}catch(RuntimeException re){
			throw re;
		}

		return listaExt;
	}
}
