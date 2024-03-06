package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoPariente;
import net.giro.rh.admon.beans.EmpleadoParienteExt;
import net.giro.rh.admon.dao.EmpleadoParienteDAO;

@Stateless
public class EmpleadoParienteFac implements EmpleadoParienteRem{
	private static Logger log = Logger.getLogger(EmpleadoParienteFac.class);
	private InitialContext ctx;
	private EmpleadoParienteDAO ifzEmpleadoPariente;
	private ConvertExt convertidor;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
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

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(EmpleadoPariente entity) throws Exception {
		try {
			return this.ifzEmpleadoPariente.save(entity, null);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Long save(EmpleadoParienteExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.EmpleadoParienteExtToEmpleadoPariente(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(EmpleadoPariente entity) throws Exception {
		try {
			this.ifzEmpleadoPariente.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(EmpleadoParienteExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.EmpleadoParienteExtToEmpleadoPariente(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(EmpleadoPariente entity) throws Exception {
		try {
			this.ifzEmpleadoPariente.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(EmpleadoParienteExt entityExt) throws Exception {
		try {
			EmpleadoPariente entity = this.convertidor.EmpleadoParienteExtToEmpleadoPariente(entityExt);
			this.ifzEmpleadoPariente.delete(entity.getId());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public EmpleadoPariente findById(Long id) {
		try {
			return this.ifzEmpleadoPariente.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<EmpleadoPariente> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleadoPariente.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoPariente> findAll() {
		try {
			return this.ifzEmpleadoPariente.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<EmpleadoParienteExt> findByIdEmpleadoParentesco(long idEmpleado) {
		List<EmpleadoParienteExt> listaExt = new ArrayList<>();
		try {
			
			List<EmpleadoPariente> lista = this.ifzEmpleadoPariente.findByIdEmpleadoParentesco(idEmpleado);
			for(EmpleadoPariente ep: lista){
				listaExt.add(  this.convertidor.EmpleadoParienteToEmpleadoParienteExt( ep )  );
			}
		} catch (Exception re) {		
			throw re;
		}
		return listaExt;
	}

	@Override
	public List<EmpleadoPariente> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			//System.out.println("---> LOGICA :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<EmpleadoPariente> lista = this.ifzEmpleadoPariente.findByPropertyPojoCompleto(propertyName, tipo, value);
			//System.out.println("---> LOGICA :: listFactura(" + lista.size() + ")");
			return lista;
		}catch(Exception re){
			throw re;
		}
	}

	@Override
	public List<EmpleadoParienteExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value) {
		List<EmpleadoParienteExt> listaExt = new ArrayList<EmpleadoParienteExt>();
		
		try{
			List<EmpleadoPariente> lista = this.ifzEmpleadoPariente.findByPropertyPojoCompleto(propertyName, tipo, value);
			
			for (EmpleadoPariente EmpleadoPariente : lista) {
				EmpleadoParienteExt pojoAux = this.convertidor.EmpleadoParienteToEmpleadoParienteExt(EmpleadoPariente);
				if ("".equals(tipo))
					listaExt.add(pojoAux);
			}
		}catch(Exception re){
			throw re;
		}

		return listaExt;
	}
}
