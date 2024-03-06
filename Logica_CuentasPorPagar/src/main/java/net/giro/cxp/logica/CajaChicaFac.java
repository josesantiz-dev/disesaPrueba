package net.giro.cxp.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.tyg.admon.Cheques;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.FacturaExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.plataforma.InfoSesion;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.ChequesRem;
import net.giro.tyg.logica.CuentasBancariasRem;
import net.giro.util.cxp.Errores;

import org.apache.log4j.Logger;

@Stateless
public class CajaChicaFac implements CajaChicaRem{
	private static Logger log = Logger.getLogger(CajaChicaFac.class);
	private InitialContext ctx = null;
	private String modulo = "CXP";
	private InfoSesion infoSesion;
	private PagosGastosRem ifzPagosGastos;
	private PersonaRem ifzPersonas;
	private SucursalesRem ifzSucursales;	
	private ChequesRem ifzCheques;
	private CuentasBancariasRem ifzCtasBancos;
	private ConvertExt convertidor;
	
	public CajaChicaFac(){
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);

    		this.ifzPagosGastos	= (PagosGastosRem)		ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
    		this.ifzPersonas 	= (PersonaRem)			ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");	
    		this.ifzSucursales 	= (SucursalesRem)		ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
    		this.ifzCheques 	= (ChequesRem)			ctx.lookup("ejb:/Logica_TYG//ChequesFac!net.giro.tyg.logica.ChequesRem");
    		this.ifzCtasBancos 	= (CuentasBancariasRem)	ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
    		
