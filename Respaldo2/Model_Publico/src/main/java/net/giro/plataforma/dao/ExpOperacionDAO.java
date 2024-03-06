package net.giro.plataforma.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.beans.ExpOperacion;

@Remote
public interface ExpOperacionDAO extends DAO<ExpOperacion> {

}
