package net.giro.cargas.documentos.plataforma;

import net.giro.cargas.documentos.plataforma.PerfilTmp;

public class PerfilTmp implements java.io.Serializable  {
	private static final long serialVersionUID = 1L;
	private int idPerfilValor, nivel;
	private String nombre, detalle;
	private int menuId;
	private PerfilTmp combinacion; 
	public PerfilTmp(){
		this.nivel = 0;
		this.idPerfilValor = 0;
		this.menuId = 0;
		this.nombre = null;
		this.detalle = null;
		this.combinacion = null;
	}
	public PerfilTmp(int id, String nombre, String detalle, int nivel, int menuId){
		this.idPerfilValor = id;
		this.nombre = nombre;
		this.detalle = detalle;
		this.nivel = nivel;
		this.menuId = menuId;
	}
	
	public void setIdPerfilValor(int id) {
		this.idPerfilValor = id;
	}
	public int getIdPerfilValor() {
		return idPerfilValor;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombre() {
		return this.combinacion==null ? this.nombre:this.nombre + "-" + getNombCombinaciones(this.combinacion);
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getDetalle() {
		return this.combinacion==null ? this.detalle:this.detalle + "-" + getDetCombinaciones(this.combinacion);
	}
	public String ToString(){
		return getDetalle();
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public int getNivel() {
		return nivel;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setCombinacion(PerfilTmp combinacion) {
		this.combinacion = combinacion;
	}
	public PerfilTmp getCombinacion() {
		return combinacion;
	}
	private String getDetCombinaciones(PerfilTmp p){
			return p.getDetalle();
	}
	private String getNombCombinaciones(PerfilTmp p){
			return p.getNombre();
	}
}
