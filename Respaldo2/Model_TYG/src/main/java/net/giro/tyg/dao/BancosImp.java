package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;

import net.giro.tyg.admon.Banco;

@Stateless
public class BancosImp extends DAOImpl<Banco> implements BancosDAO {
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Banco entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Banco> saveOrUpdateList(List<Banco> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

}
