package net.giro.plataforma.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ImpuestoEquivalencia;

@Remote
public interface ImpuestoEquivalenciaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ImpuestoEquivalencia entity) throws Exception;
	
	public List<ImpuestoEquivalencia> saveOrUpdateList(List<ImpuestoEquivalencia> entities) throws Exception;
	
	public List<ImpuestoEquivalencia> findByTransaccion(Long codigoTransaccion) throws Exception;
	
	public List<ImpuestoEquivalencia> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ImpuestoEquivalencia> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
}
