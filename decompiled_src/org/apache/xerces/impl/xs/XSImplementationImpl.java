package org.apache.xerces.impl.xs;

import org.apache.xerces.dom.CoreDOMImplementationImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSException;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.w3c.dom.DOMImplementation;

public class XSImplementationImpl extends CoreDOMImplementationImpl implements XSImplementation {
   static XSImplementationImpl singleton = new XSImplementationImpl();

   public static DOMImplementation getDOMImplementation() {
      return singleton;
   }

   public boolean hasFeature(String var1, String var2) {
      return var1.equalsIgnoreCase("XS-Loader") && (var2 == null || var2.equals("1.0")) || super.hasFeature(var1, var2);
   }

   public XSLoader createXSLoader(StringList var1) throws XSException {
      XSLoaderImpl var2 = new XSLoaderImpl();
      if (var1 == null) {
         return var2;
      } else {
         for(int var3 = 0; var3 < var1.getLength(); ++var3) {
            if (!var1.item(var3).equals("1.0")) {
               String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1.item(var3)});
               throw new XSException((short)1, var4);
            }
         }

         return var2;
      }
   }

   public StringList getRecognizedVersions() {
      StringListImpl var1 = new StringListImpl(new String[]{"1.0"}, 1);
      return var1;
   }
}
