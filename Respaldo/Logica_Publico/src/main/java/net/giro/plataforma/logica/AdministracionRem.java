package net.giro.plataforma.logica;


import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.UsuarioExt;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.Funcion;
import net.giro.plataforma.seguridad.beans.Menu;
import net.giro.plataforma.seguridad.beans.MenuFuncion;
import net.giro.plataforma.seguridad.beans.Perfil;
import net.giro.plataforma.seguridad.beans.PerfilValor;
import net.giro.plataforma.seguridad.beans.PerfilValorGral;
import net.giro.plataforma.seguridad.beans.Responsabilidad;
import net.giro.plataforma.seguridad.beans.Usuario;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;
import net.giro.publico.respuesta.Respuesta;



@Remote
public interface AdministracionRem {
	
	public Respuesta autocompletarAplicacion();
	
	public Respuesta buscarFuncion(String tipoBusqueda, String valorBusqueda);
	public Respuesta buscarMenu(String property, Object value);
	public Respuesta buscarAplicacion(String tipoBusqueda, String valorBusqueda);
	public Respuesta buscarPerfil(String tipoBusqueda, String valorBusqueda);
	public Respuesta buscarResponsabilidad(String tipoBusqueda, String valorBusqueda);
	public Respuesta buscarUsuarioExt(String tipoBusqueda, String valorBusqueda);	
	
	
	public Respuesta autocompletarResponsabilidad();
	public Respuesta autocompletarMenu();
	public Respuesta buscarConGroVal(String tipoBusqueda,String valorBusqueda);
	
	public Respuesta findLikeNombreAplicacion(String nombFuncion,Aplicacion app, int max);
	public Respuesta findMenuFuncionById(long id);
	public Respuesta findMenuFuncionByMenuId(long id);
	public Respuesta findConValoresByGrupoNombre(String grupoNombre);
	public Respuesta findUsuariosLikeClaveUsuario(String clave);
	public Respuesta findEmpresasLikeClaveUsuario(String clave);
	public Respuesta findSucursalesLikeClaveUsuario(String clave);
	public Respuesta findResponsabilidadesLikeClaveUsuario(String clave);
	public Respuesta findConValoresById(long id);
	public Respuesta findByNivelPerfil(String nombrePerfil, HashMap<Integer, String> param);
	public Aplicacion findAplicacionById(long id);
	
	
	public Respuesta listarUsuarioResponsabilidad(UsuarioExt usuarioExt);
	
	public Respuesta salvar(Funcion funcion);
	public Respuesta salvar(ConGrupoValores congrupovalores);
	public Respuesta salvar(Menu menu);
	public Respuesta salvar(MenuFuncion menuFuncion);
	public Respuesta salvar(Perfil perfil);
	public Respuesta salvar(Responsabilidad responsabilidad);
	public Respuesta salvar(UsuarioResponsabilidad usuarioresponsabilidad);
	public Respuesta salvar(UsuarioExt usuarioExt);
	public Respuesta salvar(PerfilValor perfilValor);
	
	
	

	public Respuesta eliminar(Funcion funcion);
	public Respuesta eliminar(ConGrupoValores congrupovalores);
	public Respuesta eliminar(Menu menu);
	public Respuesta eliminar(MenuFuncion menuFuncion);
	public Respuesta eliminar(Perfil perfil);
	public Respuesta eliminar(Responsabilidad responsabilidad);
	public Respuesta eliminar(UsuarioResponsabilidad usuarioResponsabilidad);
	public Respuesta eliminar(PerfilValor perfilValor);
//	List<Empleado> buscarEmpleado(String tipoBusqueda, String valorBusqueda)
//			throws ExcepConstraint;
	
	public List<Usuario> buscarUsuario(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint;
	public List<UsuarioResponsabilidad> buscarUsuarioResponsabilidad(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint;
	public String buscarPerfilPersona(String string) throws ExcepConstraint;
	
	public Funcion findFuncionById(long id) throws Exception;
	public List<PerfilValorGral> findPerfilValorGralByNivelPerfil(String valor, HashMap<Integer, String> params) throws Exception;
	
	public long salvar(Usuario usuario)throws ExcepConstraint;
	public long salvar(Aplicacion aplicacion)throws ExcepConstraint;
	
	public InfoSesion getInfoSesion();
	public void setInfoSesion(InfoSesion infoSesion);
	public String getNiveles(long niveles);
	
	
	}
