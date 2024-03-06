package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.beans.ChecadorExt;

@Remote
public interface ChecadorRem {
	public void setInfoSecion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(Checador Checador) throws ExcepConstraint;
	public Long save(ChecadorExt ChecadorExt) throws ExcepConstraint;
	
	public void update(Checador Checador) throws ExcepConstraint;
	public void update(ChecadorExt ChecadorExt) throws ExcepConstraint;
	
	public void delete(Long Checador) throws ExcepConstraint;

	public Checador findById(Long id);
	public ChecadorExt findExtById(Long id) throws Exception;
	
	public List<Checador> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	public List<ChecadorExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Checador> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	public List<ChecadorExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Checador> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	public List<ChecadorExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Checador> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	public List<ChecadorExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Checador> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	public List<ChecadorExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension) throws Exception;
	
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta) throws Exception;
	
	public Checador convertChecadorExtToChecador(ChecadorExt entity) throws Exception;
	public ChecadorExt convertChecadorToChecadorExt(Checador entity) throws Exception;
}
