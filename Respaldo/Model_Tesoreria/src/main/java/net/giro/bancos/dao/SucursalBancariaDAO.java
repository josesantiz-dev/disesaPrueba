package net.giro.bancos.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.bancos.beans.SucursalBancaria;

@Remote
public interface SucursalBancariaDAO extends DAO<SucursalBancaria> {

}
