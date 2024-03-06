package net.izel.framework.util;

import java.math.BigDecimal;
//import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalculoCAT {

	//------------------------------------ Variables obtenidas por parametro
	private double vPeriodo = 0.00d;
	private double vMontoCredito = 0.00d;
	@SuppressWarnings("unused")
	private double vGarLiq = 0.00d;
	@SuppressWarnings("unused")
	private double vAhorro = 0.00d;
	@SuppressWarnings("unused")
	private double vRetenciones;
	@SuppressWarnings("unused")
	private List<DatosCatOBJ> listMinistras = new ArrayList<DatosCatOBJ>();
	@SuppressWarnings("unused")
	private List<DatosCatOBJ> listPagares = new ArrayList<DatosCatOBJ>();

	//------------------------------------ Variable resultante
	private BigDecimal vCAT = BigDecimal.ZERO;

	CalculoCAT(double periodo, double montoCredito, double vGarLiq, double vAhorro, double vRetenciones, List<DatosCatOBJ> listMinistras, List<DatosCatOBJ> listPagares){
		this.vPeriodo = periodo;
		this.vMontoCredito = montoCredito;
		this.vGarLiq = vGarLiq;
		this.vAhorro = vAhorro;
		this.vRetenciones = vRetenciones;
		this.listMinistras = listMinistras;
		this.listPagares = listPagares;
		
		this.vCAT = CAT(vPeriodo, vMontoCredito, vGarLiq, vAhorro, vRetenciones, listMinistras, listPagares );
		
		System.out.println("Calculo CAT : " + this.vCAT );
	
	}

	// control	monto_garantia	comision	numero_pagos	cat	    monto	tasaf_agte	diascalculo	tasa_anual	plazo	dias	amort_fija	descripcion
	// B-00311/14	500	          500	         12	      155.36	10500	6.61	        30	      79.32	      M	     30	      1295	     MEJORA
	// C-00442/14  3500	         2100	         12	      141.29	72100	6.08	        28	      78.17142857 M	     30	      9350	     CREDIMPULSA


	@SuppressWarnings("unused")
	public static BigDecimal CAT(double periodo, double montoCredito, double vGarLiq, double vAhorro, double vRetenciones, List<DatosCatOBJ> listMinistras, List<DatosCatOBJ> listPagares){
		double cat = 0.00d;
		double vFactorDias = 0.00d;
		double calculoAnios = 0.00d;
		double fechaMin2double = 0.00d;
		double fechaIni2double = 0.00d;
		double fechaVen2double = 0.00d;
		double garantiaLiquidas = 0.00d;
		String fechaMinistraTexto = "";
		String fechaInicioTexto = "";
		Date fechaInicio = null;
		List<DatosCatOBJ> listMinistrasFinal = new ArrayList<DatosCatOBJ>();
		List<DatosCatOBJ> listPagaresFinal = new ArrayList<DatosCatOBJ>();
		DatosCatOBJ ministrasObj = null;
		DatosCatOBJ pagaresObj = null;
		DatosCatOBJ pagaresTmp = null;
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		int numeroMinistras = 0;
		int numeroPagares = 0;
		int vi = 0;
		BigDecimal catTotal = BigDecimal.ZERO;	
		Date fechaVen2;


		garantiaLiquidas = vGarLiq + vAhorro;	
		vFactorDias = obtieneFactorDias(periodo);

		if( listMinistras == null ){
			return catTotal;
		}else{
			vi = 0;
			for( DatosCatOBJ obj : listMinistras ){
				calculoAnios = 0.00d;
				if( fechaInicio == null ){
					fechaInicio = new Date();
					fechaInicio = obj.getFecha();
				}
				
				Date nuevoDate= new Date();
				fechaMinistraTexto = sdf.format( obj.getFecha() );
				fechaInicioTexto = sdf.format(fechaInicio);
				fechaMin2double = Double.valueOf(fechaMinistraTexto);
				fechaIni2double = Double.valueOf(fechaInicioTexto);

				calculoAnios = (( fechaMin2double - fechaIni2double ) / periodo) / vFactorDias;
				

				if( calculoAnios == 0.00d ){
					pagaresTmp = new DatosCatOBJ();
					pagaresTmp.setAnios( calculoAnios );
					pagaresTmp.setFecha( listPagares.get(vi).getFecha() );
					pagaresTmp.setMonto( ( vRetenciones + garantiaLiquidas) );
					listPagaresFinal.add(pagaresTmp);
					//listPagaresTmp.add(pagaresObj);
				}

				ministrasObj = new DatosCatOBJ();
				ministrasObj.setAnios(calculoAnios);
				ministrasObj.setFecha(obj.getFecha());
				ministrasObj.setMonto(obj.getMonto());

				listMinistrasFinal.add( ministrasObj );
				vi++;
			}
		}

		numeroMinistras = vi;

		fechaVen2double = fechaIni2double + periodo;
		vi = 0;

		for( DatosCatOBJ obj : listPagares ){

		
			calculoAnios = 0.00d;
			if( periodo <= 180.00d ){
				calculoAnios =  (( fechaVen2double - fechaIni2double ) / periodo) / vFactorDias;

			}else{
				calculoAnios =  ( fechaVen2double - fechaIni2double ) / vFactorDias;
				
			}
			System.out.println(( fechaVen2double));
			

			pagaresObj = new DatosCatOBJ();
			pagaresObj.setAnios(calculoAnios);
			// pagaresObj.setMonto( obj.getMonto()  ); 
			pagaresObj.setMonto( ( obj.getMonto_ven() - obj.getMonto_pag() ) + obj.getInt_int() + obj.getInt_normal() ); 
			pagaresObj.setFecha(obj.getFecha());

			listPagaresFinal.add(pagaresObj);
			
			vi++;
			fechaVen2double += periodo;
		}

		numeroPagares = vi;


		if( garantiaLiquidas > 0.00d ){
			double ultimoPagare = listPagaresFinal.get(numeroPagares).getMonto() - garantiaLiquidas ;

			pagaresObj = new DatosCatOBJ();
			pagaresObj.setAnios( listPagaresFinal.get(numeroPagares).getAnios() );
			pagaresObj.setMonto( ultimoPagare ); 
			pagaresObj.setFecha( listPagaresFinal.get(numeroPagares).getFecha () );
			listPagaresFinal.set(numeroPagares, pagaresObj );
		}

		cat = calculoCAT( numeroMinistras, numeroPagares + 1, listMinistrasFinal, listPagaresFinal );
		
		catTotal = BigDecimal.valueOf(cat).setScale(2, RoundingMode.HALF_UP);
		
		

		return catTotal;
	}


	public static double calculoCAT(int numeroMinistras, int numeroPagares, List<DatosCatOBJ> listMinistras, List<DatosCatOBJ> listPagares ){

		//---- Declaracion Variables locales
		double cat = 0.00;
		int vrep = 0;

		double vincr = 0.1;
		double vdif = 0.00;

		double vtotmin = 0.00; 
		double vtotpag = 0.00;


		do{
			cat = cat + vincr;

			vtotmin = 0.00d;
			for( int vi = 0; vi < numeroMinistras; vi++ ){
				vtotmin = vtotmin + ( listMinistras.get(vi).getMonto() / ( Math.pow( 1 + cat, listMinistras.get(vi).getAnios() ) ) );
			}

			vtotpag = 0.00d;
			for( int vi = 0; vi < numeroPagares; vi++ ){
				vtotpag = vtotpag + ( listPagares.get(vi).getMonto() / ( Math.pow( 1 + cat, listPagares.get(vi).getAnios() ) ) );
			}

			vdif = vtotmin - vtotpag;

			if( vdif > 0.00d ){
				cat = cat - vincr;
				vincr = vincr / 10.00d;
			}

			vrep = vrep + 1;

		}while( vincr != 0.00001d && vrep < 301 ); // vincr != 0.00001d && vrep < 301 

		if( vrep > 300 ){
			cat = -1.00d;
		}

		if( cat > 0.00d ){
			cat = cat * 100.00d;
		}

		return cat;
	}


	/*
	 *  periodo    : 7, 15, 30, 180, 60, 120, 90, 365
	 */

	public static double obtieneFactorDias( double periodo ){
		double factorDias = 0.00d;


		if( periodo == 7.00d ){	   
			factorDias = 52.00d;
		}else if( periodo == 15.00d ){	
			factorDias = 24.00d;
		}else if( periodo == 30.00d ){	
			factorDias = 12.00d;
		}else if( periodo == 60.00d ){	
			factorDias = 6.00d;
		}else if( periodo == 90.00d ){
			factorDias = 4.00d;
		}else if( periodo == 120.00d){
			factorDias = 3.00d;
		}else if( periodo == 180.00d){
			factorDias = 2.00d;
		}else{ factorDias = 365.00d; }

		return factorDias;
	}

}

