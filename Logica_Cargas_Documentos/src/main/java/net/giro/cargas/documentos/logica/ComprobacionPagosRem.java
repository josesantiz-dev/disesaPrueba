package net.giro.cargas.documentos.logica;

import java.util.List;

import javax.ejb.Remote;

import mx.gob.sat.cfdi.v40.Comprobante;
import net.giro.cargas.documentos.beans.ComprobacionPago;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface ComprobacionPagosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public ComprobacionPago save(ComprobacionPago entity) throws Exception;
	
	public List<ComprobacionPago> saveOrUpdateList(List<ComprobacionPago> entities) throws Exception;
	
	public void cancelar(long idComprobacionPago) throws Exception;
	
	public void deleteCFDI(long idComprobacionPago) throws Exception;

	public ComprobacionPago findById(long idComprobacionPago) throws Exception;

	public Respuesta comprobarEstatusSAT(long idComprobacionPago) throws Exception;
	
	public Respuesta importarCFDI(byte[] fileSrc) throws Exception;
	
	public Respuesta importarCFDI(Comprobante comprobante) throws Exception;
}
