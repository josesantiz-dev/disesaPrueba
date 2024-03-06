package net.giro.cxp.dao;


import net.giro.DAO;
import net.giro.cxp.beans.Factura;

import javax.ejb.Remote;


@Remote
public interface FacturaDAO extends DAO<Factura> {
	
}
