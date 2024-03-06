package net.giro.adp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraEmpleado;
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.adp.dao.ObraEmpleadoDAO;
import net.giro.comun.ExcepConstraint;

@Stateless
public class ObraEmpleadoFac implements ObraEmpleadoRem {
	private static Logger log = Logger.getLogger(ObraEmpleadoFac.class);
	InitialContext ctx;
	private ConvertExt convertidor;

	private ObraEmpleadoDAO ifzObraEmpleados;
	
	public ObraEmpleadoFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            this.ifzObraEmpleados = (ObraEmpleadoDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraEmpleadoImp!net.giro.adp.dao.ObraEmpleadoDAO");
            
            this.convertidor = new ConvertExt();
            this.convertidor.setFrom("ObraEmpleadoFac");
            this.convertidor.setMostrarSystemOut(false);
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear ObraEmpleadoFac", e);
			ctx = null;
		}
	}
	
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public Long save(ObraEmpleado entity) throws ExcepConstraint {
		try {
			return this.ifzObraEmpleados.save(entity);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.save(Entity)", re);
			throw re;
		}
	}

	@Override
	public void delete(Long idObraEmpleado) throws ExcepConstraint {
		try {
			this.ifzObraEmpleados.delete(idObraEmpleado);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.delete(idEntity)", re);
			throw re;
		}
	}

	@Override
	public void update(ObraEmpleado entity) throws ExcepConstraint {
		try {
			this.ifzObraEmpleados.update(entity);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.update(Entity)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findAll() {
		try {
			return this.ifzObraEmpleados.findAll();
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findAll()", re);
			throw re;
		}
	}

	@Override
	public ObraEmpleado findById(Long id) {
		try {
			return this.ifzObraEmpleados.findById(id);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findById(id)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findByProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzObraEmpleados.findByProperty(propertyName, value, max);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findByProperty(propertyName, value, max)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzObraEmpleados.findLikeProperty(propertyName, value, max);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findLikeProperty(propertyName, value, max)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzObraEmpleados.findByColumnName(propertyName, value);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findByProperty(propertyName, value)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value) {
		try {
			return this.ifzObraEmpleados.findLikeColumnName(propertyName, value.toString());
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findLikeProperty(propertyName, value)", re);
			throw re;
		}
	}
	
	@Override
	public Long save(ObraEmpleadoExt entityExt) throws ExcepConstraint {
		try {
			ObraEmpleado entity = this.convertidor.ObraEmpleadoExtToObraEmpleado(entityExt);
			return this.save(entity);
		} catch (Exception e) {
			log.error("Error en salvar", e);
			throw e;
		}
	}
	
	@Override
	public void update(ObraEmpleadoExt entityExt) throws ExcepConstraint {
		try {
			ObraEmpleado entity = this.convertidor.ObraEmpleadoExtToObraEmpleado(entityExt);
			this.update(entity);
		} catch (Exception e) {
			log.error("Error en update", e);
			throw e;
		}
	}
	
	@Override
	public ObraEmpleadoExt findByIdExt(Long id) throws Exception {
		try {
			ObraEmpleado entity = this.findById(id);
			return this.convertidor.ObraEmpleadoToObraEmpleadoExt(entity);
		} catch (Exception e) {
			log.error("Error en findByIdExt", e);
			throw e;
		}
	}
	
	@Override
	public List<ObraEmpleadoExt> findByPropertyExt(String propertyName, Object value) throws Exception {
		List<ObraEmpleadoExt> listaExt = new ArrayList<ObraEmpleadoExt>();
		
		try {
			List<ObraEmpleado> lista = this.findByProperty(propertyName, value);
			for (ObraEmpleado var : lista) {
				listaExt.add(this.convertidor.ObraEmpleadoToObraEmpleadoExt(var));
			}
		} catch (Exception e) {
			log.error("Error en findByPropertyExt(propertyName, value)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<ObraEmpleadoExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception {
		List<ObraEmpleadoExt> listaExt = new ArrayList<ObraEmpleadoExt>();
		
		try {
			List<ObraEmpleado> lista = this.findByProperty(propertyName, value, max);
			for (ObraEmpleado var : lista) {
				listaExt.add(this.convertidor.ObraEmpleadoToObraEmpleadoExt(var));
			}
		} catch (Exception e) {
			log.error("Error en findByPropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<ObraEmpleadoExt> findLikePropertyExt(String propertyName, Object value) throws Exception {
		List<ObraEmpleadoExt> listaExt = new ArrayList<ObraEmpleadoExt>();
		
		try {
			List<ObraEmpleado> lista = this.findLikeProperty(propertyName, value);
			for (ObraEmpleado var : lista) {
				listaExt.add(this.convertidor.ObraEmpleadoToObraEmpleadoExt(var));
			}
		} catch (Exception e) {
			log.error("Error en findLikePropertyExt(propertyName, value)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<ObraEmpleadoExt> findLikePropertyExt(String propertyName, Object value, int max) throws Exception {
		List<ObraEmpleadoExt> listaExt = new ArrayList<ObraEmpleadoExt>();
		
		try {
			List<ObraEmpleado> lista = this.findLikeProperty(propertyName, value, max);
			for (ObraEmpleado var : lista) {
				listaExt.add(this.convertidor.ObraEmpleadoToObraEmpleadoExt(var));
			}
		} catch (Exception e) {
			log.error("Error en findLikePropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}
}
