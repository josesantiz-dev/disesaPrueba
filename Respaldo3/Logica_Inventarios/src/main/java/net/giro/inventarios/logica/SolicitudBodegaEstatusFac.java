package net.giro.inventarios.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.SolicitudBodegaEstatus;
import net.giro.inventarios.dao.SolicitudBodegaEstatusDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class SolicitudBodegaEstatusFac implements SolicitudBodegaEstatusRem {
	private static Logger log = Logger.getLogger(SolicitudBodegaEstatusFac.class);
	private InfoSesion infoSesion;
	private SolicitudBodegaEstatusDAO ifzSolicitudBodegaEstatus;
	
	public SolicitudBodegaEstatusFac() {
		Hashtable<String, Object> environtment = null;
		InitialContext ctx = null;
		
		try {
			environtment = new Hashtable<String, Object>();
            environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environtment);
            this.ifzSolicitudBodegaEstatus = (SolicitudBodegaEstatusDAO) ctx.lookup("ejb:/Model_Inventarios//SolicitudBodegaEstatusImp!net.giro.inventarios.dao.SolicitudBodegaEstatusDAO");
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear SolicitudBodegaEstatusFac", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Long save(SolicitudBodegaEstatus entity) throws Exception {
		try {
			return this.ifzSolicitudBodegaEstatus.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public long save(long idSolicitud, long idMovimiento, long idTraspaso) throws Exception {
		try {
			return this.save(new SolicitudBodegaEstatus(idSolicitud, idMovimiento, idTraspaso, this.infoSesion.getAcceso().getUsuario().getId(), getIdEmpresa()));
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public List<SolicitudBodegaEstatus> saveOrUpdateList(List<SolicitudBodegaEstatus> entities) throws Exception {
		try {
			return this.ifzSolicitudBodegaEstatus.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public SolicitudBodegaEstatus saveMovimiento(long idSolicitud, long idMovimiento) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SolicitudBodegaEstatus saveTraspaso(long idSolicitud, long idTraspaso) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void update(SolicitudBodegaEstatus entity) throws Exception {
		try {
			this.ifzSolicitudBodegaEstatus.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(long idSolicitudBodegaEstatus) throws Exception {
		try {
			this.ifzSolicitudBodegaEstatus.delete(idSolicitudBodegaEstatus);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public SolicitudBodegaEstatus findById(long idSolicitudBodegaEstatus) {
		try {
			return this.ifzSolicitudBodegaEstatus.findById(idSolicitudBodegaEstatus);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<SolicitudBodegaEstatus> findAll(long idSolicitud, String orderBy) throws Exception {
		try {
			return this.ifzSolicitudBodegaEstatus.findAll(idSolicitud, orderBy);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<SolicitudBodegaEstatus> findAllByMovimiento(long idSolicitud, long idMovimiento) throws Exception {
		try {
			return this.ifzSolicitudBodegaEstatus.findAllByMovimiento(idSolicitud, idMovimiento);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<SolicitudBodegaEstatus> findAllByTraspaso(long idSolicitud, long idTraspaso) throws Exception {
		try {
			return this.ifzSolicitudBodegaEstatus.findAllByTraspaso(idSolicitud, idTraspaso);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public SolicitudBodegaEstatus findLast(long idSolicitud) throws Exception {
		List<SolicitudBodegaEstatus> lista = null;

		try {
			lista = this.findAll(idSolicitud, null);
			if (lista == null || lista.isEmpty())
				return null;
			return lista.get(0);
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
 *  3.1 | 2019-07-16 | Javier Tirado 	| Creacion de Facade
 */
