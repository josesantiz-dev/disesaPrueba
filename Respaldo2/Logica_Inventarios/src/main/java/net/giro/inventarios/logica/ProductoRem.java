package net.giro.inventarios.logica;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.respuesta.Respuesta;

@Remote
public interface ProductoRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(Producto entity) throws Exception;

	public List<Producto> saveOrUpdateList(List<Producto> entities) throws Exception;

	public void update(Producto entity) throws Exception;

	public void delete(Producto entity) throws Exception;
	
	public Producto findById(Long id);

	public Producto findByClave(String clave);

	public List<Producto> findAll();

	public List<Producto> findAllActivos();

	public List<Producto> findBy(Object value, long idFamilia, int tipoMaestro, int limite);
	
	public List<Producto> findByProperty(String propertyName, Object value);
	
	public List<Producto> findByNombre(String descripcion);

	public List<Producto> findByClaveRango(String prefix, int inicioRango, int limiteRango) throws Exception;

	public List<Producto> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<Producto> findByProperties(HashMap<String, Object> params, int limite, String orderBy) throws Exception;
	
	public List<Producto> findLike(String value, long idFamilia, int tipoMaestro, int limite);

	public List<Producto> findLikeProperty(String propertyName, Object value, int limite);
	
	public List<Producto> findLikeProperty(String propertyName, Object value, Long idFamilia, int limite);
	
	public List<Producto> findLikeProperty(String propertyName, Object propertyValue, Long idFamilia, int tipo, int limite) throws Exception;

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
	
	public ProductoExt convertir(Producto entity);
	
	public Producto convertir(ProductoExt entity);

	public List<Producto> convertirLista(List<ProductoExt> entities);
	
	public List<ProductoExt> convertirListaExtendida(List<Producto> entities);
	
	public Respuesta actualizarProductos(byte[] fileSrc, String fileExtension, ConGrupoValores unidadesMedida, ConGrupoValores familias) throws Exception;

	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension, ConGrupoValores unidadesMedida, ConGrupoValores familias) throws Exception;
	
	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension, ConGrupoValores unidadesMedida, ConGrupoValores familias, LinkedHashMap<String, String> maestroLayoutReference) throws Exception;
	
	public List<Producto> comprobarProductos(List<Producto> productos);
	
	public Respuesta exportarMaestro(List<Producto> listaProductos, String nombreMaestro) throws Exception;
	
	public Respuesta exportarMaestro(HashMap<String, Object> params, String orderBy, String nombreMaestro) throws Exception;

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	public Long save(ProductoExt entity) throws Exception;
	
	public void update(ProductoExt entity) throws Exception;
	
	public void delete(ProductoExt entity) throws Exception;

	public ProductoExt findExtById(Long id);

	public List<ProductoExt> findExtAll();
	
	public ProductoExt findExtByClave(String clave);
	
	public List<ProductoExt> findExtBy(Object value, long idFamilia, int tipoMaestro, int limite);

	public List<ProductoExt> findExtByProperty(String propertyName, Object value);
	
	public List<ProductoExt> findExtByNombre(String descripcion);
	
	public List<ProductoExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<ProductoExt> findExtByClaveRango(String prefix, int inicioRango, int limiteRango) throws Exception;
	
	public List<ProductoExt> findExtLike(String value, long idFamilia, int tipoMaestro, int limite);

	public List<ProductoExt> findExtLikeProperty(String propertyName, Object value, int limite);
	
	public List<ProductoExt> findExtLikeProperty(String propertyName, Object value, Long idFamilia, int limite);
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