package net.giro.plataforma.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.InventariosExcluidos;
import net.giro.plataforma.dao.InventariosExcluidosDAO;

@Stateless
public class InventariosExcluidosFac implements InventariosExcluidosRem {
	private static Logger log = Logger.getLogger(InventariosExcluidosFac.class);
	private InitialContext ctx;
	private InventariosExcluidosDAO ifzInventariosExcluidos;
	private InfoSesion infoSesion;

	
	public InventariosExcluidosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzInventariosExcluidos = (InventariosExcluidosDAO) this.ctx.lookup("ejb:/Model_Publico//InventariosExcluidosImp!net.giro.plataforma.dao.InventariosExcluidosDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Publico.SubfamiliaImpuestosFac", e);
			ctx = null;
		}
	}


	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();//.getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(InventariosExcluidos entity) throws Exception {
		try {
			return this.ifzInventariosExcluidos.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.InventariosExcluidosFac.save(InventariosExcluidos)", e);
			throw e;
		}
	}

	@Override
	public List<InventariosExcluidos> saveOrUpdateList(List<InventariosExcluidos> entities) throws Exception {
		try {
			return this.ifzInventariosExcluidos.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.InventariosExcluidosFac.saveOrUpdateList(List<InventariosExcluidos> entities)", e);
			throw e;
		}
	}

	@Override
	public List<InventariosExcluidos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzInventariosExcluidos.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.InventariosExcluidosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<InventariosExcluidos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzInventariosExcluidos.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.InventariosExcluidosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}
}
