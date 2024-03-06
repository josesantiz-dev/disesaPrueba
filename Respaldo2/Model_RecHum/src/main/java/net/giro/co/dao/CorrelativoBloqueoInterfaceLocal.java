package net.giro.co.dao;

import javax.ejb.Local;

@Local
public interface CorrelativoBloqueoInterfaceLocal {
	
	public Long GenerarCorrelativo(Long strCodEmpr,String strNombTab);

}
