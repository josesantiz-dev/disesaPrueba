package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.dao.TraspasoDetalleDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class TraspasoDetalleFac implements TraspasoDetalleRem {
	private static Logger log = Logger.getLogger(TraspasoDetalleFac.class);
	private InfoSesion infoSesion;
	private TraspasoDetalleDAO ifzTraspasoDetalle;
	private ConvertExt convertidor;
	
	public TraspasoDetalleFac() {
		Hashtable<String, Object> environtment = null;
		InitialContext ctx = null;
		
		try {
			environtment = new Hashtable<String, Object>();
            environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environtment);
            this.ifzTraspasoDetalle = (TraspasoDetalleDAO) ctx.lookup("ejb:/Model_Inventarios//TraspasoDetalleImp!net.giro.inventarios.dao.TraspasoDetalleDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear TraspasoDetalleFac", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Long save(TraspasoDetalle entity) throws Exception {
		try {
			return this.ifzTraspasoDetalle.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public List<TraspasoDetalle> saveOrUpdateList(List<TraspasoDetalle> entities) throws Exception {
		try {
			return this.ifzTraspasoDetalle.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void update(TraspasoDetalle entity) throws Exception {
		try {
			this.ifzTraspasoDetalle.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(long idTraspasoDetalle) throws Exception {
		try {
			this.ifzTraspasoDetalle.delete(idTraspasoDetalle);
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	public void deleteAll(List<TraspasoDetalle> entities) throws Exception {
		try {
			if (entities == null || entities.isEmpty())
				return;
			for (TraspasoDetalle entity : entities)
				this.delete(entity.getId());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public TraspasoDetalle findById(long idTraspasoDetalle) {
		try {
			return this.ifzTraspasoDetalle.findById(idTraspasoDetalle);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<TraspasoDetalle> findAll(long idAlmacenTraspaso) {
		try {
			return this.ifzTraspasoDetalle.findAll(idAlmacenTraspaso);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalle> findByProperty(String propertyName, Object value, long idAlmacenTraspaso) {
		try {
			return this.ifzTraspasoDetalle.findByProperty(propertyName, value, idAlmacenTraspaso);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalle> findLikeProperty(String propertyName, Object value, long idAlmacenTraspaso) {
		try {
			return this.ifzTraspasoDetalle.findLikeProperty(propertyName, value, idAlmacenTraspaso);
		} catch (Exception re) {
			throw re;
		}
	}

	// -------------------------------------------------------------------------------
	// CONVERTIDORES
	// -------------------------------------------------------------------------------

	@Override
	public TraspasoDetalle convertir(TraspasoDetalleExt entity) {
		return this.convertidor.getPojo(entity);
	}

	@Override
	public TraspasoDetalleExt convertir(TraspasoDetalle entity) {
		return this.convertidor.getExtendido(entity);
	}

	@Override
	public List<TraspasoDetalle> convertirLista(List<TraspasoDetalleExt> extendidos) {
		List<TraspasoDetalle> entities = new ArrayList<TraspasoDetalle>();
        
        try {
            if (extendidos == null || extendidos.isEmpty()) 
                return entities;
            for (TraspasoDetalleExt item : extendidos) 
                entities.add(this.convertidor.getPojo(item));
        } catch (Exception re) {        
            throw re;
        }

        return entities;
	}

	@Override
	public List<TraspasoDetalleExt> extenderLista(List<TraspasoDetalle> entities) {
		List<TraspasoDetalleExt> listaExt = new ArrayList<TraspasoDetalleExt>();
        
        try {
            if (entities == null || entities.isEmpty()) 
                return listaExt;
            for (TraspasoDetalle item : entities) 
                listaExt.add(this.convertidor.getExtendido(item));
        } catch (Exception re) {        
            throw re;
        }

        return listaExt;
	}

	// -------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------

	@Override
	public Long save(TraspasoDetalleExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalleExt> saveOrUpdateListExt(List<TraspasoDetalleExt> entities) throws Exception {
		List<TraspasoDetalle> lista = null;
		
		try {
			lista = new ArrayList<TraspasoDetalle>();
			for (TraspasoDetalleExt item : entities)
				lista.add(this.convertidor.getPojo(item));
			lista = this.saveOrUpdateList(lista);
			entities.clear();
			for (TraspasoDetalle a : lista)
				entities.add(this.convertidor.getExtendido(a));
		} catch (Exception re) {		
			throw re;
		}
		
		return entities;
	}

	@Override
	public void update(TraspasoDetalleExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.getPojo(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void deleteAllExt(List<TraspasoDetalleExt> extendidos) throws Exception {
		try {
			if (extendidos == null || extendidos.isEmpty())
				return;
			for (TraspasoDetalleExt entity : extendidos)
				this.delete(entity.getId());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public TraspasoDetalleExt findByIdExt(long idTraspasoDetalle) {
		try {
			return this.convertidor.getExtendido(this.ifzTraspasoDetalle.findById(idTraspasoDetalle));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalleExt> findExtAll(long idAlmacenTraspaso) {
		List<TraspasoDetalleExt> listaExt = new ArrayList<>();
		List<TraspasoDetalle> lista = null;
		
		try {
			lista = this.findAll(idAlmacenTraspaso);
			if (lista != null && ! lista.isEmpty()) {
				for (TraspasoDetalle a : lista)
					listaExt.add(this.convertidor.getExtendido(a));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<TraspasoDetalleExt> findExtByProperty(String propertyName, Object value, long idAlmacenTraspaso) {
		List<TraspasoDetalleExt> listaExt = new ArrayList<>();
		List<TraspasoDetalle> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value, idAlmacenTraspaso);
			if (lista != null && ! lista.isEmpty()) {
				for (TraspasoDetalle a : lista)
					listaExt.add(this.convertidor.getExtendido(a));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<TraspasoDetalleExt> findExtLikeProperty(String propertyName, Object value, long idAlmacenTraspaso) {
		List<TraspasoDetalleExt> listaExt = new ArrayList<>();
		List<TraspasoDetalle> lista = null;
		
		try {
			lista = this.findLikeProperty(propertyName, value, idAlmacenTraspaso);
			if (lista != null && ! lista.isEmpty()) {
				for (TraspasoDetalle a : lista)
					listaExt.add(this.convertidor.getExtendido(a));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	// -----------------------------------------------------------------------------------------------
	// PRIVADAS
	// -----------------------------------------------------------------------------------------------
	
	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
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