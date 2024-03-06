package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.Ejecutable;

@Remote
public interface EjecutableDAO extends DAO<Ejecutable> {

}
