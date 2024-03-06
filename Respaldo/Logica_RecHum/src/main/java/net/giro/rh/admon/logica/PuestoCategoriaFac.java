package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.PuestoCategoria;
import net.giro.rh.admon.beans.PuestoCategoriaExt;
import net.giro.rh.admon.dao.PuestoCategoriaDAO;

@Stateless
public class PuestoCategoriaFac implements PuestoCategoriaRem {
	private static Logger log = Logger.getLogger(PuestoCategoriaFac.class);
	private InitialContext ctx;
	private PuestoCategoriaDAO ifzPuestoCategoria;
	private ConvertExt convertidor;
	
	
	public PuestoCategoriaFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//PuestoCategoriaImp!net.giro.rh.admon.dao.PuestoCategoriaDAO";
            this.ifzPuestoCategoria = (PuestoCategoriaDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear PuestoCategoriaFac", e);
			ctx = null;
		}
	}
	
	
	public Long save(PuestoCategoria entity) throws ExcepConstraint {
		try {
			return this.ifzPuestoCategoria.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(PuestoCategoriaExt entityExt) throws ExcepConstraint {
		try {
			PuestoCategoria entity = this.convertidor.PuestoCategoriaExtToPuestoCategoria(entityExt);
			return this.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(PuestoCategoria entity) throws ExcepConstraint {
		try {
			this.ifzPuestoCategoria.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(PuestoCategoriaExt entityExt) throws ExcepConstraint {
		try {
			PuestoCategoria entity = this.convertidor.PuestoCategoriaExtToPuestoCategoria(entityExt);
			this.ifzPuestoCategoria.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public PuestoCategoria update(PuestoCategoria entity) throws ExcepConstraint {
		try {
			this.ifzPuestoCategoria.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	} 
	
	public PuestoCategoria update(PuestoCategoriaExt entityExt) throws ExcepConstraint {
		try {
			PuestoCategoria entity = this.convertidor.PuestoCategoriaExtToPuestoCategoria(entityExt);
			this.ifzPuestoCategoria.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public PuestoCategoria findById(Long id) {
		try {
			return this.ifzPuestoCategoria.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<PuestoCategoria> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzPuestoCategoria.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<PuestoCategoriaExt> findByPropertyExt(String propertyName, final Object value) {
		List<PuestoCategoriaExt> listaExt = new ArrayList<PuestoCategoriaExt>();
		try {
			List<PuestoCategoria> listaPuestoCategorias = this.ifzPuestoCategoria.findByProperty(propertyName, value);
			for(PuestoCategoria pc: listaPuestoCategorias ){
				listaExt.add(  this.convertidor.PuestoCategoriaToPuestoCategoriaExt( pc )  );
			}
		} catch (RuntimeException re) {
			throw re;
		}
		return listaExt;
	}
	
	public List<PuestoCategoriaExt> findByPuestoCategoriaExt(int idPuesto, int idCategoria) {
		List<PuestoCategoriaExt> listaExt = new ArrayList<PuestoCategoriaExt>();
		try {
			List<PuestoCategoria> listaPuestoCategorias = this.ifzPuestoCategoria.findByPuestoCategoria(idPuesto, idCategoria) ;
			for(PuestoCategoria pc: listaPuestoCategorias ){
				listaExt.add(  this.convertidor.PuestoCategoriaToPuestoCategoriaExt( pc )  );
			}
		} catch (RuntimeException re) {
			throw re;
		}
		return listaExt;
	}

	public List<PuestoCategoriaExt> findByIdPuesto(int idPuesto) {
		List<PuestoCategoriaExt> listaExt = new ArrayList<PuestoCategoriaExt>();
		try {
			List<PuestoCategoria> listaPuestoCategorias = this.ifzPuestoCategoria.findByIdPuesto(idPuesto) ;
			for(PuestoCategoria pc: listaPuestoCategorias ){
				listaExt.add(  this.convertidor.PuestoCategoriaToPuestoCategoriaExt( pc )  );
			}
		} catch (RuntimeException re) {
			throw re;
		}
		return listaExt;
	}
	
	public List<PuestoCategoria> findAll() {
		try {
			return this.ifzPuestoCategoria.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<PuestoCategoriaExt> findAllExt() {
		List<PuestoCategoriaExt> listExt = new ArrayList<PuestoCategoriaExt>();
		try {
			 List<PuestoCategoria> listaPuestoCategoria = this.ifzPuestoCategoria.findAll();
			 for(PuestoCategoria pc: listaPuestoCategoria){
				 PuestoCategoriaExt pce = this.convertidor.PuestoCategoriaToPuestoCategoriaExt(pc);
				 listExt.add(pce);
			 }
		} catch (RuntimeException re) {		
			throw re;
		}
		return listExt;
	}
	
	public List<PuestoCategoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			//System.out.println("---> LOGICA :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<PuestoCategoria> lista = this.ifzPuestoCategoria.findByPropertyPojoCompleto(propertyName, tipo, value);
			//System.out.println("---> LOGICA :: listFactura(" + lista.size() + ")");
			return lista;
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	public List<PuestoCategoriaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<PuestoCategoriaExt> listaExt = new ArrayList<PuestoCategoriaExt>();
		
		try{
			System.out.println("---> LOGICA PuestoCategoriaEXT :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<PuestoCategoria> lista = this.ifzPuestoCategoria.findByPropertyPojoCompleto(propertyName, tipo, value);
			System.out.println("---> LOGICA PuestoCategoriaEXT :: Lista :: " + lista.size());
			
			for (PuestoCategoria PuestoCategoria : lista) {
				PuestoCategoriaExt pojoAux = this.convertidor.PuestoCategoriaToPuestoCategoriaExt(PuestoCategoria);
				System.out.println("---> LOGICA :: FOR :: pojoAux :: " + "pojoAux.getId() <-- no implementado" + " :: " );
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					System.out.println("No se agregó este item a la lista: "+PuestoCategoria.getId() );
			}
			System.out.println("---> LOGICA :: Lista Extendida :: " + listaExt.size());
		}catch(RuntimeException re){
			throw re;
		}

		return listaExt;
	}

	public PuestoCategoriaExt findByIdExt(Long id) {

		PuestoCategoriaExt entityExt = new PuestoCategoriaExt();
		try {
			PuestoCategoria pc = this.ifzPuestoCategoria.findById(id);
			entityExt = this.convertidor.PuestoCategoriaToPuestoCategoriaExt(pc);
		} catch (RuntimeException re) {	
			throw re;
		}
		return entityExt;
	}

	
	@Override
	public List<PuestoCategoria> findByPuestoAndCategoria(Long idPuesto, Long idCategoria) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		try {
			params.put("idPuesto", idPuesto);
			params.put("idCategoria", idCategoria);
			return this.findByProperties(params);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.PuestoCategoriaFac.findByPuestoAndCategoria(idPuesto, idCategoria)", e);
			throw e;
		}
	}
	

	@Override
	public List<PuestoCategoria> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.ifzPuestoCategoria.findByProperties(params);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.PuestoCategoriaFac.findByProperties(params)", e);
			throw e;
		}
	}	
}


//HISTORIAL DE MODIFICACIONES 
//-------------------------------------------------------------------------------------------------------------------
//VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
//-------------------------------------------------------------------------------------------------------------------
//  1.0   | 2016-09-20 | Javier Tirado      | Se agrego el metodo salvar a partir de un pojo extendido
