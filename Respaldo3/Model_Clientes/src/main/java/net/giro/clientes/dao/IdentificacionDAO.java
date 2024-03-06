package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.Identificacion;

@Remote
public interface IdentificacionDAO extends DAO<Identificacion> {

}
