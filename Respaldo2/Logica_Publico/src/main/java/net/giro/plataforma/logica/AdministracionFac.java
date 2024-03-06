package net.giro.plataforma.logica;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.dao.EmpresaDAO;
import net.giro.ne.dao.SucursalDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.Utilerias;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.beans.UsuarioExt;
import net.giro.plataforma.dao.ConGrupoValoresDAO;
import net.giro.plataforma.dao.ConValoresDAO;
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
import net.giro.plataforma.seguridad.dao.AplicacionDAO;
import net.giro.plataforma.seguridad.dao.FuncionDAO;
import net.giro.plataforma.seguridad.dao.MenuDAO;
import net.giro.plataforma.seguridad.dao.MenuFuncionDAO;
import net.giro.plataforma.seguridad.dao.PerfilDAO;
import net.giro.plataforma.seguridad.dao.PerfilValorDAO;
import net.giro.plataforma.seguridad.dao.ResponsabilidadDAO;
import net.giro.plataforma.seguridad.dao.UsuarioResponsabilidadDAO;
import net.giro.plataforma.seguridad.dao.UsuariosDAO;
import net.giro.plataforma.seguridad.logica.AutentificacionFac;
import net.giro.publico.util.Errores;
import net.giro.respuesta.Respuesta;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

