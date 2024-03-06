package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.seguridad.beans.Menu;

@Remote
public interface MenuDAO extends DAO<Menu> {
	public void deleteByMenu(Menu menu) throws ExcepConstraint;
}
