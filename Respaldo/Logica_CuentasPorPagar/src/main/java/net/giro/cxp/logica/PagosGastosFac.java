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
import javax.rmi.PortableRemoteObject;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.logica.MensajeTransaccionRem;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.SucursalExt;
import net.giro.cxp.dao.PagosGastosDAO;
import net.giro.cxp.dao.PagosGastosDetDAO;
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
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.logica.ChequesRem;
import net.giro.tyg.logica.CtasBancoOperacionesRem;
import net.giro.tyg.logica.CtasBancoRem;

@Stateless
public class PagosGastosFac implements PagosGastosRem {
	private static Logger log = Logger.getLogger(PagosGastosFac.class);

	private InfoSesion infoSesion;
	private InitialContext ctx;
	private UserTransaction utx;
	private Object lookup;
	private PagosGastosDAO ifzPagosGastos;
	private PagosGastosDetDAO ifzPagosGastosDet;
	private SucursalesRem ifzSucursal;
	private PersonaRem ifzPersona;
	private NegociosRem ifzNegocio;
	private CtasBancoRem ifzCtasBanco;
	private EmpresasRem ifzEmpresas;
	private sendMessageRem ifzMessage;
	private CtasBancoOperacionesRem ifzBancosOperaciones;
	private ChequesRem ifzCheques;
	private NQueryRem ifzEditorSQL;
	private ConValoresDAO ifzConValores;
	private MensajeTransaccionRem ifzMsgTransaccion;
	private ConvertExt convertidor;
	private static String orderBy;
	private static Long estatus;

