package net.giro.cargas.documentos.dao;


import java.util.List;

import net.giro.DAOImplFact;
import net.giro.cargas.documentos.beans.ComprobacionFactura;

import javax.ejb.Stateless;


@Stateless
public class ComprobacionFacturaImp extends DAOImplFact<ComprobacionFactura> implements ComprobacionFacturaDAO  {
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(ComprobacionFactura entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<ComprobacionFactura> saveOrUpdateList(List<ComprobacionFactura> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
