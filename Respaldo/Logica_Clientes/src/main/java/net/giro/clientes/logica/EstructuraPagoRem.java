package net.giro.clientes.logica;

import javax.ejb.Remote;

import net.giro.clientes.beans.EstructuraImportacionCuentaExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface EstructuraPagoRem {
	
	public Respuesta cargarEstructurasImportacion();

	public Respuesta eliminarEstructuraImportacion(EstructuraImportacionCuentaExt pojoEstructuraImportacionExt);
	
	public Respuesta guardarEstructuraImportacion(EstructuraImportacionCuentaExt pojoEstructuraImportacionExt);

	public Respuesta listarCtasBancos();
	
    public InfoSesion getInfoSesion();
    public void setInfoSesion(InfoSesion infoSesion);
}
