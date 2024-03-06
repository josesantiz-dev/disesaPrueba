package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.CampoTabla;

@Remote
public interface CampoTablaDAO extends DAO<CampoTabla> {

}
