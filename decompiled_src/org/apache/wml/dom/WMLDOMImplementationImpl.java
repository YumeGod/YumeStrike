package org.apache.wml.dom;

import org.apache.wml.WMLDOMImplementation;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class WMLDOMImplementationImpl extends DOMImplementationImpl implements WMLDOMImplementation {
   static final DOMImplementationImpl singleton = new WMLDOMImplementationImpl();

   public static DOMImplementation getDOMImplementation() {
      return singleton;
   }

   public Document createDocument(String var1, String var2, DocumentType var3) throws DOMException {
      WMLDocumentImpl var4 = new WMLDocumentImpl(var3);
      Element var5 = var4.createElementNS(var1, var2);
      var4.appendChild(var5);
      return var4;
   }
}
