package net.giro.plataforma.ubicacion.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.ubicacion.beans.Colonia;

@Remote
public interface ColoniaDAO extends DAO<Colonia> {
	public List<Colonia> findByPropertyLikeValor(String propertyName,final Object value,String propertyName2,final Object value2) throws ExcepConstraint;
}
