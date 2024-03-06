package net.giro.plataforma.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ImpuestosRelacionados;
import net.giro.plataforma.dao.ImpuestosRelacionadosDAO;

@Stateless
public class ImpuestosRelacionadosFac implements ImpuestosRelacionadosRem {
	private static Logger log = Logger.getLogger(ImpuestosRelacionadosFac.class);
	private InfoSesion infoSesion;
	private ImpuestosRelacionadosDAO ifzBase;
	
	public ImpuestosRelacionadosFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzBase = (ImpuestosRelacionadosDAO) ctx.lookup("ejb:/Model_Publico//ImpuestosRelacionadosImp!net.giro.plataforma.dao.ImpuestosRelacionadosDAO");
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar el EJB", e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(ImpuestosRelacionados entity) throws Exception {
		try {
			return this.ifzBase.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.ImpuestosRelacionadosFac.save(ImpuestosRelacionados)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestosRelacionados> saveOrUpdateList(List<ImpuestosRelacionados> entities) throws Exception {
		try {
			return this.ifzBase.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.ImpuestosRelacionadosFac.saveOrUpdateList(List<ImpuestosRelacionados> entities)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestosRelacionados> findByImpuesto(Long idImpuesto) throws Exception {
		try {
			return this.ifzBase.findByImpuesto(idImpuesto);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ImpuestosRelacionadosFac.findByImpuesto(List<ImpuestosRelacionados> entities)", e);
			throw e;
		}
	}

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-04-15 | Javier Tirado 	| Creacion de EJB.
 */