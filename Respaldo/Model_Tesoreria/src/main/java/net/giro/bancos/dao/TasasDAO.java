package net.giro.bancos.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.bancos.beans.Tasas;

@Remote
public interface TasasDAO extends DAO<Tasas> {

}
