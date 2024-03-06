package net.giro.inventarios.logica;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.respuesta.Respuesta;

@Remote
public interface ProductoRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(Producto entity) throws Exception;

	public List<Producto> saveOrUpdateList(List<Producto> entities) throws Exception;

	public void update(Producto entity) throws Exception;

	public Producto cancelar(long idProducto) throws Exception;
	
	public void delete(long idProducto) throws Exception;
	
	public Producto findById(Long idProducto);

	public Producto findByClave(String clave);

	public List<Producto> findAll();
	
	public List<Producto> findAll(boolean incluyeCancelados, boolean incluyeOcultos, String orderBy);

	public List<Producto> findLike(String value, long idFamilia, int tipoMaestro, String orderBy, int limite);

	public List<Producto> findLike(String value, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, String orderBy, int limite);

	public List<Producto> findLikeProperty(String propertyName, Object value, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, String orderBy, int limite);

	public List<Producto> findLikeProperty(String propertyName, Object value, long idFamilia, int tipoMaestro, String orderBy, int limite);

	public List<Producto> findLikeProperty(String propertyName, Object value, int limite);
	
	public List<Producto> findLikeProperty(String propertyName, Object value, Long idFamilia, int limite);

	public List<Producto> findByProperty(String propertyName, Object propertyValue, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, String orderBy, int limite);

	public List<Producto> findByProperty(String propertyName, Object propertyValue, long idFamilia, int tipoMaestro, String orderBy, int limite);
	
	public List<Producto> findByClaveRango(String prefix, int inicioRango, int limiteRango) throws Exception;

	public List<Producto> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<Producto> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception;
	
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, int limite) throws Exception;
	
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, int limite, String orderBy) throws Exception;

	public List<Producto> findLikeProperties(HashMap<String, String> params, int tipo, int limite, String orderBy) throws Exception;
	
	/**
	 * Valida si la clave asignada al producto ya existe en otro producto
	 * @param idProducto ID asignado al producto si hay un registro previo, de lo contrario menor o igual cero o null
	 * @param clave Clave asignada al producto la cual se debe validar, no puede ser null
	 * @return Verdadero si la clave ya esta registrada
	 */
	public boolean validarClaveProducto(Long idProducto, String clave);
	
	/**
	 * Valida si la clave asignada al producto ya existe en otro producto
	 * @param pojoProducto Producto a ser comparado (ID y Clave)
	 * @return Verdadero si la clave ya esta registrada
	 */
	public boolean validarClaveProducto(Producto pojoProducto);

	/**
	 * Valida si la clave asignada al producto ya existe en otro producto
	 * @param pojoProducto Producto extendido a ser comparado (ID y Clave)
	 * @return Verdadero si la clave ya esta registrada
	 */
	public boolean validarClaveProducto(ProductoExt pojoProducto);
	
	public Respuesta actualizarProductos(byte[] fileSrc, String fileExtension, ConGrupoValores unidadesMedida, ConGrupoValores familias) throws Exception;

	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension, ConGrupoValores unidadesMedida, ConGrupoValores familias) throws Exception;
	
	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension, ConGrupoValores unidadesMedida, ConGrupoValores familias, LinkedHashMap<String, String> maestroLayoutReference) throws Exception;

	public List<Producto> comprobarProductos(List<Producto> productos);

	public List<Producto> findList(List<Long> idProductos) throws Exception;
	
	public Respuesta exportarMaestro(List<Producto> listaProductos, String nombreMaestro) throws Exception;
	
	public Respuesta exportarMaestro(HashMap<String, Object> params, String orderBy, String nombreMaestro) throws Exception;

	// --------------------------------------------------------------------------------------------
	
	public List<ConValores> recuperarTiposMaestro() throws Exception;
	
	public List<ConValores> recuperarEspecialidades(long idMaestro) throws Exception;
	
	public List<ConValores> recuperarFamilias(long idEspecialidad) throws Exception;
	
	public List<ConValores> recuperarSubFamilias(long idFamilia) throws Exception;
	
	public List<ConValores> recuperarFamiliasByMaestro(long idMaestro) throws Exception;

	// --------------------------------------------------------------------------------------------
	// CONVERSIONES
	// --------------------------------------------------------------------------------------------

	public ProductoExt convertir(Producto entity);
	
	public Producto convertir(ProductoExt entity);
	
	public List<ProductoExt> extenderLista(List<Producto> entities);
	
	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	public Long save(ProductoExt entity) throws Exception;
	
	public void update(ProductoExt entity) throws Exception;
	
	//public void delete(ProductoExt entity) throws Exception;

	public ProductoExt findExtById(Long idProducto);

	public ProductoExt findExtByClave(String clave);
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-06-19 | Javier Tirado 	| Añado el metodo findLikeProperties
 * 	2.2 | 2017-06-19 | Javier Tirado 	| Añado el metodo exportarMaestro.
 *  2.1 | 2017-03-31 | Javier Tirado 	| Añado los metodos analizarArchivo y comprobarProductos: estos metodos deberian suplir al metodo actualizarProductos.
 *  2.1 | 2016-11-12 | Javier Tirado 	| Añado los metodos: 
 *  										1. findLikeProperty(String propertyName, Object value, int limite)
 *  										2. findLikeProperty(String propertyName, Object value, Long idFamilia, int limite)
 *  										3. convertir(Producto entity)
 *  										4. convertir(ProductoExt entity)
 *  										5. convertirLista(List<ProductoExt> entities)
 *  										6. convertirListaExtendida(List<Producto> entities) 
 *  										7. saveOrUpdateList(List<Producto> entities)
 */