package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.beans.EmpleadoFiniquitoExt;
import net.giro.rh.admon.dao.EmpleadoFiniquitoDAO;

@Stateless
public class EmpleadoFiniquitoFac implements EmpleadoFiniquitoRem{
	private static Logger log = Logger.getLogger(EmpleadoFiniquitoFac.class);
	private InitialContext ctx;
	private EmpleadoFiniquitoDAO ifzEmpleadoFiniquito;
	private ConvertExt convertidor;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
	public EmpleadoFiniquitoFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            this.ifzEmpleadoFiniquito = (EmpleadoFiniquitoDAO) ctx.lookup("ejb:/Model_RecHum//EmpleadoFiniquitoImp!net.giro.rh.admon.dao.EmpleadoFiniquitoDAO");
            this.convertidor = new ConvertExt(); 
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear EmpleadoFiniquitoFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(EmpleadoFiniquito entity) throws Exception {
		try {
			return this.ifzEmpleadoFiniquito.save(entity, null);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(EmpleadoFiniquito entity) throws Exception {
		try {
			this.ifzEmpleadoFiniquito.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(EmpleadoFiniquito entity) throws Exception {
		try {
			this.ifzEmpleadoFiniquito.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public EmpleadoFiniquito findById(Long id) {
		try {
			return this.ifzEmpleadoFiniquito.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<EmpleadoFiniquito> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleadoFiniquito.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoFiniquito> findAll() {
		try {
			return this.ifzEmpleadoFiniquito.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado) {
		List<EmpleadoFiniquito> lista = new ArrayList<EmpleadoFiniquito>();
		try{
			lista = this.ifzEmpleadoFiniquito.findByEmpleado(nombreEmpleado);
		}catch (Exception re) {		
			throw re;
		}
		return lista;
	}
	
	// -----------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------

	@Override
	public Long save(EmpleadoFiniquitoExt entity) throws Exception {
		try {
			return this.save(this.convertidor.EmpleadoFiniquitoExtToEmpleadoFiniquito(entity));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(EmpleadoFiniquitoExt entity) throws Exception {
		try {
			this.ifzEmpleadoFiniquito.delete(this.convertidor.EmpleadoFiniquitoExtToEmpleadoFiniquito(entity));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(EmpleadoFiniquitoExt entity) throws Exception {
		try {
			this.update(this.convertidor.EmpleadoFiniquitoExtToEmpleadoFiniquito(entity));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<EmpleadoFiniquitoExt> findAllExt() {
		List<EmpleadoFiniquitoExt> listaExt = new ArrayList<>();
		try {
			List<EmpleadoFiniquito> lista = this.findAll();
			for(EmpleadoFiniquito ef: lista){
				listaExt.add( this.convertidor.EmpleadoFiniquitoToEmpleadoFiniquitoExt(ef) );
			}
		} catch (Exception re) {		
			throw re;
		}
		return listaExt;
	}

	@Override
	public List<EmpleadoFiniquitoExt> findByEmpleadoExt(String nombreEmpleado) {
		List<EmpleadoFiniquitoExt> listaExt = new ArrayList<EmpleadoFiniquitoExt>();
		try{
			List<EmpleadoFiniquito> lista = new ArrayList<EmpleadoFiniquito>();
			lista = this.findByEmpleado(nombreEmpleado);
			for(EmpleadoFiniquito ef: lista){
				listaExt.add( this.convertidor.EmpleadoFiniquitoToEmpleadoFiniquitoExt(ef) );
			}
		}catch (Exception re) {		
			throw re;
		}
		return listaExt;
	}
}
