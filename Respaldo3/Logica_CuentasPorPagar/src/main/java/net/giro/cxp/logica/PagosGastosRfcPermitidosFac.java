package net.giro.cxp.logica;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxp.beans.PagosGastosRfcPermitidos;
import net.giro.cxp.dao.PagosGastosRfcPermitidosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class PagosGastosRfcPermitidosFac implements PagosGastosRfcPermitidosRem {
	private static Logger log = Logger.getLogger(PagosGastosDetFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private PagosGastosRfcPermitidosDAO ifzPagosGastosRfcPermitidos;
	
	public PagosGastosRfcPermitidosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
    		this.ifzPagosGastosRfcPermitidos = (PagosGastosRfcPermitidosDAO) this.ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosRfcPermitidosImp!net.giro.cxp.dao.PagosGastosRfcPermitidosDAO");
    	} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear PagosGastosRfcPermitidosFac", e);
			ctx = null;
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(PagosGastosRfcPermitidos entity) throws Exception {
		try {
			return this.ifzPagosGastosRfcPermitidos.save(entity, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Logica_CuentasPorPagar.PagosGastosRfcPermitidosFac,save(entity)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<PagosGastosRfcPermitidos> saveOrUpdateList(List<PagosGastosRfcPermitidos> entities) throws Exception {
		try {
			return this.ifzPagosGastosRfcPermitidos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Logica_CuentasPorPagar.save(entity)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(PagosGastosRfcPermitidos entity) throws Exception {
		try {
			this.ifzPagosGastosRfcPermitidos.update(entity);
		} catch (Exception re) {
			log.error("Logica_CuentasPorPagar.PagosGastosRfcPermitidosFac,update(entity)", re);
			throw re;
		}
	}

	@Override
	public PagosGastosRfcPermitidos findById(Long idPagosGastosRfcPermitidos) {
		try {
			return this.ifzPagosGastosRfcPermitidos.findById(idPagosGastosRfcPermitidos);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosRfcPermitidosFac.findById(idPagosGastosRfcPermitidos)", e);
			throw e;
		} 
	}

	@Override
	public List<PagosGastosRfcPermitidos> findAll() throws Exception {
		try {
			return this.findAll("", "");
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosRfcPermitidosFac.findById(idPagosGastosRfcPermitidos)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastosRfcPermitidos> findAll(String rfc) throws Exception {
		try {
			return this.findAll(rfc, "");
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosRfcPermitidosFac.findById(idPagosGastosRfcPermitidos)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastosRfcPermitidos> findAll(String rfc, String orderBy) throws Exception {
		try {
			return this.ifzPagosGastosRfcPermitidos.findAll(rfc, getIdEmpresa(), orderBy);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosRfcPermitidosFac.findById(idPagosGastosRfcPermitidos)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastosRfcPermitidos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosRfcPermitidosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastosRfcPermitidos> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzPagosGastosRfcPermitidos.findByProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosRfcPermitidosFac.findByProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastosRfcPermitidos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosRfcPermitidosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastosRfcPermitidos> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzPagosGastosRfcPermitidos.findLikeProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PagosGastosRfcPermitidosFac.findLikeProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public boolean comprobarPermiso(String tipoPagosGastos, String rfc, String tipoComprobante, String metodoPago) throws Exception {
		List<PagosGastosRfcPermitidos> registros = null;
		Date fecha = null;
		
		try {
			registros = this.findAll(rfc, "id desc");
			if (registros == null || registros.isEmpty())
				return false;
			
			for (PagosGastosRfcPermitidos registro : registros) {
				if (! tipoPagosGastos.equals(registro.getTipoPagosGastos())) // C | P | F ...
					continue;
				if (! tipoComprobante.equals(registro.getTipoComprobante())) // I | E | P ...
					continue;
				if (! metodoPago.equals(registro.getMetodoPagoComprobante()))// PUE | PPD ...
					continue;
				if (registro.getDeterminado() == 0)
					return registro.getPermitido() == 1;
				
				fecha = Calendar.getInstance().getTime();
				if (fecha.compareTo(registro.getFechaDesde()) < 0)
					return false;
				
				if (fecha.compareTo(registro.getFechaHasta()) > 0) {
					if (registro.getPermitido() == 1) {
						registro.setPermitido(0);
						this.update(registro);
					}
					return false;
				}
				
				if (registro.getPermitido() == 0) {
					registro.setPermitido(1);
					this.update(registro);
				}
				return true;
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar los RFC permitidos para el tipo: " + tipoPagosGastos, e);
		}
		
		return false;
	}

	// --------------------------------------------------------------------------------------
	// PRIVADOS 
	// --------------------------------------------------------------------------------------

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
