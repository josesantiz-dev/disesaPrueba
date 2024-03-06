package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.PerfilAcceso;

@Remote
public interface PerfilAccesoDAO extends DAO<PerfilAcceso> {

}
