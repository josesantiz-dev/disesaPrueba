package net.giro.tyg.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.tyg.admon.CuentaBancaria;

@Remote
public interface CuentasBancariasRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public long save(CuentaBancaria entity) throws Exception;

	public void delete(CuentaBancaria entity) throws Exception;
	
	public CuentaBancaria update(CuentaBancaria entity) throws Exception;

	public CuentaBancaria findById(long id);

	public CuentaBancaria findAllById(long id);

	public List<CuentaBancaria> findAllByProperty(String propertyName, String value, int maximo,String empresas);
	
	public List<CuentaBancaria> findByProperty(String propertyName, Object value,String empresas);
	
	public List<CuentaBancaria> findLikeColumnName(String tipoBusqueda, String valorBusqueda);
	
	public List<CuentaBancaria> findLikeClaveNombreCuenta(String value, int max, String empresas, Long empresaId);
	
	public List<CuentaBancaria> findAll(String empresas);
	
	public List<CuentaBancaria> findTodas();
	
	public String aplicaCargo(Double monto, CuentaBancaria ctaBancaria);
	
	public String aplicaCargoAbono(double monto, CuentaBancaria ctaBancaria, String movto) throws Exception;
	
	public boolean haySaldoSuficiente(double montoSolicitado, long bancoId);
	
	public boolean esBancoCierre(final Object value);
}

