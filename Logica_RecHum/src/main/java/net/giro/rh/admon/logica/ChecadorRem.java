package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;

@Remote
public interface ChecadorRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Checador entity) throws Exception;
	
	public List<Checador> saveOrUpdateList(List<Checador> entities) throws Exception;

	public void update(Checador Checador) throws Exception;
	
	public void delete(Long idChecador) throws Exception;

	public Checador findById(Long idChecador);

	public List<Checador> findAll(long idObra, String orderBy) throws Exception;
	
	public List<Checador> findLike(String value, String orderBy, int limite) throws Exception;

	public List<Checador> findLikeProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception;

	public List<Checador> findByProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception;

	public List<Checador> findByDate(Date fecha, String obra, String orderBy) throws Exception;
	
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta, String orderBy) throws Exception;

	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta, long idObra, String orderBy) throws Exception;
	
	public List<Checador> asistenciasNominas(Date fechaDesde, Date fechaHasta, long idObra, String orderBy) throws Exception;

	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension) throws Exception;

	public Respuesta importarChecador(byte[] source, LinkedHashMap<String, String> layout) throws Exception;
	
	public List<Checador> findAsistencias(String propertyName, Object value, Long idObra, String orderBy, int limite) throws Exception;
}