    		convertidor = new ConvertExt();    		
    		infoSesion = new InfoSesion();
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear CajaChicaFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
		this.ifzPagosGastos.setInfoSesion(this.infoSesion);
	}

	@Override
	public List<PagosGastos> saveOrUpdateList(List<PagosGastos> entities) throws Exception {
		try {
			this.ifzPagosGastos.setInfoSesion(this.infoSesion);
			return this.ifzPagosGastos.saveOrUpdateList(entities);
		} catch (Exception e) {
			throw e;
		}
	}

	// --------------------------------------------------------------------------------------
	// EXTENDIDO
	// --------------------------------------------------------------------------------------

	@Override
	public Respuesta salvar(PagosGastosExt entityExt, boolean band) throws Exception{
		Respuesta reg = new Respuesta();
		try{
			entityExt.setBeneficiario(entityExt.getIdBeneficiario().getNombre());
			entityExt.setTipoBeneficiario("P");

			this.ifzCtasBancos.setInfoSesion(this.infoSesion);
			if (! this.ifzCtasBancos.haySaldoSuficiente(entityExt.getMonto(), entityExt.getIdCuentaOrigen().getId())){
				reg.setResultado(5);
				reg.setRespuesta("No hay saldo sufuciente");
			} else {
				this.ifzPagosGastos.setInfoSesion(this.infoSesion);
				reg = this.ifzPagosGastos.salvar(entityExt);
			}
		}catch(Exception re){
			log.error("error en metodo salvar", re);
			reg.setResultado(-1);
			reg.setRespuesta("Error al salvar");
			throw re;
		}
	
		return reg;
	}

	@Override
	public Respuesta actualizar(PagosGastosExt entityExt, String estatus, Date fech_modificacion) throws Exception {
		Respuesta reg = new Respuesta();
		
		try{
			entityExt.setBeneficiario(entityExt.getIdBeneficiario().getNombre());
			entityExt.setTipoBeneficiario("P");
			entityExt.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entityExt.setFechaModificacion(fech_modificacion);
			entityExt.setEstatus(estatus);

			this.ifzPagosGastos.setInfoSesion(this.infoSesion);
			this.ifzPagosGastos.update(entityExt);
			
			reg.setObjeto(entityExt);
			reg.setResultado(0);
		}catch(Exception re){
			log.error("error en metodo salvar", re);
			reg.setResultado(-1);
			reg.setRespuesta("Error al salvar");
			throw re;
		}
	
		return reg;
	}

	@Override
	public Respuesta cancelacion(PagosGastosExt entityExt, Date fech_modificacion) throws Exception{
		try {
			this.ifzPagosGastos.setInfoSesion(this.infoSesion);
			return this.ifzPagosGastos.cancelacion(entityExt, fech_modificacion);
		} catch(Exception re) {
			log.error("error en metodo cancelacion", re);
			throw re;
		}
	}
	
	@Override
	public Respuesta buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda){
		Respuesta respuesta = new Respuesta();

		try{
			this.ifzPagosGastos.setInfoSesion(this.infoSesion);
			System.out.println("---> LOGICA: buscarMovimientosCuentas('" + tipoBusqueda + "', '" + valorBusqueda.toString() + "')");
			//tipo 'C' al parecer es para Caja chica
			//estatus 'G' -> generado. estatus 'X' -> cancelacion
			
			/*String queryString = "select mov from pagos_gastos mov " +
								"where mov.tipo = 'M' and (mov.estatus = 'G' or mov.estatus = 'X') ";
			
			Map<String, Object> params = new HashMap<String, Object>();
			
			if(tipoBusqueda.equals("fecha")){
				Date fecha = (Date) valorBusqueda;
				queryString += "and mov.fecha = :valorBusqueda";
				
				params.put("valorBusqueda", fecha);
			} else if (tipoBusqueda.equals("beneficiario")) {
				String nombre = (String) valorBusqueda;
				
				HashMap<String, Long> idsPersonas = this.ifzPersonas.getIdListLikeNombre(nombre);
				
				int id = 1;
				for(Entry<String, Long> e : idsPersonas.entrySet()){
					params.put(e.getKey(), e.getValue());
					if(id == 1)
						queryString += "and ( mov.idBeneficiario = :valor" + id +" ";
					else
						queryString += "or mov.idBeneficiario = :valor" + id + " ";
					id++;
				}
				if(id > 1)
					queryString += ") ";
			} else if(tipoBusqueda.equals("idCuentaOrigen")) {
				CtasBancoExt pojoCtasBancoExt = (CtasBancoExt) valorBusqueda;
				Long idCuenta = Long.valueOf(pojoCtasBancoExt.getId());
				
				queryString += "and mov.idCuentaOrigen = :valorBusqueda";
				
				params.put("valorBusqueda", idCuenta);
			}
			
			System.out.println("---> QUERY: " + queryString);
			List<PagosGastos> listPagosGastos = ifzPagosGastos.query(queryString, params);*/

			/*List<PagosGastosExt> listPagosGastosExt = new ArrayList<PagosGastosExt>();
			List<PagosGastos> listPagosGastos = ifzPagosGastos.buscarMovimientosCuentas(tipoBusqueda, valorBusqueda);
			for(PagosGastos pojoPagosGastos : listPagosGastos){
				listPagosGastosExt.add(convertidor.PagosGastosToPagosGastosExt(pojoPagosGastos));
			}*/
			
			List<PagosGastosExt> listPagosGastosExt = this.ifzPagosGastos.buscarMovimientosCuentas(tipoBusqueda, valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMovimientosCuentas", listPagosGastosExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_MOVIMIENTOS_CUENTAS);
			respuesta.setBody(null);
			log.error("Error en CajaChicaFac.buscarMovimientosCuentas", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta buscarCuentasBanco(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		
		try{
			this.ifzCtasBancos.setInfoSesion(this.infoSesion);
			System.out.println("---> LOGICA: buscarCuentasBanco('" + tipoBusqueda + "', '" + valorBusqueda.toString() + "')");
			List<CuentaBancaria> listCuentasBanco = this.ifzCtasBancos.findLikeColumnName(tipoBusqueda, valorBusqueda);
			List<CtasBancoExt> listCuentasBancoExt = new ArrayList<CtasBancoExt>();
			
			for(CuentaBancaria pojoCtasBanco : listCuentasBanco){
				listCuentasBancoExt.add(convertidor.CtasBancoToCtasBancoExt(pojoCtasBanco));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listCuentasBanco", listCuentasBancoExt);
		} catch (Exception e) {
			log.error("Error en CajaChicaFac.buscarCuentasBanco", e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_CUENTAS_BANCO);
			respuesta.setBody(null);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarPersonas(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		try {
			this.ifzPersonas.setInfoSesion(this.infoSesion);
			List<PersonaExt> listPersonasExt = new ArrayList<PersonaExt>();
			List<Persona> listPersonas = ifzPersonas.findLikeColumnName(tipoBusqueda, valorBusqueda);
			
			for (Persona pojoPersona : listPersonas){
				listPersonasExt.add(convertidor.PersonaToPersonaExt(pojoPersona));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listPersonas", listPersonasExt);
		} catch (Exception e) {
			log.error("Error en CajaChicaFac.buscarPersonas", e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_PERSONAS);
			respuesta.setBody(null);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarSucursales(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		try { 
			this.ifzPersonas.setInfoSesion(this.infoSesion);
			List<Sucursal> listSucursales = ifzSucursales.findLikeColumnName(tipoBusqueda, valorBusqueda);
			/*List<SucursalExt> listSucursalesExt = new ArrayList<SucursalExt>();
			
			for(Sucursal pojoSucursal : listSucursales) {
				listSucursalesExt.add(convertidor.SucursalToSucursalExt(pojoSucursal));
			}*/
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listSucursales", listSucursales);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_SUCURSALES);
			respuesta.setBody(null);
			log.error("Error en CajaChicaFac.buscarSucursales", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta guardarMovimientosCuentas(PagosGastosExt pojoPagosGastosExt, List<FacturaExt> listFacturas) {
		Respuesta respuesta = new Respuesta();
		try {
			this.ifzPagosGastos.setInfoSesion(this.infoSesion);
			if("G".equals(pojoPagosGastosExt.getOperacion()))
				pojoPagosGastosExt.setIdCuentaOrigen(null); //Desconosco por que hace esto, Comprobacion de caja chica = "G", por cierto nunca es operacion "G"...
			
			if(pojoPagosGastosExt.getOperacion().equals("C") || pojoPagosGastosExt.getOperacion().equals("2")){
				if(listFacturas.isEmpty()){
					respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FALTAN_FACTURAS);
					respuesta.setBody(null);
					return respuesta;
				}
				
					//Aqui deberia realizar una ejecucion de la funcion cancela_cheques, cheque la funcion y esta toda comentada.
					//No jale la funcion, omito esta parte por que no hacia nada realmente...
				Respuesta respuestaCheque = generaCheque(pojoPagosGastosExt);
				if(respuestaCheque.getErrores().getCodigoError() > 0L){
					return respuestaCheque;
				}
			}
			
			PagosGastos pojoPagosGastos = convertidor.PagosGastosExtToPagosGastos(pojoPagosGastosExt);
			
			pojoPagosGastos.setModificadoPor((short) infoSesion.getAcceso().getId()); 
			pojoPagosGastos.setFechaModificacion(Calendar.getInstance().getTime());
			
			if(pojoPagosGastos.getIdPagosGastosRef() == 0L){
				pojoPagosGastos.setTipo("C"); //Comprobacion de caja chica
				pojoPagosGastos.setEstatus("G"); //Generado
				
				pojoPagosGastos.setCreadoPor((short)infoSesion.getAcceso().getId());
				pojoPagosGastos.setFechaCreacion(Calendar.getInstance().getTime());
				
				this.ifzPagosGastos.save(pojoPagosGastos);
			} else
				this.ifzPagosGastos.update(pojoPagosGastos);
			
			pojoPagosGastosExt = convertidor.PagosGastosToPagosGastosExt(pojoPagosGastos);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoMovimientosCuentas", pojoPagosGastos);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_MOVIMIENTOS_CUENTAS);
			respuesta.setBody(null);
			log.error("Error en CajaChicaFac.guardarMovimientosCuentas", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta generaCheque(PagosGastosExt pojoPagosGastos){
		Respuesta respuesta = new Respuesta();
		try{
			Cheques pojoCheque = new Cheques();
			
			pojoCheque.setBancoId(pojoPagosGastos.getIdCuentaOrigen().getId());
			String control = String.format("%06D", pojoPagosGastos.getNoCheque()) + "-" + 
					pojoPagosGastos.getIdCuentaOrigen().getSucursalBancaria().getNombre();
			if(control.length() > 17)
				control = control.substring(0, 17);
			pojoCheque.setControl(control);
			pojoCheque.setMinistracion(0L);
			pojoCheque.setEstatus("E");
			pojoCheque.setTipo("T");
			pojoCheque.setFecha(pojoPagosGastos.getFecha());
			pojoCheque.setImporte(BigDecimal.valueOf(pojoPagosGastos.getMonto()));
			pojoCheque.setFolio(pojoPagosGastos.getNoCheque().toString());
			
			pojoCheque.setCreadoPor(infoSesion.getAcceso().getId());
			pojoCheque.setFechaCreacion(Calendar.getInstance().getTime());
			pojoCheque.setModificadoPor(infoSesion.getAcceso().getId());
			pojoCheque.setFechaModificacion(Calendar.getInstance().getTime());

			this.ifzCheques.setInfoSesion(this.infoSesion);
			pojoCheque.setId(ifzCheques.save(pojoCheque));
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoCheque", pojoCheque);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GENERAR_CHEQUE);
			respuesta.setBody(null);
			log.error("Error en CajaChicaFac.generaCheque", e);
		}
		return respuesta;
	}
}
