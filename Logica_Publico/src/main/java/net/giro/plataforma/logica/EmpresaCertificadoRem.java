package net.giro.plataforma.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.EmpresaCertificado;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface EmpresaCertificadoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void orderBy(String orderBy);

	public Long save(EmpresaCertificado entity) throws ExcepConstraint;
	
	public void update(EmpresaCertificado entity) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public EmpresaCertificado findById(Long id);
	
	public EmpresaCertificado findByEmpresa(Long idEmpresa) throws Exception;
	
	public EmpresaCertificado findByEmpresa(Empresa idEmpresa) throws Exception;

	public List<EmpresaCertificado> findAll() throws Exception;
	
	public List<EmpresaCertificado> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpresaCertificado> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<EmpresaCertificado> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<EmpresaCertificado> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<EmpresaCertificado> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public Respuesta guardarCertificado(EmpresaCertificado entity, String certificado64, String llave64) throws Exception;
	
	public Respuesta cargaCertificado(String certificadoId, String palabra, String certificado64, String llave64, String usuarioTimbre, String claveTimbre) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/07/2016 | Javier Tirado	| Creacion de EmpresaCertificadoRem