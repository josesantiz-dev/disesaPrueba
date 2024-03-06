package net.giro.inventarios.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
	private ConvertExt convertidor;
	private AlmacenDAO ifzAlmacen;
	private InfoSesion infoSesion;
	
	public AlmacenFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environment);
            this.ifzAlmacen = (AlmacenDAO) ctx.lookup("ejb:/Model_Inventarios//AlmacenImp!net.giro.inventarios.dao.AlmacenDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear AlmacenFac", e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(Almacen entity) throws Exception {
		try {
			return this.ifzAlmacen.save(entity, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.save(entity)", re);	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Almacen> saveOrUpdateList(List<Almacen> entities) throws Exception {
		try {
			return this.ifzAlmacen.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.saveOrUpdateList(entities)", re);	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(Almacen entity) throws Exception {
		try {
			this.ifzAlmacen.update(entity);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.update(entity)", re);		
			throw re;
		}
	}

	@Override
	public void delete(long idAlmacen) throws Exception {
		try {
			this.ifzAlmacen.delete(idAlmacen);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.delete(idAlmacen)", re);		
			throw re;
		}
	}

	@Override
	public Almacen findById(long idAlmacen) throws Exception {
		try {
			return this.ifzAlmacen.findById(idAlmacen);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.findById(idAlmacen)", re);		
			throw re;
		}
	}

	@Override
	public List<Almacen> findAll() throws Exception {
		try {
			return this.findAll(0, false);
		} catch (Exception re) {	
			log.error("Ocurrio un problema en AlmacenFac.findAll()", re);	
			throw re;
		}
	}

	@Override
	public List<Almacen> findByTipo(List<Integer> tipos, boolean incluyeEliminados, String orderBy) throws Exception {
		try {
			return this.ifzAlmacen.findByTipo(tipos, incluyeEliminados, getIdEmpresa(), orderBy);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.findByTipo(tipos, incluyeEliminados, orderBy)", re);	
			throw re;
		}
	}

	@Override
	public List<Almacen> findAll(int tipo) throws Exception {
		try {
			return this.findAll(tipo, false);
		} catch (Exception re) { 
			log.error("Ocurrio un problema en AlmacenFac.tipo)", re);	
			throw re;
		}
	}

	public List<Almacen> findAll(int tipo, boolean incluyeEliminados) throws Exception {
		try {
			return this.ifzAlmacen.findAll(tipo, incluyeEliminados, getIdEmpresa());
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.findAll(tipo, incluyeEliminados)", re);	
			throw re;
		}
	}
	
	@Override
	public List<Almacen> findByProperty(String propertyName, Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0, "", 0);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.findByProperty(propertyName, value)", re);	
			throw re;
		}
	}

	@Override
	public List<Almacen> findByProperty(String propertyName, Object value, int tipo, String orderBy, int limite) throws Exception {
		try {
			return this.ifzAlmacen.findByProperty(propertyName, value, tipo, false, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.findByProperty(propertyName, value, tipo)", re);	
			throw re;
		}
	}

	@Override
	public List<Almacen> findLike(String value, int tipo, boolean incluyeEliminados, String orderBy, int limite) throws Exception {
		try {
			while (value.trim().contains("  "))
				value = value.trim().replace("  ", " ");
			value = (value.trim().contains("+") ? value.trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.trim());
			value = (value.trim().contains("|") ? value.trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.trim());
			value = (value.trim().contains(" ") ? value.trim().replace(" ", "%") : value.trim());
			
			return this.ifzAlmacen.findLike(value, tipo, incluyeEliminados, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.findLike(value, tipo, incluyeEliminados, limite)", re);	
			throw re;
		}
	}

	@Override
	public List<Almacen> findLikeProperty(String propertyName, Object value, int tipo, boolean incluyeEliminados, String orderBy, int limite) throws Exception {
		try {
			if (value instanceof java.lang.String) {
				if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
					return this.findLike(value.toString(), tipo, incluyeEliminados, orderBy, limite);
				
				while (value.toString().trim().contains("  "))
					value = value.toString().trim().replace("  ", " ");
				value = (value.toString().trim().contains("+") ? value.toString().trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.toString().trim());
				value = (value.toString().trim().contains("|") ? value.toString().trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.toString().trim());
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			}
			
			return this.ifzAlmacen.findLikeProperty(propertyName, value, tipo, incluyeEliminados, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.findLikeProperty(propertyName, value, tipo, incluyeEliminados, limite)", re);	
			throw re;
		}
	}

	@Override
	public List<Almacen> findLikeProperty(String propertyName, String value) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0, false, "", 0);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.findLikeProperty(propertyName, value)", re);	
			throw re;
		}
	}

	@Override
	public List<Almacen> findLikeProperty(String propertyName, String value, int tipo) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, tipo, false, "", 0);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.findLikeProperty(propertyName, value, tipo)", re);	
			throw re;
		}
	}

	@Override
	public boolean comprobarPrincipal(long idSucursal, long idAlmacen) throws Exception {
		List<Almacen> lista = null;
		
		try {
			lista = this.ifzAlmacen.comprobarPrincipal(idSucursal, idAlmacen, getIdEmpresa());
			return lista != null && ! lista.isEmpty() ? true : false;
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.comprobarPrincipal(idSucursal, idAlmacen)", re);	
			throw re;
		}
	}
	
	@Override
	public boolean comprobarNombre(String nombre, long idAlmacen) throws Exception {
		List<Almacen> lista = null;
		
		try {
			lista = this.ifzAlmacen.comprobarNombre(nombre, idAlmacen, getIdEmpresa());
			return lista != null && ! lista.isEmpty() ? true : false;
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.comprobarNombre(nombre, idAlmacen)", re);
			throw re;
		}
	}

	public List<Almacen> findByEncargado(long idEncargado) throws Exception {
		try {
			return this.findByProperty("idEncargado", idEncargado);
		} catch (Exception re) {
			log.error("Ocurrio un problema en AlmacenFac.findByEncargado(idEncargado)", re);	
			throw re;
		}
	}
	
	@Override
	public Almacen convertir(AlmacenExt extendido) throws Exception {
		try {
			return this.convertidor.getPojo(extendido);
		} catch (Exception re) {
			log.error("Ocurrio un problema normalizar extendido", re);	
			throw re;
		}
	}

	@Override
	public AlmacenExt convertir(Almacen entity) throws Exception {
		try {
			return this.convertidor.getExtendido(entity);
		} catch (Exception re) {
			log.error("Ocurrio un problema extendider Almacen", re);	
			throw re;
		}
	}

	// --------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------

	@Override
	public Long save(AlmacenExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AlmacenExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.getPojo(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	// --------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L;
	}
	
	private Long getCodigoEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L;
	}
}
