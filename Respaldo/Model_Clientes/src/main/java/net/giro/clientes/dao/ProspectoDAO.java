package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAO;
import net.giro.clientes.beans.Prospecto;
import net.giro.comun.ExcepConstraint;

import javax.ejb.Remote;


@Remote
public interface ProspectoDAO extends DAO<Prospecto> {
	public List<Prospecto> findLikePersonaPropiedad(String propiedad, String valor, Long valor2) throws ExcepConstraint;
	
	public List<Prospecto> findLikeNegocioPropiedad(String propiedad, String valor, Long valor2) throws ExcepConstraint;
}
