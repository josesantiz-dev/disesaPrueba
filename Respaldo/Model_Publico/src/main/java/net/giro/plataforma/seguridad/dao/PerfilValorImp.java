package net.giro.plataforma.seguridad.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAOImpl;
import net.giro.plataforma.seguridad.beans.Perfil;
import net.giro.plataforma.seguridad.beans.PerfilValor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PerfilValorImp extends DAOImpl<PerfilValor> implements PerfilValorDAO  {
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public PerfilValor findByPerfilNivelValor(Perfil pg, Long nivel, Long valorNivel) throws Exception {
		List<PerfilValor> res = null;
		
		try {
			final String queryString = "select pv from PerfilValor pv where " +
			" pv.perfil.id = " + pg.getId() + " and pv.nivel = " + nivel + (nivel == 64 ? "" : " and pv.valorNivel = " + valorNivel);
			Query query = entityManager.createQuery(queryString);
			res = query.getResultList();
			return res.isEmpty() ? null : res.get(0);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public String findPerfil(Perfil perfil, HashMap<Integer, String> params) {
		List<Object> res = null;
		String queryString = null;
		String valNivel = null;
		
		try {
			for(int nivel = 1; nivel <= 64 ; nivel = 2 * nivel){
				valNivel = params.get(nivel);
				if(valNivel == null)
					continue;
				queryString = "select pv.valorPerfil from PerfilesValores pv where " +
				" pv.perfilesGasto = :perfGast and pv.nivel = " + nivel + (nivel == 64 ? "" : " and pv.valorNivel = " + valNivel);
				
				Query query = entityManager.createQuery(queryString);
				query.setParameter("perfGast", perfil);
				res = query.getResultList();
				
				if(!res.isEmpty())
					break;
			}
			
			return res == null || res.isEmpty() ? "" : res.get(0).toString();
		} catch (Exception e) {
			throw e;
		}
	}
}
