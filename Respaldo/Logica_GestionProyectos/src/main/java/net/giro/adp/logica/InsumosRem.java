package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.Insumos;
import net.giro.adp.beans.InsumosExt;
import net.giro.plataforma.InfoSesion;
import net.giro.comun.ExcepConstraint;
import net.giro.respuesta.Respuesta;

@Remote
public interface InsumosRem {
	public void setInfoSecion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);

	public Long save(Insumos entity) throws ExcepConstraint;
	public Long save(InsumosExt entityExt) throws ExcepConstraint;
	
	public void update(Insumos entity) throws ExcepConstraint;
	public void update(InsumosExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;
	
	public List<Insumos> findAll();

	public Insumos findById(Long id);
	public InsumosExt findByIdExt(Long id) throws Exception;

	public List<Insumos> findByProperty(String propertyName, Object value, int max);
	public List<InsumosExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception;

	public List<Insumos> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	public List<InsumosExt> findLikePropertyExt(String propertyName, final Object value, int max) throws Exception;
	
	public Respuesta analizarExcel(byte[] fileSrc, String fileExtension, HashMap<String, String> insumosCellReference) throws Exception;
	
	public boolean comprobarInsumo(Long idObra);

	public List<Insumos> findByActivos(int max);
	public List<InsumosExt> findExtByActivos(int max);
	
	Insumos convertir(InsumosExt target) throws Exception;	
	InsumosExt convertir(Insumos target) throws Exception;
}
