package net.giro.plataforma.ubicacion.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.ubicacion.beans.Municipio;

@Remote
public interface MunicipioDAO extends DAO<Municipio> {
	public List<Municipio> findByPropertyLikeValor(String nombrepojo,final Object pojo,String propiedad,final Object valorpropiedad) throws ExcepConstraint;
}
