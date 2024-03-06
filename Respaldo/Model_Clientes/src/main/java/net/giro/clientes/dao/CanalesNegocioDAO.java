package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.CanalesNegocio;

@Remote
public interface CanalesNegocioDAO extends DAO<CanalesNegocio> {

}
