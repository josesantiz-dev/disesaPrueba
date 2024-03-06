package net.giro.bancos.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.bancos.beans.InstitucionBancaria;

@Remote
public interface InstitucionBancariaDAO extends DAO<InstitucionBancaria> {

}
