package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.Obra;

@Remote
public interface ObraDAO extends DAO<Obra> {
	public void estatus(Long estatus);
	public void orderBy(String orderBy);

	public List<Obra> findAll();
	
	public List<Obra> findByProperty(String propertyName, final Object value, int opcion) throws Exception;

	public List<Obra> findObraPrincipalByProperty(String propertyName, final Object value, int tipoObra, Long idObra) throws Exception;

	public List<Obra> findByPropertyPojoCompleto(String propertyName, String tipo, Object value) throws Exception;
	
	public List<Obra> findByProperties(HashMap<String, Object> params, int tipoObra) throws Exception;
	
	public List<Obra> findLikeProperty(String propertyName, final String value, int opcion) throws Exception;
	
	public List<Obra> findLikeProperty(String propertyName, final String value, boolean incluyeAdministrativas) throws Exception;
	
	public List<Obra> findObraPrincipalLikeProperty(String propertyName, final Object value, int tipoObra, Long idObra) throws Exception;
	
	public List<Obra> findLikeProperties(HashMap<String, String> params, int tipoObra) throws Exception;
	
	public List<Obra> findByMultiProperties(HashMap<String, Object> params, String unionProps, int tipoObra, int limite) throws Exception;
	
	public List<Obra> findLikeMultiProperties(HashMap<String, String> params, String unionProps, int tipoObra, int limite) throws Exception;
	
	public List<Obra> findInProperty(String propertyName, List<Object> values) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-17 | Javier Tirado 	| Añado los metodos orderBy, findByProperties y findLikeProperties. Normal y extendido
 * 1.2 | 2017-01-12 | Javier Tirado 	| Añado los metodos findByMultiProperties y findLikeMultiProperties.
 */