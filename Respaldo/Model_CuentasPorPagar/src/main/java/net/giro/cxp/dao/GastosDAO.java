package net.giro.cxp.dao;

import net.giro.DAO;
import javax.ejb.Remote;
import net.giro.cxp.beans.Gastos;

@Remote
public interface GastosDAO extends DAO<Gastos> {

}
