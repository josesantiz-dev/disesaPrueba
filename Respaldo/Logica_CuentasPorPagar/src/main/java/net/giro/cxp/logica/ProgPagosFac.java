package net.giro.cxp.logica;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.comun.ExcepConstraint;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.ProgPagos;
import net.giro.cxp.beans.ProgPagosExt;
import net.giro.cxp.beans.SucursalExt;
import net.giro.cxp.dao.ProgPagosDAO;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;

@Stateless
@Remote(ProgPagosRem.class)  
public class ProgPagosFac implements ProgPagosRem {
	// property constants
		public static final String CREADO_POR = "creadoPor";
		public static final String MODIFICADO_POR = "modificadoPor";
		public static final String AGENTE = "agente";
		public static final String TOTAL = "total";
		public static final String ESTATUS = "estatus";
		public static final String TRANSFERENCIA_ID = "transferenciaId";
		public static final String MONTO_AUTORIZADO = "montoAutorizado";
		public static final String OBSERVACIONES = "observaciones";
		public static final String MONTO_REVISADO = "montoRevisado";
		public static final String SALDO_ACTUAL = "saldoActual";
		
		private ConvertExt convertidor;
		private ProgPagosDAO ifzProgPagos;
		private SucursalesRem ifzSucursal;
		private PersonaRem ifzPersona;
		private NegociosRem ifzNegocio;
		
		private InitialContext ctx;

		public ProgPagosFac() {
    		try {
        		Hashtable<String, Object> p = new Hashtable<String, Object>();
                p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
				this.ctx = new InitialContext(p);
				
				this.ifzProgPagos = (ProgPagosDAO)  this.ctx.lookup("ejb:/Model_CuentasPorPagar//ProgPagosImp!net.giro.cxp.dao.ProgPagosDAO");
				this.ifzSucursal  = (SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
				this.ifzPersona   = (PersonaRem) 	this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
				this.ifzNegocio   = (NegociosRem) 	this.ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
	    		
				this.convertidor = new ConvertExt();
	    		this.convertidor.setFrom("ProgPagosFac");
				this.convertidor.setMostrarSystemOut(false);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		public Long save(ProgPagosExt entity)  throws Exception {
			long id = 0L;
			
			try {
				ProgPagos pojoProgPagos = this.convertidor.ProgPagosExtToProgPagos(entity);
				id = this.ifzProgPagos.save(pojoProgPagos);
				entity.setId(id);
			} catch (RuntimeException re) {
				throw re;
			}
			
			return id;
		}
		
		public void delete(ProgPagosExt entity) throws ExcepConstraint {
			try {
				this.ifzProgPagos.delete(entity.getId());
			} catch (RuntimeException re) {
				throw re;
			}
		}

		public ProgPagosExt update(ProgPagosExt entity) throws Exception {
			try {
				ProgPagos pojoProgPagos = this.convertidor.ProgPagosExtToProgPagos(entity);
				this.ifzProgPagos.update(pojoProgPagos);
			} catch (RuntimeException re) {
				throw re;
			}
			
			return entity;
		}

		public ProgPagosExt findById(Long id) throws Exception {
			try {
				ProgPagos instance = this.ifzProgPagos.findById(id);
				if (instance != null)
					return this.convertidor.ProgPagosToProgPagosExt(instance);
			} catch (RuntimeException re) {
				throw re;
			}
			
			return null;
		}
		
		public ProgPagosExt findByIdPojoCompleto(long id) throws Exception {
			try {
				ProgPagos instance = this.ifzProgPagos.findByIdPojoCompleto(id);
				if (instance != null)
					return this.convertidor.ProgPagosToProgPagosExt(instance);
			} catch (RuntimeException re) {
				throw re;
			}
			
			return null;
		}

		public List<ProgPagosExt> findByProperty(String propertyName, final Object value) throws Exception {
			List<ProgPagosExt> listProgPagosExt = new ArrayList<ProgPagosExt>();
			
			try {
				List<ProgPagos> listProgPagos = this.ifzProgPagos.findByProperty(propertyName, value);
				if (listProgPagos.size() > 0) {
					for (ProgPagos progPagos : listProgPagos) {
						ProgPagosExt pojoProgPagosExt = this.convertidor.ProgPagosToProgPagosExt(progPagos);
						listProgPagosExt.add(pojoProgPagosExt);
					}
				} 
			} catch (RuntimeException re) {
				throw re;
			}
			
			return listProgPagosExt;
		}

		public List<ProgPagosExt> findByProgramaciones(long value, int max) throws Exception {
			List<ProgPagosExt> listProgPagosExt = new ArrayList<ProgPagosExt>();
			
			try {
				List<ProgPagos> listProgPagos = this.ifzProgPagos.findByProgramaciones(value, max);
				if (listProgPagos.size() > 0) {
					for (ProgPagos progPagos : listProgPagos) {
						ProgPagosExt pojoProgPagosExt = this.convertidor.ProgPagosToProgPagosExt(progPagos);
						listProgPagosExt.add(pojoProgPagosExt);
					}
				} 
			} catch (RuntimeException re) {
				throw re;
			}
			
			return listProgPagosExt;
		}
		
		public List<ProgPagosExt> findByRevisiones(Date fech1, Date fech2, int max) throws Exception {
			List<ProgPagosExt> listProgPagosExt = new ArrayList<ProgPagosExt>();
			
			try {
				List<ProgPagos> listProgPagos = this.ifzProgPagos.findByRevisiones(fech1, fech2, max);
				if (listProgPagos.size() > 0) {
					for (ProgPagos progPagos : listProgPagos) {
						ProgPagosExt pojoProgPagosExt = this.convertidor.ProgPagosToProgPagosExt(progPagos);
						listProgPagosExt.add(pojoProgPagosExt);
					}
				} 
			} catch (RuntimeException re) {
				throw re;
			}
			
			return listProgPagosExt;
		}
		
		public List<ProgPagosExt> findAll() throws Exception {
			List<ProgPagosExt> listProgPagosExt = new ArrayList<ProgPagosExt>();
			
			try {
				List<ProgPagos> listProgPagos = this.ifzProgPagos.findAll();
				if (listProgPagos.size() > 0) {
					for (ProgPagos progPagos : listProgPagos) {
						ProgPagosExt pojoProgPagosExt = this.convertidor.ProgPagosToProgPagosExt(progPagos);
						listProgPagosExt.add(pojoProgPagosExt);
					}
				} 
			} catch (RuntimeException re) {
				throw re;
			}
			
			return listProgPagosExt;
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
				
				if("P".equals(tipoPersona)) {
					pojoPersona = this.ifzPersona.findById(id);
				} else {
					Negocio pojoNegocio = this.ifzNegocio.findById(id);
					pojoPersona.setId(pojoNegocio.getId());
					pojoPersona.setNombre(pojoNegocio.getNombre());
					pojoPersona.setRfc(pojoNegocio.getRfc());
					pojoPersona.setBanco(pojoNegocio.getBanco());
					pojoPersona.setClabeInterbancaria(pojoNegocio.getClabeInterbancaria());
					pojoPersona.setTipoPersona(0L);
				}
				
				if (pojoPersona != null) {
					pojoPersonaExt = this.convertidor.PersonaToPersonaExt(pojoPersona);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return pojoPersonaExt;
		}
}
