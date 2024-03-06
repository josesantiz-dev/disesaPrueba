package net.giro.cxc.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.log4j.Logger;

import net.giro.cxc.beans.FacturasRelacionadas;
import net.giro.cxc.dao.FacturasRelacionadasDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class FacturasRelacionadasFac implements FacturasRelacionadasRem {
	private static Logger log = Logger.getLogger(FacturasRelacionadasFac.class);
	private InfoSesion infoSesion;
	private long idUsuario;
	private long idEmpresa;
	private long codigoEmpresa;
	// -------------------------------------------------------------------
	private FacturasRelacionadasDAO ifzFacturasRelacionadas;
	private ConvertExt convertidor;

	public FacturasRelacionadasFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;

		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzFacturasRelacionadas = (FacturasRelacionadasDAO) ctx.lookup(
					"ejb:/Model_CuentasPorCobrar//FacturasRelacionadasImp!net.giro.cxc.dao.FacturasRelacionadasDAO");
			this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturasRelacionadasDAO", e);
		}
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
		if (this.infoSesion != null) {
			this.idUsuario = this.infoSesion.getAcceso().getUsuario().getId();
			this.idEmpresa = this.infoSesion.getEmpresa().getId();
			this.codigoEmpresa = this.infoSesion.getEmpresa().getCodigo();
		}
	}

	@Override
	public void setInfoSesion(long idUsuario, long idEmpresa, long codigoEmpresa) {
		this.idUsuario = idUsuario;
		this.idEmpresa = idEmpresa;
		this.codigoEmpresa = codigoEmpresa;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(FacturasRelacionadas entity) throws Exception {
		try {
			return this.ifzFacturasRelacionadas.save(entity, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Error en Logica_CuentasPorCobrar.FacturasRelacionadasFac.save(entity)", re);
			throw re;
		}
	}

	@Override
	public void deleteFacturasRelacionadas(long idFactura) throws Exception {
		try {
			this.ifzFacturasRelacionadas.deleteFacturasRelacionadas(idFactura);
		} catch (Exception re) {
			log.error("Error en Logica_CuentasPorCobrar.FacturasRelacionadasFac.delete(idFactura)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<FacturasRelacionadas> findFacturasById(long idFactura) throws Exception {
		try {
			return this.ifzFacturasRelacionadas.findFacturasById(idFactura);
		} catch (Exception re) {
			log.error("\nError en Logica_CuentasPorCobrar.FacturasRelacionadasFac.findById(idFactura)\n", re);
			throw re;
		}
	}
	// ------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------

	private Long getIdUsuario() {
		if (this.infoSesion != null)
			this.idUsuario = this.infoSesion.getAcceso().getUsuario().getId();
		this.idUsuario = (this.idUsuario > 0L ? this.idUsuario : 1L);
		return this.idUsuario;
	}

	private Long getIdEmpresa() {
		if (this.infoSesion != null)
			this.idEmpresa = this.infoSesion.getEmpresa().getId();
		this.idEmpresa = (this.idEmpresa > 0L ? this.idEmpresa : 1L);
		return this.idEmpresa;
	}

	private Long getCodigoEmpresa() {
		if (this.infoSesion != null)
			this.codigoEmpresa = this.infoSesion.getEmpresa().getCodigo();
		this.codigoEmpresa = (this.codigoEmpresa > 0L ? this.codigoEmpresa : 1L);
		return this.codigoEmpresa;
	}

	private String getEmpresa() {
		if (this.infoSesion != null)
			return this.infoSesion.getEmpresa().getEmpresa();
		return "";
	}

	private String valueToString(Object value) {
		return (value != null ? value.toString().trim() : "");
	}

	private double valueToDouble(Object value) {
		return ((!"".equals(valueToString(value))) ? Double.valueOf(valueToString(value)) : 0);
	}

	private int valueToInteger(Object value) {
		return ((!"".equals(valueToString(value))) ? Integer.valueOf(valueToString(value)) : 0);
	}


}
