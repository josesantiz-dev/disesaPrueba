package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoReferencia;
import net.giro.rh.admon.beans.EmpleadoReferenciaExt;
import net.giro.rh.admon.dao.EmpleadoReferenciaDAO;

@Stateless
public class EmpleadoReferenciaFac implements EmpleadoReferenciaRem {
	private static Logger log = Logger.getLogger(EmpleadoReferenciaFac.class);
	private InitialContext ctx;
	private EmpleadoReferenciaDAO ifzEmpleadoReferencia;
	private ConvertExt convertidor;
	private InfoSesion infoSesion;
	
	
	public EmpleadoReferenciaFac(){
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            this.ifzEmpleadoReferencia = (EmpleadoReferenciaDAO) ctx.lookup("ejb:/Model_RecHum//EmpleadoReferenciaImp!net.giro.rh.admon.dao.EmpleadoReferenciaDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear EmpleadoReferenciaFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(EmpleadoReferencia entity) throws Exception {
		try {
			return this.ifzEmpleadoReferencia.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(EmpleadoReferencia entity) throws Exception {
		try {
			this.ifzEmpleadoReferencia.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(EmpleadoReferencia entity) throws Exception {
		try {
			this.ifzEmpleadoReferencia.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public EmpleadoReferencia findById(Long id) {
		try {
			return this.ifzEmpleadoReferencia.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<EmpleadoReferencia> findAll() {
		try {
			return this.ifzEmpleadoReferencia.findAll(getIdEmpresa());
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<EmpleadoReferencia> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleadoReferencia.findByProperty(propertyName, value, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoReferencia> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			return this.ifzEmpleadoReferencia.findByPropertyPojoCompleto(propertyName, tipo, value, getIdEmpresa());
		} catch (Exception re){
			throw re;
		}
	}

	// -------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// -------------------------------------------------------------------------------------------------------

	@Override
	public Long save(EmpleadoReferenciaExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.EmpleadoReferenciaExtToEmpleadoReferencia(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(EmpleadoReferenciaExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.EmpleadoReferenciaExtToEmpleadoReferencia(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(EmpleadoReferenciaExt entityExt) throws Exception {
		try {
			this.ifzEmpleadoReferencia.delete(this.convertidor.EmpleadoReferenciaExtToEmpleadoReferencia(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<EmpleadoReferenciaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<EmpleadoReferenciaExt> listaExt = new ArrayList<EmpleadoReferenciaExt>();
		List<EmpleadoReferencia> lista = null;
		
		try{
			lista = this.findByPropertyPojoCompleto(propertyName, tipo, value);
			for (EmpleadoReferencia EmpleadoReferencia : lista) {
				EmpleadoReferenciaExt pojoAux = this.convertidor.EmpleadoReferenciaToEmpleadoReferenciaExt(EmpleadoReferencia);
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					log.info("No se agregó este item a la lista: "+EmpleadoReferencia.getId() );
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
