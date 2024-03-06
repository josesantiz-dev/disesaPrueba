package net.giro.tyg.dao;


import java.util.List;
import java.util.Map;

import net.giro.DAO;
import net.giro.tyg.admon.MovimientosCuentas;

import javax.ejb.Remote;


@Remote
public interface MovimientosCuentasDAO extends DAO<MovimientosCuentas> {
	public List<MovimientosCuentas> query(String queryString, Map<String, Object> parameters);
}
