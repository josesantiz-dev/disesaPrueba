package net.giro.plataforma.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.ne.beans.Empresa;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface EmpresasRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void orderBy(String orderBy);

	public Long save(Empresa entity) throws Exception;
	
	public void update(Empresa entity) throws Exception;
	
	public void delete(Long entityId) throws Exception;

	public Empresa findById(Long idEmpresa);

	public List<Empresa> findFoolById(Long idEmpresa) throws Exception;

	public List<Empresa> findAll() throws Exception;
	
	public List<Empresa> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Empresa> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Empresa> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Empresa> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<Empresa> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<Empresa> findByPropertyLike(String propertyName, String value);

	public Respuesta buscarDomicilios(HashMap<String, String> params);
	
	public Respuesta buscarEmpresas(String tipo, String valor);

	public Respuesta cargarMonedas();
	
	public Respuesta eliminarEmpresa(Empresa pojoEmpresa);
	
	public Respuesta guardarEmpresa(Empresa pojoEmpresa);
}


//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/07/2016 | Javier Tirado	| Modificacion de EmpresaRem