package net.giro.cxc.fe;
 
 
public interface CFDI {
	public AcuseRecepcion timbrar(Integer facturaId,
			String xml,
			String certificadoPEM,
			String llavePEM,
			String llaveENC,
			String usuarioTimbre,
			String claveTimbre
	);
	
	public AcuseCancelacion cancelar(Integer facturaId,
			String xml,
			byte[] certificadoPEM,
			String llavePEM,
			byte[] llaveENC,
			String usuarioTimbre,
			String claveTimbre
	);
}