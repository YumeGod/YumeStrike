package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class DOMImplementationImpl extends CoreDOMImplementationImpl implements DOMImplementation {
   static DOMImplementationImpl singleton = new DOMImplementationImpl();

   public static DOMImplementation getDOMImplementation() {
      return singleton;
   }

   public boolean hasFeature(String var1, String var2) {
      boolean var3 = super.hasFeature(var1, var2);
      if (var3) {
         return var3;
      } else {
         boolean var4 = var2 == null || var2.length() == 0;
         if (var1.startsWith("+")) {
            var1 = var1.substring(1);
         }

         return var1.equalsIgnoreCase("Events") && (var4 || var2.equals("2.0")) || var1.equalsIgnoreCase("MutationEvents") && (var4 || var2.equals("2.0")) || var1.equalsIgnoreCase("Traversal") && (var4 || var2.equals("2.0")) || var1.equalsIgnoreCase("Range") && (var4 || var2.equals("2.0")) || var1.equalsIgnoreCase("MutationEvents") && (var4 || var2.equals("2.0"));
      }
   }

   public Document createDocument(String var1, String var2, DocumentType var3) throws DOMException {
      if (var1 == null && var2 == null && var3 == null) {
         return new DocumentImpl();
      } else if (var3 != null && var3.getOwnerDocument() != null) {
         String var6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null);
         throw new DOMException((short)4, var6);
      } else {
         DocumentImpl var4 = new DocumentImpl(var3);
         Element var5 = var4.createElementNS(var1, var2);
         var4.appendChild(var5);
         return var4;
      }
   }
}
