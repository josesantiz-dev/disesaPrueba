package net.giro.cxp.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.cxp.beans.Retenciones;

@Remote
public interface RetencionesDAO extends DAO<Retenciones> {
}
