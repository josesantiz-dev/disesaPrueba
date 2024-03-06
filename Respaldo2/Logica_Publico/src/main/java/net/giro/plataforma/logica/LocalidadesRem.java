package net.giro.plataforma.logica;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.respuesta.Respuesta;

@Remote
public interface LocalidadesRem {
	public Respuesta buscarLocalidades(String tipo, String valor);

	public Respuesta buscarMunicipios(Estado pojoEstado, String nombre);

	public Respuesta cargarEstados();

	public Respuesta cargarMunicipios(Estado pojoEstado);
	
	public Respuesta eliminarLocalidad(Localidad pojoLocalidad);
	
	public Respuesta guardarLocalidad(Localidad pojoLocalidad);

	public Respuesta guardarMunicipio(Municipio pojoMunicipio);
	
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Localidad findById(long id);
}
