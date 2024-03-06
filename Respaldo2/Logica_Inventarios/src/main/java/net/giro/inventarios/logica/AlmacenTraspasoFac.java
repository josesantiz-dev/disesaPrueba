package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.dao.AlmacenTraspasoDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class AlmacenTraspasoFac implements AlmacenTraspasoRem {
	private static Logger log = Logger.getLogger(AlmacenTraspasoFac.class);
	private InitialContext ctx;
	private ConvertExt convertidor;
	private AlmacenTraspasoDAO ifzAlmacenTraspaso;
	private InfoSesion infoSesion;
	
	
	public AlmacenTraspasoFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzAlmacenTraspaso = (AlmacenTraspasoDAO) this.ctx.lookup("ejb:/Model_Inventarios//AlmacenTraspasoImp!net.giro.inventarios.dao.AlmacenTraspasoDAO");
            this.convertidor = new ConvertExt(); 
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear AlmacenTraspasoFac", e);
			ctx = null;
		}
	}
	

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(AlmacenTraspaso entity) throws Exception {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.save(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> saveOrUpdateList(List<AlmacenTraspaso> entities) throws Exception {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.saveOrUpdateList(entities);
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	public void update(AlmacenTraspaso entity) throws Exception {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			this.ifzAlmacenTraspaso.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(AlmacenTraspaso entity) throws Exception {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			this.ifzAlmacenTraspaso.delete(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenTraspaso findById(Long id) {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findAll() {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findAllActivos() {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findAllActivos();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findByProperty(String propertyName, Object value) {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findLikeProperty(String propertyName, String value) {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findLikeProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<AlmacenTraspaso> findByAlmacen(String nombreAlmacen) {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findByAlmacen(nombreAlmacen);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findByAlmacenOrigen(String nombreAlmacen) {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findByAlmacenOrigen(nombreAlmacen);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findByAlmacenDestino(String nombreAlmacen) {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findByAlmacenDestino(nombreAlmacen);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen) {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findByAlmacenOrdenCompleta(tipoAlmacen, nombreAlmacen);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findLikeWithDestino(String value, long idAlmacenDestino) {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findLikeWithDestino(value, idAlmacenDestino);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findIncompletosLikeProperty(String propertyName, String propertyValue, String orderBy, int limite) {
		try {
			this.ifzAlmacenTraspaso.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenTraspaso.findIncompletosLikeProperty(propertyName, propertyValue, orderBy, limite);
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	public AlmacenTraspaso convertir(AlmacenTraspasoExt entity) {
		return this.convertidor.AlmacenTraspasoExtToAlmacenTraspaso(entity);
	}

	@Override
	public AlmacenTraspasoExt convertir(AlmacenTraspaso entity) {
		return this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(entity);
	}

	// -----------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------
	
	@Override
	public Long save(AlmacenTraspasoExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.AlmacenTraspasoExtToAlmacenTraspaso(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AlmacenTraspasoExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.AlmacenTraspasoExtToAlmacenTraspaso(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(AlmacenTraspasoExt entityExt) throws Exception {
		try {
			this.delete(this.convertidor.AlmacenTraspasoExtToAlmacenTraspaso(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenTraspasoExt findByIdExt(Long id) {
		try {
			return this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(this.findById(id));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspasoExt> findAllExt() {
		List<AlmacenTraspasoExt> listaExt = new ArrayList<AlmacenTraspasoExt>();
		List<AlmacenTraspaso> lista = null;
		
		try {
			lista = this.findAll();
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenTraspaso at : lista)
					listaExt.add(this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at));
			}
		} catch (Exception re) {		
			throw re;
		}

		return listaExt;
	}
	
	@Override
	public List<AlmacenTraspasoExt> findExtLikeProperty(String propertyName, String value) {
		List<AlmacenTraspasoExt> listaExt = new ArrayList<AlmacenTraspasoExt>();
		List<AlmacenTraspaso> lista = null;
		
		try {
			lista = this.findLikeProperty(propertyName, value);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenTraspaso at : lista)
					listaExt.add(this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}
	
	@Override
	public List<AlmacenTraspasoExt> findExtByAlmacen(String nombreAlmacen) {
		List<AlmacenTraspasoExt> listaExt = new ArrayList<AlmacenTraspasoExt>();
		List<AlmacenTraspaso> lista = null;
		
		try {
			lista = this.findByAlmacen(nombreAlmacen);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenTraspaso at : lista)
					listaExt.add(this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AlmacenTraspasoExt> findExtByAlmacenOrigen(String nombreAlmacen) {
		List<AlmacenTraspasoExt> listaExt = new ArrayList<AlmacenTraspasoExt>();
		List<AlmacenTraspaso> lista = null;
		
		try {
			lista = this.findByAlmacenOrigen(nombreAlmacen);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenTraspaso at : lista)
					listaExt.add(this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AlmacenTraspasoExt> findExtByAlmacenDestino(String nombreAlmacen) {
		List<AlmacenTraspasoExt> listaExt = new ArrayList<AlmacenTraspasoExt>();
		List<AlmacenTraspaso> lista = null;
		
		try {
			lista = this.findByAlmacenDestino(nombreAlmacen);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenTraspaso at : lista)
					listaExt.add(this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AlmacenTraspasoExt> findExtByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen) {
		List<AlmacenTraspasoExt> listaExt = new ArrayList<AlmacenTraspasoExt>();
		List<AlmacenTraspaso> lista = null;
		
		try {
			lista = this.findByAlmacenOrdenCompleta(tipoAlmacen, nombreAlmacen);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenTraspaso at : lista)
					listaExt.add(this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AlmacenTraspasoExt> findExtLikeWithDestino(String value, long idAlmacenDestino) {
		List<AlmacenTraspasoExt> listaExt = new ArrayList<AlmacenTraspasoExt>();
		List<AlmacenTraspaso> lista = null;
		
		try {
			lista = this.findLikeWithDestino(value, idAlmacenDestino);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenTraspaso at : lista)
					listaExt.add(this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}
	
	@Override
	public List<AlmacenTraspasoExt> findExtIncompletosLikeProperty(String propertyName, String propertyValue, String orderBy, int limite) {
		List<AlmacenTraspasoExt> listaExt = new ArrayList<AlmacenTraspasoExt>();
		List<AlmacenTraspaso> lista = null;
		
		try {
			lista = this.findIncompletosLikeProperty(propertyName, propertyValue, orderBy, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenTraspaso at : lista)
					listaExt.add(this.convertidor.AlmacenTraspasoToAlmacenTraspasoExt(at));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
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

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Añado disponibilidad del convertidor
 */