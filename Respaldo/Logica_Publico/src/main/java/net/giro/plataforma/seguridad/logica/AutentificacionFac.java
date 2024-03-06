/***
 * valida al usuario contra la base de datos, ademas, permite obtener las sucursales visibles,
 * responsabilidades, menus y campos a deshabilidar en las pantallas.
 * @author (Guitate) Omar Magdiel Aguayo Garcia
 */

package net.giro.plataforma.seguridad.logica;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.Pagina;
import net.giro.plataforma.SingletonSesion;
import net.giro.plataforma.seguridad.Login;
import net.giro.plataforma.seguridad.beans.Acceso;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.Funcion;
import net.giro.plataforma.seguridad.beans.Menu;
import net.giro.plataforma.seguridad.beans.MenuFuncion;
import net.giro.plataforma.seguridad.beans.NivelPerfil;
import net.giro.plataforma.seguridad.beans.Usuario;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;
import net.giro.plataforma.seguridad.dao.AccesosDAO;
import net.giro.plataforma.seguridad.dao.AplicacionDAO;
import net.giro.plataforma.seguridad.dao.FuncionDAO;
import net.giro.plataforma.seguridad.dao.MenuDAO;
import net.giro.plataforma.seguridad.dao.MenuFuncionDAO;
import net.giro.plataforma.seguridad.dao.UsuarioResponsabilidadDAO;
import net.giro.plataforma.seguridad.dao.UsuariosDAO;
import net.giro.plataforma.seguridad.beans.Perfil;
import net.giro.plataforma.seguridad.dao.PerfilDAO;
import org.apache.log4j.Logger;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Stateful
public class AutentificacionFac implements AutentificacionRem {
	private static Logger log = Logger.getLogger(AutentificacionFac.class);
	private InitialContext ctx = null;
	private UsuarioResponsabilidadDAO  ifzUsuarioResp;
	private UsuariosDAO ifzUsuarios;
	private AccesosDAO ifzAccesos;
	private AplicacionDAO ifzAplicaciones;
	private MenuDAO ifzMenus;
	private MenuFuncionDAO ifzMenuFunciones;
	private FuncionDAO ifzFunciones;
	private PerfilDAO ifzPerfiles;
    @Resource
    private SessionContext sctx;

    private Connection 	connection = null;	
	private DataSource 	dataSource  = null;
	
