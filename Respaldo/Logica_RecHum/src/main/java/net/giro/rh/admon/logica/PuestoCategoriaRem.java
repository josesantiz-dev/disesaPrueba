package net.giro.rh.admon.logica;

import java.util.HashMap;
import java.util.List; 

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.PuestoCategoria;
import net.giro.rh.admon.beans.PuestoCategoriaExt;

@Remote
public interface PuestoCategoriaRem {

	public Long save(PuestoCategoria entity) throws ExcepConstraint;
	public Long save(PuestoCategoriaExt entityExt) throws ExcepConstraint;
	
	public void delete(PuestoCategoria entity) throws ExcepConstraint;
	//public void delete(PuestoCategoriaExt entityExt) throws ExcepConstraint;

	public PuestoCategoria update(PuestoCategoria entity) throws ExcepConstraint;
	public PuestoCategoria update(PuestoCategoriaExt  entity) throws ExcepConstraint; 

	public PuestoCategoria findById(Long id);
	public PuestoCategoriaExt findByIdExt(Long id);
	

	public List<PuestoCategoria> findByProperty(String propertyName, Object value);
	public List<PuestoCategoriaExt> findByPropertyExt(String propertyName, Object value);
	

	public List<PuestoCategoriaExt> findByPuestoCategoriaExt(int idPuesto, int idCategoria);
	
	public List<PuestoCategoriaExt> findByIdPuesto(int idPuesto);

	public List<PuestoCategoria> findAll();
	public List<PuestoCategoriaExt> findAllExt();
	
	public List<PuestoCategoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	//public List<PuestoCategoriaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
	public List<PuestoCategoria> findByPuestoAndCategoria(Long idPuesto, Long idCategoria) throws Exception;
	public List<PuestoCategoria> findByProperties(HashMap<String, Object> params) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-18 | Javier Tirado 	| Añado metodo findByPuestoAndCategoria que busca por ID de puesto y ID de categoria.
 *  2.2 | 2017-05-18 | Javier Tirado 	| Añado metodo findByProperties para buscar por varias propiedades a la vez
 */