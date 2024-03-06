package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.FacturaTimbre;
import net.giro.plataforma.InfoSesion;

@Remote
public interface FacturaTimbreRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public void setInfoSesion(long idUsuario, long idEmpresa, long codigoEmpresa);
	
	public long save(FacturaTimbre entity) throws Exception;

	public FacturaTimbre saveOrUpdate(FacturaTimbre entity) throws Exception;
		
	public List<FacturaTimbre> saveOrUpdateList(List<FacturaTimbre> entities) throws Exception;

	public void update(FacturaTimbre entity) throws Exception;
	
	// -------------------------------------------------------------------------------------------------------------------
	
	public FacturaTimbre findById(long idTimbre) throws Exception;
	
	public List<FacturaTimbre> findAll() throws Exception;
	
	public List<FacturaTimbre> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<FacturaTimbre> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public FacturaTimbre comprobarTimbre(String serie, String folio) throws Exception;
}
