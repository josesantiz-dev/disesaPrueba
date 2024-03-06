package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.InsumosDetalles;

@Remote
public interface InsumosDetallesDAO  extends DAO<InsumosDetalles> {
	public List<InsumosDetalles> findAll(Long idInsumos);

	public List<InsumosDetalles> findByProperty(String propertyName, final Object value, int max);

	public List<InsumosDetalles> findLikeProperty(String propertyName, final Object value, int max);
}
