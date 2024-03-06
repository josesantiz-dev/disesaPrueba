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

import net.giro.ne.beans.Empresa;
import net.giro.ne.dao.EmpresaDAO;
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
	private EmpresaDAO ifzEmpresas;
	private AccesosDAO ifzAccesos;
	private AplicacionDAO ifzAplicaciones;
	private MenuDAO ifzMenus;
	private MenuFuncionDAO ifzMenuFunciones;
	private FuncionDAO ifzFunciones;
	private PerfilDAO ifzPerfiles;
    @Resource
    private SessionContext sctx;
    private Connection connection = null;	
	private DataSource dataSource  = null;
    @EJB
    SingletonSesion sesion;    
	private InfoSesion infoSesion = null;
 
	
	public AutentificacionFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		this.ctx = new InitialContext(p);
    		
    		ifzUsuarios 	 = (UsuariosDAO) this.ctx.lookup("ejb:/Model_Publico//UsuariosImp!net.giro.plataforma.seguridad.dao.UsuariosDAO");
    		ifzEmpresas 	 = (EmpresaDAO) this.ctx.lookup("ejb:/Model_Publico//EmpresaImp!net.giro.ne.dao.EmpresaDAO");
    		ifzAccesos 		 = (AccesosDAO) this.ctx.lookup("ejb:/Model_Publico//AccesosImp!net.giro.plataforma.seguridad.dao.AccesosDAO");
    		ifzUsuarioResp 	 = (UsuarioResponsabilidadDAO) this.ctx.lookup("ejb:/Model_Publico//UsuarioResponsabilidadImp!net.giro.plataforma.seguridad.dao.UsuarioResponsabilidadDAO");
    		ifzAplicaciones  = (AplicacionDAO) this.ctx.lookup("ejb:/Model_Publico//AplicacionImp!net.giro.plataforma.seguridad.dao.AplicacionDAO");
    		ifzMenus 		 = (MenuDAO) this.ctx.lookup("ejb:/Model_Publico//MenuImp!net.giro.plataforma.seguridad.dao.MenuDAO");
    		ifzMenuFunciones = (MenuFuncionDAO) this.ctx.lookup("ejb:/Model_Publico//MenuFuncionImp!net.giro.plataforma.seguridad.dao.MenuFuncionDAO");
    		ifzFunciones 	 = (FuncionDAO) this.ctx.lookup("ejb:/Model_Publico//FuncionImp!net.giro.plataforma.seguridad.dao.FuncionDAO");
    		ifzPerfiles 	 = (PerfilDAO) this.ctx.lookup("ejb:/Model_Publico//PerfilImp!net.giro.plataforma.seguridad.dao.PerfilDAO");
    		
    		dataSource 		 = (DataSource) this.ctx.lookup( "java:/giroDS" ); 
    	} catch (Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}	
	}


	// -----------------------------------------------------------------------------------------------
	// SESION
	// -----------------------------------------------------------------------------------------------

	public InfoSesion getInfoSesion() {
		return infoSesion;
	}

	public int conectar(Login login) {
		List<Usuario> lstUsuarios = null;
		Aplicacion aplicacion = null;
		Usuario usuario = null;
		int result = -1;
		
		try{
			this.infoSesion = null;
			lstUsuarios = this.ifzUsuarios.findByColumnName("usuario", login.getUsuario());
			if (lstUsuarios.size() != 1) {
				return 1;
			}

			result = 0;
			usuario = lstUsuarios.get(0);
			aplicacion = this.ifzAplicaciones.findById(login.getAplicacionId());
			nuevaSesion(login,usuario,aplicacion);
			
			usuario.setUltimoAcceso(Calendar.getInstance().getTime());
			this.ifzUsuarios.update(usuario);
		} catch (Exception re) {
			result = -1;
			log.error("Error en el metodo login", re);
		}
		
		return result;
	}
	
	public void desconectar(InfoSesion infoSesion) {
		this.sesion.logout(infoSesion);
	}

	// -----------------------------------------------------------------------------------------------
	// RESPONSABILIDADES
	// -----------------------------------------------------------------------------------------------
	
	public List<UsuarioResponsabilidad> getUsuarioResponsabilidades() {
		List<UsuarioResponsabilidad> usuarioResponsabilidades = null;
		
		try {
			usuarioResponsabilidades = new ArrayList<UsuarioResponsabilidad>();
			if (infoSesion != null) {
				List<UsuarioResponsabilidad> lstTmpusuarioResponsabilidades = ifzUsuarioResp.findByColumnName("usuario", infoSesion.getAcceso().getUsuario());
				for (UsuarioResponsabilidad ur : lstTmpusuarioResponsabilidades) 
					if (ur.getResponsabilidad() != null && ur.getResponsabilidad().getAplicacion().getId() == infoSesion.getAcceso().getAplicacion().getId() )
						usuarioResponsabilidades.add(ur);
			}
		} catch (Exception e) {
			log.error("Error en el metodo getUsuarioResponsabilidades", e);
		}
		
		return usuarioResponsabilidades;
	}
	
	public void setResponsabilidad(UsuarioResponsabilidad usuarioResponsabilidad)  { 
		infoSesion.setResponsabilidad(usuarioResponsabilidad);
	}

	// -----------------------------------------------------------------------------------------------
	// EMPRESAS
	// -----------------------------------------------------------------------------------------------
	
	public List<Empresa> getUsuarioEmpresas() {
		List<Empresa> usuarioEmpresas = null;
		
		try {
			usuarioEmpresas = new ArrayList<Empresa>();
			if (infoSesion != null) {
				// TODO: Recuperar empresas asignadas al usuario
				List<Empresa> lstTmpusuarioResponsabilidades = ifzEmpresas.findAll();//.findByColumnName("usuario", infoSesion.getAcceso().getUsuario());
				for (Empresa ur : lstTmpusuarioResponsabilidades) 
					usuarioEmpresas.add(ur);
			}
		} catch (Exception e) {
			log.error("Error en el metodo getUsuarioEmpresas", e);
		}
		
		return usuarioEmpresas;
	}

	public void setEmpresa(Empresa usuarioEmpresa)  { 
		infoSesion.setEmpresa(usuarioEmpresa);
	}

	// -----------------------------------------------------------------------------------------------
	// PERFIL
	// -----------------------------------------------------------------------------------------------
	
	/***
	 * 
	 * @param nivelPerfiles es el ArrayList de niveles que contienen los perfiles del usuario, cada objeto NivelPerfil tiene el numero de nivel
	 * y un ArrayList de Perfiles el cual contiene la informacion del perfil como; id del registro, nombre del nivel y el detalle del mismo
	 * @param perfilBuscado: define el perfil a buscar en la lista
	 * @return true si lo encuentra, en caso contrario false
	 */
	public static boolean tienePerfil(ArrayList<NivelPerfil> nivelPerfiles, String perfilBuscado) {
		//		try{
		//			//recorremos entre los niveles en que se encontro perfiles del usuario
		//			for (NivelPerfil nPerfil:nivelPerfiles) {
		//				//recorremos los perfiles del nivel
		//				for (PerfilTmp p: nPerfil.getDetalles()) {
		//					if (p.getDetalle().equals(perfilBuscado))
		//						return true;
		//				}
		//			}
		//		} catch (Exception re) {log.error("Error en el metodo tienePerfil", re);}
		return false;
	}

	public String getPerfil(String perfil) {
		List<Perfil> listPerfiles = null;
		Statement stmt = null;
		ResultSet rs = null;
		String queryString = null;
		String valNivel = null;
		String valor  ="";

		if (!sesion.valida(infoSesion)) {
			log.info("Sesion invalida " + infoSesion.getAcceso());
			return "";
		}		
		
		listPerfiles = ifzPerfiles.findByColumnName("perfil", perfil);
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
			stmt = this.connection.createStatement();
			
			for (int nivel = 1; nivel <= 64 ; nivel <<= 1) {
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
					
					if (nivel != 1) {
						queryString = 
								"select pv.ai from b761110ccfe pv where " +
								" pv.af = " +  listPerfiles.get(0).getId()  + " and pv.ag = " + nivel + 
								            (nivel == 64 ? "" : " and pv.ah =  " + valNivel );
						rs = stmt.executeQuery(queryString);
						if (rs.next()) {
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
			} catch (Exception e) {
				log.error("Error al cerrar conexion de perfiles");
			}
		}
		
		return valor;
	}
	
	// -----------------------------------------------------------------------------------------------
	// MENU
	// -----------------------------------------------------------------------------------------------
	
	public Pagina getMenu(Menu menu) {
		Pagina 	root = null;
		
		try {
			//Menu menu = getInfoSesion().getResponsabilidad().getResponsabilidad().getMenu();
			if (menu != null)
				root = new Pagina("", menu.getMenu(), this.getSubmenu(menu), menu.getDescripcion(), "", menu.getId() * -1);
		} catch (Exception e) {
			log.error("Error en el metodo getMenu", e);
		} 
		
		return root;
	}
	
	public String getSucursalesVisibles(long usuarioId) {
		return "-1";
	}	
	
	public String getSucursalUsuario(long usuarioId) {
		return "-1";
	}
	
	public String getCamposAutorizados(long usuarioId, long pantallaId, Date fecha) {
		return "";
	}

	// -----------------------------------------------------------------------------------------------
	// privados
	// -----------------------------------------------------------------------------------------------

	private void nuevaSesion(Login login, Usuario usuario, Aplicacion aplicacion) throws Exception {
		Acceso acceso = new Acceso();
		
		acceso.setUsuario(usuario);
		acceso.setInicio(Calendar.getInstance().getTime());
		acceso.setIpTerminal(login.getIpTerminal());
		acceso.setTerminal(login.getTerminal());
		acceso.setId(ifzAccesos.save(acceso, null));
		acceso.setAplicacion(aplicacion);
		
		infoSesion = new InfoSesion();
		infoSesion.setAcceso(acceso);
		
		sesion.registrar(infoSesion);
	}

	private ArrayList<Pagina> getSubmenu(Menu menu) {
		ArrayList<Pagina> paginasSubMenu = new ArrayList<Pagina>();
		List<MenuFuncion> lstOpciones = null;
		Pagina p = null;
		Menu subMenu = null;
		Funcion funcion = null;
		
		try {
			ifzMenuFunciones.orderBy("prompt");
			lstOpciones = ifzMenuFunciones.findByProperty("menu", menu, 0);
			for (MenuFuncion menuFuncion : lstOpciones) {
			   p = new Pagina("", menuFuncion.getPrompt(), null, menuFuncion.getDescripcion(), "", menuFuncion.getId());
		       if (menuFuncion.getTipoAccion().equals("M")) {
		    	   subMenu = ifzMenus.findById(menuFuncion.getTipoId());
		    	   if (subMenu != null) { 
		    		   p.setSubMenu(getSubmenu(subMenu));
		    	   	   p.setIcono(0);
		    	   }
		       } else {
		    	   funcion = ifzFunciones.findById(menuFuncion.getTipoId());
		           if (funcion != null) {
		                p.setUrl(funcion.getForma());
		                p.setId(funcion.getIcono());
		                p.setDescripcion(funcion.getDescripcion());
		                p.setParametros(funcion.getParametros());
		           }
		       }
		       
		       paginasSubMenu.add(p);
		    }
		} catch (Exception e ) {
			 log.error("Error en el metodo getSubmenu", e);
		}
		
		return paginasSubMenu;
	}
}
