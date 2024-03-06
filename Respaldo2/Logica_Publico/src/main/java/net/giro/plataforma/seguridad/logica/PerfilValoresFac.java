package net.giro.plataforma.seguridad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.seguridad.beans.Perfil;
import net.giro.plataforma.seguridad.beans.PerfilValor;
import net.giro.plataforma.seguridad.dao.PerfilValorDAO;

@Stateless
public class PerfilValoresFac implements PerfilValoresRem {
	private static Logger log = Logger.getLogger(PerfilValoresFac.class);
	private InitialContext ctx = null;
	
	private PerfilValorDAO ifzPerfilValores;
	
	public PerfilValoresFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
	        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzPerfilValores = (PerfilValorDAO) this.ctx.lookup("ejb:/Model_Publico//PerfilValorImp!net.giro.plataforma.seguridad.dao.PerfilValorDAO");
		} catch(Exception e) {
			log.error("Error en el metodo contexto , no se pudo crear PerfilValoresFac", e);
			ctx = null;
		}
	}

	@Override
	public long save(PerfilValor entity) throws ExcepConstraint {
		try {
			return this.ifzPerfilValores.save(entity, null);
		} catch(Exception e) {
			throw e;
		}
	}

	@Override
	public void update(PerfilValor entity) throws ExcepConstraint {
		try {
			this.ifzPerfilValores.update(entity);
		} catch(Exception e) {
			throw e;
		}
	}

	@Override
	public void delete(long id) throws ExcepConstraint {
		try {
			this.ifzPerfilValores.delete(id);
		} catch(Exception e) {
			throw e;
		}
	}

	@Override
	public PerfilValor findById(long id) {
		try {
			return this.ifzPerfilValores.findById(id); 
		} catch(Exception e) {
			throw e;
		}
	}

	@Override
	public List<PerfilValor> findAll() {
		try {
			return this.ifzPerfilValores.findAll();
		} catch(Exception e) {
			throw e;
		}
	}

	@Override
	public List<PerfilValor> findByProperty(String columnName, Object value) {
		try {
			return this.ifzPerfilValores.findByColumnName(columnName, value);
		} catch(Exception e) {
			throw e;
		}
	}

	@Override
	public PerfilValor findByPerfilNivelValor(Perfil pg, Long nivel, Long valorNivel) throws Exception {
		try {
			return this.ifzPerfilValores.findByPerfilNivelValor(pg, nivel, valorNivel);
		} catch(Exception e) {
			throw e;
		}
	}

	@Override
	public String findPerfil(Perfil entity, HashMap<Integer, String> datosUsuario) throws Exception {
		try {
			return this.ifzPerfilValores.findPerfil(entity, datosUsuario);
		} catch (Exception e) {
			throw e;
		}
	}
}
