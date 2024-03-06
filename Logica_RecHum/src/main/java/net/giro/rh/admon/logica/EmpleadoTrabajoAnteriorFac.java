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
	private InfoSesion infoSesion;
	
	
	public EmpleadoTrabajoAnteriorFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzEmpleadoTrabajoAnterior = (EmpleadoTrabajoAnteriorDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoTrabajoAnteriorImp!net.giro.rh.admon.dao.EmpleadoTrabajoAnteriorDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
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
			return this.ifzEmpleadoTrabajoAnterior.save(entity, getCodigoEmpresa());
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
			return this.ifzEmpleadoTrabajoAnterior.findAll(getIdEmpresa());
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<EmpleadoTrabajoAnterior> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleadoTrabajoAnterior.findByProperty(propertyName, value, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoTrabajoAnterior> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			return this.ifzEmpleadoTrabajoAnterior.findByPropertyPojoCompleto(propertyName, tipo, value, getIdEmpresa());
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
			List<EmpleadoTrabajoAnterior> lista = this.ifzEmpleadoTrabajoAnterior.findByPropertyPojoCompleto(propertyName, tipo, value, getIdEmpresa());
			
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

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}
