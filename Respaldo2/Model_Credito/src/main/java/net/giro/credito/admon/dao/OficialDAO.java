package net.giro.credito.admon.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.credito.admon.beans.Oficial;

@Remote
public interface OficialDAO extends DAO<Oficial> {
	public List<Oficial> findByListUsuariosId(List<Long> listUsuariosId);
}
