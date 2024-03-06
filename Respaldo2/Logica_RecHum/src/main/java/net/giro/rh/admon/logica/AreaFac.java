package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.AreaExt;
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.dao.AreaDAO;

@Stateless
public class AreaFac implements AreaRem {
	private static Logger log = Logger.getLogger(AreaFac.class);
	private InitialContext ctx;
	private AreaDAO ifzArea;
	private ConvertExt convertidor;
	private InfoSesion infoSesion;

	
	public AreaFac(){
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzArea = (AreaDAO) this.ctx.lookup("ejb:/Model_RecHum//AreaImp!net.giro.rh.admon.dao.AreaDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear AreaFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(Area entity) throws Exception {
		try {
			return this.ifzArea.save(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Area> saveOrUpdateList(List<Area> entities) throws Exception {
		try {
			return this.ifzArea.saveOrUpdateList(entities);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(Area entity) throws Exception {
		try {
			this.ifzArea.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(Area entity) throws Exception {
		try {
			this.ifzArea.setEmpresa(getIdEmpresa());
			this.ifzArea.delete(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Area findById(Long id) {
		try {
			this.ifzArea.setEmpresa(getIdEmpresa());
			return this.ifzArea.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Area> findByProperty(String propertyName, final Object value) {
		try {
			this.ifzArea.setEmpresa(getIdEmpresa());
			return this.ifzArea.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Area> findAll() {
		try {
			this.ifzArea.setEmpresa(getIdEmpresa());
			return this.ifzArea.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Area> findAllActivos() {
		try {
			this.ifzArea.setEmpresa(getIdEmpresa());
			return this.ifzArea.findAllActivos();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Area> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			this.ifzArea.setEmpresa(getIdEmpresa());
			return this.ifzArea.findByPropertyPojoCompleto(propertyName, tipo, value);
		} catch (Exception re){
			throw re;
		}
	}

	// ---------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------

	@Override
	public Long save(AreaExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.AreaExtToArea(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AreaExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.AreaExtToArea(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(AreaExt entityExt) throws Exception {
		try {
			this.delete(this.convertidor.AreaExtToArea(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AreaExt findExtById(Long id) {
		try {
			return this.convertidor.AreaToAreaExt(this.ifzArea.findById(id));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AreaExt> findByPropertyExt(String propertyName, final Object value) {
		List<AreaExt> listaExt = new ArrayList<AreaExt>();
		
		try {
			List<Area> listAreas = this.findByProperty(propertyName, value);
			for(Area area: listAreas){
				AreaExt nuevaArea = this.convertidor.AreaToAreaExt(area);
				listaExt.add(nuevaArea);
			}
		} catch (Exception re) {
			throw re;
		}
		return listaExt;
	}

	@Override
	public List<AreaExt> findAllExt() {
		List<AreaExt> listaExt = new ArrayList<AreaExt>();
		
		try {
			List<Area> listAreas = this.findAll();
			for(Area a : listAreas) {
				listaExt.add(this.convertidor.AreaToAreaExt(a));
			}
		} catch (Exception re) {
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AreaExt> findExtAllActivos() {
		List<AreaExt> listaExt = new ArrayList<AreaExt>();
		
		try {
			List<Area> listAreas = this.findAllActivos();
			for (Area a : listAreas)
				listaExt.add(this.convertidor.AreaToAreaExt(a));
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AreaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<AreaExt> listaExt = new ArrayList<AreaExt>();
		
		try{
			List<Area> lista = this.findByPropertyPojoCompleto(propertyName, tipo, value);
			for (Area area : lista) {
				if ("".equals(tipo)) {
					log.info("No se agregó este item a la lista: " + area.getId());
					continue;
				}
				
				listaExt.add(this.convertidor.AreaToAreaExt(area));
			}
		} catch (Exception re){
			throw re;
		}

		return listaExt;
	}

	// ---------------------------------------------------------------------------------
	// PRIVADOS
	// ---------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}
