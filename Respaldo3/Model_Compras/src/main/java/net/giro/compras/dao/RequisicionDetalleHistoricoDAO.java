package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.RequisicionDetalleHistorico;

@Remote
public interface RequisicionDetalleHistoricoDAO extends DAO<RequisicionDetalleHistorico> {
    public void save(RequisicionDetalleHistorico entity) throws Exception;
	
	public void saveList(List<RequisicionDetalleHistorico> entities) throws Exception;

	public List<RequisicionDetalleHistorico> findAll(long idRequisicion) throws Exception;
}
