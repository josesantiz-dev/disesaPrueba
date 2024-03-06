package net.giro.cxp.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.cxp.beans.FacturaExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosExt; // import net.giro.cxp.beans.MovimientosCuentasExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface CajaChicaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public List<PagosGastos> saveOrUpdateList(List<PagosGastos> entities) throws Exception;

	public Respuesta salvar(PagosGastosExt entityExt, boolean band) throws Exception;
	
	public Respuesta actualizar(PagosGastosExt entityExt, String estatus,Date fech_modificacion) throws Exception;
	
	public Respuesta cancelacion(PagosGastosExt entityExt, Date fech_modificacion) throws Exception;
	
	public Respuesta buscarCuentasBanco(String tipoBusqueda, String valorBusqueda);
	
	public Respuesta buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda);
	
	public Respuesta buscarPersonas(String tipoBusqueda, String valorBusqueda);
	
	public Respuesta buscarSucursales(String tipoBusqueda, String valorBusqueda);
	
	public Respuesta generaCheque(PagosGastosExt pojoPagosGastos);
	
	public Respuesta guardarMovimientosCuentas(PagosGastosExt pojoPagosGastos, List<FacturaExt> listFacturas);
}
