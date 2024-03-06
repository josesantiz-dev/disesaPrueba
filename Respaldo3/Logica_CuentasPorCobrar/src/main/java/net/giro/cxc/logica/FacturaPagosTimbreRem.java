package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.FacturaPagosTimbre;
import net.giro.plataforma.InfoSesion;

@Remote
public interface FacturaPagosTimbreRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void setInfoSesion(long idUsuario, long idEmpresa, long codigoEmpresa);
	
	public long save(FacturaPagosTimbre entity) throws Exception;
	
	public FacturaPagosTimbre saveOrUpdate(FacturaPagosTimbre entity) throws Exception;
	
	public List<FacturaPagosTimbre> saveOrUpdateList(List<FacturaPagosTimbre> entities) throws Exception;

	public void update(FacturaPagosTimbre entity) throws Exception;
	
	// -------------------------------------------------------------------------------------------------------------------
	
	public FacturaPagosTimbre findById(long idTimbre) throws Exception;
	
	public List<FacturaPagosTimbre> findAll() throws Exception;
	
	public List<FacturaPagosTimbre> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<FacturaPagosTimbre> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public FacturaPagosTimbre comprobarTimbre(String serie, String folio) throws Exception;
}
