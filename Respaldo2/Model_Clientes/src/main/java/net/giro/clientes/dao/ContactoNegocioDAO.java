package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.ContactoNegocio;

@Remote
public interface ContactoNegocioDAO extends DAO<ContactoNegocio> {

}
