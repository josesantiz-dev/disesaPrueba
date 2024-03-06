package net.giro.cargas.documentos.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.cargas.documentos.beans.ComprobacionFactura;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface FacturasRem {
    public InfoSesion getInfoSesion();
    
	public void setInfoSesion(InfoSesion infoSesion);
    
    public void setInfoSesion(Date ultimaModificacion, Long idAcceso);
	
	public Long save(ComprobacionFactura entity) throws Exception;
	
	public List<ComprobacionFactura> saveOrUpdateList(List<ComprobacionFactura> entities) throws Exception;

	public void update(ComprobacionFactura entity) throws Exception;
	
	public void cancelar(long idComprobanteFactura) throws Exception;
	
	public void cancelar(ComprobacionFactura entity) throws Exception;
	
	public void deleteXML(long idComprobanteFactura);
	
	public Respuesta analizarXML(byte[] archivoSrc);
	
	public String getProperty(long idFactura, String property);
	
	public void setProperty(long idFactura, String property, String value) throws Exception;
}
