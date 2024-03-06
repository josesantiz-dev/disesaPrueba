package net.giro.plataforma.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.EmpresaCertificado;
import net.giro.plataforma.InfoSesion;
import net.giro.publico.respuesta.Respuesta;

@Remote
public interface EmpresaCertificadoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);
	//public void estatus(Long estatus);

	public Long save(EmpresaCertificado entity) throws ExcepConstraint;
	//public Long save(EmpresaCertificadoExt entityExt) throws ExcepConstraint;
	
	public void update(EmpresaCertificado entity) throws ExcepConstraint;
	//public void update(EmpresaCertificadoExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public EmpresaCertificado findById(Long id);
	//public EmpresaCertificadoExt findExtById(Long id) throws Exception;

	public List<EmpresaCertificado> findAll() throws Exception;
	//public List<EmpresaCertificadoExt> findExtAll() throws Exception;
	
	public List<EmpresaCertificado> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<EmpresaCertificadoExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpresaCertificado> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<EmpresaCertificadoExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<EmpresaCertificado> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<EmpresaCertificadoExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<EmpresaCertificado> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	//public List<EmpresaCertificadoExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<EmpresaCertificado> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<EmpresaCertificadoExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;

	//public EmpresaCertificado cancelar(EmpresaCertificado entity) throws Exception;
	//public EmpresaCertificado cancelar(EmpresaCertificadoExt entityExt) throws Exception;
	
	public Respuesta cargaCertificado(String certificadoId, String palabra, String certificado64, String llave64, String usuarioTimbre, String claveTimbre) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/07/2016 | Javier Tirado	| Creacion de EmpresaCertificadoRem