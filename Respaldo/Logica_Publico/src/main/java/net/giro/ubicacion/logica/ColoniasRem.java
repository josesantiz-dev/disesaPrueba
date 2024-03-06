package net.giro.ubicacion.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.publico.respuesta.Respuesta;


@Remote
public interface ColoniasRem {
	List<Colonia> buscarColonias(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint;

	List<Localidad> buscarLocalidades() throws ExcepConstraint;

	public Respuesta buscarLocalidades(Municipio pojoMunicipio, String valor);

	public Respuesta buscarMunicipios(Estado pojoEstado, String nombre);

	public Respuesta cargarEstados();

	public Respuesta salvar(Colonia pojoColonia);

	void eliminar(Colonia colonia) throws ExcepConstraint;

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Colonia findById(long id);
}
