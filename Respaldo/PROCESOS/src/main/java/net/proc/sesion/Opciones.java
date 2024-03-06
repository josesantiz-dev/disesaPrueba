package net.proc.sesion;

import java.util.HashMap;


public class Opciones {
		public class Grupo {
			
			public Grupo() {}
			
			public class Subgrupo {
				
				public Subgrupo() {}
				
				public class Programa {
					
					public Programa() {}
					
					CPrograma cPrograma = new CPrograma();
					HashMap<String, CParametro> cParametros = new HashMap<String,CParametro>();
					
					public CParametro getParametro(String parametro) {
						if (cParametros.get(parametro) == null)
							cParametros.put(parametro, new CParametro());
						return cParametros.get(parametro);
					}
					
					public void setCPrograma(long id, String descripcion, String titulo, String programa) {
						cPrograma.setDescripcion(descripcion);
						cPrograma.setId(id);
						cPrograma.setTitulo(titulo);
						cPrograma.setPrograma(programa);
					}

					public CPrograma getcPrograma() {
						return cPrograma;
					}

					public void setcPrograma(CPrograma cPrograma) {
						this.cPrograma = cPrograma;
					}

					public HashMap<String, CParametro> getcParametros() {
						return cParametros;
					}

					public void setcParametros(HashMap<String, CParametro> cParametros) {
						this.cParametros = cParametros;
					}
					
				}
				
				HashMap<String, Programa> programas = new HashMap<String,Programa>();
				
				public HashMap<String, Programa> getProgramas() {
					return programas;
				}

				public void setProgramas(HashMap<String, Programa> programas) {
					this.programas = programas;
				}

				public Programa getPrograma(String programa) {
					if (programas.get(programa) == null)
						programas.put(programa, new Programa());
					return programas.get(programa);
				}
				
				
			}
			HashMap<String,Subgrupo> subgrupos = new HashMap<String,Subgrupo>() ;
			
			public HashMap<String, Subgrupo> getSubgrupos() {
				return subgrupos;
			}

			public void setSubgrupos(HashMap<String, Subgrupo> subgrupos) {
				this.subgrupos = subgrupos;
			}

			public Subgrupo getSubgrupo(String subgrupo) {
				if (subgrupos.get(subgrupo) == null)
					subgrupos.put(subgrupo, new Subgrupo());
				return subgrupos.get(subgrupo);
			}
		}
		
		HashMap<String,Grupo> grupos = new HashMap<String,Grupo>();
		
		public HashMap<String, Grupo> getGrupos() {
			return grupos;
		}

		public void setGrupos(HashMap<String, Grupo> grupos) {
			this.grupos = grupos;
		}

		public Grupo getGrupo(){
			return new Grupo();
		}
		
		
		public Grupo getGrupo(String grupo) {
			if (grupos.get(grupo) == null)
				grupos.put(grupo, new Grupo());
			return grupos.get(grupo);
		}
		public Opciones() {}
		
}

 
