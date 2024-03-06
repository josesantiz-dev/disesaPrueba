package net.giro.cxp.dao;

import java.util.Date;
import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.cxp.beans.ProgPagos;

@Remote
public interface ProgPagosDAO extends DAO<ProgPagos> {
	public ProgPagos findByIdPojoCompleto(long id);
	public List<ProgPagos> findByProperty(String propertyName, Object value);
	public List<ProgPagos> findByRevisiones(Date fech1, Date fech2, int max);
	public List<ProgPagos> findByProgramaciones(long value, int max);
	public List<ProgPagos> findAll();
}
