package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.inventarios.dao.AlmacenMovimientosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class AlmacenMovimientosFac implements AlmacenMovimientosRem {
	private static Logger log = Logger.getLogger(AlmacenMovimientosFac.class);
	private InitialContext ctx;
	private AlmacenMovimientosDAO ifzAlmacenMovimientos;
	private ConvertExt convertidor;
	private InfoSesion infoSesion;
	
	
	public AlmacenMovimientosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzAlmacenMovimientos = (AlmacenMovimientosDAO) this.ctx.lookup("ejb:/Model_Inventarios//AlmacenMovimientosImp!net.giro.inventarios.dao.AlmacenMovimientosDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica Fac", e);
			ctx = null;
		}
	}
	

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(AlmacenMovimientos entity) throws Exception {
		try {
			return this.ifzAlmacenMovimientos.save(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> saveOrUpdateList(List<AlmacenMovimientos> entities) throws Exception {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenMovimientos.saveOrUpdateList(entities);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AlmacenMovimientos entity) throws Exception {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			this.ifzAlmacenMovimientos.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(AlmacenMovimientos entity) throws Exception {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			this.ifzAlmacenMovimientos.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenMovimientos findById(Long id) {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenMovimientos.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findAll() {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenMovimientos.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findAllActivos() {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenMovimientos.findAllActivos();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value) {
		try {
			return this.findByProperty(propertyName, value, 0);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value, int tipoMovimiento, String tipoEntrada, int limite) {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenMovimientos.findByProperty(propertyName, value, tipoMovimiento, tipoEntrada, limite);
		} catch (Exception re) {		
			throw re;
		}
	}
	
	@Override
	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value, int limite) {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenMovimientos.findByProperty(propertyName, value, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findLikeProperty(String propertyName, String value, int tipoMovimiento, String tipoEntrada, int limite) {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenMovimientos.findLikeProperty(propertyName, value, tipoMovimiento, tipoEntrada, limite);
		} catch (Exception re) {		
			throw re;
		}
	}
	
	@Override
	public List<AlmacenMovimientos> findSalidaByTraspaso(long idTraspaso, int tipoTraspaso, int limite) {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenMovimientos.findSalidaByTraspaso(idTraspaso, tipoTraspaso, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findByEspecificField(String propertyName, Object value, int tipoMovimiento) {
		try {
			this.ifzAlmacenMovimientos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenMovimientos.findByEspecificField(propertyName, value, tipoMovimiento);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public AlmacenMovimientos convertir(AlmacenMovimientosExt target) {
		return this.convertidor.AlmacenMovimientosExtToAlmacenMovimientos(target);
	}
	
	@Override
	public AlmacenMovimientosExt convertir(AlmacenMovimientos target) {
		return this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(target);
	}

	// --------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------------

	@Override
	public Long save(AlmacenMovimientosExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.AlmacenMovimientosExtToAlmacenMovimientos(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AlmacenMovimientosExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.AlmacenMovimientosExtToAlmacenMovimientos(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(AlmacenMovimientosExt entityExt) throws Exception {
		try {
			this.delete(this.convertidor.AlmacenMovimientosExtToAlmacenMovimientos(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenMovimientosExt findByIdExt(Long id) {
		try {
			return this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(this.findById(id));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientosExt> findAllExt() {
		List<AlmacenMovimientosExt> listaExt = new ArrayList<AlmacenMovimientosExt>();
		List<AlmacenMovimientos> lista = null;
		
		try {
			lista = this.findAll();
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenMovimientos am: lista)
					listaExt.add(this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(am));
			}
			
			return listaExt;
		} catch (Exception re) {		
			throw re;
		}
	}
	
	@Override
	public List<AlmacenMovimientosExt> findExtAllActivos() {
		List<AlmacenMovimientosExt> listaExt = new ArrayList<AlmacenMovimientosExt>();
		List<AlmacenMovimientos> lista = null;
		
		try {
			lista = this.findAllActivos();
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenMovimientos am: lista)
					listaExt.add(this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(am));
			}
			
			return listaExt;
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientosExt> findExtByProperty(String propertyName, Object value, int limite) {
		List<AlmacenMovimientosExt> listaExt = new ArrayList<AlmacenMovimientosExt>();
		List<AlmacenMovimientos> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenMovimientos am : lista)
					listaExt.add(this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(am));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}
	
	@Override
	public List<AlmacenMovimientosExt> findExtByProperty(String propertyName, Object value, int tipoMovimiento, String tipoEntrada, int limite) {
		List<AlmacenMovimientosExt> listaExt = new ArrayList<AlmacenMovimientosExt>();
		List<AlmacenMovimientos> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value, tipoMovimiento, tipoEntrada, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenMovimientos am : lista)
					listaExt.add(this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(am));
			}
		} catch (Exception re) {	
			throw re;
		}
		
		return listaExt;
	}
	
	@Override
	public List<AlmacenMovimientosExt> findExtLikeProperty(String propertyName, String value, int tipoMovimiento, String tipoEntrada, int limite) {
		List<AlmacenMovimientosExt> listaExt = new ArrayList<AlmacenMovimientosExt>();
		List<AlmacenMovimientos> lista = null;
		
		try {
			lista = this.findLikeProperty(propertyName, value, tipoMovimiento, tipoEntrada, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenMovimientos am : lista)
					listaExt.add(this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(am));
			}
		} catch (Exception re) { 
			throw re;
		}
		
		return listaExt;
	}
	
	@Override
	public List<AlmacenMovimientosExt> findExtSalidaByTraspaso(long idTraspaso, int tipoTraspaso, int limite) {
		List<AlmacenMovimientosExt> listaExt = new ArrayList<AlmacenMovimientosExt>();
		List<AlmacenMovimientos> lista = null;
		
		try {
			lista = this.findSalidaByTraspaso(idTraspaso, tipoTraspaso, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenMovimientos am : lista)
					listaExt.add(this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(am));
			}
		} catch (Exception re) { 
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AlmacenMovimientosExt> findExtByEspecificField(String propertyName, Object value, int tipoMovimiento ) {
		List<AlmacenMovimientosExt> listaExt = new ArrayList<AlmacenMovimientosExt>();
		List<AlmacenMovimientos> lista = null;
		
		try {
			lista = this.findByEspecificField(propertyName, value, tipoMovimiento);
			if (lista != null && ! lista.isEmpty()) {
				for(AlmacenMovimientos am:lista)
					listaExt.add(this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(am));
			}
			
			return listaExt;
		} catch (Exception re) {
			throw re;
		}
	}
	
	// -----------------------------------------------------------------------------------------------
	// PRIVADAS
	// -----------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}
