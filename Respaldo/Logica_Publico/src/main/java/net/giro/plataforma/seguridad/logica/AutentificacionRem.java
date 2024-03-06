package net.giro.plataforma.seguridad.logica;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.Pagina;
import net.giro.plataforma.seguridad.Login;
import net.giro.plataforma.seguridad.beans.Menu;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;


@Remote
public interface AutentificacionRem {
	/**
	 * 
	 * @param usuario cadena con el usuario que solicita acceder al sistema
	 * @param password cadena con la contrase�a del usuario
	 * @return 
	 * 		-1 	, 
	 * 		0 usuario valido, 
	 * 		1 usuario invalido,
	 * 		2 usuario blockeado,
	 * 		3 usuario expirado,
	 * 		4 usuario inactivo por lo menos 30 dias "se bloquea automaticamente"   
	 */
	public int conectar(Login login);
	
	public void desconectar(InfoSesion infoSession);
	
	public void setResponsabilidad(UsuarioResponsabilidad usuarioResponsabilidad);
	/**
	 * 
	 * @param usuarioId usuario para buscar en la tabla de perfiles
	 * @return cadena con los ids de las tablas contatenados
	 */
	public String getSucursalesVisibles(long usuarioId);
	
	public String getSucursalUsuario(long usuarioId);
	
	/**
	 * 
	 * @param menuId 
	 * @return devuelve la pagina root del menu y dentro de el sus hijos
	 */
	public Pagina getMenu(Menu menu); 
	
	public List<UsuarioResponsabilidad> getUsuarioResponsabilidades();
	
	public String getCamposAutorizados(long usuarioId, long pantallaId, Date fecha);

	public InfoSesion getInfoSesion();

	public String getPerfil(String perfil);
	
}
