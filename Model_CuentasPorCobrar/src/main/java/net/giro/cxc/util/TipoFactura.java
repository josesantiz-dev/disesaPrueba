package net.giro.cxc.util;

public enum TipoFactura {
	Contado { public String toString() { return "X"; }},
	Credito { public String toString() { return "C"; }}
}