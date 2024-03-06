package net.giro.contabilidad.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.ImpuestoEquivalencia;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ImpuestoEquivalenciaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ImpuestoEquivalencia entity) throws Exception;

	public List<ImpuestoEquivalencia> saveOrUpdateList(List<ImpuestoEquivalencia> entities) throws Exception;

	public void update(ImpuestoEquivalencia entity) throws Exception;
	
	public void delete(Long idImpuestoEquivalencia) throws Exception;

	public ImpuestoEquivalencia findById(Long idImpuestoEquivalencia) throws Exception;

	public List<ImpuestoEquivalencia> findAll(long codigoTransaccion, String orderBy) throws Exception;

	public List<ImpuestoEquivalencia> findByProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception;

	public List<ImpuestoEquivalencia> findLikeProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception;
	
	public List<ImpuestoEquivalencia> findInProperty(String columnName, List<Object> values, String orderBy, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 30/05/2016 | Javier Tirado	| Creacion de ImpuestoEquivalenciaRem