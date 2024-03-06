package net.giro.plataforma.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.beans.ConGrupoValores;

@Remote
public interface ConGrupoValoresDAO extends DAO<ConGrupoValores> {
	public List<ConGrupoValores> findLikeClaveNombre(Object value,int max);
}
