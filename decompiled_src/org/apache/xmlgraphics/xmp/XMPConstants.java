package org.apache.xmlgraphics.xmp;

import org.apache.xmlgraphics.util.QName;

public interface XMPConstants {
   String XML_NS = "http://www.w3.org/XML/1998/namespace";
   String XMLNS_NAMESPACE = "http://www.w3.org/2000/xmlns/";
   String XMP_NAMESPACE = "adobe:ns:meta/";
   String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
   String DUBLIN_CORE_NAMESPACE = "http://purl.org/dc/elements/1.1/";
   String XMP_BASIC_NAMESPACE = "http://ns.adobe.com/xap/1.0/";
   String ADOBE_PDF_NAMESPACE = "http://ns.adobe.com/pdf/1.3/";
   String PDF_A_IDENTIFICATION = "http://www.aiim.org/pdfa/ns/id/";
   /** @deprecated */
   String PDF_A_IDENTIFICATION_OLD = "http://www.aiim.org/pdfa/ns/id.html";
   String DEFAULT_LANGUAGE = "x-default";
   QName RDF_VALUE = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf", "value");
}
