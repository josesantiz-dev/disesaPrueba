package net.giro.plataforma.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.ne.beans.EmpresasUsuarios;
import net.giro.ne.dao.EmpresasUsuariosDAO;
import net.giro.plataforma.InfoSesion;

import org.apache.log4j.Logger;

@Stateless
public class EmpresasUsuariosFac implements EmpresasUsuariosRem {
	private static Logger log = Logger.getLogger(EmpresasUsuariosFac.class);
	private InitialContext ctx = null;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private EmpresasUsuariosDAO ifzEmpresasUsuarios;
	private String orderBy;

	
	public EmpresasUsuariosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
    		this.ifzEmpresasUsuarios = (EmpresasUsuariosDAO) this.ctx.lookup("ejb:/Model_Publico//EmpresasUsuariosImp!net.giro.ne.dao.EmpresasUsuariosDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear el Facade para EmpresasUsuarios", e);
    		ctx = null;
		}
	}
	
	
	@Override
	public void orderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(EmpresasUsuarios entity) throws Exception {
		try {
			return this.ifzEmpresasUsuarios.save(entity, null);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresasUsuariosFac.save(EmpresasUsuarios)", e);
			throw e;
		}
	}

	@Override
	public List<EmpresasUsuarios> saveOrUpdateList(List<EmpresasUsuarios> entities) throws Exception {
		try {
			return this.ifzEmpresasUsuarios.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresasUsuariosFac.saveOrUpdateList(EmpresasUsuarios)", e);
			throw e;
		}
	}

	@Override
	public void update(EmpresasUsuarios entity) throws Exception {
		try {
			this.ifzEmpresasUsuarios.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresasUsuariosFac.update(EmpresasUsuarios)", e);
			throw e;
		}
	}

	@Override
	public void delete(long entityId) throws Exception {
		try {
			this.ifzEmpresasUsuarios.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresasUsuariosFac.delete(EmpresasUsuarios)", e);
			throw e;
		}
	}

	@Override
	public List<EmpresasUsuarios> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzEmpresasUsuarios.orderBy(this.orderBy);
			return this.ifzEmpresasUsuarios.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresasUsuariosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<EmpresasUsuarios> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzEmpresasUsuarios.orderBy(this.orderBy);
			return this.ifzEmpresasUsuarios.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresasUsuariosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}
}
