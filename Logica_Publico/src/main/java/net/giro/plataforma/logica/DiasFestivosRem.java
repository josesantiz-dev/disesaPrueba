package net.giro.plataforma.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.DiasFestivos;

@Remote
public interface DiasFestivosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public long save(DiasFestivos entity) throws Exception;
	
	public List<DiasFestivos> saveOrUpdateList(List<DiasFestivos> entities) throws Exception;

	public void update(DiasFestivos entity) throws Exception;

	public DiasFestivos cancelar(DiasFestivos entity) throws Exception;

	public DiasFestivos findById(long idEntity) throws Exception;

	public DiasFestivos findByDate(Date fecha) throws Exception;
	
	public List<DiasFestivos> findAll(int limite) throws Exception;
	
	public List<DiasFestivos> findByProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<DiasFestivos> findLikeProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<DiasFestivos> comprobarDiaFestivo(int mes, int dia) throws Exception;
	
	public DiasFestivos comprobarDiaFestivo(Date fecha) throws Exception;
	
	public boolean validarDiaFestivo(Date fecha) throws Exception;
}
