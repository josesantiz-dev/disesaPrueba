package net.giro.cxp.logica;

import javax.ejb.Remote;

import net.giro.cxp.beans.PagosGastos;

@Remote
public interface sendMessageRem {
	public void enviar(PagosGastos mc1, Long emp);
}
