package net.giro.adp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraCobranza;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraCobranzaRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(ObraCobranza entity) throws Exception;

	public List<ObraCobranza> saveOrUpdateList(List<ObraCobranza> entities) throws Exception;

	public void update(ObraCobranza entity) throws Exception;

	public void delete(Long idObraCobranza) throws Exception;

	public void deleteAll(List<ObraCobranza> entities) throws Exception;

	public ObraCobranza findById(Long idObraCobranza) throws Exception;

	public List<ObraCobranza> findAll(Long idObra, String orderBy, int limite) throws Exception;

	public List<ObraCobranza> findLikeProperty(String propertyName, final Object value, Long idObra, String orderBy, int limite) throws Exception;

	public List<ObraCobranza> findByProperty(String propertyName, final Object value, Long idObra, String orderBy, int limite) throws Exception;

	public List<ObraCobranza> findByFactura(long idFactura) throws Exception;
	
	public void actualizarCobranza(Long idObra, List<ObraCobranza> entities) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 24/05/2016 | Javier Tirado	| Creacion de ObraCobranzaRem