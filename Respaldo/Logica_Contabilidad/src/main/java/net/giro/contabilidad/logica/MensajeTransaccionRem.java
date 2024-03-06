package net.giro.contabilidad.logica;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.beans.MensajeTransaccionExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface MensajeTransaccionRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void limite(Integer limite);
	
	public void orderBy(String orderBy);
	
	//public void estatus(Long estatus);

	public Long save(MensajeTransaccion entity) throws Exception;
	
	public Long save(MensajeTransaccionExt entityExt) throws Exception;
	
	public void update(MensajeTransaccion entity) throws Exception;
	
	public void update(MensajeTransaccionExt entityExt) throws Exception;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public MensajeTransaccion findById(Long id);
	
	//public MensajeTransaccionExt findExtById(Long id) throws Exception;

	public List<MensajeTransaccion> findAll() throws Exception;
	
	//public List<MensajeTransaccionExt> findExtAll() throws Exception;
	
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value) throws Exception;
	
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	//public List<MensajeTransaccionExt> findExtByProperty(String propertyName, final Object value) throws Exception;

	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params) throws Exception;

	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	//public List<MensajeTransaccionExt> findExtByProperties(HashMap<String, Object> params) throws Exception;

	public List<MensajeTransaccion> findLikeProperty(String propertyName, final String value) throws Exception;

	public List<MensajeTransaccion> findLikeProperty(String propertyName, final String value, int limite) throws Exception;
	
	//public List<MensajeTransaccionExt> findExtLikeProperty(String propertyName, final String value) throws Exception;

	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params) throws Exception;

	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	//public List<MensajeTransaccionExt> findExtLikeProperties(HashMap<String, String> params) throws Exception;
	
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values) throws Exception;
	
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	
	//public List<MensajeTransaccionExt> findExtInProperty(String columnName, List<Object> values) throws Exception;

	//public MensajeTransaccion cancelar(MensajeTransaccion entity) throws Exception;
	
	//public MensajeTransaccion cancelar(MensajeTransaccionExt entityExt) throws Exception;

	public Long save(Long idTransaccion, Long idOperacion, Long idMoneda, String descripcionMoneda, BigDecimal importe,
			Long idPersonaReferencia, String nombrePersonaReferencia, String referencia, Long idFormaPago,
			String referenciaFormaPago, long idUsuarioCreacionRegistro, Long idSucursal, Date fechaRegistro, Long idEmpresa,
			long creadoPor, Date fechaCreacion, long anuladoPor, Date fechaAnulacion, int estatus) throws Exception;
	
	public void enviarMensajeTransaccion (MensajeTransaccion entity) throws Exception;
	
	public MensajeTransaccion comprobarMensajeTransaccion(Long idTransaccion, Long idOperacion) throws Exception;
	
	public MensajeTransaccion convertir(MensajeTransaccionExt entityExt) throws Exception;
	
	public MensajeTransaccionExt convertir(MensajeTransaccion entity) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 14/07/2016 | Javier Tirado	| Creacion de MensajeTransaccionRem