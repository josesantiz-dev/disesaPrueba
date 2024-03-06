
package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoTrabajoAnterior;
import net.giro.rh.admon.beans.EmpleadoTrabajoAnteriorExt;
import net.giro.rh.admon.dao.EmpleadoTrabajoAnteriorDAO;

@Stateless
public class EmpleadoTrabajoAnteriorFac implements EmpleadoTrabajoAnteriorRem {
	private static Logger log = Logger.getLogger(EmpleadoTrabajoAnteriorFac.class);
	private InitialContext ctx;
	private EmpleadoTrabajoAnteriorDAO ifzEmpleadoTrabajoAnterior;
	private ConvertExt convertidor;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
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

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(EmpleadoTrabajoAnterior entity) throws Exception {
		try {
			return this.ifzEmpleadoTrabajoAnterior.save(entity, null);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(EmpleadoTrabajoAnterior entity) throws Exception {
		try {
			this.ifzEmpleadoTrabajoAnterior.delete(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(EmpleadoTrabajoAnterior entity) throws Exception {
		try {
			this.ifzEmpleadoTrabajoAnterior.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public EmpleadoTrabajoAnterior findById(Long id) {
		try {
			return this.ifzEmpleadoTrabajoAnterior.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<EmpleadoTrabajoAnterior> findAll() {
		try {
			return this.ifzEmpleadoTrabajoAnterior.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<EmpleadoTrabajoAnterior> findByProperty(String propertyName, final Object value) {
		System.out.println("Pasando por logica findbyproperty de EmpleadoTrabajoAnterior");
		try {
			return this.ifzEmpleadoTrabajoAnterior.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoTrabajoAnterior> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			return this.ifzEmpleadoTrabajoAnterior.findByPropertyPojoCompleto(propertyName, tipo, value);
		}catch(Exception re){
			throw re;
		}
	}
	
	// -----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------------------------------

	@Override
	public Long save(EmpleadoTrabajoAnteriorExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.EmpleadoTrabajoAnteriorExtToEmpleadoTrabajoAnterior(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(EmpleadoTrabajoAnteriorExt entityExt) throws Exception {
		try {
			this.ifzEmpleadoTrabajoAnterior.update(this.convertidor.EmpleadoTrabajoAnteriorExtToEmpleadoTrabajoAnterior(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(EmpleadoTrabajoAnteriorExt entityExt) throws Exception {
		try {
			this.delete(this.convertidor.EmpleadoTrabajoAnteriorExtToEmpleadoTrabajoAnterior(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<EmpleadoTrabajoAnteriorExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<EmpleadoTrabajoAnteriorExt> listaExt = new ArrayList<EmpleadoTrabajoAnteriorExt>();
		
		try{
			List<EmpleadoTrabajoAnterior> lista = this.ifzEmpleadoTrabajoAnterior.findByPropertyPojoCompleto(propertyName, tipo, value);
			
			for (EmpleadoTrabajoAnterior EmpleadoTrabajoAnterior : lista) {
				EmpleadoTrabajoAnteriorExt pojoAux = this.convertidor.EmpleadoTrabajoAnteriorToEmpleadoTrabajoAnteriorExt(EmpleadoTrabajoAnterior);
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					log.info("No se agregó este item a la lista: "+EmpleadoTrabajoAnterior.getId() );
			}
		}catch(Exception re){
			throw re;
		}

		return listaExt;
	}

}
