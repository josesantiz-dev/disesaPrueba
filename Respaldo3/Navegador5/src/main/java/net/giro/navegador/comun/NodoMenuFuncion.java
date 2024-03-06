package net.giro.navegador.comun;

import java.util.ArrayList;
import java.util.List;

public class NodoMenuFuncion{
	private Integer id;
	private int nivel;
	private int icono;
	//id de la funcion o el menu que representa, sirve como id para buscarlo en la estructura del menu
	private int funcionId;
	//a quien apunta
	private Integer tipoId;
	private String url;
	private String tipoAccion;
	private String promt;
	private String descripcion;
	private String actepDrop;
	
	private boolean menuVisible;
	
	private List<NodoMenuFuncion> listSubMenus;
	
	public NodoMenuFuncion(){
		// 'S' significa que es un submenu de inicio 'contendra funciones como hijos' y aceptara el drop de los items
		this.id = null;
		this.actepDrop = "S";
		this.promt = "Sin Nombre";
		this.descripcion = "";
		this.nivel = 1;
		this.icono = 0;
		this.funcionId = 0;
		this.tipoId = null;
		this.tipoAccion = "F";
		this.listSubMenus = null;
		this.menuVisible = false;
		this.url = "";
	}
	
	public NodoMenuFuncion(Integer id){
		// 'S' significa que es un submenu de inicio 'contendra funciones como hijos' y aceptara el drop de los items
		this.id = id;
		this.actepDrop = "S";
		this.promt = "Sin Nombre";
		this.descripcion = "";
		this.nivel = 1;
		this.icono = 0;
		this.funcionId = 0;
		this.tipoId = null;
		this.tipoAccion = "F";
		this.listSubMenus = null;
		this.menuVisible = false;
		this.url = "";
	}
	public void addNodo(NodoMenuFuncion nodo){
		if(listSubMenus == null){
			listSubMenus = new ArrayList<NodoMenuFuncion>();
			listSubMenus.add(nodo);
		}
		else
			listSubMenus.add(nodo);
	}
	
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public int getNivel() {
		return nivel;
	}
	public void setPromt(String promt) {
		this.promt = promt;
	}
	public String getPromt() {
		return promt;
	}
	public void setActepDrop(String actepDrop) {
		this.actepDrop = actepDrop;
	}
	public String getActepDrop() {
		return actepDrop;
	}
	
	public void setListSubMenus(List<NodoMenuFuncion> listSubMenus) {
		this.listSubMenus = listSubMenus;
	}

	public List<NodoMenuFuncion> getListSubMenus() {
		return listSubMenus;
	}

	public void setIcono(int icono) {
		this.icono = icono;
	}

	public int getIcono() {
		return icono;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setTipoId(Integer tipoId) {
		this.tipoId = tipoId;
	}

	public Integer getTipoId() {
		return tipoId;
	}

	public void setTipoAccion(String tipoAccion) {
		this.tipoAccion = tipoAccion;
	}

	public String getTipoAccion() {
		return tipoAccion;
	}
	
	public boolean isListaVacia(){
		return listSubMenus != null;
	}

	public void setFuncionId(int funcionId) {
		this.funcionId = funcionId;
	}

	public int getFuncionId() {
		return funcionId;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setMenuVisible(boolean menuVisible) {
		this.menuVisible = menuVisible;
	}

	public boolean isMenuVisible() {
		return menuVisible;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setEsNodo(boolean esNodo) {/** nanai **/}

	public boolean isEsNodo() {
		return listSubMenus == null || listSubMenus.isEmpty();
	}
}
