package net.giro.plataforma.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.plataforma.beans.SubfamiliaImpuestos;

@Remote
public interface SubfamiliaImpuestosDAO extends DAO<SubfamiliaImpuestos> {
	public void setEmpresa(Long idEmpresa);
	
	public long save(SubfamiliaImpuestos entity) throws Exception;
	
	public List<SubfamiliaImpuestos> saveOrUpdateList(List<SubfamiliaImpuestos> entities) throws Exception;
	
	public List<SubfamiliaImpuestos> findByProperty(String propertyName, final Object value, int limite) throws RuntimeException;

	public List<SubfamiliaImpuestos> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<SubfamiliaImpuestos> findLikeProperty(String propertyName, final String value, int limite) throws Exception;

	public List<SubfamiliaImpuestos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<SubfamiliaImpuestos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
}
