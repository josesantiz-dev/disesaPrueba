package net.giro.rh.admon.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoContratoExt;
import net.giro.rh.admon.dao.EmpleadoContratoDAO;

@Stateless
public class EmpleadoContratoFac implements EmpleadoContratoRem{
	
	private static Logger log = Logger.getLogger(EmpleadoContratoFac.class);

	InitialContext ctx;
	
	private EmpleadoContratoDAO ifzEmpleadoContrato;

	private ConvertExt convertidor;
	
	public EmpleadoContratoFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//EmpleadoContratoImp!net.giro.rh.admon.dao.EmpleadoContratoDAO";
            this.ifzEmpleadoContrato = (EmpleadoContratoDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear EmpleadoFac", e);
			ctx = null;
		}
	}
	
	public Long save(EmpleadoContrato entity) throws ExcepConstraint {
		try {
			return this.ifzEmpleadoContrato.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(EmpleadoContratoExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoContrato entity = this.convertidor.EmpleadoContratoExtToEmpleadoContrato(entityExt);
			return this.ifzEmpleadoContrato.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoContrato entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoContrato.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoContratoExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoContrato entity = this.convertidor.EmpleadoContratoExtToEmpleadoContrato(entityExt);
			this.ifzEmpleadoContrato.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoContrato update(EmpleadoContrato entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoContrato.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoContrato update(EmpleadoContratoExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoContrato entity = this.convertidor.EmpleadoContratoExtToEmpleadoContrato(entityExt);
			this.ifzEmpleadoContrato.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public EmpleadoContrato findById(Long id) {
		try {
			return this.ifzEmpleadoContrato.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public EmpleadoContratoExt findByIdExt(Long id) {
		try {
			EmpleadoContrato empleado = this.findById(id);
			return  this.convertidor.EmpleadoContratoToEmpleadoContratoExt(empleado); //this.ifzEmpleado.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<EmpleadoContrato> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleadoContrato.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<EmpleadoContrato> findAll() {
		try {
			return this.ifzEmpleadoContrato.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<EmpleadoContrato> findAllByIdEmpleado(long idEmpleado) {
		try {
			return this.ifzEmpleadoContrato.findAllByIdEmpleado(idEmpleado);
			
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	
	@Override
	public List<EmpleadoContrato> contratoValido(long idEmpleado) throws Exception {
		try {
			return this.ifzEmpleadoContrato.contratoValido(idEmpleado);
		} catch (RuntimeException re) {		
			throw re;
		}
	}
}
