package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.beans.EmpleadoFiniquitoExt;
import net.giro.rh.admon.dao.EmpleadoFiniquitoDAO;


@Stateless
public class EmpleadoFiniquitoFac implements EmpleadoFiniquitoRem{


	private static Logger log = Logger.getLogger(EmpleadoFiniquitoFac.class);
	
	InitialContext ctx;
	private EmpleadoFiniquitoDAO ifzEmpleadoFiniquito;
	private ConvertExt convertidor;
	
	public EmpleadoFiniquitoFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//EmpleadoFiniquitoImp!net.giro.rh.admon.dao.EmpleadoFiniquitoDAO";
            this.ifzEmpleadoFiniquito = (EmpleadoFiniquitoDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear EmpleadoFiniquitoFac", e);
			ctx = null;
		}
	}
	
	public Long save(EmpleadoFiniquito entity) throws ExcepConstraint {
		try {
			return this.ifzEmpleadoFiniquito.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(EmpleadoFiniquitoExt entity) throws ExcepConstraint {
		try {
			EmpleadoFiniquito ef = this.convertidor.EmpleadoFiniquitoExtToEmpleadoFiniquito(entity);
			return this.ifzEmpleadoFiniquito.save(ef);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	

	public void delete(EmpleadoFiniquito entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoFiniquito.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	public void delete(EmpleadoFiniquitoExt entity) throws ExcepConstraint {
		try {
			EmpleadoFiniquito ef = this.convertidor.EmpleadoFiniquitoExtToEmpleadoFiniquito(entity);
			this.ifzEmpleadoFiniquito.delete(ef);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public EmpleadoFiniquito update(EmpleadoFiniquito entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoFiniquito.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	public EmpleadoFiniquito update(EmpleadoFiniquitoExt entity) throws ExcepConstraint {
		try {
			EmpleadoFiniquito ef = this.convertidor.EmpleadoFiniquitoExtToEmpleadoFiniquito(entity);
			this.ifzEmpleadoFiniquito.update(ef);
			return ef;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoFiniquito findById(Long id) {
		try {
			return this.ifzEmpleadoFiniquito.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<EmpleadoFiniquito> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleadoFiniquito.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<EmpleadoFiniquito> findAll() {
		try {
			return this.ifzEmpleadoFiniquito.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	

	public List<EmpleadoFiniquitoExt> findAllExt() {
		List<EmpleadoFiniquitoExt> listaExt = new ArrayList<>();
		try {
			List<EmpleadoFiniquito> lista = this.ifzEmpleadoFiniquito.findAll();
			for(EmpleadoFiniquito ef: lista){
				listaExt.add( this.convertidor.EmpleadoFiniquitoToEmpleadoFiniquitoExt(ef) );
			}
		} catch (RuntimeException re) {		
			throw re;
		}
		return listaExt;
	}


	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado) {
		List<EmpleadoFiniquito> lista = new ArrayList<EmpleadoFiniquito>();
		try{
			lista = this.ifzEmpleadoFiniquito.findByEmpleado(nombreEmpleado);
		}catch (RuntimeException re) {		
			throw re;
		}
		return lista;
	}
	
	public List<EmpleadoFiniquitoExt> findByEmpleadoExt(String nombreEmpleado) {
		List<EmpleadoFiniquitoExt> listaExt = new ArrayList<EmpleadoFiniquitoExt>();
		try{
			List<EmpleadoFiniquito> lista = new ArrayList<EmpleadoFiniquito>();
			lista = this.ifzEmpleadoFiniquito.findByEmpleado(nombreEmpleado);
			for(EmpleadoFiniquito ef: lista){
				listaExt.add( this.convertidor.EmpleadoFiniquitoToEmpleadoFiniquitoExt(ef) );
			}
		}catch (RuntimeException re) {		
			throw re;
		}
		return listaExt;
	}
	
}
