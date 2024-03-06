package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.Grupo;

@Remote
public interface GrupoDAO extends DAO<Grupo> {

}
