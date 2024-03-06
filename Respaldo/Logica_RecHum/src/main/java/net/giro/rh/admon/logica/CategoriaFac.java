package net.giro.rh.admon.logica;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
//import net.giro.plataforma.beans.CategoriaExt;
import net.giro.rh.admon.beans.CategoriaExt;

import net.giro.rh.admon.beans.Categoria;
import net.giro.rh.admon.dao.CategoriaDAO;

@Stateless
public class CategoriaFac implements CategoriaRem {

	private static Logger log = Logger.getLogger(CategoriaFac.class);
	
	InitialContext ctx;
	private CategoriaDAO ifzCategoria;
	private ConvertExt convertidor;
	
	public CategoriaFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//CategoriaImp!net.giro.rh.admon.dao.CategoriaDAO";
            this.ifzCategoria = (CategoriaDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear CategoriaFac", e);
			ctx = null;
		}
	}
	
	public Long save(Categoria entity) throws ExcepConstraint {
		try {
			return this.ifzCategoria.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(CategoriaExt entityExt) throws ExcepConstraint {
		try {
			Categoria entity = this.convertidor.CategoriaExtToCategoria(entityExt);
			return this.ifzCategoria.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(Categoria entity) throws ExcepConstraint {
		try {
			this.ifzCategoria.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(CategoriaExt entityExt) throws ExcepConstraint {
		try {
			Categoria entity = this.convertidor.CategoriaExtToCategoria(entityExt);
			this.ifzCategoria.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Categoria update(Categoria entity) throws ExcepConstraint {
		try {
			this.ifzCategoria.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Categoria update(CategoriaExt entityExt) throws ExcepConstraint {
		try {
			Categoria entity = this.convertidor.CategoriaExtToCategoria(entityExt);
			this.ifzCategoria.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Categoria findById(Long id) {
		try {
			return this.ifzCategoria.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<Categoria> findByProperty(String propertyName, final Object value) {
		
		try {
			return this.ifzCategoria.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Categoria> findAll() {
		try {
			return this.ifzCategoria.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<Categoria> findAllActivos() {
		try {
			return this.ifzCategoria.findAllActivos();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<Categoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			//System.out.println("---> LOGICA :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<Categoria> lista = this.ifzCategoria.findByPropertyPojoCompleto(propertyName, tipo, value);
			//System.out.println("---> LOGICA :: listFactura(" + lista.size() + ")");
			return lista;
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	public List<CategoriaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<CategoriaExt> listaExt = new ArrayList<CategoriaExt>();
		
		try{
			System.out.println("---> LOGICA CategoriaEXT :: findByPropertyPojoCompleto ('" + propertyName + "', '" + tipo + "', " + value + ")");
			List<Categoria> lista = this.ifzCategoria.findByPropertyPojoCompleto(propertyName, tipo, value);
			System.out.println("---> LOGICA CategoriaEXT :: Lista :: " + lista.size());
			
			for (Categoria Categoria : lista) {
				CategoriaExt pojoAux = this.convertidor.CategoriaToCategoriaExt(Categoria);
				System.out.println("---> LOGICA :: FOR :: pojoAux :: " + "pojoAux.getId() no implementado" + " :: " );
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					System.out.println("No se agregó este item a la lista: "+Categoria.getId() );
			}
			System.out.println("---> LOGICA :: Lista Extendida :: " + listaExt.size());
		}catch(RuntimeException re){
			throw re;
		}

		return listaExt;
	}
	

}
