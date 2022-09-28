package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class PSVIDOMImplementationImpl extends CoreDOMImplementationImpl {
   static PSVIDOMImplementationImpl singleton = new PSVIDOMImplementationImpl();

   public static DOMImplementation getDOMImplementation() {
      return singleton;
   }

   public boolean hasFeature(String var1, String var2) {
      return super.hasFeature(var1, var2) || var1.equalsIgnoreCase("psvi");
   }

   public Document createDocument(String var1, String var2, DocumentType var3) throws DOMException {
      if (var3 != null && var3.getOwnerDocument() != null) {
         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "WRONG_DOCUMENT_ERR", (Object[])null));
      } else {
         PSVIDocumentImpl var4 = new PSVIDocumentImpl(var3);
         Element var5 = var4.createElementNS(var1, var2);
         var4.appendChild(var5);
         return var4;
      }
   }
}
