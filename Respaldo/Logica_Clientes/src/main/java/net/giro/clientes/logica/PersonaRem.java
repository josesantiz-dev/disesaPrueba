package net.giro.clientes.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.clientes.beans.Persona;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.beans.ConGrupoValores;

@Remote 
public interface PersonaRem {

	public Long save(Persona entity) throws ExcepConstraint;
	
	public void delete(Persona entity) throws ExcepConstraint;
	
	public Persona update(Persona entity) throws ExcepConstraint;
	
	public List<Persona> findAll();
	
	public Persona findById(Long id);
	
	public Persona findByIdPojoCompleto(Long id);

	public List<Persona> findByProperty(String propertyName, Object value);
	
	public List<Persona> findLikePersonas(Object value,int max);
	
	public List<Persona> findLikeClaveNombre(String propertyName, Object value, ConGrupoValores valGpo, String tipoPersona, int max, Boolean valido); 
	
	public List<Persona> findLikeColumnName(String propertyName, String value);
	
	public List<Persona> findLikeProveedor(Object value, ConGrupoValores valGpo,String tipoPersona,Long tipoGastoAsociado,int max);
	
	public HashMap<String, Long> getIdListLikeNombre(String value) throws Exception;
	
	//public List<Persona> findLikePersonasConGastos(Object value, ConGrupoValores valGpo, String tipoPersona, String tipo, String estatus, int max, Date fecha, String sucursales);
	
	public List<Persona> findByProperty(String propertyName, Object value, int limite);
	
	public List<Persona> findLikeProperty(String propertyName, String value, int limite);
	
	public List<Persona> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<Persona> findLikePropertyPersonaNegocio(String propertyName, String value, Integer limite) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.2 | 2017-05-18 | Javier Tirado 	| Implemento el metodo findByProperties
 */