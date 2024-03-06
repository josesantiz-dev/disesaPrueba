
package org.tempuri.realvirtual.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.tempuri package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AuthSoapHd_QNAME = new QName("http://tempuri.org/", "AuthSoapHd");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.tempuri
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RetryCfdisResponse }
     * 
     */
    public RetryCfdisResponse createRetryCfdisResponse() {
        return new RetryCfdisResponse();
    }

    /**
     * Create an instance of {@link PpoCfdTimbreCnResponse }
     * 
     */
    public PpoCfdTimbreCnResponse createPpoCfdTimbreCnResponse() {
        return new PpoCfdTimbreCnResponse();
    }

    /**
     * Create an instance of {@link ClienteUsuarioTimbre }
     * 
     */
    public ClienteUsuarioTimbre createClienteUsuarioTimbre() {
        return new ClienteUsuarioTimbre();
    }

    /**
     * Create an instance of {@link GetStatusSat }
     * 
     */
    public GetStatusSat createGetStatusSat() {
        return new GetStatusSat();
    }

    /**
     * Create an instance of {@link GetTicketSimpleResponse }
     * 
     */
    public GetTicketSimpleResponse createGetTicketSimpleResponse() {
        return new GetTicketSimpleResponse();
    }

    /**
     * Create an instance of {@link StructCfd }
     * 
     */
    public StructCfd createStructCfd() {
        return new StructCfd();
    }

    /**
     * Create an instance of {@link SchemaCfd }
     * 
     */
    public SchemaCfd createSchemaCfd() {
        return new SchemaCfd();
    }

    /**
     * Create an instance of {@link CancelTicketResponse }
     * 
     */
    public CancelTicketResponse createCancelTicketResponse() {
        return new CancelTicketResponse();
    }

    /**
     * Create an instance of {@link StructCancel }
     * 
     */
    public StructCancel createStructCancel() {
        return new StructCancel();
    }

    /**
     * Create an instance of {@link TestCfd }
     * 
     */
    public TestCfd createTestCfd() {
        return new TestCfd();
    }

    /**
     * Create an instance of {@link CancelTicketExtended }
     * 
     */
    public CancelTicketExtended createCancelTicketExtended() {
        return new CancelTicketExtended();
    }

    /**
     * Create an instance of {@link GetAcuse }
     * 
     */
    public GetAcuse createGetAcuse() {
        return new GetAcuse();
    }

    /**
     * Create an instance of {@link GetAcuseResponse }
     * 
     */
    public GetAcuseResponse createGetAcuseResponse() {
        return new GetAcuseResponse();
    }

    /**
     * Create an instance of {@link StructAcuse }
     * 
     */
    public StructAcuse createStructAcuse() {
        return new StructAcuse();
    }

    /**
     * Create an instance of {@link GetTicketSimple }
     * 
     */
    public GetTicketSimple createGetTicketSimple() {
        return new GetTicketSimple();
    }

    /**
     * Create an instance of {@link PpoCfdTimbreCn }
     * 
     */
    public PpoCfdTimbreCn createPpoCfdTimbreCn() {
        return new PpoCfdTimbreCn();
    }

    /**
     * Create an instance of {@link CancelTicket }
     * 
     */
    public CancelTicket createCancelTicket() {
        return new CancelTicket();
    }

    /**
     * Create an instance of {@link CancelTicketExtendedResponse }
     * 
     */
    public CancelTicketExtendedResponse createCancelTicketExtendedResponse() {
        return new CancelTicketExtendedResponse();
    }

    /**
     * Create an instance of {@link StructCancelExtended }
     * 
     */
    public StructCancelExtended createStructCancelExtended() {
        return new StructCancelExtended();
    }

    /**
     * Create an instance of {@link TestCfdResponse }
     * 
     */
    public TestCfdResponse createTestCfdResponse() {
        return new TestCfdResponse();
    }

    /**
     * Create an instance of {@link GetStatusSatResponse }
     * 
     */
    public GetStatusSatResponse createGetStatusSatResponse() {
        return new GetStatusSatResponse();
    }

    /**
     * Create an instance of {@link GetTicket }
     * 
     */
    public GetTicket createGetTicket() {
        return new GetTicket();
    }

    /**
     * Create an instance of {@link GetTicketResponse }
     * 
     */
    public GetTicketResponse createGetTicketResponse() {
        return new GetTicketResponse();
    }

    /**
     * Create an instance of {@link AuthSoapHd }
     * 
     */
    public AuthSoapHd createAuthSoapHd() {
        return new AuthSoapHd();
    }

    /**
     * Create an instance of {@link SchemaCfdResponse }
     * 
     */
    public SchemaCfdResponse createSchemaCfdResponse() {
        return new SchemaCfdResponse();
    }

    /**
     * Create an instance of {@link RetryCfdis }
     * 
     */
    public RetryCfdis createRetryCfdis() {
        return new RetryCfdis();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthSoapHd }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "AuthSoapHd")
    public JAXBElement<AuthSoapHd> createAuthSoapHd(AuthSoapHd value) {
        return new JAXBElement<AuthSoapHd>(_AuthSoapHd_QNAME, AuthSoapHd.class, null, value);
    }

}
