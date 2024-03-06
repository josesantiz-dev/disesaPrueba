package net.giro.adp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraSubcontratistaImpuestos;
import net.giro.adp.beans.ObraSubcontratistaImpuestosExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraSubcontratistaImpuestosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public long save(ObraSubcontratistaImpuestos entity) throws Exception;
	
	public List<ObraSubcontratistaImpuestos> saveOrUpdateList(List<ObraSubcontratistaImpuestos> entities) throws Exception;

	public List<ObraSubcontratistaImpuestos> findAll(long idObraSubcontratista, String orderBy) throws Exception;

	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public ObraSubcontratistaImpuestosExt save(ObraSubcontratistaImpuestosExt extendido) throws Exception;
	
	public List<ObraSubcontratistaImpuestosExt> saveOrUpdateListExt(List<ObraSubcontratistaImpuestosExt> extendidos) throws Exception;
	
	public List<ObraSubcontratistaImpuestosExt> findAllExt(long idObraSubcontratista, String orderBy) throws Exception;
}
