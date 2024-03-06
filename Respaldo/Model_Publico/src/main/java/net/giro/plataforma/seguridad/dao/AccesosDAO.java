package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.Acceso;

@Remote
public interface AccesosDAO extends DAO<Acceso> {

}
