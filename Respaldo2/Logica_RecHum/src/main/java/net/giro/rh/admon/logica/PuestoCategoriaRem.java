package net.giro.rh.admon.logica;

import java.util.HashMap;
import java.util.List; 

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.PuestoCategoria;

@Remote
public interface PuestoCategoriaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(PuestoCategoria entity) throws Exception;
	
	public List<PuestoCategoria> saveOrUpdateList(List<PuestoCategoria> entities) throws Exception;

	public void update(PuestoCategoria entity) throws Exception;
	
	public void delete(PuestoCategoria entity) throws Exception;

	public PuestoCategoria findById(Long id);

	public List<PuestoCategoria> findAll();

	public List<PuestoCategoria> findByProperty(String propertyName, Object value);
	
	public List<PuestoCategoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value);

	public List<PuestoCategoria> findByIdPuesto(long idPuesto);
	
	public List<PuestoCategoria> findByPuestoCategoria(Long idPuesto, Long idCategoria) throws Exception;
	
	public List<PuestoCategoria> findByProperties(HashMap<String, Object> params) throws Exception;
	
	//----------------------------------------------------------------------------------
	// EXTENDIDOS
	//----------------------------------------------------------------------------------
	
	/*public Long save(PuestoCategoriaExt entityExt) throws Exception;
	
	public void update(PuestoCategoriaExt  entity) throws Exception; 
	
	public PuestoCategoriaExt findByIdExt(Long id);
	
	public List<PuestoCategoriaExt> findAllExt();
	
	public List<PuestoCategoriaExt> findByPropertyExt(String propertyName, Object value);

	public List<PuestoCategoriaExt> findExtByIdPuesto(long idPuesto);
	
	public List<PuestoCategoriaExt> findByPuestoCategoriaExt(long idPuesto, long idCategoria) throws Exception;*/
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-18 | Javier Tirado 	| Añado metodo findByPuestoAndCategoria que busca por ID de puesto y ID de categoria.
 *  2.2 | 2017-05-18 | Javier Tirado 	| Añado metodo findByProperties para buscar por varias propiedades a la vez
 */