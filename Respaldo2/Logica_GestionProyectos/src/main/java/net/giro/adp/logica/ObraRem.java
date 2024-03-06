package net.giro.adp.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraExt;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;

@Remote
public interface ObraRem {
	public void showSystemOuts(boolean value);
	
	public void estatus(Long estatus);
	
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Obra entity) throws Exception;

	public List<Obra> saveOrUpdateList(List<Obra> entities) throws Exception;

	public void delete(Long entity) throws Exception;

	public void update(Obra entity) throws Exception;

	public Obra findById(Long id);
	
	public Obra revisado(Obra entity, long revisadoPor, Date fechaRevisado) throws Exception;
	
	public List<Obra> findAll() throws Exception;
	
	public List<Obra> findBy(final Object value, boolean incluyeAdministrativas, String orderBy, int limite) throws Exception;

	public List<Obra> findByProperty(String propertyName, Object value) throws Exception;
	
	public List<Obra> findByProperty(String propertyName, Object value, int opcion) throws Exception;
	
	public List<Obra> busquedaDinamica(String value) throws Exception;
	
	public List<Obra> findLike(String value, boolean incluyeAdministrativas, String orderBy, int limite) throws Exception;

	public List<Obra> findLikeProperty(String propertyName, final String value) throws Exception;
	
	public List<Obra> findLikeProperty(String propertyName, final String value, int opcion) throws Exception;
	
	public List<Obra> findLikeProperty(String propertyName, final String value, boolean incluyeAdministrativas) throws Exception;
	
	public List<Obra> findByPropertyPojoCompleto(String propertyName, String tipo, long value) throws Exception;
	
	public List<Obra> ObrafindByProperty(String propertyName, final Object value) throws Exception;
	
	public List<Obra> findObraPrincipalByProperty(String propertyName, final Object value, int tipoObra, Long idObra) throws Exception;
	
	public List<Obra> findObraPrincipalLikeProperty(String propertyName, final Object value, int tipoObra, Long idObra) throws Exception;
	
	public List<Obra> findByProperties(HashMap<String, Object> params, int tipoObra) throws Exception;
	
	public List<Obra> findLikeProperties(HashMap<String, String> params, int tipoObra) throws Exception;
	
	public List<Obra> findByMultiProperties(HashMap<String, Object> params, String unionProps, int tipoObra, int limite) throws Exception;
	
	public List<Obra> findLikeMultiProperties(HashMap<String, String> params, String unionProps, int tipoObra, int limite) throws Exception;

	public List<Persona> findPersonaLikeProperty(String propertyName, String value, String tipo) throws Exception;
	
	public List<Persona> findClienteByProperty(String propertyName, Object value, String tipo) throws Exception;
	
	public List<Persona> findClienteLikeProperty(String propertyName, String value, String tipo) throws Exception;
	
	public Long findEstatusCanceladoObras() throws Exception;
	
	public Long findEstatusCanceladoObras(ConGrupoValores pojoGpoEstatusObras) throws Exception;
	
	public List<Obra> findInProperty(String propertyName, List<Object> values) throws Exception;
	
	public List<Obra> findPrincipalBy(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception;
	
	public List<Obra> findPrincipalLike(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception;
	
	public List<Obra> findSubObraBy(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception;
	
	public List<Obra> findSubObraLike(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception;

	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------
	
	public Long save(ObraExt entity) throws Exception;
		
	public void update(ObraExt entity) throws Exception;

	public Obra convertir(ObraExt target) throws Exception;
	
	public ObraExt convertir(Obra target) throws Exception;

	public List<Obra> convertirList(List<ObraExt> targetList) throws Exception;
	
	public List<ObraExt> extenderLista(List<Obra> targetList) throws Exception;
	
	public ObraExt revisado(ObraExt entityExt, long revisadoPor, Date fechaRevisado) throws Exception;
	
	public ObraExt findByIdExt(Long id) throws Exception;
	
	public List<ObraExt> findExtBy(final Object value, boolean incluyeAdministrativas, String orderBy, int limite) throws Exception;
	
	public List<ObraExt> findByPropertyExt(String propertyName, Object value) throws Exception;
	
	public List<ObraExt> findByPropertyExt(String propertyName, Object value, int opcion) throws Exception;

	public List<ObraExt> findExtByProperties(HashMap<String, Object> params, int tipoObra) throws Exception;
	
	public List<ObraExt> findExtLike(final String value, boolean incluyeAdministrativas, String orderBy, int limite) throws Exception;
	
	public List<ObraExt> findLikePropertyExt(String propertyName, final String value) throws Exception;
	
	public List<ObraExt> findLikePropertyExt(String propertyName, final String value, int opcion) throws Exception;
	
	public List<ObraExt> findExtLikeProperties(HashMap<String, String> params, int tipoObra) throws Exception;

	public PersonaExt extenderPersona(Persona pojoPersona) throws Exception;
	
	public List<PersonaExt> findClienteExtByProperty(String propertyName, Object value, String tipo) throws Exception;

	public List<PersonaExt> findClienteExtLikeProperty(String propertyName, String value, String tipo) throws Exception;
	
	public List<PersonaExt> findPersonaExtLikeProperty(String propertyName, String value, String tipo) throws Exception;
	
	public List<ObraExt> findExtPrincipalBy(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception;
	
	public List<ObraExt> findExtPrincipalLike(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception;
	
	public List<ObraExt> findExtSubObraBy(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception;
	
	public List<ObraExt> findExtSubObraLike(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-09 | Javier Tirado 	| Añado los metodos findClienteByProperty y findClienteLikeProperty
 * 1.2 | 2016-11-17 | Javier Tirado 	| Añado los metodos orderBy, findByProperties y findLikeProperties. Normal y extendido
 * 1.2 | 2017-01-12 | Javier Tirado 	| Añado los metodos findByMultiProperties y findLikeMultiProperties
 * 1.2 | 2017-04-17 | Javier Tirado 	| Añado el metodo findEstatusCanceladoObras
 */