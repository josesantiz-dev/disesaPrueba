package net.giro.contabilidad.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.TransaccionesData;
import net.giro.contabilidad.dao.TransaccionesDataDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class TransaccionesDataFac implements TransaccionesDataRem {
	private static Logger log = Logger.getLogger(TransaccionesDataFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private TransaccionesDataDAO ifzTData;
	private static String orderBy;
	
	public TransaccionesDataFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzTData = (TransaccionesDataDAO) this.ctx.lookup("ejb:/Model_Contabilidad//TransaccionesDataImp!net.giro.contabilidad.dao.TransaccionesDataDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.TransaccionesDataFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void orderBy(String orderBy) {
		TransaccionesDataFac.orderBy = orderBy;
	}

	@Override
	public Long save(TransaccionesData entity) throws Exception {
		try {
			return this.ifzTData.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.save(TransaccionesData)", e);
			throw e;
		}
	}

	@Override
	public List<TransaccionesData> saveOrUpdateList(List<TransaccionesData> entities) throws Exception {
		try {
			return this.ifzTData.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.saveOrUpdateList(List<TransaccionesData> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(TransaccionesData entity) throws Exception {
		try {
			this.ifzTData.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.update(TransaccionesData)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idTransaccionesData) throws Exception {
		try {
			this.ifzTData.delete(idTransaccionesData);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.delete(idTransaccionesData)", e);
			throw e;
		}
	}

	@Override
	public void delete(List<TransaccionesData> listTransaccionesData) throws Exception {
		try {
			if (listTransaccionesData == null || listTransaccionesData.isEmpty())
				return;
			for (TransaccionesData item : listTransaccionesData) {
				if (item.getContabilizado() == 1)
					continue;
				this.delete(item.getId());
			}
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.delete(List<TransaccionesData> listTransaccionesData)", e);
			throw e;
		}
	}

	@Override
	public TransaccionesData findById(Long idTransaccionesData) {
		try {
			return this.ifzTData.findById(idTransaccionesData);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findById(idTransaccionesData)", e);
			throw e;
		}
	}

	@Override
	public List<TransaccionesData> findAll(long idMensajeTransaccion, String orderBy) throws Exception {
		try {
			return this.ifzTData.findAll(idMensajeTransaccion, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findAll(idMensajeTransaccion, orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<TransaccionesData> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzTData.orderBy(orderBy);
			return this.ifzTData.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionesData> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzTData.orderBy(orderBy);
			return this.ifzTData.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionesData> findPrevio(Long codigoTransaccion, Long poliza, Long lote) throws Exception {
		try {
			return this.ifzTData.findPrevio(codigoTransaccion, poliza, lote);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findPrevio(codigoTransaccion, poliza, lote)", e);
			throw e;
		}
	}

	@Override
	public List<TransaccionesData> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzTData.orderBy(orderBy);
			return this.ifzTData.findInProperty(columnName, values, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<TransaccionesData> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTData.orderBy(orderBy);
			return this.ifzTData.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionesData> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTData.orderBy(orderBy);
			return this.ifzTData.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}*/

	// -------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// -------------------------------------------------------------------------------------------------------------------

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