	// property constants
	/*public static final String CUENTA_ORIGEN = "cuentaOrigen";
	public static final String TIPO = "tipo";
	public static final String ESTATUS = "estatus";
	public static final String BENEFICIARIO = "beneficiario";
	public static final String NO_CHEQUE = "noCheque";
	public static final String MONTO = "monto";
	public static final String CREADO_POR = "creadoPor";
	public static final String MODIFICADO_POR = "modificadoPor";
	public static final String SUCURSAL = "sucursal";
	public static final String NO_BENEFICIARIO = "noBeneficiario";
	public static final String OPERACION = "operacion";
	public static final String TIPO_BENEF = "tipoBenef";
	public static final String CUENTA_DESTINO = "cuentaDestino";
	public static final String CUENTA_DESTINO_TERCEROS = "cuentaDestinoTerceros";
	public static final String PAGOS_GASTOS_ID_REF = "pagosGastosIdRef";
	public static final String TIPOS_MOVTO_ID = "tiposMovtoId";
	public static final String PAGO_MULTIPLE_ID = "pagoMultipleId";
	public static final String NOTA = "nota";
	public static final String CONCEPTO = "concepto";*/

	
	public PagosGastosFac() {
		try {
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);

			this.ifzPagosGastos = (PagosGastosDAO) this.ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosImp!net.giro.cxp.dao.PagosGastosDAO");
			this.ifzPagosGastosDet = (PagosGastosDetDAO) this.ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosDetImp!net.giro.cxp.dao.PagosGastosDetDAO");
			this.ifzSucursal = (SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzPersona = (PersonaRem) this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzNegocio = (NegociosRem) this.ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
			this.ifzCtasBanco = (CtasBancoRem) this.ctx.lookup("ejb:/Logica_TYG//CtasBancoFac!net.giro.tyg.logica.CtasBancoRem");
			this.ifzEmpresas = (EmpresasRem) this.ctx.lookup("ejb:/Logica_Publico//EmpresasFac!net.giro.plataforma.logica.EmpresasRem");
			this.ifzMessage = (sendMessageRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//sendMessageFac!net.giro.cxp.logica.sendMessageRem");
			this.ifzBancosOperaciones = (CtasBancoOperacionesRem) this.ctx.lookup("ejb:/Logica_TYG/CtasBancoOperacionesFac!net.giro.tyg.logica.CtasBancoOperacionesRem");
			this.ifzCheques = (ChequesRem) this.ctx.lookup("ejb:/Logica_TYG//ChequesFac!net.giro.tyg.logica.ChequesRem");
			this.ifzEditorSQL = (NQueryRem) this.ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
			this.ifzConValores = (ConValoresDAO) this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
			this.ifzMsgTransaccion = (MensajeTransaccionRem) this.ctx.lookup("ejb:/Logica_Contabilidad//MensajeTransaccionFac!net.giro.contabilidad.logica.MensajeTransaccionRem");
			
			this.ifzMsgTransaccion.setInfoSesion(this.infoSesion);
			this.ifzMsgTransaccion.showSystemOuts(false);

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
		/* this.convertidor.setMostrarSystemOut(value); */ }

	@Override
	public void orderBy(String orderBy) {
		PagosGastosFac.orderBy = orderBy;
	}

	@Override
	public void estatus(Long estatus) {
		PagosGastosFac.estatus = estatus;
	}

	public PagosGastosExt actualizar(PagosGastosExt entityExt, String estatus, Date fech_modificacion, Short usuario, Long empresa) throws Exception {
		try {
			entityExt.setModificadoPor(usuario);
			entityExt.setFechaModificacion(fech_modificacion);
			entityExt.setEstatus(estatus);

			PagosGastos entity = this.convertidor.PagosGastosExtToPagosGastos(entityExt);
			this.ifzPagosGastos.update(entity);
			// this.ifzMessage.enviar(entity, empresa != null ? empresa :
			// findEmpresa(entity).getId());
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

	public PagosGastos actualizar(PagosGastos entity, String estatus, Date fech_modificacion, Short usuario, Long empresa) throws Exception {
		try {
			entity.setModificadoPor(usuario);
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus(estatus);
			this.ifzPagosGastos.update(entity);

			// envio de mensaje para la contabilidad
			ifzMessage.enviar(entity, empresa != null ? empresa : findEmpresa(entity).getId());

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

	public Respuesta salvar(PagosGastos entity, boolean band, Long empresa) throws Exception {
		Respuesta reg = new Respuesta();
		long id;

		try {
			id = this.ifzPagosGastos.save(entity);

			entity.setId(id);
			reg.setObjeto(entity);
			reg.setResultado(0);
			reg.setReferencia(String.valueOf(entity.getId()));
			
			//enviarTransaccion(entity, empresa);
		} catch (RuntimeException re) {
			log.error("error en metodo salvar", re);
			reg.setResultado(-1);
			reg.setRespuesta("Error al salvar");
			throw re;
		}

		return reg;
	}

	public Respuesta salvar(PagosGastosExt entityExt, boolean band, Long empresa) throws Exception {
		Respuesta reg = new Respuesta();

		try {
			PagosGastos entity = this.convertidor.PagosGastosExtToPagosGastos(entityExt);
			entity.setBeneficiario(entityExt.getBeneficiario());
			reg = this.salvar(entity, band, empresa);

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

	public void update(PagosGastosExt pojoEntity) throws ExcepConstraint {
		try {
			this.ifzPagosGastos.update(this.convertidor.PagosGastosExtToPagosGastos(pojoEntity));
		} catch (Exception ex) {
			throw ex;
		}
	}

	public void update(PagosGastos pojoEntity) throws ExcepConstraint {
		try {
			this.ifzPagosGastos.update(pojoEntity);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public PagosGastos findById(Long id) {
		try {
			return this.ifzPagosGastos.findById(id);
		} catch (Exception e) {
			log.error("error en MODULO.PagosGastosFac.findById(id)", e);
			throw e;
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
	public List<PagosGastos> findAll() throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
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
	public List<PagosGastos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
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
	public List<PagosGastos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
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
	public List<PagosGastos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
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
	public List<PagosGastos> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
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
	public List<PagosGastos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzPagosGastos.estatus(estatus);
			this.ifzPagosGastos.orderBy(orderBy);
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
				if (ifzEditorSQL == null) {
					lookup = ctx.lookup("NQueryRem/remote");
					ifzEditorSQL = (NQueryRem) PortableRemoteObject.narrow(lookup, NQueryRem.class);
				}
				// validando folio cheque
				// parametros enviados: select cancela_cheque(id cta bancaria,
				// folio de cheque, true SI cancela folio, Id usuario)
				resultadoSQL = ifzEditorSQL.findNativeQuery(
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

				if (ifzCheques == null) {
					lookup = ctx.lookup("ChequesFac/remote");
					ifzCheques = (ChequesRem) PortableRemoteObject.narrow(lookup, ChequesRem.class);
				}

				// insertando el cheque
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
				ifzCheques.save(pojoCheque);
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

	public Respuesta cancelacion(PagosGastos entity, Date fech_modificacion, Short usuario, Long empresa)
			throws Exception {
		Respuesta reg = new Respuesta();

		try {
			// entity.setBeneficiario(entityExt.getIdBeneficiario().getNombre());
			entity.setModificadoPor(usuario);
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus("X");
			/*
			 * reg = cancelacionPojo(entityExt, fech_modificacion, usuario);
			 * if(reg.getResultado() == 0) reg = mensajeSaldoCtas((PagosGastos)
			 * reg.getObjeto(), empresa);
			 */

			this.ifzPagosGastos.update(entity);

			reg.setObjeto(entity);
			reg.setResultado(0);

			return reg;
		} catch (RuntimeException re) {
			log.error("error en metodo cancelacion", re);
			throw re;
		}
	}

	public Respuesta cancelacion(PagosGastosExt entityExt, Date fech_modificacion, Short usuario, Long empresa)
			throws Exception {
		Respuesta reg = new Respuesta();

		try {
			PagosGastos entity = this.convertidor.PagosGastosExtToPagosGastos(entityExt);
			if (entityExt.getIdBeneficiario() != null)
				entity.setBeneficiario(entityExt.getIdBeneficiario().getNombre());
			entity.setModificadoPor(usuario);
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus("X");
			/*
			 * reg = cancelacionPojo(entityExt, fech_modificacion, usuario);
			 * if(reg.getResultado() == 0) reg = mensajeSaldoCtas((PagosGastos)
			 * reg.getObjeto(), empresa);
			 */

			this.ifzPagosGastos.update(entity);

			reg.setObjeto(entityExt);
			reg.setResultado(0);

			return reg;
		} catch (RuntimeException re) {
			log.error("error en metodo cancelacion", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public Respuesta cancelacionPojo(PagosGastosExt entity, Date fech_modificacion, Short usuario) throws Exception {
		List<Object> resultadoSQL = new ArrayList<Object>();

		try {
			Respuesta reg = new Respuesta();
			// PagosGastosExt entity =
			// this.convertExt.PagosGastosToPagosGastosExt(pojoEntity);

			/*
			 * if(ctx == null) ctx = new InitialContext(); utx.begin();
			 * 
			 * if(ifzMessage == null){ lookup = ctx.lookup("sendMessage/local");
			 * ifzMessage =
			 * (sendMessageRem)PortableRemoteObject.narrow(lookup,sendMessageRem
			 * .class); }
			 * 
			 * if (ifzBancosOperaciones == null){ this.lookup =
			 * this.ctx.lookup("CtasBancoOperaciones/local");
			 * this.ifzBancosOperaciones =
			 * (CtasBancoOperacionesRem)PortableRemoteObject.narrow(this.lookup,
			 * CtasBancoOperacionesRem.class); }
			 */

			entity.setModificadoPor(usuario);
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus("X");

			if ("C".equals(entity.getOperacion()) || "2".equals(entity.getOperacion())) {
				// si la operacion del gasto es con cheque se CANCELA 
				/*
				 * if(ifzEditorSQL == null){ lookup =
				 * ctx.lookup("NQueryFacade/local"); ifzEditorSQL = (NQueryRem)
				 * PortableRemoteObject.narrow(lookup, NQueryRem.class); }
				 */

				// revisando el estatus del cheque...solo los E se pueden
				// cancelar
				resultadoSQL = ifzEditorSQL.findNativeQuery(
						"SELECT estatus from cheques where folio=" + entity.getNoCheque() + " and banco_id="
								+ entity.getIdCuentaOrigen().getInstitucionBancaria().getId() + " and tipo='T' ");

				if (!resultadoSQL.isEmpty()) {
					if ("E".equals(resultadoSQL.get(0).toString())) {
						/*
						 * if(ifzCheques == null){ lookup =
						 * ctx.lookup("ChequesFacade/local"); ifzCheques =
						 * (ChequesRem) PortableRemoteObject.narrow(lookup,
						 * ChequesRem.class); }
						 */

						Cheques pojoCheque = new Cheques();
						pojoCheque = ifzCheques.findChequeCompleto(String.valueOf(entity.getNoCheque()),
								(short) entity.getIdCuentaOrigen().getSucursalBancaria().getId(), "T", "E");
						pojoCheque.setEstatus("C");
						pojoCheque.setModificadoPor(Long.valueOf(usuario));
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

			// entityManager.merge(entity);
			reg.setObjeto(entity);
			reg.setResultado(0);
			// utx.commit();
			return reg;

		} catch (RuntimeException re) {
			log.error("error en metodo cancelacion", re);
			/*
			 * try { utx.rollback(); } catch (Exception e) { log.error(
			 * "error en rollback de transaccion en metodo cancelacion", e); }
			 */
			throw re;
		}
	}

	public Respuesta mensajeSaldoCtas(PagosGastos pojoEntity, Long empId) throws Exception {
		Respuesta reg = new Respuesta();

		try {
			if (ctx == null)
				ctx = new InitialContext();
			utx.begin();
			if (ifzMessage == null) {
				lookup = ctx.lookup("sendMessage/remote");
				ifzMessage = (sendMessageRem) PortableRemoteObject.narrow(lookup, sendMessageRem.class);
			}

			if (ifzBancosOperaciones == null) {
				this.lookup = this.ctx.lookup("CtasBancoOperaciones/remote");
				this.ifzBancosOperaciones = (CtasBancoOperacionesRem) PortableRemoteObject.narrow(this.lookup,
						CtasBancoOperacionesRem.class);
			}

			PagosGastosExt entity = this.convertidor.PagosGastosToPagosGastosExt(pojoEntity);

			// envio mensaje para la contabilidad
			if (!"T".equals(entity.getTipo()))
				ifzMessage.enviar(pojoEntity,
						empId != null ? empId : (Long) entity.getIdCuentaOrigen().getEmpresa().getId());

			reg.setObjeto(entity);
			reg.setResultado(0);
			reg.setReferencia(String.valueOf(entity.getId()));
			utx.commit();
			return reg;
		} catch (RuntimeException re) {
			log.error("error en metodo mensajeSaldoCtas", re);
			try {
				utx.rollback();
			} catch (Exception e) {
				log.error("error en rollback de transaccion en metodo mensajeSaldoCtas", e);
			}
			throw re;
		}
	}

	public Respuesta mensajeSaldoCtas(PagosGastosExt pojoEntityExt, Long empId) throws Exception {
		Respuesta reg = new Respuesta();

		try {
			/*
			 * if(ctx == null) ctx = new InitialContext(); utx.begin();
			 * if(ifzMessage == null){ lookup =
			 * ctx.lookup("sendMessage/remote"); ifzMessage =
			 * (sendMessageRem)PortableRemoteObject.narrow(lookup,
			 * sendMessageRem.class); }
			 * 
			 * if (ifzBancosOperaciones == null){ this.lookup =
			 * this.ctx.lookup("CtasBancoOperaciones/remote");
			 * this.ifzBancosOperaciones =
			 * (CtasBancoOperacionesRem)PortableRemoteObject.narrow(this.lookup,
			 * CtasBancoOperacionesRem.class); }
			 * 
			 * PagosGastosExt entity =
			 * this.convertExt.PagosGastosToPagosGastosExt(pojoEntity);
			 * 
			 * //envio mensaje para la contabilidad
			 * if(!"T".equals(entity.getTipo())) ifzMessage.enviar(pojoEntity,
			 * empId != null ? empId : (Long)
			 * entity.getIdCuentaOrigen().getEmpresa().getId());
			 * 
			 * reg.setObjeto(entity); reg.setResultado(0);
			 * reg.setReferencia(String.valueOf(entity.getId())); utx.commit();
			 */
			return reg;
		} catch (RuntimeException re) {
			log.error("error en metodo mensajeSaldoCtas", re);
			try {
				utx.rollback();
			} catch (Exception e) {
				log.error("error en rollback de transaccion en metodo mensajeSaldoCtas", e);
			}
			throw re;
		}
	}

	public boolean findPersonaEnUso(Persona persona) {
		List<PagosGastos> res = null;
		try {
			res = this.ifzPagosGastos.findPersonaEnUso(persona.getId());
		} catch (RuntimeException re) {
			throw re;
		}

		return res == null || res.isEmpty();
	}

	public boolean existeTransferencia(CtasBanco ctaOrigen, String folioAutorizacion, Date fecha) {
		try {
			return this.ifzPagosGastos.existeTransferencia(ctaOrigen.getId(), folioAutorizacion, fecha);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Empresa findEmpresa(PagosGastos movCta) {
		try {
			CtasBanco cuentaOrigen = this.ifzCtasBanco.findById(movCta.getIdCuentaOrigen().shortValue());
			return this.ifzEmpresas.findById(cuentaOrigen.getEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public PagosGastos findAllById(Long id) {
		try {
			return this.ifzPagosGastos.findAllById(id);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public long save(PagosGastosExt object) throws ExcepConstraint {
		try {
			PagosGastos entity = this.convertidor.PagosGastosExtToPagosGastos(object);
			return this.ifzPagosGastos.save(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public long save(PagosGastos object) throws ExcepConstraint {
		try {
			return this.ifzPagosGastos.save(object);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<PagosGastos> findLikeBenefTipoPersona(String beneficiario, String tipoPer, String tipo, String estatus, int max, String sucursales) {
		try {
			return this.ifzPagosGastos.findLikeBenefTipoPersona(beneficiario, tipoPer, tipo, estatus, max, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<PagosGastosExt> findLikeBenefTipoPersonaExt(String beneficiario, String tipoPer, String tipo, String estatus, int max, String sucursales) {
		List<PagosGastosExt> listPagosGastosExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> listPagosGastos = this.ifzPagosGastos.findLikeBenefTipoPersona(beneficiario, tipoPer, tipo, estatus, max, sucursales);
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

	public List<PagosGastos> findLikeMovCtas(String propertyName, Object value, String v_tipo, String v_estatus1,
			int max, String sucursales) {
		try {
			String valores = "";
			if ("cuentaOrigen".equals(propertyName)) {
				propertyName = "idCuentaOrigen";

				List<CtasBanco> lista1 = this.ifzCtasBanco.findLikeColumnName("numeroDeCuenta", value.toString());
				if (!lista1.isEmpty()) {
					for (CtasBanco var : lista1) {
						if (!valores.isEmpty())
							valores += ",";
						valores += var.getId();
					}
				}

				List<CtasBanco> lista2 = this.ifzCtasBanco.findLikeClaveNombreCuenta(value.toString(), max, sucursales,
						0);// .findLikeColumnName("institucionBancaria.nombreCorto",
							// value.toString());
				if (!lista2.isEmpty()) {
					for (CtasBanco var : lista2) {
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
				List<ConValores> lista = this.ifzConValores.findByGrupoNombreLikeParams(sucursales, params);// (value.toString(),
																											// null,
																											// max);
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

			return this.ifzPagosGastos.findLikeMovCtas(propertyName, valores, v_tipo, v_estatus1, max, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
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

	public List<PagosGastos> findTransferencias(String propertyName, Object value, String v_tipo, int max, String sucursales) {
		try {
			return this.ifzPagosGastos.findTransferencias(propertyName, value, v_tipo, max, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<PagosGastosExt> findTransferenciasExt(String propertyName, Object value, String v_tipo, int max, String sucursales) {
		List<PagosGastosExt> listTransferenciasExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> listTransferencias = this.ifzPagosGastos.findTransferencias(propertyName, value, v_tipo, max, sucursales);
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

	public List<PagosGastos> findLikeCajaChica(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
		try {
			return this.ifzPagosGastos.findLikeCajaChica(propertyName, value, v_tipo, v_estatus1, max, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<PagosGastosExt> findLikeCajaChicaExt(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.ifzPagosGastos.findLikeCajaChica(propertyName, value, v_tipo, v_estatus1, max, sucursales);
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

	public List<PagosGastos> findProvisiones(String propertyName, Object value, String estatus, String estatus2, int max, String sucursales) {
		try {
			return this.ifzPagosGastos.findProvisiones(propertyName, value, estatus, estatus2, max, sucursales);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<PagosGastos> findTransPorDia(Date fecha) throws Exception {
		try {
			return this.ifzPagosGastos.findTransPorDia(fecha);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<PagosGastos> findLikeGtosPorComprobarPersona(Long value, Long suc, String tipo, String estatus,
			String tipoPersona, Date fecha, String traer, int max) {
		// List<PagosGastosExt> listPagosGastosExt = new
		// ArrayList<PagosGastosExt>();

		try {
			return this.ifzPagosGastos.findLikeGtosPorComprobarPersona(value, suc, tipo, estatus, tipoPersona, fecha,
					traer, max);
			/*
			 * List<PagosGastos> listPagosGastos =
			 * this.ifzPagosGastos.findLikeGtosPorComprobarPersona(value, suc,
			 * tipo, estatus, tipoPersona, fecha, traer, max); for(PagosGastos
			 * var : listPagosGastos){ listPagosGastosExt.add(this.convertExt.
			 * PagosGastosToPagosGastosExt(var)); }
			 */
		} catch (RuntimeException re) {
			throw re;
		}

		// return listPagosGastosExt;
	}

	public List<PagosGastosExt> findLikeGtosPorComprobarPersonaExt(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max) {
		List<PagosGastosExt> listaExt = new ArrayList<PagosGastosExt>();

		try {
			List<PagosGastos> lista = this.ifzPagosGastos.findLikeGtosPorComprobarPersona(value, suc, tipo, estatus, tipoPersona, fecha, traer, max);
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

	public List<PersonaExt> findLikePersonasConGastos(Object value, ConGrupoValores valGpo, String tipoPersona, String tipo, String estatus, int max, Date fecha, String sucursales) {
		List<PersonaExt> listPersonasConGastos = new ArrayList<PersonaExt>();

		try {
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

	public SucursalExt findSucursalById(long id) {
		SucursalExt pojoSucursalExt = new SucursalExt();

		try {
			Sucursal pojoSucursal = this.ifzSucursal.findById(id);
			if (pojoSucursal != null) {
				pojoSucursalExt = this.convertidor.SucursalToSucursalExt(pojoSucursal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoSucursalExt;
	}

	public PersonaExt findPersonaById(long id) {
		PersonaExt pojoPersonaExt = new PersonaExt();

		try {
			Persona pojoPersona = this.ifzPersona.findById(id);

			if (pojoPersona != null) {
				pojoPersonaExt = this.convertidor.PersonaToPersonaExt(pojoPersona);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoPersonaExt;
	}

	public PersonaExt findPersonaById(long id, String tipoPersona) {
		PersonaExt pojoPersonaExt = new PersonaExt();

		try {
			Persona pojoPersona = new Persona();

			if ("P".equals(tipoPersona)) {
				pojoPersona = this.ifzPersona.findById(id);
			} else {
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

	public CtasBancoExt findCuentaBancariaById(long id) {
		CtasBancoExt pojoCtasBancoExt = new CtasBancoExt();

		try {
			CtasBanco pojoCtasBanco = this.ifzCtasBanco.findById(id);
			if (pojoCtasBanco != null) {
				pojoCtasBancoExt = this.convertidor.CtasBancoToCtasBancoExt(pojoCtasBanco);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pojoCtasBancoExt;
	}

	public List<PagosGastosExt> findLikeCuentaBancaria(String value, String v_tipo, String v_estatus1, int max) {
		List<PagosGastosExt> listPagosGastosExt = new ArrayList<PagosGastosExt>();
		List<PagosGastos> listPagosGastos = new ArrayList<PagosGastos>();
		String ctasBancarias = "";

		try {
			List<CtasBanco> listCtasBanco = this.ifzCtasBanco.findByProperty("numeroDeCuenta", value, "");
			for (CtasBanco var : listCtasBanco) {
				if (!ctasBancarias.equals(""))
					ctasBancarias += ",";
				ctasBancarias += String.valueOf(var.getId());
			}

			if (! "".equals(ctasBancarias)) {
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

	public List<PagosGastosExt> buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda) {
		List<PagosGastosExt> listPagosGastosExt = new ArrayList<PagosGastosExt>();

		try {
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
	public int findConsecutivoByBeneficiario(long idBeneficiario, String tipo, String estatus) {
		try {
			return this.ifzPagosGastos.findConsecutivoByBeneficiario(idBeneficiario, tipo, estatus);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Respuesta enviarTransaccion(Long entityId, Long idEmpresa) throws Exception {
		try {
			return this.enviarTransaccion(this.ifzPagosGastos.findById(entityId), idEmpresa);
		} catch (Exception e) {
			log.error("error en metodo enviarTransaccion(entityId, idEmpresa)", e);
			throw e;
		}
	}

	@Override
	public Respuesta enviarTransaccion(PagosGastos entity, Long idEmpresa) throws Exception {
		Respuesta respuesta = new Respuesta();
		LinkedHashMap<Long, Double> listTransacciones = new LinkedHashMap<Long, Double>();
		MensajeTransaccion msg = null;
		double importe = 0;
		Long idMensaje = 0L;
		Long idTransaccion = 0L;
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
			
			if (idEmpresa == null || idEmpresa <= 0L)
				idEmpresa = 1L;
			
			// Determinamos transacciones
			log.info("Determinando lote de Transacciones ... ");
			List<PagosGastosDet> listDetalles = this.ifzPagosGastosDet.findByPagosGastos(entity, 0);
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.info("No puedo determinar una Transaccion valida porque el registro indicado no tiene detalles. Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus());
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No puedo determinar una Transaccion valida. El registro indicado no tiene detalles");
				return respuesta;
			}
			
			for (PagosGastosDet var : listDetalles) {
				importe = var.getSubtotal() + (var.getTotalRetenciones() - var.getTotalRetenciones());
				
				// Registro de Gastos
				if ("P".equals(entity.getTipo()) && "C".equals(entity.getEstatus())) {
					if (var.getEsCredito() == 1) {
						tracking += "\nTransaccion 1012 (Registro de Gastos). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1012L))
							listTransacciones.put(1012L, importe);
						else
							listTransacciones.put(1012L, listTransacciones.get(1012L) + importe);
						idTransaccion = 1012L;
					} else {
						tracking += "\nTransaccion 1010 (Registro de Gastos). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
						if (! listTransacciones.containsKey(1010L))
							listTransacciones.put(1010L, importe);
						else
							listTransacciones.put(1010L, listTransacciones.get(1010L) + importe);
						idTransaccion = 1010L;
					}
					
				// Caja Chica
				} else if ("C".equals(entity.getTipo()) && "G".equals(entity.getEstatus())) {
					tracking += "\nTransaccion 1016 (Caja Chica). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
					if (! listTransacciones.containsKey(1016L))
						listTransacciones.put(1016L, importe);
					else
						listTransacciones.put(1016L, listTransacciones.get(1016L) + importe);
					idTransaccion = 1016L;
					
				// Gastos a Comprobar
				} else if ("G".equals(entity.getTipo()) && "G".equals(entity.getEstatus())) {
					tracking += "\nTransaccion 1016 (Gastos a Comprobar). Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus();
					if (! listTransacciones.containsKey(1016L))
						listTransacciones.put(1016L, importe);
					else
						listTransacciones.put(1016L, listTransacciones.get(1016L) + importe);
					idTransaccion = 1016L;
					
				// Desconocido
				} else {
					log.info("No se disparo ninguna Transaccion, Recibi entity " + entity.getId() + " tipo " + entity.getTipo() + "-" + entity.getEstatus());
					respuesta.getErrores().setCodigoError(3L);
					respuesta.getErrores().setDescError("No puedo determinar una Transaccion valida. Tipo de registro no identificado");
					return respuesta;
				}
			}
			log.info("Transacciones encontradas: " + tracking);
			
			// Obtenemos el id moneda
			CtasBanco aux = this.ifzCtasBanco.findById(entity.getIdCuentaOrigen());
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
				msg.setIdSucursal(entity.getIdSucursal());
				msg.setCreadoPor(entity.getCreadoPor());
				msg.setFechaCreacion(entity.getFechaCreacion());
				msg.setAnuladoPor(0L);
				msg.setFechaAnulacion(null);
				msg.setEstatus(0);
				msg.setFechaRegistro(entity.getFecha());
				msg.setIdEmpresa(idEmpresa);
				
				// Registramos el mensaje
				idMensaje = this.ifzMsgTransaccion.save(msg);
				log.info("Mensaje " + idMensaje + " generado para la Transaccion " + idTransaccion);
			}
			
			/*// Determinamos Transaccion
			if ("P".equals(entity.getTipo()) && "C".equals(entity.getEstatus())) {
				tracking = "Transaccion 1010 disparada desde Registro de Gastos, recibi " + entity.getTipo() + "-" + entity.getEstatus();
				idTransaccion = 1010L; //1001 - registro de gastos PAGO A PROVEEDORES
			} else if ("C".equals(entity.getTipo()) && "G".equals(entity.getEstatus())) {
				tracking = "Transaccion 1016 disparada desde Caja Chica, recibi " + entity.getTipo() + "-" + entity.getEstatus();
				idTransaccion = 1016L; //1002 - caja chica 
			} else if ("G".equals(entity.getTipo()) && "G".equals(entity.getEstatus())) {
				tracking = "Transaccion 1016 disparada desde Gastos a Comprobar, recibi " + entity.getTipo() + "-" + entity.getEstatus();
				idTransaccion = 1016L; //1002 - gastos a comprobar
			} else {
				log.info("No se disparo ninguna Transaccion, recibi " + entity.getTipo() + "-" + entity.getEstatus());
				return false;
			}
			
			msg = new MensajeTransaccion();
			msg.setIdTransaccion(idTransaccion);
			msg.setIdOperacion(entity.getId());
			msg.setIdMoneda(idMoneda);
			msg.setDescripcionMoneda(descMoneda);
			msg.setImporte(new BigDecimal(entity.getMonto()));
			msg.setIdPersonaReferencia(entity.getIdBeneficiario());
			msg.setNombrePersonaReferencia(entity.getBeneficiario());
			msg.setTipoPersona(entity.getTipoBeneficiario());
			msg.setReferencia("");
			msg.setIdFormaPago(idFormaPago);
			msg.setReferenciaFormaPago(referencia);
			msg.setIdUsuarioCreacionRegistro(entity.getCreadoPor());
			msg.setIdSucursal(entity.getIdSucursal());
			msg.setCreadoPor(entity.getCreadoPor());
			msg.setFechaCreacion(entity.getFechaCreacion());
			msg.setAnuladoPor(0L);
			msg.setFechaAnulacion(null);
			msg.setEstatus(0);
			msg.setFechaRegistro(entity.getFecha());
			msg.setIdEmpresa(idEmpresa);
			
			// Registramos el mensaje
			idMensaje = this.ifzMsgTransaccion.save(msg);
			log.info("Tracking ID " + idMensaje + " : " + tracking);*/
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorPagar.PagosGastosFac.enviarTransaccion(PagosGastosEntity, idEmpresa)", e);
			respuesta.getBody().addValor("exception", e);
		} 
		
		return respuesta;
	}
	
	@Override
	public Respuesta enviarTransaccion(PagosGastosExt entityExt, Long idEmpresa) throws Exception {
		try {
			return this.enviarTransaccion(this.convertir(entityExt), idEmpresa);
		} catch (Exception e) {
			log.error("error en metodo enviarTransaccion(entityExt, idEmpresa)", e);
			throw e;
		}
	}
	
	@Override
	public PagosGastos convertir(PagosGastosExt entity) throws Exception {
		return this.convertidor.PagosGastosExtToPagosGastos(entity);
	}

	@Override
	public PagosGastosExt convertir(PagosGastos entity) throws Exception {
		return this.convertidor.PagosGastosToPagosGastosExt(entity);
	}
}
