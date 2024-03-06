package net.giro.plataforma.seguridad.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.seguridad.beans.Perfil;
import net.giro.plataforma.seguridad.dao.PerfilDAO;

@Stateless
public class PerfilesFac implements PerfilesRem {
	private static Logger log = Logger.getLogger(PerfilesFac.class);
	private InitialContext ctx = null;
	private PerfilDAO ifzPerfiles;
	
	public PerfilesFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
	        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzPerfiles =  (PerfilDAO) this.ctx.lookup("ejb:/Model_Publico//PerfilImp!net.giro.plataforma.seguridad.dao.PerfilDAO");
		} catch(Exception e) {
			log.error("Error en el metodo contexto , no se pudo crear PerfilesFac", e);
			ctx = null;
		}
	}

	@Override
	public List<Perfil> findByProperty(String columnName, Object value) {
		return this.ifzPerfiles.findByColumnName(columnName, value);
	}

	@Override
	public long save(Perfil entity) throws ExcepConstraint {
		return this.ifzPerfiles.save(entity, null);
	}

	@Override
	public void update(Perfil entity) throws ExcepConstraint {
		this.ifzPerfiles.update(entity);
	}

	@Override
	public void delete(long id) throws ExcepConstraint {
		this.ifzPerfiles.delete(id);
	}

	@Override
	public Perfil findById(long id) {
		return this.ifzPerfiles.findById(id);
	}

	@Override
	public List<Perfil> findAll() {
		return this.ifzPerfiles.findAll();
	}
}
