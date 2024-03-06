package net.giro.plataforma.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.respuesta.Respuesta;

@Remote
public interface ConValoresRem {
	public Respuesta delete(ConValores entity);
	
	public Respuesta salvar(ConValores pojoValores);
	
	public long save(ConValores pojoValores);
	
	public void update(ConValores pojoValores);

	public ConValores findById(Long id);
	
	public ConValores findByPropertyPojoSolito(String propertyName,final Object value);

	public ConValores findByValorGrupo(String valor, ConGrupoValores grupo);

	public List<ConValores> findAll(ConGrupoValores grupo);
	
	public List<ConValores> findAll(ConGrupoValores grupo, String orderBy, int limite);
	
	public List<ConValores> findByProperty(String propertyName, Object value);
	
	public List<ConValores> buscaValorGrupo(String campo,String valor, ConGrupoValores grupo);
	
	public List<ConValores> findLikeValorIdPropiedadGrupo(String dato, ConGrupoValores grupo,int limit);
	
	/**
	 * 
	 * @param dato valor a buscar el march del id o el campo valor de la tabla
	 * @param grupo , grupo al que petenece el valor
	 * @param limit , numero de registros a devolver, en caso de ser 0 devuelve todos los registros encontrados
	 * @return Respuesta, si el codigo de error es 0 contiene 'listValroes' con lista de los valores encontrados
	 */
	public Respuesta findLikeClaveValorGrupo(String dato, ConGrupoValores grupo,int limit);
	
	public List<ConValores> findLikeByProperty(String dato, String valor,int max) throws Exception;
	
	public List<ConValores> findByGrupoNombre(String grupoNombre);
	
	public List<ConValores> findLikeValorAgenteEstatus(String dato, ConGrupoValores grupo, String estatus, int max) throws Exception;
	
	/**
	 * 
	 * @param grupoNombre nombre unico del grupo que se desea buscar entre sus detalles
	 * @param params propiedad y valor del con_valores a buscar el match
	 * @return 
	 */
	public List<ConValores> findByGrupoNombreByParams(String grupoNombre, HashMap<String, String> params);
	
	public List<ConValores> findByProperties(String propertyName1,final Object value1,String propertyName2,final Object value2);

	/**
	 * 
	 * @param valueDescripcion valor de la descripcion del grupo de valor a buscar
	 * @param valueCuenta valor a buscar de la cuenta 
	 * @param valueGrupo valor del grupo por el cual buscar 
	 * @param max valor maximo de renglones a traer en la busqueda.
	 * @return lista de los valores encontrados
	 */
	public List<ConValores> findLikeByProperties(final Object valueDescripcion,final Object valueCuenta,final Object valueGrupo,int max);

	/**
	 * 
	 * @param propertyName1 nombre de la propiedad por la que se desea buscar
	 * @param valueInicial valor del rango inicial 
	 * @param valueFinal valor del rango final a buscar 
	 * @param propertyName2 nombre de la segunda propiedad por la que se va a buscar
	 * @param grupo valor del grupo por el cual buscar 
	 * @return lista de los valores encontrados
	 */
	public List<ConValores> findByGrupoEntreValores(String propertyName1,final Object valueInicial,String propertyName2,final Object valueFinal,final Object grupo);
	
	public void setInfoSesion(InfoSesion infoSesion);
	
	public List<ConValores> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<ConValores> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<ConValores> findByProperty(String propertyName, Object value, ConGrupoValores grupo, int limit) throws Exception;
	
	public List<ConValores> findLikeProperty(String propertyName, String value, ConGrupoValores grupo, int limit) throws Exception;
}
