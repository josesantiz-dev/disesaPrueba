package net.giro.cargas.documentos.logica;

import java.util.Date;

import javax.ejb.Remote;

import net.giro.cargas.documentos.plataforma.InfoSesion;
import net.giro.cargas.documentos.respuesta.Respuesta;

@Remote
public interface FacturasRem {
	public Respuesta analizarXML(byte[] archivoSrc);
	
    public InfoSesion getInfoSesion();
    public void setInfoSesion(InfoSesion infoSesion);
    public void setInfoSesion(Date ultimaModificacion, Long idAcceso);
}