@Stateless
public class AdministracionFac implements AdministracionRem {
	private static Logger log = Logger.getLogger(AutentificacionFac.class);
	private static String modulo = "PUBLICO";
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
    @Resource
    private SessionContext sctx;
	private PerfilDAO ifzPerfiles;
	private PerfilValorDAO ifzPerfilValores;
	private ResponsabilidadDAO ifzResponsabilidad;
	private ConValoresDAO ifzConValores;
	private ConGrupoValoresDAO ifzGroConValores;
	private UsuarioResponsabilidadDAO  ifzUsuarioResp;
	private UsuariosDAO ifzUsuarios;
	private AplicacionDAO ifzAplicaciones;
	private MenuDAO ifzMenus;
	private MenuFuncionDAO ifzMenuFunciones;
	private FuncionDAO ifzFunciones;
	private EmpresaDAO ifzEmpresas;
	private SucursalDAO ifzSucursales;
    
	
	public AdministracionFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		infoSesion = new InfoSesion();
    		ifzUsuarios =  (UsuariosDAO)ctx.lookup("ejb:/Model_Publico//UsuariosImp!net.giro.plataforma.seguridad.dao.UsuariosDAO");
    		ifzUsuarioResp = (UsuarioResponsabilidadDAO)ctx.lookup("ejb:/Model_Publico//UsuarioResponsabilidadImp!net.giro.plataforma.seguridad.dao.UsuarioResponsabilidadDAO");
    		ifzAplicaciones = (AplicacionDAO)ctx.lookup("ejb:/Model_Publico//AplicacionImp!net.giro.plataforma.seguridad.dao.AplicacionDAO");
    		ifzMenus = (MenuDAO)ctx.lookup("ejb:/Model_Publico//MenuImp!net.giro.plataforma.seguridad.dao.MenuDAO");
    		ifzMenuFunciones = (MenuFuncionDAO)ctx.lookup("ejb:/Model_Publico//MenuFuncionImp!net.giro.plataforma.seguridad.dao.MenuFuncionDAO");
    		ifzFunciones = (FuncionDAO)ctx.lookup("ejb:/Model_Publico//FuncionImp!net.giro.plataforma.seguridad.dao.FuncionDAO");
    		ifzPerfiles = (PerfilDAO)ctx.lookup("ejb:/Model_Publico//PerfilImp!net.giro.plataforma.seguridad.dao.PerfilDAO");
    		ifzPerfilValores = (PerfilValorDAO)ctx.lookup("ejb:/Model_Publico//PerfilValorImp!net.giro.plataforma.seguridad.dao.PerfilValorDAO");
    	    ifzResponsabilidad=(ResponsabilidadDAO)ctx.lookup("ejb:/Model_Publico//ResponsabilidadImp!net.giro.plataforma.seguridad.dao.ResponsabilidadDAO");
    	    ifzConValores =  (ConValoresDAO)ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
    	    ifzGroConValores=(ConGrupoValoresDAO)ctx.lookup("ejb:/Model_Publico//ConGrupoValoresImp!net.giro.plataforma.dao.ConGrupoValoresDAO");
    	    ifzEmpresas =(EmpresaDAO)ctx.lookup("ejb:/Model_Publico//EmpresaImp!net.giro.ne.dao.EmpresaDAO");
    	    ifzSucursales =(SucursalDAO)ctx.lookup("ejb:/Model_Publico//SucursalImp!net.giro.ne.dao.SucursalDAO");
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}	
	}
	
	
	@Override
	public Respuesta buscarFuncion(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		
		try{
			List<Funcion> listFunciones = ifzFunciones.findLikeColumnName(tipoBusqueda,valorBusqueda);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listFunciones", listFunciones);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_FUNCION);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.buscarFuncion", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta buscarConGroVal(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		try{
			List<ConGrupoValores> listGrupoValores = ifzGroConValores.findLikeColumnName(tipoBusqueda,valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listGrupoValores", listGrupoValores);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_GRUPO_VALORES);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.buscarConGroVal", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta buscarAplicacion(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		try{
			List<Aplicacion> listAplicaciones = ifzAplicaciones.findLikeColumnName(tipoBusqueda,valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listAplicaciones", listAplicaciones);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_APLICACION);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.buscarAplicacion", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarPerfil(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		try {
			List<Perfil> listPerfiles = ifzPerfiles.findLikeColumnName(tipoBusqueda, valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listPerfiles", listPerfiles);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_PERFIL);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.buscarPerfil", e);
		}
		return respuesta;
	}
	
	@Override
	public List<Usuario> buscarUsuario(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint{
		return ifzUsuarios.findLikeColumnName(tipoBusqueda, valorBusqueda);
	}
	
	@Override
	public List<UsuarioResponsabilidad> buscarUsuarioResponsabilidad(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint{
		return ifzUsuarioResp.findLikeColumnName(tipoBusqueda, valorBusqueda);
	}
	
	@Override
	public Respuesta listarUsuarioResponsabilidad(UsuarioExt usuarioExt){
		Respuesta respuesta = new Respuesta();
		try{
			Usuario usuario = new Usuario();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(usuario, usuarioExt);	
			List<UsuarioResponsabilidad> listUsuarioResponsabilidad = ifzUsuarioResp.findByColumnName("usuario", usuario);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listUsuarioResponsabilidad", listUsuarioResponsabilidad);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_RESPONSABILIDADES);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.listarUsuarioResponsabilidad", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarResponsabilidad(String tipoBusqueda, String valorBusqueda) {
		Respuesta respuesta = new Respuesta();
		try{
			List<Responsabilidad> listResponsabilidades = ifzResponsabilidad.findLikeColumnName(tipoBusqueda,valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listResponsabilidades", listResponsabilidades);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_RESPONSABILIDAD);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.buscarResponsabilidad", e);
		}
		return respuesta;
	}
	
	
	@Override
	public Respuesta buscarMenu(String property, Object value){
		Respuesta respuesta = new Respuesta();
		try{
			List<Menu> listMenus;
			if ("".equals(property))
				listMenus = ifzMenus.findAll();
			else
				listMenus = ifzMenus.findLikeColumnName(property, String.valueOf(value));
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMenus", listMenus);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_MENU);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.buscarMenu", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta autocompletarMenu(){
		Respuesta respuesta = new Respuesta();
		try {
			List<Menu> listMenus = ifzMenus.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMenus", listMenus);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_MENUS);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.autocompletarMenu", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta autocompletarAplicacion(){
		Respuesta respuesta = new Respuesta();
		try{
			List<Aplicacion> listAplicaciones = ifzAplicaciones.findAll();	
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listAplicaciones", listAplicaciones);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_APLICACIONES);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.autocompletarAplicacion", e);
		}
			
		return respuesta;
	}
	
	@Override
	public Respuesta autocompletarResponsabilidad() {
		Respuesta respuesta = new Respuesta();
		try{
			List<Responsabilidad> listResponsabilidades = ifzResponsabilidad.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listResponsabilidades", listResponsabilidades);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_RESPONSABILIDADES);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.autocompletarResponsabilidad", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta findLikeNombreAplicacion(String nombFuncion,Aplicacion app, int max){
		Respuesta respuesta = new Respuesta();
		
		try{
			List<Funcion> listFunciones = ifzFunciones.findLikeNombreAplicacion(nombFuncion, app, max);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listFunciones", listFunciones);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_FUNCION);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.findLikeNombreAplicacion", e);
		}
		
		return respuesta;
	}
	
	@Override
	public List<PerfilValorGral> findPerfilValorGralByNivelPerfil(String valor, HashMap<Integer, String> params) throws Exception{
		try{
			
		} catch (Exception e){
			throw e;
		}
		return null;
	}
	
	@Override
	public Respuesta findConValoresById(long id){
		Respuesta respuesta = new Respuesta();
		try{
			ConValores pojoValores = ifzConValores.findById(id);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoValores", pojoValores);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_VALOR);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.findConValoresById", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta findMenuFuncionById(long id) {
		Respuesta respuesta = new Respuesta();
		try {
			MenuFuncion pojoMenuFuncion = ifzMenuFunciones.findById(id);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoMenuFuncion", pojoMenuFuncion);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_MENU_FUNCION);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.finMenuFuncionById", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta findMenuFuncionByMenuId(long id){
		Respuesta respuesta = new Respuesta();
		
		try {
			List<MenuFuncion> listMenuFuncion = ifzMenuFunciones.findByMenuId(id);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMenuFuncion", listMenuFuncion);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_MENU_FUNCION);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.findMenuFuncionByMenuId", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta findByNivelPerfil(String nombrePerfil, HashMap<Integer, String> param) {
		List<PerfilValorGral> res = new ArrayList<PerfilValorGral>();
		Respuesta respuesta = new Respuesta();
		List<Perfil> pg = null;
		
		PerfilValor perVal = null;
		
		String cadenaPerfiles = null;
		String valorNivel = null;
		int i = 0;
		try {
			
			pg = ifzPerfiles.findLikeColumnName("perfil", nombrePerfil);
			for(Perfil p:pg)
				res.add(new PerfilValorGral(p));
			
			for(PerfilValorGral pvg:res){
				cadenaPerfiles = Utilerias.getNiveles((int)pvg.getPojoPerfil().getNivelPerfil());
				for(i = 1; i <= 64 ; i = 2 * i)
					if(tienePermiso(i, cadenaPerfiles) && param.get(i) != null){
						valorNivel = param.get(i);
						perVal = ifzPerfilValores.findByPerfilNivelValor(pvg.getPojoPerfil(), Long.valueOf(i), Long.valueOf(valorNivel));
						if(perVal==null){
							perVal = new PerfilValor();
							perVal.setNivel(Long.valueOf(i));
							perVal.setPerfil(pvg.getPojoPerfil());
						}
						
						pvg.getDescripciones().put(String.valueOf(i), getValorByTipo(pvg.getPojoPerfil(), perVal.getValorPerfil()));
						
						switch(i){
							case 1: pvg.setPojoTerminal(perVal); break;
							case 2: pvg.setPojoUsuario(perVal); break;
							case 4: pvg.setPojoResponsabilidad(perVal); break;
							case 8: pvg.setPojoPuesto(perVal); break;
							case 16: pvg.setPojoSucursal(perVal); break;
							case 32: pvg.setPojoEmpresa(perVal); break;
							case 64: pvg.setPojoSitio(perVal); break;
						}
					}
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listPerfilValorGrl", res);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_PERFILES_VALORES);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.findByNivelPerfil", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Funcion findFuncionById(long id) throws Exception{
		return ifzFunciones.findById(id);
	}
	
	@Override
	public Respuesta findConValoresByGrupoNombre(String grupoNombre){
		Respuesta respuesta = new Respuesta();
		try{
			List<ConValores> listValores = ifzConValores.findByGrupoNombre(grupoNombre);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listValores", listValores);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_VALORES);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.findConValoresByGrupoNombre", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta findUsuariosLikeClaveUsuario(String clave){
		Respuesta respuesta = new Respuesta();
		try{
			List<Usuario> listUsuarios = ifzUsuarios.findLikePropiedad("id", clave);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listUsuarios", listUsuarios);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_USUARIOS);
			respuesta.setBody(null);
			log.error("Error en  AdministracionFac.findUsuariosLikeClaveUsuario", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta findResponsabilidadesLikeClaveUsuario(String clave){
		Respuesta respuesta = new Respuesta();
		try{
			List<Responsabilidad> listResponsabilidades = ifzResponsabilidad.findLikePropiedad("id", clave);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listResponsabilidades", listResponsabilidades);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_RESPONSABILIDADES);
			respuesta.setBody(null);
			log.error("Error en el metodo findUsuariosLikeClaveUsuario",e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta findEmpresasLikeClaveUsuario(String clave){
		Respuesta respuesta = new Respuesta();
		try{
			List<Empresa> listEmpresas = ifzEmpresas.findLikePropiedad("id", clave);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEmpresas", listEmpresas);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_EMPRESA);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.findUsuariosLikeClaveUsuario",e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta findSucursalesLikeClaveUsuario(String clave) {
		Respuesta respuesta = new Respuesta();
		try{
			List<Sucursal> listSucursales = ifzSucursales.findLikePropiedad("id", clave);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listSucursales", listSucursales);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LISTAR_SUCURSAL);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.findUsuariosLikeClaveUsuario",e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta salvar(PerfilValor perfilValor){
		Respuesta respuesta = new Respuesta();
		try{
			perfilValor.setModificadoPor(infoSesion.getAcceso().getId());
			perfilValor.setFechaModificacion(Calendar.getInstance().getTime());
			if(perfilValor.getId() > 0){
				ifzPerfilValores.update(perfilValor);
			}
			else{
				perfilValor.setCreadoPor(infoSesion.getAcceso().getId());
				perfilValor.setFechaCreacion(Calendar.getInstance().getTime());
				perfilValor.setId(ifzPerfilValores.save(perfilValor, null));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoPerfilValor", perfilValor);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_PERFILES_VALORES);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.salvar",e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta salvar(ConGrupoValores congrupovalores){
		Respuesta respuesta = new Respuesta();
		try { 
			congrupovalores.setModificadoPor(infoSesion.getAcceso().getId());
			congrupovalores.setFechaModificacion(Calendar.getInstance().getTime());
			if (congrupovalores.getId() <= 0){
				congrupovalores.setCreadoPor(infoSesion.getAcceso().getId());
				congrupovalores.setFechaCreacion(Calendar.getInstance().getTime());
				congrupovalores.setId(ifzGroConValores.save(congrupovalores, null));
			}
			else
				ifzGroConValores.update(congrupovalores);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoGrupoValores", congrupovalores);
		} catch(Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_GRUPO_VALORES);
			respuesta.setBody(null);
			log.error("Error al AdministracionFac.salvar para ConGrupoValores", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta  salvar(Responsabilidad responsabilidad){
		Respuesta respuesta = new Respuesta();
		try{
			responsabilidad.setModificadoPor(infoSesion.getAcceso().getId());
			responsabilidad.setFechaModificacion(Calendar.getInstance().getTime());
			if(responsabilidad.getId() > 0){
				this.ifzResponsabilidad.update(responsabilidad);
			}else{
				responsabilidad.setCreadoPor(infoSesion.getAcceso().getId());
				responsabilidad.setFechaCreacion(Calendar.getInstance().getTime());
				responsabilidad.setId(this.ifzResponsabilidad.save(responsabilidad, null));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoResponsabilidad", responsabilidad);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_RESPONSABILIDAD);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.salvar para Responsabilidad", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta salvar(UsuarioResponsabilidad usuarioresponsabilidad){
		Respuesta respuesta = new Respuesta();
		
		try {
			if(usuarioresponsabilidad.getFechaIni().after(usuarioresponsabilidad.getFechaTerm())){
				respuesta.getErrores().addCodigo(modulo, Errores.RANGO_FECHAS_INVALIDO);
				respuesta.setBody(null);
				
				return respuesta;
			}
			
			usuarioresponsabilidad.setModificadoPor(infoSesion.getAcceso().getId());
			usuarioresponsabilidad.setFechaModificacion(Calendar.getInstance().getTime());
			if (usuarioresponsabilidad.getId() <= 0){
				usuarioresponsabilidad.setCreadoPor(infoSesion.getAcceso().getId());
				usuarioresponsabilidad.setFechaCreacion(Calendar.getInstance().getTime());
				usuarioresponsabilidad.setId(ifzUsuarioResp.save(usuarioresponsabilidad, null));
			}
			else
				ifzUsuarioResp.update(usuarioresponsabilidad);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoUsuarioResponsabilidad", usuarioresponsabilidad);
		} catch(Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_AGREGAR_RESPONSABILIDAD);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.salvar para UsuarioResponsabilidad", e);
		}
		return respuesta;
	}
	
	@Override
	public long salvar(Usuario usuario) throws ExcepConstraint{
		try{
			usuario.setModificadoPor(infoSesion.getAcceso().getId());
			usuario.setFechaModificacion(Calendar.getInstance().getTime());
			if (usuario.getId() <= 0)
				this.ifzUsuarios.update(usuario);
			else{
				usuario.setCreadoPor(infoSesion.getAcceso().getId());
				usuario.setFechaCreacion(Calendar.getInstance().getTime());
				return ifzUsuarios.save(usuario, null);
			}
		} catch (Exception e) {
			log.error("Error al salvar usuario");
		}
		return usuario.getId();
	}
	
	@Override
	public Respuesta salvar(Perfil perfil){
		Respuesta respuesta = new Respuesta();
		
		try {
			perfil.setModificadoPor(infoSesion.getAcceso().getId());
			perfil.setFechaModificacion(Calendar.getInstance().getTime());
			if (perfil.getId() <= 0){
				perfil.setCreadoPor(infoSesion.getAcceso().getId());
				perfil.setFechaCreacion(Calendar.getInstance().getTime());
				perfil.setId(ifzPerfiles.save(perfil, null));
			}
			else
				ifzPerfiles.update(perfil);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoPerfil", perfil);
		} catch(Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_FUNCION);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.salvar para Perfil", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta salvar(Funcion funcion){
		Respuesta respuesta = new Respuesta();
		try {
			funcion.setModificadoPor(infoSesion.getAcceso().getId());
			funcion.setFechaModificacion(Calendar.getInstance().getTime());
			if (funcion.getId() <= 0){
				funcion.setCreadoPor(infoSesion.getAcceso().getId());
				funcion.setFechaCreacion(Calendar.getInstance().getTime());
				funcion.setId(ifzFunciones.save(funcion, null));
			}
			else
				ifzFunciones.update(funcion);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoFuncion", funcion);
		} catch(Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_FUNCION);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.salvar para funcion", e);
		}
		return respuesta;
	}
	
	@Override
	public long salvar(Aplicacion aplicacion)throws ExcepConstraint {
		try {
			aplicacion.setModificadoPor(infoSesion.getAcceso().getId());
			aplicacion.setFechaModificacion(Calendar.getInstance().getTime());
			if (aplicacion.getId() <= 0){
				aplicacion.setCreadoPor(infoSesion.getAcceso().getId());
				aplicacion.setFechaCreacion(Calendar.getInstance().getTime());
				return ifzAplicaciones.save(aplicacion, null);
			}
			else
				ifzAplicaciones.update(aplicacion);
		} catch(Exception e) {
			log.error("Error al salvar funcion", e);
		}
		return aplicacion.getId();
	}
	
	@Override
	public Respuesta salvar(Menu menu){
		Respuesta respuesta = new Respuesta();
		try {
			menu.setModificadoPor(infoSesion.getAcceso().getId());
			menu.setFechaModificacion(Calendar.getInstance().getTime());
			if (menu.getId() <= 0){
				menu.setCreadoPor(infoSesion.getAcceso().getId());
				menu.setFechaCreacion(Calendar.getInstance().getTime());
				menu.setId(ifzMenus.save(menu, null));
			}
			else
				ifzMenus.update(menu);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoMenu", menu);
		} catch(Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_MENU);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac para Menu", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta salvar(MenuFuncion menuFuncion){
		Respuesta respuesta = new Respuesta();
		try {
			menuFuncion.setModificadoPor(infoSesion.getAcceso().getId());
			menuFuncion.setFechaModificacion(Calendar.getInstance().getTime());
			if (menuFuncion.getId() <= 0){
				menuFuncion.setCreadoPor(infoSesion.getAcceso().getId());
				menuFuncion.setFechaCreacion(Calendar.getInstance().getTime());
				menuFuncion.setId(ifzMenuFunciones.save(menuFuncion, null));
			}
			else
				ifzMenuFunciones.update(menuFuncion);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoMenuFuncion", menuFuncion);
		} catch(Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_MENU_FUNCION);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.salvar para MenuFuncion", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminar(PerfilValor perfilValor){
		Respuesta respuesta = new Respuesta();
		try{
			ifzPerfilValores.delete(perfilValor.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch(Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_PERFILES_VALORES);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.eliminar para PerfilValor", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminar(Perfil perfil) {
		Respuesta respuesta = new Respuesta();
		try {
			if (perfil.getId() > 0)				
				ifzPerfiles.delete(perfil.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (ExcepConstraint e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_PERFIL);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.eliminar para Perfil", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminar(ConGrupoValores congrupovalores){
		Respuesta respuesta = new Respuesta();
		try {
			if (congrupovalores.getId() > 0)				
				ifzGroConValores.delete(congrupovalores.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (ExcepConstraint e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_GRUPO_VALORES);
			respuesta.setBody(null);
			log.error("Error al eliminar funcion", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminar(Responsabilidad responsabilidad){
		Respuesta respuesta = new Respuesta();
		try {
			if (responsabilidad.getId() > 0)				
				ifzResponsabilidad.delete(responsabilidad.getId());
			respuesta.getErrores().setCodigoError(0L);
		} catch (ExcepConstraint e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_RESPONSABILIDAD);
			respuesta.setBody(null);
			log.error("Error al eliminar funcion", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminar(UsuarioResponsabilidad usuarioResponsabilidad){
		Respuesta respuesta = new Respuesta();
		try {
			if (usuarioResponsabilidad.getId() > 0)				
				ifzUsuarioResp.delete(usuarioResponsabilidad.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (ExcepConstraint e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_QUITAR_RESPONSABILIDAD);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.eliminar para UsuarioResponsabilidad", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta eliminar(Menu menu){
		Respuesta respuesta = new Respuesta();
		try {
			if (menu.getId() > 0)
				ifzMenus.deleteByMenu(menu);
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_MENU);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.eliminar", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminar(MenuFuncion menuFuncion){
		Respuesta respuesta = new Respuesta();
		try {
			if (menuFuncion.getId() > 0)				
				ifzMenuFunciones.delete(menuFuncion.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (ExcepConstraint e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_MENU_FUNCION);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.eliminar para MenuFuncion", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminar(Funcion funcion){
		Respuesta respuesta = new Respuesta();
		try {
			if (funcion.getId() > 0)				
				ifzFunciones.delete(funcion.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (ExcepConstraint e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_FUNCION);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.eliminar para Funcion", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta  buscarUsuarioExt(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		List<UsuarioExt> listUsuariosExt = new ArrayList<UsuarioExt>(); 
		try {
			List<Usuario> listUsuarios = ifzUsuarios.findLikeColumnName(tipoBusqueda, valorBusqueda);
			for(Usuario usuario : listUsuarios){
				UsuarioExt usuarioExt = new UsuarioExt();
				try {
					BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
					BeanUtils.copyProperties(usuarioExt, usuario);			
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				listUsuariosExt.add(usuarioExt);
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listUsuarios", listUsuariosExt);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_USUARIO);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta salvar(UsuarioExt usuarioExt){
		Respuesta respuesta = new Respuesta();
		Usuario usuario = new Usuario();
		
		try {
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(usuario, usuarioExt);	
			
			if(usuario.getFechaInicio().after(usuario.getFechaTerminacion())){
				respuesta.getErrores().addCodigo(modulo, Errores.RANGO_FECHAS_INVALIDO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if (usuario.getIdEmpleado() == null || usuario.getIdEmpleado() <= 0L)
				usuario.setIdEmpleado(1L);
			
			usuario.setModificadoPor(infoSesion.getAcceso().getId());
			usuario.setFechaModificacion(Calendar.getInstance().getTime());
			if (usuario.getId() > 0)
				this.ifzUsuarios.update(usuario);
			else{
				usuario.setCreadoPor(infoSesion.getAcceso().getId());
				usuario.setFechaCreacion(Calendar.getInstance().getTime());
				usuario.setId(ifzUsuarios.save(usuario, null));
			}
			
			BeanUtils.copyProperties(usuarioExt, usuario);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoUsuario", usuarioExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_USUARIO);
			respuesta.setBody(null);
			log.error("Error en AdministracionFac.salvar para UsuarioExt", e);
		}
		
		
		return respuesta;
	}
	
	@Override
	public String buscarPerfilPersona(String string) throws ExcepConstraint {
		return "S";
	}
	
	@Override
	public InfoSesion getInfoSesion() {
		return infoSesion;
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public String getNiveles(long niveles){
		return Utilerias.getNiveles((int)niveles);
	}
	
	private boolean tienePermiso(int nivel, String niveles){
		for(String str : niveles.split(","))
			if(Integer.valueOf(str) == nivel)
				return true;
		
		return false;
	}
	
	private String getValorByTipo(Perfil perfil, String id){
		String res = "";
		ConValores valor;
		try {
			if(id == null)
				return "";
			
			
			if("comboGrupo".equals(perfil.getTipoComponente()) && perfil.getComponenteSrc() != null){
				valor = ifzConValores.findById(Long.valueOf(id));
				res = valor != null ? valor.getValor() : "";
			}else if("check".equals(perfil.getTipoComponente())){
				res = "S".equals(id) ? "Habilitado" : "Deshabilitado" ;
			}else
				res = id;
		} catch (Exception e) {
			res = null;
			log.error("Error en el metodo getValorByTipo", e);
		}
		return res;
	}
	
	@Override
	public Aplicacion findAplicacionById(long id) {
		try {
			return this.ifzAplicaciones.findById(id);
		} catch (Exception e){
			log.error("Error en AdministracionFac.findAplicacionById", e);
			throw e;
		}
	}
}
