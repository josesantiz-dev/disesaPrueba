package net.giro.tyg.logica;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.CtasBanco;

@Remote
public interface CtasBancoOperacionesRem {
	public String aplicaCargoAbono(double monto, CtasBanco ctaBancaria, String movto) throws ExcepConstraint;
}
