package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.Aplicacion;

@Remote
public interface AplicacionDAO extends DAO<Aplicacion> {

}
