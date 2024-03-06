package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.DirectorioTelefonico;

@Remote
public interface DirectorioTelefonicoDAO extends DAO<DirectorioTelefonico> {

}
