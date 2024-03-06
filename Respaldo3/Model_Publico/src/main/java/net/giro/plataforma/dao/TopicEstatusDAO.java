package net.giro.plataforma.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.plataforma.beans.TopicEstatus;

@Remote
public interface TopicEstatusDAO extends DAO<TopicEstatus> {
	public long save(TopicEstatus entity, long codigoEmpresa) throws Exception;
	
	public List<TopicEstatus> saveOrUpdateList(List<TopicEstatus> entities, long codigoEmpresa) throws Exception;

	public List<TopicEstatus> findAll(String orderBy, long idEmpresa, int limite) throws Exception;
	
	public List<TopicEstatus> findLikeProperty(String propertyName, Object value, Date fecha, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<TopicEstatus> findByProperty(String propertyName, Object value, Date fecha, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<TopicEstatus> findByDates(Date fechaInicial, Date fechaFinal, long idEmpresa) throws Exception;
	
	public List<TopicEstatus> comprobarComando(String target, String referencia, String referenciaExtra, String atributos, Date fecha, long idEmpresa) throws Exception;
}
