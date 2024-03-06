package net.giro.tyg.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.beans.ConValores;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.admon.GastoDeCobranza;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.admon.Tasas;
import net.giro.tyg.admon.TiposTasas;
import net.giro.tyg.admon.ValoresTasas;
import net.giro.tyg.admon.SucursalBancaria;
import net.giro.plataforma.InfoSesion;

@Remote
public interface TygRem {
	
	//interface de los metodos guardar o salvar
	public Respuesta salvar(Banco catbancos);
	public Respuesta salvar(Moneda moneda);
	public Respuesta salvar(CuentaBancaria ctasbanco);
	public Respuesta salvar(Tasas tasas);
	public Respuesta salvar(MonedasValores monedavalores);
	public Respuesta salvar(SucursalBancaria sucursalbancaria);	
	public Respuesta salvar(TiposTasas tipotasas);
	public Respuesta salvar(ValoresTasas valorestasas);
	public Respuesta salvar(FormasPagos formaspagos);
	public long salvar(GastoDeCobranza gastodecobranza) throws Exception;
	
	//interfaces de los metodos buscar
	public Respuesta buscarMoneda(String tipoBusqueda, String valorBusqueda);
	
	public Respuesta buscarBanco(String TipoBusqueda, String valorBusqueda);
	public Respuesta buscarFormasPagos(String tipoBusqueda, String valorBusqueda);
	public Respuesta buscarSucursal(String tipoBusqueda, String valorBusqueda);
	public Respuesta buscarCtasBancos(String tipoBusqueda, String valorBusqueda);
	public Respuesta buscarMonedaV(Long monedaBase, Long monedaDestino);
	public Respuesta buscarValoresTasas(String tipoBusqueda, String valorBusqueda);
	public Respuesta buscarTasas(String tipoBusqueda, String valorBusqueda);
	public Respuesta buscarTipoTasas(String tipoBusqueda, String valorBusqueda);
	public List<GastoDeCobranza> buscarGastosDeCobranza(String tipoBusqueda, String valorBusqueda) throws Exception;
//	public List<Seguimiento> buscarSeguimientos(String tipoBusqueda, String valorBusqueda, String valorStatus) throws Exception;
	public List<ConValores> buscarEstatus(String tipoBusqueda, String valorBusqueda) throws Exception;
	
	
	//interface de los metodos autocompletar
	public Respuesta autocompletarMoneda();
	public Respuesta autoacompletarBanco();
	public Respuesta autoacompletarEmpresa();
	public Respuesta autoacompletarSucursal();
	public Respuesta autoacompletarEstado();
	public Respuesta autoCargarTasas();
	public List<MonedasValores> autoacompletarMonedaValor()throws Exception;

	//interface de los metodos eliminar
	public Respuesta eliminarSucursalBancaria(SucursalBancaria sucursalbancaria);
	public Respuesta eliminarCtasBancarias(CuentaBancaria ctasbanco);
	public Respuesta eliminarMonedaValor(MonedasValores monedavalores);
	public Respuesta eliminarBancos(Banco catbancos);
	public Respuesta eliminarValoresTasas(ValoresTasas valorestasas);
	public Respuesta eliminarTipoTasas(TiposTasas tipotasas);
	public Respuesta eliminarTasas(Tasas tasas);
	public Respuesta eliminarFormasPago(FormasPagos formaspagos) throws Exception;
	public void eliminarGastosDeCobranza(GastoDeCobranza gastosdecobranza) throws Exception ;
	
	public Respuesta listarMonedasValores();
	
	public InfoSesion getInfoSesion();
	public void setInfoSesion(InfoSesion infoSesion);
}
