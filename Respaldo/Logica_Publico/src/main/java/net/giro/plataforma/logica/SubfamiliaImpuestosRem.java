package net.giro.plataforma.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.beans.SubfamiliaImpuestos;

@Remote
public interface SubfamiliaImpuestosRem {
	public Long save(SubfamiliaImpuestos entity) throws ExcepConstraint;
	
	public void update(SubfamiliaImpuestos entity) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public SubfamiliaImpuestos findById(Long id);

	public List<SubfamiliaImpuestos> findAll() throws Exception;
	
	public List<SubfamiliaImpuestos> findByProperty(String propertyName, final Object value) throws Exception;

	public List<SubfamiliaImpuestos> findByProperties(HashMap<String, Object> params) throws Exception;

	public List<SubfamiliaImpuestos> findLikeProperty(String propertyName, final String value) throws Exception;

	public List<SubfamiliaImpuestos> findLikeProperties(HashMap<String, String> params) throws Exception;
	
	public List<SubfamiliaImpuestos> findInProperty(String columnName, List<Object> values) throws Exception;
}
