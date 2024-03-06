package net.giro.tyg.logica;

import java.math.BigDecimal;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.CtasBanco;

public class CtasBancoOperaciones implements CtasBancoOperacionesRem {
	private InitialContext ctx;
	//private Object lookup;
	private CtasBancoRem ifzBanco;
	
	public CtasBancoOperaciones() throws NamingException,Exception{	
		Hashtable<String, Object> p = new Hashtable<String, Object>();
        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");            
        ctx = new InitialContext(p);
        
		this.ifzBanco = (CtasBancoRem) ctx.lookup("ejb:/Logica_TYG//CtasBancoRemFac!net.giro.clientes.dao.CtasBancoRem");
	}
	
	public String aplicaCargoAbono(double monto, CtasBanco ctaBancaria, String movto) throws ExcepConstraint{
		try{
			BigDecimal mto = new BigDecimal(monto);
			BigDecimal saldo = ctaBancaria.getSaldo();
			saldo = saldo.setScale(2,BigDecimal.ROUND_HALF_UP);
			mto = mto.setScale(2, BigDecimal.ROUND_HALF_UP);
			if("+".equals(movto))
				saldo = saldo.add(mto);
			else if ("-".equals(movto))
				saldo = saldo.subtract(mto);
			ctaBancaria.setSaldo(saldo);
			ifzBanco.update(ctaBancaria);
			return "OK";
		}catch (RuntimeException re) {
			throw re;
		}
	}
}
