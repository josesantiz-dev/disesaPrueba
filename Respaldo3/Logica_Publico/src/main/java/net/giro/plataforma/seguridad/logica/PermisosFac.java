package net.giro.plataforma.seguridad.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.seguridad.TipoPermiso;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.Funcion;
import net.giro.plataforma.seguridad.beans.PermisosResponsabilidad;
import net.giro.plataforma.seguridad.beans.PermisosUsuario;
import net.giro.plataforma.seguridad.dao.AplicacionDAO;
import net.giro.plataforma.seguridad.dao.FuncionDAO;
import net.giro.plataforma.seguridad.dao.PermisosResponsabilidadesDAO;
import net.giro.plataforma.seguridad.dao.PermisosUsuariosDAO;

@Stateless
public class PermisosFac implements PermisosRem {
	private static Logger log = Logger.getLogger(PermisosFac.class);
	private InfoSesion infoSesion;
	private PermisosUsuariosDAO ifzBaseUsuario;
	private PermisosResponsabilidadesDAO ifzBaseResponsabilidad;
	private AplicacionDAO ifzAplicaciones;
	private FuncionDAO ifzFunciones;

	public PermisosFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzBaseUsuario = (PermisosUsuariosDAO) ctx.lookup("ejb:/Model_Publico//PermisosUsuariosImp!net.giro.plataforma.seguridad.dao.PermisosUsuariosDAO");
			this.ifzBaseResponsabilidad = (PermisosResponsabilidadesDAO) ctx.lookup("ejb:/Model_Publico//PermisosResponsabilidadesImp!net.giro.plataforma.seguridad.dao.PermisosResponsabilidadesDAO");
			this.ifzAplicaciones = (AplicacionDAO) ctx.lookup("ejb:/Model_Publico//AplicacionImp!net.giro.plataforma.seguridad.dao.AplicacionDAO");
			this.ifzFunciones = (FuncionDAO) ctx.lookup("ejb:/Model_Publico//FuncionImp!net.giro.plataforma.seguridad.dao.FuncionDAO");
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicilizar el EJB", e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public PermisosUsuario save(PermisosUsuario entity) throws Exception {
		try {
			if (this.infoSesion == null) {
				log.error("No se pudo comprobar la Sesion del usuario actual");
				return null;
			}
			
			if (entity != null) {
				if (entity.getId() == null || entity.getId() <= 0L) 
					entity.setId(this.ifzBaseUsuario.save(entity, getCodigoEmpresa()));
				else
					this.ifzBaseUsuario.update(entity);
			}
			return entity;
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar los permisos de Usuario", e);
			throw e;
		}
	}

	@Override
	public PermisosResponsabilidad save(PermisosResponsabilidad entity) throws Exception {
		try {
			if (this.infoSesion == null) {
				log.error("No se pudo comprobar la Sesion del usuario actual");
				return null;
			}
			
			if (entity != null) {
				if (entity.getId() == null || entity.getId() <= 0L) 
					entity.setId(this.ifzBaseResponsabilidad.save(entity, getCodigoEmpresa()));
				else
					this.ifzBaseResponsabilidad.update(entity);
			}
			return entity;
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar los permisos por Responsabilidad", e);
			throw e;
		}
	}

	@Override
	public List<PermisosUsuario> saveListByUsuario(List<PermisosUsuario> entities) throws Exception {
		try {
			if (this.infoSesion == null) {
				log.error("No se pudo comprobar la Sesion del usuario actual");
				return null;
			}
			
			return this.ifzBaseUsuario.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar los permisos de Usuario", e);
			throw e;
		}
	}

	@Override
	public List<PermisosResponsabilidad> saveListByResponsabilidad(List<PermisosResponsabilidad> entities) throws Exception {
		try {
			if (this.infoSesion == null) {
				log.error("No se pudo comprobar la Sesion del usuario actual");
				return null;
			}
			
			return this.ifzBaseResponsabilidad.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar los permisos por Responsabilidad", e);
			throw e;
		}
	}

	@Override
	public List<PermisosUsuario> findByUsuario(long idUsuario, String orderBy) throws Exception {
		try {
			if (this.infoSesion == null) {
				log.error("No se pudo comprobar la Sesion del usuario actual");
				return null;
			}
			System.out.println("Usuario " + idUsuario);
			System.out.println("Empresa " + getIdEmpresa());
			return this.ifzBaseUsuario.findAll(idUsuario, getIdEmpresa(), orderBy);
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar los permisos por Usuario", e);
			throw e;
		}
	}

	@Override
	public List<PermisosResponsabilidad> findByResponsabilidad(long idResponsabilidad, String orderBy) throws Exception {
		try {
			if (this.infoSesion == null) {
				log.error("No se pudo comprobar la Sesion del usuario actual");
				return null;
			}
			
			return this.ifzBaseResponsabilidad.findAll(idResponsabilidad, getIdEmpresa(), orderBy);
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar los permisos por Responsabilidad", e);
			throw e;
		}
	}

	@Override
	public void deleteListFromUsuario(List<PermisosUsuario> permisos) throws Exception {
		try {
			if (this.infoSesion == null) {
				log.error("No se pudo comprobar la Sesion del usuario actual");
				return;
			}
			
			if (permisos == null || permisos.isEmpty()) {
				log.error("No se indicaron permisos para borrar");
				return;
			}
			
			for (PermisosUsuario permiso : permisos) {
				if (permiso.getId() != null || permiso.getId() > 0L)
					this.ifzBaseUsuario.delete(permiso.getId());
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar borrar los permisos indicados", e);
			throw e;
		}
	}

	@Override
	public void deleteListFromResponsabilidad(List<PermisosResponsabilidad> permisos) throws Exception {
		try {
			if (this.infoSesion == null) {
				log.error("No se pudo comprobar la Sesion del usuario actual");
				return;
			}
			
			if (permisos == null || permisos.isEmpty()) {
				log.error("No se indicaron permisos para borrar");
				return;
			}
			
			for (PermisosResponsabilidad permiso : permisos) {
				if (permiso.getId() != null || permiso.getId() > 0L)
					this.ifzBaseResponsabilidad.delete(permiso.getId());
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar borrar los permisos indicados", e);
			throw e;
		}
	}

	@Override
	public boolean validarPermiso(PermisosUsuario permiso, TipoPermiso tipoPermiso) {
		if (permiso == null)
			return false;
		if (tipoPermiso == null || tipoPermiso.equals(TipoPermiso.Ninguno))
			return false;
		return (TipoPermiso.fromValue(permiso.getPermiso())).equals(tipoPermiso);
	}

	// -----------------------------------------------------------------------------------------------------------------------

	@Override
	public List<Aplicacion> getAplicaciones() throws Exception {
		try {
			return this.ifzAplicaciones.findAll(null);
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar las Aplicaciones registradas", e);
			throw e;
		}
	}

	@Override
	public List<Funcion> getFunciones(long idAplicacion) throws Exception {
		try {
			return this.ifzFunciones.findAll(idAplicacion, null);
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar las Funciones de la Aplicacion indicada", e);
			throw e;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------------------------------

	public long getIdEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L;
	}

	public long getCodigoEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L;
	}
}
