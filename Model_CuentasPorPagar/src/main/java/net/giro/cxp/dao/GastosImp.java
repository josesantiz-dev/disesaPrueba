package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;

import net.giro.cxp.beans.Gastos;

@Stateless
public class GastosImp extends DAOImpl<Gastos> implements GastosDAO {
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Gastos entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Gastos> saveOrUpdateList(List<Gastos> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
