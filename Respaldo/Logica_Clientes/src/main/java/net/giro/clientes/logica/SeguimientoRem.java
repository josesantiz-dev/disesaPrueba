package net.giro.clientes.logica;

import java.util.HashMap;

import javax.ejb.Remote;

import net.giro.clientes.beans.SeguimientoExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface SeguimientoRem {
	
	public Respuesta autorizarSeguimiento(Long idSeguimiento, Long idProspecto);
	
	public Respuesta buscarEstatusSeguimiento();
	public Respuesta buscarModosContacto();
	public Respuesta buscarMotivosRechazo();
	public Respuesta buscarSeguimientoPorTodos(String tipoBusqueda,String valorBusqueda, Long estatusSeguimiento);
	public Respuesta buscarSeguimientoPorAsignados(String tipoBusqueda,String valorBusqueda, Long estatusSeguimiento, Long idUsuario);
	
	public Respuesta expirarSeguimiento(SeguimientoExt seguimientoExt);
	
	public Respuesta insertaSeguimientoProspecto(SeguimientoExt pojoSeguimientoExt);
	
	public Respuesta rechazarSeguimiento(Long idSeguimiento, Long idProspecto,Long idMotivoRechazo);

	public InfoSesion getInfoSesion();
	public void setInfoSesion(InfoSesion infoSesion);

	public Respuesta GenerarHistorico(SeguimientoExt pojoSeguimiento, HashMap<String, String> params);
	
	/*
	 ***************METODOS COMENTADOS POR QUE NO SE UTILIZAN*****************
	 public Respuesta buscarEspecialistas();
	 public Respuesta buscarProspectos(String tipoBusqueda,String valorBusqueda, String estatusSeguimiento);
	 public Respuesta buscarSeguimientos(String tipoBusqueda, String valorBusqueda);
	 public Respuesta detallesSeguimiento(Long id);
	 public long salvar(SeguimientoExt seguimientoExt);
	 */
}
