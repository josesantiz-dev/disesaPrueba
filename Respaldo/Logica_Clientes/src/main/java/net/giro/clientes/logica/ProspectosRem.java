package net.giro.clientes.logica;


import java.util.List;

import javax.ejb.Remote;

import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.EstatusSeguimientoExt;
import net.giro.clientes.beans.OficialExt;
import net.giro.clientes.beans.Prospecto;
import net.giro.clientes.beans.ProspectoExt;
import net.giro.clientes.beans.Seguimiento;
import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Sucursal;
import net.giro.plataforma.beans.ConValores;
import net.giro.respuesta.Respuesta;


@Remote
public interface ProspectosRem {

	List<EstatusSeguimientoExt> buscarEstatusSeguimiento() throws ExcepConstraint;

	List<ProspectoExt> buscarProspectosPorPersona(String tipoBusqueda, String valorBusqueda,Long estatusSeguimiento) throws ExcepConstraint;
	
	public List<ProspectoExt> buscarProspectosPorNegocio(String tipoBusqueda,String valorBusqueda, Long estatusSeguimiento) throws ExcepConstraint;

	List<ConValores> buscarModosContacto() throws ExcepConstraint;

	List<ConValores> buscarComoEntero() throws ExcepConstraint;

	List<Sucursal> buscarSucursales() throws ExcepConstraint;

	List<OficialExt> buscarEspecialistas() throws ExcepConstraint;

	List<ConValores> buscarTiposEstablecimiento() throws ExcepConstraint;

	List<ConValores> buscarRangosFacturacion() throws ExcepConstraint;

	List<ConValores> buscarCalificaciones() throws ExcepConstraint;

	Respuesta salvar(ProspectoExt prospectoExt);

	Long salvar(Seguimiento seguimiento) throws ExcepConstraint;

	Seguimiento buscarSeguimiento(ProspectoExt prospectoExt) throws ExcepConstraint;
	
	public ProspectoExt convertProspectoToProspectoExt(Prospecto prospecto) throws ExcepConstraint;
	
	public Prospecto convertProspectoExtToProspecto(ProspectoExt prospectoExt) throws ExcepConstraint;

	DomicilioExt buscarDomicilioPrincipal(ProspectoExt prospecto) throws ExcepConstraint;
}
