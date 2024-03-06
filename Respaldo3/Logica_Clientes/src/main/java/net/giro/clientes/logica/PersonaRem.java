package net.giro.clientes.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.clientes.beans.Persona;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.respuesta.Respuesta;

@Remote 
public interface PersonaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Persona entity) throws Exception;

	public List<Persona> saveOrUpdateList(List<Persona> entities) throws Exception;

	public Persona update(Persona entity) throws Exception;

	public void delete(long idPersona) throws Exception;
	
	public void delete(Persona entity) throws Exception;
	
	public List<Persona> findAll();
	
	public List<Persona> findAll(boolean incluyeEliminados, boolean incluyeSistema, String orderBy);
	
	public Persona findById(Long id);
	
	public Persona findByIdPojoCompleto(Long id);
	
	public List<Persona> findByProperty(String propertyName, Object value);

	public List<Persona> findByProperty(String propertyName, Object value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite);
	
	public List<Persona> findLike(String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite);
	
	public List<Persona> findLikeProperty(String propertyName, String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite);
	
	public List<Persona> findLikePersonas(Object value,int max);
	
	public List<Persona> findLikeClaveNombre(String propertyName, Object value, ConGrupoValores valGpo, String tipoPersona, int max, Boolean valido); 
	
	public List<Persona> findLikeColumnName(String propertyName, String value);
	
	public List<Persona> findLikeProveedor(Object value, ConGrupoValores valGpo,String tipoPersona,Long tipoGastoAsociado,int max);
	
	public HashMap<String, Long> getIdListLikeNombre(String value) throws Exception;
	
	public List<Persona> findByProperty(String propertyName, Object value, int limite);
	
	public List<Persona> findLikeProperty(String propertyName, String value, int limite);
	
	public List<Persona> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<Persona> findLikePropertyPersonaNegocio(String propertyName, String value, Integer limite) throws Exception;

	public List<Persona> busquedaDinamica(String value) throws Exception;
	
	public List<Persona> busquedaDinamica(String propertyName, String propertyValue) throws Exception;
	
	/**
	 * Metodo que devuelve la direccion de la Persona indicada.
	 * @param idPersona
	 * @return Respuesta Valores: pojo DomicilioExt, domicilio, numero_exterior, numero_interior, colonia, codigo_postal, ciudad o localidad, municipio, estado, pais
	 * @throws Exception
	 */
	public Respuesta buscarDomicilio(long idPersona) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.2 | 2017-05-18 | Javier Tirado 	| Implemento el metodo findByProperties
 */