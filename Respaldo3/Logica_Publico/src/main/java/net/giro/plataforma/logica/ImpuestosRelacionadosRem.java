package net.giro.plataforma.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ImpuestosRelacionados;

@Remote
public interface ImpuestosRelacionadosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ImpuestosRelacionados entity) throws Exception;
	
	public List<ImpuestosRelacionados> saveOrUpdateList(List<ImpuestosRelacionados> entities) throws Exception;
	
	public List<ImpuestosRelacionados> findByImpuesto(Long idImpuesto) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-04-15 | Javier Tirado 	| Creacion de EJB.
 */