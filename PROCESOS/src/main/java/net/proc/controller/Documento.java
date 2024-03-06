package net.proc.controller;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Documento
{
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
  public static void main(String[] args)
  {
    try
    {
      String tmp = "and ddkdkdk %s";
      
	int cont = 0;
      while (tmp.indexOf("%s") >= 0)
      {
        tmp = tmp.substring(tmp.indexOf("%s") + 1);
        cont++;
      }
      String mensaje = "Error de proceso  {buscar cliente},{Clase: buscadId}, {exception: null pointer}";
      
      RelacionDato dat1 = new RelacionDato();
      dat1.campoBase = "cuenta_id";
      String campos = "empresa, cuenta_id, fecha, representante, beneficiario, credito, aportacion, total, neto, financiado, ahorro, subsidio, neto_letras, subsidio_letras";
      
      dat1.camposComplemento = campos;
      dat1.nombreNodo = "contrato";
      dat1.nombrePlural = "contratos";
      
      dat1.relaciones = new ArrayList();
      
      Gson gson = new Gson();
      System.out.println(gson.toJson(dat1));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
