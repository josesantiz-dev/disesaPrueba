package net.giro.ne.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Regiones;
import net.giro.ne.beans.Sucursal;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.respuesta.Respuesta;

@Remote
public interface SucursalesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Sucursal entity) throws Exception;
	
	public List<Sucursal> saveOrUpdateList(List<Sucursal> entities) throws Exception;

	public Respuesta salvar(Sucursal sucursal);

	public void eliminar(Sucursal sucursal) throws Exception;

	public Sucursal findById(Long id);
	
	public List<Sucursal> findLikePropiedad(String propiedad, String valor) throws Exception;
	
	public List<Sucursal> findLikeColumnName(String propiedad, String valor);
	
	public List<Sucursal> buscarSucursales(String tipoBusqueda, String valorBusqueda) throws Exception;

	public List<Colonia> buscarColonia(String busquedaColonia) throws Exception;

	public Respuesta buscarColonias(Localidad pojoLocalidad, String nombre);
	
	public List<ConValores> buscarDomicilios(HashMap<String, String> params) throws Exception;

	public List<Empresa> buscarEmpresas(String busquedaEmpresa) throws Exception;

	public Respuesta buscarLocalidades(Municipio pojoMunicipio, String valor);

	public Respuesta buscarMunicipios(Estado pojoEstado, String nombre);

	public List<Regiones> buscarRegiones(String busquedaRegiones) throws Exception;

	public Respuesta cargarEstados();

	public List<Sucursal> findAll() throws Exception;
	
	public Respuesta folioFacturacion(Sucursal entity) throws Exception;
}


// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA   |        AUTOR     | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//   1.0   | 2016-10-19 | Javier Tirado    | Añado el metodo public net.giro.respuesta.Respuesta folioFacturacion(Sucursal entity)