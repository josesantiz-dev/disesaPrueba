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
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraEmpleadoFac implements ObraEmpleadoRem {
	private static Logger log = Logger.getLogger(ObraEmpleadoFac.class);
	private InitialContext ctx;
	private ConvertExt convertidor;
	private InfoSesion infoSesion;

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
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(ObraEmpleado entity) throws Exception {
		try {
			return this.ifzObraEmpleados.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.save(Entity)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> saveOrUpdateList(List<ObraEmpleado> entities) throws Exception {
		try {
			return this.ifzObraEmpleados.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraEmpleadoFac.saveOrUpdateList(List<ObraSatics> entities)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idObraEmpleado) throws Exception {
		try {
			this.ifzObraEmpleados.setEmpresa(getIdEmpresa());
			this.ifzObraEmpleados.delete(idObraEmpleado);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.delete(idEntity)", re);
			throw re;
		}
	}

	@Override
	public void update(ObraEmpleado entity) throws Exception {
		try {
			this.ifzObraEmpleados.setEmpresa(getIdEmpresa());
			this.ifzObraEmpleados.update(entity);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.update(Entity)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findAll() {
		try {
			this.ifzObraEmpleados.setEmpresa(getIdEmpresa());
			return this.ifzObraEmpleados.findAll();
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findAll()", re);
			throw re;
		}
	}

	@Override
	public ObraEmpleado findById(Long id) {
		try {
			this.ifzObraEmpleados.setEmpresa(getIdEmpresa());
			return this.ifzObraEmpleados.findById(id);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findById(id)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findByProperty(String propertyName, Object value, int max) {
		try {
			this.ifzObraEmpleados.setEmpresa(getIdEmpresa());
			return this.ifzObraEmpleados.findByProperty(propertyName, value, max);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findByProperty(propertyName, value, max)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, int max) {
		try {
			this.ifzObraEmpleados.setEmpresa(getIdEmpresa());
			return this.ifzObraEmpleados.findLikeProperty(propertyName, value, max);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findLikeProperty(propertyName, value, max)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findByProperty(String propertyName, Object value) {
		try {
			this.ifzObraEmpleados.setEmpresa(getIdEmpresa());
			return this.ifzObraEmpleados.findByColumnName(propertyName, value);
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findByProperty(propertyName, value)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value) {
		try {
			this.ifzObraEmpleados.setEmpresa(getIdEmpresa());
			return this.ifzObraEmpleados.findLikeColumnName(propertyName, value.toString());
		} catch (RuntimeException re) {	
			log.error("Error en ObraEmpleadoFac.findLikeProperty(propertyName, value)", re);
			throw re;
		}
	}
	
	@Override
	public Long save(ObraEmpleadoExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.ObraEmpleadoExtToObraEmpleado(entityExt));
		} catch (Exception e) {
			log.error("Error en salvar", e);
			throw e;
		}
	}
	
	@Override
	public void update(ObraEmpleadoExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.ObraEmpleadoExtToObraEmpleado(entityExt));
		} catch (Exception e) {
			log.error("Error en update", e);
			throw e;
		}
	}
	
	@Override
	public ObraEmpleadoExt findByIdExt(Long id) throws Exception {
		try {
			return this.convertidor.ObraEmpleadoToObraEmpleadoExt(this.findById(id));
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
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}
