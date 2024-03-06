package net.giro.sesion;

import java.io.Serializable;
import java.util.HashMap;

import net.giro.sesion.Grupo;

public class Opciones implements Serializable {
	private static final long serialVersionUID = 4904790096623221900L;
	private HashMap<String, Grupo> grupos = new HashMap<String, Grupo>();
	
	public Opciones() {
		this.grupos = new HashMap<String, Grupo>();
	}

	public HashMap<String, Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(HashMap<String, Grupo> grupos) {
		this.grupos = grupos;
	}

	public Grupo getGrupo() {
		return new Grupo();
	}
	
	public Grupo getGrupo(String grupo) {
		if (this.grupos.get(grupo) == null)
			this.grupos.put(grupo, new Grupo());
		return this.grupos.get(grupo);
	}
	
	
	/*public class Grupo {
		private HashMap<String, Subgrupo> subgrupos = new HashMap<String, Subgrupo>();
		
		public Grupo() {
			this.subgrupos = new HashMap<String, Subgrupo>();
		}

		public HashMap<String, Subgrupo> getSubgrupos() {
			return subgrupos;
		}

		public void setSubgrupos(HashMap<String, Subgrupo> subgrupos) {
			this.subgrupos = subgrupos;
		}

		public Subgrupo getSubgrupo(String subgrupo) {
			if (this.subgrupos.get(subgrupo) == null)
				this.subgrupos.put(subgrupo, new Subgrupo());
			return this.subgrupos.get(subgrupo);
		}

		
		public class Subgrupo {
			private HashMap<String, Programa> programas = new HashMap<String,Programa>();
			
			public Subgrupo() {
				this.programas = new HashMap<String,Programa>();
			}
			
			public HashMap<String, Programa> getProgramas() {
				return programas;
			}

			public void setProgramas(HashMap<String, Programa> programas) {
				this.programas = programas;
			}

			public Programa getPrograma(String programa) {
				if (this.programas.get(programa) == null)
					this.programas.put(programa, new Programa());
				return this.programas.get(programa);
			}
			
			
			public class Programa {
				private CPrograma cPrograma = new CPrograma();
				private HashMap<String, CParametro> cParametros = new HashMap<String,CParametro>();
				
				public Programa() {
					this.cParametros = new HashMap<String,CParametro>();
					this.cPrograma = new CPrograma();
				}
				
				public CParametro getParametro(String parametro) {
					if (this.cParametros.get(parametro) == null)
						this.cParametros.put(parametro, new CParametro());
					return this.cParametros.get(parametro);
				}
				
				public void setCPrograma(long id, String descripcion, String titulo, String programa) {
					this.cPrograma.setDescripcion(descripcion);
					this.cPrograma.setId(id);
					this.cPrograma.setTitulo(titulo);
					this.cPrograma.setPrograma(programa);
				}

				public CPrograma getcPrograma() {
					return this.cPrograma;
				}

				public void setcPrograma(CPrograma cPrograma) {
					this.cPrograma = cPrograma;
				}

				public HashMap<String, CParametro> getcParametros() {
					return this.cParametros;
				}

				public void setcParametros(HashMap<String, CParametro> cParametros) {
					this.cParametros = cParametros;
				}
			}
		}
	}*/
}

 
