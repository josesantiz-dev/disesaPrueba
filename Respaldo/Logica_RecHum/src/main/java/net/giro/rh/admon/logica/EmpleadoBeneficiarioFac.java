package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoBeneficiario;
import net.giro.rh.admon.beans.EmpleadoBeneficiarioExt;
import net.giro.rh.admon.dao.EmpleadoBeneficiarioDAO;

@Stateless
public class EmpleadoBeneficiarioFac implements EmpleadoBeneficiarioRem {
	
private static Logger log = Logger.getLogger(EmpleadoBeneficiarioFac.class);
	
	InitialContext ctx;
	private EmpleadoBeneficiarioDAO ifzEmpleadoBeneficiario;
	private ConvertExt  convertidor;
	
	public EmpleadoBeneficiarioFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//EmpleadoBeneficiarioImp!net.giro.rh.admon.dao.EmpleadoBeneficiarioDAO";
            this.ifzEmpleadoBeneficiario = (EmpleadoBeneficiarioDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear EmpleadoBeneficiarioFac", e);
			ctx = null;
		}
	}
	
	public Long save(EmpleadoBeneficiario entity) throws ExcepConstraint {
		try {
			return this.ifzEmpleadoBeneficiario.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(EmpleadoBeneficiarioExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoBeneficiario entity = this.convertidor.EmpBeneficiarioExtToEmpBeneficiario(entityExt);
			return this.ifzEmpleadoBeneficiario.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoBeneficiario entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoBeneficiario.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoBeneficiarioExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoBeneficiario entity = this.convertidor.EmpBeneficiarioExtToEmpBeneficiario(entityExt);
			this.ifzEmpleadoBeneficiario.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoBeneficiario update(EmpleadoBeneficiario entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoBeneficiario.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoBeneficiario update(EmpleadoBeneficiarioExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoBeneficiario entity = this.convertidor.EmpBeneficiarioExtToEmpBeneficiario(entityExt);
			this.ifzEmpleadoBeneficiario.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public EmpleadoBeneficiario findById(Long id) {
		try {
			return this.ifzEmpleadoBeneficiario.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<EmpleadoBeneficiario> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleadoBeneficiario.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<EmpleadoBeneficiario> findAll() {
		try {
			return this.ifzEmpleadoBeneficiario.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<EmpleadoBeneficiarioExt> findByIdEmpleado(long idEmpleado) {
		List<EmpleadoBeneficiarioExt> listaExt = new ArrayList<>();
		try { 
			List<EmpleadoBeneficiario> lista = this.ifzEmpleadoBeneficiario.findByIdEmpleado(idEmpleado);
			for(EmpleadoBeneficiario eb:lista){
				listaExt.add(  this.convertidor.EmpBeneficiarioToEmpBeneficiarioExt(eb)  );
			}
		} catch (RuntimeException re) {		
			throw re;
		}
		return listaExt;
	}
	
	public List<EmpleadoBeneficiario> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			List<EmpleadoBeneficiario> lista = this.ifzEmpleadoBeneficiario.findByPropertyPojoCompleto(propertyName, tipo, value);
			return lista;
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	public List<EmpleadoBeneficiarioExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<EmpleadoBeneficiarioExt> listaExt = new ArrayList<EmpleadoBeneficiarioExt>();
		
		try{
			List<EmpleadoBeneficiario> lista = this.ifzEmpleadoBeneficiario.findByPropertyPojoCompleto(propertyName, tipo, value);
			
			for (EmpleadoBeneficiario empleadoBeneficiario : lista) {
				EmpleadoBeneficiarioExt pojoAux = this.convertidor.EmpBeneficiarioToEmpBeneficiarioExt(empleadoBeneficiario);
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					System.out.println("No se agregó este item a la lista: "+empleadoBeneficiario.getId() );
			}
			System.out.println("---> LOGICA :: Lista Extendida :: " + listaExt.size());
		}catch(RuntimeException re){
			throw re;
		}

		return listaExt;
	}
	
	
}
