package net.giro.cargas.documentos.impresion;

import java.util.HashMap;

import org.apache.log4j.Logger;

import net.giro.comun.Errores;

public class ConcurrenteImpresion {
	private static Logger log = Logger.getLogger(ConcurrenteImpresion.class);

	public ConcurrenteImpresion() {}
	
	public Resultado procesaImpresion(String aplicacionId, String programaId, String tipo, String usuario, String rol, String nombreDoc, HashMap<String, String> parametrosReporte) {
		ParametrosSocket  param;
		SocketPoolImp sockPool;
		Resultado resultado;
		
		param= new ParametrosSocket();
		sockPool= new SocketPoolImp ();
		resultado= new Resultado(); 

		try {
			if (param.getCodigo() == 0L) {
				param.setNombreDocumento(nombreDoc);
				param.setAplicacionId(aplicacionId);
				param.setProgramaId(programaId);
				param.setTipo(tipo);
				param.setUsuario(usuario);
				param.setRol(rol);
				param.setParametrosReporte(parametrosReporte);
				sockPool.setParamSocket(param);
				sockPool.SocketPoolImpresion();
				resultado= sockPool.getResultado();
				resultado.setID( sockPool.getID()) ;
			}
		} catch (Exception ex) {
			resultado.setCodigo(Errores.ERROR_INESPERADO);
			resultado.setError_descr(ex.toString());
			log.error(String.format("CurrenteImpresion::procesaImpresion Exception: %s", ex.toString() ) );
		}
		
		return resultado;
	}
}
