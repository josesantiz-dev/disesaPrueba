package net.giro.plataforma.seguridad.dao;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.comun.ExcepConstraint;

import net.giro.plataforma.seguridad.beans.Menu;

@Stateless
public class MenuImp extends DAOImpl<Menu> implements MenuDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	public void deleteByMenu(Menu menu) throws ExcepConstraint{
		String queryString = null;
		Query query = null;
		queryString = "select res from Responsabilidad res where res.menu.id = " + menu.getId() + " ";
		query = entityManager.createQuery(queryString);
		//query.setParameter("menu", menu);
		if(query.getResultList().size() > 0)
			throw new ExcepConstraint("Imposible eliminar el menu, esta asignado a una o mas responsabilidades.", "Menu");

		queryString = "delete from MenuFuncion mf where mf.menu.id = " + menu.getId() + " ";
		//query.setParameter("menu", menu);
		query = entityManager.createQuery(queryString);
		query.executeUpdate();

		menu = entityManager.getReference(Menu.class, menu.getId());
		entityManager.remove(menu);
	}
}
