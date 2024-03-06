@XmlSchema(
		namespace="http://www.sat.gob.mx/cfd/3",
		elementFormDefault=XmlNsForm.QUALIFIED,
		xmlns={
				@XmlNs(namespaceURI = "http://www.sat.gob.mx/cfd/3", prefix = "cfdi"),
				@XmlNs(namespaceURI = "http://www.w3.org/2001/XMLSchema-instance",  prefix = "xsi"),
				@XmlNs(namespaceURI = "http://www.sat.gob.mx/Pagos", prefix = "pago10"),
				@XmlNs(namespaceURI = "http://www.sat.gob.mx/TimbreFiscalDigital", prefix="tfd")})
package net.giro.cxc.fe.documentos;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;