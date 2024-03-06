package net.izel.framework.util;
import java.util.Date;


 public class DatosCatOBJ 
{
	public Date fecha;
	public double anios;
	public double monto;
	
	// Datos para pagare
	
	public double monto_pag;
	public double monto_ven;
	public double int_int;
	public double int_normal;
	
	public DatosCatOBJ(){
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public double getAnios() {
		return anios;
	}

	public void setAnios(double anios) {
		this.anios = anios;
	}

	public double getMonto_pag() {
		return monto_pag;
	}

	public void setMonto_pag(double monto_pag) {
		this.monto_pag = monto_pag;
	}

	public double getInt_int() {
		return int_int;
	}

	public void setInt_int(double int_int) {
		this.int_int = int_int;
	}

	public double getInt_normal() {
		return int_normal;
	}

	public void setInt_normal(double int_normal) {
		this.int_normal = int_normal;
	}

	public double getMonto_ven() {
		return monto_ven;
	}

	public void setMonto_ven(double monto_ven) {
		this.monto_ven = monto_ven;
	}
	
}
