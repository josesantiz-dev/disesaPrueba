package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.beans.MensajeTransaccionExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface MensajeTransaccionRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(MensajeTransaccion entity) throws Exception;

	public MensajeTransaccion saveOrUpdate(MensajeTransaccion entity) throws Exception;
	
	public List<MensajeTransaccion> saveOrUpdateList(List<MensajeTransaccion> entities) throws Exception;

	public void update(MensajeTransaccion entity) throws Exception;

	public void delete(long idMensajeTransaccion) throws Exception;

	public MensajeTransaccion findById(long idMensajeTransaccion);

	public List<MensajeTransaccion> findAll() throws Exception;
	
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value) throws Exception;

	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception;

	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value, boolean incluyeContabilizados, boolean incluyeCancelados, String orderBy, int limite) throws Exception;

	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params) throws Exception;

	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception;

	public List<MensajeTransaccion> findLikeProperty(String propertyName, final Object value) throws Exception;

	public List<MensajeTransaccion> findLikeProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception;

	public List<MensajeTransaccion> findLikeProperty(String propertyName, final Object value, boolean incluyeContabilizados, boolean incluyeCancelados, String orderBy, int limite) throws Exception;

	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params) throws Exception;

	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params, String orderBy, int limite) throws Exception;
	
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values) throws Exception;
	
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values, String orderBy, int limite) throws Exception;

	/*public Long save(Long codigoTransaccion, Long idOperacion, String descripcionOperacion, 
			Long idMoneda, String descripcionMoneda, BigDecimal importe,
			Long idPersonaReferencia, String nombrePersonaReferencia, String tipoPersonaReferencia, String referencia, 
			Long idFormaPago, String descripcionFormaPago, String referenciaFormaPago, 
			Long idSucursal, String descripcionSucursal, long idUsuarioCreacionRegistro, Date fechaRegistro, Long idEmpresa, String descripcionEmpresa, 
			long creadoPor, Date fechaCreacion, long anuladoPor, Date fechaAnulacion, int estatus) throws Exception;*/
	
	public void enviarMensajeTransaccion (MensajeTransaccion entity) throws Exception;
	
	public MensajeTransaccion comprobarMensajeTransaccion(long idTransaccion, long idOperacion) throws Exception;
	
	public MensajeTransaccion convertir(MensajeTransaccionExt entityExt) throws Exception;
	
	public MensajeTransaccionExt convertir(MensajeTransaccion entity) throws Exception;
	
	// -----------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------------------

	public Long save(MensajeTransaccionExt entityExt) throws Exception;

	public void update(MensajeTransaccionExt entityExt) throws Exception;
	
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 14/07/2016 | Javier Tirado	| Creacion de MensajeTransaccionRem