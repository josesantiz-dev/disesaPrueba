package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.PerfilAccesoDetalle;

@Remote
public interface PerfilAccesoDetalleDAO extends DAO<PerfilAccesoDetalle> {

}
