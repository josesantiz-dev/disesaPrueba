package net.giro.cargas.documentos.dao;


import net.giro.DAO;
import net.giro.cargas.documentos.beans.ComprobacionFactura;

import javax.ejb.Remote;


@Remote
public interface ComprobacionFacturaDAO extends DAO<ComprobacionFactura> {
	
}
