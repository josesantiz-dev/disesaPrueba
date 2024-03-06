package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.PersonaNombresAlterno;

@Remote
public interface PersonaNombresAlternoDAO extends DAO<PersonaNombresAlterno> {

}
