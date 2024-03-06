package net.giro.cargas.documentos.logica;

import java.util.List;

import javax.ejb.Remote;

import mx.gob.sat.cfdi.v40.Comprobante;
import net.giro.cargas.documentos.beans.ComprobacionNomina;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface ComprobacionNominaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public ComprobacionNomina save(ComprobacionNomina entity) throws Exception;
	
	public List<ComprobacionNomina> saveOrUpdateList(List<ComprobacionNomina> entities) throws Exception;
	
	public void cancelar(long idComprobacionNomina) throws Exception;
	
	public void deleteCFDI(long idComprobacionNomina) throws Exception;

	public ComprobacionNomina findById(long idComprobacionNomina) throws Exception;

	public Respuesta comprobarEstatusSAT(long idComprobacionNomina) throws Exception;
	
	public Respuesta importarCFDI(byte[] fileSrc) throws Exception;
	
	public Respuesta importarCFDI(Comprobante comprobante) throws Exception;
}
