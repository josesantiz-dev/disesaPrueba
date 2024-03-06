/***
 * valida al usuario contra la base de datos, ademas, permite obtener las sucursales visibles,
 * responsabilidades, menus y campos a deshabilidar en las pantallas.
 * @author (Guitate) Omar Magdiel Aguayo Garcia
 */

package net.giro.plataforma.seguridad.logica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.EmpresasUsuarios;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.Pagina;
import net.giro.plataforma.SingletonSesion;
import net.giro.plataforma.logica.EmpresasUsuariosRem;
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
	private EmpresasUsuariosRem ifzUsuEmpresas;
	private NQueryRem ifzQuery;
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
    private SingletonSesion sesion;
	private InfoSesion infoSesion = null;
 
	public AutentificacionFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		this.ctx = new InitialContext(p);
    		this.ifzUsuarios 	 	= (UsuariosDAO) this.ctx.lookup("ejb:/Model_Publico//UsuariosImp!net.giro.plataforma.seguridad.dao.UsuariosDAO");
    		this.ifzUsuEmpresas 	= (EmpresasUsuariosRem) this.ctx.lookup("ejb:/Logica_Publico//EmpresasUsuariosFac!net.giro.plataforma.logica.EmpresasUsuariosRem");
    		this.ifzQuery 			= (NQueryRem) this.ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
    		this.ifzAccesos 		= (AccesosDAO) this.ctx.lookup("ejb:/Model_Publico//AccesosImp!net.giro.plataforma.seguridad.dao.AccesosDAO");
    		this.ifzUsuarioResp 	= (UsuarioResponsabilidadDAO) this.ctx.lookup("ejb:/Model_Publico//UsuarioResponsabilidadImp!net.giro.plataforma.seguridad.dao.UsuarioResponsabilidadDAO");
    		this.ifzAplicaciones 	= (AplicacionDAO) this.ctx.lookup("ejb:/Model_Publico//AplicacionImp!net.giro.plataforma.seguridad.dao.AplicacionDAO");
    		this.ifzMenus 		 	= (MenuDAO) this.ctx.lookup("ejb:/Model_Publico//MenuImp!net.giro.plataforma.seguridad.dao.MenuDAO");
    		this.ifzMenuFunciones 	= (MenuFuncionDAO) this.ctx.lookup("ejb:/Model_Publico//MenuFuncionImp!net.giro.plataforma.seguridad.dao.MenuFuncionDAO");
    		this.ifzFunciones 	 	= (FuncionDAO) this.ctx.lookup("ejb:/Model_Publico//FuncionImp!net.giro.plataforma.seguridad.dao.FuncionDAO");
    		this.ifzPerfiles 	 	= (PerfilDAO) this.ctx.lookup("ejb:/Model_Publico//PerfilImp!net.giro.plataforma.seguridad.dao.PerfilDAO");
    		this.dataSource 		= (DataSource) this.ctx.lookup( "java:/giroDS" ); 
    	} catch (Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}	
	}

	// -----------------------------------------------------------------------------------------------
	// SESION
	// -----------------------------------------------------------------------------------------------

	@Override
	public InfoSesion getInfoSesion() {
		InfoSesion aux = null;
		
		if (this.infoSesion != null && this.infoSesion.getEmpresa() != null) {
			aux = this.sesion.validar(this.infoSesion);
			if (aux.getEmpresa().getId().longValue() != this.infoSesion.getEmpresa().getId().longValue())
				this.infoSesion = aux;
		}
		
		return this.infoSesion;
	}

	@Override
	public InfoSesion getInfoSesion(long idUsuario, long idAplicacion) {
		return this.sesion.validar(idUsuario, idAplicacion);
	}

	@Override
	public int conectar(Login login) {
		List<Usuario> lstUsuarios = null;
		Aplicacion aplicacion = null;
		Usuario usuario = null;
		int result = -1;
		
		try{
			this.infoSesion = null;
			lstUsuarios = this.ifzUsuarios.findByColumnName("usuario", login.getUsuario());
			if (lstUsuarios.size() != 1)
				return 1;

			result = 0;
			usuario = lstUsuarios.get(0);
			aplicacion = this.ifzAplicaciones.findById(login.getAplicacionId());
			nuevaSesion(login, usuario, aplicacion);
			usuario.setUltimoAcceso(Calendar.getInstance().getTime());
			this.ifzUsuarios.update(usuario);
		} catch (Exception re) {
			log.error("Error en el metodo login", re);
			result = -1;
		}
		
		return result;
	}

	@Override
	public void propagaEmpresa(InfoSesion infoSession, Empresa pojoEmpresa) {
		this.infoSesion = infoSession;
		this.sesion.propagateCompany(infoSession, pojoEmpresa);
	}
	
	@Override
	public void desconectar(InfoSesion infoSesion) {
		this.desconectar(infoSesion, false);
	}

	@Override
	public void desconectar(InfoSesion infoSession, boolean keepCompany) {
		if (keepCompany)
			this.sesion.changeModule(infoSession);//, infoSession.getEmpresa());
		else
			this.sesion.logout(infoSession);
	}

	@Override
	public List<String> usuarios() {
		HashMap<String, String> sesiones = new HashMap<String, String>();
		List<String> usuarios = new ArrayList<String>();
		
		sesiones = this.sessiones();
		for (Entry<String, String> item : sesiones.entrySet()) {
			if (! usuarios.contains(item.getKey()))
				usuarios.add(item.getKey());
		}
		
		return usuarios;
	}

	@Override
	public HashMap<String, String> sessiones() {
		HashMap<String, String> sesiones = null;
		
		sesiones = this.sesion.loggedIn();
		if (sesiones == null)
			sesiones = new HashMap<String, String>();
		return sesiones;
	}

	// -----------------------------------------------------------------------------------------------
	// RESPONSABILIDADES
	// -----------------------------------------------------------------------------------------------

	@Override
	public List<UsuarioResponsabilidad> getUsuarioResponsabilidades() {
		List<UsuarioResponsabilidad> usuarioResponsabilidades = null;
		List<UsuarioResponsabilidad> lstTmpusuarioResponsabilidades = null;
		
		try {
			usuarioResponsabilidades = new ArrayList<UsuarioResponsabilidad>();
			if (this.infoSesion != null) {
				lstTmpusuarioResponsabilidades = this.ifzUsuarioResp.findByColumnName("usuario", this.infoSesion.getAcceso().getUsuario());
				for (UsuarioResponsabilidad ur : lstTmpusuarioResponsabilidades) {
					if (ur.getResponsabilidad() != null && ur.getResponsabilidad().getAplicacion().getId() == this.infoSesion.getAcceso().getAplicacion().getId())
						usuarioResponsabilidades.add(ur);
				}
			}
		} catch (Exception e) {
			log.error("Error en el metodo getUsuarioResponsabilidades", e);
		}
		
		return usuarioResponsabilidades;
	}

	@Override
	public void setResponsabilidad(UsuarioResponsabilidad usuarioResponsabilidad)  { 
		this.infoSesion.setResponsabilidad(usuarioResponsabilidad);
	}

	// -----------------------------------------------------------------------------------------------
	// EMPRESAS
	// -----------------------------------------------------------------------------------------------

	@Override
	public List<Empresa> getUsuarioEmpresas() {
		List<EmpresasUsuarios> listUsuarioEmpresas = null;
		List<Empresa> usuarioEmpresas = null;
		
		try {
			usuarioEmpresas = new ArrayList<Empresa>();
			if (this.infoSesion != null) {
				// Recuperamos empresas asignadas al usuario
				this.ifzUsuEmpresas.orderBy("idEmpresa");
				listUsuarioEmpresas = this.ifzUsuEmpresas.findByProperty("idUsuario.id", this.infoSesion.getAcceso().getUsuario().getId(), 0);
				if (listUsuarioEmpresas != null && ! listUsuarioEmpresas.isEmpty()) {
					for (EmpresasUsuarios item : listUsuarioEmpresas) 
						usuarioEmpresas.add(item.getIdEmpresa());
					
					if (usuarioEmpresas.size() > 1) {
						Collections.sort(usuarioEmpresas, new Comparator<Empresa>() {
							@Override
							public int compare(Empresa o1, Empresa o2) {
								return o1.getId().compareTo(o2.getId());
							}
						});
					}
				}
			}
		} catch (Exception e) {
			log.error("Error en el metodo getUsuarioEmpresas", e);
		}
		
		return usuarioEmpresas;
	}

	@Override
	public void setEmpresa(Empresa usuarioEmpresa)  { 
		this.infoSesion.setEmpresa(usuarioEmpresa);
		this.sesion.putSession(this.infoSesion);
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

	@Override
	public String getPerfil(String perfil) {
		List<Perfil> listPerfiles = null;
		Statement stmt = null;
		ResultSet rs = null;
		String queryString = null;
		String valNivel = null;
		String valor  ="";
		
		if (! this.sesion.valida(infoSesion)) {
			log.info("Sesion invalida " + this.infoSesion.getAcceso());
			return "";
		}		
		
		listPerfiles = this.ifzPerfiles.findByColumnName("perfil", perfil);
		if (listPerfiles.isEmpty()) {
			log.info("No se encontro el perfil " +  perfil);
			return "";
		}
		
		if (listPerfiles.size() > 1 ) {
			log.info("Mas de un perfil con el mismo nombre : "  + perfil);
			return "";
		}
				
		try {   
			this.connection = this.dataSource.getConnection();
			stmt = this.connection.createStatement();
			
			for (int nivel = 1; nivel <= 64 ; nivel <<= 1) {
				if (((int)listPerfiles.get(0).getNivelPerfil() & nivel) != 0) {
					switch(nivel) {
						case 1: valNivel = this.infoSesion.getAcceso().getTerminal();
								break;
						case 2: valNivel =  String.valueOf(this.infoSesion.getAcceso().getUsuario().getId());
								break;
						case 4: valNivel =  String.valueOf(this.infoSesion.getResponsabilidad().getId());
								break;
						case 8: valNivel = "0";
								break;
						case 16:valNivel = "0";
								break;
						case 32:valNivel = "0";
								break;
					}
					
					if (nivel != 1) {
						queryString = "select pv.ai from b761110ccfe pv where pv.af = " +  listPerfiles.get(0).getId()  + " and pv.ag = " + nivel + (nivel == 64 ? "" : " and pv.ah =  " + valNivel);
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

	@Override
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

	@Override
	public String getSucursalesVisibles(long usuarioId) {
		return "-1";
	}	

	@Override
	public String getSucursalUsuario(long usuarioId) {
		return "-1";
	}

	@Override
	public String getCamposAutorizados(long usuarioId, long pantallaId, Date fecha) {
		return "";
	}

	// -----------------------------------------------------------------------------------------------
	// TIPO DE CAMBIO
	// -----------------------------------------------------------------------------------------------

	@Override
	@SuppressWarnings("unchecked")
	public double getTipoCambio(Date fecha) {
		List<Double> items = null;
		String queryString = "";
		// ---------------------------------
		SimpleDateFormat formatter = null;
		double tipoCambio = 0;
		
		try {
			if (fecha == null)
				return tipoCambio;
			
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			queryString += "select valor from monedas_valores where date(fecha_desde) = date(':fecha')";
			queryString = queryString.replace(":fecha", formatter.format(fecha));
			// -------------------------------------------------------------------------------
			items = this.ifzQuery.findNativeQuery(queryString);
			if (items == null || items.isEmpty())
				return tipoCambio;
			tipoCambio = items.get(0).doubleValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el Tipo de Cambio de la fecha indicada: " + formatter.format(fecha), e);
			tipoCambio = 0;
		}
		
		return tipoCambio;
	}

	// -----------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------

	private void nuevaSesion(Login login, Usuario usuario, Aplicacion aplicacion) throws Exception {
		Acceso acceso = new Acceso();
		
		acceso.setUsuario(usuario);
		acceso.setInicio(Calendar.getInstance().getTime());
		acceso.setIpTerminal(login.getIpTerminal());
		acceso.setTerminal(login.getTerminal());
		acceso.setAplicacion(aplicacion);
		
		this.infoSesion = this.sesion.comprobar(usuario.getId(), aplicacion.getId());
		if (this.infoSesion == null) {
			acceso.setId(this.ifzAccesos.save(acceso, null));
			
			this.infoSesion = new InfoSesion();
			this.infoSesion.setAcceso(acceso);
			/*if (this.sesion.keepingCompany())
				this.infoSesion.setEmpresa(this.sesion.getCompany());*/
			this.sesion.registrar(this.infoSesion);
			/*if (this.infoSesion.getEmpresa() == null)
				this.infoSesion.setEmpresa(this.sesion.getCompany());*/
		}
	}

	private ArrayList<Pagina> getSubmenu(Menu menu) {
		ArrayList<Pagina> paginasSubMenu = new ArrayList<Pagina>();
		List<MenuFuncion> lstOpciones = null;
		Pagina p = null;
		Menu subMenu = null;
		Funcion funcion = null;
		
		try {
			this.ifzMenuFunciones.orderBy("prompt");
			lstOpciones = this.ifzMenuFunciones.findByProperty("menu", menu, 0);
			for (MenuFuncion menuFuncion : lstOpciones) {
			   p = new Pagina("", menuFuncion.getPrompt(), null, menuFuncion.getDescripcion(), "", menuFuncion.getId());
		       if (menuFuncion.getTipoAccion().equals("M")) {
		    	   subMenu = this.ifzMenus.findById(menuFuncion.getTipoId());
		    	   if (subMenu != null) { 
		    		   p.setSubMenu(getSubmenu(subMenu));
		    	   	   p.setIcono(0);
		    	   }
		       } else {
		    	   funcion = this.ifzFunciones.findById(menuFuncion.getTipoId());
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
