package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.seguridad.beans.Responsabilidad;

@Remote
public interface ResponsabilidadDAO extends DAO<Responsabilidad> {
	public List<Responsabilidad> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint;
}
