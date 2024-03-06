package net.giro.adp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraCobranza;
import net.giro.adp.logica.ObraCobranzaRem;
import net.giro.adp.logica.ObraRem;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.navegador.LoginManager;

@ViewScoped
@ManagedBean(name="cobranzaAction")
public class CobranzaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CobranzaAction.class);
	private InitialContext ctx; 
	private LoginManager loginManager; 
	
	// Interfaces
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	private FacturaRem ifzFacturas;
	private FacturaDetalleRem ifzFacConceptos;
	private ObraCobranzaRem ifzCobranza;
	private List<ObraCobranza> listObraCobranza;
	private int numPaginaObraCobranza;
	private double totalCobranza;
	private boolean permiteGuardarCobranza;
	// Busqueda
	private List<SelectItem> busquedaOpciones;	
	private String busquedaCampo;
	private String busquedaValor;
	private int busquedaTipo;
	private int busquedaPaginas;
	// Variables de control
	private boolean incompleto;
	private int tipoMensaje;
	private String mensaje;
    private long usuarioId;
    //private String usuario;
    private double porcentajeIva;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public CobranzaAction() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzCobranza = (ObraCobranzaRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraCobranzaFac!net.giro.adp.logica.ObraCobranzaRem");
			this.ifzFacturas = (FacturaRem)	this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacConceptos = (FacturaDetalleRem)	this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			
			// Inicializaciones
			this.usuarioId = 0;
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = (long) this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.porcentajeIva = Double.valueOf(this.loginManager.getAutentificacion().getPerfil("VALOR_IVA"));
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet()) {
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			}
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			this.busquedaOpciones = new ArrayList<SelectItem>();
			this.busquedaOpciones.add(new SelectItem("nombre", "Obra"));
			this.busquedaOpciones.add(new SelectItem("nombreCliente", "Cliente"));
			this.busquedaOpciones.add(new SelectItem("nombreContrato", "Contrato"));
			this.busquedaOpciones.add(new SelectItem("id", "Clave"));
			this.busquedaCampo = busquedaOpciones.get(0).getDescription();
			this.busquedaValor = "";
			this.busquedaTipo = 0;
			this.busquedaPaginas = 1;
			this.numPaginaObraCobranza = 1;
		} catch (Exception e) {
			log.error("Error en constructor CobranzaAction", e);
			this.ctx = null;
		}
	}
	

	public void buscar() {
		try {
			control();
			if ("".equals(this.busquedaCampo))
				this.busquedaCampo = "nombre";

			this.busquedaPaginas = 1;
			if (this.listObras != null)
				this.listObras.clear();

			log.info("Cobranza ... buscando obras");
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.busquedaCampo, this.busquedaValor, this.busquedaTipo);
			if (this.listObras.isEmpty()) {
				control(2);
				log.info("ERROR 2: Busqueda sin resultados");
				return;
			}

			log.info("Cobranza ... se encontraron " + this.listObras.size() + " obras");
		} catch(Exception e) {
			control(true);
    		log.error("Error en GestionProyectos.CobranzaAction.buscar", e);
    	}
	}
	
	public void ver() {
		double monto = 0;
		BigDecimal montoAux = BigDecimal.ZERO;
		FacturaDetalle pojoDetalle;
		ObraCobranza itemCobranza;
		ObraCobranza itemAux;
		
		try {
			control();
			this.totalCobranza = 0;
			log.info("Cobranza detalles ... Preparando");
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(true, 1, "ERROR: Debe seleccionar una Obra");
				log.info("ERROR n: No selecciono ninguna Obra");
				return;
			}

			this.numPaginaObraCobranza = 1;
			if (this.listObraCobranza == null)
				this.listObraCobranza = new ArrayList<ObraCobranza>();
			this.listObraCobranza.clear();
			
			// recuperamos las facturas actuales de la obra
			log.info("Cobranza detalles ... Recuperando facturas de la Obra seleccionada: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
			List<Factura> listFacturas = this.ifzFacturas.findByProperty("idObra", this.pojoObra.getId(), 120);
			if (listFacturas == null || listFacturas.isEmpty()) {
				log.info("Cobranza detalles ... No se encontraron facturas para la obra seleccionada");
				return;
			}
			
			// Generamos el listado de cobranza
			log.info("Cobranza detalles ... Generando listado");
			for (Factura var : listFacturas) {
				if (this.isDebug) log.info("Cobranza detalles ... Factura " + var.getId() + " - " + var.getFolioFactura());
				if (var.getEstatus() == 0) {
					if (this.isDebug) log.info("Cobranza detalles ... Descartada, esta cancelada");
					continue;
				}
				
				// Recuperamos los detalles de la factura
				if (this.isDebug) log.info("Cobranza detalles ... Recuperamos detalles de factura");
				List<FacturaDetalle> lista = this.ifzFacConceptos.findByProperty("idFactura", var.getId());
				if (lista == null || lista.isEmpty()) {
					if (this.isDebug) log.info("Cobranza detalles ... Descartada, factura sin detalles");
					continue;
				}
				
				// Recuperamos el primer concepto de la factura
				pojoDetalle = lista.get(0);
				
				// Recuperamos la cobranza si corresponde
				if (this.isDebug) log.info("Cobranza detalles ... Coprobando cobranza previa");
				itemAux = this.ifzCobranza.comprobarConcepto(this.pojoObra.getId(), var.getId(), pojoDetalle.getId());
				if (itemAux != null) {
					if (this.isDebug) log.info("Cobranza detalles ... Encontrada, actualizo monto");
					itemCobranza = itemAux;
					itemCobranza.setMonto(itemCobranza.getMonto().setScale(2, BigDecimal.ROUND_HALF_EVEN));
				} else {
					if (this.isDebug) log.info("Cobranza detalles ... No encontrada, preparo registro");
					monto = var.getSubtotal().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue() + var.getImpuestos();
					montoAux = new BigDecimal(monto).setScale(2, BigDecimal.ROUND_HALF_EVEN);
					
					itemCobranza = new ObraCobranza();
					itemCobranza.setIdObra(this.pojoObra);
					itemCobranza.setNombreObra(this.pojoObra.getNombre());
					itemCobranza.setIdFactura(var.getId());
					itemCobranza.setFolio(var.getFolioFactura());
					itemCobranza.setFecha(var.getFechaEmision());
					itemCobranza.setIdConcepto(pojoDetalle.getId());
					itemCobranza.setConcepto(pojoDetalle.getDescripcionConcepto());
					itemCobranza.setMonto(montoAux); //itemCobranza.setMonto(new BigDecimal(monto));
				}
				
				// Agregamos al listado
				if (this.isDebug) log.info("Cobranza detalles ... Añado a listado");
				this.listObraCobranza.add(itemCobranza);
			}

			log.info("Cobranza detalles ... Totalizamos");
			totalizarCobranza();
			log.info("Cobranza detalles ... Terminado");
		} catch(Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en GestionProyectos.CobranzaAction.ver", e);
    	}
	}
	
	public void guardar() {
		try {
			control();

			log.info("Cobranza ... Preparando para guardar");
			if (this.listObraCobranza.size() <= 0) {
				control("Sin detalles para guardar");
				log.info("Cobranza sin detalles");
				return;
			}
			
			if (this.totalCobranza <= 0) {
				log.info("Cobranza ... Totalizamos");
				totalizarCobranza();
			}
			
			if (! this.permiteGuardarCobranza) {
				control(6);
				log.info("ERROR 6: Verifique, la columna Monto debe coincidir con la columna Total");
				return;
			}
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getNombre() == "") {
				control("Hubo un problema con la obra seleccionada.");
				log.info("Cobranza ... Obra NULL en pojo, ID o Nombre");
				return;
			}

			log.info("Cobranza ... Guardando");
			for (ObraCobranza var : this.listObraCobranza) {
				var.setIdObra(this.pojoObra);
				var.setNombreObra(this.pojoObra.getNombre());
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					var.setId(this.ifzCobranza.save(var));
					log.info("Cobranza ... Registro " + var.getId() + " guardando");
				} else {
					// Actualizamos en la BD
					this.ifzCobranza.update(var);
					log.info("Cobranza ... Registro " + var.getId() + " actualizado");
				}
			}
			log.info("Cobranza ... Terminado");
		} catch(Exception e) {
			control(true);
    		log.error("Error en GestionProyectos.CobranzaAction.guardar", e);
    	}
	}
	
	public void totalizarCobranza() {
		Double monto = 0D;
		BigDecimal montoAux = BigDecimal.ZERO;
		double porcentaje = 0;
		
		if (this.listObraCobranza == null)
			this.listObraCobranza = new ArrayList<ObraCobranza>();
		
		this.totalCobranza = 0;
		this.permiteGuardarCobranza = true;
		if (this.isDebug) log.info("Cobranza detalles ... Totalizamos");
		for (ObraCobranza var : this.listObraCobranza) {
			// Calculamos Amortizacion
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos amortizacion");
			monto = var.getEstimacion().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
			porcentaje = var.getPorcentajeAnticipo().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
			monto = ((monto * porcentaje) / 100);
			montoAux = new BigDecimal(monto).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			var.setAmortizacion(montoAux); // var.setAmortizacion(new BigDecimal(monto));
			
			// Calculamos Fondo Garantia
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos fondo garantia");
			monto = var.getEstimacion().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
			porcentaje = var.getPorcentajeRetencion().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
			monto = ((monto * porcentaje) / 100);
			montoAux = new BigDecimal(monto).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			var.setFondoGarantia(montoAux); // var.setFondoGarantia(new BigDecimal(monto));
			
			if (var.getAnticipo().doubleValue() > 0) {
				if (this.isDebug) log.info("Cobranza detalles ... Obtenemos anticipo");
				monto = var.getAnticipo().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
			} else {
				if (this.isDebug) log.info("Cobranza detalles ... Obtenemos estimacion");
				monto = var.getEstimacion().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue() - (var.getAmortizacion().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue() + var.getFondoGarantia().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
			}

			if (this.isDebug) log.info("Cobranza detalles ... Asignamos monto (Subtotal)");
			montoAux = new BigDecimal(monto).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			var.setSubtotal(montoAux); // var.setSubtotal(new BigDecimal(monto));
			monto = montoAux.doubleValue();
			
			// Caculamos el IVA del subtotal (monto)
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos IVA");
			porcentaje = ((monto * this.porcentajeIva) / 100);
			montoAux = new BigDecimal(porcentaje).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			var.setIva(montoAux); // var.setIva(new BigDecimal(porcentaje));
			porcentaje = montoAux.doubleValue();

			// Calculamos el total redondeado a 2 decimales
			if (this.isDebug) log.info("Cobranza detalles ... Calculamos total (Subtotal + IVA)");
			monto = monto + porcentaje;
			montoAux = new BigDecimal(monto).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			var.setTotal(montoAux);
			monto = montoAux.doubleValue();

			if (this.isDebug) log.info("Cobranza detalles ... Sumamos total y asignamos");
			this.totalCobranza += monto;
			montoAux = var.getMonto().setScale(2, BigDecimal.ROUND_HALF_EVEN);
			if (this.isDebug) log.info("Cobranza detalles ... Comprobamos si debemos permitir guardar la cobranza (diferencia en total)");
			if (! monto.equals(montoAux.doubleValue())) {
				this.permiteGuardarCobranza = false;
			}
		}
	}
	
	private void control() {
		control(false, 0, "");
	}
	
	private void control(boolean value) {
		this.incompleto = value;
		
		if (! value) {
			control(false, 0, "");
			return;
		} 

		control(true, 1, "ERROR");
	}
		
	private void control(int tipoMensaje) {
		if (tipoMensaje == 0) {
			control(false);
			return;
		}
		
		control(true, tipoMensaje, "ERROR");
	}
	
	private void control(String mensaje) {
		if (mensaje == null || "".equals(mensaje)) {
			control(false);
			return;
		}
		
		control(true, -1, mensaje);
	}
	
	private void control(boolean procesoInterrumpido, int tipo, String mensaje) {
		this.incompleto = procesoInterrumpido;
		this.tipoMensaje = tipo;
		this.mensaje = mensaje;
	}

	// -------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------------

	public double getPorcentajeAnticipo() {
		if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())
			return this.listObraCobranza.get(0).getPorcentajeAnticipo().doubleValue();
		return 0;
	}
	
	public void setPorcentajeAnticipo(double value) {
		if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())  {
			for (ObraCobranza var : this.listObraCobranza) {
				var.setPorcentajeAnticipo(new BigDecimal(value));
			}
			
			totalizarCobranza();
		}
	}
	
	public double getPorcentajeRetencion() {
		if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())
			return this.listObraCobranza.get(0).getPorcentajeRetencion().doubleValue();
		return 0;
	}
	
	public void setPorcentajeRetencion(double value) {
		if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())  {
			for (ObraCobranza var : this.listObraCobranza) {
				var.setPorcentajeRetencion(new BigDecimal(value));
			}
			
			totalizarCobranza();
		}
	}

	public boolean getOperacion() {
		return incompleto;
	}

	public void setOperacion(boolean incompleto) {
		this.incompleto = incompleto;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}	
	
	public List<SelectItem> getBusquedaOpciones() {
		return busquedaOpciones;
	}

	public void setBusquedaOpciones(List<SelectItem> busquedaOpciones) {
		this.busquedaOpciones = busquedaOpciones;
	}

	public String getBusquedaCampo() {
		return busquedaCampo;
	}

	public void setBusquedaCampo(String busquedaCampo) {
		this.busquedaCampo = busquedaCampo;
	}

	public String getBusquedaValor() {
		return busquedaValor;
	}

	public void setBusquedaValor(String busquedaValor) {
		this.busquedaValor = busquedaValor;
	}

	public int getBusquedaTipo() {
		return busquedaTipo;
	}

	public void setBusquedaTipo(int busquedaTipo) {
		this.busquedaTipo = busquedaTipo;
	}
	
	public int getBusquedaPaginas() {
		return busquedaPaginas;
	}

	public void setBusquedaPaginas(int busquedaPaginas) {
		this.busquedaPaginas = busquedaPaginas;
	}

	public List<Obra> getListObras() {
		return listObras;
	}
	
	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}
	
	public Obra getPojoObra() {
		return pojoObra;
	}
	
	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}
	
	public List<ObraCobranza> getListObraCobranza() {
		return listObraCobranza;
	}

	public void setListObraCobranza(List<ObraCobranza> listObraCobranza) {
		this.listObraCobranza = listObraCobranza;
	}

	public int getNumPaginaObraCobranza() {
		return numPaginaObraCobranza;
	}

	public void setNumPaginaObraCobranza(int numPaginaObraCobranza) {
		this.numPaginaObraCobranza = numPaginaObraCobranza;
	}
	
	public double getTotalCobranza() {
		return totalCobranza;
	}

	public void setTotalCobranza(double totalCobranza) {
		this.totalCobranza = totalCobranza;
	}

	public boolean isPermiteGuardarCobranza() {
		return permiteGuardarCobranza;
	}

	public void setPermiteGuardarCobranza(boolean permiteGuardarCobranza) {
		this.permiteGuardarCobranza = permiteGuardarCobranza;
	}
	
	public boolean getDebugging() {
		if (this.paramsRequest.containsKey("DEBUG") && "1".equals(this.paramsRequest.get("DEBUG"))) 
			return true;
		return false;
	}
	
	public void setDebugging(boolean value) { }
}
