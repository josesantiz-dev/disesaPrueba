package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.ContactoPersona;

@Remote
public interface ContactoPersonaDAO extends DAO<ContactoPersona> {

}
