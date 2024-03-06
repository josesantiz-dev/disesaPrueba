package net.giro.tyg.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.ValoresTasas;


@Remote
public interface ValoresTasasDAO extends DAO<ValoresTasas>{
	public List<ValoresTasas> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint;
}
