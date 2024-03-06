package net.giro.plataforma.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.DiasFestivosNegociacion;

@Remote
public interface DiasFestivosNegociacionRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public long save(DiasFestivosNegociacion entity) throws Exception;
	
	public List<DiasFestivosNegociacion> saveOrUpdateList(List<DiasFestivosNegociacion> entities) throws Exception;

	public void update(DiasFestivosNegociacion entity) throws Exception;

	public DiasFestivosNegociacion cancelar(DiasFestivosNegociacion entity) throws Exception;

	public DiasFestivosNegociacion findById(long idEntity) throws Exception;

	public List<DiasFestivosNegociacion> findAll(int limite) throws Exception;

	public List<DiasFestivosNegociacion> findAll(long idObra, int limite) throws Exception;
	
	public List<DiasFestivosNegociacion> findByProperty(String propertyName, Object value, long idObra, int limite) throws Exception;
	
	public List<DiasFestivosNegociacion> findLikeProperty(String propertyName, Object value, long idObra, int limite) throws Exception;
	
	public List<DiasFestivosNegociacion> comprobarNegociacion(long idDiaFestivo, long idObra) throws Exception;
	
	public boolean validarNegociacion(long idDiaFestivo, long idObra) throws Exception;

	public List<DiasFestivosNegociacion> cancelables(Date fecha, long idObra) throws Exception;
}
