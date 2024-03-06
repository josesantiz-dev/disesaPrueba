package net.giro.plataforma.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ImpuestoEquivalencia;
import net.giro.plataforma.dao.ImpuestoEquivalenciaDAO;

@Stateless
public class ImpuestoEquivalenciaFac implements ImpuestoEquivalenciaRem {
	private static Logger log = Logger.getLogger(InventariosExcluidosFac.class);
	private InitialContext ctx;
	private ImpuestoEquivalenciaDAO ifzImpEquivalencia;
	private InfoSesion infoSesion;

	
	public ImpuestoEquivalenciaFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzImpEquivalencia = (ImpuestoEquivalenciaDAO) this.ctx.lookup("ejb:/Model_Publico//ImpuestoEquivalenciaImp!net.giro.plataforma.dao.ImpuestoEquivalenciaDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Publico.SubfamiliaImpuestosFac", e);
			ctx = null;
		}
	}


	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(ImpuestoEquivalencia entity) throws Exception {
		try {
			return this.ifzImpEquivalencia.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.ImpuestoEquivalenciaFac.save(ImpuestoEquivalencia)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestoEquivalencia> saveOrUpdateList(List<ImpuestoEquivalencia> entities) throws Exception {
		try {
			return this.ifzImpEquivalencia.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.ImpuestoEquivalenciaFac.saveOrUpdateList(List<ImpuestoEquivalencia> entities)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestoEquivalencia> findByTransaccion(Long codigoTransaccion) throws Exception {
		try {
			return this.ifzImpEquivalencia.findByTransaccion(codigoTransaccion);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ImpuestoEquivalenciaFac.findByTransaccion(Long codigoTransaccion)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestoEquivalencia> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzImpEquivalencia.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ImpuestoEquivalenciaFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestoEquivalencia> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzImpEquivalencia.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ImpuestoEquivalenciaFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}
}
