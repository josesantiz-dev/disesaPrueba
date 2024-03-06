package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenExt;
import net.giro.inventarios.dao.AlmacenDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class AlmacenFac implements AlmacenRem {
	private static Logger log = Logger.getLogger(AlmacenFac.class);
	private InitialContext ctx;
	private ConvertExt convertidor;
	private AlmacenDAO ifzAlmacen;
	private InfoSesion infoSesion;
	
	
	public AlmacenFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzAlmacen = (AlmacenDAO) this.ctx.lookup("ejb:/Model_Inventarios//AlmacenImp!net.giro.inventarios.dao.AlmacenDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear AlmacenFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(Almacen entity) throws Exception {
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			return this.ifzAlmacen.save(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Almacen> saveOrUpdateList(List<Almacen> entities) throws Exception {
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			return this.ifzAlmacen.saveOrUpdateList(entities);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void update(Almacen entity) throws Exception {
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			this.ifzAlmacen.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(Almacen entity) throws Exception {
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			this.ifzAlmacen.delete(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Almacen findById(Long id) {
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			return this.ifzAlmacen.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Almacen> findAll() {
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			return this.ifzAlmacen.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Almacen> findAllActivos() {
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			return this.ifzAlmacen.findAllActivos();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Almacen> findByProperty(String propertyName, Object value) {
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			return this.ifzAlmacen.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Almacen> findLikeProperty(String propertyName, String value) throws Exception {
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			return this.ifzAlmacen.findLikeProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public boolean comprobarPrincipal(Long idSucursal, Long idAlmacen) throws Exception {
		List<Almacen> lista = null;
		
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			lista = this.ifzAlmacen.comprobarPrincipal(idSucursal, idAlmacen);
			return lista != null && ! lista.isEmpty() ? true : false;
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	public boolean comprobarNombre(String nombre, Long idAlmacen) throws Exception {
		List<Almacen> lista = null;
		
		try {
			this.ifzAlmacen.setEmpresa(getIdEmpresa());
			lista = this.ifzAlmacen.comprobarNombre(nombre, idAlmacen);
			return lista != null && ! lista.isEmpty() ? true : false;
		} catch (Exception re) {	
			throw re;
		}
	}

	// --------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------

	@Override
	public Long save(AlmacenExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.AlmacenExtToAlmacen(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AlmacenExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.AlmacenExtToAlmacen(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(AlmacenExt entityExt) throws Exception {
		try {
			this.delete(this.convertidor.AlmacenExtToAlmacen(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenExt findByIdExt(Long id) throws Exception {
		try {
			return this.convertidor.AlmacenToAlmacenExt(this.findById(id));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenExt> findAllExt() throws Exception {
		List<AlmacenExt> listaExt = new ArrayList<AlmacenExt>();
		List<Almacen> lista = null;
		
		try {
			lista = this.findAll();
			if (lista != null && ! lista.isEmpty()) {
				for (Almacen a : lista)
					listaExt.add(this.convertidor.AlmacenToAlmacenExt(a));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AlmacenExt> findExtByProperty(String propertyName, Object value) throws Exception {
		List<AlmacenExt> listaExt = new ArrayList<AlmacenExt>();
		List<Almacen> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value);
			if (lista != null && ! lista.isEmpty()) {
				for (Almacen a : lista)
					listaExt.add(this.convertidor.AlmacenToAlmacenExt(a));
			}
		} catch (Exception re) {
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AlmacenExt> findExtLikeProperty(String propertyName, String value) throws Exception {
		List<AlmacenExt> listaExt = new ArrayList<>();
		List<Almacen> lista = null;
		
		try {
			lista = this.findLikeProperty(propertyName, value);
			if (lista != null && ! lista.isEmpty()) {
				for (Almacen a : lista)
					listaExt.add(this.convertidor.AlmacenToAlmacenExt(a));
			}
		} catch (Exception re) {
			throw re;
		}
		
		return listaExt;
	}

	// --------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}
