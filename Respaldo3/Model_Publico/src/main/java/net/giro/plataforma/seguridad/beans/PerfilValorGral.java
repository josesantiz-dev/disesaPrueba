package net.giro.plataforma.seguridad.beans;

import java.io.Serializable;
import java.util.HashMap;

public class PerfilValorGral implements Serializable{
	
	private static final long serialVersionUID = -7873708158668944560L;
	private Perfil	pojoPerfil;
	private PerfilValor	pojoUsuario;
	private PerfilValor	pojoResponsabilidad;
	private PerfilValor	pojoPuesto;
	private PerfilValor	pojoSucursal;
	private PerfilValor	pojoEmpresa;
	private PerfilValor	pojoTerminal;
	private PerfilValor	pojoSitio;
	private HashMap<Object,Object> descripciones;
	private int tipoSeleccion;
	private boolean editado;
	
	public PerfilValorGral(Perfil pg){
		tipoSeleccion = 1;
		this.pojoPerfil	= pg;
		this.pojoTerminal = null;
		this.pojoUsuario	= null;
		this.pojoResponsabilidad = null;
		this.pojoSucursal	= null;
		this.pojoPuesto	= null;
		this.pojoEmpresa = null;
		this.pojoSitio	= null;
		descripciones = new HashMap<Object,Object>();
	}
	
	public PerfilValorGral(){/*nanai*/}
	
	public void setNombrePerfil(String nombrePerfil) {/* nanai */}
	
	public String getNombrePerfil() {
		return this.pojoPerfil.getPerfil();
	}
	public String getNombreTerminal() {
		return getValorPerfil(this.pojoTerminal);
	}
	public void setNombreTerminal(String nombreTerminal) {
		if(pojoTerminal==null)
			pojoTerminal = new PerfilValor();
		pojoUsuario.setValorPerfil(nombreTerminal);
		this.editado = true;
	} 
	
	public String getNombreUsuario() {
		return getValorPerfil(this.pojoUsuario);
	}
	public void setNombreUsuario(String nombreUsuario) {
		if(pojoUsuario==null)
			pojoUsuario = new PerfilValor();
		pojoUsuario.setValorPerfil(nombreUsuario);
		this.editado = true;
	}
	
	public String getNombreResponsabilidad() {
		return getValorPerfil(this.pojoResponsabilidad);
	}
	public void setNombreResponsabilidad(String nombreResponsabilidad) {
		if(pojoResponsabilidad==null)
			pojoResponsabilidad = new PerfilValor();
		pojoResponsabilidad.setValorPerfil(nombreResponsabilidad);
		this.editado = true;
	}
	
	public String getNombrePuesto() {
		return getValorPerfil(this.pojoPuesto);
	}
	public void setNombrePuesto(String nombrePuesto) {
		if(pojoPuesto==null)
			pojoPuesto = new PerfilValor();
		pojoPuesto.setValorPerfil(nombrePuesto);
		this.editado = true;
	}
	public String getNombreSucursal() {
		return getValorPerfil(this.pojoSucursal);
	}
	public void setNombreSucursal(String nombreSucursal) {
		if(pojoSucursal==null)
			pojoSucursal = new PerfilValor();
		pojoSucursal.setValorPerfil(nombreSucursal);
		this.editado = true;
	}
	public String getNombreEmpresa() {
		return getValorPerfil(this.pojoEmpresa);
	}
	public void setNombreEmpresa(String nombreEmpresa) {
		if(pojoEmpresa==null)
			pojoEmpresa = new PerfilValor();
		pojoEmpresa.setValorPerfil(nombreEmpresa);
		this.editado = true;
	}
	public String getNombreSitio() {
		return getValorPerfil(this.pojoSitio);
	}
	public void setNombreSitio(String nombreSitio) {
		if(pojoSitio==null)
			pojoSitio = new PerfilValor();
		pojoSitio.setValorPerfil(nombreSitio);
		this.editado = true;
	}
	public Perfil getPojoPerfil() {
		return pojoPerfil;
	}
	public void setPojoPerfil(Perfil pojoPerfil) {
		this.pojoPerfil = pojoPerfil;
	}
	public PerfilValor getPojoTerminal() {
		return pojoTerminal;
	}
	public void setPojoTerminal(PerfilValor pojoTerminal) {
		this.pojoTerminal = pojoTerminal;
	}
	public PerfilValor getPojoUsuario() {
		return pojoUsuario;
	}
	public void setPojoUsuario(PerfilValor pojoUsuario) {
		this.pojoUsuario = pojoUsuario;
	}
	public PerfilValor getPojoPuesto() {
		return pojoPuesto;
	}
	public void setPojoPuesto(PerfilValor pojoPuesto) {
		this.pojoPuesto = pojoPuesto;
	}
	public PerfilValor getPojoSucursal() {
		return pojoSucursal;
	}
	public void setPojoSucursal(PerfilValor pojoSucursal) {
		this.pojoSucursal = pojoSucursal;
	}
	public PerfilValor getPojoEmpresa() {
		return pojoEmpresa;
	}
	public void setPojoEmpresa(PerfilValor pojoEmpresa) {
		this.pojoEmpresa = pojoEmpresa;
	}
	public PerfilValor getPojoSitio() {
		return pojoSitio;
	}
	public void setPojoSitio(PerfilValor pojoSitio) {
		this.pojoSitio = pojoSitio;
	}

	private String getValorPerfil(PerfilValor pojo){
		return pojo==null || pojo.getValorPerfil()==null ? "" : pojo.getValorPerfil();
	}

	public void setEditado(boolean editado) {
		this.editado = editado;
	}

	public boolean isEditado() {
		return editado;
	}

	public void setPojoResponsabilidad(PerfilValor pojoResponsabilidad) {
		this.pojoResponsabilidad = pojoResponsabilidad;
	}

	public PerfilValor getPojoResponsabilidad() {
		return pojoResponsabilidad;
	}

	public void setTipoSeleccion(int tipoSeleccion) {
		this.tipoSeleccion = tipoSeleccion;
	}

	public int getTipoSeleccion() {
		return tipoSeleccion;
	}

	public void setValorSeleccion(String valorSeleccion) { /** nanai **/}

	public String getValorSeleccion() {
		switch(tipoSeleccion){
			case 1 : return		pojoTerminal != null && pojoPerfil != null ? pojoPerfil.getPerfil() + " (Terminal)"	: " ";
			case 2 : return		pojoUsuario != null && pojoPerfil != null ? pojoPerfil.getPerfil() + " (Usuario)"	: " ";
			case 4 : return		pojoResponsabilidad != null &&  pojoPerfil != null ? pojoPerfil.getPerfil() + " (Responsabilidad)" : " ";
			case 8 : return		pojoPuesto 	!= null && pojoPerfil != null ? pojoPerfil.getPerfil() + " (Puesto)": " ";
			case 16 : return	pojoSucursal 	!= null && pojoPerfil != null ? pojoPerfil.getPerfil() + " (Sucursal)"	: " ";
			case 32 : return 	pojoEmpresa!= null 	&& pojoPerfil != null ? pojoPerfil.getPerfil() + " (Empresa)" : " ";
			case 64 : return 	pojoSitio	!= null && pojoPerfil != null ? pojoPerfil.getPerfil() + " (Sitio)" : " ";
			default: return "";
		}
	}

	public void setDescripciones(HashMap<Object,Object> descripciones) {
		this.descripciones = descripciones;
	}

	public HashMap<Object,Object> getDescripciones() {
		return descripciones;
	}
}

