package net.giro.rh.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.Categoria;
import net.giro.rh.admon.beans.Puesto;
import net.giro.rh.admon.beans.PuestoCategoria;
import net.giro.rh.admon.logica.CategoriaRem;
import net.giro.rh.admon.logica.PuestoCategoriaRem;
import net.giro.rh.admon.logica.PuestoRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="puestoCategoriaAction")
public class PuestoCategoriaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PuestoCategoriaAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	private List<PuestoCategoria> listaPuestosCategoriasGrid;
	private PuestoCategoria pojoPuestoCategoria;
	private PuestoCategoriaRem ifzPuestoCategoria;
	private CategoriaRem ifzCategoria;
	private PuestoRem ifzPuesto;
	private List<Puesto> listaPuestos;
	private List<Categoria> listaCategorias;
	private List<SelectItem> listaCboPuestos;
	private long idPuesto;
	private Puesto pojoPuesto;
	private List<SelectItem> listaCboCategorias;
	private long idCategoria;
	private Categoria pojoCategoria;
	// Para el Nuevo/Editar PuestoCategoria
	private List<SelectItem> listaCboNuevoPuesto;
	private long idNuevoPuesto;
	private List<SelectItem> listaCboNuevaCategoria;
	private long idNuevaCategoria;
    private long usuarioId;
	private int numPagina;
	// control
	private boolean band;
	private int tipoMensaje;
	private String resOperacion;
	
	public PuestoCategoriaAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			this.ctx = new InitialContext();
			this.ifzPuesto = (PuestoRem) this.ctx.lookup("ejb:/Logica_RecHum//PuestoFac!net.giro.rh.admon.logica.PuestoRem");
			this.ifzCategoria = (CategoriaRem) this.ctx.lookup("ejb:/Logica_RecHum//CategoriaFac!net.giro.rh.admon.logica.CategoriaRem");
			this.ifzPuestoCategoria = (PuestoCategoriaRem) this.ctx.lookup("ejb:/Logica_RecHum//PuestoCategoriaFac!net.giro.rh.admon.logica.PuestoCategoriaRem");
	
			this.ifzPuesto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCategoria.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPuestoCategoria.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.pojoPuestoCategoria = new PuestoCategoria();
			this.listaPuestosCategoriasGrid = new ArrayList<PuestoCategoria>();
			this.pojoPuesto = new Puesto();
			this.pojoCategoria = new Categoria();
			
			listarPuestos();
			listarCategorias();
			cargarPuestos();
			cargarCategorias();
			cargarNuevosPuestos();
			cargarNuevasCategorias();
		} catch (Exception e) {
			log.error("Error en RecHum.PuestoCategoriaAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if (this.idPuesto == 0 && this.idCategoria == 0) {
				this.listaPuestosCategoriasGrid = this.ifzPuestoCategoria.findAll();
				Collections.sort(this.listaPuestosCategoriasGrid, new Comparator<PuestoCategoria>() {
					@Override
					public int compare(PuestoCategoria o1, PuestoCategoria o2) {
						return o1.getIdCategoria().getDescripcion().compareTo(o2.getIdCategoria().getDescripcion());
					}
				});
			} else if (this.idPuesto > 0 && this.idCategoria == 0) {
				//log.info("Buscar Todos los puestos concordantes");
				this.listaPuestosCategoriasGrid = this.ifzPuestoCategoria.findByProperty("idPuesto", this.idPuesto);
				Collections.sort(this.listaPuestosCategoriasGrid, new Comparator<PuestoCategoria>() {
					@Override
					public int compare(PuestoCategoria o1, PuestoCategoria o2) {
						return o1.getIdCategoria().getDescripcion().compareTo(o2.getIdCategoria().getDescripcion());
					}
				});
			} else if (this.idPuesto == 0 && this.idCategoria > 0) {
				//log.info("Buscar Todos las categorias concordantes");
				this.listaPuestosCategoriasGrid = this.ifzPuestoCategoria.findByProperty("idCategoria", this.idCategoria);
				Collections.sort(this.listaPuestosCategoriasGrid, new Comparator<PuestoCategoria>() {
					@Override
					public int compare(PuestoCategoria o1, PuestoCategoria o2) {
						return o1.getIdPuesto().getDescripcion().compareTo(o2.getIdPuesto().getDescripcion());
					}
				});
			} else {
				//log.info("Buscar que concuerde puesto y categoria");
				this.listaPuestosCategoriasGrid = this.ifzPuestoCategoria.findByPuestoCategoria(idPuesto, idCategoria);
				Collections.sort(this.listaPuestosCategoriasGrid, new Comparator<PuestoCategoria>() {
						@Override
						public int compare(PuestoCategoria o1, PuestoCategoria o2) {
							return o1.getIdCategoria().getDescripcion().compareTo(o2.getIdCategoria().getDescripcion());
						}
				});
			}
			
			if (this.listaPuestosCategoriasGrid == null || this.listaPuestosCategoriasGrid.isEmpty()) 
				control(-1, "La busqueda no devolvio resultados");
		} catch(Exception e) {
			control("Ocurrio un problema al consultar los PuestosCategorias", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.pojoPuestoCategoria = new PuestoCategoria();
		} catch (Exception e) {
			control("Ocurrio un problema al inicializar un PuestoCategoria", e);
		}
	}
	
	public void guardar() {
		List<PuestoCategoria> existentes = null;
		
		try {
			control();
			//al no estar vacia, quiere decir que ya hay algun PuestoCategoria con ese puesto y esa categoría
			existentes = this.ifzPuestoCategoria.findByPuestoCategoria(this.pojoPuesto.getId(), this.pojoCategoria.getId());
			if (existentes != null && ! existentes.isEmpty()) {
				if (this.pojoPuestoCategoria.getId() == null || this.pojoPuestoCategoria.getId() <= 0L) {
					control(-1, "Ya existe un Puesto-Categoría con estos valores");
					return;
				}
				
				for (PuestoCategoria existente : existentes) {
					if (this.pojoPuestoCategoria.getId().longValue() == existente.getId().longValue())
						continue;
					if (existente.getEstatus() == 1)
						continue;
					if (this.pojoPuesto.getId().longValue() == existente.getIdPuesto().getId().longValue() && this.pojoCategoria.getId().longValue() == existente.getIdCategoria().getId().longValue()) {
						control(-1, "Ya existe un Puesto-Categoría con estos valores");
						return;
					}
				}
			}

			this.pojoPuestoCategoria.setIdPuesto(this.pojoPuesto);
			this.pojoPuestoCategoria.setIdCategoria(this.pojoCategoria);
			if (this.pojoPuestoCategoria.getId() == null || this.pojoPuestoCategoria.getId() <= 0L) {
				this.pojoPuestoCategoria.setEstatus(0);
				this.pojoPuestoCategoria.setCreadoPor(this.usuarioId);
				this.pojoPuestoCategoria.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoPuestoCategoria.setModificadoPor(this.usuarioId);
				this.pojoPuestoCategoria.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.pojoPuestoCategoria.setId(this.ifzPuestoCategoria.save(this.pojoPuestoCategoria));
				this.listaPuestosCategoriasGrid.add(this.pojoPuestoCategoria);
			} else {
				this.pojoPuestoCategoria.setModificadoPor(this.usuarioId);
				this.pojoPuestoCategoria.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.ifzPuestoCategoria.update(this.pojoPuestoCategoria); 
				for (PuestoCategoria var : this.listaPuestosCategoriasGrid) {
					if (var.getId() == this.pojoPuestoCategoria.getId()) {
						var = this.pojoPuestoCategoria;
						break;
					}
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al guardar el PuestoCategoria", e);
		}
	}
	
	public void editar() {
		try {	
			control();
			//Es donde se asigna el objeto para editar/eliminar
			this.pojoPuesto = this.pojoPuestoCategoria.getIdPuesto();
			this.pojoCategoria = this.pojoPuestoCategoria.getIdCategoria();
			this.idNuevoPuesto = this.pojoPuestoCategoria.getIdPuesto().getId();
			this.idNuevaCategoria = this.pojoPuestoCategoria.getIdCategoria().getId();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el PuestoCategoria indicado", e);
		}
	}

	public void eliminar() {
		try {
			control();
			this.pojoPuestoCategoria.setEstatus(1);
			this.pojoPuestoCategoria.setModificadoPor(this.usuarioId);
			this.pojoPuestoCategoria.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzPuestoCategoria.update(this.pojoPuestoCategoria);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Eliminar el PuestoCategoria indicado", e);
		}
	}
	
	private void cargarPuestos() {
		this.listaCboPuestos = null;
		this.listaCboPuestos = new ArrayList<SelectItem>();
		this.listaCboPuestos.add( new SelectItem("0", "Todos"));
		for (Puesto p : this.listaPuestos) 
			this.listaCboPuestos.add(new SelectItem(p.getId().toString(), p.getDescripcion()));
	}
	
	private void cargarNuevosPuestos() {
		this.listaCboNuevoPuesto = null;
		this.listaCboNuevoPuesto = new ArrayList<SelectItem>();
		for (Puesto p : this.listaPuestos) {
			if (p.getEstatus() == 0)
				this.listaCboNuevoPuesto.add(new SelectItem(p.getId().toString(), p.getDescripcion()));
		}
	}
	
	private void cargarNuevasCategorias() {
		this.listaCboNuevaCategoria = null;
		this.listaCboNuevaCategoria = new ArrayList<SelectItem>();
		for (Categoria c : this.listaCategorias) {
			if (c.getEstatus() == 0)
				this.listaCboNuevaCategoria.add(new SelectItem(c.getId().toString(), c.getDescripcion()));
		}
	}
	
	private void cargarCategorias() {
		this.listaCboCategorias = null;
		this.listaCboCategorias = new ArrayList<SelectItem>();
		this.listaCboCategorias.add(new SelectItem("0", "Todos"));
		for (Categoria c : this.listaCategorias) 
			this.listaCboCategorias.add(new SelectItem(c.getId().toString(), c.getDescripcion()));
	}

	//  -- ------------------------------------> El listade de puestos y el listado de categorias

	private void listarCategorias() {
		this.listaCategorias = this.ifzCategoria.findAllActivos();
		Collections.sort(this.listaCategorias, new Comparator<Categoria>() {
			@Override
			public int compare(Categoria o1, Categoria o2) {
				return o1.getDescripcion().compareTo(o2.getDescripcion());
			}
		});
	}

	private void listarPuestos() {
		try {
			if (this.listaPuestos == null)
				this.listaPuestos = new ArrayList<Puesto>();
			if (this.listaPuestos.isEmpty())
				this.listaPuestos = this.ifzPuesto.findAll();
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Puestos", e);
		}
	}
	
	private void control() {
		this.band = false;
		this.tipoMensaje = 0;
		this.resOperacion = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		this.band = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.resOperacion = mensaje.replace("\n", "<br/>");
		log.error("\n\n" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + " :: [ACTION]\n" + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	// --------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------------------------------
	
	public boolean isBand() {
		return band;
	}

	public void setBand(boolean band) {
		this.band = band;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public PuestoCategoria getPojoPuestoCategoria() {
		return pojoPuestoCategoria;
	}

	public void setPojoPuestoCategoria(PuestoCategoria pojoPuestoCategoria) {
		this.pojoPuestoCategoria = pojoPuestoCategoria;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public List<PuestoCategoria> getListaPuestosCategoriasGrid() {
		return listaPuestosCategoriasGrid;
	}

	public void setListaPuestosCategoriasGrid(List<PuestoCategoria> listaPuestosCategoriasGrid) {
		this.listaPuestosCategoriasGrid = listaPuestosCategoriasGrid;
	}
	
	public List<Puesto> getListaPuestos() {
		return listaPuestos;
	}

	public void setListaPuestos(List<Puesto> listaPuestos) {
		this.listaPuestos = listaPuestos;
	}

	public List<Categoria> getListaCategorias() {
		return listaCategorias;
	}

	public void setListaCategorias(List<Categoria> listaCategorias) {
		this.listaCategorias = listaCategorias;
	}

	public List<SelectItem> getListaCboPuestos() {
		return listaCboPuestos;
	}

	public void setListaCboPuestos(List<SelectItem> listaCboPuestos) {
		this.listaCboPuestos = listaCboPuestos;
	}

	public long getIdPuesto() {
		return idPuesto;
	}

	public void setIdPuesto(long idPuesto) {
		this.idPuesto = idPuesto;
	}

	public List<SelectItem> getListaCboCategorias() {
		return listaCboCategorias;
	}

	public void setListaCboCategorias(List<SelectItem> listaCboCategorias) {
		this.listaCboCategorias = listaCboCategorias;
	}
	
	public long getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(long idCategoria) {
		this.idCategoria = idCategoria;
	}

	//Nuevo Editar Puesto
	
	public List<SelectItem> getListaCboNuevoPuesto() {
		return listaCboNuevoPuesto;
	}

	public void setListaCboNuevoPuesto(List<SelectItem> listaCboNuevoPuesto) {
		this.listaCboNuevoPuesto = listaCboNuevoPuesto;
	}

	public long getIdNuevoPuesto() {
		return idNuevoPuesto;
	}

	public void setIdNuevoPuesto(long idNuevoPuesto) {
		this.idNuevoPuesto = idNuevoPuesto;
		try {
			for (Puesto p : this.listaPuestos) {
				if (p.getId() == idNuevoPuesto) {
					this.pojoPuesto = this.ifzPuesto.findById(idNuevoPuesto);
					break;
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al asignar el Puesto");
		}
	}

	public List<SelectItem> getListaCboNuevaCategoria() {
		return listaCboNuevaCategoria;
	}

	public void setListaCboNuevaCategoria(List<SelectItem> listaCboNuevaCategoria) {
		this.listaCboNuevaCategoria = listaCboNuevaCategoria;
	}

	public long getIdNuevaCategoria() {
		return idNuevaCategoria;
	}

	public void setIdNuevaCategoria(long idNuevaCategoria) {
		this.idNuevaCategoria = idNuevaCategoria;
		for (Categoria c : this.listaCategorias) {
			if (c.getId() == idNuevaCategoria) {
				this.pojoCategoria = this.ifzCategoria.findById(idNuevaCategoria);
				break;
			}
		}
	}
}


//HISTORIAL DE MODIFICACIONES 
//-------------------------------------------------------------------------------------------------------------------
//VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
//-------------------------------------------------------------------------------------------------------------------
//  1.0   | 2016-09-20 | Javier Tirado      | Se reasigno el pojo principal de edicion al guardar un nevo PuestoCategoria en el metodo Guardar
