package net.giro.plataforma.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.SubfamiliaImpuestos;
import net.giro.plataforma.beans.SubfamiliaImpuestosExt;

@Remote
public interface SubfamiliaImpuestosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(SubfamiliaImpuestos entity) throws Exception;
	
	public List<SubfamiliaImpuestos> saveOrUpdateList(List<SubfamiliaImpuestos> entities) throws Exception;
	
	public void update(SubfamiliaImpuestos entity) throws Exception;

	public void delete(Long entityId) throws Exception;

	public SubfamiliaImpuestos findById(Long id);

	public List<SubfamiliaImpuestos> findAll() throws Exception;
	
	public List<SubfamiliaImpuestos> findByProperty(String propertyName, final Object value) throws Exception;

	public List<SubfamiliaImpuestos> findByProperties(HashMap<String, Object> params) throws Exception;

	public List<SubfamiliaImpuestos> findLikeProperty(String propertyName, final String value) throws Exception;

	public List<SubfamiliaImpuestos> findLikeProperties(HashMap<String, String> params) throws Exception;
	
	public List<SubfamiliaImpuestos> findInProperty(String columnName, List<Object> values) throws Exception;
	
	public SubfamiliaImpuestos convertir(SubfamiliaImpuestosExt entityExt) throws Exception;
	
	public SubfamiliaImpuestosExt convertir(SubfamiliaImpuestos entity) throws Exception;

	// ---------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------------------------------

	public SubfamiliaImpuestosExt findExtById(Long id) throws Exception;
	
	public List<SubfamiliaImpuestosExt> findExtByProperty(String propertyName, final Object value) throws Exception;

	public List<SubfamiliaImpuestosExt> findExtLikeProperty(String propertyName, final String value) throws Exception;
}
