package net.giro.rh.admon.logica;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;

import net.giro.rh.admon.beans.AreaExt;
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.dao.AreaDAO;



@Stateless
public class AreaFac implements AreaRem {

	private static Logger log = Logger.getLogger(AreaFac.class);
	
	InitialContext ctx;
	private AreaDAO ifzArea;
	private ConvertExt convertidor;
	
	public AreaFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//AreaImp!net.giro.rh.admon.dao.AreaDAO";
            this.ifzArea = (AreaDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear AreaFac", e);
			ctx = null;
		}
	}
	
	public Long save(Area entity) throws ExcepConstraint {
		try {
			return this.ifzArea.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(AreaExt entityExt) throws ExcepConstraint {
		try {
			Area entity = this.convertidor.AreaExtToArea(entityExt);
			return this.ifzArea.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(Area entity) throws ExcepConstraint {
		try {
			this.ifzArea.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(AreaExt entityExt) throws ExcepConstraint {
		try {
			Area entity = this.convertidor.AreaExtToArea(entityExt);
			entity.setEstatus(1);
			this.ifzArea.update(entity);
			//this.ifzArea.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Area update(Area entity) throws ExcepConstraint {
		try {
			this.ifzArea.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Area update(AreaExt entityExt) throws ExcepConstraint {
		try {
			Area entity = this.convertidor.AreaExtToArea(entityExt);
			
			this.ifzArea.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Area findById(Long id) {
		try {
			return this.ifzArea.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<Area> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzArea.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<AreaExt> findByPropertyExt(String propertyName, final Object value) {
		List<AreaExt> listaExt = new ArrayList<AreaExt>();
		try {
			List<Area> listAreas = this.findByProperty(propertyName, value);
			for(Area area: listAreas){
				AreaExt nuevaArea = this.convertidor.AreaToAreaExt(area);
				listaExt.add(nuevaArea);
			}
		} catch (RuntimeException re) {
			throw re;
		}
		return listaExt;
	}

	public List<Area> findAll() {
		try {
			return this.ifzArea.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<Area> findAllActivos() {
		try {
			return this.ifzArea.findAllActivos();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<AreaExt> findAllExt(){
		List<AreaExt> listaExt = new ArrayList<AreaExt>();
		
		try{
			List<Area> listAreas = this.findAll();
			for(Area a: listAreas){
				AreaExt nuevaArea = this.convertidor.AreaToAreaExt(a);
				listaExt.add(nuevaArea);
			}
			
		}catch(RuntimeException re){
			throw re;
		}
		
		return listaExt;
	}
	
	public List<Area> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			List<Area> lista = this.ifzArea.findByPropertyPojoCompleto(propertyName, tipo, value);
			return lista;
		}catch(RuntimeException re){
			throw re;
		}
	}
	
	public List<AreaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<AreaExt> listaExt = new ArrayList<AreaExt>();
		
		try{
			List<Area> lista = this.ifzArea.findByPropertyPojoCompleto(propertyName, tipo, value);
			
			for (Area area : lista) {
				AreaExt pojoAux = this.convertidor.AreaToAreaExt(area);
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					System.out.println("No se agregó este item a la lista: "+area.getId() );
			}
		}catch(RuntimeException re){
			throw re;
		}

		return listaExt;
	}
	
}
