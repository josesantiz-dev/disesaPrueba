package net.giro.bancos.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.bancos.beans.TiposTasas;

@Remote
public interface TiposTasasDAO extends DAO<TiposTasas> {

}
