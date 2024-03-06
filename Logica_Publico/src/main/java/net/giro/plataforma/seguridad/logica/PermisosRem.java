package net.giro.plataforma.seguridad.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.seguridad.TipoPermiso;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.Funcion;
import net.giro.plataforma.seguridad.beans.PermisosResponsabilidad;
import net.giro.plataforma.seguridad.beans.PermisosUsuario;

@Remote
public interface PermisosRem { 
	public void setInfoSesion(InfoSesion infoSesion);
	
	public PermisosUsuario save(PermisosUsuario entity) throws Exception;
	
	public PermisosResponsabilidad save(PermisosResponsabilidad entity) throws Exception;
	
	public List<PermisosUsuario> saveListByUsuario(List<PermisosUsuario> entities) throws Exception;
	
	public List<PermisosResponsabilidad> saveListByResponsabilidad(List<PermisosResponsabilidad> entities) throws Exception;
	
	public List<PermisosUsuario> findByUsuario(long idUsuario, String orderBy) throws Exception;
	
	public List<PermisosResponsabilidad> findByResponsabilidad(long idResponsabilidad, String orderBy) throws Exception;
	
	public void deleteListFromUsuario(List<PermisosUsuario> permisos) throws Exception;
	
	public void deleteListFromResponsabilidad(List<PermisosResponsabilidad> permisos) throws Exception;
	
	public boolean validarPermiso(PermisosUsuario permiso, TipoPermiso tipoPermiso);

	// -----------------------------------------------------------------------------------------------------------------------
	
	public List<Aplicacion> getAplicaciones() throws Exception;
	
	public List<Funcion> getFunciones(long idAplicacion) throws Exception;
}