    @EJB
    SingletonSesion sesion;    
	private InfoSesion infoSesion = null;
 
	
	public AutentificacionFac() {
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    		ifzUsuarios 	 = (UsuariosDAO) ctx.lookup("ejb:/Model_Publico//UsuariosImp!net.giro.plataforma.seguridad.dao.UsuariosDAO");
    		ifzAccesos 		 = (AccesosDAO) ctx.lookup("ejb:/Model_Publico//AccesosImp!net.giro.plataforma.seguridad.dao.AccesosDAO");
    		ifzUsuarioResp 	 = (UsuarioResponsabilidadDAO) ctx.lookup("ejb:/Model_Publico//UsuarioResponsabilidadImp!net.giro.plataforma.seguridad.dao.UsuarioResponsabilidadDAO");
    		ifzAplicaciones  = (AplicacionDAO) ctx.lookup("ejb:/Model_Publico//AplicacionImp!net.giro.plataforma.seguridad.dao.AplicacionDAO");
    		ifzMenus 		 = (MenuDAO) ctx.lookup("ejb:/Model_Publico//MenuImp!net.giro.plataforma.seguridad.dao.MenuDAO");
    		ifzMenuFunciones = (MenuFuncionDAO) ctx.lookup("ejb:/Model_Publico//MenuFuncionImp!net.giro.plataforma.seguridad.dao.MenuFuncionDAO");
    		ifzFunciones 	 = (FuncionDAO) ctx.lookup("ejb:/Model_Publico//FuncionImp!net.giro.plataforma.seguridad.dao.FuncionDAO");
    		ifzPerfiles 	 = (PerfilDAO) ctx.lookup("ejb:/Model_Publico//PerfilImp!net.giro.plataforma.seguridad.dao.PerfilDAO");
    		
    		dataSource 		 = (DataSource) ctx.lookup( "java:/giroDS" ); 
    	} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}	
	}

	
	public InfoSesion getInfoSesion() {
		return infoSesion;
	}
	
	public List<UsuarioResponsabilidad> getUsuarioResponsabilidades() {
		List<UsuarioResponsabilidad> usuarioResponsabilidades = null;
		
		try {
			usuarioResponsabilidades = new ArrayList<UsuarioResponsabilidad>();
			if (infoSesion != null) {
				List<UsuarioResponsabilidad> lstTmpusuarioResponsabilidades = ifzUsuarioResp.findByColumnName("usuario", infoSesion.getAcceso().getUsuario());
				for(UsuarioResponsabilidad ur : lstTmpusuarioResponsabilidades) 
					if (ur.getResponsabilidad() != null && ur.getResponsabilidad().getAplicacion().getId() == infoSesion.getAcceso().getAplicacion().getId() )
						usuarioResponsabilidades.add(ur);
			}
		} catch(Exception e){
			log.error("Error en el metodo getUsuarioResponsabilidades", e);
		}
		
		return usuarioResponsabilidades;
	}
	
	public int conectar(Login login) {
		int result = -1;
		try{
			infoSesion = null;
			List<Usuario> lstUsuarios = ifzUsuarios.findByColumnName("usuario", login.getUsuario());
			if (lstUsuarios.size() != 1) {
				return 1;
			}
			
			Usuario usuario = lstUsuarios.get(0);
			Aplicacion aplicacion = ifzAplicaciones.findById(login.getAplicacionId());
//			if (! usuario.getPassword().equals(login.getPassword())) {			
//				return 1;
//				
//			}
//			 
//			if(usuario.getBloqueado() == 1L)
//				result = 2;
//			else if(usuario.getExpirado() == 1L)
//				result = 3;
//			else if(usuario.getFechaCambio() == null || Utilerias.getDifDiasFechas(usuario.getFechaCambio(), Calendar.getInstance().getTime()) >= 30 ){
//				usuario.setBloqueado(1L);
//				ifzUsuarios.update(usuario);
//				result = 4;
//			} else{
				result = 0;
				nuevaSesion(login,usuario,aplicacion);
				usuario.setUltimoAcceso(Calendar.getInstance().getTime());
				ifzUsuarios.update(usuario);
//			}
		}catch(Exception re){
			result = -1;
			log.error("Error en el metodo login", re);
		}
		
		return result;
	}
	
	private void nuevaSesion(Login login, Usuario usuario, Aplicacion aplicacion) throws Exception {
		Acceso acceso = new Acceso();
		acceso.setUsuario(usuario);
		acceso.setInicio(Calendar.getInstance().getTime());
		acceso.setIpTerminal(login.getIpTerminal());
		acceso.setTerminal(login.getTerminal());
		acceso.setId(ifzAccesos.save(acceso));
		acceso.setAplicacion(aplicacion);
		infoSesion = new InfoSesion();
		infoSesion.setAcceso(acceso);
		sesion.registrar(infoSesion);
	}
	
	public void setResponsabilidad(UsuarioResponsabilidad usuarioResponsabilidad)  { 
		infoSesion.setResponsabilidad(usuarioResponsabilidad);
	}

	public void desconectar(InfoSesion infoSesion){
			sesion.logout(infoSesion);
	}
	
	/***
	 * 
	 * @param nivelPerfiles es el ArrayList de niveles que contienen los perfiles del usuario, cada objeto NivelPerfil tiene el numero de nivel
	 * y un ArrayList de Perfiles el cual contiene la informacion del perfil como; id del registro, nombre del nivel y el detalle del mismo
	 * @param perfilBuscado: define el perfil a buscar en la lista
	 * @return true si lo encuentra, en caso contrario false
	 */
	public static boolean tienePerfil(ArrayList<NivelPerfil> nivelPerfiles, String perfilBuscado){
//		try{
//			//recorremos entre los niveles en que se encontro perfiles del usuario
//			for(NivelPerfil nPerfil:nivelPerfiles){
//				//recorremos los perfiles del nivel
//				for(PerfilTmp p: nPerfil.getDetalles()){
//					if(p.getDetalle().equals(perfilBuscado))
//						return true;
//				}
//			}
//		}catch(Exception re){log.error("Error en el metodo tienePerfil", re);}
		return false;
	}
	
	public Pagina getMenu(Menu menu){
		Pagina 	root = null;
		try {
			//Menu menu = getInfoSesion().getResponsabilidad().getResponsabilidad().getMenu();
			if(menu != null) {
				root = new Pagina("", menu.getMenu(),this.getSubmenu(menu),menu.getDescripcion(), "", menu.getId()*-1);
			}
		} catch(Exception e){
			log.error("Error en el metodo getMenu", e);
		} 
		return root;
	}
	
	private ArrayList<Pagina> getSubmenu(Menu menu){
		ArrayList<Pagina> paginasSubMenu = new ArrayList<Pagina>();
		
		Pagina p = null;
		try {
			ifzMenuFunciones.orderBy("prompt");
			List<MenuFuncion> lstOpciones =  ifzMenuFunciones.findByProperty("menu", menu, 0);
			//List<MenuFuncion> lstOpciones =  ifzMenuFunciones.findByColumnName("menu", menu);
			for(MenuFuncion menuFuncion : lstOpciones) {
				   p = new Pagina("", menuFuncion.getPrompt(), null, menuFuncion.getDescripcion(), "", menuFuncion.getId());
			       if (menuFuncion.getTipoAccion().equals("M")) {
			    	   Menu subMenu = ifzMenus.findById(menuFuncion.getTipoId());
			    	   if (subMenu != null) { 
			    		   p.setSubMenu(getSubmenu(subMenu));
			    	   	   p.setIcono(0);
			    	   }
			       } else {
			    	   Funcion funcion = ifzFunciones.findById(menuFuncion.getTipoId());
			           if (funcion != null){
			                p.setUrl(funcion.getForma());
			                p.setId(funcion.getIcono());
			                p.setDescripcion(funcion.getDescripcion());
			                p.setParametros(funcion.getParametros());
			           }
			       }
			       paginasSubMenu.add(p);
			    }
		} catch(Exception e ) {
			 log.error("Error en el metodo getSubmenu", e);
		}
		return paginasSubMenu;
	}
	
	public String getSucursalesVisibles(long usuarioId){
		/*Object 			lookup = null;
		List<Object>	listResult = null;
		NQueryFacadeLocal	ifzQuery = null;
		
		try {
			lookup = context.lookup("NQueryFacade/local");
			ifzQuery = (NQueryFacadeLocal) PortableRemoteObject.narrow(lookup, NQueryFacadeLocal.class);
			
			listResult = ifzQuery.findNativeQuery("select (sucursales_visibles(" + usuarioId + ")) as result");
			return !listResult.isEmpty() && listResult.get(0) != null ?  listResult.get(0).toString() : "-1"; 
		} catch (Exception e) {
			log.error("Error en el metodo getSucursalesVisibles", e);
			return null;
		}*/
		return "-1";
	}	
	
	public String getSucursalUsuario(long usuarioId){
		/*Object 			lookup = null;
		List<Object>	listResult = null;
		NQueryFacadeLocal	ifzQuery = null;
		
		try {
			lookup = context.lookup("NQueryFacade/local");
			ifzQuery = (NQueryFacadeLocal) PortableRemoteObject.narrow(lookup, NQueryFacadeLocal.class);
			
			listResult = ifzQuery.findNativeQuery("select (sucursal_usuario(" + usuarioId + ")) as result");
			return !listResult.isEmpty() && listResult.get(0) != null ?  listResult.get(0).toString() : "-1"; 
		} catch (Exception e) {
			log.error("Error en el metodo getSucursalUsuario", e);
			return null;
		}*/
		return "-1";
	}
	
	public String getCamposAutorizados(long usuarioId, long pantallaId, Date fecha){
		String res = "";
//		List<CampoTablaGrupo> listRes = null;
//		try {
//			if(this.ifzCamposAutorizados == null){
//				lookup = context.lookup("CampoTablaGrupoFacade/remote");
//				ifzCamposAutorizados = (CampoTablaGrupoFacadeRemote) PortableRemoteObject.narrow(lookup, CampoTablaGrupoFacadeRemote.class);
//			}
//			
//			listRes = this.ifzCamposAutorizados.findByUsuarioFechaPantalla(usuarioId, pantallaId, fecha);
//			
//			for(CampoTablaGrupo ca : listRes)
//				res+= ("".equals(res) ? "" : ",") + ca.getTabla().getCampo();
//			
			return res;
//		} catch (Exception e) {
//			log.error("Error en el metodo getCamposAutorizados", e);
//			return "";
//		}
	}

	public String getPerfil(String perfil){
		String queryString = null;
		String valNivel = null;
		String valor  ="";

		if (!sesion.valida(infoSesion)) {
			log.info("Sesion invalida " + infoSesion.getAcceso());
			return "";
		}		
		
		List<Perfil> listPerfiles = ifzPerfiles.findByColumnName("perfil", perfil);
		if (listPerfiles.isEmpty()) {
			log.info("No se encontro el perfil " +  perfil );
			return "";
		}
		
		if (listPerfiles.size() > 1 ) {
			log.info("Mas de un perfil con el mismo nombre : "  + perfil);
			return "";
		}
				
		try {   
			this.connection = dataSource.getConnection();
			Statement stmt = this.connection.createStatement();
			
			for(int nivel = 1; nivel <= 64 ; nivel <<= 1){
				if (((int)listPerfiles.get(0).getNivelPerfil() & nivel) != 0) {
					switch(nivel) {
						case 1: valNivel = infoSesion.getAcceso().getTerminal();
								break;
						case 2: valNivel =  String.valueOf(infoSesion.getAcceso().getUsuario().getId());
								break;
						case 4: valNivel =  String.valueOf(infoSesion.getResponsabilidad().getId());
								break;
						case 8: valNivel = "0";
								break;
						case 16:valNivel = "0";
								break;
						case 32:valNivel = "0";
								break;
					}
					
					if(nivel != 1){
						queryString = 
								"select pv.ai  from b761110ccfe pv where " +
								" pv.af = " +  listPerfiles.get(0).getId()  + " and pv.ag = " + nivel + 
								            (nivel == 64 ? "" : " and pv.ah =  " + valNivel );
						ResultSet rs = stmt.executeQuery(queryString);
						if(rs.next()) {
							valor = rs.getString(1);
							break;
						}
					}
				}
			}
			
		} catch (Exception e) {
			log.error("Error en metodo findPerfil", e);
			return null;
		} finally {
			try {
				this.connection.close();
			} catch(Exception e) {
				log.error("Error al cerrar conexion de perfiles");
			}
		}
		
		return valor;
	}	
}
