package net.giro.tyg.dao;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.tyg.admon.CatBancos;

@Remote
public interface CatBancosDAO extends DAO<CatBancos> {
	
}
