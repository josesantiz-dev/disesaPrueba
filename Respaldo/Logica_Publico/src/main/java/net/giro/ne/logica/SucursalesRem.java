package net.giro.ne.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Regiones;
import net.giro.ne.beans.Sucursal;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.publico.respuesta.Respuesta;


@Remote
public interface SucursalesRem {
	public Sucursal findById(Long id);
	
	public List<Sucursal> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint;
	
	public List<Sucursal> findLikeColumnName(String propiedad, String valor);
	
	public List<Sucursal> buscarSucursales(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint;

	public Respuesta salvar(Sucursal sucursal);

	public void eliminar(Sucursal sucursal) throws ExcepConstraint;

	public List<Colonia> buscarColonia(String busquedaColonia) throws ExcepConstraint;

	public Respuesta buscarColonias(Localidad pojoLocalidad, String nombre);
	
	public List<ConValores> buscarDomicilios(HashMap<String, String> params) throws ExcepConstraint;

	public List<Empresa> buscarEmpresas(String busquedaEmpresa) throws ExcepConstraint;

	public Respuesta buscarLocalidades(Municipio pojoMunicipio, String valor);

	public Respuesta buscarMunicipios(Estado pojoEstado, String nombre);

	public List<Regiones> buscarRegiones(String busquedaRegiones) throws ExcepConstraint;

	public Respuesta cargarEstados();

	public void setInfoSesion(InfoSesion infoSesion);
	
	public List<Sucursal> findAll() throws Exception;
	
	public net.giro.respuesta.Respuesta folioFacturacion(Sucursal entity) throws Exception;
}


// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA   |        AUTOR     | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//   1.0   | 2016-10-19 | Javier Tirado    | Añado el metodo public net.giro.respuesta.Respuesta folioFacturacion(Sucursal entity)