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

import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.dao.MovimientosDetalleDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class MovimientosDetalleFac implements MovimientosDetalleRem {
	private static Logger log = Logger.getLogger(MovimientosDetalleFac.class);
	private InfoSesion infoSesion;
	private MovimientosDetalleDAO ifzMovimientos;
	private ConvertExt convertidor;
	
	public MovimientosDetalleFac() {
		Hashtable<String, Object> environtment = null;
		InitialContext ctx = null;
		
		try {
			environtment = new Hashtable<String, Object>();
            environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environtment);
            this.ifzMovimientos = (MovimientosDetalleDAO) ctx.lookup("ejb:/Model_Inventarios//MovimientosDetalleImp!net.giro.inventarios.dao.MovimientosDetalleDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar el EJB", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Long save(MovimientosDetalle entity) throws Exception {
		try {
            return this.ifzMovimientos.save(entity, getCodigoEmpresa());
        } catch (Exception re) {    
            throw re;
        }
	}
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public List<MovimientosDetalle> saveOrUpdateList(List<MovimientosDetalle> entities) throws Exception {
		try {
            return this.ifzMovimientos.saveOrUpdateList(entities, getCodigoEmpresa());
        } catch (Exception re) {
            throw re;
        }
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void update(MovimientosDetalle entity) throws Exception {
		try {
            this.ifzMovimientos.update(entity);
        } catch (Exception re) {    
            throw re;
        }
	}

	@Override
	public void delete(long idMovimientosDetalle) throws Exception {
		try {
            this.ifzMovimientos.delete(idMovimientosDetalle);
        } catch (Exception re) {    
            throw re;
        }
	}

	@Override
	public void delete(MovimientosDetalle entity) throws Exception {
		try {
            this.delete(entity.getId());
        } catch (Exception re) {    
            throw re;
        }
	}

	@Override
	public MovimientosDetalle findById(Long idMovimientosDetalle) throws Exception {
		try {
            return this.ifzMovimientos.findById(idMovimientosDetalle);
        } catch (Exception re) {    
            throw re;
        }
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MovimientosDetalle> findAll(long idAlmacenMovimiento) throws Exception {
		try {
            return this.ifzMovimientos.findAll(idAlmacenMovimiento);
        } catch (Exception re) {        
            throw re;
        }
	}

	@Override
	public List<MovimientosDetalle> findLikeProperty(String propertyName, Object value, long idAlmacenMovimiento) throws Exception {
		try {
            return this.ifzMovimientos.findLikeProperty(propertyName, value, idAlmacenMovimiento);
        } catch (Exception re) {
            throw re;
        }
	}

	@Override
	public List<MovimientosDetalle> findByProperty(String propertyName, Object value, long idAlmacenMovimiento) throws Exception {
		try {
            return this.ifzMovimientos.findByProperty(propertyName, value, idAlmacenMovimiento);
        } catch (Exception re) {
            throw re;
        }
	}

	// --------------------------------------------------------------------------------------------
	// CONVERTIDOR 
	// --------------------------------------------------------------------------------------------
	
	@Override
	public MovimientosDetalle convertir(MovimientosDetalleExt target) throws Exception {
		return this.convertidor.getPojo(target);
	}

	@Override
	public MovimientosDetalleExt convertir(MovimientosDetalle target) throws Exception {
		return this.convertidor.getExtendido(target);
	}

	@Override
	public List<MovimientosDetalle> convertirLista(List<MovimientosDetalleExt> extendidos) throws Exception {
		List<MovimientosDetalle> entities = new ArrayList<MovimientosDetalle>();
        
        try {
            if (extendidos == null || extendidos.isEmpty()) 
                return entities;
            for (MovimientosDetalleExt item : extendidos) 
                entities.add(this.convertidor.getPojo(item));
        } catch (Exception re) {        
            throw re;
        }

        return entities;
	}

	@Override
	public List<MovimientosDetalleExt> extenderLista(List<MovimientosDetalle> entities) throws Exception {
		List<MovimientosDetalleExt> extendidos = new ArrayList<MovimientosDetalleExt>();
        
        try {
            if (entities == null || entities.isEmpty()) 
                return extendidos;
            for (MovimientosDetalle item : entities) 
                extendidos.add(this.convertidor.getExtendido(item));
        } catch (Exception re) {        
            throw re;
        }

        return extendidos;
	}

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	@Override
	public List<MovimientosDetalleExt> saveOrUpdateListExt(List<MovimientosDetalleExt> entities) throws Exception {
		List<MovimientosDetalleExt> resultados = null;
		List<MovimientosDetalle> targets = null;
		
		try {
			targets = this.convertirLista(entities);
			targets = this.saveOrUpdateList(targets);
			resultados = this.extenderLista(targets);
		 } catch (Exception re) {        
            throw re;
        }

        return resultados;
	}

	@Override
	public List<MovimientosDetalleExt> findAllExt(long idAlmacenMovimiento) throws Exception {
		List<MovimientosDetalleExt> listaExt = new ArrayList<MovimientosDetalleExt>();
        List<MovimientosDetalle> lista = null;
        
        try {
            lista = this.findAll(idAlmacenMovimiento);
            if (lista != null && ! lista.isEmpty()) {
                for (MovimientosDetalle md : lista)
                    listaExt.add(this.convertidor.getExtendido(md));
            }
        } catch (Exception re) {        
            throw re;
        }
        
        return listaExt;
	}

	/*@Override
	public Long save(MovimientosDetalleExt entityExt) throws Exception {
		try {
            return this.save(this.convertidor.getPojo(entityExt));
        } catch (Exception re) {    
            throw re;
        }
	}

	@Override
	public void update(MovimientosDetalleExt extendido) throws Exception {
		try {
            this.update(this.convertidor.getPojo(extendido));
        } catch (Exception re) {    
            throw re;
        }
	}

	@Override
	public MovimientosDetalleExt findByIdExt(Long idMovimientosDetalle) {
		try {
            return this.convertidor.getExtendido(this.ifzMovimientos.findById(idMovimientosDetalle));
        } catch (Exception re) {    
            throw re;
        }
	}

	@Override
	public List<MovimientosDetalleExt> findExtByProperty(String propertyName, Object value, long idAlmacenMovimiento) {
		List<MovimientosDetalleExt> listaExt = new ArrayList<MovimientosDetalleExt>();
        List<MovimientosDetalle> lista = null;
        
        try {
            lista = this.findByProperty(propertyName, value, idAlmacenMovimiento);
            if (lista != null && ! lista.isEmpty()) {
                for (MovimientosDetalle a : lista)
                    listaExt.add(this.convertidor.getExtendido(a));
            }
        } catch (Exception re) {
            throw re;
        }
        
        return listaExt;
	}

	@Override
	public List<MovimientosDetalleExt> findExtLikeProperty(String propertyName, Object value, long idAlmacenMovimiento) {
		List<MovimientosDetalleExt> listaExt = new ArrayList<MovimientosDetalleExt>();
        List<MovimientosDetalle> lista = null;
        
        try {
            lista = this.findLikeProperty(propertyName, value, idAlmacenMovimiento);
            if (lista != null && ! lista.isEmpty()) {
                for (MovimientosDetalle a : lista)
                    listaExt.add(this.convertidor.getExtendido(a));
            }
        } catch (Exception re) {
            throw re;
        }
        
        return listaExt;
	}

	@Override
	public List<MovimientosDetalleExt> findDetallesExtByIdOrdenCompra(long idOrdenCompra) {
		return null;
	}*/

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
