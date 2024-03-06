package net.giro.ne.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.ne.beans.Moneda;

@Remote
public interface MonedaDAO extends DAO<Moneda> {

}
