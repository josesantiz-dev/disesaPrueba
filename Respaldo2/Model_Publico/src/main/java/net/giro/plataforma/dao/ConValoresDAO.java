package net.giro.plataforma.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;

@Remote
public interface ConValoresDAO extends DAO<ConValores> {
	public List<ConValores> findAll(ConGrupoValores grupo, String orderBy, int limite);
	
	public List<ConValores> findByGrupoNombreByParams(String grupoNombre, HashMap<String, String> params);
	
	public List<ConValores> findByGrupoNombreLikeParams(String grupoNombre, HashMap<String, String> params);
	
	public ConValores findByPropertyPojoSolito(String propertyName,final Object value);

	public ConValores findByValorGrupo(String valor, ConGrupoValores grupo);
	
	public List<ConValores> buscaValorGrupo(String campo,String valor, ConGrupoValores grupo);
	
	public List<ConValores> findLikeValorIdPropiedadGrupo(String dato, ConGrupoValores grupo,int limit);
	
	public List<ConValores> findLikeClaveValorGrupo(String dato, ConGrupoValores grupo,int limit);
	
	public List<ConValores> findLikeByProperty(String dato, String valor,int max);
	
	public List<ConValores> findByGrupoNombre(String grupoNombre);
	
	public List<ConValores> findByProperties(String propertyName1,final Object value1,String propertyName2,final Object value2);
	
	public List<ConValores> findLikeByProperties(final Object valueDescripcion,final Object valueCuenta,final Object valueGrupo,int max);
	
	public List<ConValores> findByGrupoEntreValores(String propertyName1,final Object valueInicial,String propertyName2,final Object valueFinal,final Object grupo);
	
	public List<ConValores> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<ConValores> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<ConValores> findByProperty(String propertyName, Object value, ConGrupoValores grupo, int limit) throws Exception;
	
	public List<ConValores> findLikeProperty(String propertyName, String value, ConGrupoValores grupo, int limit) throws Exception;
}
