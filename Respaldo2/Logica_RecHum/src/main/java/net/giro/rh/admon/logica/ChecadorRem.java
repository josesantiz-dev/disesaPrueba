package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;

@Remote
public interface ChecadorRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void orderBy(String orderBy);

	public Long save(Checador entity) throws Exception;
	
	public List<Checador> saveOrUpdateList(List<Checador> entities) throws Exception;

	public void update(Checador Checador) throws Exception;
	
	public void delete(Long Checador) throws Exception;

	public Checador findById(Long id);
	
	public List<Checador> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Checador> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Checador> findLikeProperty(String propertyName, final String value, int limite) throws Exception;
	
	public List<Checador> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Checador> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension) throws Exception;
	
	public List<Checador> findByDate(Date fecha, String obra) throws Exception;
	
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta) throws Exception;
}
