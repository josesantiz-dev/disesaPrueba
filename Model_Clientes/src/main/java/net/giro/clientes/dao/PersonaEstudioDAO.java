package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.PersonaEstudio;

@Remote
public interface PersonaEstudioDAO extends DAO<PersonaEstudio> {

}
