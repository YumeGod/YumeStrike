package org.apache.batik.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public class GenericDOMImplementation extends AbstractDOMImplementation {
   protected static final DOMImplementation DOM_IMPLEMENTATION = new GenericDOMImplementation();

   public static DOMImplementation getDOMImplementation() {
      return DOM_IMPLEMENTATION;
   }

   public DocumentType createDocumentType(String var1, String var2, String var3) {
      throw new DOMException((short)9, "Doctype not supported");
   }

   public Document createDocument(String var1, String var2, DocumentType var3) throws DOMException {
      GenericDocument var4 = new GenericDocument(var3, this);
      var4.appendChild(var4.createElementNS(var1, var2));
      return var4;
   }
}
