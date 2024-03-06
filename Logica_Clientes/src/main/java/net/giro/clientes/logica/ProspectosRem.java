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
	public List<EstatusSeguimientoExt> buscarEstatusSeguimiento() throws ExcepConstraint;

	public List<ProspectoExt> buscarProspectosPorPersona(String tipoBusqueda, String valorBusqueda,Long estatusSeguimiento) throws ExcepConstraint;
	
	public List<ProspectoExt> buscarProspectosPorNegocio(String tipoBusqueda,String valorBusqueda, Long estatusSeguimiento) throws ExcepConstraint;

	public List<ConValores> buscarModosContacto() throws ExcepConstraint;

	public List<ConValores> buscarComoEntero() throws ExcepConstraint;

	public List<Sucursal> buscarSucursales() throws ExcepConstraint;

	public List<OficialExt> buscarEspecialistas() throws ExcepConstraint;

	public List<ConValores> buscarTiposEstablecimiento() throws ExcepConstraint;

	public List<ConValores> buscarRangosFacturacion() throws ExcepConstraint;

	public List<ConValores> buscarCalificaciones() throws ExcepConstraint;

	public Respuesta salvar(ProspectoExt prospectoExt);

	public Long salvar(Seguimiento seguimiento) throws ExcepConstraint;

	public Seguimiento buscarSeguimiento(ProspectoExt prospectoExt) throws ExcepConstraint;
	
	public ProspectoExt convertProspectoToProspectoExt(Prospecto prospecto) throws ExcepConstraint;
	
	public Prospecto convertProspectoExtToProspecto(ProspectoExt prospectoExt) throws ExcepConstraint;

	public DomicilioExt buscarDomicilioPrincipal(ProspectoExt prospecto) throws ExcepConstraint;
}
