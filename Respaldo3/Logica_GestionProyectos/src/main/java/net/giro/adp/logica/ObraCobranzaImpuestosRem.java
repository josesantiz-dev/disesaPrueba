package net.giro.adp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraCobranzaImpuestos;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraCobranzaImpuestosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public long save(ObraCobranzaImpuestos entity) throws Exception;
	
	public List<ObraCobranzaImpuestos> saveOrUpdateList(List<ObraCobranzaImpuestos> entities) throws Exception;

	public List<ObraCobranzaImpuestos> findAll(long idObraCobranza, String orderBy) throws Exception;
}
