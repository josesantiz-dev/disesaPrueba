package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoReferencia;
import net.giro.rh.admon.beans.EmpleadoReferenciaExt;
import net.giro.rh.admon.dao.EmpleadoReferenciaDAO;

@Stateless
public class EmpleadoReferenciaFac implements EmpleadoReferenciaRem {
	
	private static Logger log = Logger.getLogger(EmpleadoReferenciaFac.class);
	
	InitialContext ctx;
	private EmpleadoReferenciaDAO ifzEmpleadoReferencia;
	private ConvertExt convertidor;
	
	public EmpleadoReferenciaFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//EmpleadoReferenciaImp!net.giro.rh.admon.dao.EmpleadoReferenciaDAO";
            this.ifzEmpleadoReferencia = (EmpleadoReferenciaDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear EmpleadoReferenciaFac", e);
			ctx = null;
		}
	}
	
	public Long save(EmpleadoReferencia entity) throws ExcepConstraint {
		try {
			return this.ifzEmpleadoReferencia.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(EmpleadoReferenciaExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoReferencia entity = this.convertidor.EmpleadoReferenciaExtToEmpleadoReferencia(entityExt);
			return this.ifzEmpleadoReferencia.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoReferencia entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoReferencia.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoReferenciaExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoReferencia entity = this.convertidor.EmpleadoReferenciaExtToEmpleadoReferencia(entityExt);
			this.ifzEmpleadoReferencia.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoReferencia update(EmpleadoReferencia entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoReferencia.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoReferencia update(EmpleadoReferenciaExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoReferencia entity = this.convertidor.EmpleadoReferenciaExtToEmpleadoReferencia(entityExt);
			this.ifzEmpleadoReferencia.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoReferencia findById(Long id) {
		try {
			return this.ifzEmpleadoReferencia.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<EmpleadoReferencia> findByProperty(String propertyName, final Object value) {
		System.out.println("Pasando por logica findbyproperty de EmpleadoReferencia");
		try {
			return this.ifzEmpleadoReferencia.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<EmpleadoReferencia> findAll() {
		try {
			return this.ifzEmpleadoReferencia.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<EmpleadoReferencia> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			//System.out.println("---> LOGICA :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<EmpleadoReferencia> lista = this.ifzEmpleadoReferencia.findByPropertyPojoCompleto(propertyName, tipo, value);
			//System.out.println("---> LOGICA :: listFactura(" + lista.size() + ")");
			return lista;
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	public List<EmpleadoReferenciaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<EmpleadoReferenciaExt> listaExt = new ArrayList<EmpleadoReferenciaExt>();
		
		try{
			System.out.println("---> LOGICA EmpleadoReferenciaEXT :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<EmpleadoReferencia> lista = this.ifzEmpleadoReferencia.findByPropertyPojoCompleto(propertyName, tipo, value);
			System.out.println("---> LOGICA EmpleadoReferenciaEXT :: Lista :: " + lista.size());
			
			for (EmpleadoReferencia EmpleadoReferencia : lista) {
				EmpleadoReferenciaExt pojoAux = this.convertidor.EmpleadoReferenciaToEmpleadoReferenciaExt(EmpleadoReferencia);
				System.out.println("---> LOGICA :: FOR :: pojoAux :: " + "pojoAux.getId() sin implementar"+ " :: " );
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					System.out.println("No se agregó este item a la lista: "+EmpleadoReferencia.getId() );
			}
			System.out.println("---> LOGICA :: Lista Extendida :: " + listaExt.size());
		}catch(RuntimeException re){
			throw re;
		}

		return listaExt;
	}
}
