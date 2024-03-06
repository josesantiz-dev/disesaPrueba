package net.giro.plataforma.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.ne.beans.EmpresasUsuarios;
import net.giro.plataforma.InfoSesion;

@Remote
public interface EmpresasUsuariosRem {
	public void orderBy(String orderBy);
	
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(EmpresasUsuarios entity) throws Exception;

	public List<EmpresasUsuarios> saveOrUpdateList(List<EmpresasUsuarios> entities) throws Exception;
	
	public void update(EmpresasUsuarios entity) throws Exception;
	
	public void delete(long entityId) throws Exception;
	
	public List<EmpresasUsuarios> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpresasUsuarios> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
}
