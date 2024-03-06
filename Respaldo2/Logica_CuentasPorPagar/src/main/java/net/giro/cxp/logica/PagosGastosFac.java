package net.giro.cxp.logica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import net.giro.cargas.documentos.logica.FacturasRem;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.logica.MensajeTransaccionRem;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.SucursalExt;
import net.giro.cxp.dao.PagosGastosDAO;
import net.giro.cxp.logica.ConvertExt;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.NQueryRem;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.logica.EmpresasRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Cheques;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.ChequesRem;
import net.giro.tyg.logica.CuentasBancariasRem;

@Stateless
public class PagosGastosFac implements PagosGastosRem {
	private static Logger log = Logger.getLogger(PagosGastosFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private UserTransaction utx;
	private PagosGastosDAO ifzPagosGastos;
	private PagosGastosDetRem ifzPagosGastosDet;
	private FacturasRem	ifzFacturas;
	private SucursalesRem ifzSucursal;
	private PersonaRem ifzPersona;
	private NegociosRem ifzNegocio;
	private CuentasBancariasRem ifzCtasBanco;
	private EmpresasRem ifzEmpresas;
	private sendMessageRem ifzMessage;
	private ChequesRem ifzCheques;
	private NQueryRem ifzEditorSQL;
	private ConValoresDAO ifzConValores;
	private MensajeTransaccionRem ifzMsgTransaccion;
	private ConvertExt convertidor;
	private static String orderBy;
	private static Long estatus;

	
	public PagosGastosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzPagosGastos = (PagosGastosDAO) this.ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosImp!net.giro.cxp.dao.PagosGastosDAO");
			this.ifzPagosGastosDet = (PagosGastosDetRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzFacturas = (FacturasRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//FacturasFac!net.giro.cargas.documentos.logica.FacturasRem");
    		this.ifzSucursal = (SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzPersona = (PersonaRem) this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzNegocio = (NegociosRem) this.ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
			this.ifzCtasBanco = (CuentasBancariasRem) this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
			this.ifzEmpresas = (EmpresasRem) this.ctx.lookup("ejb:/Logica_Publico//EmpresasFac!net.giro.plataforma.logica.EmpresasRem");
			this.ifzMessage = (sendMessageRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//sendMessageFac!net.giro.cxp.logica.sendMessageRem");
			//this.ifzBancosOperaciones = (CtasBancoOperacionesRem) this.ctx.lookup("ejb:/Logica_TYG/CtasBancoOperacionesFac!net.giro.tyg.logica.CtasBancoOperacionesRem");
			this.ifzCheques = (ChequesRem) this.ctx.lookup("ejb:/Logica_TYG//ChequesFac!net.giro.tyg.logica.ChequesRem");
			this.ifzEditorSQL = (NQueryRem) this.ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
			this.ifzConValores = (ConValoresDAO) this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
			this.ifzMsgTransaccion = (MensajeTransaccionRem) this.ctx.lookup("ejb:/Logica_Contabilidad//MensajeTransaccionFac!net.giro.contabilidad.logica.MensajeTransaccionRem");
			
			this.ifzMsgTransaccion.setInfoSesion(this.infoSesion);

			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("PagosGastosFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear Logica_CuentasPorPagar.PagosGastosFac", e);
			this.ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value); 
	}

	@Override
	public void orderBy(String orderBy) {
		PagosGastosFac.orderBy = orderBy;
	}

	@Override
	public void estatus(Long estatus) {
		PagosGastosFac.estatus = estatus;
	}

	@Override
	public Long save(PagosGastos object) throws Exception {
		try {
			return this.ifzPagosGastos.save(object, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastos> saveOrUpdateList(List<PagosGastos> entities) throws Exception {
		try {
			return this.ifzPagosGastos.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public PagosGastos actualizar(PagosGastos entity, String estatus, Date fech_modificacion) throws Exception {
		try {
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus(estatus);
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			this.ifzPagosGastos.update(entity);

			// envio de mensaje para la contabilidad
			this.ifzMessage.enviar(entity, getIdEmpresa());

			return entity;
		} catch (RuntimeException re) {
			log.error("error metodo actualizar", re);
			try {
				utx.rollback();
			} catch (Exception e) {
				log.error("error en rollback de transaccion en metodo actualizar", e);
			}
			throw re;
		}
	}

	@Override
	public Respuesta salvar(PagosGastos entity, boolean band) throws Exception {
		Respuesta reg = new Respuesta();
		long id;

		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			id = this.ifzPagosGastos.save(entity);

			entity.setId(id);
			reg.setObjeto(entity);
			reg.setResultado(0);
			reg.setReferencia(String.valueOf(entity.getId()));
		} catch (RuntimeException re) {
			log.error("error en metodo salvar", re);
			reg.setResultado(-1);
			reg.setRespuesta("Error al salvar");
			throw re;
		}

		return reg;
	}

	@Override
	public void update(PagosGastos pojoEntity) throws Exception {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			this.ifzPagosGastos.update(pojoEntity);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public PagosGastos findById(Long id) {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findById(id);
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastos> findAll() throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findAll();
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findAll()", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<PagosGastos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<PagosGastos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<PagosGastos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<PagosGastos> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<PagosGastos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Respuesta salvaPojo(PagosGastos pojoEntity, boolean band) throws Exception {
		Respuesta reg = new Respuesta();
		List<Object> resultadoSQL = new ArrayList<Object>();
		String control = "";

		try {
			if (ctx == null)
				ctx = new InitialContext();
			utx.begin();

			PagosGastosExt entity = this.convertidor.PagosGastosToPagosGastosExt(pojoEntity);
			if ("C".equals(entity.getOperacion()) || "2".equals(entity.getOperacion())) {
				// si la operacion del gasto es con cheque se inserta aqui mismo el cheque
				// validando folio cheque
				// parametros enviados: select cancela_cheque(id cta bancaria,
				// folio de cheque, true SI cancela folio, Id usuario)
				resultadoSQL = this.ifzEditorSQL.findNativeQuery(
						"select cancela_cheques(" + entity.getIdCuentaOrigen().getInstitucionBancaria().getId() + ","
								+ entity.getNoCheque() + "," + band + "," + entity.getCreadoPor() + ")");

				if (!resultadoSQL.isEmpty()) {
					for (Object var : resultadoSQL) {
						if (!"BIEN".equals(var)) {
							reg.setResultado(-1);
							reg.setRespuesta(var.toString());
							utx.commit();
							return reg;
						}
					}
				}

				Cheques pojoCheque = new Cheques();
				pojoCheque.setBancoId(entity.getIdCuentaOrigen().getId());
				control = String.format("%06d", entity.getNoCheque()) + "-"
						+ entity.getIdCuentaOrigen().getSucursalBancaria().getNombre();
				pojoCheque.setControl(control.length() > 17 ? control.substring(0, 17) : control);
				pojoCheque.setMinistracion(0L);
				pojoCheque.setEstatus("E");
				pojoCheque.setTipo("T");
				pojoCheque.setFecha(entity.getFecha());
				pojoCheque.setImporte(BigDecimal.valueOf(entity.getMonto()));
				pojoCheque.setFolio(String.valueOf(entity.getNoCheque()));
				pojoCheque.setCreadoPor(Long.valueOf(entity.getCreadoPor()));
				pojoCheque.setFechaCreacion(Calendar.getInstance().getTime());
				pojoCheque.setModificadoPor(Long.valueOf(entity.getModificadoPor()));
				pojoCheque.setFechaModificacion(Calendar.getInstance().getTime());

				this.ifzCheques.setInfoSesion(this.infoSesion);
				this.ifzCheques.save(pojoCheque);
			}

			// entityManager.persist(entity);
			reg.setObjeto(entity);
			reg.setResultado(0);
			reg.setReferencia(String.valueOf(entity.getIdPagosGastosRef()));

			utx.commit();
		} catch (RuntimeException re) {
			log.error("error en metodo salvaPojo", re);
			try {
				utx.rollback();
			} catch (Exception e) {
				log.error("error en rollback de transaccion en metodo salvaPojo", e);
			}
			throw re;
		}

		return reg;
	}

	@Override
	public Respuesta cancelacion(PagosGastos entity, Date fech_modificacion)
			throws Exception {
		Respuesta reg = new Respuesta();

		try {
			// entity.setBeneficiario(entityExt.getIdBeneficiario().getNombre());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus("X");
			/*
			 * reg = cancelacionPojo(entityExt, fech_modificacion, usuario);
			 * if(reg.getResultado() == 0) reg = mensajeSaldoCtas((PagosGastos)
			 * reg.getObjeto(), empresa);
			 */

			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			this.ifzPagosGastos.update(entity);

			reg.setObjeto(entity);
			reg.setResultado(0);

			return reg;
		} catch (RuntimeException re) {
			log.error("error en metodo cancelacion", re);
			throw re;
		}
	}

	@Override
	public Respuesta mensajeSaldoCtas(PagosGastos pojoEntity) throws Exception {
		Respuesta reg = new Respuesta();

		try {
			PagosGastosExt entity = this.convertidor.PagosGastosToPagosGastosExt(pojoEntity);

			// envio mensaje para la contabilidad
			if (!"T".equals(entity.getTipo()))
				ifzMessage.enviar(pojoEntity, getIdEmpresa());

			reg.setObjeto(entity);
			reg.setResultado(0);
			reg.setReferencia(String.valueOf(entity.getId()));
			return reg;
		} catch (RuntimeException re) {
			log.error("error en metodo mensajeSaldoCtas", re);
			throw re;
		}
	}

	@Override
	public boolean findPersonaEnUso(Persona persona) {
		List<PagosGastos> res = null;
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			res = this.ifzPagosGastos.findPersonaEnUso(persona.getId());
		} catch (RuntimeException re) {
			throw re;
		}

		return res == null || res.isEmpty();
	}

	@Override
	public boolean existeTransferencia(CuentaBancaria ctaOrigen, String folioAutorizacion, Date fecha) {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.existeTransferencia(ctaOrigen.getId(), folioAutorizacion, fecha);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public Empresa findEmpresa(PagosGastos movCta) {
		try {
			this.ifzCtasBanco.setInfoSesion(this.infoSesion);
			CuentaBancaria cuentaOrigen = this.ifzCtasBanco.findById(movCta.getIdCuentaOrigen().shortValue());
			return this.ifzEmpresas.findById(cuentaOrigen.getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public PagosGastos findAllById(Long id) {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findAllById(id);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastos> findLikeBenefTipoPersona(String beneficiario, String tipoPer, String tipo, String estatus, int max, String sucursales) {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findLikeBenefTipoPersona(beneficiario, tipoPer, tipo, estatus, max, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastos> findLikeMovCtas(String propertyName, Object value, String v_tipo, String v_estatus1,
			int max, String sucursales) {
		try {
			String valores = "";
			if ("cuentaOrigen".equals(propertyName)) {
				propertyName = "idCuentaOrigen";

				this.ifzCtasBanco.setInfoSesion(this.infoSesion);
				List<CuentaBancaria> lista1 = this.ifzCtasBanco.findLikeColumnName("numeroDeCuenta", value.toString());
				if (!lista1.isEmpty()) {
					for (CuentaBancaria var : lista1) {
						if (!valores.isEmpty())
							valores += ",";
						valores += var.getId();
					}
				}

				List<CuentaBancaria> lista2 = this.ifzCtasBanco.findLikeClaveNombreCuenta(value.toString(), max, sucursales, getIdEmpresa());
				if (!lista2.isEmpty()) {
					for (CuentaBancaria var : lista2) {
						if (!valores.isEmpty())
							valores += ",";
						valores += var.getId();
					}
				}
			} else if ("tiposMovtoId".equals(propertyName)) {
				propertyName = "idTiposMovimiento";
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("valor", value.toString());
				params.put("descripcion", value.toString());
				List<ConValores> lista = this.ifzConValores.findByGrupoNombreLikeParams(sucursales, params);
				if (!lista.isEmpty()) {
					for (ConValores var : lista) {
						if (!valores.isEmpty())
							valores += ",";
						valores += var.getId();
					}
				}
			} else {
				valores = value.toString();
			}

			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findLikeMovCtas(propertyName, valores, v_tipo, v_estatus1, max, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastos> findTransferencias(String propertyName, Object value, String v_tipo, int max, String sucursales) {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findTransferencias(propertyName, value, v_tipo, max, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastos> findLikeCajaChica(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findLikeCajaChica(propertyName, value, v_tipo, v_estatus1, max, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastos> findProvisiones(String propertyName, Object value, String estatus, String estatus2, int max, String sucursales) {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findProvisiones(propertyName, value, estatus, estatus2, max, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastos> findTransPorDia(Date fecha) throws Exception {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findTransPorDia(fecha);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<PagosGastos> findLikeGtosPorComprobarPersona(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max) {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findLikeGtosPorComprobarPersona(value, suc, tipo, estatus, tipoPersona, fecha, traer, max);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public int findConsecutivoByBeneficiario(long idBeneficiario, String tipo, String estatus) {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.ifzPagosGastos.findConsecutivoByBeneficiario(idBeneficiario, tipo, estatus);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Respuesta enviarTransaccion(Long entityId) throws Exception {
		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			return this.enviarTransaccion(this.ifzPagosGastos.findById(entityId));
		} catch (Exception e) {
			log.error("error en metodo enviarTransaccion(entityId)", e);
			throw e;
		}
	}

	@Override
	public Respuesta enviarTransaccion(PagosGastos entity) throws Exception {
		Respuesta respuesta = new Respuesta();
		LinkedHashMap<Long, Double> listTransacciones = null;
		List<PagosGastosDetExt> listConceptos = null;
		MensajeTransaccion msg = null;
		double importe = 0;
		Long idMensaje = 0L;
		Long idTransaccion = 0L;
		Long idTransaccionInicial = 0L;
		Long idFormaPago = 0L;
		Long idMoneda = 0L;
		String descMoneda = "";
		String referencia = "";
		String tracking = "";
		
		try {
			if (entity == null) {
				log.info("No se disparo ninguna Transaccion, recibi Entity nulo");
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No puedo determinar una Transaccion valida. El registro indicado esta nulo o vacio");
				return respuesta;
			}
			
			// Determinamos transacciones
			log.info("Determinando lote de Transacciones. Recuperando conceptos ... ");
			this.ifzPagosGastosDet.setInfoSesion(this.infoSesion);
			listConceptos = this.ifzPagosGastosDet.findByPagosGastosExt(entity, 0);
			if (listConceptos == null || listConceptos.isEmpty()) {
				log.info("No puedo determinar una Transaccion valida porque el registro indicado no tiene detalles. Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus());
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No puedo determinar una Transaccion. El registro indicado no tiene conceptos");
				return respuesta;
			}
			
			idTransaccionInicial = 0L;
			listTransacciones = new LinkedHashMap<Long, Double>();
			for (PagosGastosDetExt concepto : listConceptos) {
				importe = concepto.getSubtotal() + (concepto.getTotalRetenciones() - concepto.getTotalRetenciones());
				
				if (idTransaccion <= 0L)
					idTransaccionInicial = idTransaccion;
				
				// REGISTRO DE GASTOS
				if ("P".equals(entity.getTipo()) && "C".equals(entity.getEstatus())) {
					// CONSUMO DE COMBUSTIBLE
					if (10003921L == concepto.getIdConcepto().getId()) { 
						tracking += "\nTransaccion 1019 (Registro de Gastos: CONSUMO DE COMBUSTIBLE). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1019L))
							listTransacciones.put(1019L, importe);
						else
							listTransacciones.put(1019L, listTransacciones.get(1019L) + importe);
						idTransaccion = 1019L;
						
					// COMISIONES DEL BANCO
					} else if (10003924L == concepto.getIdConcepto().getId()) { 
						tracking += "\nTransaccion 1022 (Registro de Gastos: COMISIONES DEL BANCO). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1022L))
							listTransacciones.put(1022L, importe);
						else
							listTransacciones.put(1022L, listTransacciones.get(1022L) + importe);
						idTransaccion = 1022L;
					
					// HONORARIOS
					} else if (10003973L == concepto.getIdConcepto().getId()) { 
						tracking += "\nTransaccion 1026 (Registro de Gastos: HONORARIOS). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1026L))
							listTransacciones.put(1026L, importe);
						else
							listTransacciones.put(1026L, listTransacciones.get(1026L) + importe);
						idTransaccion = 1026L;
					
					// ARRENDAMIENTO
					} else if (10003915L == concepto.getIdConcepto().getId()) { 
						tracking += "\nTransaccion 1028 (Registro de Gastos: ARRENDAMIENTO). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1028L))
							listTransacciones.put(1028L, importe);
						else
							listTransacciones.put(1028L, listTransacciones.get(1028L) + importe);
						idTransaccion = 1028L;
					
					// IMSS
					} else if (10003978L == concepto.getIdConcepto().getId()) { 
						tracking += "\nTransaccion 1029 (Registro de Gastos: IMSS). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1029L))
							listTransacciones.put(1029L, importe);
						else
							listTransacciones.put(1029L, listTransacciones.get(1029L) + importe);
						idTransaccion = 1029L;
						
					// INFONAVIT
					} else if (10003980L == concepto.getIdConcepto().getId()) { 
						tracking += "\nTransaccion 1029 (Registro de Gastos: INFONAVIT). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1029L))
							listTransacciones.put(1029L, importe);
						else
							listTransacciones.put(1029L, listTransacciones.get(1029L) + importe);
						idTransaccion = 1029L;
					
					// ACTIVO FIJO
					} else if (concepto.getIdConcepto().getAtributo1() != null && "10000791".equals(concepto.getIdConcepto().getAtributo1().trim())) { 
						tracking += "\nTransaccion 1030 (Registro de Gastos: ACTIVO FIJO). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1030L))
							listTransacciones.put(1030L, importe);
						else
							listTransacciones.put(1030L, listTransacciones.get(1030L) + importe);
						idTransaccion = 1030L;
						
					// COMPRA DE MATERIAL - CREDITO
					} else if (concepto.getEsCredito()) {
						tracking += "\nTransaccion 1012 (Registro de Gastos: COMPRA DE MATERIAL - CREDITO). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1012L))
							listTransacciones.put(1012L, importe);
						else
							listTransacciones.put(1012L, listTransacciones.get(1012L) + importe);
						idTransaccion = 1012L;
						
					// COMPRA DE MATERIAL - CONTADO
					} else {
						tracking += "\nTransaccion 1010 (Registro de Gastos: COMPRA DE MATERIAL - CONTADO). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1010L))
							listTransacciones.put(1010L, importe);
						else
							listTransacciones.put(1010L, listTransacciones.get(1010L) + importe);
						idTransaccion = 1010L;
					}
					
				// CAJA CHICA
				} else if ("C".equals(entity.getTipo()) && "G".equals(entity.getEstatus())) {
					tracking += "\nTransaccion 1016 (Caja Chica). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
					if (! listTransacciones.containsKey(1016L))
						listTransacciones.put(1016L, importe);
					else
						listTransacciones.put(1016L, listTransacciones.get(1016L) + importe);
					idTransaccion = 1016L;
					
				// GASTOS A COMPROBAR
				} else if ("G".equals(entity.getTipo()) && "G".equals(entity.getEstatus())) {
					tracking += "\nTransaccion 1016 (Gastos a Comprobar). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
					if (! listTransacciones.containsKey(1016L))
						listTransacciones.put(1016L, importe);
					else
						listTransacciones.put(1016L, listTransacciones.get(1016L) + importe);
					idTransaccion = 1016L;
					
				// DESCONOCIDO
				} else {
					log.info("No se disparo ninguna Transaccion, Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus());
					respuesta.getErrores().setCodigoError(3L);
					respuesta.getErrores().setDescError("No puedo determinar una Transaccion valida. Tipo de registro no identificado");
					return respuesta;
				}
				
				if (idTransaccionInicial > 0L && idTransaccionInicial.longValue() != idTransaccion.longValue()) {
					log.info("No se disparo ninguna Transaccion. Multiples Transacciones posibles para el registro indicado. Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus());
					respuesta.getErrores().setCodigoError(4L);
					respuesta.getErrores().setDescError("Multiples Transacciones posibles para el registro indicado.");
					return respuesta;
				}
			}
			log.info("Transacciones encontradas: " + tracking);
			
			// Obtenemos el id moneda
			this.ifzCtasBanco.setInfoSesion(this.infoSesion);
			CuentaBancaria aux = this.ifzCtasBanco.findById(entity.getIdCuentaOrigen());
			if (aux != null) {
				idMoneda = aux.getMoneda().getId();
				descMoneda = aux.getMoneda().getNombre();
			}

			// Forma de Pago y Referencia
			referencia = "";
			if ("E".equals(entity.getOperacion())) {
				idFormaPago = 10000031L; // EFECTIVO 
				referencia = "";
			} else if ("C".equals(entity.getOperacion())) {
				idFormaPago = 10000032L; // CHEQUE 
				referencia = (entity.getNoCheque() != null && entity.getNoCheque() > 0) ? entity.getNoCheque().toString() : "";
			} else if ("T".equals(entity.getOperacion())) {
				idFormaPago = 10000033L; // TRANSFERENCIA
				referencia = (entity.getFolioAutorizacion() != null && ! "".equals(entity.getFolioAutorizacion())) ? entity.getFolioAutorizacion() : "";
			} else if ("D".equals(entity.getOperacion())) {
				idFormaPago = 10000035L; // DEPOSITO
				referencia = (entity.getNoCheque() != null && entity.getNoCheque() > 0) ? entity.getNoCheque().toString() : "";
			} else {
				idFormaPago = 10000031L; // EFECTIVO 
				referencia = "";
			}
			
			// Generamos el mensajes necesarios
			for (Entry<Long, Double> item : listTransacciones.entrySet()) {
				idTransaccion = item.getKey();
				importe = item.getValue();
				
				msg = new MensajeTransaccion();
				msg.setIdTransaccion(idTransaccion);
				msg.setIdOperacion(entity.getId());
				msg.setFechaRegistro(entity.getFecha());
				msg.setIdEmpresa(getIdEmpresa());
				msg.setIdSucursal(entity.getIdSucursal());
				msg.setIdMoneda(idMoneda);
				msg.setDescripcionMoneda(descMoneda);
				msg.setImporte(new BigDecimal(importe));
				msg.setIdPersonaReferencia(entity.getIdBeneficiario());
				msg.setNombrePersonaReferencia(entity.getBeneficiario());
				msg.setTipoPersona(entity.getTipoBeneficiario());
				msg.setReferencia("");
				msg.setIdFormaPago(idFormaPago);
				msg.setReferenciaFormaPago(referencia);
				msg.setIdUsuarioCreacionRegistro(entity.getCreadoPor());
				msg.setCreadoPor(entity.getCreadoPor());
				msg.setFechaCreacion(entity.getFechaCreacion());
				msg.setAnuladoPor(0L);
				msg.setFechaAnulacion(null);
				msg.setEstatus(0);
				
				// Registramos el mensaje
				this.ifzMsgTransaccion.setInfoSesion(this.infoSesion);
				idMensaje = this.ifzMsgTransaccion.save(msg);
				log.info("Mensaje " + idMensaje + " generado para la Transaccion " + idTransaccion);
			}
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorPagar.PagosGastosFac.enviarTransaccion(PagosGastosEntity, idEmpresa)", e);
			respuesta.getBody().addValor("exception", e);
		} 
		
		return respuesta;
	}
	
	@Override
	public void cancelar(Long idPagosGastos) throws Exception {
		try {
			this.cancelar(this.ifzPagosGastos.findById(idPagosGastos));
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public PagosGastos cancelar(PagosGastos entity) throws Exception {
		List<PagosGastosDet> detalles = null;
		
		try {
			if (this.infoSesion != null && this.infoSesion.getAcceso() != null && this.infoSesion.getAcceso().getUsuario() != null && this.infoSesion.getAcceso().getUsuario().getId() > 0L)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			else
				entity.setModificadoPor(entity.getModificadoPor() * -1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setEstatus("X");
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			this.ifzPagosGastos.update(entity);

			this.ifzPagosGastosDet.setInfoSesion(this.infoSesion);
			detalles = this.ifzPagosGastosDet.findByPagosGastos(entity, 0);
			if (detalles != null && ! detalles.isEmpty()) {
				for (PagosGastosDet det : detalles) {
					if (det.getIdXml() != null && det.getIdXml() > 0L) {
						this.ifzFacturas.setInfoSesion(this.infoSesion); //this.ifzFacturas.setInfoSesion(convertInfoSesion());
						this.ifzFacturas.cancelar(det.getIdXml());
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return entity;
	}

	@Override
	public PagosGastos convertir(PagosGastosExt entity) throws Exception {
		return this.convertidor.PagosGastosExtToPagosGastos(entity);
	}

	@Override
	public PagosGastosExt convertir(PagosGastos entity) throws Exception {
		return this.convertidor.PagosGastosToPagosGastosExt(entity);
	}

	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------

	@Override
	public PagosGastosExt actualizar(PagosGastosExt entityExt, String estatus, Date fech_modificacion) throws Exception {
		try {
			entityExt.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entityExt.setFechaModificacion(fech_modificacion);
			entityExt.setEstatus(estatus);

			this.update(this.convertidor.PagosGastosExtToPagosGastos(entityExt));
			return entityExt;
		} catch (RuntimeException re) {
			log.error("error metodo actualizar", re);
			try {
				utx.rollback();
			} catch (Exception e) {
				log.error("error en rollback de transaccion en metodo actualizar", e);
			}
			throw re;
		}
	}

	@Override
	public Respuesta salvar(PagosGastosExt entityExt, boolean band) throws Exception {
		Respuesta reg = new Respuesta();
		PagosGastos entity = null;

		try {
			entity = this.convertidor.PagosGastosExtToPagosGastos(entityExt);
			entity.setBeneficiario(entityExt.getBeneficiario());
			reg = this.salvar(entity, band);

			entity.setId(Long.valueOf(reg.getReferencia()));
			entityExt.setId(Long.valueOf(reg.getReferencia()));
			reg.setObjeto(entityExt);
			reg.setResultado(0);
			reg.setReferencia(String.valueOf(entityExt.getId()));
		} catch (RuntimeException re) {
			log.error("error en metodo salvar", re);
			reg.setResultado(-1);
			reg.setRespuesta("Error al salvar");
			throw re;
		}

		return reg;
	}

	@Override
	public void update(PagosGastosExt pojoEntity) throws Exception {
		try {
			this.update(this.convertidor.PagosGastosExtToPagosGastos(pojoEntity));
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public PagosGastosExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.PagosGastosToPagosGastosExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<PagosGastosExt> findExtAll() throws Exception {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.findAll();
			if (lista != null) {
				for (PagosGastos var : lista) {
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findExtAll()", e);
			throw e;
		}

		return listaExt;
	}

	@Override
	public List<PagosGastosExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.findByProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}

		return listaExt;
	}

	@Override
	public List<PagosGastosExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}

		return listaExt;
	}

	@Override
	public List<PagosGastosExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.findInProperty(columnName, values, limite);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}

		return listaExt;
	}

	@Override
	public List<PagosGastosExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.findByProperties(params, limite);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findExtByProperties(params, limite)", e);
			throw e;
		}

		return listaExt;
	}

	@Override
	public List<PagosGastosExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.findLikeProperties(params, limite);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}

		return listaExt;
	}

	@Override
	public Respuesta cancelacion(PagosGastosExt entityExt, Date fech_modificacion) throws Exception {
		Respuesta reg = new Respuesta();

		try {
			PagosGastos entity = this.convertidor.PagosGastosExtToPagosGastos(entityExt);
			if (entityExt.getIdBeneficiario() != null)
				entity.setBeneficiario(entityExt.getIdBeneficiario().getNombre());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus("X");
			/*
			 * reg = cancelacionPojo(entityExt, fech_modificacion, usuario);
			 * if(reg.getResultado() == 0) reg = mensajeSaldoCtas((PagosGastos)
			 * reg.getObjeto(), empresa);
			 */

			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			this.ifzPagosGastos.update(entity);

			reg.setObjeto(entityExt);
			reg.setResultado(0);

			return reg;
		} catch (RuntimeException re) {
			log.error("error en metodo cancelacion", re);
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Respuesta cancelacionPojo(PagosGastosExt entity, Date fech_modificacion) throws Exception {
		List<Object> resultadoSQL = new ArrayList<Object>();

		try {
			Respuesta reg = new Respuesta();
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus("X");

			if ("C".equals(entity.getOperacion()) || "2".equals(entity.getOperacion())) {
				// si la operacion del gasto es con cheque se CANCELA 

				// revisando el estatus del cheque...solo los E se pueden
				// cancelar
				resultadoSQL = ifzEditorSQL.findNativeQuery(
						"SELECT estatus from cheques where folio=" + entity.getNoCheque() + " and banco_id="
								+ entity.getIdCuentaOrigen().getInstitucionBancaria().getId() + " and tipo='T' ");

				if (!resultadoSQL.isEmpty()) {
					if ("E".equals(resultadoSQL.get(0).toString())) {

						Cheques pojoCheque = new Cheques();
						this.ifzCheques.setInfoSesion(this.infoSesion);
						pojoCheque = ifzCheques.findChequeCompleto(String.valueOf(entity.getNoCheque()),
								(short) entity.getIdCuentaOrigen().getSucursalBancaria().getId(), "T", "E");
						pojoCheque.setEstatus("C");
						pojoCheque.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
						pojoCheque.setFechaModificacion(fech_modificacion);
						pojoCheque = ifzCheques.update(pojoCheque);
					} else {
						if ("P".equals(resultadoSQL.get(0).toString())) {
							reg.setRespuesta("No es posible cancelar este gasto porque el cheque ha sido cobrado!");
							reg.setResultado(-1);
							// utx.commit();
							return reg;
						} else {
							if ("C".equals(resultadoSQL.get(0).toString())) {
								reg.setRespuesta(
										"Error: El estatus del cheque del gasto esta CANCELADO y el gasto se encuentra VIGENTE.");
								reg.setResultado(-1);
								// utx.commit();
								return reg;
							}
						}

					}

				}
			}

			reg.setObjeto(entity);
			reg.setResultado(0);
			return reg;

		} catch (RuntimeException re) {
			log.error("error en metodo cancelacion", re);
			throw re;
		}
	}

	@Override
	public Respuesta mensajeSaldoCtas(PagosGastosExt pojoEntityExt) throws Exception {
		Respuesta reg = new Respuesta();

		try {
			return reg;
		} catch (RuntimeException re) {
			log.error("error en metodo mensajeSaldoCtas", re);
			throw re;
		}
	}

	@Override
	public long save(PagosGastosExt object) throws Exception {
		try {
			return this.save(this.convertidor.PagosGastosExtToPagosGastos(object));
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosExt> findLikeBenefTipoPersonaExt(String beneficiario, String tipoPer, String tipo, String estatus, int max, String sucursales) {
		List<PagosGastosExt> listPagosGastosExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> listPagosGastos = this.findLikeBenefTipoPersona(beneficiario, tipoPer, tipo, estatus, max, sucursales);
			if (listPagosGastos != null && ! listPagosGastos.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : listPagosGastos)
					listPagosGastosExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listPagosGastosExt.size() + " registros");
			}
		} catch (RuntimeException re) {
			throw re;
		}

		return listPagosGastosExt;
	}

	@Override
	public List<PagosGastosExt> findLikeGtosComprobar(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
		List<PagosGastosExt> listGtosComprobarExt = new ArrayList<PagosGastosExt>();

		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			List<PagosGastos> listGtosComprobar = this.ifzPagosGastos.findLikeGtosComprobar(propertyName, value, v_tipo, v_estatus1, max, sucursales);
			if (listGtosComprobar != null && ! listGtosComprobar.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : listGtosComprobar)
					listGtosComprobarExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listGtosComprobarExt.size() + " registros");
			}
		} catch (RuntimeException re) {
			throw re;
		}

		return listGtosComprobarExt;
	}

	@Override
	public List<PagosGastosExt> findExtLikeMovCtas(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
		List<PagosGastosExt> listMovsExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.findLikeMovCtas(propertyName, value, v_tipo, v_estatus1, max, sucursales);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listMovsExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listMovsExt.size() + " registros");
			}
		} catch (RuntimeException re) {
			throw re;
		}

		return listMovsExt;
	}

	@Override
	public List<PagosGastosExt> findTransferenciasExt(String propertyName, Object value, String v_tipo, int max, String sucursales) {
		List<PagosGastosExt> listTransferenciasExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> listTransferencias = this.findTransferencias(propertyName, value, v_tipo, max, sucursales);
			if (listTransferencias != null && ! listTransferencias.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : listTransferencias)
					listTransferenciasExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listTransferenciasExt.size() + " registros");
			}
		} catch (RuntimeException re) {
			throw re;
		}

		return listTransferenciasExt;
	}

	@Override
	public List<PagosGastosExt> findLikeCajaChicaExt(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.findLikeCajaChica(propertyName, value, v_tipo, v_estatus1, max, sucursales);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (RuntimeException re) {
			throw re;
		}

		return listaExt;
	}

	@Override
	public List<PagosGastosExt> findLikeGtosPorComprobarPersonaExt(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max) {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.findLikeGtosPorComprobarPersona(value, suc, tipo, estatus, tipoPersona, fecha, traer, max);
			if (lista != null && ! lista.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : lista)
					listaExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listaExt.size() + " registros");
			}
		} catch (RuntimeException re) {
			throw re;
		}

		return listaExt;
	}

	@Override
	public List<PersonaExt> findLikePersonasConGastos(Object value, ConGrupoValores valGpo, String tipoPersona, String tipo, String estatus, int max, Date fecha, String sucursales) {
		List<PersonaExt> listPersonasConGastos = new ArrayList<PersonaExt>();

		try {this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			List<PagosGastos> listPagosGastos = this.ifzPagosGastos.findLikePersonasConGastos(value.toString(), tipoPersona, tipo, estatus, max, null, sucursales);
			if (listPagosGastos != null && ! listPagosGastos.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : listPagosGastos)
					listPersonasConGastos.add(this.convertidor.PagosGastosToPagosGastosExt(var).getIdBeneficiario());
				log.info("Se extendieron " + listPersonasConGastos.size() + " registros");
			}
		} catch (RuntimeException re) {
			throw re;
		}

		return listPersonasConGastos;
	}

	@Override
	public SucursalExt findSucursalById(long id) {
		SucursalExt pojoSucursalExt = new SucursalExt();

		try {
			this.ifzSucursal.setInfoSesion(this.infoSesion);
			Sucursal pojoSucursal = this.ifzSucursal.findById(id);
			if (pojoSucursal != null) {
				pojoSucursalExt = this.convertidor.SucursalToSucursalExt(pojoSucursal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoSucursalExt;
	}

	@Override
	public PersonaExt findPersonaById(long id) {
		PersonaExt pojoPersonaExt = new PersonaExt();

		try {
			this.ifzPersona.setInfoSesion(this.infoSesion);
			Persona pojoPersona = this.ifzPersona.findById(id);

			if (pojoPersona != null) {
				pojoPersonaExt = this.convertidor.PersonaToPersonaExt(pojoPersona);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoPersonaExt;
	}

	@Override
	public PersonaExt findPersonaById(long id, String tipoPersona) {
		PersonaExt pojoPersonaExt = new PersonaExt();

		try {
			Persona pojoPersona = new Persona();

			if ("P".equals(tipoPersona)) {
				this.ifzPersona.setInfoSesion(this.infoSesion);
				pojoPersona = this.ifzPersona.findById(id);
			} else {
				this.ifzNegocio.setInfoSesion(this.infoSesion);
				Negocio pojoNegocio = this.ifzNegocio.findById(id);
				pojoPersona.setId(pojoNegocio.getId());
				pojoPersona.setNombre(pojoNegocio.getNombre());
			}

			if (pojoPersona != null) {
				pojoPersonaExt = this.convertidor.PersonaToPersonaExt(pojoPersona);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoPersonaExt;
	}

	@Override
	public CtasBancoExt findCuentaBancariaById(long id) {
		CtasBancoExt pojoCtasBancoExt = new CtasBancoExt();

		try {
			this.ifzCtasBanco.setInfoSesion(this.infoSesion);
			CuentaBancaria pojoCtasBanco = this.ifzCtasBanco.findById(id);
			if (pojoCtasBanco != null) {
				pojoCtasBancoExt = this.convertidor.CtasBancoToCtasBancoExt(pojoCtasBanco);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoCtasBancoExt;
	}

	@Override
	public List<PagosGastosExt> findLikeCuentaBancaria(String value, String v_tipo, String v_estatus1, int max) {
		List<PagosGastosExt> listPagosGastosExt = new ArrayList<PagosGastosExt>();
		List<PagosGastos> listPagosGastos = new ArrayList<PagosGastos>();
		String ctasBancarias = "";

		try {
			this.ifzCtasBanco.setInfoSesion(this.infoSesion);
			List<CuentaBancaria> listCtasBanco = this.ifzCtasBanco.findByProperty("numeroDeCuenta", value, "");
			for (CuentaBancaria var : listCtasBanco) {
				if (!ctasBancarias.equals(""))
					ctasBancarias += ",";
				ctasBancarias += String.valueOf(var.getId());
			}

			if (! "".equals(ctasBancarias)) {
				this.ifzPagosGastos.setEmpresa(getIdEmpresa());
				listPagosGastos = this.ifzPagosGastos.findCtasBancoById(ctasBancarias, v_tipo, v_estatus1, max);
				if (listPagosGastos != null && ! listPagosGastos.isEmpty()) {
					log.info("Extendiendo PagosGastos");
					for (PagosGastos var : listPagosGastos)
						listPagosGastosExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
					log.info("Se extendieron " + listPagosGastosExt.size() + " registros");
				}
			}
		} catch (Exception ex) {
			throw ex;
		}

		return listPagosGastosExt;
	}

	@Override
	public List<PagosGastosExt> buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda) {
		List<PagosGastosExt> listPagosGastosExt = new ArrayList<PagosGastosExt>();

		try {
			this.ifzPagosGastos.setEmpresa(getIdEmpresa());
			List<PagosGastos> listPagosGastos = this.ifzPagosGastos.buscarMovimientosCuentas(tipoBusqueda, valorBusqueda);
			if (listPagosGastos != null && ! listPagosGastos.isEmpty()) {
				log.info("Extendiendo PagosGastos");
				for (PagosGastos var : listPagosGastos)
					listPagosGastosExt.add(this.convertidor.PagosGastosToPagosGastosExt(var));
				log.info("Se extendieron " + listPagosGastosExt.size() + " registros");
			}
		} catch (Exception ex) {
			throw ex;
		}

		return listPagosGastosExt;
	}

	@Override
	public Respuesta enviarTransaccion(PagosGastosExt entityExt) throws Exception {
		try {
			return this.enviarTransaccion(this.convertir(entityExt));
		} catch (Exception e) {
			log.error("error en metodo enviarTransaccion(entityExt)", e);
			throw e;
		}
	}

	@Override
	public PagosGastosExt cancelar(PagosGastosExt entity) throws Exception {
		try {
			return this.convertidor.PagosGastosToPagosGastosExt(this.cancelar(this.convertidor.PagosGastosExtToPagosGastos(entity)));
		} catch (Exception e) {
			throw e;
		}
	}

	// -------------------------------------------------------------------------------------------
	// PRIVADOS
	// -------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}
