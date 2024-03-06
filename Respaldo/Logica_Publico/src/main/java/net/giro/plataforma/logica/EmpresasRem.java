package net.giro.plataforma.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Empresa;
import net.giro.plataforma.InfoSesion;
import net.giro.publico.respuesta.Respuesta;

@Remote
public interface EmpresasRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(Empresa entity) throws ExcepConstraint;
	//public Long save(EmpresaExt entityExt) throws ExcepConstraint;
	
	public void update(Empresa entity) throws ExcepConstraint;
	//public void update(EmpresaExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public Empresa findById(Long id);
	//public EmpresaExt findExtById(Long id) throws Exception;

	public List<Empresa> findAll() throws Exception;
	//public List<EmpresaExt> findExtAll() throws Exception;
	
	public List<Empresa> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<EmpresaExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Empresa> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<EmpresaExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Empresa> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<EmpresaExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Empresa> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	//public List<EmpresaExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<Empresa> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<EmpresaExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
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