package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.FacturasRelacionadas;
import net.giro.plataforma.InfoSesion;

@Remote
public interface FacturasRelacionadasRem {
	public void showSystemOuts(boolean value);
	
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void setInfoSesion(long idUsuario, long idEmpresa, long codigoEmpresa);

	public Long save(FacturasRelacionadas entity) throws Exception;
	
	public void deleteFacturasRelacionadas(long idFactura) throws Exception;
	
	public List<FacturasRelacionadas> findFacturasById(long idFactura) throws Exception;
	
}