package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.EstadosNegocio;

@Remote
public interface EstadosNegocioDAO extends DAO<EstadosNegocio> {

}
