package net.giro.plataforma.ubicacion.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.ubicacion.beans.Estado;

@Remote
public interface EstadoDAO extends DAO<Estado> {

}
