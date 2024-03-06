package net.giro.bancos.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.bancos.beans.ValoresTasas;

@Remote
public interface ValoresTasasDAO extends DAO<ValoresTasas> {

}
