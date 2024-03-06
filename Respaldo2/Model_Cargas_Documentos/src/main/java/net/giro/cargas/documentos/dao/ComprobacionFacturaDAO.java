package net.giro.cargas.documentos.dao;


import java.util.List;

import net.giro.DAO;
import net.giro.cargas.documentos.beans.ComprobacionFactura;

import javax.ejb.Remote;


@Remote
public interface ComprobacionFacturaDAO extends DAO<ComprobacionFactura> {
	public void setEmpresa(Long idEmpresa);
	
	public long save(ComprobacionFactura entity) throws Exception;
	
	public List<ComprobacionFactura> saveOrUpdateList(List<ComprobacionFactura> entities) throws Exception;
}
