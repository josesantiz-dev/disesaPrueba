package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.Seguimiento;
import net.giro.comun.ExcepConstraint;

@Remote
public interface SeguimientoDAO extends DAO<Seguimiento> {
	public List<Seguimiento> findLikePropiedadYEstatus(String propiedad, String valor, Long valor2) throws ExcepConstraint;
}
