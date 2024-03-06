package net.giro.cxc.fe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.naming.InitialContext;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import javax.sql.DataSource;

import org.apache.commons.ssl.Base64;
import org.apache.commons.ssl.PKCS8Key;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.logica.FacturaDetalleRem;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import util.Correo;

public class FactElect {
	private static Logger log = Logger.getLogger(FactElect.class.getName());
	private InitialContext ctx = null;
	private DataSource datasource = null;
	private Connection conn;
	//private FacturaRem ifzFacturas;
	private FacturaDetalleRem ifzFacturaDetalles;
	//private PersonaRem ifzPersonas;
	//private ClientesRem ifzClientes;
	HashMap<String, Object> params = new HashMap<String, Object>();
	private boolean facturaActualizada;

	public FactElect() {
		try {
			this.ctx = new InitialContext();
			//this.ifzFacturas = (FacturaRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacturaDetalles = (FacturaDetalleRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			//this.ifzPersonas = (PersonaRem) this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			//this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
		} catch (Exception e) {
			log.error("Ocurrio un problema al instancias FactElect", e);
		}
	}

	private Connection getConn() {
		try {
			if (this.conn == null) {
				this.ctx = new InitialContext();
				this.datasource = ((DataSource)this.ctx.lookup("java:/giroDS"));
				this.conn = this.datasource.getConnection();
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar la coneccion", e);
		}

		return this.conn;
	}

	public String resumenFacturacion(long procesoId, int empresaId) {
		ResultSet rs = null;
		String sql = null;
		String res = "";
		String cuerpo = null;
		Correo correo = null;
		String toCorreo;
		//String toCCCorreo;
		String soporteCorreo = "ameza@condese.com";
		
		try {
			conn = getConn();
			sql ="select  " +
				" case when estatus = 'G' then 'Generada' " +
				"   when estatus='C' then 'Cancelada'" +
				"   when estatus='X' then 'Timbrado Cancelada'" +
				"   when estatus='F' then 'Foliada'" +
				"   when estatus='T' then 'Timbrada'" +
				" else estatus end " +
				"   ,count(*)  " +
				" from fe_facturas " +
				" where id_proceso = " + procesoId +
				" group by estatus";
			rs = this.conn.createStatement().executeQuery(sql);
			if (! rs.next()) {
				res = "No se encontraron datos para el proceso " + procesoId;
				cuerpo ="<html><body>" + res + "</body></html>";
			} else {
				try {
					cuerpo ="<html><body><table><tr><td colspan=\"2\">Resultado del proceso</td></tr>";
					while (rs.next()) {
						cuerpo += "<tr><td>" + rs.getString(1)  + "</td><td>"
								+ rs.getString(2) + "</td></tr>";
					}
					cuerpo += "</table></body></html>";
					res += "\nDatos generados para correo";
				} catch(Exception e) {
					res = "Error al generar correo \n" + e.toString();
					cuerpo ="<html><body>" + res + "</body></html>";
					Util.log(e);
				}
			}
		} catch(Exception e) {
			res += "\nError en proceso de generacion de  correo \n" + e.toString();
			cuerpo = "<html><body>" + res + "</body></html>";
			Util.log(e);
		}

		try {
			correo = new Correo();
			ctx = new InitialContext();
			correo.conexion(ctx);
			rs = this.conn.createStatement().executeQuery(
					"select correo  " +
					" from cat_empresas " +
					" where empresa_id = " + empresaId );
			if ( rs.next() ) {
				res += "\nNo se encontraron las cuentas de correo para la empresa " + empresaId;
				return res;
			}
			toCorreo = rs.getString(1) != null ? rs.getString(1) : soporteCorreo;
			correo.msgsend(toCorreo, "Resumen de Facturacion, Proceso:" + procesoId, cuerpo);
			res += "\nCorreo enviado a " + toCorreo;
		} catch(Exception e ) {
			res += "\nError al crear conexion de correo \n" + e.toString();
			Util.log(e);
		}

		try {
			this.conn.close();
			conn = null;
			correo.desconexion();
		} catch(Exception e) { }

		return res;
	}

	public int cargaCertificados(int certificadoId, String palabra, String certificado64, String llave64, String usuarioTimbre, String claveTimbre) {
		int res =  0;
		String cmd = null;
		Runtime runtime = null;
		Process exec = null;
		
		try {
			String sCertificado = "/tmp/" + certificadoId + ".cer";
			String sLlave = "/tmp/" + certificadoId + ".key";
			String sCertificadoPEM = "/tmp/" + certificadoId + ".cer.pem";
			String sLLavePEM = "/tmp/" + certificadoId + ".key.pem";
			String sLLaveENC = "/tmp/" + certificadoId + ".key.enc";

			byte[] bytesCertificado = Base64.decodeBase64(certificado64);
			FileOutputStream fo = null;
			fo = new FileOutputStream(new File(sCertificado));
			fo.write(bytesCertificado);
			fo.close();

			byte[] bytesLlave = Base64.decodeBase64(llave64);
			fo = new FileOutputStream(new File(sLlave));
			fo.write(bytesLlave);
			fo.close();

			new PKCS8Key(bytesLlave, palabra.toCharArray());

			X509Certificate c = X509Certificate.getInstance(bytesCertificado);

			runtime = Runtime.getRuntime();
			cmd = "openssl pkcs8 -inform DER -in " + sLlave + " -passin pass:" + palabra + " -out "
						+ sLLavePEM;
						log.info(cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();

			cmd = "openssl x509 -inform DER -outform PEM -in " + sCertificado + " -pubkey -out " + sCertificadoPEM;
			log.info(cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();
			cmd = "openssl rsa -in " + sLLavePEM + " -des3 -out " + sLLaveENC
						+ " -passout pass:" + claveTimbre;
			log.info(cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();

			byte[] bytesCertificadoPEM = (new String(readAllBytes(get(sCertificadoPEM)))).getBytes();
			byte[] bytesLlavePEM = (new String(readAllBytes(get(sLLavePEM)))).getBytes();
			byte[] bytesLlaveENC = (new String(readAllBytes(get(sLLaveENC)))).getBytes();

			getConn();

			this.conn.createStatement().execute("update fe_certificados set estatus='D' where empresa_id = (select empresa_id from fe_certificados where certificado_id = " + certificadoId + ")");

			PreparedStatement ps = this.conn.prepareStatement("update fe_certificados set " +
							" llave_data=?, " +
							" estatus='A', " +
							" certificado_data=? ," +
							" asunto=?,  " +
							" del=?,  " +
							" al=?,  " +
							" no_certificado=?,  " +
							" palabra=?, " +
							" certificado_data_pem=?, " +
							" llave_data_pem=?, " +
							" llave_data_enc=?, " +
							" usuario_timbre=?, " +
							" clave_timbre=?  " +
							" where certificado_id = ?");
			ps.setBytes(1, bytesLlave);
			ps.setBytes(2, bytesCertificado);
			ps.setString(3, c.getSubjectDN().getName());
			ps.setDate(4, new Date(c.getNotBefore().getTime()));
			ps.setDate(5, new Date(c.getNotAfter().getTime()));
			ps.setString(6, hex2decimal(c.getSerialNumber().toString(16)));
			ps.setString(7, new String(DESCryptix.encripta(palabra)));
			ps.setBytes(8, bytesCertificadoPEM);
			ps.setBytes(9, bytesLlavePEM);
			ps.setBytes(10, bytesLlaveENC);
			ps.setString(11, new String(DESCryptix.encripta(usuarioTimbre)));
			ps.setString(12, new String(DESCryptix.encripta(claveTimbre)));
			ps.setInt(13, certificadoId);
			ps.execute();
		} catch (FileNotFoundException e) {
			Util.log(e);
			res = -1;
		} catch (SQLException e) {
			Util.log(e);
			res = -2;
		} catch (IOException e) {
			Util.log(e);
			res = -3;
		} catch (GeneralSecurityException e) {
			Util.log(e);
			res = -4;
		} catch (CertificateException e) {
			Util.log(e);
			res = -5;
		} catch (Exception e) {
			Util.log(e);
			res = -6; 
		}

		try {
			this.conn.close(); 
		} catch (Exception e) { }

		return res;
	}

	// ERROR //
	//Los folios se asignan por sucursal
	// retorna 1 o 0 si se pudo o no foliar
	public String foliar(long facturaId, long usuarioId) throws Exception {
		String strQuery = "";
		int empresaId = 0;
		int sucursalId = 0;
		int ifolio = 0;
		String serie = "";
		String res = "Ok";
		
		try {
			getConn();
			
			strQuery = "SELECT f.* "
					+ "    , o.id_cliente "
					+ "    , 'G' as estatus2 "
					+ "    , o.tipo_cliente "
					+ "    , COALESCE(mp.ag, '') AS forma_pago "
					+ "    , CASE f.id_metodo_pago WHEN 10003606 THEN 1 ELSE f.id_metodo_pago END AS forma_pago_id "
					+ "    , COALESCE(fp.forma_pago, '') AS metodo_pago "
					+ "FROM factura f "
					+ "    INNER JOIN obra o ON o.id_obra = f.id_obra "
					+ "    LEFT JOIN formas_pagos fp ON fp.forma_pago_id = f.id_forma_pago "
					+ "    LEFT JOIN de7a4d94446 mp ON mp.aa = f.id_metodo_pago "
					+ "WHERE f.id = " + facturaId;
			//"select f.*, o.id_cliente, 'G' as estatus2, o.tipo_cliente, COALESCE(mp.ag, '') AS metodo_pago from factura f inner join obra o ON o.id_obra = f.id_obra left join de7a4d94446 mp ON mp.aa = f.id_metodo_pago where f.id = " + facturaId;
			ResultSet  rsFactura = this.conn.createStatement().executeQuery(strQuery);
			if (! rsFactura.next())
				return "no existe la factura";
			
			if (! rsFactura.getString("estatus2").equals("G"))
				return "estatus diferente de Generado no puede foliarse";
			
			empresaId = rsFactura.getString("id_empresa") == null ? 0 : rsFactura.getInt("id_empresa");
			sucursalId = rsFactura.getString("id_sucursal") == null ? 0 : rsFactura.getInt("id_sucursal");
			serie = rsFactura.getString("serie") == null ? "" : rsFactura.getString("serie");
			ifolio = rsFactura.getString("folio") == null ? 0 : rsFactura.getInt("folio");
			
			if (empresaId == 0 || sucursalId == 0)
				return "factura sin empresa y/o sucursal asignado no puede sellarse";
			
			if ("".equals(serie) || ifolio <= 0) {
				String secuencia = "";
				ResultSet rsFolios = null;
				ResultSet rs = null;
				Statement stmt = this.conn.createStatement();
				
				// Asignamos serie y folio
				strQuery = "SELECT COALESCE(serie, '') AS serie FROM a535303dbc WHERE aa = " + sucursalId + ";";
				rsFolios = stmt.executeQuery(strQuery);
				if (! rsFolios.next()) {
					serie = "";
					ifolio = 0;
					return "No fue posible asignar Serie y Folio a la factura. No se ha asignado serie a la sucursal.";
				}
				
				if (rsFolios.getString("serie") == null || "".equals(rsFolios.getString("serie"))) {
					serie = "";
					ifolio = 0;
					return "No fue posible asignar Serie y Folio a la factura. No se ha asignado serie a la sucursal.";
				}
				
				serie = rsFolios.getString("serie");
				secuencia = "seq_sucursal_folio_facturacion_" + sucursalId + "_serie_" + serie.trim().toLowerCase();
				
				strQuery = "select 1 from pg_class where relkind = 'S'  and relname = '" + secuencia + "'";
				rs  = stmt.executeQuery(strQuery);
				if (! rs.next()) {
					strQuery = "CREATE SEQUENCE " +  secuencia  + " INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;";
					stmt.execute(strQuery);
				}
				
				strQuery = "SELECT nextval('" + secuencia + " ') AS folio ";
				rs  = stmt.executeQuery(strQuery);
				if (! rs.next())
					return "imposible acceder a la sec de folios " + secuencia;

				ifolio = rs.getInt("folio");
				this.params.put("serie", serie);
				this.params.put("folio", Integer.valueOf(ifolio));
				this.params.put("id_folio", sucursalId);
				
				// Actualizamos el folio asignado a la sucursal
				strQuery = "UPDATE a535303dbc SET folio = " + ifolio + " WHERE aa = " + sucursalId + ";";
				stmt.execute(strQuery);
			}

			strQuery = "SELECT no_certificado "
					+ "    , certificado_data "
					+ "    , llave_data "
					+ "    , palabra "
					+ "    , certificado_data_pem "
					+ "    , llave_data_pem "
					+ "    , llave_data_enc "
					+ "    , usuario_timbre "
					+ "    , clave_timbre "
					+ "FROM empresa_certificado "
					+ "WHERE (estatus IS NULL OR estatus='A') AND id_empresa = " + empresaId;
			ResultSet rsCertificados = this.conn.createStatement().executeQuery(strQuery);
			if (! rsCertificados.next() )
					return "certificado no definido para la empresa";

			String palabra = rsCertificados.getString("palabra"); 				// DESCryptix.desencripta(rsCertificados.getString("palabra").getBytes());
			String usuarioTimbre = rsCertificados.getString("usuario_timbre"); 	// DESCryptix.desencripta(rsCertificados.getString("usuario_timbre").getBytes());
			String claveTimbre = rsCertificados.getString("clave_timbre"); 		// DESCryptix.desencripta(rsCertificados.getString("clave_timbre").getBytes());

			// Generar el xml del comprobante
			DecimalFormat df = new DecimalFormat("#######0.00");
			DecimalFormat cp = new DecimalFormat("00000");
			Namespace xmlns = Namespace.getNamespace("cfdi", "http://www.sat.gob.mx/cfd/3");
			Element root = new Element("Comprobante",xmlns);
			Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			Attribute attSchema = new Attribute("schemaLocation", "http://www.sat.gob.mx/cfd/2" + " http://www.sat.gob.mx/sitio_internet/cfd/2/cfdv2.xsd", xsi);
			
			root.setAttribute(attSchema);
			root.setAttribute(new Attribute("version","3.2"));
			if (rsFactura.getString("serie") != null) 				//if (! rsFolios.getString("serie").trim().equals(""))
				root.setAttribute(new Attribute("serie", rsFactura.getString("serie")));
			root.setAttribute(new Attribute("folio",(ifolio != -1 ? String.valueOf(ifolio) : "")));
			root.setAttribute(new Attribute("fecha",(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date(rsFactura.getTimestamp("fecha_creacion").getTime()) ).replace(' ','T')))); //aaaa-mm-ddThh:mm:ss,
			if(rsFactura.getString("metodo_pago") == null || "".equals(rsFactura.getString("metodo_pago")))
				root.setAttribute(new Attribute("metodoDePago", "METODODEPAGO"));
			else
				root.setAttribute(new Attribute("metodoDePago", rsFactura.getString("metodo_pago")));
			if (rsFactura.getString("forma_pago_id") == null || rsFactura.getString("forma_pago_id").equals("1") )
				root.setAttribute(new Attribute("formaDePago", "EL PAGO DE ESTA FACTURA (CONTRAPRESTACION) SE EFECTUARA EN UNA SOLA EXHIBICION, SI POR ALGUNA RAZON NO FUERA ASI, EMITIREMOS LOS COMPROBANTES DE LAS PARCIALIDADES RESPECTIVAS"));
			else
				root.setAttribute(new Attribute("formaDePago", "EL PAGO DE ESTA FACTURA (CONTRAPRESTACION) SE EFECTUARA EN PARCIALIDADES"));
			root.setAttribute(new Attribute("noCertificado",rsCertificados.getString("no_certificado")));
			root.setAttribute(new Attribute("certificado", new String(Base64.encodeBase64(rsCertificados.getBytes("certificado_data")))));


			if (rsFactura.getFloat("descuento") > 0) {
				root.setAttribute(new Attribute("descuento", df.format(rsFactura.getFloat("descuento"))));
				if (! rsFactura.getString("motivo_descuento").equals(""))
						root.setAttribute(new Attribute("motivoDescuento", rsFactura.getString("motivo_descuento")));
			}
			root.setAttribute(new Attribute("subTotal",  df.format(rsFactura.getFloat("subtotal"))));
			double totalFactura = 0;
			totalFactura = (rsFactura.getDouble("subtotal") + rsFactura.getDouble("impuestos")) - rsFactura.getDouble("retenciones");
			root.setAttribute(new Attribute("total",  df.format(totalFactura)));
			
			this.params.put("nocertificado", rsCertificados.getString("no_certificado"));

			root.setAttribute(new Attribute("tipoDeComprobante", "ingreso"));
			Element emisor = new Element("Emisor",xmlns);

			strQuery = "SELECT emp.rfc AS rfc_emp "
					+ "    , emp.af AS nombre "
					+ "    , emp.ag AS domicilio "
					+ "    , domicilio_empresa(emp.aa, 1) AS direccion "
					+ "    , domicilio_empresa(emp.aa, 2) AS no_externo "
					+ "    , domicilio_empresa(emp.aa, 3) AS no_interno "
					+ "    , COALESCE(col.af, '') AS colonia "
					+ "    , COALESCE(loc.af, '') AS localidad "
					+ "    , COALESCE(mun.af, '') AS municipio "
					+ "    , COALESCE(edo.af, '') AS estado "
					+ "    , 'Mexico' AS pais "
					+ "    , COALESCE(col.ah, '0') AS cp "
					+ "FROM a858c9f4c5 emp "
					+ "    LEFT JOIN aebeC25af3 col ON col.aa = emp.ar "
					+ "    LEFT JOIN a810505fff loc ON loc.aa = col.ag "
					+ "    LEFT JOIN f724d6e190 mun ON mun.aa = loc.ag "
					+ "    LEFT JOIN bc3400dd42 edo ON edo.aa = mun.ag "
					+ "WHERE emp.aa = " + empresaId;
			ResultSet rsEmisor = this.conn.createStatement().executeQuery(strQuery);
			if (! rsEmisor.next())
					return "Empresa emisora no encontrado";

			root.setAttribute(new Attribute("LugarExpedicion", rsEmisor.getString("localidad")));

			emisor.setAttribute(new Attribute("rfc",rsEmisor.getString("rfc_emp")));
			emisor.setAttribute(new Attribute("nombre", rsEmisor.getString("nombre")));
			emisor.addContent(new Element("DomicilioFiscal",xmlns).setAttributes(
				java.util.Arrays.asList(
						new Attribute("calle", rsEmisor.getString("direccion")),
						new Attribute("noExterior", rsEmisor.getString("no_externo") == null ?  "S/N" : rsEmisor.getString("no_externo")),
						new Attribute("noInterior", rsEmisor.getString("no_interno") == null ?  "S/N" : rsEmisor.getString("no_interno")),
						new Attribute("colonia", rsEmisor.getString("colonia")),
						new Attribute("localidad", rsEmisor.getString("localidad")),
						new Attribute("municipio", rsEmisor.getString("municipio")),
						new Attribute("estado", rsEmisor.getString("estado")),
						new Attribute("pais", rsEmisor.getString("pais")),
						new Attribute("codigoPostal",cp.format(rsEmisor.getInt("cp")))
					)
				)
			);

			strQuery = "select sucursal_domicilio(suc.aa, 1) AS domicilio "
					+ "    , sucursal_domicilio(suc.aa, 2) AS no_externo "
				    + "    , sucursal_domicilio(suc.aa, 3) AS no_interno "
				    + "    , COALESCE(col.af, '') AS colonia "
				    + "    , COALESCE(loc.af, '') AS localidad "
				    + "    , COALESCE(mun.af, '') AS municipio "
				    + "    , COALESCE(edo.af, '') AS estado "
				    + "    , 'Mexico' AS pais "
				    + "    , COALESCE(col.ah, '0') AS cp " 
				    + "from a535303dbc suc " 
				    + "    LEFT JOIN aebeC25af3 col ON col.aa = suc.ar "
				    + "    LEFT JOIN a810505fff loc ON loc.aa = col.ag "
				    + "    LEFT JOIN f724d6e190 mun ON mun.aa = loc.ag "
				    + "    LEFT JOIN bc3400dd42 edo ON edo.aa = mun.ag "
				    + "WHERE suc.aa = " + sucursalId;
			ResultSet rsAgente = this.conn.createStatement().executeQuery(strQuery);
			if (! rsAgente.next())
				return "Sucursal emisora no encontrado";
			emisor.addContent(
					new Element("ExpedidoEn",xmlns).setAttributes(
						java.util.Arrays.asList(
							new Attribute("calle", rsAgente.getString("domicilio")),
							new Attribute("noExterior", rsAgente.getString("no_externo") == null ?  "S/N" : rsAgente.getString("no_externo")),
							new Attribute("noInterior", rsAgente.getString("no_interno") == null ?  "S/N" : rsAgente.getString("no_interno")),
							new Attribute("colonia", rsAgente.getString("colonia")),
							new Attribute("localidad", rsAgente.getString("localidad")),
							new Attribute("municipio", rsAgente.getString("municipio")),
							new Attribute("estado", rsAgente.getString("estado")),
							new Attribute("pais", rsAgente.getString("pais")),
							new Attribute("codigoPostal", cp.format(rsAgente.getInt("cp")))
						)
					)
			);
			emisor.addContent(
					new Element("RegimenFiscal",xmlns).setAttributes(
						java.util.Arrays.asList(
							new Attribute("Regimen", "MI REGIMEN")
							)
					)
			);
			root.addContent(emisor);
			
			strQuery = "SELECT id_cliente, rfc, nombre, direccion"
					+ ", domicilio, no_externo, no_interno, colonia, localidad, municipio, estado, pais, cp"
					+ ", tipo, tiene_domicilio, telefono, domicilio_id, colonia_id, principal, estatus"
					+ ", creado_por, fecha_creacion, modificado_por, fecha_modificacion "
					+ "FROM v_persona_negocio "
					+ "WHERE id_cliente = " + rsFactura.getLong("id_cliente") 
					+ " AND tipo = '" + rsFactura.getString("tipo_cliente") + "';";
			ResultSet rsReceptor = this.conn.createStatement().executeQuery(strQuery);
			if (! rsReceptor.next())
				return "Domicilio receptor no encontrado";
			
			Element receptor = new Element("Receptor",xmlns);
			receptor.setAttribute(new Attribute("rfc", rsReceptor.getString("rfc")));
			receptor.setAttribute(new Attribute("nombre", rsReceptor.getString("nombre")));

			if(rsReceptor.getInt("tiene_domicilio") > 0) {
				receptor.addContent(new Element("Domicilio",xmlns).setAttributes(
						java.util.Arrays.asList(
							new Attribute("calle", rsReceptor.getString("domicilio")),
							new Attribute("noExterior", rsReceptor.getString("no_externo")),
							new Attribute("noInterior", rsReceptor.getString("no_interno")),
							new Attribute("colonia", rsReceptor.getString("colonia")),
							new Attribute("localidad", rsReceptor.getString("localidad")),
							new Attribute("municipio", rsReceptor.getString("municipio")),
							new Attribute("estado", rsReceptor.getString("estado")),
							new Attribute("pais", rsReceptor.getString("pais")),
							new Attribute("codigoPostal",cp.format(rsReceptor.getInt("cp")))
						)
					)
				);
				
				this.params.put("domicilio", rsReceptor.getString("direccion"));
				this.params.put("ciudad", rsReceptor.getString("localidad"));
				this.params.put("municipio", rsReceptor.getString("municipio"));
				this.params.put("estado", rsReceptor.getString("estado"));
				this.params.put("pais", rsReceptor.getString("pais"));
				this.params.put("colonia", rsReceptor.getString("colonia"));
				this.params.put("cp", rsReceptor.getString("cp"));

				if (! "".equals(rsReceptor.getString("no_externo")) && ! "SN".equals(rsReceptor.getString("no_externo").toUpperCase()))
					this.params.put("no_externo", rsReceptor.getString("no_externo"));
				
				if (! "".equals(rsReceptor.getString("no_interno")))
					this.params.put("no_interno", rsReceptor.getString("no_interno"));
			}
			root.addContent(receptor);
			
			this.params.put("rfc", rsReceptor.getString("rfc"));
			this.params.put("cliente", rsReceptor.getString("nombre"));
			
			Element conceptos = new Element("Conceptos",xmlns);
			
			List<FacturaDetalleExt> listDetalles = this.ifzFacturaDetalles.findByPropertyPojoCompletoExt("", facturaId);
			if (listDetalles == null || listDetalles.isEmpty())
				return "Factura sin conceptos";
			
			for (FacturaDetalleExt var : listDetalles) {
				Element concepto = new Element("Concepto",xmlns);
				concepto.setAttributes(
						java.util.Arrays.asList(
								new Attribute("cantidad", df.format(var.getCantidad())),
								new Attribute("unidad", "No Aplica"),
								new Attribute("noIdentificacion", var.getIdConcepto().getId().toString()),
								new Attribute("descripcion", var.getIdConcepto().getDescripcion()),
								new Attribute("valorUnitario", df.format(var.getCosto())),
								new Attribute("importe", df.format(var.getImporte()))
				));
				conceptos.addContent(concepto);
			}
			root.addContent(conceptos);
			
			Element impuestos = new Element("Impuestos",xmlns);
			if (rsFactura.getFloat("impuestos") > 0) {
				impuestos.setAttribute("totalImpuestosTrasladados",df.format(rsFactura.getFloat("impuestos")));
				Element traslados = new Element("Traslados",xmlns);

				strQuery = "SELECT (SUM(fd.costo * fd.cantidad) * (fd.porcentaje_iva / 100)) AS total "
						+ "    , fd.porcentaje_iva "
						+ "FROM factura_detalle fd "
						+ "WHERE fd.id_factura = " + facturaId + " "
						+ "GROUP BY fd.porcentaje_iva ";
				//strQuery = " select (sum(fd.costo)*(fd.porcentaje_iva/100)) as total,fd.porcentaje_iva from factura_detalle fd where fd.id_factura=" + facturaId + " group by fd.porcentaje_iva";
				ResultSet rsFacturasDetallesiva = this.conn.createStatement().executeQuery(strQuery);
				
				for( ;rsFacturasDetallesiva.next(); ) {
					if (rsFacturasDetallesiva.getFloat("total") <= 0) continue;
					Element traslado = new Element("Traslado",xmlns);
					traslado.setAttribute("impuesto","IVA");
					traslado.setAttribute("tasa",df.format(rsFacturasDetallesiva.getFloat("porcentaje_iva")));
					traslado.setAttribute("importe", df.format(rsFacturasDetallesiva.getFloat("total")));
					traslados.addContent(traslado);
				}
				
				impuestos.addContent(traslados);
			}
			
			if (rsFactura.getFloat("retenciones") > 0) {
				impuestos.setAttribute("totalImpuestosRetenidos",df.format(rsFactura.getFloat("retenciones")));
				
				Element retenciones = new Element("Retenciones",xmlns);
				if (rsFactura.getFloat("retension_iva") > 0) {
					Element retencion = new Element("Retencion",xmlns);
					retencion.setAttribute("impuesto","IVA");
					retencion.setAttribute("importe",df.format(rsFactura.getFloat("retension_iva")));
					retenciones.addContent(retencion);
				}
				
				if (rsFactura.getFloat("retension_isr") > 0) {
					Element retencion = new Element("Retencion",xmlns);
					retencion.setAttribute("impuesto","ISR");
					retencion.setAttribute("importe",df.format(rsFactura.getFloat("retension_isr")));
					retenciones.addContent(retencion);
				}
				
				impuestos.addContent(retenciones);
			}
			root.addContent(impuestos);
			
			Document document = new Document(root);
			GeneradorCFD cfd = new GeneradorCFD(document, rsCertificados.getBinaryStream("llave_data"), palabra, "3.2");
			
			// agregar el sello al documento y escribir el XML incluido el sello al directorio /tmp/
			document.getRootElement().setAttribute(new Attribute("sello", new String(cfd.getSelloDigital())));
			FileWriter fwxml = new FileWriter(new File("/tmp/"+ facturaId  + ".xml"));
			StringWriter sw = new StringWriter();
			XMLOutputter outputter = new XMLOutputter();
			outputter.output(document, fwxml);
			outputter.output(document, sw);
			sw.close();
			fwxml.close();
			
			this.params.put("xml", cfd.getXML().getBytes());
			this.params.put("cadena_original", cfd.getCadenaOriginal());
			this.params.put("sello", cfd.getSelloDigital());
			
			// escribir un archivo con la cadena original utilizada para generar el sello
			FileWriter fwco = new FileWriter(new File("/tmp/"+ facturaId  + ".txt"));
			fwco.write(cfd.getCadenaOriginal());
			fwco.close();


			CFDI cfdi = new CFDIRealVirtual();
			/*AcuseRecepcion acuse = cfdi.timbrar(facturaId,
				sw.toString(),
				rsCertificados.getString("certificado_data_pem"),
				rsCertificados.getString("llave_data_pem"),
				rsCertificados.getString("llave_data_enc"),
				usuarioTimbre,
				claveTimbre
			);*/
			AcuseRecepcion acuse = cfdi.timbrar(sw.toString(), usuarioTimbre, claveTimbre);

			this.params.put("state", acuse.getState());
			this.params.put("descripcion", acuse.getDescripcion());
			
			if (acuse.getState() == 0) {
				res = "";
				this.params.put("cfdi", acuse.getXml()); //this.params.put("cfdi", acuse.getXml().getBytes()); 
				this.params.put("timbre", acuse.getTimbre());
				this.params.put("fecha_timbrado", acuse.getFechaTimbrado().replace("T", " "));
				this.params.put("uuid", acuse.getUuid());
				this.params.put("sello_sat", acuse.getSelloSat());
				this.params.put("no_certificado_sat", acuse.getNoCertificadoSat());
			} else {
				res = acuse.getDescripcion();
			}
			
		} catch(SQLException e) {
			Util.log(e);
			res = e.toString();
		} catch (Exception e) {
			Util.log(e);
			res = e.toString();
		} finally {
			// Actualizamos datos de factura
			this.facturaActualizada = actualizamosFactura(facturaId, usuarioId);
		}

		try { 
			this.conn.close(); 
		} catch(Exception e) {
			Util.log(e);
		}

		return  res;
	}
	
	public boolean facturaActualizada() {
		return this.facturaActualizada;
	}
	
	public HashMap<String, Object> getData() {
		return this.params;
	}
	
	private boolean actualizamosFactura(long idFactura, long idUsuario) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String strQuery = "";
		
		try {
			for (Entry<String, Object> item : this.params.entrySet()) {
				if (item == null || item.getValue() == null || item.getValue().getClass() == null) continue;
				if (! "".equals(strQuery)) strQuery += ",";
				
				if (java.util.Date.class == item.getValue().getClass()) 
					strQuery += item.getKey() + " = date('" + formateador.format((Date) item.getValue()) + "')";
				else if (java.lang.Integer.class == item.getValue().getClass()) 
					strQuery += item.getKey() + " = " + item.getValue().toString();
				else if (java.lang.Long.class == item.getValue().getClass()) 
					strQuery += item.getKey() + " = " + item.getValue().toString();
				else if (java.lang.Double.class == item.getValue().getClass()) 
					strQuery += item.getKey() + " = " + item.getValue().toString();
				else if (java.math.BigDecimal.class == item.getValue().getClass()) 
					strQuery += item.getKey() + " = " + ((BigDecimal) item.getValue()).toString();
				else
					strQuery += item.getKey() + " = '" + item.getValue().toString() + "'";
			}
			
			if ("".equals(strQuery))
				return false;
			
			strQuery = "UPDATE factura SET " + strQuery + ", fecha_modificacion = NOW(), modificado_por = " + idUsuario + " WHERE id = $factura;";
			strQuery = strQuery.replace("$factura", String.valueOf(idFactura));
			
			this.conn.createStatement().execute(strQuery);
			return true;
		} catch (Exception e) {
			System.out.println("----------------------------------------------------------------------------------------------------------");
			System.out.println("Error al actualizar datos en factura. Logica_CuentasPorCobrar.FactElect.actualizamosFactura(int idFactura)");
			e.printStackTrace();
			System.out.println("----------------------------------------------------------------------------------------------------------");
			return false;
		}
	}
	
	public String cancelar(long facturaId, long usuarioId) {
		String res = "Ok";

		try{
			getConn();
			ResultSet  rsFactura = this.conn.createStatement().executeQuery("select * from fe_facturas where factura_id = " + facturaId);
			if (! rsFactura.next())
				return "no existe la factura";
			int empresaId = rsFactura.getString("empresa_id") == null ?  0 : rsFactura.getInt("empresa_id");
			int agenteId = rsFactura.getString("agente_id") == null ?  0 : rsFactura.getInt("agente_id");
			if (empresaId == 0 || agenteId == 0)
				return "factura sin empresa y/o sucursal asignado no puede sellarse";

			ResultSet rsCertificados = this.conn.createStatement().executeQuery(
					"select no_certificado," +
					" certificado_data, llave_data," +
					" certificado_data_pem," +
					" llave_data_pem," +
					" llave_data_enc," +
					" usuario_timbre," +
					" clave_timbre " +
					"from fe_certificados " +
					"where estatus='A' and empresa_id = " + empresaId);
			if (! rsCertificados.next() )
					return "certificado no definido para la empresa";
			String usuarioTimbre = DESCryptix.desencripta(rsCertificados.getString("usuario_timbre").getBytes());
			String claveTimbre = DESCryptix.desencripta(rsCertificados.getString("clave_timbre").getBytes());
			String cfdiXml =  rsFactura.getString("cfdi");
			CFDI cfdi = new CFDIRealVirtual();

			/*AcuseCancelacion acuse = cfdi.cancelar(facturaId,
				cfdiXml,
				rsCertificados.getBytes("certificado_data_pem"),
				rsCertificados.getString("llave_data_pem"),
				rsCertificados.getBytes("llave_data_enc"),
				usuarioTimbre,
				claveTimbre
			);*/
			AcuseCancelacion acuse = cfdi.cancelar(cfdiXml, usuarioTimbre, claveTimbre);
			if (acuse.getState() == 0) {
				res = "Ok";
				String acuseCadena = "";
				if(acuse.getAcuse() != null)
					acuseCadena = acuse.getAcuse();
				PreparedStatement ps = this.conn.prepareStatement("update fe_facturas set estatus='X', " +
						"fecha_modificacion=current_timestamp(0), fecha_cancelacion=current_timestamp(0),"
						+ "modificado_por = ?, acuse_cancelacion = ? , estado_cancelacion = ? , " +
						" mensaje_cancelacion = ?  where factura_id = ? ");
				ps.setLong(1, usuarioId);
				ps.setBytes(2, acuseCadena.getBytes());
				ps.setLong(3, acuse.getState());
				ps.setString(4, acuse.getDescripcion());
				ps.setLong(5, facturaId);
				ps.execute();
			} else {
				res = acuse.getDescripcion();
			}
		} catch(SQLException e) {
			Util.log(e);
			res = e.toString();
		} catch (Exception e) {
			Util.log(e);
			res = e.toString();
		}
	
		try { 
			this.conn.close(); 
		} catch(Exception e) {}

		return  res;
	}

	public static String hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		int val = 0;
		String res = "";
		
		s = s.toUpperCase();

		for (int i = 0; i < s.length(); ) {
			val = 0;
			char c = s.charAt(i++);
			int d = digits.indexOf(c);
			val = 16 * d;
			c = s.charAt(i++);
			d = digits.indexOf(c);
			val += d;
			res = res + String.valueOf((char)val);
		}
		
		return res;
	}
}

// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA    |        AUTOR       | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//    1.0  | 2016-09-21  | Javier Tirado      | Se agrega la columna "tipo_cliente" en la consulta de la factura.
//         |             |                    | Se agrega la ejecucion de la vista "v_persona_negocio" para datos de clientes.
//    1.0  | 2016-10-19  | Javier Tirado      | Modifico el metodo foliar. Tome la serie y folio de la sucursal si no los tiene asignados 