package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.Puesto;
import net.giro.rh.admon.beans.PuestoExt;
import net.giro.rh.admon.dao.PuestoDAO;

@Stateless
public class PuestoFac implements PuestoRem {

	private static Logger log = Logger.getLogger(PuestoFac.class);
	
	InitialContext ctx;
	private PuestoDAO ifzPuesto;
	private ConvertExt convertidor;
	
	public PuestoFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//PuestoImp!net.giro.rh.admon.dao.PuestoDAO";
            this.ifzPuesto = (PuestoDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear PuestoFac", e);
			ctx = null;
		}
	}
	
	public Long save(Puesto entity) throws ExcepConstraint {
		try {
			return this.ifzPuesto.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(PuestoExt entityExt) throws ExcepConstraint {
		try {
			Puesto entity = this.convertidor.PuestoExtToPuesto(entityExt);
			return this.ifzPuesto.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(Puesto entity) throws ExcepConstraint {
		try {
			this.ifzPuesto.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(PuestoExt entityExt) throws ExcepConstraint {
		try {
			Puesto entity = this.convertidor.PuestoExtToPuesto(entityExt);
			this.ifzPuesto.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Puesto update(Puesto entity) throws ExcepConstraint {
		try {
			this.ifzPuesto.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Puesto update(PuestoExt entityExt) throws ExcepConstraint {
		try {
			Puesto entity = this.convertidor.PuestoExtToPuesto(entityExt);
			this.ifzPuesto.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Puesto findById(Long id) {
		try {
			return this.ifzPuesto.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<Puesto> findByProperty(String propertyName, final Object value) {
		//System.out.println("Pasando por logica findbyproperty de Puesto");
		try {
			return this.ifzPuesto.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Puesto> findAll() {
		try {
			return this.ifzPuesto.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<Puesto> findAllActivos() {
		try {
			return this.ifzPuesto.findAllActivos();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<Puesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			//System.out.println("---> LOGICA :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<Puesto> lista = this.ifzPuesto.findByPropertyPojoCompleto(propertyName, tipo, value);
			//System.out.println("---> LOGICA :: listFactura(" + lista.size() + ")");
			return lista;
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	public List<PuestoExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<PuestoExt> listaExt = new ArrayList<PuestoExt>();
		
		try{
			System.out.println("---> LOGICA PuestoEXT :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<Puesto> lista = this.ifzPuesto.findByPropertyPojoCompleto(propertyName, tipo, value);
			System.out.println("---> LOGICA PuestoEXT :: Lista :: " + lista.size());
			
			for (Puesto Puesto : lista) {
				PuestoExt pojoAux = this.convertidor.PuestoToPuestoExt(Puesto);
				System.out.println("---> LOGICA :: FOR :: pojoAux :: " + "pojoAux.getId() <-- no implementado" + " :: " );
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					System.out.println("No se agregó este item a la lista: "+Puesto.getId() );
			}
			System.out.println("---> LOGICA :: Lista Extendida :: " + listaExt.size());
		}catch(RuntimeException re){
			throw re;
		}

		return listaExt;
	}
}
